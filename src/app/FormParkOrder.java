package app;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import commonui.FormDialogBox;
import core.Controller;
import virtualui.VirtualUIFrame;
import externallib.StringLib;

import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosCheckItem;
import om.PosTaxScType;
import om.PosTaxScTypeList;
import om.PosDisplayPanelZone;
import om.PosStation;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;

public class FormParkOrder extends VirtualUIForm implements FrameParkOrderListener {
	private TemplateBuilder m_oTemplateBuilder;
	private FrameParkOrder m_oFrameParkOrder;
	
	private String m_sSelectedFileName;
	private boolean m_bCancelByUser;
	private String m_sParkMethod;
	
	private ArrayList<String> m_oParkOrderFileNameList;
	
	private PosDisplayPanelZone m_oOrderingBasketDisplayPanelZone;
	
	public static final String SELECTED_PARKING = "s";
	public static final String UNSELECT_PARKING = "u";
	
	public FormParkOrder(Controller oParentController, PosDisplayPanelZone oOrderingBasketDisplayPanelZone) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmParkOrder.xml");
		
		VirtualUIFrame oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameCover, "fraCoverFrame");
		this.attachChild(oFrameCover);
		
		m_oOrderingBasketDisplayPanelZone = oOrderingBasketDisplayPanelZone;
		
		// Park Order Frame
		m_oFrameParkOrder = new FrameParkOrder(m_oOrderingBasketDisplayPanelZone);
		m_oTemplateBuilder.buildFrame(m_oFrameParkOrder, "fraParkOrder");
		m_oFrameParkOrder.addListener(this);
		this.attachChild(m_oFrameParkOrder);
		
		m_sSelectedFileName = "";
		m_sParkMethod = "";
		m_bCancelByUser = false;
		
	}
	
	public void setParkMethod(String sMethod){
		m_sParkMethod = sMethod;
	}
	
	public boolean isUserCancel(){
		return m_bCancelByUser;
	}
	
	public String getSelectedFileName(){
		return m_sSelectedFileName;
	}
	
	public void init(ArrayList<String> oParkOrderFileNameList){
		m_oParkOrderFileNameList = oParkOrderFileNameList;
		String sStationName = "";
		
		// Create the list for selection
		ArrayList<String> oParkOrderTimeList = new ArrayList<String>();
		ArrayList<String> oParkOrderEmployeeNameList = new ArrayList<String>();
		ArrayList<String> oParkOrderStationNameList = new ArrayList<String>();
		
		//get station list
		HashMap<Integer, PosStation> oPosStationMapList = AppGlobal.g_oPosStationList.getPosStationMapList();
		
		for(int i=0; i<m_oParkOrderFileNameList.size(); i++){
			String split[] = m_oParkOrderFileNameList.get(i).split("_");
			if(split.length < 4)
				continue;
			
			String sTime = "";
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
				Date d = formatter.parse(split[1]);
				((SimpleDateFormat) formatter).applyPattern("HH:mm:ss");
				sTime = formatter.format(d);
			} catch (ParseException e) {
				e.printStackTrace();
				AppGlobal.stack2Log(e);
			}
			
			oParkOrderTimeList.add(sTime);
			oParkOrderEmployeeNameList.add(split[3]);
			sStationName = oPosStationMapList.get(Integer.parseInt(split[2])).getName(AppGlobal.g_oCurrentLangIndex.get());
			oParkOrderStationNameList.add(sStationName);
		}
		
		m_oFrameParkOrder.addParkingOrder(oParkOrderTimeList, oParkOrderEmployeeNameList, oParkOrderStationNameList);
		
		if (!m_oParkOrderFileNameList.isEmpty()){
			// Default load the first park order
			String sParkOrderFileName = m_oParkOrderFileNameList.get(0);
			this.loadParkingOrder(sParkOrderFileName);
			
			// Store the selected file
			m_sSelectedFileName = sParkOrderFileName;
			
			m_oFrameParkOrder.setParkingOrderBackground(0, this.SELECTED_PARKING);
		}
	}
	
	private void loadParkingOrder(String sParkOrderFileName){
		FuncParkOrder oFuncParkOrder = new FuncParkOrder();
		FuncCheck oFuncCheck = new FuncCheck();
		if(m_sParkMethod.equals(FuncParkOrder.PARK_ORDER_BY_STATION)){
			if(oFuncParkOrder.loadParkOrderByStation(oFuncCheck, sParkOrderFileName, false)){
				// Load success
				this.drawOrderingBasket(oFuncCheck);
			}else{
				// Fail to load park order
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("fail_to_retrieve_parking_order"));
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
		}else{
			if(oFuncParkOrder.loadParkOrderByOutlet(oFuncCheck,AppGlobal.g_oFuncOutlet.get().getOutletId(), sParkOrderFileName, false)){
				// Load success
				this.drawOrderingBasket(oFuncCheck);
			}else{
				// Fail to load park order
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("parking_order_has_been_retrieved_by_other_station"));
				oFormDialogBox.show();
				oFormDialogBox = null;
			}
		}
	}
	
	private void drawOrderingBasket(FuncCheck oFuncCheck) {
		
		m_oFrameParkOrder.getItemBasket().removeAllSections();
		if(!m_oOrderingBasketDisplayPanelZone.isDisplaySeatNumberSection())
			m_oFrameParkOrder.getItemBasket().addSection(0, AppGlobal.g_oLang.get()._("shared", ""), false);
		else
			m_oFrameParkOrder.getItemBasket().addSection(0, AppGlobal.g_oLang.get()._("shared", ""), true);
		for(int i = 1; i <= AppGlobal.MAX_SEATS; i++) {
			//add new section and not visible
			m_oFrameParkOrder.getItemBasket().addSection(i, AppGlobal.g_oLang.get()._("seat", " ", i), false);
		}
		m_oFrameParkOrder.getItemBasket().setSelectedSection(0);
		
		for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
			// Create ordering basket section for seat
			ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getItemList(i);
			
			if(oFuncCheckItemList != null && !oFuncCheckItemList.isEmpty()) {					//Has ordered items in seat i
				if(!m_oFrameParkOrder.getItemBasket().isSectionVisible(i)) {
					if(!m_oOrderingBasketDisplayPanelZone.isDisplaySeatNumberSection())
						m_oFrameParkOrder.getItemBasket().setSectionVisible(i, false);
					else
						m_oFrameParkOrder.getItemBasket().setSectionVisible(i, true);
				}
				else {
					if(!m_oOrderingBasketDisplayPanelZone.isDisplaySeatNumberSection())
						m_oFrameParkOrder.getItemBasket().setSectionVisible(i, false);
				}
				m_oFrameParkOrder.getItemBasket().setSelectedSection(i);
				
				for (int j=0; j<oFuncCheckItemList.size(); j++) {
					FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
					PosCheckItem oPosCheckItem = oFuncCheckItem.getCheckItem();
					
					// Add normal item to ordering basket
					boolean bIsChildItem = false;
					if(oFuncCheckItem.isSetMenuItem())
						bIsChildItem = true;
					
					String[] sNameArray = new String[AppGlobal.LANGUAGE_COUNT];
					for (int k=0; k<AppGlobal.LANGUAGE_COUNT; k++) {
						sNameArray[k] = oFuncCheckItem.getBilingualItemDescriptionByIndex(k+1);
					}
					m_oFrameParkOrder.getItemBasket().addItem(i, j, oPosCheckItem.getQty(), BigDecimal.ZERO, sNameArray, AppGlobal.g_oFuncOutlet.get().getItemRoundDecimal(), BigDecimal.ZERO, true, bIsChildItem, 0, false, oPosCheckItem.getSeq());
					this.updateBasketItemInfo(oFuncCheck, i, j);
				}
			}
		}

		// After finish drawing the basket, update each item's price
		for(int i = 0; i <= AppGlobal.MAX_SEATS; i++) {
			// Create ordering basket section for seat
			ArrayList<FuncCheckItem> oFuncCheckItemList = (ArrayList<FuncCheckItem>)oFuncCheck.getItemList(i);
			
			if(oFuncCheckItemList != null && !oFuncCheckItemList.isEmpty()) {					//Has ordered items in seat i
				for (int j=0; j<oFuncCheckItemList.size(); j++) {
					FuncCheckItem oFuncCheckItem = oFuncCheckItemList.get(j);
					updateBasketItemPrice(oFuncCheck, i, j, oFuncCheckItem);
				}
			}
		}
		
		// Set the selected basket to shared
		m_oFrameParkOrder.getItemBasket().setSelectedSection(0);
		
		// Move the scroll view to the last insert node
		m_oFrameParkOrder.getItemBasket().moveScrollToSectionBottom(0);
	}
	
	private void updateBasketItemInfo(FuncCheck oFuncCheck, int iSeatNo, int iItemIndex) {
		FuncCheckItem oFuncCheckItem = oFuncCheck.getCheckItem(iSeatNo, iItemIndex);
		
		String[] sStatusArray = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		String[] sDiscountArray = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		String[] sDisplayInformationArray = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		
		ArrayList<PosTaxScType> sScTaxAddedInformationArray = new ArrayList<PosTaxScType>();
		ArrayList<PosTaxScType> sScTaxWaivedInformationArray = new ArrayList<PosTaxScType>();
		
		PosTaxScTypeList oScList = new PosTaxScTypeList();
		oScList.getAllServiceCharges();
		HashMap<Integer, PosTaxScType> oScTypeList = oScList.getTaxScTypeList();
		PosTaxScTypeList oTaxList = new PosTaxScTypeList();
		oTaxList.getAllTaxes();
		HashMap<Integer, PosTaxScType> oTaxTypeList = oTaxList.getTaxScTypeList();
		
		// Get the record of extra info
		if(oFuncCheckItem.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0)) {
			
			JSONObject oAddWaiveObject = new JSONObject();
			JSONObject oScObject = new JSONObject();
			JSONObject oTaxObject = new JSONObject();
			
			try {
				PosCheckExtraInfo oPosCheckExtraInfo = oFuncCheckItem.getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0);
				oAddWaiveObject = new JSONObject(oPosCheckExtraInfo.getValue());
				
				// Get the records from extra info
				oScObject = oAddWaiveObject.optJSONObject("sc");
				if (oScObject != null) {
					for (int j = 1; j <= 5; j++) {
						String sValue = oScObject.optString(Integer.toString(j), "");
						if (sValue.isEmpty())
							continue;
						
						// Sc(s) Label
						if (sValue.equals("a") && oScTypeList.containsKey(j))
							sScTaxAddedInformationArray.add(oScTypeList.get(j));
						
						if (sValue.equals("w") && oScTypeList.containsKey(j))
							sScTaxWaivedInformationArray.add(oScTypeList.get(j));
					}
				}

				oTaxObject = oAddWaiveObject.optJSONObject("tax");
				if (oTaxObject != null) {
					for (int j = 1; j <= 25; j++) {
						String sValue = oTaxObject.optString(Integer.toString(j), "");
						if (sValue.isEmpty())
							continue;
						
						// Tax(s) Label
						if (sValue.equals("a") && oTaxTypeList.containsKey(j))
							sScTaxAddedInformationArray.add(oTaxTypeList.get(j));
						
						if (sValue.equals("w") && oTaxTypeList.containsKey(j))
							sScTaxWaivedInformationArray.add(oTaxTypeList.get(j));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for (int i=0; i<AppGlobal.LANGUAGE_COUNT; i++) {
			String sStatus = "";
			// Check if item is delivered or not
			if(oFuncCheckItem.isPreorderItem())
				sStatus = AppGlobal.g_oLang.get()._("pre_order", "")[i];
			
			if(oFuncCheckItem.hasDelivered()){
				if(!sStatus.isEmpty())
					sStatus += ", ";
				sStatus += AppGlobal.g_oLang.get()._("delivered", "")[i];
			}
			
			if(oFuncCheckItem.isPendingItem()) {
				if(!sStatus.isEmpty())
					sStatus += ", ";
				sStatus += AppGlobal.g_oLang.get()._("pending", "")[i];
			}
			
			if(oFuncCheckItem.isTakeoutItem()){
				if(!sStatus.isEmpty())
					sStatus += ", ";
				sStatus += AppGlobal.g_oLang.get()._("takeout", "")[i];
			}
			
			if(oFuncCheckItem.isCookingTimeOverdue()){ 
				if (!sStatus.isEmpty())
					sStatus += ", ";
				sStatus += AppGlobal.g_oLang.get()._("cooking_overtime", "")[i];
			}
			
			if(oFuncCheckItem.isNoKitchenSlip()){ 
				if (!sStatus.isEmpty())
					sStatus += ", ";
				sStatus += AppGlobal.g_oLang.get()._("no_kitchen_slip", "")[i];
			}
			
			sStatusArray[i] = sStatus;
			
			// Add display information to the basket
			String sDisplayInformation = "";
			
			if (oFuncCheckItem.hasDisplayInformation()) {
				for (String sDisplayInfo : oFuncCheckItem.getDisplayInformationList()) {
					if (!sDisplayInformation.isEmpty())
						sDisplayInformation += ", ";
					sDisplayInformation += sDisplayInfo;
				}
			}
			sDisplayInformationArray[i] = sDisplayInformation;
			
			// Add the discount to the basket
			StringBuilder sDiscountList = new StringBuilder();
			if(oFuncCheckItem.hasItemDiscount(false)) {
				for(int k = 0; k < oFuncCheckItem.getItemDiscountList().size(); k++) {
					PosCheckDiscount oCheckDiscount = oFuncCheckItem.getItemDiscountList().get(k);
					
					if(sDiscountList.length() > 0){
						sDiscountList.append(", ");
					}
					
					if (oCheckDiscount.getShortName(i + 1).isEmpty())
						sDiscountList.append(oCheckDiscount.getName(i + 1));
					else
						sDiscountList.append(oCheckDiscount.getShortName(i + 1));
				}
			}
			
			sDiscountArray[i] = sDiscountList.toString();
		}
		
		m_oFrameParkOrder.getItemBasket().updateInfo(iSeatNo, iItemIndex, sStatusArray, sDiscountArray, oFuncCheckItem.getModifierList(), null, sDisplayInformationArray, sScTaxAddedInformationArray, sScTaxWaivedInformationArray);
	}
	
	private void updateBasketItemPrice(FuncCheck oFuncCheck, int iSeatNo, int iItemIndex, FuncCheckItem oFuncCheckItem){
		BigDecimal dNewPrice = oFuncCheckItem.getCheckItem().getRoundTotal().add(oFuncCheckItem.getCheckItem().getPreDisc().add(oFuncCheckItem.getCheckItem().getMidDisc().add(oFuncCheckItem.getCheckItem().getPostDisc())));
		String sNewPrice = AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(dNewPrice);
		
		if(AppGlobal.g_oFuncStation.get().IsDisplayTaxInItems())
			dNewPrice = dNewPrice.add(oFuncCheckItem.getTaxTotal(true));
		
		m_oFrameParkOrder.getItemBasket().setNewPrice(iSeatNo, iItemIndex, sNewPrice);
		
		// If the item is child, update parent item's basket display
		if(oFuncCheckItem.isSetMenuItem()){
			FuncCheckItem oParentFuncCheckItem = oFuncCheckItem.getParentFuncCheckItem();
			int iParentSeatNo = oParentFuncCheckItem.getCheckItem().getSeatNo();
			int iParentItemIndex = 0;
			for(FuncCheckItem oFuncCheckItem2:oFuncCheck.getItemList(iParentSeatNo)){
				if(oFuncCheckItem2 == oParentFuncCheckItem){
					// Locate the parent item to update basket
					updateBasketItemPrice(oFuncCheck, iParentSeatNo, iParentItemIndex, oFuncCheckItem2);
					break;
				}
				iParentItemIndex++;
			}
		}
	}
	
	@Override
	public void FrameParkOrder_clickConfirm() {
		
		m_bCancelByUser = false;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void FrameParkOrder_clickCancel() {
		
		m_bCancelByUser = true;
		
		// Finish showing this form
		this.finishShow();
	}

	@Override
	public void frameParkOrder_clickParkingOrder(int iRecordIndex) {
		if(iRecordIndex < m_oParkOrderFileNameList.size()){
			String sParkOrderFileName = m_oParkOrderFileNameList.get(iRecordIndex);
			this.loadParkingOrder(sParkOrderFileName);
			
			// Store the selected file
			m_sSelectedFileName = sParkOrderFileName;
			
			for(int i=0; i<m_oParkOrderFileNameList.size(); i++){
				if(i == iRecordIndex)
					m_oFrameParkOrder.setParkingOrderBackground(i, this.SELECTED_PARKING);
				else
					m_oFrameParkOrder.setParkingOrderBackground(i, this.UNSELECT_PARKING);
			}
		}
	}
}

