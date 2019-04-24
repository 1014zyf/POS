package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FormDialogBox;
import commonui.FormInputBox;
import commonui.FormSelectionBox;
import externallib.StringLib;
import externallib.Util;
import om.MenuItemCourse;
import om.MenuItemCourseList;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCustomType;
import om.PosCustomTypeList;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameCheckFunctionListener {
	FuncCheckItem frameCheckFunction_moveItemToOtherSeat(int iOriSeatNo, FuncCheckItem oOriFuncCheckItem, int iNewSeatNo, BigDecimal dChgQty);
	boolean frameCheckFunction_changeItemCourse(int iTargetSeatNo, FuncCheckItem oTargetFuncCheckItem, int iNewCourseId);
	boolean frameCheckFunction_checkMinimumChargeItem(FuncCheckItem oFuncCheckItem);
	void frameCheckFunction_clickTable();
	void frameCheckFunction_clickCover();
	void frameCheckFunction_exit();
}

public class FrameCheckFunction extends VirtualUIFrame implements FrameColumnViewListener, FrameCheckDetailListener, FrameSeatPanelListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FuncCheck m_oFuncCheck;
	private MenuItemCourseList m_oMenuItemCourseList;
	private AppGlobal.OPERATION_MODE m_eOperationMode;
	
	private VirtualUIFrame m_oFrameMainFrame;
	private VirtualUIFrame m_oFrameRightMainFrame;
	
	private VirtualUILabel m_oLabelHeader;
	private VirtualUIFrame m_oFrameFunctionPanel;
	private FrameCheckDetail m_oFrameHeader;
	private FrameCheckDetailInfo m_oFrameCheckDetailInfo;
	private FrameColumnView m_oFrameColumnView;
	private FrameSeatPanel m_oFrameSeatPanel;
	private FrameCheckDiscountInfo m_oFrameCheckDiscountInfo;
	//private int m_iDiscountShow;	  //0: no show	1: show already
	
	private VirtualUIButton m_oButtonViewBySeat;
	private VirtualUIButton m_oButtonViewByCourse;
	private VirtualUIButton m_oButtonViewWholeCheck;
	private VirtualUIFrame m_oFrameHeaderUnderLine;
	
	private int m_iShowMode;		// 0: View whole check		1: View by seat no.		2: View by course no.
	
	private HashMap<Integer, Integer> m_oColumnSeatList;
	private HashMap<Integer, Integer> m_oSectionIndexCourseIdMap;
	
	private HashMap<Integer, Integer> m_oColumnCourseList;
	private HashMap<Integer, Integer> m_oSectionIndexSeatNoMap;
	
	private PosCustomTypeList m_oPosCustomTypeList;
	
	private final static String BACKGROUND_COLOR_UNSELECT = "#00000000";
	private final static String FOREGROUND_COLOR_UNSELECT = "#000000";
	private final static String BACKGROUND_COLOR_SELECT = "#0055B8";
	private final static String FOREGROUND_COLOR_SELECT = "#FFFFFFFF";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameCheckFunctionListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCheckFunctionListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCheckFunctionListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameCheckFunction(PosCustomTypeList oPosCustomTypeList) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckFunctionListener>();
		m_oColumnSeatList = new HashMap<Integer, Integer>();
		m_oSectionIndexCourseIdMap = new HashMap<Integer, Integer>();
		m_oColumnCourseList = new HashMap<Integer, Integer>();
		m_oSectionIndexSeatNoMap = new HashMap<Integer, Integer>();
		
		if (oPosCustomTypeList != null)
			m_oPosCustomTypeList = oPosCustomTypeList;
		else {
			m_oPosCustomTypeList = new PosCustomTypeList();
			m_oPosCustomTypeList.getCustomTypesByType(PosCustomType.TYPE_CHECK);
		}
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckFunction.xml");
		
		// Header
		m_oLabelHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelHeader, "lblHeader");
		this.attachChild(m_oLabelHeader);
		
		//Main frame for detail and column view
		m_oFrameMainFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameMainFrame, "fraMainFrame");
		this.attachChild(m_oFrameMainFrame);
		
		m_oFrameRightMainFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightMainFrame, "fraRightMainFrame");
		m_oFrameRightMainFrame.setVisible(true);
		this.attachChild(m_oFrameRightMainFrame);
		
		// Function panel
		m_oFrameFunctionPanel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameFunctionPanel, "fcnPanel");
		m_oFrameRightMainFrame.attachChild(m_oFrameFunctionPanel);
		m_oFrameFunctionPanel.setVisible(true);
		
		// Header
		m_oFrameHeader = new FrameCheckDetail();
		m_oTemplateBuilder.buildFrame(m_oFrameHeader, "fraHeader");
		m_oFrameHeader.createFunctionButtons(FrameCheckDetail.FOR_FRAME_CHECK_FUNCTION);
		m_oFrameHeader.addListener(this);
		m_oFrameRightMainFrame.attachChild(m_oFrameHeader);
		
		// Underline for the header
		m_oFrameHeaderUnderLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameHeaderUnderLine, "fraHeaderUnderLine");
		m_oFrameHeader.attachChild(m_oFrameHeaderUnderLine);
		
		// Attach check detail info. frame
		m_oFrameCheckDetailInfo = new FrameCheckDetailInfo(FrameCheckDetailInfo.TYPE_FRAME_CHECK_DETAIL_INFO_BY_CHECK_FUNCTION);
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDetailInfo, "fraCheckDetailInfo");
		m_oFrameMainFrame.attachChild(m_oFrameCheckDetailInfo);
		
		// Column view
		m_oFrameColumnView = null;
		
		// Seat panel for view by seat
		m_oFrameSeatPanel = new FrameSeatPanel(FrameSeatPanel.TYPE_SEAT_PANEL);
		m_oTemplateBuilder.buildFrame(m_oFrameSeatPanel, "fraSeatPanel");
		m_oFrameSeatPanel.setVisible(false);
		m_oFrameSeatPanel.setCourseDescription(AppGlobal.g_oLang.get()._("undefined_course", ""));
		// Add Seat to Seat Panel
		m_oFrameSeatPanel.addNumberOfSeat(AppGlobal.MAX_SEATS);
		m_oFrameSeatPanel.addListener(this);
		this.attachChild(m_oFrameSeatPanel);
		
		// View by seat button
		m_oButtonViewBySeat = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonViewBySeat, "btnViewBySeat");
		m_oButtonViewBySeat.setValue(AppGlobal.g_oLang.get()._("view_by_seat", ""));
		m_oButtonViewBySeat.setVisible(true);
		this.attachChild(m_oButtonViewBySeat);
		
		// View by course button
		m_oButtonViewByCourse = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonViewByCourse, "btnViewByCourse");
		m_oButtonViewByCourse.setValue(AppGlobal.g_oLang.get()._("view_by_course_no", ""));
		m_oButtonViewByCourse.setVisible(true);
		this.attachChild(m_oButtonViewByCourse);
		
		// View whole check
		m_oButtonViewWholeCheck = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonViewWholeCheck, "btnViewWholeCheck");
		m_oButtonViewWholeCheck.setValue(AppGlobal.g_oLang.get()._("view_whole_check", ""));
		m_oButtonViewWholeCheck.setVisible(true);
		this.attachChild(m_oButtonViewWholeCheck);
		
		m_oButtonViewByCourse.setBackgroundColor(BACKGROUND_COLOR_UNSELECT);
		m_oButtonViewByCourse.setForegroundColor(FOREGROUND_COLOR_UNSELECT);
		m_oButtonViewBySeat.setBackgroundColor(BACKGROUND_COLOR_UNSELECT);
		m_oButtonViewBySeat.setForegroundColor(FOREGROUND_COLOR_UNSELECT);
		
		// Discount information
		//m_iDiscountShow = 0;
		m_oFrameCheckDiscountInfo = new FrameCheckDiscountInfo();
		m_oTemplateBuilder.buildFrame(m_oFrameCheckDiscountInfo, "fraCheckDiscountInfo");
		m_oFrameCheckDiscountInfo.init();
		m_oFrameRightMainFrame.attachChild(m_oFrameCheckDiscountInfo);
		
		m_iShowMode = -1;
	}
	
	// Init check detail
	public void initCheckInfo() {
		m_oFrameCheckDetailInfo.clearDetailList();
		
		if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.fast_food) || m_eOperationMode.equals(AppGlobal.OPERATION_MODE.self_order_kiosk)) 
			m_oFrameHeader.setTableFrameVisible(false);
		else {
			m_oFrameHeader.setTableFrameVisible(true);
			String [] sTableName = m_oFuncCheck.getTableName();
			if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.bar_tab) && m_oFuncCheck.getTableExtension().length() > 0 && m_oFuncCheck.getTableExtension().substring(m_oFuncCheck.getTableExtension().length() - 1).compareTo(AppGlobal.BAR_TAB_TABLE_EXTENSION) == 0)
				sTableName = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, AppGlobal.g_oLang.get()._("auto"));
			m_oFrameHeader.setTableNo(sTableName);
		}
		m_oFrameHeader.setCover(m_oFuncCheck.getCover());
		
		DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.fast_food))
			m_oFrameCheckDetailInfo.setTableNumber(AppGlobal.g_oLang.get()._("fast_food_mode", ""));
		else if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.self_order_kiosk))
			m_oFrameCheckDetailInfo.setTableNumber(AppGlobal.g_oLang.get()._("self_order_kiosk_mode", ""));
		else {
			String [] sTableName = m_oFuncCheck.getTableName();
			if(m_eOperationMode.equals(AppGlobal.OPERATION_MODE.bar_tab) && m_oFuncCheck.getTableExtension().length() > 0 && m_oFuncCheck.getTableExtension().substring(m_oFuncCheck.getTableExtension().length() - 1).compareTo(AppGlobal.BAR_TAB_TABLE_EXTENSION) == 0)
				sTableName = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, AppGlobal.g_oLang.get()._("auto"));
			String sTableRef = m_oFuncCheck.getCheckExtraInfoBySectionAndVariable(PosCheckExtraInfo.SECTION_TABLE_INFORMATION, PosCheckExtraInfo.VARIABLE_TABLE_REFERENCE, 0);
			if (m_eOperationMode.equals(AppGlobal.OPERATION_MODE.bar_tab) && sTableRef != null && !sTableRef.isEmpty())
				sTableName = StringLib.appendStringArray(sTableName, " (", sTableRef, ")");
			m_oFrameCheckDetailInfo.setTableNumber(sTableName);
		}
		if(((m_eOperationMode.equals(AppGlobal.OPERATION_MODE.fast_food) && !AppGlobal.g_oFuncOutlet.get().getBusinessDay().isCheckNumGeneratedByOutlet()) || AppGlobal.g_oFuncSmartStation.isStandaloneRole()) && !m_oFuncCheck.isOldCheck())
			m_oFrameCheckDetailInfo.setCheckNumber(AppGlobal.g_oFuncStation.get().getNextCheckPrefixNumber(false));
		else
			m_oFrameCheckDetailInfo.setCheckNumber(m_oFuncCheck.getCheckPrefixNo());
		m_oFrameCheckDetailInfo.setCover(String.valueOf(m_oFuncCheck.getCover()));
		if(m_oFuncCheck.isOldCheck()) {
			m_oFrameCheckDetailInfo.setOpenEmployee(m_oFuncCheck.getOpenUserName());
			m_oFrameCheckDetailInfo.setOpenTime(timeFormatter.print(m_oFuncCheck.getOpenLocTime()));
		}
		if(m_oFuncCheck.hasMember()) {
			m_oFrameCheckDetailInfo.setCheckMember(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, m_oFuncCheck.getMemberDisplayName()), m_oFuncCheck.getMemberNumber());
		}else {
			// For interface member
			String sMemberName = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NAME);
			String sEnglishName = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_ENGLISH_NAME);
			String sMemberNumber = m_oFuncCheck.getCheckExtraInfoValueListBySectionVariable(PosCheckExtraInfo.SECTION_MEMBERSHIP_INTERFACE, PosCheckExtraInfo.VARIABLE_MEMBER_NUMBER);
			
			String sDisplayName = sMemberName;
			if (sDisplayName == null || sDisplayName.isEmpty())
				sDisplayName = sEnglishName;
			else if (sEnglishName != null && !sEnglishName.isEmpty())
				sDisplayName += ", " + sEnglishName;
			
			if(sDisplayName !=null && sMemberNumber != null && !sDisplayName.isEmpty() && !sMemberNumber.isEmpty())
				m_oFrameCheckDetailInfo.setCheckMember(StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, sDisplayName), sMemberNumber);
		}
		m_oFrameCheckDetailInfo.setPrintCount(String.valueOf(m_oFuncCheck.getPrintCount()));
		m_oFrameCheckDetailInfo.setItemCount(m_oFuncCheck.getOrderedItemCount(true).toPlainString());
		m_oFrameCheckDetailInfo.setNewItemCount(m_oFuncCheck.getNewItemCount(true).toPlainString());
		BigDecimal dScTotal = m_oFuncCheck.getServiceChargeTotal();
		m_oFrameCheckDetailInfo.setGratuityTotal(AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(m_oFuncCheck.getGratuityTotal()));
		if(AppGlobal.g_oFuncStation.get().getSeparateInclusiveTaxOnDisplay()){
			BigDecimal dTaxTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(m_oFuncCheck.getTaxTotal().add(m_oFuncCheck.getInclusiveTaxTotal())));
			BigDecimal dInclusiveTax = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(m_oFuncCheck.getInclusiveTaxTotal()));
			BigDecimal dSubTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(m_oFuncCheck.getNetItemTotal().subtract(m_oFuncCheck.getInclusiveTaxTotal())));
			
			BigDecimal dOriSubTotal = new BigDecimal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(m_oFuncCheck.getNetItemTotal()));
			BigDecimal dTmpRound = dOriSubTotal.subtract(dInclusiveTax).subtract(dSubTotal).add(m_oFuncCheck.getRoundAmount());
			
			BigDecimal oCheckTaxScRefTotal = m_oFuncCheck.getCheckTaxScRefTotal();
			if (oCheckTaxScRefTotal.compareTo(BigDecimal.ZERO) > 0) {
				dSubTotal = dSubTotal.add(oCheckTaxScRefTotal);
				dScTotal = dScTotal.subtract(oCheckTaxScRefTotal);
			}
			
			m_oFrameCheckDetailInfo.setSC(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(dScTotal));
			m_oFrameCheckDetailInfo.setSubTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(dSubTotal));
			m_oFrameCheckDetailInfo.setTax(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(dTaxTotal));
			
			if(dTmpRound.compareTo(BigDecimal.ZERO) == 0)
				m_oFrameCheckDetailInfo.setRoundAmount(Util.HERORound(dTmpRound, AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
			else
				m_oFrameCheckDetailInfo.setRoundAmount(dTmpRound.stripTrailingZeros().toPlainString());
		}else{
			m_oFrameCheckDetailInfo.setSC(AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(m_oFuncCheck.getServiceChargeTotal()));
			m_oFrameCheckDetailInfo.setSubTotal(AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(m_oFuncCheck.getNetItemTotal()));
			m_oFrameCheckDetailInfo.setTax(AppGlobal.g_oFuncOutlet.get().roundTaxAmountToString(m_oFuncCheck.getTaxTotal()));
			if(m_oFuncCheck.getRoundAmount().compareTo(BigDecimal.ZERO) == 0)
				m_oFrameCheckDetailInfo.setRoundAmount(Util.HERORound(m_oFuncCheck.getRoundAmount(), AppGlobal.g_oFuncOutlet.get().getCheckRoundMethod(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal()).toPlainString());
			else
				m_oFrameCheckDetailInfo.setRoundAmount(m_oFuncCheck.getRoundAmount().stripTrailingZeros().toPlainString());
		}
		m_oFrameCheckDetailInfo.setCheckTotal(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(m_oFuncCheck.getCheckTotal()));
		m_oFrameCheckDetailInfo.setDiscountTotal(AppGlobal.g_oFuncOutlet.get().roundDiscAmountToString(m_oFuncCheck.getCheckDiscountTotal().add(m_oFuncCheck.getItemDiscountTotal().subtract(m_oFuncCheck.getExtraChargeTotal()))));
		m_oFrameCheckDetailInfo.setExtraChargeTotal(AppGlobal.g_oFuncOutlet.get().roundDiscAmountToString(m_oFuncCheck.getExtraChargeTotal()));
		
		if(m_oPosCustomTypeList.getTypeList().size() > 0){
			if (m_oFuncCheck.getCustomTypeId() != 0) {
				PosCustomType oPosCustomType = m_oPosCustomTypeList.getTypeList().get(m_oFuncCheck.getCustomTypeId());
				m_oFrameCheckDetailInfo.setCheckType(oPosCustomType.getNameArray());
			}
		}
		
		if(!m_oFuncCheck.getCheckBusinessPeriodId().isEmpty())
			m_oFrameCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriodById(m_oFuncCheck.getCheckBusinessPeriodId()).getName(AppGlobal.g_oCurrentLangIndex.get()));
		else
			m_oFrameCheckDetailInfo.setMealPeriod(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getName(AppGlobal.g_oCurrentLangIndex.get()));
		
		m_oFrameCheckDetailInfo.setTitleBarDisable();
	}
	
	// Init the frame for Whole Check View
	public void initForWholeCheckView(FuncCheck oFuncCheck, MenuItemCourseList oMenuItemCourseList, AppGlobal.OPERATION_MODE eOperationMode) {
		
		m_oFuncCheck = oFuncCheck;
		m_oMenuItemCourseList = oMenuItemCourseList;
		m_eOperationMode = eOperationMode;
		
		m_oFrameCheckDetailInfo.clearDetailList();
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
			if(m_oFrameColumnView != null)
				m_oFrameColumnView.removeMyself();
			
			// Set the no. of column
			m_oFrameColumnView = new FrameColumnView();
			m_oTemplateBuilder.buildFrame(m_oFrameColumnView, "columnView");
			m_oFrameColumnView.setHeight(m_oFrameColumnView.getHeight() + m_oFrameSeatPanel.getHeight());
			m_oFrameColumnView.init(1, true);
			m_oFrameColumnView.addListener(this);
			m_oFrameMainFrame.attachChild(m_oFrameColumnView);

		}
		
		// Hide the seat panel
		m_oFrameSeatPanel.setVisible(false);
		
		// Load all item to the column view
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name()))
			prepareColumnForCourseView(0);
		
		initCheckInfo();
		
		// Clear list information
		if(m_oFrameCheckDiscountInfo.getCheckListCount() > 0)
			m_oFrameCheckDiscountInfo.removeAllDiscountItem();
		if(m_oFrameCheckDiscountInfo.getExtraChargeListCount() > 0)
			m_oFrameCheckDiscountInfo.removeAllExtraChargeItem();
		
		List<PosCheckDiscount> oAppliedCheckPartyDiscountList = m_oFuncCheck.getCurrentPartyAppliedCheckDiscount();
		if(oAppliedCheckPartyDiscountList.size()>0){
			for(PosCheckDiscount oPostCheckDiscount: oAppliedCheckPartyDiscountList){
				PosCheckExtraInfo oPosCheckExtraInfo = null;
				oPosCheckExtraInfo = oPostCheckDiscount.getExtraInfoFromList(PosCheckExtraInfo.SECTION_DISCOUNT, PosCheckExtraInfo.VARIABLE_REFERENCE, 0);
				String[] sDiscountItems = null;
				if(oPosCheckExtraInfo == null)
					sDiscountItems = oPostCheckDiscount.getName();
				else {
					sDiscountItems = new String[5];
					for (int i = 0; i < 5; i++)
						sDiscountItems[i] = (oPostCheckDiscount.getName())[i] + " (" + oPosCheckExtraInfo.getValue() + ")";
				}
				
				if(oPostCheckDiscount.getUsedFor().equals(PosCheckDiscount.USED_FOR_EXTRA_CHARGE))
					m_oFrameCheckDiscountInfo.addExtraChargeItem(sDiscountItems);
				else
					m_oFrameCheckDiscountInfo.addDiscountItem(sDiscountItems);
			}
		}
		m_oFrameCheckDiscountInfo.setVisible(true);
		
		m_iShowMode = 0;
		
		// Hide "View By Seat" and "View By Course No." button in stock delivery operation mode
		showSeatAndCourseButton((m_eOperationMode.equals(AppGlobal.OPERATION_MODE.stock_delivery))? false: true);
	}
	
	private void showSeatAndCourseButton(boolean bShow) {
		m_oButtonViewBySeat.setVisible(bShow);
		m_oButtonViewByCourse.setVisible(bShow);
	}
	
	/*private void prepareColumnForWholeCheckView(int iColumnIndex){
		// Prepare each column
		m_oFrameColumnView.updateColumnHeader(iColumnIndex, "");
		m_oFrameColumnView.addSection(0, AppGlobal.g_oLang.get()._("shared"), false);
		m_oFrameColumnView.addSectionToElement(iColumnIndex, 0, 0, "", false);
		for(int i=0; i<=AppGlobal.MAX_SEATS; i++) { 	
			if(m_oFuncCheck.getItemList(i).isEmpty())
				continue;
			
			// Add a section for seat no.
			if(i > 0){
				m_oFrameColumnView.addSection(i, AppGlobal.g_oLang.get()._("seat")+" "+i, false);
				m_oFrameColumnView.addSectionToElement(iColumnIndex, i, 0, "", false);
			}
			
			for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(i)) {
				m_oFrameColumnView.addItemToElement(iColumnIndex, i, 0, oFuncCheckItem);
			}
		}
	}*/
	
	private void prepareColumnForCourseView(int iColumnIndex) {
		m_oFrameColumnView.updateColumnHeader(iColumnIndex, "");

		m_oFrameColumnView.addSection(0, AppGlobal.g_oLang.get()._("undefined_course"), false);
		m_oFrameColumnView.addSectionToElement(iColumnIndex, 0, 0, "", false);
		for(int iSeatNo = 0; iSeatNo <= AppGlobal.MAX_SEATS; iSeatNo++) {
			if(m_oFuncCheck.getItemList(iSeatNo).isEmpty())
				continue;
			
			for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(iSeatNo)) {
				int iItemCourseId = oFuncCheckItem.getCheckItem().getCourseId();
				if(iItemCourseId == 0)
					m_oFrameColumnView.addItemToElement(iColumnIndex, 0, 0, oFuncCheckItem);
			}
		}
		
		for(MenuItemCourse oMenuItemCourse:m_oMenuItemCourseList.getItemCourseList()) {
			boolean bDisplayCourseName = false;
			
			for(int iSeatNo = 0; iSeatNo <= AppGlobal.MAX_SEATS; iSeatNo++) {
				if(m_oFuncCheck.getItemList(iSeatNo).isEmpty())
					continue;
				
				for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(iSeatNo)) {
					int iItemCourseId = oFuncCheckItem.getCheckItem().getCourseId();
					//if(iItemCourseId == oMenuItemCourse.getSeq()) { // FOUND RELATED COURSE ITEM
					if(iItemCourseId == oMenuItemCourse.getIcouId()) {
						if (!bDisplayCourseName) { // For first time display course name if item list has related course item
							String sMenuItemCourseName;
							if(oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
								sMenuItemCourseName = oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get());
							else
								sMenuItemCourseName = oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get());
							m_oFrameColumnView.addSection(oMenuItemCourse.getSeq(), sMenuItemCourseName, false);
							m_oFrameColumnView.addSectionToElement(iColumnIndex, oMenuItemCourse.getSeq(), 0, "", false);
							
							bDisplayCourseName = true;
						}
						m_oFrameColumnView.addItemToElement(iColumnIndex, oMenuItemCourse.getSeq(), 0, oFuncCheckItem);
					}
				}
			}
		}
	}
	
	// Init the frame for Seat Column View
	public void initForSeatColumnView(){

		int iColumnCount = 3;
		
		if(m_oFrameColumnView != null){
			m_oFrameColumnView.removeMyself();
		}
		
		// Set the no. of column
		m_oFrameColumnView = new FrameColumnView();
		m_oTemplateBuilder.buildFrame(m_oFrameColumnView, "columnView");
		m_oFrameColumnView.init(iColumnCount, false);
		m_oFrameColumnView.addListener(this);
		m_oFrameMainFrame.attachChild(m_oFrameColumnView);
		
		// Show the seat panel
		//m_oFrameSeatPanel.setWidth(m_oFrameColumnView.getWidth());
		m_oFrameSeatPanel.setVisible(true);
		m_oFrameSeatPanel.bringToTop();
		
		// Create course section
		m_oFrameColumnView.addSection(0, AppGlobal.g_oLang.get()._("undefined_course"), true);
		for(int i = 0; i <= iColumnCount; i++)
			m_oFrameColumnView.addSectionToElement(i, 0, 0, "", false);
		m_oSectionIndexCourseIdMap.put(0, 0);
		for(MenuItemCourse oMenuItemCourse:m_oMenuItemCourseList.getItemCourseList()){
			String sMenuItemCourseName;
			if(oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
				sMenuItemCourseName = oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get());
			else
				sMenuItemCourseName = oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get());
			m_oFrameColumnView.addSection(oMenuItemCourse.getSeq(), "(" + oMenuItemCourse.getSeq() + ") " + sMenuItemCourseName, true);
			for(int i = 0; i <= iColumnCount; i++)
				m_oFrameColumnView.addSectionToElement(i, oMenuItemCourse.getSeq(), 0, "", false);
		m_oSectionIndexCourseIdMap.put(oMenuItemCourse.getSeq(), oMenuItemCourse.getIcouId());
		}
		
		// Load all item to the column view
		for(int i = 0; i < iColumnCount; i++) {
			// Prepare column
			prepareColumnForSeatColumnView(i, i);
		}
		
		m_iShowMode = 1;
	}

	private void prepareColumnForSeatColumnView(int iColumnIndex, int iSeatNo){
		// Prepare each column
		if(iSeatNo == 0)
			m_oFrameColumnView.updateColumnHeader(iColumnIndex, AppGlobal.g_oLang.get()._("shared"));
		else
			m_oFrameColumnView.updateColumnHeader(iColumnIndex, AppGlobal.g_oLang.get()._("seat")+" "+iSeatNo);
		
		for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(iSeatNo)) {
			int iCourseId = oFuncCheckItem.getCheckItem().getCourseId();
			if(iCourseId > 0){
				int iSectionIndex = 0;
				for(Entry<Integer, Integer> entry:m_oSectionIndexCourseIdMap.entrySet()){
					if(entry.getValue() == iCourseId){
						iSectionIndex = entry.getKey();
					}
				}
				m_oFrameColumnView.addItemToElement(iColumnIndex, iSectionIndex, 0, oFuncCheckItem);
			}else{
				m_oFrameColumnView.addItemToElement(iColumnIndex, 0, 0, oFuncCheckItem);
			}
		}
		
		// Key: column index	Value: seat no.
		m_oColumnSeatList.put(iColumnIndex, iSeatNo);
	}
	
	private void clearColumnForSeatColumnView(int iTargetColumnIndex){
		for(Entry<Integer, Integer> entry:m_oSectionIndexCourseIdMap.entrySet()){
			m_oFrameColumnView.removeAllItemFromElement(iTargetColumnIndex, entry.getKey());
		}
	}
	
	private void changeColumnForSeatColumnView(int iColumnIndex){
		String sErrMsg;
		
		// Ask seat no.
		FormInputBox oFormInputBox = new FormInputBox(this.getParentForm());
		oFormInputBox.init();
		oFormInputBox.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.NUMBER);
		oFormInputBox.setTitle(AppGlobal.g_oLang.get()._("seat"));
		oFormInputBox.setMessage(AppGlobal.g_oLang.get()._("please_input_the_seat_no")+":");
		oFormInputBox.show();
		
		String sNewSeat = oFormInputBox.getInputValue();
		if(sNewSeat == null)
			return;
		
		int iNewSeatNo = 0;
		try{
			iNewSeatNo = Integer.valueOf(sNewSeat);
		}catch(NumberFormatException e){
			sErrMsg = AppGlobal.g_oLang.get()._("invalid_input");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}
		
		if(iNewSeatNo > AppGlobal.MAX_SEATS){
			sErrMsg = AppGlobal.g_oLang.get()._("seat_no_cannot_be_larger_than")+" "+ AppGlobal.MAX_SEATS;
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}
		
		// Key: column index	Value: seat no.
		boolean bFound = false;
		for(Entry<Integer, Integer> entry:m_oColumnSeatList.entrySet()){
			if(iNewSeatNo == entry.getValue()){
				bFound = true;
				break;
			}
		}
		if(bFound){
			sErrMsg = AppGlobal.g_oLang.get()._("seat_is_loaded");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}
		
		// Clear the original column
		clearColumnForSeatColumnView(iColumnIndex);
		
		// Prepare the new column
		prepareColumnForSeatColumnView(iColumnIndex, iNewSeatNo);
		
		// De-select all items
		m_oFrameColumnView.deSelectAllLine();
	}

	// Init the frame for Course Column View
	public void initForCourseColumnView(){

		int iColumnCount = 3;
		
		if(m_oFrameColumnView != null){
			m_oFrameColumnView.removeMyself();
		}
		
		// Set the no. of column
		m_oFrameColumnView = new FrameColumnView();
		m_oTemplateBuilder.buildFrame(m_oFrameColumnView, "columnView");
		m_oFrameColumnView.setHeight(m_oFrameColumnView.getHeight() + m_oFrameSeatPanel.getHeight());
		m_oFrameColumnView.init(iColumnCount, false);
		m_oFrameColumnView.addListener(this);
		m_oFrameMainFrame.attachChild(m_oFrameColumnView);
		
		// Hide the seat panel
		m_oFrameSeatPanel.setVisible(false);
		
		// Create course section
		m_oFrameColumnView.addSection(0, "", false);
		for(int i = 0; i <= iColumnCount; i++)
			m_oFrameColumnView.addSectionToElement(i, 0, 0, "", false);
		m_oSectionIndexSeatNoMap.put(0, 0);
		
		// Load all item to the column view
		// First column for no course
		prepareColumnForCourseColumnView(0, 0);
		
		for(int i=1; i<iColumnCount; i++){
			for(MenuItemCourse oMenuItemCourse:m_oMenuItemCourseList.getItemCourseList()){
				if(oMenuItemCourse.getSeq() == i){
					// Prepare column
					prepareColumnForCourseColumnView(i, oMenuItemCourse.getIcouId());
					
					break;
				}
			}
		}
		
		m_iShowMode = 2;
	}
	
	private void prepareColumnForCourseColumnView(int iColumnIndex, int iCourseId){

		if(iCourseId == 0){
			m_oFrameColumnView.updateColumnHeader(iColumnIndex, AppGlobal.g_oLang.get()._("undefined_course"));
		}else{
			MenuItemCourse oMenuItemCourse = null;
			boolean bFound = false;
			for(MenuItemCourse oMenuItemCourse1:m_oMenuItemCourseList.getItemCourseList()){
				if(oMenuItemCourse1.getIcouId() == iCourseId){
					oMenuItemCourse = oMenuItemCourse1;
					bFound = true;
					break;
				}
			}
			if(bFound == false)
				return;
			
			// Prepare each column
			String sMenuItemCourseName;
			if(oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
				sMenuItemCourseName = oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get());
			else
				sMenuItemCourseName = oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get());
			m_oFrameColumnView.updateColumnHeader(iColumnIndex, "(" + oMenuItemCourse.getSeq() + ") " + sMenuItemCourseName);
		}
		
		for(int iSeatNo = 0; iSeatNo <= AppGlobal.MAX_SEATS; iSeatNo++) {
			if(m_oFuncCheck.getItemList(iSeatNo).isEmpty())
				continue;
			
			for(FuncCheckItem oFuncCheckItem:m_oFuncCheck.getItemList(iSeatNo)) {
				int iItemCourseId = oFuncCheckItem.getCheckItem().getCourseId();
				if(iItemCourseId == iCourseId){
					m_oFrameColumnView.addItemToElement(iColumnIndex, 0, 0, oFuncCheckItem);
				}
			}
		}
		
		// Key: column index	Value: course id
		m_oColumnCourseList.put(iColumnIndex, iCourseId);
	}
	
	private void clearColumnForCourseColumnView(int iTargetColumnIndex){
		for(Entry<Integer, Integer> entry:m_oSectionIndexSeatNoMap.entrySet()){
			m_oFrameColumnView.removeAllItemFromElement(iTargetColumnIndex, entry.getKey());
		}
	}
	
	private void changeColumnForCourseView(int iColumnIndex){
		ArrayList<String> oOptionList = new ArrayList<String>();
		ArrayList<Integer> oCourseIdList = new ArrayList<Integer>();
		
		boolean bFound = false;
		for(Entry<Integer, Integer> entry1:m_oColumnCourseList.entrySet()){
			if(entry1.getValue() == 0){
				bFound = true;
				break;
			}
		}
		if(!bFound){
			oOptionList.add(AppGlobal.g_oLang.get()._("undefined_course"));
			oCourseIdList.add(0);
		}
		
		for(MenuItemCourse oMenuItemCourse:m_oMenuItemCourseList.getItemCourseList()){
			bFound = false;
			for(Entry<Integer, Integer> entry1:m_oColumnCourseList.entrySet()){
				if(entry1.getValue() == oMenuItemCourse.getIcouId()){
					bFound = true;
					break;
				}
			}
			if(bFound)
				continue;
			
			String sMenuItemCourseName;
			if(oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
				sMenuItemCourseName = oMenuItemCourse.getName(AppGlobal.g_oCurrentLangIndex.get());
			else
				sMenuItemCourseName = oMenuItemCourse.getShortName(AppGlobal.g_oCurrentLangIndex.get());
			oOptionList.add("(" + oMenuItemCourse.getSeq() + ") " + sMenuItemCourseName);
			oCourseIdList.add(oMenuItemCourse.getIcouId());
		}
		FormSelectionBox oFormSelectionBox = new FormSelectionBox(this.getParentForm());
		oFormSelectionBox.initWithSingleSelection(AppGlobal.g_oLang.get()._("please_select_the_course"), oOptionList, false);
		oFormSelectionBox.show();
		
		int iNewCourseId = 0;
		if (oFormSelectionBox.isUserCancel()) {
			return;
		} else {
			ArrayList<Integer> oSelectionBoxResult = oFormSelectionBox.getResultList();
			int iIndex = oSelectionBoxResult.get(0);
			iNewCourseId = oCourseIdList.get(iIndex);
		}
				
		// Clear the original column
		clearColumnForCourseColumnView(iColumnIndex);
		
		// Prepare the new column
		prepareColumnForCourseColumnView(iColumnIndex, iNewCourseId);
		
		// De-select all items
		m_oFrameColumnView.deSelectAllLine();
	}
	
	// Set header desc
	public void setTitle(String sTitle){
		m_oLabelHeader.setValue(sTitle);
	}
	
	public void initButtonBackground() {
		this.updateButtonColor(m_oButtonViewWholeCheck);
	}
	
	// set visible of table button
	public void setCheckTableFrameVisible(boolean bVisible) {
		m_oFrameHeader.setTableFrameVisible(bVisible);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_iShowMode != 2 && m_oButtonViewByCourse.getId() == iChildId) {
			initForCourseColumnView();
			this.updateButtonColor(m_oButtonViewByCourse);
			m_oFrameCheckDiscountInfo.setVisible(false);
			bMatchChild = true;
		} else if (m_iShowMode != 1 && m_oButtonViewBySeat.getId() == iChildId) {
			initForSeatColumnView();
			this.updateButtonColor(m_oButtonViewBySeat);
			m_oFrameCheckDiscountInfo.setVisible(false);
			bMatchChild = true;
		} else if (m_iShowMode != 0 && m_oButtonViewWholeCheck.getId() == iChildId) {
			initForWholeCheckView(m_oFuncCheck, m_oMenuItemCourseList, m_eOperationMode);
			this.updateButtonColor(m_oButtonViewWholeCheck);
			m_oFrameCheckDetailInfo.bringToTop();
			m_oFrameCheckDiscountInfo.setVisible(true);
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
	
	// highlight selected button color
	private void updateButtonColor(VirtualUIButton oSelectedButton) {
		VirtualUIButton[] oButtonList = { m_oButtonViewWholeCheck, m_oButtonViewByCourse, m_oButtonViewBySeat };
		
		for (VirtualUIButton oButton: oButtonList) {
			if (oSelectedButton == oButton)
				continue;
			
			oButton.setBackgroundColor(BACKGROUND_COLOR_UNSELECT);
			oButton.setForegroundColor(FOREGROUND_COLOR_UNSELECT);
		}
		
		oSelectedButton.setBackgroundColor(BACKGROUND_COLOR_SELECT);
		oSelectedButton.setForegroundColor(FOREGROUND_COLOR_SELECT);
	}
	
	@Override
	public void frameColumnViewListener_clickPlaceButton(
			int iTargetColumnIndex, int iTargetSectionIndex) {
		
		// Loop the selected lines
		TreeMap<String, Integer> oItemList = m_oFrameColumnView.getSelectedLine();
		for(Entry<String, Integer> entry: oItemList.entrySet()){
			String sKey = entry.getKey(); 
			String split[] = sKey.split("_");
			int iColumnIndex = Integer.parseInt(split[0]);
			int iCourseIndex = Integer.parseInt(split[1]);
			int iSelectedSectionIndex = Integer.parseInt(split[2]);
			int iSelectedItemIndex = Integer.parseInt(split[3]);
			if(m_oColumnSeatList.containsKey(iColumnIndex)){
				int iOriginalSeatNo = m_oColumnSeatList.get(iColumnIndex);
				int iTargetSeatNo = m_oColumnSeatList.get(iTargetColumnIndex);
				
				int iOriginalCourseId = m_oSectionIndexCourseIdMap.get(iCourseIndex);
				int iNewCourseId = m_oSectionIndexCourseIdMap.get(iTargetSectionIndex);
				
				// Remove the selected item from the view
				FuncCheckItem oOriginalFuncCheckItem = m_oFrameColumnView.removeItemFromElement(iColumnIndex, iCourseIndex, iSelectedSectionIndex, iSelectedItemIndex);
				FuncCheckItem oNewFuncCheckItem = oOriginalFuncCheckItem;
				
				// Check FuncCheckItem whether is minimum charge item
				if(oOriginalFuncCheckItem == null) {
					// De-select all items
					m_oFrameColumnView.deSelectAllLine();
					return;
				}
				
				// ********* Main process *********
				// Process change course no.
				if(iOriginalCourseId != iNewCourseId){
					for (FrameCheckFunctionListener listener : listeners) {
						// Raise the event to parent to move item to other seat
						boolean bResult = listener.frameCheckFunction_changeItemCourse(iOriginalSeatNo, oOriginalFuncCheckItem, iNewCourseId);
						if(bResult == false){
							// Fail to change course
								iNewCourseId = iOriginalCourseId;
							}
							break;
						}
					}
				
				// Process change seat no.
				if(iOriginalSeatNo != iTargetSeatNo){
					for (FrameCheckFunctionListener listener : listeners) {
						// Raise the event to parent to move item to other seat
						FuncCheckItem oFuncCheckItem = listener.frameCheckFunction_moveItemToOtherSeat(iOriginalSeatNo, oOriginalFuncCheckItem, iTargetSeatNo, BigDecimal.ZERO);
						if(oFuncCheckItem != null){
							// Change success
							oNewFuncCheckItem = oFuncCheckItem;
						}
						break;
					}
				}
				// *******************************
				
				// Add the selected item to target element
				m_oFrameColumnView.addItemToElement(iTargetColumnIndex, iTargetSectionIndex, 0, oNewFuncCheckItem);
			}
		}
		
		// De-select all items
		m_oFrameColumnView.deSelectAllLine();
	}

	@Override
	public void frameColumnViewListener_clickColumnHeader(int iColumnIndex) {
		if (m_iShowMode == 1)
			// Change seat no.
			this.changeColumnForSeatColumnView(iColumnIndex);
		else if (m_iShowMode == 2)
			// Change course no.
			this.changeColumnForCourseView(iColumnIndex);
		
	}

	@Override
	public void frameCheckDetail_Clicked(String sFuncValue) {
		if (sFuncValue.equals("Table")) {
			for (FrameCheckFunctionListener listener : listeners) {
				// Raise the event to parent
				listener.frameCheckFunction_clickTable();
				m_oFrameHeader.setTableNo(m_oFuncCheck.getTableName());
				initCheckInfo();
				break;
			}
		} else if (sFuncValue.equals("Cover")) {
			for (FrameCheckFunctionListener listener : listeners) {
				// Raise the event to parent
				listener.frameCheckFunction_clickCover();
				m_oFrameHeader.setCover(m_oFuncCheck.getCover());
				initCheckInfo();
				break;
			}
		} else if (sFuncValue.equals("Detail")) {
			for (FrameCheckFunctionListener listener : listeners) {
				// Raise the event to parent
				listener.frameCheckFunction_exit();
				break;
			}
		}
	}
	
	@Override
	public void frameSeatPanel_selectedSeat(int iNewSeatNo) {
		// Key: column index	Value: seat no.
		boolean bFound = false;
		for(Entry<Integer, Integer> entry:m_oColumnSeatList.entrySet()){
			if(iNewSeatNo == entry.getValue()){
				bFound = true;
				break;
			}
		}
		if(bFound){
			String sErrMsg = AppGlobal.g_oLang.get()._("seat_is_loaded");
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this.getParentForm());
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(sErrMsg);
			oFormDialogBox.show();
			return;
		}
		
		// Last column
		int iColumnIndex = m_oColumnSeatList.size() - 1;
		
		// Clear the original column
		clearColumnForSeatColumnView(iColumnIndex);
		
		// Prepare the new column
		prepareColumnForSeatColumnView(iColumnIndex, iNewSeatNo);
		
		// De-select all items
		m_oFrameColumnView.deSelectAllLine();
	}

	@Override
	public void frameSeatPanel_clickCourse() {
	}
	
	@Override
	public boolean frameColumnViewListener_isMinimumChargeItem(FuncCheckItem oFuncCheckItem) {
		boolean bIsMinimumChargeItem = false;
		for (FrameCheckFunctionListener listener : listeners) {
			// Raise the event to parent
			if(listener.frameCheckFunction_checkMinimumChargeItem(oFuncCheckItem))
				bIsMinimumChargeItem = true;
			break;
		}
		
		return bIsMinimumChargeItem;
	}
}
