package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameTipsTrackingListener {
	void frameTipsTracking_clickSave();
	void frameTipsTracking_clickExit();
	void frameTipsTracking_clickAddEmployee();
	void frameTipsTracking_clickDeleteEmployee();
	void frameTipsTracking_clickDirectTipsIn();
	void frameTipsTracking_clickSwitchOutlet();
	void frameTipsTracking_clickTipsFromOthersInfo();
	void frameTipsTracking_clickCommonBasketCell(int iNewSectionId, int iNewItemIndex, int iNewFieldIndex, String sPrevValue);
	boolean frameTipsTracking_checkUpdateCellValue(String sNewValue);
	void frameTipsTracking_clickPrevPage();
	void frameTipsTracking_clickNextPage();
}

public class FrameTipsTracking extends VirtualUIFrame implements FrameNumberPadListener, FrameCommonBasketListener{
	TemplateBuilder m_oTemplateBuilder;

	private FrameCommonBasket<String> m_oFrameTransactionList;

	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;

	private VirtualUILabel m_oLblTitleTipsIn;
	private VirtualUILabel m_oLblTitleSCIn;
	private VirtualUILabel m_oLblTitleDirectTipsIn;
	private VirtualUILabel m_oLblTitleTipsFromOthers;
	private VirtualUILabel m_oLblTitleSCFromOthers;
	private VirtualUILabel m_oLblTitleDirectTipsFromOthers;
	private VirtualUILabel m_oLblTitleTipsBal;
	private VirtualUILabel m_oLblTitleSCBal;
	private VirtualUILabel m_oLblTitleDirectTipsBal;

	private VirtualUILabel m_oLblTipsIn;
	private VirtualUILabel m_oLblSCIn;
	private VirtualUILabel m_oLblDirectTipsIn;
	private VirtualUILabel m_oLblTipsFromOthers;
	private VirtualUILabel m_oLblSCFromOthers;
	private VirtualUILabel m_oLblDirectTipsFromOthers;
	private VirtualUILabel m_oLblTipsBal;
	private VirtualUILabel m_oLblSCBal;
	private VirtualUILabel m_oLblDirectTipsBal;

	private VirtualUIImage m_oImgTipsFromOthersInfo;
	private VirtualUIFrame m_oFrameTipsFromOthersInfo;
	private VirtualUIButton m_oButtonAddEmployee;
	private VirtualUIButton m_oButtonDeleteEmployee;
	private VirtualUIButton m_oButtonDirectTipsIn;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private VirtualUIFrame m_oFrameButtonPrevPage;
	private VirtualUIFrame m_oFrameButtonNextPage;

	private VirtualUILabel m_oLblEmployee;
	private VirtualUILabel m_oLblOutlet;
	private VirtualUIButton m_oButtonSwitchOutlet;

	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUIButton m_oButtonSave;
	private VirtualUIButton m_oButtonExit;

	private int m_iCurrentItemListItemIndex;
	private int m_iCurrentItemListFieldIndex;
	private final static int[] FIELD_WIDTHS = {40, 140, 210, 90, 180, 130};

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameTipsTrackingListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameTipsTrackingListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameTipsTrackingListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameTipsTracking() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTipsTrackingListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTipsTracking.xml");

		// Create Left Content Frame
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);

		// Result list
		m_oFrameTransactionList = new FrameCommonBasket<String>();
		m_oTemplateBuilder.buildFrame(m_oFrameTransactionList, "fraTransactionList");
		m_oFrameTransactionList.init();
		m_oFrameTransactionList.addListener(this);
		m_oFrameLeftContent.attachChild(m_oFrameTransactionList);

		VirtualUIFrame oFrameHeaderBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameHeaderBackground, "fraHeader");
		this.attachChild(oFrameHeaderBackground);

		// Tips and SC in titles
		m_oLblTitleTipsIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleTipsIn, "headerTipsIn");
		this.attachChild(m_oLblTitleTipsIn);
		m_oLblTitleTipsIn.setValue(AppGlobal.g_oLang.get()._("tips_in"));
		m_oLblTitleSCIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleSCIn, "headerSCIn");
		this.attachChild(m_oLblTitleSCIn);
		m_oLblTitleSCIn.setValue(AppGlobal.g_oLang.get()._("service_charge_in"));
		m_oLblTitleDirectTipsIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleDirectTipsIn, "headerDirectTipsIn");
		this.attachChild(m_oLblTitleDirectTipsIn);
		m_oLblTitleDirectTipsIn.setValue(AppGlobal.g_oLang.get()._("direct_tips_in"));

		// Tips and SC total from others titles
		m_oLblTitleTipsFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleTipsFromOthers, "headerTipsFromOthers");
		this.attachChild(m_oLblTitleTipsFromOthers);
		m_oLblTitleTipsFromOthers.setValue(AppGlobal.g_oLang.get()._("tips_total_from_others"));
		m_oLblTitleSCFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleSCFromOthers, "headerSCFromOthers");
		this.attachChild(m_oLblTitleSCFromOthers);
		m_oLblTitleSCFromOthers.setValue(AppGlobal.g_oLang.get()._("service_charge_total_from_others"));
		m_oLblTitleDirectTipsFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleDirectTipsFromOthers, "headerDirectTipsFromOthers");
		this.attachChild(m_oLblTitleDirectTipsFromOthers);
		m_oLblTitleDirectTipsFromOthers.setValue(AppGlobal.g_oLang.get()._("direct_tips_total_from_others"));

		// Tips and SC balance titles
		m_oLblTitleTipsBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleTipsBal, "headerTipsBal");
		this.attachChild(m_oLblTitleTipsBal);
		m_oLblTitleTipsBal.setValue(AppGlobal.g_oLang.get()._("tips_balance"));
		m_oLblTitleSCBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleSCBal, "headerSCBal");
		this.attachChild(m_oLblTitleSCBal);
		m_oLblTitleSCBal.setValue(AppGlobal.g_oLang.get()._("service_charge_balance"));
		m_oLblTitleDirectTipsBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTitleDirectTipsBal, "headerDirectTipsBal");
		this.attachChild(m_oLblTitleDirectTipsBal);
		m_oLblTitleDirectTipsBal.setValue(AppGlobal.g_oLang.get()._("direct_tips_balance"));

		// Tips and SC in
		m_oLblTipsIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTipsIn, "lblTipsIn");
		this.attachChild(m_oLblTipsIn);
		m_oLblSCIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSCIn, "lblSCIn");
		this.attachChild(m_oLblSCIn);
		m_oLblDirectTipsIn = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblDirectTipsIn, "lblDirectTipsIn");
		this.attachChild(m_oLblDirectTipsIn);

		// Tips and SC total from others
		m_oLblTipsFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTipsFromOthers, "lblTipsFromOthers");
		this.attachChild(m_oLblTipsFromOthers);
		m_oLblSCFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSCFromOthers, "lblSCFromOthers");
		this.attachChild(m_oLblSCFromOthers);
		m_oLblDirectTipsFromOthers = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblDirectTipsFromOthers, "lblDirectTipsFromOthers");
		this.attachChild(m_oLblDirectTipsFromOthers);

		// Tips and SC balance
		m_oLblTipsBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblTipsBal, "lblTipsBal");
		this.attachChild(m_oLblTipsBal);
		m_oLblSCBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblSCBal, "lblSCBal");
		this.attachChild(m_oLblSCBal);
		m_oLblDirectTipsBal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblDirectTipsBal, "lblDirectTipsBal");
		this.attachChild(m_oLblDirectTipsBal);

		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(FIELD_WIDTHS[0]);
		sFieldValues.add("");
		iFieldWidths.add(FIELD_WIDTHS[1]);
		sFieldValues.add(AppGlobal.g_oLang.get()._("employee_no"));
		iFieldWidths.add(FIELD_WIDTHS[2]);
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		iFieldWidths.add(FIELD_WIDTHS[3]);
		sFieldValues.add(AppGlobal.g_oLang.get()._("tips_out"));
		iFieldWidths.add(FIELD_WIDTHS[4]);
		sFieldValues.add(AppGlobal.g_oLang.get()._("service_charge_out"));
		iFieldWidths.add(FIELD_WIDTHS[5]);
		sFieldValues.add(AppGlobal.g_oLang.get()._("direct_tips_out"));

		m_oFrameTransactionList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameTransactionList.setHeaderFormat(35, 18, "5,0,0,5");
		for (int iColumn = 3; iColumn < 6; iColumn++)
			m_oFrameTransactionList.setHeaderTextAlign(iColumn, HeroActionProtocol.View.Attribute.TextAlign.RIGHT + ","
					+ HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);
		m_oFrameTransactionList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oFrameTransactionList.setBottomUnderlineVisible(false);

		// Tips From Others Info button
		m_oFrameTipsFromOthersInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTipsFromOthersInfo, "fraTipsFromOthersInfo");
		m_oFrameTipsFromOthersInfo.allowClick(true);
		m_oFrameTipsFromOthersInfo.setClickServerRequestBlockUI(true);
		m_oFrameTipsFromOthersInfo.allowLongClick(false);
		m_oFrameTipsFromOthersInfo.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameTipsFromOthersInfo);

		m_oImgTipsFromOthersInfo = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgTipsFromOthersInfo, "ImgTipsFromOthersInfo");
		m_oImgTipsFromOthersInfo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_info.png");
		m_oFrameTipsFromOthersInfo.attachChild(m_oImgTipsFromOthersInfo);

		// Add Employee button
		m_oButtonAddEmployee = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonAddEmployee, "btnAddEmployee");
		m_oButtonAddEmployee.setValue(AppGlobal.g_oLang.get()._("add_employee"));
		m_oFrameLeftContent.attachChild(m_oButtonAddEmployee);

		// Delete Employee button
		m_oButtonDeleteEmployee = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDeleteEmployee, "btnDeleteEmployee");
		m_oButtonDeleteEmployee.setValue(AppGlobal.g_oLang.get()._("delete_employee"));
		m_oButtonDeleteEmployee.setEnabled(false);
		m_oFrameLeftContent.attachChild(m_oButtonDeleteEmployee);

		// Direct Tips In button
		m_oButtonDirectTipsIn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDirectTipsIn, "btnDirectTipsIn");
		m_oButtonDirectTipsIn.setValue(AppGlobal.g_oLang.get()._("direct_tips_in"));
		m_oFrameLeftContent.attachChild(m_oButtonDirectTipsIn);

		// Create prev page button
		m_oFrameButtonPrevPage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameButtonPrevPage, "fraPrevPage");
		m_oFrameButtonPrevPage.allowClick(true);
		m_oFrameButtonPrevPage.setClickServerRequestBlockUI(true);
		m_oFrameButtonPrevPage.allowLongClick(false);
		m_oFrameButtonPrevPage.setLongClickServerRequestBlockUI(false);
		// m_oFrameButtonPrevPage.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oFrameButtonPrevPage);

		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oFrameButtonPrevPage.attachChild(m_oImgButtonPrevPage);

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oFrameLeftContent.attachChild(m_oLblPage);

		// Create next page button
		m_oFrameButtonNextPage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameButtonNextPage, "fraNextPage");
		m_oFrameButtonNextPage.allowClick(true);
		m_oFrameButtonNextPage.setClickServerRequestBlockUI(true);
		m_oFrameButtonNextPage.allowLongClick(false);
		m_oFrameButtonNextPage.setLongClickServerRequestBlockUI(false);
		// m_oFrameButtonNextPage.setVisible(false);
		m_oFrameLeftContent.attachChild(m_oFrameButtonNextPage);

		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oFrameButtonNextPage.attachChild(m_oImgButtonNextPage);

		// Create Right Content Frame
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);

		// Employee ID and name
		m_oLblEmployee = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblEmployee, "lblEmployee");
		m_oFrameRightContent.attachChild(m_oLblEmployee);

		// Outlet name
		m_oLblOutlet = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblOutlet, "lblOutlet");
		m_oFrameRightContent.attachChild(m_oLblOutlet);

		// Switch Outlet button
		m_oButtonSwitchOutlet = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSwitchOutlet, "btnSwitchOutlet");
		m_oButtonSwitchOutlet.setValue(AppGlobal.g_oLang.get()._("switch_outlet"));
		this.attachChild(m_oButtonSwitchOutlet);

		// Temporarily disable Switch Outlet button
		m_oButtonSwitchOutlet.setVisible(false);
		m_oButtonSwitchOutlet.setEnabled(false);

		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.setClearReplaceCancelEnter(true);
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameRightContent.attachChild(m_oFrameNumberPad);

		// Save button
		m_oButtonSave = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonSave, "btnSave");
		m_oButtonSave.setValue(AppGlobal.g_oLang.get()._("save"));
		m_oButtonSave.setEnabled(false);
		m_oButtonSave.allowClick(true);
		m_oFrameRightContent.attachChild(m_oButtonSave);

		// Exit button
		m_oButtonExit = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonExit, "btnExit");
		m_oButtonExit.setValue(AppGlobal.g_oLang.get()._("exit"));
		m_oFrameRightContent.attachChild(m_oButtonExit);

		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
	}

	public void addRecord(int iSectionId, int iItemIndex, String sEmployeeNumber, String sEmployeeName,
			BigDecimal dTipsOut, BigDecimal dSCOut, BigDecimal dDirectTipsOut, boolean bPrintable) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();

		String sTipsOut, sSCOut, sDirectTipsOut;
		sTipsOut = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(dTipsOut);
		sSCOut = AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(dSCOut);
		sDirectTipsOut = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(dDirectTipsOut);

		iFieldWidths.add(FIELD_WIDTHS[0]);
		if (bPrintable)
			sFieldValues.add(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
		else
			sFieldValues.add("");
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
		iFieldWidths.add(FIELD_WIDTHS[1]);
		sFieldValues.add(sEmployeeNumber);
		sFieldAligns.add("");
		iFieldWidths.add(FIELD_WIDTHS[2]);
		sFieldValues.add(sEmployeeName);
		sFieldAligns.add("");
		iFieldWidths.add(FIELD_WIDTHS[3]);
		sFieldValues.add(sTipsOut);
		iFieldWidths.add(FIELD_WIDTHS[4]);
		sFieldValues.add(sSCOut);
		iFieldWidths.add(FIELD_WIDTHS[5]);
		sFieldValues.add(sDirectTipsOut);
		for (int iColumn = 3; iColumn < 6; iColumn++)
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + ","
					+ HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL);

		m_oFrameTransactionList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		for (int iFieldIndex = 0; iFieldIndex < 6; iFieldIndex++){
			m_oFrameTransactionList.setFieldTextSize(iSectionId, iItemIndex, iFieldIndex, 18);
			m_oFrameTransactionList.setFieldPadding(iSectionId, iItemIndex, iFieldIndex, "15,5,0,5");
		}
	}

	public void clearAllRecords() {
		m_oFrameTransactionList.removeAllItems(0);
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
	}

	public void setCellFieldBackgroundColorEdited(int iSectionId, int iItemIndex, int iFieldIndex, boolean bEdited) {
		if (bEdited) {
			if (iFieldIndex == 1 || iFieldIndex == 2)
				m_oFrameTransactionList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#E1ECF8");
			else {
				m_oFrameTransactionList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#DDDDDD");
				// Set "Save" button to clickable
				if (!m_oButtonSave.getEnabled())
					setSaveButtonEnabled(true);
			}
		} else
			m_oFrameTransactionList.setFieldBackgroundColor(iSectionId, iItemIndex, iFieldIndex, "#00000000");
	}

	public void setSaveButtonEnabled(boolean bEnabled) {
		if (m_oButtonSave.getEnabled() != bEnabled) {
			m_oButtonSave.setEnabled(bEnabled);

			if (bEnabled) {
				m_oButtonSave.setBackgroundColor("#0055B8");
				m_oButtonSave.setForegroundColor("#FFFFFF");
			} else {
				m_oButtonSave.setBackgroundColor("#FFFFFF");
				m_oButtonSave.setForegroundColor("#999999");
			}
		}
	}

	public void setDeleteButtonEnabled(boolean bEnabled) {
		if (m_oButtonDeleteEmployee.getEnabled() != bEnabled) {
			m_oButtonDeleteEmployee.setEnabled(bEnabled);

			if (bEnabled) {
				m_oButtonDeleteEmployee.setBackgroundColor("#0055B8");
				m_oButtonDeleteEmployee.setForegroundColor("#FFFFFF");
			} else {
				m_oButtonDeleteEmployee.setBackgroundColor("#FFFFFF");
				m_oButtonDeleteEmployee.setForegroundColor("#999999");
			}
		}
	}

	public void showPrinterIcon(int iSectionId, int iItemIndex, boolean bShow) {
		if (bShow)
			m_oFrameTransactionList.setFieldValue(iSectionId, iItemIndex, 0,
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_printer.png");
		else
			m_oFrameTransactionList.setFieldValue(iSectionId, iItemIndex, 0, "");
	}

 	public void setFrameInfo(String sTipsIn, String sSCIn, String sDirectTipsIn, String sTipsFromOthers,
		String sSCFromOthers, String sDirectTipsFromOthers, String sTipsBal, String sSCBal,
		String sDirectTipsBal, String sEmployeeNum, String sEmployeeName, String sOutletName) {
		m_oLblTipsIn.setValue(sTipsIn);
		m_oLblSCIn.setValue(sSCIn);
		m_oLblDirectTipsIn.setValue(sDirectTipsIn);
		m_oLblTipsFromOthers.setValue(sTipsFromOthers);
		m_oLblSCFromOthers.setValue(sSCFromOthers);
		m_oLblDirectTipsFromOthers.setValue(sDirectTipsFromOthers);
		m_oLblTipsBal.setValue(sTipsBal);
		m_oLblSCBal.setValue(sSCBal);
		m_oLblDirectTipsBal.setValue(sDirectTipsBal);
		m_oLblEmployee.setValue(AppGlobal.g_oLang.get()._("employee") + ": " + sEmployeeNum + "  " + sEmployeeName);
		m_oLblOutlet.setValue(AppGlobal.g_oLang.get()._("outlet") + ": " + sOutletName);
	}

	public void setTipsBalance (String sTipsBal) {
		m_oLblTipsBal.setValue(sTipsBal);
	}

	public void setSCBalance (String sSCBal) {
		m_oLblSCBal.setValue(sSCBal);
	}

	public void setDirectTipsIn (String sDirectTipsIn) {
		m_oLblDirectTipsIn.setValue(sDirectTipsIn);
	}

	public void setDirectTipsBalance (String sDirectTipsBal) {
		m_oLblDirectTipsBal.setValue(sDirectTipsBal);
	}

	public void setPageNumber(int iTotalPage, int iPageNumber) {
		if (iTotalPage > 1) {
			// employees count > page record number, show page number label
			m_oLblPage.setValue(Integer.toString(iPageNumber) + " / " + Integer.toString(iTotalPage));
			m_oLblPage.setVisible(true);
			m_oFrameButtonPrevPage.setVisible(true);
			m_oFrameButtonNextPage.setVisible(true);
		} else {
			m_oLblPage.setVisible(false);
			m_oFrameButtonPrevPage.setVisible(false);
			m_oFrameButtonNextPage.setVisible(false);
		}
	}

	public void showNextPageButton(boolean bShow) {
		if (bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oFrameButtonNextPage.setEnabled(bShow);
	}

	public void showPrevPageButton(boolean bShow) {
		if (bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oFrameButtonPrevPage.setEnabled(bShow);
	}

	private boolean updatePreviousFieldValue() {
		// For fields no need to be updated
		if (m_iCurrentItemListItemIndex < 0 || (m_iCurrentItemListFieldIndex != 3 && m_iCurrentItemListFieldIndex != 4
				&& m_iCurrentItemListFieldIndex != 5))
			return true;

		boolean bCanUpdate = false;
		// Get current value in edit field
		String sNewValue = m_oFrameTransactionList.getEditFieldValue();
		for (FrameTipsTrackingListener listener : listeners)
			// Raise the event to parent to check if the update is possible
			bCanUpdate = listener.frameTipsTracking_checkUpdateCellValue(sNewValue);
		if (bCanUpdate) {
			BigDecimal dNewValue = new BigDecimal(sNewValue.trim());
			String sFinalValue = null;
			if (m_iCurrentItemListFieldIndex == 3 || m_iCurrentItemListFieldIndex == 5)
				sFinalValue = AppGlobal.g_oFuncOutlet.get().roundPaymentAmountToString(dNewValue);
			else
				sFinalValue = AppGlobal.g_oFuncOutlet.get().roundSCAmountToString(dNewValue);

			m_oFrameTransactionList.setFieldValue(0, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex,
					sFinalValue);
			return true;
		}
		m_oFrameTransactionList.setEditField(0, m_iCurrentItemListItemIndex, m_iCurrentItemListFieldIndex);
		return false;
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonSave.getId()) {
			// Press "ENTER" first in case user forgets to press
			FrameNumberPad_clickEnter();

			for (FrameTipsTrackingListener listener : listeners)
				// Raise the event to parent
				listener.frameTipsTracking_clickSave();

			m_iCurrentItemListItemIndex = -1;
			m_iCurrentItemListFieldIndex = -1;
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameTransactionList.clearEditField();

			bMatchChild = true;

		} else if (iChildId == m_oButtonExit.getId()) {
			for (FrameTipsTrackingListener listener : listeners)
				// Raise the event to parent
				listener.frameTipsTracking_clickExit();
			bMatchChild = true;

		} else if (iChildId == m_oButtonAddEmployee.getId()) {
			FrameNumberPad_clickCancel();
			for (FrameTipsTrackingListener listener : listeners)
				// Raise the event to parent
				listener.frameTipsTracking_clickAddEmployee();
			bMatchChild = true;

		} else if (iChildId == m_oButtonDeleteEmployee.getId()) {
			FrameNumberPad_clickCancel();
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickDeleteEmployee();
			bMatchChild = true;

		} else if (iChildId == m_oButtonDirectTipsIn.getId()) {
			FrameNumberPad_clickCancel();
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickDirectTipsIn();
			bMatchChild = true;

		} else if (iChildId == m_oButtonSwitchOutlet.getId()) {
			FrameNumberPad_clickCancel();
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickSwitchOutlet();
			bMatchChild = true;

		} else if (iChildId == m_oFrameTipsFromOthersInfo.getId()) {
			FrameNumberPad_clickCancel();
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickTipsFromOthersInfo();
			bMatchChild = true;

		} else if (iChildId == m_oFrameButtonNextPage.getId()) {
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickNextPage();
			bMatchChild = true;

		} else if (iChildId == m_oFrameButtonPrevPage.getId()) {
			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickPrevPage();
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		if (updatePreviousFieldValue()) {
			String sPrevValue = null;
			if (m_iCurrentItemListItemIndex >= 0 && m_iCurrentItemListFieldIndex >= 0)
				sPrevValue = m_oFrameTransactionList.getFieldValue(0, m_iCurrentItemListItemIndex,
						m_iCurrentItemListFieldIndex);
			else
				return;

			// Update current index to next field index
			boolean bSetNextField = true;
			if (m_iCurrentItemListFieldIndex == 3 || m_iCurrentItemListFieldIndex == 4)
				m_iCurrentItemListFieldIndex++;
			else if (m_iCurrentItemListFieldIndex == 5)
				if (m_iCurrentItemListItemIndex < m_oFrameTransactionList.getItemCellCount(0) - 1) {
					m_iCurrentItemListFieldIndex = 3;
					m_iCurrentItemListItemIndex++;
				} else { // Last row
					bSetNextField = false;
					m_iCurrentItemListItemIndex = -1;
					m_iCurrentItemListFieldIndex = -1;
				}
			else
				m_iCurrentItemListFieldIndex = 3;

			for (FrameTipsTrackingListener listener : listeners)
				// Raise the event to parent
				listener.frameTipsTracking_clickCommonBasketCell(0, m_iCurrentItemListItemIndex,
						m_iCurrentItemListFieldIndex, sPrevValue);

			// Edit next field
			m_oFrameNumberPad.clearEnterSubmitId();
			if (bSetNextField) {
				VirtualUIBasicElement oElement = m_oFrameTransactionList.setEditField(0, m_iCurrentItemListItemIndex,
						m_iCurrentItemListFieldIndex);
				m_oFrameNumberPad.setEnterSubmitId(oElement);
				m_oButtonSave.addClickServerRequestSubmitElement(oElement);
			} else
				m_oFrameTransactionList.clearEditField();
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		m_iCurrentItemListItemIndex = -1;
		m_iCurrentItemListFieldIndex = -1;
		m_oFrameNumberPad.clearEnterSubmitId();
		m_oFrameTransactionList.clearEditField();
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasket, int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
		if (updatePreviousFieldValue()) {

			// Update index in FormTipsTracking
			String sPrevValue = null;
			if (m_iCurrentItemListItemIndex >= 0 && m_iCurrentItemListFieldIndex >= 0)
				sPrevValue = m_oFrameTransactionList.getFieldValue(0, m_iCurrentItemListItemIndex,
						m_iCurrentItemListFieldIndex);

			for (FrameTipsTrackingListener listener : listeners)
				listener.frameTipsTracking_clickCommonBasketCell(iSectionIndex, iItemIndex, iFieldIndex, sPrevValue);

			// Move to the clicked field
			m_oFrameNumberPad.clearEnterSubmitId();
			if (iFieldIndex == 3 || iFieldIndex == 4 || iFieldIndex == 5) {
				VirtualUIBasicElement oElement = m_oFrameTransactionList.setEditField(iSectionIndex, iItemIndex, iFieldIndex);
				m_oFrameNumberPad.setEnterSubmitId(oElement);
				m_oButtonSave.addClickServerRequestSubmitElement(oElement);
			} else
				m_oFrameTransactionList.clearEditField();

			m_iCurrentItemListItemIndex = iItemIndex;
			m_iCurrentItemListFieldIndex = iFieldIndex;
			
			if (!m_oButtonSave.getEnabled())
				setSaveButtonEnabled(true);
		}
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}
}
