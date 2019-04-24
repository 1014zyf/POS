package app;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import core.Controller;
import om.PosCheckExtraInfo;
import om.PosCheckExtraInfoList;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;


/** interface for the listeners/observers callback method */
interface FormSearchAdvanceOrderListener {
	void formSearchAdvanceOrder_RetrievedRecord(String sCheckId);
}

public class FormSearchAdvanceOrder extends VirtualUIForm implements FrameSearchAdvanceOrderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameSearchAdvanceOrder m_oFrameSearchAdvanceOrder;
	
	private boolean m_bUserCancel = false;
	
	private ArrayList<ArrayList<PosCheckExtraInfo>> m_oPosCheckExtraInfoList;
	private int m_iCurrentPage;
	private int m_iTotalPage;

	public static final int MAX_RECORD_SIZE = 7;	// record show in each page
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormSearchAdvanceOrderListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormSearchAdvanceOrderListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormSearchAdvanceOrderListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormSearchAdvanceOrder(Controller oParentController, String sTitle) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSearchAdvanceOrder.xml");
		listeners = new ArrayList<FormSearchAdvanceOrderListener>();

		m_iCurrentPage = 0;
		m_iTotalPage = 0;
		m_oPosCheckExtraInfoList = new ArrayList<ArrayList<PosCheckExtraInfo>>();
		
		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);

		m_oFrameSearchAdvanceOrder = new FrameSearchAdvanceOrder(sTitle);
		m_oFrameSearchAdvanceOrder.addListener(this);
		m_oFrameSearchAdvanceOrder.showDefaultRecords();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchAdvanceOrder, "fraSearchAdvanceOrder");
		
		this.attachChild(m_oFrameSearchAdvanceOrder);
	}
	
	// update records of page
	private void showResultRecordsAtPage(int iPage) {
		int iStartIndex = (iPage - 1) * MAX_RECORD_SIZE;
		int iEndIndex = iPage * MAX_RECORD_SIZE;
		if (iEndIndex > m_oPosCheckExtraInfoList.size())
			iEndIndex = m_oPosCheckExtraInfoList.size();
		
		for (int i = iStartIndex; i < iEndIndex; i++) {
				ArrayList<PosCheckExtraInfo> oPosCheckExtraInfotList = new ArrayList<PosCheckExtraInfo>();
				oPosCheckExtraInfotList = m_oPosCheckExtraInfoList.get(i);
				String sName = "", sMobile = "", sPickupDate = "", sDeposit = "", sReference = "";
				for(int j = 0; j < oPosCheckExtraInfotList.size(); j++){
					switch(oPosCheckExtraInfotList.get(j).getVariable()) {
						case PosCheckExtraInfo.VARIABLE_REFERENCE:
							sReference = oPosCheckExtraInfotList.get(j).getValue();
							break;
						case PosCheckExtraInfo.VARIABLE_PICKUP_DATE:
							sPickupDate = oPosCheckExtraInfotList.get(j).getValue();
							break;
						case PosCheckExtraInfo.VARIABLE_GUEST_NAME:
							sName = oPosCheckExtraInfotList.get(j).getValue();
							break;
						case PosCheckExtraInfo.VARIABLE_PHONE:
							sMobile = oPosCheckExtraInfotList.get(j).getValue();
							break;
						case PosCheckExtraInfo.VARIABLE_DEPOSIT_AMOUNT:
							sDeposit = oPosCheckExtraInfotList.get(j).getValue();
							break;
					}
			}
			m_oFrameSearchAdvanceOrder.addDepositRecord(sReference, sPickupDate, sName, sMobile, sDeposit);
		}
	}

	@Override
	public void frameSearchAdvanceOrder_clickClose() {
		m_bUserCancel = true;
		this.finishShow();
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}

	@Override
	public void frameSearchAdvanceOrder_clickRecord(String sReferenceNo) {
		// TODO Auto-generated method stub
		for(FormSearchAdvanceOrderListener listener: listeners) {
			listener.formSearchAdvanceOrder_RetrievedRecord(sReferenceNo);
		}
		this.finishShow();
	}

	@Override
	public void frameSearchAdvanceOrder_clickRetreive(JSONObject oSearchInfoJSON) {
		try {
			oSearchInfoJSON.put("section", PosCheckExtraInfo.SECTION_ADVANCE_ORDER);
			oSearchInfoJSON.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			oSearchInfoJSON.put("shopId", AppGlobal.g_oFuncOutlet.get().getShopId());
			oSearchInfoJSON.put("shopTimeZone", AppGlobal.g_oFuncOutlet.get().getShop().getTimezone());
			String sTimezoneName = "";
			if (!AppGlobal.g_oFuncOutlet.get().getShop().getTimezoneName().isEmpty())
				sTimezoneName = AppGlobal.g_oFuncOutlet.get().getShop().getTimezoneName();
			oSearchInfoJSON.put("shopTimeZoneName", sTimezoneName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Retrieve Data from DB
		ArrayList<ArrayList<PosCheckExtraInfo>> oPosCheckExtraInfoListByCheckLevel = new ArrayList<ArrayList<PosCheckExtraInfo>>();
		
		PosCheckExtraInfoList oPosCheckExtraInfoList = new PosCheckExtraInfoList();
		oPosCheckExtraInfoList.readAllAdvancedOrderBySearchingInfo(oSearchInfoJSON);
		oPosCheckExtraInfoListByCheckLevel = oPosCheckExtraInfoList.getCheckExtraInfoListByCheckLevel();
		
		if(oPosCheckExtraInfoListByCheckLevel.isEmpty()){
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_advance_order_record_is_found"));
			oFormDialogBox.show();
			return;
		}
		
		m_oFrameSearchAdvanceOrder.clearRecords();
		m_oPosCheckExtraInfoList = oPosCheckExtraInfoListByCheckLevel;
		
		// Reset page number
		m_iCurrentPage = 1;
		m_iTotalPage = m_oPosCheckExtraInfoList.size() / MAX_RECORD_SIZE;
		if (m_oPosCheckExtraInfoList.size() % MAX_RECORD_SIZE > 0)
			m_iTotalPage++;

		showResultRecordsAtPage(m_iCurrentPage);
		m_oFrameSearchAdvanceOrder.updatePageButton(m_iCurrentPage, m_iTotalPage);
		m_oFrameSearchAdvanceOrder.clearSearchField();
	}

	@Override
	public void frameSearchAdvanceOrder_clickNextPage() {
		// TODO Auto-generated method stub
		m_iCurrentPage++;
		m_oFrameSearchAdvanceOrder.clearRecords();
		this.showResultRecordsAtPage(m_iCurrentPage);
		m_oFrameSearchAdvanceOrder.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}

	@Override
	public void frameSearchAdvanceOrder_clickPrevPage() {
		// TODO Auto-generated method stub
		m_iCurrentPage--;
		m_oFrameSearchAdvanceOrder.clearRecords();
		this.showResultRecordsAtPage(m_iCurrentPage);
		m_oFrameSearchAdvanceOrder.updatePageButton(m_iCurrentPage, m_iTotalPage);
	}
	
	@Override
	public void frameSearchAdvanceOrder_clickReset() {
		// TODO Auto-generated method stub
		m_oFrameSearchAdvanceOrder.clearSearchField();
	}
}
