package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FramePanelItemSelectPanelListener {
	void framePanelItemSelectPanel_CancelClicked();
	void framePanelitemSelectPanel_ConfirmClicked();
}

public class FramePanelItemSelectPanel extends VirtualUIFrame implements FrameCommonBasketListener {
	class ClsSelectedPaymentDetail {
		int m_iPaymId;
		String m_sAmount;
		HashMap<String, String> m_oOtherInfo;
		
		public ClsSelectedPaymentDetail(int iPaymId, String sAmount, HashMap<String, String> oOtherInfo) {
			m_iPaymId = iPaymId;
			m_sAmount = sAmount;
			m_oOtherInfo = oOtherInfo;
		}
		
		public int getPaymId() {
			return m_iPaymId;
		}
		
		public String getAmount() {
			return m_sAmount;
		}

		public HashMap<String, String> getOtherInfo(){
			return m_oOtherInfo;
		}
	}
	
	TemplateBuilder m_oTemplateBuilder;

	private FrameCommonBasket m_oCommonBasket;
	private VirtualUIButton m_oCancelButton;
	private VirtualUIButton m_oConfirmButton;
	
	private List<Integer> m_oSelectedItemList;
	private List<ClsSelectedPaymentDetail> m_oSelectedPaymentDetailList;
	
	private String m_sCurrentDisplayType;
	
	private boolean m_bStockSoldOut;
	
	static public String DISPLAY_TYPE_ITEM = "I";
	static public String DISPLAY_TYPE_PAYMENT_METHOD = "P";
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FramePanelItemSelectPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FramePanelItemSelectPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FramePanelItemSelectPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	//Constructor
	public FramePanelItemSelectPanel(String sDisplayType) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FramePanelItemSelectPanelListener>();
		m_oSelectedItemList = new ArrayList<Integer>();
		m_oSelectedPaymentDetailList = new ArrayList<>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraPanelItemSelectPanel.xml");
		
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// Item List
		m_oCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oCommonBasket, "fraSelectedItemBasket");
		m_oCommonBasket.init();
		m_oCommonBasket.addListener(this);
		m_oCommonBasket.setVisible(false);
		this.attachChild(m_oCommonBasket);

		m_sCurrentDisplayType = sDisplayType;
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValues = new ArrayList<String[]>();
		if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_ITEM)) {
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				iFieldWidths.add(480);
			else
				iFieldWidths.add(400);
			sFieldValues.add(AppGlobal.g_oLang.get()._("selected_items", ""));
		} else
		if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_PAYMENT_METHOD)) {
			iFieldWidths.add(200);
			iFieldWidths.add(200);
			sFieldValues.add(AppGlobal.g_oLang.get()._("payment_method", ""));
			sFieldValues.add(AppGlobal.g_oLang.get()._("amount", ""));
		}
		m_oCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oCommonBasket.setHeaderFormat(0, 24, "0,0,0,10");
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		// set common basket underline invisible
		m_oCommonBasket.setBottomUnderlineVisible(false);

		/////////////////////////////////////////////////////////////////
		// Cancel button
		m_oCancelButton = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oCancelButton, "butCancel");
		m_oCancelButton.allowClick(true);
		m_oCancelButton.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		this.attachChild(m_oCancelButton);
		
		/////////////////////////////////////////////////////////////////
		// Confirm button
		m_oConfirmButton = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oConfirmButton, "butConfirm");
		m_oConfirmButton.allowClick(true);
		m_oConfirmButton.setVisible(false);
		m_oConfirmButton.setValue(AppGlobal.g_oLang.get()._("confirm", ""));
		this.attachChild(m_oConfirmButton);
		
		m_bStockSoldOut = false;
	}
	
	public void setCommonBasketVisible(boolean bShow) {
		m_oCommonBasket.setVisible(bShow);
		m_oConfirmButton.setVisible(bShow);
		
		if(bShow) {
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
				m_oCancelButton.setLeft(52);
				m_oCancelButton.setTop(650);
				m_oConfirmButton.setLeft(212);
			}else {
				m_oCancelButton.setLeft(52);
				m_oCancelButton.setTop(611);
				m_oConfirmButton.setLeft(212);
			}
		} else {
			Integer iLeft = ((this.getWidth() - m_oCancelButton.getWidth()) / 2);
			m_oCancelButton.setLeft(iLeft);
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
				Integer iTop = ((this.getHeight() - m_oCancelButton.getHeight()) / 2);
				m_oCancelButton.setTop(iTop);
			}
		}
	}

	public void resetButtonsDesc() {
		clearSelectedItemBasket();
		m_oCancelButton.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		m_oConfirmButton.setValue(AppGlobal.g_oLang.get()._("confirm", ""));
	}

	public void addItem(int iSectionId, int iId, String sItemName) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			iFieldWidths.add(480);
		else
			iFieldWidths.add(400);
		sFieldValues.add(sItemName);
		sFieldAligns.add("");
		m_oCommonBasket.addItem(iSectionId, m_oSelectedItemList.size(), 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oCommonBasket.setFieldPadding(iSectionId, m_oSelectedItemList.size(), 0, "0,0,0,14");
		m_oCommonBasket.moveScrollToItem(iSectionId, m_oSelectedItemList.size());
		m_oSelectedItemList.add(Integer.valueOf(iId));
	}

	public void addPaymentMethod(int iSectionId, int iId, String sPaymentMethodName, String sPaymentAmount, HashMap<String, String> oOtherInfo) {
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		
		iFieldWidths.add(10);
		sFieldValues.add("");
		sFieldAligns.add("");
		iFieldWidths.add(190);
		sFieldValues.add(sPaymentMethodName);
		sFieldAligns.add("");
		iFieldWidths.add(200);
		sFieldValues.add(sPaymentAmount);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.RIGHT + "," + HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		m_oCommonBasket.addItem(iSectionId, m_oSelectedPaymentDetailList.size(), 0, iFieldWidths, sFieldValues, sFieldAligns, null, null);
		m_oCommonBasket.moveScrollToItem(iSectionId, m_oSelectedPaymentDetailList.size());
	
		ClsSelectedPaymentDetail oSelectedPaymentDetail = new ClsSelectedPaymentDetail(iId, sPaymentAmount, oOtherInfo);
		m_oSelectedPaymentDetailList.add(oSelectedPaymentDetail);
	}
	
	public boolean haveRecord(int iId) {
		return m_oSelectedItemList.contains(Integer.valueOf(iId));
	}

	public List<Integer> getSelectedItemList() {
		return m_oSelectedItemList;
	}
	
	public List<ClsSelectedPaymentDetail> getSelectedPaymentDetail() {
		return m_oSelectedPaymentDetailList;
	}
	
	public boolean getStockSoldOut(){
		return m_bStockSoldOut;
	}
	
	public void setStockSoldOut(boolean bStockSoldOut){
		m_bStockSoldOut = bStockSoldOut;
	}

	public void clearSelectedItemBasket(){
		m_oCommonBasket.removeAllSections();
		
		m_oCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		m_oSelectedItemList.clear();
		m_oSelectedPaymentDetailList.clear();
		
		m_oCommonBasket.setUnderlineFrameVisible(false);
		m_oCommonBasket.setBottomUnderlineVisible(false);
	}
	
	public int getCommonBasketBottom() {
		return this.getTop() + m_oCommonBasket.getTop() + m_oCommonBasket.getHeight();
	}
	
	public void removeItem(int iSectionId, int iId) {
		m_oCommonBasket.removeItem(0, iId);
		if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_ITEM) && m_oSelectedItemList.size() > iId) {
			m_oSelectedItemList.remove(iId);
		} else
		if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_PAYMENT_METHOD)) {
			m_oSelectedPaymentDetailList.remove(iId);
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
	
		if (iChildId == m_oCancelButton.getId()) {
			for (FramePanelItemSelectPanelListener listener : listeners) {
				// Raise the event to parent
				listener.framePanelItemSelectPanel_CancelClicked();
				break;
			}
			bMatchChild = true;
		} else if (iChildId == m_oConfirmButton.getId()) {
			for (FramePanelItemSelectPanelListener listener : listeners) {
				// Raise the event to parent
				listener.framePanelitemSelectPanel_ConfirmClicked();
				break;
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId,
			String sNote) {
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		m_oCommonBasket.removeItem(0, iItemIndex);
		if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_ITEM))
			m_oSelectedItemList.remove(iItemIndex);
		else if (m_sCurrentDisplayType.equals(DISPLAY_TYPE_PAYMENT_METHOD))
			m_oSelectedPaymentDetailList.remove(iItemIndex);
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
}
