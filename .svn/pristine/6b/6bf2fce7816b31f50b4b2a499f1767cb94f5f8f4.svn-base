package app;

import java.math.BigDecimal;
import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosDisplayPanelZone;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameParkOrderListener {
	void frameParkOrder_clickParkingOrder(int iRecordIndex);

	void FrameParkOrder_clickConfirm();

	void FrameParkOrder_clickCancel();
}

public class FrameParkOrder extends VirtualUIFrame implements FrameCommonBasketListener, FrameOrderingBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUIFrame m_oFrameRightContent;
	private FrameCommonBasket m_oParkOrderCommonBasket;
	private FrameOrderingBasket m_oParkOrderItemBasket;
	private VirtualUIFrame m_oFrameSelected;
	private VirtualUIFrame m_oFrameUnselect;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameParkOrderListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameParkOrderListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameParkOrderListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	// constructor
	public FrameParkOrder(PosDisplayPanelZone oPosDisplayPanelZone) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameParkOrderListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraParkOrder.xml");
		
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("parking_order"));
		this.attachChild(m_oFrameTitleHeader);
		
		//Left Content
		m_oFrameLeftContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "fraLeftContent");
		this.attachChild(m_oFrameLeftContent);
		
		//Right Content
		m_oFrameRightContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameRightContent, "fraRightContent");
		this.attachChild(m_oFrameRightContent);
		
		// Park order list
		m_oParkOrderCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oParkOrderCommonBasket, "fraParkOrderBasket");
		m_oParkOrderCommonBasket.init();
		m_oParkOrderCommonBasket.addListener(this);
		m_oFrameLeftContent.attachChild(m_oParkOrderCommonBasket);
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() / 4);
		sFieldValues.add(AppGlobal.g_oLang.get()._("parking_time"));
		iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() / 3);
		sFieldValues.add(AppGlobal.g_oLang.get()._("employee"));
		iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() * 5 / 12);
		sFieldValues.add(AppGlobal.g_oLang.get()._("station_name"));
		m_oParkOrderCommonBasket.addHeader(iFieldWidths, sFieldValues);
		m_oParkOrderCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		m_oParkOrderCommonBasket.setBottomUnderlineVisible(false);
		
		// Item Basket
		m_oParkOrderItemBasket = new FrameOrderingBasket();
		m_oTemplateBuilder.buildFrame(m_oParkOrderItemBasket, "fraItemDetailBasket");
		m_oParkOrderItemBasket.init(new PosDisplayPanelZone(), FrameOrderingBasket.ORDERING_BASKET_MODE_SHOW_NORMAL);
		
		// Add listener;
		m_oParkOrderItemBasket.addListener(this);
		m_oParkOrderItemBasket.setTextSize(16);
		m_oFrameRightContent.attachChild(m_oParkOrderItemBasket);

		// Confirm button
		m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "btnOK");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		this.attachChild(m_oButtonConfirm);
		
		// Selected Parking
		m_oFrameSelected = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSelected, "fraSelected");
		
		// Unselected Parking
		m_oFrameUnselect = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnselect, "fraUnselect");
	}
	
	public void addParkingOrder(ArrayList<String> oParkOrderTimeList, ArrayList<String> oParkOrderEmployeeNameList, ArrayList<String> oParkOrderStationNameList) {
		for (int i = 0; i < oParkOrderTimeList.size(); i++) {
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();

			iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() / 4);
			sFieldValues.add(oParkOrderTimeList.get(i));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

			iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() / 3);
			sFieldValues.add(oParkOrderEmployeeNameList.get(i));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

			iFieldWidths.add(m_oParkOrderCommonBasket.getWidth() * 5 / 12);
			sFieldValues.add(oParkOrderStationNameList.get(i));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			m_oParkOrderCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			m_oParkOrderCommonBasket.setFieldTextSize(0, i, 0, 16);
			m_oParkOrderCommonBasket.setFieldTextSize(0, i, 1, 16);
			m_oParkOrderCommonBasket.setFieldTextSize(0, i, 2, 16);
		}
	}
	
	public FrameOrderingBasket getItemBasket() {
		return m_oParkOrderItemBasket;
	}
	
	public void setParkingOrderBackground(int iRecordIndex, String sParkingStatus) {
		if(sParkingStatus.equals(FormParkOrder.SELECTED_PARKING)){
			for(int i = 0 ; i < 3 ; i ++){
				m_oParkOrderCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, m_oFrameSelected.getBackgroundColor());
				m_oParkOrderCommonBasket.setFieldForegroundColor(0, iRecordIndex, i, m_oFrameSelected.getForegroundColor());
			}
		} else if(sParkingStatus.equals(FormParkOrder.UNSELECT_PARKING)){
			for(int i = 0 ; i < 3 ; i ++){
				m_oParkOrderCommonBasket.setFieldBackgroundColor(0, iRecordIndex, i, m_oFrameUnselect.getBackgroundColor());
				m_oParkOrderCommonBasket.setFieldForegroundColor(0, iRecordIndex, i, m_oFrameUnselect.getForegroundColor());
			}
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oButtonConfirm.getId()) {
			for (FrameParkOrderListener listener : listeners) {
				// Raise the event to parent
				listener.FrameParkOrder_clickConfirm();
			}

			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {

		for (FrameParkOrderListener listener : listeners) {
			// Raise the event to parent
			listener.frameParkOrder_clickParkingOrder(iItemIndex);
		}
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {

	}

	@Override
	public void frameOrderingBasketSection_SectionClicked(int iSectionId, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_QtyClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public String frameOrderingBasketCell_TempQtyClicked(int iSectionId, int iItemIndex, String sNote, String sOriQty) {
		return null;
	}

	@Override
	public void frameOrderingBasketCell_ReceiveQtyClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_DescClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_DescLongClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_PriceClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_DeleteClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public void frameOrderingBasketCell_AddQtyClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public boolean frameOrderingBasketCell_AddQtyClickedForOldItem(int iSectionId, int iItemIndex, String sNote, BigDecimal dNewQty) {
		return false;
	}

	@Override
	public void frameOrderingBasketCell_MinusQtyClicked(int iSectionId, int iItemIndex, String sNote) {

	}

	@Override
	public boolean frameOrderingBasketCell_MinusQtyClickedForOldItem(int iSectionId, int iItemIndex, String sNote, BigDecimal dNewQty) {
		return false;
	}

	@Override
	public void frameOrderingBasketChildItemCell_QtyClicked(int iSectionId, int iItemIndex, int iChildItemIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasketChildItemCell_DescClicked(int iSectionId, int iItemIndex, int iChildItemIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasketChildItemCell_PriceClicked(int iSectionId, int iItemIndex, int iChildItemIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasketModifierCell_QtyClicked(int iSectionId, int iItemIndex, int iModifierIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasketModifierCell_DescClicked(int iSectionId, int iItemIndex, int iModifierIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasketModifierCell_PriceClicked(int iSectionId, int iItemIndex, int iModifierIndex,
			String sNote) {

	}

	@Override
	public void frameOrderingBasket_UpdateBasket() {

	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameParkOrderListener listener : listeners) {
			// Raise the event to parent
			listener.FrameParkOrder_clickCancel();
		}
	}
}
