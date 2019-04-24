package app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

/** interface for the listeners/observers callback method */
interface FrameLoyaltyCheckBalanceListener {
	void frameLoyaltyCheckBalance_clickOK();
	void frameLoyaltyCheckBalance_clickPrint(FuncCheck oFuncCheck, JSONObject oBalanceDetailJSON);
}

public class FrameLoyaltyCheckBalance extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oLoyaltyCheckBalanceCommonBasket;
	private VirtualUIButton m_oButtonPrint;
	private VirtualUIButton m_oButtonOK;
	private FuncCheck m_oFuncCheck;	
	private JSONObject m_oBalanceDetailJSON;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoyaltyCheckBalanceListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoyaltyCheckBalanceListener listener) {
		listeners.add((FrameLoyaltyCheckBalanceListener) listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoyaltyCheckBalanceListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameLoyaltyCheckBalance(FuncCheck oFuncCheck, LinkedHashMap<String, String> oLoyaltyCheckBalanceValue) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoyaltyCheckBalanceListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLoyaltyCheckBalance.xml");
		
		m_oFuncCheck = oFuncCheck;
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("check_balance"));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		//Check Balance common basket
		m_oLoyaltyCheckBalanceCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oLoyaltyCheckBalanceCommonBasket, "fraLoyaltyCheckBalanceCommonBasket");
		m_oLoyaltyCheckBalanceCommonBasket.init();
		m_oLoyaltyCheckBalanceCommonBasket.addListener(this);
		this.attachChild(m_oLoyaltyCheckBalanceCommonBasket);
		
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();

		//Fields Width
  		List<Integer> oFieldsWidth = new ArrayList<Integer>();
		oFieldsWidth.add(390);
		oFieldsWidth.add(360);
		
		//Add Check Balance Result Common Basket Header
		iFieldWidth.add(oFieldsWidth.get(0));
		sFieldValue.add(AppGlobal.g_oLang.get()._("general_information", ""));
		iFieldWidth.add(oFieldsWidth.get(1));
		sFieldValue.add(AppGlobal.g_oLang.get()._("value", ""));
		
		m_oLoyaltyCheckBalanceCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oLoyaltyCheckBalanceCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//Add Check Balance Result Common Basket Detail
		int iItemIndex = 0;
		for(String oElementKey: oLoyaltyCheckBalanceValue.keySet()) {
			if(!oElementKey.equals("card_number")){
				ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
				ArrayList<String> sFieldValues = new ArrayList<String>();
				ArrayList<String> sFieldAligns = new ArrayList<String>();
				ArrayList<String> sFieldTypes = new ArrayList<String>();
				
				iFieldWidths.add(oFieldsWidth.get(0));
				sFieldValues.add(AppGlobal.g_oLang.get()._(oElementKey));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				iFieldWidths.add(oFieldsWidth.get(1));
				sFieldValues.add(AppGlobal.g_oLang.get()._(oLoyaltyCheckBalanceValue.get(oElementKey)));
				sFieldAligns.add("");
				sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
				
				m_oLoyaltyCheckBalanceCommonBasket.addItem(0, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
				iItemIndex++;
			}
		}
		
		//Print Button
		m_oButtonPrint = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonPrint, "btnPrint");
		m_oButtonPrint.setValue(AppGlobal.g_oLang.get()._("print"));
		this.attachChild(m_oButtonPrint);
		
		//OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		this.attachChild(m_oButtonOK);
		
		//Form Balance Details JSONObject
		m_oBalanceDetailJSON = new JSONObject();
		try {
			m_oBalanceDetailJSON.put("cardNumber", oLoyaltyCheckBalanceValue.get("card_number"));
			if(oLoyaltyCheckBalanceValue.containsKey("balance_point"))
				m_oBalanceDetailJSON.put("balance", oLoyaltyCheckBalanceValue.get("balance_point"));
			else if(oLoyaltyCheckBalanceValue.containsKey("bonus_balance"))
				m_oBalanceDetailJSON.put("balance", oLoyaltyCheckBalanceValue.get("bonus_balance"));
			m_oBalanceDetailJSON.put("cardBalance", oLoyaltyCheckBalanceValue.get("certification_balance"));
			m_oBalanceDetailJSON.put("memberNumber", oLoyaltyCheckBalanceValue.get("member_number"));
			m_oBalanceDetailJSON.put("guestName", oLoyaltyCheckBalanceValue.get("guest_name"));
			m_oBalanceDetailJSON.put("outletId", AppGlobal.g_oFuncOutlet.get().getOutletId());
			m_oBalanceDetailJSON.put("shopId", AppGlobal.g_oFuncOutlet.get().getShopId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oButtonPrint.getId()) {
			// Back button
			for (FrameLoyaltyCheckBalanceListener listener : listeners) {
				// Raise the event to parent
				listener.frameLoyaltyCheckBalance_clickPrint(m_oFuncCheck, m_oBalanceDetailJSON);
			}
			bMatchChild = true;
		} else if (iChildId == m_oButtonOK.getId()) {
			// Done button
			for (FrameLoyaltyCheckBalanceListener listener : listeners) {
				// Raise the event to parent
				listener.frameLoyaltyCheckBalance_clickOK();
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
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameLoyaltyCheckBalanceListener listener : listeners) {
			// Raise the event to parent
			listener.frameLoyaltyCheckBalance_clickOK();
		}
	}
}