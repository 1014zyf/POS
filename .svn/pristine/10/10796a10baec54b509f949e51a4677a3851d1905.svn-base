package app;

import om.MenuItemPrintQueue;
import om.MenuItemPrintQueueList;
import om.MenuMediaObject;
import om.PosCheckDiscount;
import om.PosCheckExtraInfo;
import om.PosTaxScType;
import om.PosTaxScTypeList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosCheckItem;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameItemDetailListener {
	void FrameItemDetail_click();
	void FrameItemDetail_orderClick();
}

public class FrameItemDetail extends VirtualUIFrame implements FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIFrame m_FrameImage;
	private VirtualUIImage m_oImageItem;
	private VirtualUILabel m_oLabelBasicDetail;
	private VirtualUIFrame m_oFrameBasicDetailUnderLineTop;
	private VirtualUIFrame m_oFrameBasicDetailUnderLineBottom;
	private VirtualUIList m_oListBasicDetail;
	private VirtualUIButton m_oButtonOrder;
	private VirtualUILabel m_oLabelDiscInfo;
	private VirtualUIFrame m_oFrameDiscInfoUnderLineTop;
	private VirtualUIFrame m_oFrameDiscInfoUnderLineBottom;
	private VirtualUIFrame m_oFrameDiscInfo;
	private VirtualUIList m_oItemDiscList;
	private VirtualUILabel m_oLabelItemDetail;
	private VirtualUIFrame m_oFrameItemDetailUnderLineTop;
	private VirtualUIFrame m_oFrameItemDetailUnderLineBottom;
	private VirtualUIList m_oListItemDescription;
//KingsleyKwan20171013ByKing	-----Start-----
	private VirtualUIFrame m_oFrameBasicDetailBox;
	private VirtualUIFrame m_oFrameDiscountBox;
	private VirtualUIFrame m_oFrameDescBox;
//KingsleyKwan20171013ByKing	----- End -----
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameItemDetailListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameItemDetailListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameItemDetailListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameItemDetail(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameItemDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraItemDetail.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
	    m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
	    m_oFrameTitleHeader.init(true);
	    m_oFrameTitleHeader.addListener(this);
	    this.attachChild(m_oFrameTitleHeader);
				
		// Item Image Frame
		m_FrameImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_FrameImage, "fraImage");
		this.attachChild(m_FrameImage);
		
		// Item image
		m_oImageItem = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageItem, "imgItem");
		m_FrameImage.attachChild(m_oImageItem);
		
//KingsleyKwan20171013ByKing	-----Start-----
		VirtualUIFrame oFrameLeftInfoCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLeftInfoCover, "fraLeftInfo");
		oFrameLeftInfoCover.setTop(oFrameLeftInfoCover.getTop() + 50);
		oFrameLeftInfoCover.setHeight(oFrameLeftInfoCover.getHeight() - 50);
		oFrameLeftInfoCover.setCornerRadius("0");
		this.attachChild(oFrameLeftInfoCover);
		
		VirtualUIFrame oFrameLeftInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLeftInfo, "fraLeftInfo");
		this.attachChild(oFrameLeftInfo);
		
		VirtualUIFrame oFrameRightInfoCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameRightInfoCover, "fraLeftInfo");
		oFrameRightInfoCover.setTop(oFrameRightInfoCover.getTop() + 100);
		oFrameRightInfoCover.setHeight(oFrameRightInfoCover.getHeight() - 100);
		oFrameRightInfoCover.setCornerRadius("0");
		this.attachChild(oFrameRightInfoCover);

		VirtualUIFrame oFrameRightInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameRightInfo, "fraRightInfo");
		this.attachChild(oFrameRightInfo);
		
		m_oFrameBasicDetailBox = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBasicDetailBox, "fraBasicDetailBox");
		this.attachChild(m_oFrameBasicDetailBox);

		
		// Basic Description Label
		m_oLabelBasicDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBasicDetail, "lblBasicDetail");
		m_oLabelBasicDetail.setValue(AppGlobal.g_oLang.get()._("basic_information"));
		m_oFrameBasicDetailBox.attachChild(m_oLabelBasicDetail);
		m_oFrameBasicDetailUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBasicDetailUnderLineTop, "lblBasicDetailUnderLineTop");
		m_oFrameBasicDetailBox.attachChild(m_oFrameBasicDetailUnderLineTop);
		
		// Basic Description List
		VirtualUIFrame oFrameListBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameListBasicDetail, "fraListBasicDetail");
		m_oFrameBasicDetailBox.attachChild(oFrameListBasicDetail);
		
		m_oListBasicDetail = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListBasicDetail, "listBasicDetail");
		oFrameListBasicDetail.attachChild(m_oListBasicDetail);

		// Order Button
		m_oButtonOrder = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOrder, "butOrder");
		m_oButtonOrder.setValue(AppGlobal.g_oLang.get()._("order"));
		m_oButtonOrder.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonOrder);
		
		m_oFrameDiscountBox = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDiscountBox, "fraDiscountBox");
		this.attachChild(m_oFrameDiscountBox);
		
		// Discount Info Title
		m_oLabelDiscInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDiscInfo, "lblDiscountInfo");
		m_oLabelDiscInfo.setValue(AppGlobal.g_oLang.get()._("discount_info"));
		m_oFrameDiscountBox.attachChild(m_oLabelDiscInfo);
		m_oFrameDiscInfoUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDiscInfoUnderLineTop, "lblDiscountInfoUnderLineTop");
		m_oFrameDiscountBox.attachChild(m_oFrameDiscInfoUnderLineTop);
		
		// Item Description Frame
		m_oFrameDiscInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDiscInfo, "fraDiscInfo");
		m_oFrameDiscountBox.attachChild(m_oFrameDiscInfo);
		
		// Item Discount Scroll Frame
		m_oItemDiscList = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oItemDiscList, "listItemDisc");
		m_oFrameDiscInfo.attachChild(m_oItemDiscList);
		
		m_oFrameDescBox = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDescBox, "fraDescBox");
		this.attachChild(m_oFrameDescBox);
		
		// Item Description Title
		m_oLabelItemDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelItemDetail, "lblItemDetail");
		m_oLabelItemDetail.setValue(AppGlobal.g_oLang.get()._("item_description"));
		m_oFrameDescBox.attachChild(m_oLabelItemDetail);
		m_oFrameItemDetailUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameItemDetailUnderLineTop, "lblItemDetailUnderLineTop");
		m_oFrameDescBox.attachChild(m_oFrameItemDetailUnderLineTop);
//KingsleyKwan20171013ByKing	----- End -----
		// Item Description Frame
		m_oListItemDescription = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListItemDescription, "listItemDescription");
		m_oFrameDescBox.attachChild(m_oListItemDescription);
	}
	
	public void setupValue(FuncCheckItem oFuncCheckItem, int iPriceLevel, boolean bOrderingPanel, int iRoundItemDecimal, List<String> oMixAndMatchRuleAndItemList){
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
		
		PosCheckItem oPosCheckItem = oFuncCheckItem.getCheckItem();
		
		//m_oLabelItemName.setValue(oFuncCheckItem.getItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()));
		m_oFrameTitleHeader.setTitle(oFuncCheckItem.getBilingualItemDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()));
		if(AppGlobal.g_oFuncOutlet.get().getBilingualLangIndexByeLangIndex(AppGlobal.g_oCurrentLangIndex.get()) > 0)
			m_oFrameTitleHeader.setTextSize(22);
		
		// setup PLU
		addItem(AppGlobal.g_oLang.get()._("plu")+":", oPosCheckItem.getCode());
		
		BigDecimal dTotalInclusiveTax = BigDecimal.ZERO;
		if(AppGlobal.g_oFuncStation.get().getSeparateInclusiveTaxOnDisplay()){
			if(!(oPosCheckItem.getQty().compareTo(BigDecimal.ONE) == 0))
				dTotalInclusiveTax = oFuncCheckItem.getInclusiveTaxTotal(false).divide(oPosCheckItem.getQty().stripTrailingZeros(),AppGlobal.g_oFuncOutlet.get().getBusinessDay().getItemDecimal(),RoundingMode.HALF_UP);
			else
				dTotalInclusiveTax = AppGlobal.g_oFuncOutlet.get().roundTaxAmountToBigDecimal(oFuncCheckItem.getInclusiveTaxTotal(false));
		}
		
		if(bOrderingPanel) {
//KingsleyKwan20171013ByKing	-----Start-----
			m_oFrameDiscountBox.setVisible(false);
			m_oFrameDescBox.setTop(210);
			m_oFrameDescBox.setLeft(m_oFrameBasicDetailBox.getLeft());
			m_oFrameDescBox.setWidth(m_oFrameBasicDetailBox.getWidth());
			m_oFrameItemDetailUnderLineTop.setWidth(m_oFrameBasicDetailUnderLineTop.getWidth());
			m_oButtonOrder.setVisible(true);
			
			//calculate the display item price for inclusive tax
			if(AppGlobal.g_oFuncStation.get().getSeparateInclusiveTaxOnDisplay()){
				BigDecimal dItemPrice = AppGlobal.g_oFuncOutlet.get().roundItemAmountToBigDecimal(oPosCheckItem.getPrice().subtract(dTotalInclusiveTax));
				addItem(AppGlobal.g_oLang.get()._("price")+":", StringLib.BigDecimalToString(dItemPrice, iRoundItemDecimal));
			}
			else
				addItem(AppGlobal.g_oLang.get()._("price")+":", StringLib.BigDecimalToString(oPosCheckItem.getPrice(), iRoundItemDecimal));
		}else {
			m_oLabelDiscInfo.setVisible(true);
			m_oFrameDiscInfoUnderLineTop.setVisible(true);
			m_oFrameDiscInfo.setVisible(true);
			m_oItemDiscList.setVisible(true);
			m_oButtonOrder.setVisible(false);
//KingsleyKwan20171013ByKing	----- End -----
			// setup Price
			addItem(AppGlobal.g_oLang.get()._("price")+":", StringLib.BigDecimalToString(oPosCheckItem.getPrice(), iRoundItemDecimal));
			
			// setup Original Price
			if(!oPosCheckItem.getPrice().equals(oPosCheckItem.getOriginalPrice()))
				addItem(AppGlobal.g_oLang.get()._("original_price")+":", StringLib.BigDecimalToString(oPosCheckItem.getOriginalPrice(), iRoundItemDecimal));
			
			// setup Quantity
			addItem(AppGlobal.g_oLang.get()._("quantity")+":", oPosCheckItem.getQty().stripTrailingZeros().toPlainString());
			
			// setup Order Time
			if(oFuncCheckItem.hasRush())
				addItem(AppGlobal.g_oLang.get()._("order_time")+":", formatter.print(oPosCheckItem.getOrderLocTime()));

			// setup Order User
			String sOrderItemUserName = oFuncCheckItem.getOrderItemUserName(AppGlobal.g_oCurrentLangIndex.get());
			if(sOrderItemUserName.length() > 0)
				addItem(AppGlobal.g_oLang.get()._("order_by")+":", sOrderItemUserName);
			
			// setup Delivery Time
			if(oFuncCheckItem.hasDelivered()){
				//Change to local time
				String sLocalTime = "";
				DateTimeFormatter dFormat = DateTimeFormat.forPattern("HH:mm:ss");
				DateTime dLocalDate = AppGlobal.convertTimeToLocal(oPosCheckItem.getDeliveryTime());
				sLocalTime  = dFormat.print(dLocalDate);
				addItem(AppGlobal.g_oLang.get()._("delivery_time")+":", sLocalTime);
			}
			
			// setup Seat
			addItem(AppGlobal.g_oLang.get()._("seat")+":", Integer.toString(oPosCheckItem.getSeatNo()));
			
			// setup Course
			//if(oPosCheckItem.getCourseId() > 0)
			//	addItem(AppGlobal.g_oLang.get()._("Course:"), Integer.toString(oPosCheckItem.getCourseId()));
			if(oPosCheckItem.isTakeoutOrderingType())
				addItemImage(AppGlobal.g_oLang.get()._("takeout")+":");

			JSONObject oAddWaiveObject = new JSONObject();
			JSONObject oScObject = new JSONObject();
			JSONObject oTaxObject = new JSONObject();
			
			// Get the record of extra info
			if(oFuncCheckItem.isExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0)) {
				try {
					PosCheckExtraInfo oPosCheckExtraInfo = oFuncCheckItem.getExtraInfoExistBySectionVariableAndIndex(PosCheckExtraInfo.SECTION_ITEM, PosCheckExtraInfo.VARIABLE_ADD_WAIVE_TAX_SC, 0);
					oAddWaiveObject = new JSONObject(oPosCheckExtraInfo.getValue());
					
					// Get the records from extra info
					oScObject = oAddWaiveObject.optJSONObject("sc");
					oTaxObject = oAddWaiveObject.optJSONObject("tax");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// setup Service Charge list
			PosTaxScTypeList oScList = new PosTaxScTypeList();
			oScList.getAllServiceCharges();
			HashMap<Integer, PosTaxScType> oScTypeList = oScList.getTaxScTypeList();
			if(oPosCheckItem.haveTaxSc(false)) {
				List<String> oScNameList = new ArrayList<String>();
				for(int i = 1; i <= 5; i++) {
					// Sc(s) Label
					BigDecimal oSc = oPosCheckItem.getSc(i); 
					if(oSc.compareTo(BigDecimal.ZERO) > 0 && oScTypeList.containsKey(i)) {
						if(oScObject != null && oScObject.has(String.valueOf(i)) && oScObject.optString(String.valueOf(i)).equals("a"))
							oScNameList.add("- " + oScTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("added")+")");
						else
							oScNameList.add("- " + oScTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()));
					}
					
					if (oScObject != null && oScObject.has(String.valueOf(i))
							&& oScObject.optString(String.valueOf(i)).equals("w"))
						oScNameList.add("- " + oScTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("waived")+")");
				}
				addList(AppGlobal.g_oLang.get()._("service_charges")+":", oScNameList);
			} else {
				List<String> oScNameList = new ArrayList<String>();
				
				for (int i = 1; i <= 5; i++) {
					// Sc(s) Label
					String oSc = oFuncCheckItem.getMenuItem().getChargeSc(i - 1);
					if (!oSc.equals(PosCheckItem.CHARGE_SC_NO) && oScTypeList.get(i) != null)
						oScNameList.add("- " + oScTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("waived")+")");
				}
				if (!oScNameList.isEmpty())
					addList(AppGlobal.g_oLang.get()._("service_charges")+":", oScNameList);
				else
					addItem(AppGlobal.g_oLang.get()._("service_charges")+":", "["+AppGlobal.g_oLang.get()._("no_selected_service_charges")+"]");
			}
			
			// setup Tax list
			PosTaxScTypeList oTaxList = new PosTaxScTypeList();
			oTaxList.getAllTaxes();
			HashMap<Integer, PosTaxScType> oTaxTypeList = oTaxList.getTaxScTypeList();
			if(oPosCheckItem.haveTaxSc(true)) {
				List<String> oTaxNameList = new ArrayList<String>();
				for (int i = 1; i <= 25; i++) {
					// Tax(s) Label
					BigDecimal oTax = oPosCheckItem.getTax(i);
					if (oTax.compareTo(BigDecimal.ZERO) > 0 && oTaxTypeList.containsKey(i)) {
						if (oTaxObject != null && oTaxObject.has(String.valueOf(i)) && oTaxObject.optString(String.valueOf(i)).equals("a"))
							oTaxNameList.add("- " + oTaxTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("added")+")");
						else
							oTaxNameList.add("- " + oTaxTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()));
					}
					
					if(oTaxObject != null && oTaxObject.has(String.valueOf(i)) && oTaxObject.optString(String.valueOf(i)).equals("w"))
						oTaxNameList.add("- " + oTaxTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("waived")+")");
				}
				addList(AppGlobal.g_oLang.get()._("taxs")+":", oTaxNameList);
			} else {
				List<String> oTaxNameList = new ArrayList<String>();
				
				for (int i = 1; i <= 25; i++) {
					// Tax(s) Label
					String sTax = oFuncCheckItem.getMenuItem().getChargeTax(i - 1);
					if (!sTax.equals(PosCheckItem.CHARGE_TAX_NO) && oTaxTypeList.get(i) != null)
						oTaxNameList.add("- " + oTaxTypeList.get(i).getName(AppGlobal.g_oCurrentLangIndex.get()) + " ("+AppGlobal.g_oLang.get()._("waived")+")");
				}
				if (!oTaxNameList.isEmpty())
					addList(AppGlobal.g_oLang.get()._("taxs")+":", oTaxNameList);
				else
					addItem(AppGlobal.g_oLang.get()._("taxs")+":", "["+AppGlobal.g_oLang.get()._("no_selected_taxs")+"]");
			}

			// setup Print Queue list
			if (oPosCheckItem.havePrintQueue()) {
				MenuItemPrintQueueList oMenuItmPrtQueueList = new MenuItemPrintQueueList();
				oMenuItmPrtQueueList.readItemQueueList();

				List<String> oPrintQueueNameList = new ArrayList<String>();
				for(int i = 1; i <= 10; i++) {
					// Print Queue(s) Label
					int iPrintQueueId = oPosCheckItem.getPrintQueueWithIndex(i);
					if(iPrintQueueId > 0) {
						MenuItemPrintQueue oMenuItmPrtQueue = oMenuItmPrtQueueList.getItemPrintQueueById(iPrintQueueId);
						
						if(oMenuItmPrtQueue != null)
							oPrintQueueNameList.add("- " + oMenuItmPrtQueue.getName(AppGlobal.g_oCurrentLangIndex.get()));
					}
				}
				addList(AppGlobal.g_oLang.get()._("print_queues")+":", oPrintQueueNameList);
			} else
				addItem(AppGlobal.g_oLang.get()._("print_queues")+":", "["+AppGlobal.g_oLang.get()._("no_selected_print_queues")+"]");

			// setup Modifier list
			if (oFuncCheckItem.getModifierList().isEmpty())
				addItem(AppGlobal.g_oLang.get()._("modifiers")+":", "["+AppGlobal.g_oLang.get()._("no_selected_modifiers")+"]");
			else {
				List<String> oModifierNameList = new ArrayList<String>();
				for (FuncCheckItem oModiItem:oFuncCheckItem.getModifierList()) {
					// Modifier(s) Label
					oModifierNameList.add("- " + oModiItem.getItemShortDescriptionByIndex(AppGlobal.g_oCurrentLangIndex.get()));
				}
				addList(AppGlobal.g_oLang.get()._("modifiers")+":", oModifierNameList);
			}
			
			// setup mix and match rule and item
			if(oMixAndMatchRuleAndItemList != null && !oMixAndMatchRuleAndItemList.isEmpty())
				addList(AppGlobal.g_oLang.get()._("mix_rule")+":", oMixAndMatchRuleAndItemList);
			
			// setup pending item
			if(oFuncCheckItem.isPendingItem())
				addItemImage(AppGlobal.g_oLang.get()._("pending_item")+":");
		}
		
		
		// setup the Item Image
		if (oFuncCheckItem.getMenuItem().getMediaUrl(MenuMediaObject.USED_FOR_MAIN_PCITURE) != null)
			m_oImageItem.setSource(oFuncCheckItem.getMenuItem().getMediaUrl(MenuMediaObject.USED_FOR_MAIN_PCITURE));
		else {
			m_oImageItem.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/item_detail_image.png");
			VirtualUILabel oLabelEmptyImage = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelEmptyImage, "lblEmptyImage");
			oLabelEmptyImage.setValue(oFuncCheckItem.getItemShortDescriptionByIndex(1));
			m_FrameImage.attachChild(oLabelEmptyImage);
		}
		
		// setup the Discount List
		if (oFuncCheckItem.hasItemDiscount(false) == false) {
			VirtualUILabel oLabelDisc = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelDisc, "lblItemDisc");
			oLabelDisc.setValue("["+AppGlobal.g_oLang.get()._("no_selected_discounts")+"]");
			m_oItemDiscList.attachChild(oLabelDisc);
		}
		else {
			for (PosCheckDiscount oPosCheckDiscount:oFuncCheckItem.getItemDiscountList()) {
				// Discount(s) Label
				VirtualUILabel oLabelDisc = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabelDisc, "lblItemDisc");
				String sDiscountName = "- ";
				if(oPosCheckDiscount.getShortName(AppGlobal.g_oCurrentLangIndex.get()).isEmpty())
					sDiscountName += oPosCheckDiscount.getName(AppGlobal.g_oCurrentLangIndex.get());
				else
					sDiscountName += oPosCheckDiscount.getShortName(AppGlobal.g_oCurrentLangIndex.get());
				
				// show disount value precentage/fix amount
				if(oPosCheckDiscount.isPercentageDiscountMethod()) {
					BigDecimal dRate = oPosCheckDiscount.getRate().multiply(new BigDecimal("-100.0"));
					sDiscountName = sDiscountName.concat(" ("+dRate.stripTrailingZeros().toPlainString()+"%)");
				}else if(oPosCheckDiscount.isFixAmountDiscountMethod()) {
					BigDecimal dRate = oPosCheckDiscount.getFixAmount().multiply(new BigDecimal("-1.0"));
					sDiscountName = sDiscountName.concat(" ("+StringLib.BigDecimalToString(dRate, AppGlobal.g_oFuncOutlet.get().getBusinessDay().getCheckDecimal())+")");
				}
				oLabelDisc.setValue(sDiscountName);
				m_oItemDiscList.attachChild(oLabelDisc);
			}
		}
		
		// Item description
		if (oFuncCheckItem.getMenuItem().getDescription1(AppGlobal.g_oCurrentLangIndex.get()) != null
				|| oFuncCheckItem.getMenuItem().getDescription1(AppGlobal.g_oCurrentLangIndex.get()) != null) {
			addItemDescription(AppGlobal.g_oLang.get()._("description")+"1: ", oFuncCheckItem.getMenuItem().getDescription1(AppGlobal.g_oCurrentLangIndex.get()));
			addItemDescription(AppGlobal.g_oLang.get()._("description")+"2: ", oFuncCheckItem.getMenuItem().getDescription2(AppGlobal.g_oCurrentLangIndex.get()));
		}
	}
	
	private void addItem(String sTitle, String sContent) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraBasicDetail");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUILabel oLabelContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelContent, "lblContent");
		oLabelContent.setValue(sContent);
		oFrameBasicDetail.attachChild(oLabelContent);
				
		m_oListBasicDetail.attachChild(oFrameBasicDetail);
	}
	
	private void addItemImage(String sTitle) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraBasicDetail");
		this.attachChild(oFrameBasicDetail);
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUIImage oImageContent = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImageContent, "imgContent");
		oImageContent.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/basket_selected_image.png");
		oLabelTitle.setHeight(oImageContent.getHeight());
		oFrameBasicDetail.setHeight(oImageContent.getHeight());
		oFrameBasicDetail.attachChild(oImageContent);
		
		m_oListBasicDetail.attachChild(oFrameBasicDetail);
	}
	
	private void addList(String sTitle, List<String> oContentList) {
		VirtualUIFrame oFrameBasicDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameBasicDetail, "fraBasicDetail");
		
		VirtualUILabel oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelTitle, "lblTitle");
		oLabelTitle.setValue(sTitle);
		oFrameBasicDetail.attachChild(oLabelTitle);

		VirtualUIList oListContent = new VirtualUIList();
		m_oTemplateBuilder.buildList(oListContent, "listContent");
		
		int iHeight = 0;
		for(String sContent: oContentList) {
			VirtualUILabel oLabelContent = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelContent, "lblItemContent");
			oLabelContent.setValue(sContent);
			oListContent.attachChild(oLabelContent);
			
			iHeight += oLabelContent.getHeight();
		}
		oListContent.setHeight(iHeight);
		oFrameBasicDetail.attachChild(oListContent);
		oFrameBasicDetail.setHeight(iHeight+2);
		
		m_oListBasicDetail.attachChild(oFrameBasicDetail);
	}
	
	private void addItemDescription(String sTitle, String sContent) {
		VirtualUIFrame oFrameItemDescriptionDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameItemDescriptionDetail, "fraItemDescription");
		
		VirtualUILabel oLabelDescriptionTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelDescriptionTitle, "lblDescriptionTitle");
		oLabelDescriptionTitle.setValue(sTitle);
		oFrameItemDescriptionDetail.attachChild(oLabelDescriptionTitle);
		
		VirtualUILabel oLabelDescriptionContent = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelDescriptionContent, "lblDescriptionContent");
		oLabelDescriptionContent.setValue(sContent);
		oFrameItemDescriptionDetail.attachChild(oLabelDescriptionContent);
		
		m_oListItemDescription.attachChild(oFrameItemDescriptionDetail);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		for (FrameItemDetailListener listener : listeners) {
			// Find the clicked button
			if (m_oButtonOrder.getId() == iChildId) {
				// Raise the event to parent
				listener.FrameItemDetail_orderClick();
				bMatchChild = true;
				break;
			}
		}
		
		return bMatchChild;
	}
	
	public void setOrderButtonVisibility(boolean bVisible) {
		m_oButtonOrder.setVisible(bVisible);
	}
	
	@Override
	public void FrameTitleHeader_close() {
		for (FrameItemDetailListener listener : listeners) {
			// Raise the event to parent
			listener.FrameItemDetail_click();
		}
	}
	
}
