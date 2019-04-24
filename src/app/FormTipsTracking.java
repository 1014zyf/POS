package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormConfirmBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormListMessageBox;
import core.Controller;
import externallib.StringLib;
import om.PosActionLog;
import om.PosActionPrintQueue;
import om.PosBusinessDay;
import om.PosCheck;
import om.PosTipsTrackTransactions;
import om.PosTipsTrackTransactionsList;
import om.OutOutlet;
import om.UserUser;
import om.UserUserList;

import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FormTipsTrackingListener {
	UserUser formTipsTracking_inputEmployee();
	OutOutlet formTipsTracking_selectOutlet();
}

public class FormTipsTracking extends VirtualUIForm implements FrameTipsTrackingListener {

	TemplateBuilder m_oTemplateBuilder;

	private FrameTipsTracking m_oFrameTipsTracking;
	VirtualUIFrame m_oFrameCover;

	// Lists of ALL ToEmployee tips tracking records from database
	private TreeMap<Integer, Integer> m_oSeqEmployeeIdPairs;					// Index of display sequence, ToEmployeeId pairs
	private TreeMap<Integer, UserUser> m_oEmployeeIdUserPairs;					// EmployeeId, UserUser pairs
	private TreeMap<Integer, BigDecimal[]> m_oToEmployeeIdValuesPairs;			// ToEmployeeId, TipsOut and SCOut pairs
	private TreeMap<Integer, BigDecimal[]> m_oFromEmployeeIdValuesPairs;		// FromEmployeeId, TipsOut and SCOut pairs

	// Lists of records for operation on the current page only
	private TreeMap<Integer, Integer> m_oCurrentPageSeqIdPairs;					// Current page display sequence, ToEmployeeId pairs
	private List<Integer> m_oSelectedEmployeeIds;								// Store selected ToEmployeeIds
	private List<Integer> m_oNewEmployeeIds;									// Store newly added ToEmployeeIds
	private List<Integer> m_oDeletedEmployeeIds;								// Store deleted ToEmployeeIds
	private TreeMap<Integer, BigDecimal[]> m_oEmployeeIdModifiedValuesPairs;	// Store modified values of ToEmployees

	private BigDecimal m_dTipsIn;
	private BigDecimal m_dSCIn;
	private BigDecimal m_dDirectTipsIn;
	private BigDecimal m_dTipsFromOthers;
	private BigDecimal m_dSCFromOthers;
	private BigDecimal m_dDirectTipsFromOthers;
	private BigDecimal m_dTipsBalance;
	private BigDecimal m_dSCBalance;
	private BigDecimal m_dDirectTipsBalance;

	private Integer m_iCurrentEditEmployeeId;				// Store the current editing ToEmployeeId
	private BigDecimal m_dCurrentEditOriValue;				// Store the original value before modifying the cell
	private UserUser m_oCurrentEmployee;					// FromEmployee
	private OutOutlet m_oCurrentOutlet;						// Current outlet
	private PosBusinessDay m_oCurrentBday;					// Current business day
	private boolean m_bOwnedTipsMode;

	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	private int m_iCurrentPage;
	private int m_iTotalPage;

	private PosTipsTrackTransactionsList m_oPosTipsTrackTransactionsList;

	private final static int MAX_ITEM_PER_PAGE = 8;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormTipsTrackingListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormTipsTrackingListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormTipsTrackingListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FormTipsTracking(Controller oParentController) {
		super(oParentController);
		listeners = new ArrayList<FormTipsTrackingListener>();

		m_oSeqEmployeeIdPairs = new TreeMap<Integer, Integer>();
		m_oEmployeeIdUserPairs = new TreeMap<Integer, UserUser>();
		m_oToEmployeeIdValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		m_oFromEmployeeIdValuesPairs = new TreeMap<Integer, BigDecimal[]>();
		m_oCurrentPageSeqIdPairs = new TreeMap<Integer, Integer>();
		m_oSelectedEmployeeIds = new ArrayList<Integer>();
		m_oNewEmployeeIds = new ArrayList<Integer>();
		m_oDeletedEmployeeIds = new ArrayList<Integer>();
		m_oEmployeeIdModifiedValuesPairs = new TreeMap<Integer, BigDecimal[]>();

		m_dTipsIn = BigDecimal.ZERO;
		m_dSCIn = BigDecimal.ZERO;
		m_dDirectTipsIn = BigDecimal.ZERO;
		m_dTipsFromOthers = BigDecimal.ZERO;
		m_dSCFromOthers = BigDecimal.ZERO;
		m_dDirectTipsFromOthers = BigDecimal.ZERO;
		m_dTipsBalance = BigDecimal.ZERO;
		m_dSCBalance = BigDecimal.ZERO;
		m_dDirectTipsBalance = BigDecimal.ZERO;

		m_iCurrentEditEmployeeId = Integer.valueOf(-1);
		m_dCurrentEditOriValue = BigDecimal.ZERO;
		m_oCurrentOutlet = AppGlobal.g_oFuncOutlet.get().getOutlet();
		m_oCurrentBday = AppGlobal.g_oFuncOutlet.get().getBusinessDay();

		m_iCurrentPage = 0;
		m_iTotalPage = 0;
	}

	public void init(UserUser oEmployee, boolean bOwnedTipsOut) {
		m_oTemplateBuilder = new TemplateBuilder();

		m_oTemplateBuilder.loadTemplate("frmTipsTracking.xml");

		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		m_oFrameCover.setVisible(false);
		this.attachChild(m_oFrameCover);

		m_oFrameTipsTracking = new FrameTipsTracking();
		m_oTemplateBuilder.buildFrame(m_oFrameTipsTracking, "fraTipsTracking");
		m_oFrameTipsTracking.addListener(this);
		this.attachChild(m_oFrameTipsTracking);

		m_oCurrentEmployee = oEmployee;
		m_bOwnedTipsMode = bOwnedTipsOut;
		loadTipsTrackingRecord();
	}

	private void loadTipsTrackingRecord() {
		// Initialize the TreeMaps
		createToEmployeeIdPairs();

		m_iTotalPage = calculateTotalPageOfPairs();

		if (m_iTotalPage >= 1)
			m_iCurrentPage = 1;
		else
			m_iCurrentPage = 0;

		loadTipsTrackTransactionsAtPage(m_iCurrentPage);
		updatePageButtonsVisibility();
	}

	private void loadTipsTrackTransactions(){
		TreeMap<Integer, BigDecimal[]> oResultToOthers = new TreeMap<Integer, BigDecimal[]>();
		TreeMap<Integer, BigDecimal[]> oResultToUser = new TreeMap<Integer, BigDecimal[]>();
		PosTipsTrackTransactionsList oPosTipsTrackTransactionsList = new PosTipsTrackTransactionsList();
		oPosTipsTrackTransactionsList.readByTypeBdayUserShopOlet(m_oCurrentBday.getBdayId(), m_oCurrentEmployee.getUserId(), m_oCurrentOutlet.getShopId(), m_oCurrentOutlet.getOletId());
		for (Entry<Integer, PosTipsTrackTransactions> entry : oPosTipsTrackTransactionsList.getPosTipsTrackTransactionsList().entrySet()) {
			if (entry.getValue().getToUserId() != m_oCurrentEmployee.getUserId())
				constructIdAmoutArrayPair(oResultToOthers, entry, entry.getValue().getToUserId());
			else if (entry.getValue().getFromUserId() != m_oCurrentEmployee.getUserId())
				constructIdAmoutArrayPair(oResultToUser, entry, entry.getValue().getFromUserId());
		}
		m_dTipsIn = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPosTipsTrackTransactionsList.getTipsInFromChecks());
		m_dSCIn = AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(oPosTipsTrackTransactionsList.getServiceChargeInFromChecks());
		m_dDirectTipsIn = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPosTipsTrackTransactionsList.getDirectTipsIn());
		m_dTipsFromOthers = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPosTipsTrackTransactionsList.getTipsTotalFromOthers());
		m_dSCFromOthers = AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(oPosTipsTrackTransactionsList.getServiceChargeTotalFromOthers());
		m_dDirectTipsFromOthers = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(oPosTipsTrackTransactionsList.getDirectTipsTotalFromOthers());
		m_oToEmployeeIdValuesPairs = oResultToOthers;
		m_oFromEmployeeIdValuesPairs = oResultToUser;
	}

	private TreeMap<Integer, BigDecimal[]> constructIdAmoutArrayPair(TreeMap<Integer, BigDecimal[]> oResult, Entry<Integer, PosTipsTrackTransactions> oEntry, Integer iKeyUserId) {
		if (oResult.get(iKeyUserId) == null) {
			// create record
			BigDecimal[] dAmountArray = new BigDecimal[3];
			if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_TIPS) == 0) {
				dAmountArray[0] = oEntry.getValue().getAmount();
				dAmountArray[1] = BigDecimal.ZERO;
				dAmountArray[2] = BigDecimal.ZERO;
			} else if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_SERVICE_CHARGE) == 0) {
				dAmountArray[0] = BigDecimal.ZERO;
				dAmountArray[1] = oEntry.getValue().getAmount();
				dAmountArray[2] = BigDecimal.ZERO;
			} else if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_DIRECT_TIPS) == 0) {
				dAmountArray[0] = BigDecimal.ZERO;
				dAmountArray[1] = BigDecimal.ZERO;
				dAmountArray[2] = oEntry.getValue().getAmount();
			}
			oResult.put(iKeyUserId, dAmountArray);
		} else {
			// edit from old records
			BigDecimal dAmount = oEntry.getValue().getAmount();
			if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_TIPS) == 0)
				oResult.get(iKeyUserId)[0] = dAmount;
			else if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_SERVICE_CHARGE) == 0)
				oResult.get(iKeyUserId)[1] = dAmount;
			else if (oEntry.getValue().getType().compareTo(PosTipsTrackTransactions.TYPE_DIRECT_TIPS) == 0)
				oResult.get(iKeyUserId)[2] = dAmount;
		}
		return oResult;
	}

	// For initialising m_oSeqEmployeeIdPairs, m_oEmployeeIdUserPairs, m_oToEmployeeIdValuesPairs and m_oFromEmployeeIdValuesPairs
	private void createToEmployeeIdPairs() {
		HashMap<Integer, Integer> oEmployeeIds = new HashMap<Integer, Integer>();		// Id, EmployeeId pairs
		m_oSeqEmployeeIdPairs.clear();
		m_oEmployeeIdUserPairs.clear();
		m_oToEmployeeIdValuesPairs.clear();
		m_oFromEmployeeIdValuesPairs.clear();

		// Retrieve all tips transaction values from database by using bday, shopId, outletId, fromEmployeeId
		// Firstly initialise m_oToEmployeeIdValuesPairs and m_oFromEmployeeIdValuesPairs only
		loadTipsTrackTransactions();

		// Then put IDs to m_oSeqEmployeeIdPairs and m_oEmployeeIdUserPairs by using the IDs
		// from m_oToEmployeeIdValuesPairs and m_oFromEmployeeIdValuesPairs
		int iTempIndex = 0;
		for (Integer iToEmployeeId : m_oToEmployeeIdValuesPairs.keySet()) {
			m_oSeqEmployeeIdPairs.put(iTempIndex++, iToEmployeeId);
			oEmployeeIds.put(iToEmployeeId, iToEmployeeId);
		}
		for (Integer iFromEmployeeId : m_oFromEmployeeIdValuesPairs.keySet())
			oEmployeeIds.put(iFromEmployeeId, iFromEmployeeId);

		int iIndex = 0;
		if (!oEmployeeIds.isEmpty()) {
			// Load user information
			UserUserList oUserList = new UserUserList();
			oUserList.readUserByIdList(oEmployeeIds);
			HashMap<Integer, UserUser> oUserIdUser = oUserList.getUserList();		// UserId, UserUser pairs
			m_oEmployeeIdUserPairs.putAll(oUserIdUser);

			// Filter out the ToEmployeeIds that does not exist in oUserUserList.getUserList()
			TreeMap<Integer, Integer> oFilteredSeqEmployeeIdPairs = new TreeMap<Integer, Integer>();
			for (Entry<Integer, Integer> entry : m_oSeqEmployeeIdPairs.entrySet())
				if (oUserIdUser.containsKey(entry.getValue()))
					oFilteredSeqEmployeeIdPairs.put(iIndex++, entry.getValue());
			m_oSeqEmployeeIdPairs = oFilteredSeqEmployeeIdPairs;

			// Filter out the FromEmployeeIds that does not exist in oUserUserList.getUserList()
			for (Integer iFromEmployeeId : m_oFromEmployeeIdValuesPairs.keySet())
				if (!oUserIdUser.containsKey(iFromEmployeeId))
					m_oFromEmployeeIdValuesPairs.remove(iFromEmployeeId);
		}
	}

	private int calculateTotalPageOfPairs() {
		if (m_oSeqEmployeeIdPairs.size() <= 0)
			return 0;
		int iTotalPage = 0;
		iTotalPage = m_oSeqEmployeeIdPairs.size() / MAX_ITEM_PER_PAGE;
		if (m_oSeqEmployeeIdPairs.size() % MAX_ITEM_PER_PAGE > 0)
			iTotalPage++;
		return iTotalPage;
	}

	private void loadTipsTrackTransactionsAtPage(int iPageNumber) {
		// Clear memory for current page
		m_oCurrentPageSeqIdPairs.clear();
		m_oSelectedEmployeeIds.clear();
		m_oNewEmployeeIds.clear();
		m_oDeletedEmployeeIds.clear();
		m_oEmployeeIdModifiedValuesPairs.clear();
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_iCurrentEditEmployeeId = Integer.valueOf(-1);
		m_dCurrentEditOriValue = BigDecimal.ZERO;
		m_oFrameTipsTracking.setDeleteButtonEnabled(false);
		m_oFrameTipsTracking.setSaveButtonEnabled(false);

		// Calculate the tips and SC balance
		BigDecimal dTotalTipsOut = BigDecimal.ZERO;
		BigDecimal dTotalSCOut = BigDecimal.ZERO;
		BigDecimal dTotalDirectTipsOut = BigDecimal.ZERO;
		for (Entry<Integer, Integer> entry : m_oSeqEmployeeIdPairs.entrySet()) {
			dTotalTipsOut = dTotalTipsOut.add(m_oToEmployeeIdValuesPairs.get(entry.getValue())[0]);
			dTotalSCOut = dTotalSCOut.add(m_oToEmployeeIdValuesPairs.get(entry.getValue())[1]);
			dTotalDirectTipsOut = dTotalDirectTipsOut.add(m_oToEmployeeIdValuesPairs.get(entry.getValue())[2]);
		}

		m_dTipsBalance = m_dTipsIn.add(m_dTipsFromOthers).subtract(dTotalTipsOut);
		m_dSCBalance = m_dSCIn.add(m_dSCFromOthers).subtract(dTotalSCOut);
		m_dDirectTipsBalance = m_dDirectTipsIn.add(m_dDirectTipsFromOthers).subtract(dTotalDirectTipsOut);

		// Put display sequence, employee Id in m_oCurrentPageSeqIdPairs
		if (iPageNumber >= 1) {
			int iStartIndex = (iPageNumber - 1) * MAX_ITEM_PER_PAGE;
			for (int iIndex = iStartIndex; iIndex < m_oSeqEmployeeIdPairs.size() && (iIndex - iStartIndex) < MAX_ITEM_PER_PAGE; iIndex++)
				m_oCurrentPageSeqIdPairs.put(Integer.valueOf(iIndex - iStartIndex), m_oSeqEmployeeIdPairs.get(Integer.valueOf(iIndex)));
		}

		// Reload common basket
		// Clear common basket first
		m_oFrameTipsTracking.clearAllRecords();

		String sTipsIn = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dTipsIn);
		String sSCIn = AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_dSCIn);
		String sDirectTipsIn = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsIn);
		String sTipsFromOthers = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dTipsFromOthers);
		String sSCFromOthers = AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_dSCFromOthers);
		String sDirectTipsFromOthers = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsFromOthers);
		String sTipsBal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dTipsBalance);
		String sSCBal = AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_dSCBalance);
		String sDirectTipsBal = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsBalance);

		// Set frame infos: employee; outlet; tips and SC in, from others and balance
		m_oFrameTipsTracking.setFrameInfo(sTipsIn, sSCIn, sDirectTipsIn, sTipsFromOthers, sSCFromOthers,
				sDirectTipsFromOthers, sTipsBal, sSCBal, sDirectTipsBal, String.valueOf(m_oCurrentEmployee.getNumber()),
				m_oCurrentEmployee.getFirstName(AppGlobal.g_oCurrentLangIndex.get()) + " "
						+ m_oCurrentEmployee.getLastName(AppGlobal.g_oCurrentLangIndex.get()),
				m_oCurrentOutlet.getName(AppGlobal.g_oCurrentLangIndex.get()));

		// Add rows to common basket
		int iItemIndex = 0;
		for (Entry<Integer, Integer> entry : m_oCurrentPageSeqIdPairs.entrySet()) {
			UserUser oEmployee = m_oEmployeeIdUserPairs.get(entry.getValue());
			BigDecimal dTipsOut = m_oToEmployeeIdValuesPairs.get(entry.getValue())[0];
			BigDecimal dSCOut = m_oToEmployeeIdValuesPairs.get(entry.getValue())[1];
			BigDecimal dDirectTipsOut = m_oToEmployeeIdValuesPairs.get(entry.getValue())[2];
			m_oFrameTipsTracking.addRecord(0, iItemIndex++, oEmployee.getNumber(),
					oEmployee.getFirstName(AppGlobal.g_oCurrentLangIndex.get()) + " " +
					oEmployee.getLastName(AppGlobal.g_oCurrentLangIndex.get()), dTipsOut, dSCOut, dDirectTipsOut, true);
		}
	}

	private ArrayList<PosTipsTrackTransactions> constructPosTipsTransactionsListObject(TreeMap<Integer, List<String>> oIdTypePairs, TreeMap<Integer, List<BigDecimal>> oIdValuesPairs, TreeMap<Integer, String> oIdOperationPairs) {
		ArrayList<PosTipsTrackTransactions> oPosMultiTipsTrackTransactions = new ArrayList<PosTipsTrackTransactions>();

		//each entry: { ID -> ValueByType[tips, SC, direct tips] }
		//loop ID
		for (Entry<Integer, List<BigDecimal>> oValues : oIdValuesPairs.entrySet()) {
			//loop ValueByType
			for(int iTypeIndex = 0; iTypeIndex < oValues.getValue().size(); iTypeIndex++) {
				int iTempId = oValues.getKey();
				PosTipsTrackTransactions oTransaction = new PosTipsTrackTransactions();
				oTransaction.setAmount(oValues.getValue().get(iTypeIndex));
				oTransaction.setOperation(oIdOperationPairs.get(iTempId));
				oTransaction.setType(oIdTypePairs.get(iTempId).get(iTypeIndex));
				oTransaction.setBdayId(m_oCurrentBday.getBdayId());
				oTransaction.setFromUserId(m_oCurrentEmployee.getUserId());
				oTransaction.setToUserId(oValues.getKey());
				oTransaction.setOletId(m_oCurrentOutlet.getOletId());
				oTransaction.setShopId(m_oCurrentOutlet.getShopId());
				oTransaction.setTransactionStatId(AppGlobal.g_oFuncStation.get().getStationId());

				oPosMultiTipsTrackTransactions.add(oTransaction);
			}
		}
		return oPosMultiTipsTrackTransactions;
	}

	private boolean processSave() {

		TreeMap<Integer, List<String>> oIdTypePairs = new TreeMap<Integer, List<String>>();
		TreeMap<Integer, List<BigDecimal>> oIdValuesPairs = new TreeMap<Integer, List<BigDecimal>>();
		TreeMap<Integer, String> oIdOperationPairs = new TreeMap<Integer, String>();

		if (!m_oDeletedEmployeeIds.isEmpty())
			for (Integer oDeleteId : m_oDeletedEmployeeIds) {
				oIdTypePairs.put(oDeleteId, Arrays.asList(PosTipsTrackTransactions.TYPE_TIPS,
						PosTipsTrackTransactions.TYPE_SERVICE_CHARGE, PosTipsTrackTransactions.TYPE_DIRECT_TIPS));
				oIdValuesPairs.put(oDeleteId, Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
				oIdOperationPairs.put(oDeleteId, PosTipsTrackTransactions.OPERATION_DELETE);

				// Add log to action log list
				String sLogRemark = "FromEmployeeID:" + m_oCurrentEmployee.getUserId();
				sLogRemark += ", OutletID:" + m_oCurrentOutlet.getOletId();
				sLogRemark += ", ToEmployeeID:" + oDeleteId.toString();
				sLogRemark += "[Delete To Employee]";

				String sFuncKey = "";
				if (m_bOwnedTipsMode)
					sFuncKey = AppGlobal.FUNC_LIST.own_tips_and_sc_out.name();
				else
					sFuncKey = AppGlobal.FUNC_LIST.tips_and_sc_out_from_other_employee.name();
				AppGlobal.g_oActionLog.get().addActionLog(sFuncKey, PosActionLog.ACTION_RESULT_SUCCESS, "",
						AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
						AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			}
		else {
			for (Entry<Integer, BigDecimal[]> entry : m_oEmployeeIdModifiedValuesPairs.entrySet()) {
				String sLogRemark = "FromEmployeeID:" + m_oCurrentEmployee.getUserId();
				sLogRemark += ", OutletID:" + m_oCurrentOutlet.getOletId();
				sLogRemark += ", ToEmployeeID:" + entry.getKey().toString();

				BigDecimal oOriginalValues[] = m_oToEmployeeIdValuesPairs.get(entry.getKey());
				int iValuesIndex = 0;
				String[] sTypeLists = { PosTipsTrackTransactions.TYPE_TIPS,
						PosTipsTrackTransactions.TYPE_SERVICE_CHARGE, PosTipsTrackTransactions.TYPE_DIRECT_TIPS };
				String[] sLogRemarkLists = { "TipsOut", "SCOut", "DirectTipsOut" };
				List<String> oType = new ArrayList<String>();
				List<BigDecimal> oValues = new ArrayList<BigDecimal>();
				boolean bIsOriginalValueModified = false;
				for (BigDecimal dModifiedAmount : entry.getValue()) {
					if (dModifiedAmount.compareTo(oOriginalValues[iValuesIndex]) != 0) {
						oType.add(sTypeLists[iValuesIndex]);
						oIdTypePairs.put(entry.getKey(), oType);

						oValues.add(dModifiedAmount);
						oIdValuesPairs.put(entry.getKey(), oValues);

						sLogRemark += "[" + sLogRemarkLists[iValuesIndex] + ":"
								+ StringLib.BigDecimalToStringWithoutZeroDecimal(dModifiedAmount) + "]";
						bIsOriginalValueModified = true;
					}
					iValuesIndex++;
				}
				if (!bIsOriginalValueModified) {
					oIdTypePairs.put(entry.getKey(), Arrays.asList(PosTipsTrackTransactions.TYPE_TIPS));
					oIdValuesPairs.put(entry.getKey(), Arrays.asList(entry.getValue()[0]));
					sLogRemark += "[TipsOut:"
							+ StringLib.BigDecimalToStringWithoutZeroDecimal(entry.getValue()[0]) + "]";
				}

				if (m_oNewEmployeeIds.contains(entry.getKey())) {
					oIdOperationPairs.put(entry.getKey(), PosTipsTrackTransactions.OPERATION_NEW);
					m_oNewEmployeeIds.remove(entry.getKey());
				} else
					oIdOperationPairs.put(entry.getKey(), PosTipsTrackTransactions.OPERATION_UPDATE);

				// Add log to action log list
				String sFuncKey = "";
				if (m_bOwnedTipsMode)
					sFuncKey = AppGlobal.FUNC_LIST.own_tips_and_sc_out.name();
				else
					sFuncKey = AppGlobal.FUNC_LIST.tips_and_sc_out_from_other_employee.name();
				AppGlobal.g_oActionLog.get().addActionLog(sFuncKey, PosActionLog.ACTION_RESULT_SUCCESS, "",
						AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(),
						AppGlobal.g_oFuncOutlet.get().getOutletId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(),
						AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(),
						AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", sLogRemark);
			}
		}
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);

		if (oIdValuesPairs.isEmpty())
			return false;
		
		ArrayList<PosTipsTrackTransactions> oPosMultiTipsTrackTransactions = constructPosTipsTransactionsListObject(oIdTypePairs, oIdValuesPairs, oIdOperationPairs);

		// *****************************************************************
		// Create thread to load information
		AppThreadManager oAppThreadManager = new AppThreadManager();

		// Create parameter array
		m_oPosTipsTrackTransactionsList = new PosTipsTrackTransactionsList();
		Object[] oParameters = new Object[1];
		oParameters[0] = oPosMultiTipsTrackTransactions;
		oAppThreadManager.addThread(1, m_oPosTipsTrackTransactionsList, "addUpdateWithMutlipleTransactions", oParameters);
		
		// Run all of the threads
		oAppThreadManager.runThread();

		// Wait for the thread to finish
		oAppThreadManager.waitForThread();

		//Get result
		boolean bLastUpdateSuccess = m_oPosTipsTrackTransactionsList.isLastUpdateSuccess();
		return bLastUpdateSuccess;
	}

	private void updatePageButtonsVisibility() {
		m_oFrameTipsTracking.setPageNumber(m_iTotalPage, m_iCurrentPage);

		if (m_iTotalPage <= 1) {
			m_oFrameTipsTracking.showNextPageButton(false);
			m_oFrameTipsTracking.showPrevPageButton(false);
			return;
		}

		m_oFrameTipsTracking.showNextPageButton(true);
		m_oFrameTipsTracking.showPrevPageButton(true);

		if (m_iCurrentPage == 1)
			m_oFrameTipsTracking.showPrevPageButton(false);
		if (m_iCurrentPage == m_iTotalPage)
			m_oFrameTipsTracking.showNextPageButton(false);
	}

	private void checkUnsavedItems() {
		// Check if saved yet
		if (!m_oEmployeeIdModifiedValuesPairs.isEmpty()) {
			// If not saved, ask for save or abort
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("record_is_edited") + System.lineSeparator()
					+ AppGlobal.g_oLang.get()._("save_the_changes") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked()) {
				// Save the record
				if (processSave()) {
					printTipTrackingSlip(new ArrayList<Integer>(m_oEmployeeIdModifiedValuesPairs.keySet()));
					createToEmployeeIdPairs();
					m_iTotalPage = calculateTotalPageOfPairs();
					if (m_iCurrentPage > m_iTotalPage)
						m_iCurrentPage = m_iTotalPage;

					loadTipsTrackTransactionsAtPage(m_iCurrentPage);
					updatePageButtonsVisibility();
					m_oFrameTipsTracking.setSaveButtonEnabled(false);
				}
			}
		}
	}

	private void printTipTrackingSlip(List<Integer> iToEmployeeIds) {
		JSONObject oHeaderJSONObject = new JSONObject(), oInfoJSONObject = new JSONObject(),
				oTempJSONObject = new JSONObject(), oToEmployeeJSONObject = null;
		JSONArray oTempJSONArray = new JSONArray(), oValuesJSONArray = null;
		PosCheck oPosCheck = new PosCheck();

		try {
			oHeaderJSONObject.put("header", "Tip Tracking");
			oInfoJSONObject.put("stationId", AppGlobal.g_oFuncStation.get().getStationId());
			oInfoJSONObject.put("userId", AppGlobal.g_oFuncUser.get().getUserId());
			oInfoJSONObject.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());

			oTempJSONObject.put("fromEmployeeId", m_oCurrentEmployee.getUserId());
			for (Integer iToEmployeeId : iToEmployeeIds) {
				oValuesJSONArray = new JSONArray();
				BigDecimal oValues[] = m_oEmployeeIdModifiedValuesPairs.containsKey(iToEmployeeId)
						? m_oEmployeeIdModifiedValuesPairs.get(iToEmployeeId)
						: m_oToEmployeeIdValuesPairs.get(iToEmployeeId);
				oValuesJSONArray.put(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oValues[0]));
				oValuesJSONArray.put(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(oValues[1]));
				oValuesJSONArray.put(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(oValues[2]));
				oToEmployeeJSONObject = new JSONObject();
				oToEmployeeJSONObject.put(iToEmployeeId.toString(), oValuesJSONArray);
				oTempJSONArray.put(oToEmployeeJSONObject);
			}
			oTempJSONObject.put("toEmployeeIds", oTempJSONArray);
			oTempJSONObject.put("tipBalance",
					AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dTipsBalance));
			oTempJSONObject.put("scBalance", AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_dSCBalance));
			oTempJSONObject.put("directTipBalance",
					AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsBalance));
			oInfoJSONObject.put("tipTrackingInfo", oTempJSONObject);
		} catch (JSONException jsone) {
			AppGlobal.stack2Log(jsone);
		}

		// *****************************************************************
		// Create thread to print special slip
		AppThreadManager oAppThreadManager = new AppThreadManager();

		// Add the method to the thread manager
		// Thread 1 : Print special slip
		// Create parameter array
		Object[] oParameters = new Object[5];
		oParameters[0] = PosActionPrintQueue.KEY_TIP_TRACKING;
		oParameters[1] = oHeaderJSONObject;
		oParameters[2] = oInfoJSONObject;
		oParameters[3] = AppGlobal.g_oCurrentLangIndex.get();
		oParameters[4] = 0;
		oAppThreadManager.addThread(1, oPosCheck, "printSpecialSlip", oParameters);

		// Run the thread without wait
		oAppThreadManager.runThread();
	}

	@Override
	public void frameTipsTracking_clickSave() {
		// Save the record
		if (processSave()) {
			printTipTrackingSlip(new ArrayList<Integer>(m_oEmployeeIdModifiedValuesPairs.keySet()));
			createToEmployeeIdPairs();
			m_iTotalPage = calculateTotalPageOfPairs();
			if (m_iCurrentPage > m_iTotalPage)
				m_iCurrentPage = m_iTotalPage;

			loadTipsTrackTransactionsAtPage(m_iCurrentPage);
			updatePageButtonsVisibility();
		}
	}

	@Override
	public void frameTipsTracking_clickExit() {
		checkUnsavedItems();

		// Finish showing this form
		finishShow();
	}

	@Override
	public boolean frameTipsTracking_checkUpdateCellValue(String sNewValue) {
		String sErrMsg = "";
		try {
			BigDecimal dNewValue = new BigDecimal(sNewValue.trim());
			if (m_iCurrentItemListFieldIndex == 3 || m_iCurrentItemListFieldIndex == 5)
				dNewValue = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dNewValue);
			else if (m_iCurrentItemListFieldIndex == 4)
				dNewValue = AppGlobal.g_oFuncOutlet.get().roundSCAmountToBigDecimal(dNewValue);
		} catch (NumberFormatException e) {
			sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
		}
		if (sErrMsg.isEmpty())
			return true;
		FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
		oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
		oFormDialogBox.setMessage(sErrMsg);
		oFormDialogBox.show();
		return false;
	}

	@Override
	public void frameTipsTracking_clickCommonBasketCell(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex,
			String sPrevEditValue) {
		// Check if previous edit is modified
		if ((m_iCurrentItemListItemIndex >= 0) && (m_iCurrentItemListFieldIndex == 3 || m_iCurrentItemListFieldIndex == 4
						|| m_iCurrentItemListFieldIndex == 5)
				&& m_iCurrentEditEmployeeId.compareTo(Integer.valueOf(-1)) != 0 && sPrevEditValue != null) {
			boolean bNeedSave = false;
			boolean bSetBackgroundColor = false;

			BigDecimal[] oNewValues = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
			BigDecimal[] oOriginalValues = m_oToEmployeeIdValuesPairs.get(m_iCurrentEditEmployeeId);
			if (m_oEmployeeIdModifiedValuesPairs.containsKey(m_iCurrentEditEmployeeId))
				oNewValues = m_oEmployeeIdModifiedValuesPairs.get(m_iCurrentEditEmployeeId);
			else {
				oNewValues[0] = oOriginalValues[0];
				oNewValues[1] = oOriginalValues[1];
				oNewValues[2] = oOriginalValues[2];
			}

			BigDecimal dPrevValue = new BigDecimal(sPrevEditValue);
			int iValueIndex = m_iCurrentItemListFieldIndex - 3;
			oNewValues[iValueIndex] = dPrevValue;

			for (int iIndex = 0; iIndex < oNewValues.length; iIndex++)
				if (oNewValues[iIndex].compareTo(oOriginalValues[iIndex]) != 0) {
					bNeedSave = true;
					if (iValueIndex == iIndex)
						bSetBackgroundColor = true;
				}

			// Update m_oItemIDModifiedValuesPairs
			if (bNeedSave) {
				m_oEmployeeIdModifiedValuesPairs.put(m_iCurrentEditEmployeeId, oNewValues);
				m_oFrameTipsTracking.showPrinterIcon(0, m_iCurrentItemListItemIndex, false);
			} else if (!m_oNewEmployeeIds.contains(m_iCurrentEditEmployeeId)) {
				m_oEmployeeIdModifiedValuesPairs.remove(m_iCurrentEditEmployeeId);
				m_oFrameTipsTracking.showPrinterIcon(0, m_iCurrentItemListItemIndex, true);
				if (m_oEmployeeIdModifiedValuesPairs.isEmpty())
					m_oFrameTipsTracking.setSaveButtonEnabled(false);
			}

			// Update field background
			m_oFrameTipsTracking.setCellFieldBackgroundColorEdited(0, m_iCurrentItemListItemIndex,
					m_iCurrentItemListFieldIndex, bSetBackgroundColor);

			// Update tips and SC balance
			if (m_iCurrentItemListFieldIndex == 3) {
				m_dTipsBalance = m_dTipsBalance.subtract(dPrevValue.subtract(m_dCurrentEditOriValue));
				m_oFrameTipsTracking.setTipsBalance(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dTipsBalance));
			} else if (m_iCurrentItemListFieldIndex == 4) {
				m_dSCBalance = m_dSCBalance.subtract(dPrevValue.subtract(m_dCurrentEditOriValue));
				m_oFrameTipsTracking.setSCBalance(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_dSCBalance));
			} else {
				m_dDirectTipsBalance = m_dDirectTipsBalance.subtract(dPrevValue.subtract(m_dCurrentEditOriValue));
				m_oFrameTipsTracking.setDirectTipsBalance(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsBalance));
			}
		}

		m_iCurrentItemListItemIndex = iNewItemIndex;
		m_iCurrentItemListFieldIndex = iNewFieldIndex;
		m_iCurrentEditEmployeeId = Integer.valueOf(-1);
		m_dCurrentEditOriValue = BigDecimal.ZERO;

		// Update the current editing Employee ID
		if (iNewItemIndex >= 0) {
			m_iCurrentEditEmployeeId = m_oCurrentPageSeqIdPairs.get(Integer.valueOf(iNewItemIndex));

			if (iNewFieldIndex == 0) {
				if (!m_oEmployeeIdModifiedValuesPairs.containsKey(m_iCurrentEditEmployeeId))
					printTipTrackingSlip(Arrays.asList(m_iCurrentEditEmployeeId));
			} else if (iNewFieldIndex == 1 || iNewFieldIndex == 2) {
				// Highlight employee number or name field if clicked
				if (!m_oSelectedEmployeeIds.contains(m_iCurrentEditEmployeeId)) {
					m_oSelectedEmployeeIds.add(m_iCurrentEditEmployeeId);

					m_oFrameTipsTracking.setCellFieldBackgroundColorEdited(0, m_iCurrentItemListItemIndex, 1, true);
					m_oFrameTipsTracking.setCellFieldBackgroundColorEdited(0, m_iCurrentItemListItemIndex, 2, true);
					m_oFrameTipsTracking.setDeleteButtonEnabled(true);
				} else {
					m_oSelectedEmployeeIds.remove(m_iCurrentEditEmployeeId);
					m_oFrameTipsTracking.setCellFieldBackgroundColorEdited(0, m_iCurrentItemListItemIndex, 1, false);
					m_oFrameTipsTracking.setCellFieldBackgroundColorEdited(0, m_iCurrentItemListItemIndex, 2, false);

					// if no item is selected, set Delete button to disable
					if (m_oSelectedEmployeeIds.isEmpty())
						m_oFrameTipsTracking.setDeleteButtonEnabled(false);
				}
			} else if (iNewFieldIndex == 3 || iNewFieldIndex == 4 || iNewFieldIndex == 5) {
				if (m_oEmployeeIdModifiedValuesPairs.containsKey(m_iCurrentEditEmployeeId))
					m_dCurrentEditOriValue = m_oEmployeeIdModifiedValuesPairs.get(m_iCurrentEditEmployeeId)[iNewFieldIndex - 3];
				else
					m_dCurrentEditOriValue = m_oToEmployeeIdValuesPairs.get(m_iCurrentEditEmployeeId)[iNewFieldIndex - 3];
			}
		}
	}

	@Override
	public void frameTipsTracking_clickAddEmployee() {
		// Force user to save the current page if a new page would be turned to after adding employee
		if (m_iCurrentPage != m_iTotalPage || m_oCurrentPageSeqIdPairs.size() >= MAX_ITEM_PER_PAGE)
			checkUnsavedItems();

		// Ask for a input of a new employee
		UserUser oNewEmployee = new UserUser();
		for (FormTipsTrackingListener listener : listeners)
			oNewEmployee = listener.formTipsTracking_inputEmployee();
		if (oNewEmployee.getUserId() == 0)
			return;

		// Forbid user entering duplicate employee or current from employee
		String sErrMsg = "";
		Integer iNewEmployeeId = Integer.valueOf(oNewEmployee.getUserId());
		if (iNewEmployeeId.equals(m_oCurrentEmployee.getUserId()))
			sErrMsg = AppGlobal.g_oLang.get()._("same_employee_as_the_current_employee");
		for (Entry<Integer, Integer> entry : m_oSeqEmployeeIdPairs.entrySet())
			if (iNewEmployeeId.equals(entry.getValue()))
				sErrMsg = AppGlobal.g_oLang.get()._("employee_already_exists_in_the_list");
		if (!sErrMsg.isEmpty()) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}

		m_oEmployeeIdUserPairs.put(iNewEmployeeId, oNewEmployee);
		m_oToEmployeeIdValuesPairs.put(iNewEmployeeId, new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO });

		if (m_iTotalPage == 0) {
			m_iTotalPage++;
			m_iCurrentPage = m_iTotalPage;
		}
		if (m_iCurrentPage == m_iTotalPage && m_oCurrentPageSeqIdPairs.size() < MAX_ITEM_PER_PAGE) {
			m_oFrameTipsTracking.addRecord(0, m_oCurrentPageSeqIdPairs.size(), oNewEmployee.getNumber(),
					oNewEmployee.getFirstName(AppGlobal.g_oCurrentLangIndex.get()) + " "
							+ oNewEmployee.getLastName(AppGlobal.g_oCurrentLangIndex.get()),
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, false);
			m_oCurrentPageSeqIdPairs.put(Integer.valueOf(m_oCurrentPageSeqIdPairs.size()), iNewEmployeeId);
			m_oSeqEmployeeIdPairs.put(Integer.valueOf(m_oSeqEmployeeIdPairs.size()), iNewEmployeeId);
		} else {
			for (Integer iNewId : m_oNewEmployeeIds) {
				m_oSeqEmployeeIdPairs.values().remove(iNewId);
				m_oToEmployeeIdValuesPairs.remove(iNewId);
			}
			m_oSeqEmployeeIdPairs.put(Integer.valueOf(m_oSeqEmployeeIdPairs.size()), iNewEmployeeId);
			m_iTotalPage = calculateTotalPageOfPairs();
			m_iCurrentPage = m_iTotalPage;
			loadTipsTrackTransactionsAtPage(m_iCurrentPage);
			updatePageButtonsVisibility();
			m_oFrameTipsTracking.showPrinterIcon(0, m_oCurrentPageSeqIdPairs.size() - 1, false);
		}
		m_oEmployeeIdModifiedValuesPairs.put(iNewEmployeeId, new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO });
		m_oNewEmployeeIds.add(iNewEmployeeId);
		m_oFrameTipsTracking.setSaveButtonEnabled(true);
		m_oFrameTipsTracking.frameCommonBasketCell_FieldClicked(0, 0, m_oCurrentPageSeqIdPairs.size() - 1, 3, "");
	}

	@Override
	public void frameTipsTracking_clickDeleteEmployee() {
		if (!m_oEmployeeIdModifiedValuesPairs.isEmpty()) {
			checkUnsavedItems();
			return;
		}

		if (!m_oSelectedEmployeeIds.isEmpty()) {
			// If there are selected items, ask for delete or abort
			FormConfirmBox oFormConfirmBox = new FormConfirmBox(AppGlobal.g_oLang.get()._("yes"),
					AppGlobal.g_oLang.get()._("no"), this);
			oFormConfirmBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormConfirmBox.setMessage(AppGlobal.g_oLang.get()._("delete_selected_employees") + "?");
			oFormConfirmBox.show();
			if (oFormConfirmBox.isOKClicked()) {
				for (Integer iSelectedId : m_oSelectedEmployeeIds) {
					if (!m_oNewEmployeeIds.contains(iSelectedId))
						m_oDeletedEmployeeIds.add(iSelectedId);
				}

				boolean bLoadTipsTrackTransactions = true;
				if (!m_oDeletedEmployeeIds.isEmpty())
					bLoadTipsTrackTransactions = processSave();
				if (bLoadTipsTrackTransactions) {
					createToEmployeeIdPairs();

					// Calculate page number
					m_iTotalPage = calculateTotalPageOfPairs();
					if (m_iCurrentPage > m_iTotalPage)
						m_iCurrentPage = m_iTotalPage;

					loadTipsTrackTransactionsAtPage(m_iCurrentPage);
					updatePageButtonsVisibility();
				}
				m_oDeletedEmployeeIds.clear();
			}
		}
	}

	@Override
	public void frameTipsTracking_clickDirectTipsIn() {
		//checkUnsavedItems();

		// ask amount
		boolean bBreak = false;
		BigDecimal dTargetAmount = BigDecimal.ZERO;
		do {
			String sErrorMessage = "";
			FormInputBox oFormInputBox = new FormInputBox(this);
			oFormInputBox.init();
			oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
			oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("direct_tips_in"));
			oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_direct_tips_amount") + ": ");
			oFormInputBox.show();

			if (oFormInputBox.getInputValue() == null)
				return;
			else {
				try {
					dTargetAmount = new BigDecimal(oFormInputBox.getInputValue().trim());
					dTargetAmount = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToBigDecimal(dTargetAmount);

					if (dTargetAmount.compareTo(BigDecimal.ZERO) < 0)
						sErrorMessage = AppGlobal.g_oLang.get()._("invalid_input");
					else if (dTargetAmount.compareTo(m_dDirectTipsIn.subtract(m_dDirectTipsBalance)) < 0)
						sErrorMessage = AppGlobal.g_oLang.get()._("value_need_to_be_larger_than_or_equal_to") + " "
								+ AppGlobal.g_oFuncOutlet.get()
										.roundPaymentAmountToString(m_dDirectTipsIn.subtract(m_dDirectTipsBalance));
					else
						bBreak = true;

				} catch (NumberFormatException e) {
					sErrorMessage = AppGlobal.g_oLang.get()._("invalid_input");
				}

				if (!sErrorMessage.isEmpty()) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("warning"));
					oFormDialogBox.setMessage(sErrorMessage);
					oFormDialogBox.show();
					oFormDialogBox = null;
				}
			}
		} while (!bBreak);
		
		if (dTargetAmount.compareTo(m_dDirectTipsIn) != 0) {
			m_oDeletedEmployeeIds.clear();
			TreeMap<Integer, BigDecimal[]> oPreviousModifiedValues = new TreeMap<Integer, BigDecimal[]>();
			oPreviousModifiedValues.putAll(m_oEmployeeIdModifiedValuesPairs);
			m_oEmployeeIdModifiedValuesPairs.clear();
			m_oToEmployeeIdValuesPairs.put(new Integer(m_oCurrentEmployee.getUserId()),
					new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, m_dDirectTipsIn });
			m_oEmployeeIdModifiedValuesPairs.put(new Integer(m_oCurrentEmployee.getUserId()),
					new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, dTargetAmount });

			if (processSave()) {
				m_oEmployeeIdModifiedValuesPairs.remove(new Integer(m_oCurrentEmployee.getUserId()));
				m_oEmployeeIdModifiedValuesPairs.putAll(oPreviousModifiedValues);
				m_oToEmployeeIdValuesPairs.remove(new Integer(m_oCurrentEmployee.getUserId()));
				m_dDirectTipsBalance = m_dDirectTipsBalance.add(dTargetAmount).subtract(m_dDirectTipsIn);
				m_dDirectTipsIn = dTargetAmount;
				m_oFrameTipsTracking.setDirectTipsIn(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsIn));
				m_oFrameTipsTracking.setDirectTipsBalance(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(m_dDirectTipsBalance));
			}
		}
	}

	@Override
	public void frameTipsTracking_clickTipsFromOthersInfo() {
		FormListMessageBox oFormListMessageBox = new FormListMessageBox(true, this);
		oFormListMessageBox.setTitle(AppGlobal.g_oLang.get()._("tips_and_sc_from_others"));
		oFormListMessageBox.setCloseButtonVisible(true);
		oFormListMessageBox.setHeaderTextSize(22);
		oFormListMessageBox.setMessageTextSize(20);
		oFormListMessageBox.setMessagePadding("10,8,0,8");

		ArrayList<String> oTextAligns = new ArrayList<String>();
		for (int i = 0; i < 2; i++)
			oTextAligns.add(HeroActionProtocol.View.Attribute.TextAlign.LEFT + ","
					+ HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		for (int j = 0; j < 3; j++)
			oTextAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + ","
					+ HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		oFormListMessageBox.setMessageTextAlign(oTextAligns);

		oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("employee_no"), 210);
		oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("name"), 240);
		oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("tips_in"), 100);
		oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("service_charge_in"), 210);
		oFormListMessageBox.addColumnHeader(AppGlobal.g_oLang.get()._("direct_tips_in"), 160);
		oFormListMessageBox.setFrameMessageHeight(47);

		for (Entry<Integer, BigDecimal[]> entry : m_oFromEmployeeIdValuesPairs.entrySet()) {
			ArrayList<String> oMessageArray = new ArrayList<String>();
			UserUser oEmployee = m_oEmployeeIdUserPairs.get(entry.getKey());
			oMessageArray.add(oEmployee.getNumber());
			oMessageArray.add(oEmployee.getFirstName(AppGlobal.g_oCurrentLangIndex.get()) + " "
					+ oEmployee.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			oMessageArray.add(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(entry.getValue()[0]));
			oMessageArray.add(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(entry.getValue()[1]));
			oMessageArray.add(AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(entry.getValue()[2]));
			oFormListMessageBox.addMessage(oMessageArray);
		}

		oFormListMessageBox.show();
	}

	@Override
	public void frameTipsTracking_clickSwitchOutlet() {
		checkUnsavedItems();

		OutOutlet oOutlet = new OutOutlet();

		// Ask for a input of outlet
		for (FormTipsTrackingListener listener : listeners)
			oOutlet = listener.formTipsTracking_selectOutlet();

		if (oOutlet != null) {
			int iOutletId = oOutlet.getOletId();
			if (iOutletId > 0 && m_oCurrentOutlet.getOletId() != iOutletId) {

				// Check if the BusinessDay for another outlet exists
				PosBusinessDay oBusinessDay = new PosBusinessDay();
				if (!oBusinessDay.readByDateOutletId(AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDateInString(), oOutlet.getOletId())) {
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("daily_start_has_not_been_carried_out"));
					oFormDialogBox.show();
					return;
				}

				m_oCurrentBday = oBusinessDay;
				m_oCurrentOutlet = oOutlet;
				loadTipsTrackingRecord();
			}
		}
	}

	@Override
	public void frameTipsTracking_clickNextPage() {
		checkUnsavedItems();

		if ((m_iCurrentPage + 1) <= m_iTotalPage) {
			m_iCurrentPage++;
			loadTipsTrackTransactionsAtPage(m_iCurrentPage);

			updatePageButtonsVisibility();
		}
	}

	@Override
	public void frameTipsTracking_clickPrevPage() {
		checkUnsavedItems();

		for (Integer iNewId : m_oNewEmployeeIds) {
			m_oSeqEmployeeIdPairs.values().remove(iNewId);
			m_oToEmployeeIdValuesPairs.remove(iNewId);
		}
		m_iTotalPage = calculateTotalPageOfPairs();
		if ((m_iCurrentPage - 1) >= 1) {
			m_iCurrentPage--;
			loadTipsTrackTransactionsAtPage(m_iCurrentPage);

			updatePageButtonsVisibility();
		}
	}

}
