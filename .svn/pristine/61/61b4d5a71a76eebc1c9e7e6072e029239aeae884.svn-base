package app;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.joda.time.LocalDateTime;
import org.json.JSONArray;

import commonui.FormSelectionBox;
import commonui.FormDialogBox;
import commonui.FormInputBox;
import externallib.StringLib;
import om.PosBusinessDay;
import om.PosCheck;
import om.PosCheckExtraInfo;
import om.PosVoidReason;
import om.PosVoidReasonList;
import om.UserUser;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameCheckListPanelListener {
	void frameCheckListPanel_CheckListRecordClicked(PosCheck oCheck, int iListingType);
	void frameCheckListPanel_ButtonContinueClicked();
}

public class FrameCheckListPanel extends VirtualUIFrame implements FrameCommonBasketListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private List<HashMap<String, String>> m_oFloorPlanTableList;
	private FrameCommonBasket m_oCheckListCommonBasket;
	private HashMap<Integer, String> m_oUserNameList;
	private int m_iCheckListRowHeight;
	
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUIButton m_oButtonSearch;
	private VirtualUIButton m_oButtonViewAll;
	
	private VirtualUILabel m_oLabelTotalCheck;
	private VirtualUILabel m_oLableTotalAmount;
	
	private VirtualUIButton m_oButtonContinue;
	
	private HashMap<Integer, PosCheck> m_oCheckList;
	private TreeMap<Integer, PosCheck> m_oDisplayCheckList;
	private List<String> m_oColumnHeaderList;
	private HashMap<String, String> m_oColumnHeaderSortStatusList;
	
	private String m_sTableTitleRefType;
	private String m_sTableColumnRefType;
	private int m_iSetCheckListingByType;
	
	private boolean m_bRecordLoaded;
	private boolean m_bIsShowTotalDue;
	private int m_iCurrentPageStartNo;
	private String m_sPanelType;
	public int m_iPageRecordCount;
	private int m_iScrollIndex = 0;
	private int m_iCheckListingType;
	private AppGlobal.OPERATION_MODE m_eOperationMode;
	private HashMap<String, String> m_oBusinessDayStringList;
	
	private  HashMap<Integer, PosVoidReason> m_oVoidReasonList;
	
	public static String TYPE_PAID_CHECK = "p";
	public static String TYPE_OPEN_CHECK = "o";
	public static String TYPE_VOID_CHECK = "v";
	public static String TYPE_PAST_DATE_CHECK = "pd";
	
	public static String COLUMN_HEADER_TABLE = "t";
	public static String COLUMN_HEADER_CHECK = "c";
	public static String COLUMN_HEADER_GUEST = "g";
	public static String COLUMN_HEADER_ROOM_NO = "rm";
	public static String COLUMN_HEADER_DAY = "day";
	public static String COLUMN_HEADER_CHECK_TOTAL = "tot";
	public static String COLUMN_HEADER_OPEN_TIME = "ot";
	public static String COLUMN_HEADER_OPEN_USER = "ou";
	public static String COLUMN_HEADER_CLOSE_TIME = "ct";
	public static String COLUMN_HEADER_CLOSE_USER = "cu";
	public static String COLUMN_HEADER_VOID_TIME = "vt";
	public static String COLUMN_HEADER_VOID_REASON = "vr";
	public static String COLUMN_HEADER_VOID_USER = "vu";
	public static String COLUMN_HEADER_PAYMENT_METHOD = "p";
	
	public static String COLUMN_HEADER_TABLE_REFERENCE = "tr";
	
	public static String SORT_ORDER_NONE = "";
	public static String SORT_ORDER_ASCENDING = "a";
	public static String SORT_ORDER_DESCENDING = "d";
	
	public static int TYPE_CHECK_LISTING_BY_NORMAL = 0;
	public static int TYPE_CHECK_LISTING_BY_TABLE_REFERENCE = 1;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCheckListPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCheckListPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCheckListPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	// constructor
	public FrameCheckListPanel(String sPanelType, int iListingtype, AppGlobal.OPERATION_MODE eOperationMode) {
		// read calculation method
		if (AppGlobal.g_oFuncStation.get().getCheckTotalCalculationMethod().equals(FuncStation.CHECK_LISTING_CALCULATION_METHOD_TOTAL_DUE))
			m_bIsShowTotalDue = true;
		else
			m_bIsShowTotalDue = false;
		
		m_eOperationMode = eOperationMode;
		m_sPanelType = sPanelType;
		m_bRecordLoaded = false;
		m_iCurrentPageStartNo = 0;
		m_iCheckListingType = iListingtype;
		m_oFloorPlanTableList = AppGlobal.g_oFuncOutlet.get().getTableNameList();
		m_oCheckList = new HashMap<Integer, PosCheck>();
		m_oDisplayCheckList = new TreeMap<Integer, PosCheck>();
		m_oColumnHeaderList = new ArrayList<String>();
		m_oColumnHeaderSortStatusList = new HashMap<String, String>();
		m_oBusinessDayStringList = new HashMap<String, String>();
		
		m_iPageRecordCount = 8;
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckListPanelListener>();
		
		m_oUserNameList = new HashMap<Integer, String>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckListPanel.xml");

		m_oCheckListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCheckListCommonBasket, "scrfraCheckListPanel");
		m_oCheckListCommonBasket.init();
		m_oCheckListCommonBasket.addListener(this);
		this.attachChild(m_oCheckListCommonBasket);
		
		// Create Total Check Number label
		m_oLabelTotalCheck = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTotalCheck, "lblTotalCheck");
		this.attachChild(m_oLabelTotalCheck);
		
		// Create Total Amount label
		m_oLableTotalAmount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLableTotalAmount, "lblTotalAmount");
		this.attachChild(m_oLableTotalAmount);
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);
		
		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);
		
		// Check No TextBox
		m_oLabelCheckNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblCheckNo");
		this.attachChild(m_oLabelCheckNo);
		
		
		
		// Search button
		m_oButtonSearch = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSearch, "btnSearch");
		
		m_oButtonSearch.setValue(AppGlobal.g_oLang.get()._("search_by") + System.lineSeparator()
				+ AppGlobal.g_oLang.get()._("check_no"));
		
		if(m_iCheckListingType == FrameCheckListing.TYPE_PAST_DATE)
			m_oButtonSearch.setVisible(false);
		this.attachChild(m_oButtonSearch);
		
		// View All button
		m_oButtonViewAll = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonViewAll, "btnViewAll");
		m_oButtonViewAll.setValue(AppGlobal.g_oLang.get()._("show_all"));
		if(m_iCheckListingType == FrameCheckListing.TYPE_PAST_DATE)
			m_oButtonViewAll.setVisible(false);
		this.attachChild(m_oButtonViewAll);
		
		// Continue button (for daily close check listing use)
		m_oButtonContinue = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonContinue, "btnContinue");
		m_oButtonContinue.setValue(AppGlobal.g_oLang.get()._("confirm"));
		m_oButtonContinue.setVisible(false);
		this.attachChild(m_oButtonContinue);
		
		if(m_iCheckListingType == FrameCheckListing.TYPE_PAST_DATE)
			m_iPageRecordCount = 9;
		
		if(sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK)) {
			m_oVoidReasonList = new HashMap<Integer, PosVoidReason>();
			PosVoidReasonList oTempPosVoidReasonList = new PosVoidReasonList();
			oTempPosVoidReasonList.readAll();
			m_oVoidReasonList = oTempPosVoidReasonList.getVoidReasonList();
		}
		
		m_iCheckListRowHeight = 36;
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_iCheckListRowHeight = 50;
			m_iPageRecordCount = 10;
		}
		
		this.clearCheckNoLabel();
	}
	
	public void initCheckList(JSONArray oCheckJSONArray, boolean bCheckFloorPlan) {
		m_bRecordLoaded = true;
		ArrayList<PosCheck> oRemainCheckList = new ArrayList<PosCheck>();
		int iCount = 0;
		
		if (oCheckJSONArray == null)
			return;
		
		if (bCheckFloorPlan) {
			boolean bPrintTitle = false;
			if (oCheckJSONArray.length() > 0) {
				for (int j = 0; j < oCheckJSONArray.length(); j++) {
					if (oCheckJSONArray.isNull(j))
						continue;
					
					PosCheck oCheck = new PosCheck(oCheckJSONArray.optJSONObject(j));
					if (!isInFloorPlan(oCheck.getTable())) {
						oRemainCheckList.add(oCheck);
						continue;
					}
					
					if (!bPrintTitle) {
						iCount++;
						bPrintTitle = true;
					}

					m_oCheckList.put(iCount, oCheck);
					iCount++;
				}
			}
			
			if(!oRemainCheckList.isEmpty()) {
				iCount++;
				for(PosCheck oCheck: oRemainCheckList) {
					m_oCheckList.put(iCount, oCheck);
					iCount++;
				}
			}
		} else {
			for(int j = 0; j < oCheckJSONArray.length(); j++) {
				if (oCheckJSONArray.isNull(j))
					continue;
				
				PosCheck oCheck = new PosCheck(oCheckJSONArray.optJSONObject(j));
				m_oCheckList.put(j, oCheck);
			}
		}
	}
	
	public void addCheckListPanelTitle(String sPanelType) {
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		// Display total header
		String sDisplayTotalHeader = AppGlobal.g_oLang.get()._("total");
		if(m_bIsShowTotalDue)
			sDisplayTotalHeader = AppGlobal.g_oLang.get()._("total_due");
		
		if(sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(70);
				sFieldValues.add(m_sTableTitleRefType);
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(80);				
				sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("open_time"));
				iFieldWidths.add(120);
				sFieldValues.add(AppGlobal.g_oLang.get()._("total"));
				
				m_oColumnHeaderList.add(m_sTableColumnRefType);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_GUEST);
				m_oColumnHeaderList.add(COLUMN_HEADER_OPEN_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
			}else {
				iFieldWidths.add(50);
				sFieldValues.add("");
				iFieldWidths.add(100);
				sFieldValues.add(m_sTableTitleRefType);
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
				iFieldWidths.add(200);
				sFieldValues.add(AppGlobal.g_oLang.get()._("open_time"));
				iFieldWidths.add(200);
				sFieldValues.add(AppGlobal.g_oLang.get()._("open_user"));
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("total"));
				
				m_oColumnHeaderList.add("");
				m_oColumnHeaderList.add(m_sTableColumnRefType);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_GUEST);
				m_oColumnHeaderList.add(COLUMN_HEADER_OPEN_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_OPEN_USER);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
			}
		} else if(sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK)){
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(80);
				sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
				iFieldWidths.add(50);
				sFieldValues.add(m_sTableTitleRefType);
				iFieldWidths.add(120);
				sFieldValues.add(sDisplayTotalHeader);
				iFieldWidths.add(120);
				sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method"));
				
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_GUEST);
				m_oColumnHeaderList.add(m_sTableColumnRefType);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
				m_oColumnHeaderList.add(COLUMN_HEADER_PAYMENT_METHOD);
			}else {
				iFieldWidths.add(32);
				sFieldValues.add("");
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("open_time"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("close_time"));
				iFieldWidths.add(200);
				sFieldValues.add(AppGlobal.g_oLang.get()._("close_user"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("cover"));
				iFieldWidths.add(100);
				sFieldValues.add(sDisplayTotalHeader);
				iFieldWidths.add(90);
				sFieldValues.add(m_sTableTitleRefType);
				iFieldWidths.add(139);
				sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method"));
				
				m_oColumnHeaderList.add("");
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_OPEN_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_CLOSE_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_CLOSE_USER);
				m_oColumnHeaderList.add(COLUMN_HEADER_GUEST);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
				m_oColumnHeaderList.add(m_sTableColumnRefType);
				m_oColumnHeaderList.add(COLUMN_HEADER_PAYMENT_METHOD);
			}
		} else if(sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK)){
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(85);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_user"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_reason"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_time"));
				iFieldWidths.add(85);
				sFieldValues.add(sDisplayTotalHeader);
				
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_USER);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_REASON);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
			}else {
				iFieldWidths.add(60);
				sFieldValues.add("");
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(170);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_user"));
				iFieldWidths.add(170);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_reason"));
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("void_time"));
				iFieldWidths.add(100);
				sFieldValues.add(sDisplayTotalHeader);
				iFieldWidths.add(180);
				sFieldValues.add(m_sTableTitleRefType);
				
				m_oColumnHeaderList.add("");
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_USER);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_REASON);
				m_oColumnHeaderList.add(COLUMN_HEADER_VOID_TIME);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
				m_oColumnHeaderList.add(m_sTableColumnRefType);
			}
		}else if(sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)){
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(85);
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("room_no"));
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("business_day"));
				iFieldWidths.add(85);
				sFieldValues.add(sDisplayTotalHeader);
				iFieldWidths.add(100);
				sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method"));
				
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_ROOM_NO);
				m_oColumnHeaderList.add(COLUMN_HEADER_DAY);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
				m_oColumnHeaderList.add(COLUMN_HEADER_PAYMENT_METHOD);
			}else {
				iFieldWidths.add(32);
				sFieldValues.add("");
				iFieldWidths.add(150);
														   
				sFieldValues.add(AppGlobal.g_oLang.get()._("check_no"));
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("room_no"));
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("business_day"));
				iFieldWidths.add(150);
				sFieldValues.add(sDisplayTotalHeader);
				iFieldWidths.add(150);
				sFieldValues.add(m_sTableTitleRefType);
				iFieldWidths.add(150);
				sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method"));
				
				m_oColumnHeaderList.add("");
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK);
				m_oColumnHeaderList.add(COLUMN_HEADER_ROOM_NO);
				m_oColumnHeaderList.add(COLUMN_HEADER_DAY);
				m_oColumnHeaderList.add(COLUMN_HEADER_CHECK_TOTAL);
				m_oColumnHeaderList.add(m_sTableColumnRefType);
				m_oColumnHeaderList.add(COLUMN_HEADER_PAYMENT_METHOD);
			}
		}
		
		// mark default sort column header order
		for(String sColumnHeader: m_oColumnHeaderList) {
			m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_NONE);
			
			if (sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK) && sColumnHeader.equals(m_sTableColumnRefType))
				m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_ASCENDING);
			else if ((sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) && sColumnHeader.equals(COLUMN_HEADER_CLOSE_TIME))
					|| (sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK) && sColumnHeader.equals(COLUMN_HEADER_CHECK)))
				m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_DESCENDING);
		}
		
		m_oCheckListCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCheckListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		
		m_oCheckListCommonBasket.setUpperlineVisible(true);
		
		m_oCheckListCommonBasket.setHeaderFormat(36, 15, "");
		
		m_oCheckListCommonBasket.setBottomUnderlineVisible(true);
	}
	
	public void addCheckListRecord(String sType, int iItemIndex, PosCheck oCheck, int iCheckRoundDecimal) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		HashMap<Integer, String> sFieldInfo1sArray = new HashMap<Integer, String>();
		
		int iIndex = 0;
		
		//String sTable = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
		boolean bFound;
		int iArrCount=0;
		String sTable = "";
		
		// Display Total Amount
		BigDecimal oDisplayTotal = BigDecimal.ZERO;
		if(m_bIsShowTotalDue)
			oDisplayTotal = oCheck.getCheckTotal().add(oCheck.getSurchargeTotal()).add(oCheck.getTipsTotal());
		else
			oDisplayTotal = oCheck.getCheckTotal();
		
		if(m_iSetCheckListingByType == FrameCheckListPanel.TYPE_CHECK_LISTING_BY_TABLE_REFERENCE) {
			for(iArrCount=0, bFound=false, sTable="" ; bFound==false && iArrCount<oCheck.getCheckExtraInfoArrayList().size(); iArrCount++ ) {
				if(oCheck.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
					&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
					&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
				) {
					sTable = oCheck.getCheckExtraInfoArrayList().get(iArrCount).getValue();
					bFound = true;
				}
			}
		}else {
			sTable = AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(oCheck.getTable()), oCheck.getTableExtension())[AppGlobal.g_oCurrentLangIndex.get()-1];
			if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.bar_tab) && oCheck.getTableExtension().length() > 0 && oCheck.getTableExtension().substring(oCheck.getTableExtension().length() - 1).compareTo(AppGlobal.BAR_TAB_TABLE_EXTENSION) == 0)
				sTable = AppGlobal.g_oLang.get()._("auto");
		}
		
		if(sType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(70);
				sFieldValues.add(sTable);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(80);
				sFieldValues.add(Integer.toString(oCheck.getGuests()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(120);
				sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), iCheckRoundDecimal));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
			}else {
				iFieldWidths.add(50);
				if(oCheck.isRevenue())
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_revenue.png");
				else
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_non_revenue.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(sTable);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(Integer.toString(oCheck.getGuests()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
				iFieldWidths.add(200);
				sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(200);
				sFieldValues.add(getUserName(oCheck.getOpenUserId()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), iCheckRoundDecimal));
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
			}
		} else if (sType.equals(FrameCheckListPanel.TYPE_PAID_CHECK)){
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(100);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(80);
				sFieldValues.add(Integer.toString(oCheck.getGuests()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(50);
				sFieldValues.add(sTable);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(120);
				sFieldValues.add(StringLib.BigDecimalToString(oDisplayTotal, iCheckRoundDecimal));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				if(!oCheck.getCheckPaymentArrayList().isEmpty()){
					iFieldWidths.add(120);
					sFieldValues.add(oCheck.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get()));
					sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
					
					if(oCheck.getCheckPaymentArrayList().get(0).getMealUserId() > 0)
						sFieldInfo1sArray.put(iIndex, getUserName(oCheck.getCheckPaymentArrayList().get(0).getMealUserId()));
					
					iIndex++;
				}
				
			}else {
				iFieldWidths.add(32);
				if(oCheck.isRevenue())
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_revenue.png");
				else
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_non_revenue.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(timeFormat.format(oCheck.getOpenLocTime().toDate()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				if(oCheck.getCloseLocTime() != null)
					sFieldValues.add(timeFormat.format(oCheck.getCloseLocTime().toDate()));
				else
					sFieldValues.add("/");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(200);
				if(oCheck.getCloseUserId() != 0)
					sFieldValues.add(getUserName(oCheck.getCloseUserId()));
				else
					sFieldValues.add("/");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(Integer.toString(oCheck.getGuests()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(StringLib.BigDecimalToString(oDisplayTotal, iCheckRoundDecimal));
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(90);
				sFieldValues.add(sTable);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				if(!oCheck.getCheckPaymentArrayList().isEmpty()){
					iFieldWidths.add(139);
					sFieldValues.add(oCheck.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get()));
					sFieldAligns.add("");
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
					
					if(oCheck.getCheckPaymentArrayList().get(0).getMealUserId() > 0)
						sFieldInfo1sArray.put(iIndex, getUserName(oCheck.getCheckPaymentArrayList().get(0).getMealUserId()));
					
					iIndex++;
				}	
			}
		} else if (sType.equals(FrameCheckListPanel.TYPE_VOID_CHECK)) {
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(85);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(getUserName(oCheck.getVoidUserId()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				if(oCheck.getVoidVdrsId() == 0)
					sFieldValues.add("");
				else
					sFieldValues.add(this.getVoidReasonName(oCheck.getVoidVdrsId()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(timeFormat.format(oCheck.getVoidLocTime().toDate()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(85);
				sFieldValues.add(StringLib.BigDecimalToString(oDisplayTotal, iCheckRoundDecimal));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
			}else {
				iFieldWidths.add(60);
				if(oCheck.isRevenue())
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_revenue.png");
				else
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_non_revenue.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				iIndex++;
				
				iFieldWidths.add(150);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
				iFieldWidths.add(170);
				sFieldValues.add(getUserName(oCheck.getVoidUserId()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(170);
				if(oCheck.getVoidVdrsId() == 0)
					sFieldValues.add("");
				else
					sFieldValues.add(this.getVoidReasonName(oCheck.getVoidVdrsId()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(timeFormat.format(oCheck.getVoidLocTime().toDate()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(100);
				sFieldValues.add(StringLib.BigDecimalToString(oDisplayTotal, iCheckRoundDecimal));
				
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(180);
				sFieldValues.add(sTable);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
			}
		}else if (sType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)) {
			String sRoom = "";
			for (PosCheckExtraInfo oCheckExtraInfo: oCheck.getCheckExtraInfoArrayList()) {
				if(oCheckExtraInfo.getVariable().equals(PosCheckExtraInfo.VARIABLE_ROOM)) {
					sRoom = oCheckExtraInfo.getValue();
					if (sRoom == null)
						sRoom = "";
					break;
				}
			}
			
			if(!m_oBusinessDayStringList.containsKey(oCheck.getBusinessDayId())){
				PosBusinessDay oBusinessDay = new PosBusinessDay();
				oBusinessDay.readById(oCheck.getBusinessDayId(), "");
				String sBussinessDay = "";
				if(oBusinessDay.getStatus() == PosBusinessDay.STATUS_NOT_RUNNING)
					sBussinessDay = oBusinessDay.getDateInStringWithFormat("yyyy-MM-dd");
				m_oBusinessDayStringList.put(oCheck.getBusinessDayId(), sBussinessDay);
			}
			
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				iFieldWidths.add(85);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
				iFieldWidths.add(100);
				sFieldValues.add(sRoom);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
				iFieldWidths.add(100);
				sFieldValues.add(m_oBusinessDayStringList.get(oCheck.getBusinessDayId()));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;

				iFieldWidths.add(85);
				sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), iCheckRoundDecimal));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				
				if(!oCheck.getCheckPaymentArrayList().isEmpty()){
					iFieldWidths.add(100);
					sFieldValues.add(oCheck.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get()));
					sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
					
					if(oCheck.getCheckPaymentArrayList().get(0).getMealUserId() > 0)
						sFieldInfo1sArray.put(iIndex, getUserName(oCheck.getCheckPaymentArrayList().get(0).getMealUserId()));
					
					iIndex++;
				}
				
			}
			else {
				iFieldWidths.add(32);
				if(oCheck.isRevenue())
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_revenue.png");
				else
					sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_non_revenue.png");
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(oCheck.getCheckPrefixNo());
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(sRoom);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(m_oBusinessDayStringList.get(oCheck.getBusinessDayId()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(StringLib.BigDecimalToString(oCheck.getCheckTotal(), iCheckRoundDecimal));
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				iFieldWidths.add(150);
				sFieldValues.add(sTable);
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				iIndex++;
				if(!oCheck.getCheckPaymentArrayList().isEmpty()){
					iFieldWidths.add(150);
					sFieldValues.add(oCheck.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get()));
					sFieldAligns.add("");
					sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
					if(oCheck.getCheckPaymentArrayList().get(0).getMealUserId() > 0)
						sFieldInfo1sArray.put(iIndex, getUserName(oCheck.getCheckPaymentArrayList().get(0).getMealUserId()));
				
					iIndex++;
				}
			}
		}
		
		m_oCheckListCommonBasket.addItem(0, iItemIndex, m_iCheckListRowHeight, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		
		
		for(Entry<Integer, String> entry:sFieldInfo1sArray.entrySet()){
			m_oCheckListCommonBasket.setFieldInfo1(0, iItemIndex, entry.getKey(), entry.getValue());
		}
		
		for(int i=0; i<sFieldValues.size(); i++)
			m_oCheckListCommonBasket.setFieldTextSize(0, iItemIndex, i, 15);
			
	}
	
	public void removeCheckListRecord() {
		m_oCheckListCommonBasket.removeAllItems(0);
	}
	
	public void addCheckListSection(int iItemIndex, String sSectionName) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		iFieldWidths.add(960);

		sFieldValues.add(sSectionName);
		
		sFieldAligns.add("");
		
		m_oCheckListCommonBasket.addItem(0, iItemIndex, 36, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oCheckListCommonBasket.setFieldBackgroundColor(0, iItemIndex, 0, "#F0FAFF");
		m_oCheckListCommonBasket.setFieldForegroundColor(0, iItemIndex, 0, "#02428B");
		m_oCheckListCommonBasket.setFieldTextSize(0, iItemIndex, 0, 14);
		m_oCheckListCommonBasket.allowClick(false);
		
	}
 
	public boolean isInFloorPlan(int iTableNo) {
		for(HashMap<String, String> oTable: m_oFloorPlanTableList) {
			if(oTable.get("table").equals(Integer.toString(iTableNo)))
				return true;
		}
		
		return false;
	}
	
	public boolean loadedRecord() {
		return this.m_bRecordLoaded;
	}
	
	// Add Check Record 
	public void updateCheckListRecord() {
		removeCheckListRecord();
		
		if (m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)) {
			
			ArrayList<PosCheck> oRemainCheckList = new ArrayList<PosCheck>();
			boolean bPrintTitle = false;
			int iCount = 0;
			
			for(Entry<Integer, PosCheck> entry: m_oDisplayCheckList.entrySet()) {
				PosCheck oCheck = entry.getValue();
				
				if (!isInFloorPlan(oCheck.getTable())) {
					oRemainCheckList.add(oCheck);
					continue;
				}
				
				if(!bPrintTitle) {
					addCheckListSection(iCount++, AppGlobal.g_oLang.get()._("current_floor_plan"));
					bPrintTitle = true;
				}
				
				this.addCheckListRecord(FrameCheckListPanel.TYPE_OPEN_CHECK, iCount, oCheck, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal());
				iCount++;
			}
			
			if(!oRemainCheckList.isEmpty()) {
				addCheckListSection(iCount++, AppGlobal.g_oLang.get()._("other_floor_plan"));
				for(PosCheck oCheck: oRemainCheckList) {
					this.addCheckListRecord(FrameCheckListPanel.TYPE_OPEN_CHECK, iCount, oCheck, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal());
					iCount++;
				}
			}
			
			// initialize scroll index to zero
			m_iScrollIndex = 0;
			m_oCheckListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
		} else {
			int iCount = 0;
			for (int i = m_iCurrentPageStartNo; i < m_iCurrentPageStartNo+m_iPageRecordCount && i < m_oDisplayCheckList.size(); i++) {
				this.addCheckListRecord(m_sPanelType, iCount, m_oDisplayCheckList.get(i), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal());
				iCount++;
			}
		}
		
		updatePageUpDownVisibility();
	}
	
	public void setDisplayCheckList() {
		m_iCurrentPageStartNo = 0;
		m_oDisplayCheckList.clear();
		for(Entry<Integer, PosCheck> entry: m_oCheckList.entrySet()) {
			m_oDisplayCheckList.put(entry.getKey(), entry.getValue());
		}
		//m_oDisplayCheckList = m_oCheckList;
	}
	
	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = 0;
		
		if(m_sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK)||m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)) {
			if(m_sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)){
				iCurrentPanelRecordCount = m_oDisplayCheckList.size();
			}
			else{
				iCurrentPanelRecordCount = m_oCheckListCommonBasket.getItemCount(0);
			}

			if(iCurrentPanelRecordCount > m_iPageRecordCount)
				iPage = (m_iCurrentPageStartNo/m_iPageRecordCount)+1;
			
			if(m_iCurrentPageStartNo > 0)
				bShowPageUp = true;
			
			if(iCurrentPanelRecordCount > m_iPageRecordCount && m_iCurrentPageStartNo+m_iPageRecordCount < iCurrentPanelRecordCount)
				bShowPageDown = true;
		}

		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}
	
	public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}
	
	public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}
	
	public void setPageNumber(int iNumber) {
		int iTotalPage = 0;
		if(iNumber > 0) {
			if(m_sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)){
				iTotalPage = (int)Math.ceil(1.0*m_oDisplayCheckList.size()/m_iPageRecordCount);
			}
			else if(m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)){
				iTotalPage = (int)Math.ceil(1.0*m_oCheckListCommonBasket.getItemCount(0)/m_iPageRecordCount);
			}
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber + " / " + iTotalPage);
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}
	
	public void clearCheckNoLabel() {
		m_oLabelCheckNo.setValue("");
		m_oLabelCheckNo.setVisible(false);
	}

	public void updateSearchButtonColor() {
		String sUnselectedBackgroundColor = "#A0B3B7";
		String sSelectedBackgroundColor = "#0055B8";
		
		if(!m_oLabelCheckNo.getValue().isEmpty()) {
			m_oButtonSearch.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonViewAll.setBackgroundColor(sUnselectedBackgroundColor);
		} else {
			m_oButtonViewAll.setBackgroundColor(sSelectedBackgroundColor);
			m_oButtonSearch.setBackgroundColor(sUnselectedBackgroundColor);
		}
	}
	
	public void setContinueButtonVisible(boolean bShow) {
		m_oButtonContinue.setVisible(bShow);
	}
	
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iPageRecordCount >= 0) {
				m_iCurrentPageStartNo -= m_iPageRecordCount;
				if(m_sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)){
					updateCheckListRecord();
				}
				else if(m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)){
					updatePageUpDownVisibility();
					m_iScrollIndex -= m_iPageRecordCount;
					m_oCheckListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
				}
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			 // PAGE DOWN
			if(m_sPanelType.equals(FrameCheckListPanel.TYPE_PAID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_VOID_CHECK) || m_sPanelType.equals(FrameCheckListPanel.TYPE_PAST_DATE_CHECK)){
				if(m_iCurrentPageStartNo+m_iPageRecordCount < m_oCheckList.size()) {
					m_iCurrentPageStartNo += m_iPageRecordCount;
					updateCheckListRecord();
				}
			}
			else if(m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)){
				if(m_iCurrentPageStartNo+m_iPageRecordCount < m_oCheckListCommonBasket.getItemCount(0)) {
					m_iCurrentPageStartNo += m_iPageRecordCount;
					updatePageUpDownVisibility();
				}	
				m_iScrollIndex += m_iPageRecordCount;
				m_oCheckListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oButtonSearch.getId()) {
			// SEARCH
			clearCheckNoLabel();
			String sInputBoxReturnValue;
			boolean bSelectBoxSelectedByTableReference = false;
			while(true){
				
				//Check listing by table ref: choose search method
				if(m_iSetCheckListingByType == FrameCheckListPanel.TYPE_CHECK_LISTING_BY_TABLE_REFERENCE) {
					ArrayList<String> oOptionList = new ArrayList<String>();
					oOptionList.add(AppGlobal.g_oLang.get()._("search_by_table_ref"));
					oOptionList.add(AppGlobal.g_oLang.get()._("search_by_check_no"));
					FormSelectionBox oFormSelectionBox = new FormSelectionBox(this.getParentForm());
					oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_search_method"), oOptionList, false);
					oFormSelectionBox.show();
					if(oFormSelectionBox.isUserCancel()) {
						bMatchChild = true;
						return bMatchChild;
					}else
						if(oFormSelectionBox.getResultList().get(0) == 0)
							bSelectBoxSelectedByTableReference = true;
				}
				
				// Ask for search input
				FormInputBox oFormInputBox = new FormInputBox(this.getParentForm());
				oFormInputBox.init();
				oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DEFAULT);
				oFormInputBox.showKeyboard();
				if(bSelectBoxSelectedByTableReference) {
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("table_ref"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_table_ref")+":");
				}else {
					oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("check_no"));
					oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_check_no")+":");
				}
				oFormInputBox.show();
				if(oFormInputBox.isUserCancel()) {
					bMatchChild = true;
					return bMatchChild;
				}
				
				sInputBoxReturnValue = oFormInputBox.getInputValue();
				if(sInputBoxReturnValue == null)
					return false;
				else if(sInputBoxReturnValue.isEmpty()) {
					String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
					FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
					oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
					oFormDialogBox.setMessage(sErrMsg);
					oFormDialogBox.show();
					oFormDialogBox = null;
				} else {
					/*try{
						Integer.valueOf(sCheckNo);
					}catch(NumberFormatException e){
						String sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
						FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
						oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
						oFormDialogBox.setMessage(sErrMsg);
						oFormDialogBox.show();
						return false;
					}*/
					
					break;
				}
			}
			if(bSelectBoxSelectedByTableReference) {
				m_oLabelCheckNo.setValue(AppGlobal.g_oLang.get()._("table_ref")+": "+sInputBoxReturnValue);
				m_oLabelCheckNo.setVisible(true);
				this.searchCheck(sInputBoxReturnValue, COLUMN_HEADER_TABLE_REFERENCE);
			}else{
				m_oLabelCheckNo.setValue(AppGlobal.g_oLang.get()._("check_no")+": "+sInputBoxReturnValue);
				m_oLabelCheckNo.setVisible(true);
				this.searchCheck(sInputBoxReturnValue, COLUMN_HEADER_CHECK);
			}
			
			
			this.updateSearchButtonColor();
			bMatchChild = true;
		} else if (iChildId == m_oButtonViewAll.getId()) {
			// VIEW ALL
			this.clearCheckNoLabel();
			this.setDisplayCheckList();
			this.updateCheckListRecord();
			
			this.updateSearchButtonColor();
			
			bMatchChild = true;
		} else if (iChildId == this.m_oButtonContinue.getId()) {
			for (FrameCheckListPanelListener listener : listeners)
				listener.frameCheckListPanel_ButtonContinueClicked();
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	public void searchCheck(String sSearchRef, String sRefType) {
		removeCheckListRecord();
		m_oDisplayCheckList.clear();

		int iCount = 0;
		int iArrCount;
		boolean bFound = false;
		if(m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK)) {
			ArrayList<PosCheck> oRemainCheckList = new ArrayList<PosCheck>();
			boolean bPrintTitle = false;
			for(Entry<Integer, PosCheck> entry: m_oCheckList.entrySet()) {
				PosCheck oCheck = entry.getValue();
				bFound=false;
				//check if table ref match search ref
				if(sRefType==COLUMN_HEADER_TABLE_REFERENCE && !oCheck.getCheckExtraInfoArrayList().isEmpty()) {
					for(iArrCount=0, bFound = false; iArrCount < oCheck.getCheckExtraInfoArrayList().size() && bFound==false; iArrCount++) {
						if(oCheck.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getValue().contains(sSearchRef)
						)
							bFound = true;
					}
				}
				//check if check no match search ref
				else if (sRefType==COLUMN_HEADER_CHECK && oCheck.getCheckPrefixNo().compareTo(sSearchRef) == 0)
					bFound = true;
				
				if(bFound==true) {
					if (!isInFloorPlan(oCheck.getTable())) {
						oRemainCheckList.add(oCheck);
						continue;
					}
					
					if(!bPrintTitle) {
						iCount++;
						bPrintTitle = true;
					}
					m_oDisplayCheckList.put(iCount, oCheck);
					iCount++;
				}
			}
			
			if(!oRemainCheckList.isEmpty()) {
				iCount++;
				for(PosCheck oCheck: oRemainCheckList) {
					m_oDisplayCheckList.put(iCount, oCheck);
					iCount++;
				}
			}
		} else {
			for(Entry<Integer, PosCheck> entry: m_oCheckList.entrySet()) {
				PosCheck oCheck = entry.getValue();
				bFound=false;
				//check if table ref match search ref
				if(sRefType==COLUMN_HEADER_TABLE_REFERENCE && !oCheck.getCheckExtraInfoArrayList().isEmpty()) {
					for(iArrCount=0 ; iArrCount < oCheck.getCheckExtraInfoArrayList().size() && bFound==false ; iArrCount++) {
						if(oCheck.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
							&& oCheck.getCheckExtraInfoArrayList().get(iArrCount).getValue().contains(sSearchRef)
						) 
							bFound = true;
				
					}
				}
				//check if check no match search ref
				else if (sRefType==COLUMN_HEADER_CHECK && oCheck.getCheckPrefixNo().compareTo(sSearchRef) == 0) {
					bFound = true;
				}
				
				if(bFound==true) {
					m_oDisplayCheckList.put(iCount, oCheck);
					iCount++;
				}
				
			}
		}
		
		m_iCurrentPageStartNo = 0;
		updateCheckListRecord();
		
		updatePageUpDownVisibility();
	}
	
	private void sortRecord(String sColumnHeader) {
		String sSortOrder = m_oColumnHeaderSortStatusList.get(sColumnHeader);
		List<PosCheck> oNewDisplayCheckList = new ArrayList<PosCheck>(m_oDisplayCheckList.values());

		// reset all column header sort to none
		for(String sSortKey: m_oColumnHeaderSortStatusList.keySet()) {
			m_oColumnHeaderSortStatusList.put(sSortKey, SORT_ORDER_NONE);
		}
		
		// update current column header
		if(sSortOrder.equals(SORT_ORDER_ASCENDING))
			this.m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_DESCENDING);
		else 
			this.m_oColumnHeaderSortStatusList.put(sColumnHeader, SORT_ORDER_ASCENDING);
		sSortOrder = m_oColumnHeaderSortStatusList.get(sColumnHeader);
			
		// According to to column header user clicked, sort the results
		if (sColumnHeader.equals(COLUMN_HEADER_TABLE)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						int iTableDiff = oPosCheck1.getTable() - oPosCheck2.getTable();
						if(iTableDiff == 0)
							iTableDiff = oPosCheck1.getTableExtension().compareTo(oPosCheck2.getTableExtension());
						return iTableDiff;
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						int iTableDiff = oPosCheck2.getTable() - oPosCheck1.getTable();
						if(iTableDiff == 0)
							iTableDiff = oPosCheck2.getTableExtension().compareTo(oPosCheck1.getTableExtension());
						return iTableDiff;
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_CHECK)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return oPosCheck1.getCheckNo()-oPosCheck2.getCheckNo();
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return oPosCheck2.getCheckNo()-oPosCheck1.getCheckNo();
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_GUEST)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return oPosCheck1.getGuests()-oPosCheck2.getGuests();
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return oPosCheck2.getGuests()-oPosCheck1.getGuests();
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_CHECK_TOTAL)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return (oPosCheck1.getCheckTotal().subtract(oPosCheck2.getCheckTotal())).intValue();
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						return (oPosCheck2.getCheckTotal().subtract(oPosCheck1.getCheckTotal())).intValue();
					}
				});
			}
		}else if (sColumnHeader.equals(COLUMN_HEADER_DAY)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String sDay1 = m_oBusinessDayStringList.get(oPosCheck1.getBusinessDayId());
						String sDay2 = m_oBusinessDayStringList.get(oPosCheck2.getBusinessDayId());
						try{
							Date oBusinessDay1 = formatter.parse(sDay1);
							Date oBusinessDay2 = formatter.parse(sDay2);
							return oBusinessDay1.compareTo(oBusinessDay2);	
						}catch(Exception e){
							return 0;
						}
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String sDay1 = m_oBusinessDayStringList.get(oPosCheck1.getBusinessDayId());
						String sDay2 = m_oBusinessDayStringList.get(oPosCheck2.getBusinessDayId());
						try{
							Date oBusinessDay1 = formatter.parse(sDay1);
							Date oBusinessDay2 = formatter.parse(sDay2);
							return oBusinessDay2.compareTo(oBusinessDay1);	
						}catch(Exception e){
							return 0;
						}
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_OPEN_TIME)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckOpenTime1 = oPosCheck1.getOpenLocTime().toLocalDateTime();
						LocalDateTime oCheckOpenTime2 = oPosCheck2.getOpenLocTime().toLocalDateTime();
						return oCheckOpenTime1.compareTo(oCheckOpenTime2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckOpenTime1 = oPosCheck1.getOpenLocTime().toLocalDateTime();
						LocalDateTime oCheckOpenTime2 = oPosCheck2.getOpenLocTime().toLocalDateTime();
						return oCheckOpenTime2.compareTo(oCheckOpenTime1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_OPEN_USER)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sOpenUser1 = getUserName(oPosCheck1.getOpenUserId());
						String sOpenUser2 = getUserName(oPosCheck2.getOpenUserId());
						return sOpenUser1.compareTo(sOpenUser2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sOpenUser1 = getUserName(oPosCheck1.getOpenUserId());
						String sOpenUser2 = getUserName(oPosCheck2.getOpenUserId());
						return sOpenUser2.compareTo(sOpenUser1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_CLOSE_TIME)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckCloseTime1 = oPosCheck1.getCloseLocTime().toLocalDateTime();
						LocalDateTime oCheckCloseTime2 = oPosCheck2.getCloseLocTime().toLocalDateTime();
						return oCheckCloseTime1.compareTo(oCheckCloseTime2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckCloseTime1 = oPosCheck1.getCloseLocTime().toLocalDateTime();
						LocalDateTime oCheckCloseTime2 = oPosCheck2.getCloseLocTime().toLocalDateTime();
						return oCheckCloseTime2.compareTo(oCheckCloseTime1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_CLOSE_USER)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sCloseUser1 = getUserName(oPosCheck1.getCloseUserId());
						String sCloseUser2 = getUserName(oPosCheck2.getCloseUserId());
						return sCloseUser1.compareTo(sCloseUser2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sCloseUser1 = getUserName(oPosCheck1.getCloseUserId());
						String sCloseUser2 = getUserName(oPosCheck2.getCloseUserId());
						return sCloseUser2.compareTo(sCloseUser1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_VOID_TIME)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckVoidTime1 = oPosCheck1.getVoidLocTime().toLocalDateTime();
						LocalDateTime oCheckVoidTime2 = oPosCheck2.getVoidLocTime().toLocalDateTime();
						return oCheckVoidTime1.compareTo(oCheckVoidTime2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						LocalDateTime oCheckVoidTime1 = oPosCheck1.getVoidLocTime().toLocalDateTime();
						LocalDateTime oCheckVoidTime2 = oPosCheck2.getVoidLocTime().toLocalDateTime();
						return oCheckVoidTime2.compareTo(oCheckVoidTime1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_VOID_USER)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sVoidUser1 = getUserName(oPosCheck1.getVoidUserId());
						String sVoidUser2 = getUserName(oPosCheck2.getVoidUserId());
						return sVoidUser1.compareTo(sVoidUser2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sVoidUser1 = getUserName(oPosCheck1.getVoidUserId());
						String sVoidUser2 = getUserName(oPosCheck2.getVoidUserId());
						return sVoidUser2.compareTo(sVoidUser1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_VOID_REASON)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sVoidReason1 = "";
						String sVoidReason2 = "";
						if(m_oVoidReasonList.containsKey(oPosCheck1.getVoidVdrsId()))
							sVoidReason1 = m_oVoidReasonList.get(oPosCheck1.getVoidVdrsId()).getName(AppGlobal.g_oCurrentLangIndex.get());
						if(m_oVoidReasonList.containsKey(oPosCheck2.getVoidVdrsId()))
							sVoidReason2 = m_oVoidReasonList.get(oPosCheck2.getVoidVdrsId()).getName(AppGlobal.g_oCurrentLangIndex.get());
						return sVoidReason1.compareTo(sVoidReason2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sVoidReason1 = "";
						String sVoidReason2 = "";
						if(m_oVoidReasonList.containsKey(oPosCheck1.getVoidVdrsId()))
							sVoidReason1 = m_oVoidReasonList.get(oPosCheck1.getVoidVdrsId()).getName(AppGlobal.g_oCurrentLangIndex.get());
						if(m_oVoidReasonList.containsKey(oPosCheck2.getVoidVdrsId()))
							sVoidReason2 = m_oVoidReasonList.get(oPosCheck2.getVoidVdrsId()).getName(AppGlobal.g_oCurrentLangIndex.get());
						return sVoidReason2.compareTo(sVoidReason1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_PAYMENT_METHOD)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sPaymentMethod1 = oPosCheck1.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get());
						String sPaymentMethod2 = oPosCheck2.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get());
						return sPaymentMethod1.compareTo(sPaymentMethod2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						String sPaymentMethod1 = oPosCheck1.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get());
						String sPaymentMethod2 = oPosCheck2.getCheckPaymentArrayList().get(0).getName(AppGlobal.g_oCurrentLangIndex.get());
						return sPaymentMethod2.compareTo(sPaymentMethod1);
					}
				});
			}
		} else if (sColumnHeader.equals(COLUMN_HEADER_TABLE_REFERENCE)) {
			if(sSortOrder.equals(SORT_ORDER_ASCENDING)) {
				
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						int iArrCount;
						boolean bFound;
						String sTabeReference1 = "";
						String sTabeReference2 = "";
						
						bFound = false;
						if(!oPosCheck1.getCheckExtraInfoArrayList().isEmpty()) {
							for(iArrCount=0, bFound=false; bFound==false && iArrCount<oPosCheck1.getCheckExtraInfoArrayList().size(); iArrCount++ ) {
								if(oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
									&& oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
									&& oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
								) {
									sTabeReference1 = oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getValue();
									bFound = true;
								}
							}
						}
//						if(bFound==false) {
//							sTabeReference1="";
//						}
						
						bFound = false;
						if(!oPosCheck2.getCheckExtraInfoArrayList().isEmpty()) {
							for(iArrCount=0, bFound=false; bFound==false && iArrCount<oPosCheck2.getCheckExtraInfoArrayList().size(); iArrCount++ ) {
								if(oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
									&& oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
									&& oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
								) {
									sTabeReference2 = oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getValue();
									bFound = true;
								}
							}
						}
//						if(bFound==false) {
//							sTabeReference2="";
//						}
						return sTabeReference1.compareTo(sTabeReference2);
					}
				});
			} else {
				Collections.sort(oNewDisplayCheckList, new Comparator<PosCheck>() {
					@Override
					public int compare(PosCheck oPosCheck1, PosCheck oPosCheck2) {
						int iArrCount;
						boolean bFound;
						String sTabeReference1 = "";
						String sTabeReference2 = "";
						
						bFound = false;
						if(!oPosCheck1.getCheckExtraInfoArrayList().isEmpty()) {
							for(iArrCount=0, bFound=false; bFound==false && iArrCount<oPosCheck1.getCheckExtraInfoArrayList().size(); iArrCount++ ) {
								if(oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
									&& oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
									&& oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
								) {
									sTabeReference1 = oPosCheck1.getCheckExtraInfoArrayList().get(iArrCount).getValue();
									bFound = true;
								}
							}
						}
//						if(bFound==false) {
//							sTabeReference1="";
//						}
						
						bFound = false;
						if(!oPosCheck2.getCheckExtraInfoArrayList().isEmpty()) {
							for(iArrCount=0, bFound=false; bFound==false && iArrCount<oPosCheck2.getCheckExtraInfoArrayList().size(); iArrCount++ ) {
								if(oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getBy().equals(PosCheckExtraInfo.BY_CHECK) 
									&& oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getSection().equals(PosCheckExtraInfo.SECTION_TABLE_INFORMATION)
									&& oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getVariable().equals(PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE)
								) {
									sTabeReference2 = oPosCheck2.getCheckExtraInfoArrayList().get(iArrCount).getValue();
									bFound = true;
								}
							}
						}
						return sTabeReference2.compareTo(sTabeReference1);
					}
				});
			}
		}
		
		m_oDisplayCheckList.clear();
		boolean bPrintTitle = false;
		int iCount = 0;
		List<PosCheck> oRemainCheckList = new ArrayList<PosCheck>();
		for(PosCheck oPosCheck: oNewDisplayCheckList) {
			if(m_sPanelType.equals(TYPE_OPEN_CHECK)) {
				if(!this.isInFloorPlan(oPosCheck.getTable())) {
					oRemainCheckList.add(oPosCheck);
					continue;
				}
				
				if(!bPrintTitle) {
					iCount++;
					bPrintTitle = true;
				}
			}
			
			m_oDisplayCheckList.put(iCount, oPosCheck);
			iCount++;
		}

		if(m_sPanelType.equals(TYPE_OPEN_CHECK)) {
			// reset page number
			m_iCurrentPageStartNo = 0;
			
			// add other floor plan table to m_oDisplayCheckList. One iCount will be skip for the tab "other floor plan"
			if(!oRemainCheckList.isEmpty()){
				for(PosCheck oPosCheck: oRemainCheckList)
					m_oDisplayCheckList.put(++iCount, oPosCheck);
			}
		}
		
		updateCheckListRecord();
	}
	
	private String getUserName(int iId) {
		if (m_oUserNameList.containsKey(iId))
			return m_oUserNameList.get(iId);
		
		UserUser oUser = new UserUser();
		oUser.readByUserId(iId);
		String sUserName = oUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUser.getLastName(AppGlobal.g_oCurrentLangIndex.get());
		
		m_oUserNameList.put(iId, sUserName);
		return sUserName;
	}
	
	public void setTotalCheck(int iTotalCheck){
		m_oLabelTotalCheck.setValue(AppGlobal.g_oLang.get()._("no_of_checks") + ": " + iTotalCheck);
	}
	
	public void setTotalAmount(String sTotalAmount){
		m_oLableTotalAmount.setValue(AppGlobal.g_oLang.get()._("total") + ": "+ sTotalAmount);
	}
	
	private String getVoidReasonName(int iVoidReasonId) {
		if(m_oVoidReasonList.containsKey(iVoidReasonId))
			return m_oVoidReasonList.get(iVoidReasonId).getName(AppGlobal.g_oCurrentLangIndex.get());
		else
			return "";
	}
	
	public void setCheckListingByType(int iSetCheckListingByType) {
		//Choose between check listing by table ref or table no
		m_iSetCheckListingByType = iSetCheckListingByType;
		if(m_iSetCheckListingByType == FrameCheckListPanel.TYPE_CHECK_LISTING_BY_TABLE_REFERENCE) {
			m_sTableTitleRefType = AppGlobal.g_oLang.get()._("table_ref");
			m_sTableColumnRefType = COLUMN_HEADER_TABLE_REFERENCE;
			m_oButtonSearch.setValue(AppGlobal.g_oLang.get()._("search"));
		}
		else {
			m_sTableTitleRefType = AppGlobal.g_oLang.get()._("table_no");
			m_sTableColumnRefType = COLUMN_HEADER_TABLE;
		}
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		
		PosCheck oCheck = new PosCheck();
		// For the open check listing, get the selected item index of the display list directly
		if (m_sPanelType.equals(FrameCheckListPanel.TYPE_OPEN_CHECK))
			oCheck = m_oDisplayCheckList.get(iItemIndex);
		else
			oCheck = m_oDisplayCheckList.get(m_iCurrentPageStartNo+iItemIndex);
		
		if(oCheck != null) {
			for (FrameCheckListPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameCheckListPanel_CheckListRecordClicked(oCheck, m_iCheckListingType);
			}
		}
	}
	
	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		String sHeaderField = m_oColumnHeaderList.get(iFieldIndex);
		this.sortRecord(sHeaderField);
	}
	
	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
}
