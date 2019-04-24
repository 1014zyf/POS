package app;

import java.util.ArrayList;
import java.util.List;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameConfirmOrderDialogListener {
	void frameConfirmOrderDialog_clickClose();
	void frameConfirmOrderDialog_clickBack(String sTable, String sTableExtension);
	void frameConfirmOrderDialog_timeout();
}

public class FrameConfirmOrderDialog extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUILabel m_oLabelCheckTotalTitle;
	private VirtualUILabel m_oLabelCheckTotal;
	private FrameCommonBasket m_oOrderSummaryCommonBasket;
	private VirtualUIButton m_oButtonBack;
	private VirtualUIButton m_oButtonDone;
	public final int m_iPageRecordCount = 8;
	private FuncCheck m_oFuncCheck;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameConfirmOrderDialogListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameConfirmOrderDialogListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameConfirmOrderDialogListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameConfirmOrderDialog(FuncCheck oFuncCheck) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameConfirmOrderDialogListener>();
		
		m_oFuncCheck = oFuncCheck;
		
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraConfirmOrderDialog.xml");
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("order_summary", ""));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		//Order list common basket
		m_oOrderSummaryCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oOrderSummaryCommonBasket, "fraOrderListCommonBasket");
		m_oOrderSummaryCommonBasket.init();
		m_oOrderSummaryCommonBasket.addListener(this);
		this.attachChild(m_oOrderSummaryCommonBasket);
		
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();
		
		//Fields Width
  		List<Integer> oFieldsWidth = new ArrayList<Integer>();
		oFieldsWidth.add(490);
		oFieldsWidth.add(260);
		oFieldsWidth.add(210);
		
		//Add Drawer List Common Basket Header
		iFieldWidth.add(oFieldsWidth.get(0));
		sFieldValue.add(AppGlobal.g_oLang.get()._("item", ""));
		iFieldWidth.add(oFieldsWidth.get(1));
		sFieldValue.add(AppGlobal.g_oLang.get()._("quantity", ""));
		iFieldWidth.add(oFieldsWidth.get(2));
		sFieldValue.add(AppGlobal.g_oLang.get()._("price", ""));
		
		m_oOrderSummaryCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oOrderSummaryCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		
		String sCurrencySign = "$";
		if(!AppGlobal.g_oFuncOutlet.get().getCurrencySign().isEmpty())
			sCurrencySign = AppGlobal.g_oFuncOutlet.get().getCurrencySign();
		
		List<List<FuncCheckItem>> oPartyWholeItems = oFuncCheck.getWholeItemList();
		for (List<FuncCheckItem> oItemListForSingleSeat : oPartyWholeItems) {
			for (int iItemIndex = 0; iItemIndex < oItemListForSingleSeat.size(); iItemIndex++) {
				ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
				ArrayList<String> sFieldValues = new ArrayList<String>();
				ArrayList<String> sFieldAligns = new ArrayList<String>();
				ArrayList<String> sFieldTypes = new ArrayList<String>();
				
				//item name
				iFieldWidths.add(oFieldsWidth.get(0));
				sFieldValues.add(oItemListForSingleSeat.get(iItemIndex).getMenuItemName(AppGlobal.g_oCurrentLangIndex.get()));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				//item quantity
				iFieldWidths.add(oFieldsWidth.get(1));
				sFieldValues.add(oItemListForSingleSeat.get(iItemIndex).getCheckItem().getQty().stripTrailingZeros().toString());
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				//item price
				iFieldWidths.add(oFieldsWidth.get(1));
				String sPrice = StringLib.BigDecimalToString(oItemListForSingleSeat.get(iItemIndex).getNetItemTotal(), AppGlobal.g_oFuncOutlet.get().getItemRoundDecimal());
				sFieldValues.add(sCurrencySign + sPrice);
				sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				m_oOrderSummaryCommonBasket.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
			}
		}
		
		//Check Total Title
		m_oLabelCheckTotalTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotalTitle, "lblCheckTotalTitle");
		m_oLabelCheckTotalTitle.setValue(AppGlobal.g_oLang.get()._("check_total", ""));
		this.attachChild(m_oLabelCheckTotalTitle);
		
		//Check Total
		m_oLabelCheckTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
		String sCheckTotal = StringLib.BigDecimalToString(oFuncCheck.getCheckTotal(), AppGlobal.g_oFuncOutlet.get().getCheckRoundDecimal());
		m_oLabelCheckTotal.setValue(sCurrencySign + sCheckTotal);

		this.attachChild(m_oLabelCheckTotal);
		
		//Back Button
		m_oButtonBack = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonBack, "btnBack");
		m_oButtonBack.setValue(AppGlobal.g_oLang.get()._("back_to_order"));
		this.attachChild(m_oButtonBack);
		
		//Done Button
		m_oButtonDone = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonDone, "btnDone");
		m_oButtonDone.setValue(AppGlobal.g_oLang.get()._("pay"));
		this.attachChild(m_oButtonDone);
		
	}
	
	// Create timer
	public void setConfirmOrderDialogTimeout(int iTimeout){
		this.addTimer("confirm_order_dialog_timeout", iTimeout, false, "confirm_order_dialog_timeout", true, true, null);
	}
	
	// Start timer
	public void setConfirmOrderDialogTimeoutTimer(boolean bStart){
		this.controlTimer("confirm_order_dialog_timeout", bStart);
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if (sNote.equals("confirm_order_dialog_timeout")){
				// Cashier timeout
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameConfirmOrderDialogListener listener : listeners) {
					// Raise the event to parent
					listener.frameConfirmOrderDialog_timeout();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonBack.getId()) {
			// Back button
			for (FrameConfirmOrderDialogListener listener : listeners) {
				listener.frameConfirmOrderDialog_clickBack(m_oFuncCheck.getTableNo(), m_oFuncCheck.getTableExtension());
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonDone.getId()) {
			// Done button
			for (FrameConfirmOrderDialogListener listener : listeners) {
				listener.frameConfirmOrderDialog_clickClose();
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
		
	}
}
