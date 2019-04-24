package app;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosCheckGratuity;
import om.PosGratuity;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameAskGratuityListener {
	void frameAskGratuity_cancelFrame();
	void frameAskGratuity_finishFrame();
	BigDecimal frameAskGratuity_askAmount(boolean bIsPercentage);
	String frameAskGratuity_askName();
	boolean frameAskGratuity_askAuth(int iUserGroupId);
}

public class FrameAskGratuity extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;
	
	private FrameCommonBasket m_oFrameGratuityList;
	
	private VirtualUIFrame m_oFrameTotalInfo;
	private VirtualUILabel m_oLabelSubTotalHeader;
	private VirtualUILabel m_oLabelGratTotalHeader;
	private VirtualUILabel m_oLabelCheckTotalHeader;
	private VirtualUILabel m_oLabelSubTotal;
	private VirtualUILabel m_oLabelGratTotal;
	private VirtualUILabel m_oLabelCheckTotal;
	private VirtualUIFrame m_oFrameTotalUnderline;
	private VirtualUIFrame m_oFrameTotalStraightLine1;
	private VirtualUIFrame m_oFrameTotalStraightLine2;
	
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIButton m_oButtonFinish;
	
	private BigDecimal m_oSubTotal;
	private BigDecimal m_oCheckTotalWithoutGratuity;
	private String m_sBasketSelectedImageSource;
	private String m_sBasketDeselectedImageSource;
	private List<PosCheckGratuity> m_oCheckGratuityList;
	
	
	private double m_oOrginalGrat;
	private double m_oUpdatedGrat;
	private ArrayList<Integer> m_oGratuityTraList;
	private ArrayList<Integer> m_oOrgGratuityTraList;
	private HashMap<Integer, String> m_oGraValue;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAskGratuityListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAskGratuityListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAskGratuityListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameAskGratuity(){
		m_oGratuityTraList = new ArrayList<Integer>();
		m_oOrgGratuityTraList = new ArrayList<Integer>();
		m_oGraValue = new HashMap<Integer, String >();
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAskGratuityListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAskGratuity.xml");
		
		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("please_select_gratuities"));
		this.attachChild(m_oFrameTitleHeader);
		
		// Finish button
		m_oButtonFinish = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonFinish, "btnFinish");
		m_oButtonFinish.setValue(AppGlobal.g_oLang.get()._("confirm"));
		this.attachChild(m_oButtonFinish);
		
		////////////////////////////// Result list
		m_oFrameGratuityList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameGratuityList, "fraGratuityList");
		m_oFrameGratuityList.init();
		m_oFrameGratuityList.addListener(this);
		this.attachChild(m_oFrameGratuityList);
		
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add(50);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5*2);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		
		sFieldValues.add("");
		sFieldValues.add(AppGlobal.g_oLang.get()._("name"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("type"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("gratuity_value"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("calculated_gratuity"));
		
		m_oFrameGratuityList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameGratuityList.setHeaderFormat(40, 16, "");
		m_oFrameGratuityList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		//////////////////////////////// Result list
		
		//////////////////////////////// Total Info list
		m_oFrameTotalInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTotalInfo, "fraTotalInfo");
		this.attachChild(m_oFrameTotalInfo);
		
		//Sub Total Header
		m_oLabelSubTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSubTotalHeader, "lblSubTotalHeader");
		m_oLabelSubTotalHeader.setValue(AppGlobal.g_oLang.get()._("sub_total"));
		m_oFrameTotalInfo.attachChild(m_oLabelSubTotalHeader);
		
		//Gratuity Total Header
		m_oLabelGratTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelGratTotalHeader, "lblGratTotalHeader");
		m_oLabelGratTotalHeader.setValue(AppGlobal.g_oLang.get()._("gratuity_total"));
		m_oFrameTotalInfo.attachChild(m_oLabelGratTotalHeader);
		
		//Check Total Header
		m_oLabelCheckTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotalHeader, "lblCheckTotalHeader");
		m_oLabelCheckTotalHeader.setValue(AppGlobal.g_oLang.get()._("check_total"));
		m_oFrameTotalInfo.attachChild(m_oLabelCheckTotalHeader);

		//Sub Total
		m_oLabelSubTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSubTotal, "lblSubTotal");
		m_oFrameTotalInfo.attachChild(m_oLabelSubTotal);
		
		//Grat Total
		m_oLabelGratTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelGratTotal, "lblGratTotal");
		m_oFrameTotalInfo.attachChild(m_oLabelGratTotal);

		//Check Total
		m_oLabelCheckTotal = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
		m_oFrameTotalInfo.attachChild(m_oLabelCheckTotal);
		
		//Underline
		m_oFrameTotalUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTotalUnderline, "fraTotalUnderline");
		m_oFrameTotalInfo.attachChild(m_oFrameTotalUnderline);
		
		//Straightline 1
		m_oFrameTotalStraightLine1 = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTotalStraightLine1, "fraTotalStrLine1");
		m_oFrameTotalInfo.attachChild(m_oFrameTotalStraightLine1);
		
		//Straightline 2
		m_oFrameTotalStraightLine2 = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTotalStraightLine2, "fraTotalStrLine2");
		m_oFrameTotalInfo.attachChild(m_oFrameTotalStraightLine2);
		////////////////////////////////Total Info list
		
		m_oSubTotal = BigDecimal.ZERO;
		m_oCheckTotalWithoutGratuity = BigDecimal.ZERO;
		m_sBasketSelectedImageSource = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_h.png";
		m_sBasketDeselectedImageSource = AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png";
		m_oCheckGratuityList = new ArrayList<PosCheckGratuity>();
	}
	
	public void init(BigDecimal dSubTotal, BigDecimal dCheckTotalWithoutGratuity, List<PosCheckGratuity> oPosCheckGratuityList){
		String sName = "", sMethod = "", sRate = "", sFixAmount = "", sCalculatedValue = "";
		m_oCheckGratuityList = oPosCheckGratuityList;
		int iIndex = 0;
		List<PosGratuity> m_oPosGratuityList = AppGlobal.g_oFuncOutlet.get().getPosGratuityList();
		for(PosGratuity oPosGratuity:m_oPosGratuityList) {
			if((oPosGratuity.getMethod().equals(PosGratuity.METHOD_PERCENTAGE) && oPosGratuity.getRate().compareTo(BigDecimal.ZERO) == 0))
				this.m_oGraValue.put(iIndex, AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oPosGratuity.getRate().multiply(new BigDecimal("100.0"))));
			else if(oPosGratuity.getMethod().equals(PosGratuity.METHOD_FIX_AMOUNT) && oPosGratuity.getFixAmount().compareTo(BigDecimal.ZERO) == 0)
				this.m_oGraValue.put(iIndex, AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oPosGratuity.getFixAmount()));
			
			iIndex++;
		}
		
		// Add name, method and calculated tips for each record
		for(int i = 0; i < AppGlobal.g_oFuncOutlet.get().getPosGratuityList().size(); i++){
			sName = sMethod = sRate = sFixAmount = sCalculatedValue = "";
			PosGratuity oGratuity = AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(i);
			// Name
			sName = oGratuity.getName()[AppGlobal.g_oCurrentLangIndex.get()-1];
			// Calculated tips
			if (oGratuity.getMethod().equals(PosGratuity.METHOD_PERCENTAGE)){
				if (oGratuity.getRate().compareTo(BigDecimal.ZERO) > 0)
					sCalculatedValue = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dSubTotal.multiply(oGratuity.getRate()));
			}else if (oGratuity.getMethod().equals(PosGratuity.METHOD_FIX_AMOUNT)){
				if (oGratuity.getFixAmount().compareTo(BigDecimal.ZERO) > 0)
					sCalculatedValue = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oGratuity.getFixAmount());
			}
			
			// Method
			if (oGratuity.getMethod().equals(PosGratuity.METHOD_PERCENTAGE)) {
				sMethod = AppGlobal.g_oLang.get()._("percentage");
				if (sCalculatedValue.isEmpty())
					sMethod = AppGlobal.g_oLang.get()._("open_percentage");
			}else {
				sMethod = AppGlobal.g_oLang.get()._("fix_amount");
				if (sCalculatedValue.isEmpty())
					sMethod = AppGlobal.g_oLang.get()._("open_fix_amount");
			}
			sMethod = AppGlobal.g_oLang.get()._(sMethod);
			if (oGratuity.getRate().compareTo(BigDecimal.ZERO) > 0)
				sRate = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oGratuity.getRate().multiply(new BigDecimal("100.0")));
			if (oGratuity.getFixAmount().compareTo(BigDecimal.ZERO) > 0)
				sFixAmount = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oGratuity.getFixAmount());
			
			addRecord(0, i, sName, sMethod, sRate, sFixAmount, sCalculatedValue);
			checkSelectedRecord(0, i, oGratuity);
		}
		
		// Update Sub. & Gratuity & Check Total Label Value
		m_oSubTotal = dSubTotal;
		m_oCheckTotalWithoutGratuity = dCheckTotalWithoutGratuity;
		ShowTotalLabelValue();
	}
	
	public void addRecord(int iSectionId, int iItemIndex, String sName, String sMethod, String sRate, String sFixAmount, String sCalculatedValue){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		
		// Gratuity: Tick Icon, Name, Type, Amount, Calculated Value
		iFieldWidths.add(50);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5*2);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		iFieldWidths.add((m_oFrameGratuityList.getWidth()-50)/5);
		
		sFieldValues.add(m_sBasketDeselectedImageSource);
		sFieldValues.add(sName);
		sFieldValues.add(sMethod);
		if (sMethod.equals(AppGlobal.g_oLang.get()._("open_percentage")) || sMethod.equals(AppGlobal.g_oLang.get()._("open_fix_amount")))
			sFieldValues.add("");
		else if (sMethod.equals(AppGlobal.g_oLang.get()._("percentage")))
			sFieldValues.add(sRate + "%");
		else
			sFieldValues.add(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sFixAmount);
		if (!sCalculatedValue.isEmpty())
			sCalculatedValue = AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sCalculatedValue;
		sFieldValues.add(sCalculatedValue);
		
		sFieldAligns.add("");
		sFieldAligns.add("");
		sFieldAligns.add("");
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.RIGHT);
		
		sFieldTypes.add(HeroActionProtocol.View.Type.IMAGE);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		ArrayList<VirtualUIBasicElement> oSubmitIdElements = new ArrayList<VirtualUIBasicElement>();
		m_oFrameGratuityList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, oSubmitIdElements);
		m_oFrameGratuityList.setFieldTextSize(iSectionId, iItemIndex, 0, 16);
		m_oFrameGratuityList.setFieldTextSize(iSectionId, iItemIndex, 1, 16);
		m_oFrameGratuityList.setFieldTextSize(iSectionId, iItemIndex, 2, 16);
		m_oFrameGratuityList.setFieldTextSize(iSectionId, iItemIndex, 3, 16);
		m_oFrameGratuityList.setFieldTextSize(iSectionId, iItemIndex, 4, 16);
		m_oFrameGratuityList.moveScrollToItem(iSectionId, iItemIndex);
	}
	
	public void checkSelectedRecord(int iSectionId, int iItemIndex, PosGratuity oGratuity){
		// Compare saved record will default gratuity options
		for (PosCheckGratuity oPosCheckGratuity: m_oCheckGratuityList){
			if (oPosCheckGratuity.getGratId() == oGratuity.getGratId() && oPosCheckGratuity.getStatus().equals(PosCheckGratuity.STATUS_ACTIVE)){
				// Restore previous entered value for name
				if (oGratuity.getInputName().equals(PosGratuity.INPUT_NAME_APPEND_DESC) || oGratuity.getInputName().equals(PosGratuity.INPUT_NAME_OPEN_DESC) )
					m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 1, oPosCheckGratuity.getName(AppGlobal.g_oCurrentLangIndex.get()));
				
				// Restore previous entered value for open tips
				if  ((oGratuity.getMethod().equals(PosGratuity.METHOD_PERCENTAGE) && oGratuity.getRate().compareTo(BigDecimal.ZERO) == 0))
					m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 3, AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oPosCheckGratuity.getRate().multiply(new BigDecimal("100.0"))) + "%");
				else if ((oGratuity.getMethod().equals(PosGratuity.METHOD_FIX_AMOUNT) && oGratuity.getFixAmount().compareTo(BigDecimal.ZERO) == 0))
					m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 3, AppGlobal.g_oFuncOutlet.get().getCurrencySign() + 
							AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oPosCheckGratuity.getTotal()));
				m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 4, AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(oPosCheckGratuity.getRoundTotal()));
				if(oPosCheckGratuity.getStatus().equals(oGratuity.STATUS_ACTIVE)) {
					
					  System.out.println("checkSelectedRecord():"+iItemIndex);
					  this.m_oGratuityTraList.add(iItemIndex);
					  this.m_oOrgGratuityTraList.add(iItemIndex);
					
				}
				//
				// Check if the record marked delete
				if (oPosCheckGratuity.getStatus().equals(PosCheckGratuity.STATUS_DELETED)){
					m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 0, m_sBasketDeselectedImageSource);
					m_oFrameGratuityList.setAllFieldsBackgroundColor(iSectionId, iItemIndex, "FFFFFF");
				}else
					m_oFrameGratuityList.setFieldValue(iSectionId, iItemIndex, 0, m_sBasketSelectedImageSource);
				break;
			}
		}
	}
	
	private void ShowTotalLabelValue(){
		// Show SubTotal, Gratuity Total and Check Total
		BigDecimal dGratuityTotal = BigDecimal.ZERO;
		for (PosCheckGratuity oPosCheckGratuity: m_oCheckGratuityList){
			if (oPosCheckGratuity.getStatus().equals(PosCheckGratuity.STATUS_ACTIVE))
				dGratuityTotal = dGratuityTotal.add(oPosCheckGratuity.getRoundTotal());
		}
		
		m_oLabelSubTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(m_oSubTotal));
		m_oLabelGratTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dGratuityTotal));
		m_oLabelCheckTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(m_oCheckTotalWithoutGratuity.add(dGratuityTotal)));
	 	this.m_oOrginalGrat = Double.parseDouble(m_oLabelGratTotal.getValue().replaceAll("[^\\d.]", ""));
	}
	
	private void UpdateTotalLabelValue(){
		// Update SubTotal, Gratuity Total, Check Total
		BigDecimal dGratuityTotal = BigDecimal.ZERO;
		for (PosCheckGratuity oPosCheckGratuity: m_oCheckGratuityList){
			if (oPosCheckGratuity.getStatus().equals(PosCheckGratuity.STATUS_ACTIVE))
				dGratuityTotal = dGratuityTotal.add(oPosCheckGratuity.getRoundTotal());
		}
		
		m_oLabelSubTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundItemAmountToString(m_oSubTotal));
		m_oLabelGratTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dGratuityTotal));
		m_oLabelCheckTotal.setValue(AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(m_oCheckTotalWithoutGratuity.add(dGratuityTotal)));
		this.m_oUpdatedGrat = Double.parseDouble(m_oLabelGratTotal.getValue().replaceAll("[^\\d.]", ""));
	}
	
	public double getGratuityChange() {
		if(this.m_oOrginalGrat == 0 && this.m_oUpdatedGrat == 0)
			return 0;
		else {
			double returnValue = this.m_oUpdatedGrat - this.m_oOrginalGrat;
			return returnValue;
		}
	}
	
	public ArrayList<Integer> getGratuityTraList(){
		return this.m_oGratuityTraList;
	}
	
	public ArrayList<Integer> getOrgGratuityTraList(){
		return this.m_oOrgGratuityTraList;
	}
	
	public HashMap<Integer,String> getGratuityValue() {
		return this.m_oGraValue;
	}
	
	public List<PosCheckGratuity> getCheckGratuityList() {
		return m_oCheckGratuityList;
	}
	
	public void setCheckGratuityList(List<PosCheckGratuity> oCheckGratuityList) {
		this.m_oCheckGratuityList = oCheckGratuityList;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (m_oButtonFinish.getId() == iChildId) {
			for (FrameAskGratuityListener listener : listeners) {
				// Raise the event to parent
				listener.frameAskGratuity_finishFrame();
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
	}
	
	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex,
			int iItemIndex, int iFieldIndex, String sNote) {
		if(this.m_oGratuityTraList.contains(iItemIndex))
			this.m_oGratuityTraList.remove(this.m_oGratuityTraList.indexOf(iItemIndex));
		else
			this.m_oGratuityTraList.add(iItemIndex);
		
		BigDecimal dInputValue = BigDecimal.ZERO, dTipsAmount = BigDecimal.ZERO, dRoundedTipsAmount = BigDecimal.ZERO;
		String sTipsAmount = "", sRounderTipsAmount = "", sInputName = "";
		String sName[] = new String[5], sShortName[] = new String[5];
		boolean bIsPercentage = false, bRowSelected = false, bRowDeselected = false, bRecordFound = false, bIsOpenValue = false, bIsOpenName = false, bIsAppendName = false;
		
		for (FrameAskGratuityListener listener : listeners) {
			if (iBasketId != m_oFrameGratuityList.getId())
				break;
			if(listener.frameAskGratuity_askAuth(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getUserGroupId()) == false)
				break;
			// Check open method
			if  ((AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getMethod().equals(PosGratuity.METHOD_PERCENTAGE) &&
					AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getRate().compareTo(BigDecimal.ZERO) == 0) ||
					 (AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getMethod().equals(PosGratuity.METHOD_FIX_AMOUNT) &&
							 AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getFixAmount().compareTo(BigDecimal.ZERO) == 0))
				bIsOpenValue = true;
			// Check input name type
			if (AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getInputName().equals(PosGratuity.INPUT_NAME_OPEN_DESC))
				bIsOpenName = true;
			else if (AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getInputName().equals(PosGratuity.INPUT_NAME_APPEND_DESC))
				bIsAppendName = true;
			// Check percentage method
			bIsPercentage = m_oFrameGratuityList.getFieldValue(iSectionIndex, iItemIndex, 2).equals(AppGlobal.g_oLang.get()._("percentage")) ||
					m_oFrameGratuityList.getFieldValue(iSectionIndex, iItemIndex, 2).equals(AppGlobal.g_oLang.get()._("open_percentage"));
			
			// Name field clicked
			if (iFieldIndex == 1 && (bIsOpenName || bIsAppendName)){
				sInputName = listener.frameAskGratuity_askName();
				if (sInputName.isEmpty())
					break;
				if (bIsOpenName){
					for (int i=0; i<5; i++){
						sName[i] = sInputName;
						sShortName[i] = sInputName;
					}
					m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 1, sInputName);
				}else{
					for (int i=0; i<5; i++){
						sName[i] = AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getName()[i] + sInputName;
						sShortName[i] = AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getShortName()[i] + sInputName;
					}
					m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 1, sName[AppGlobal.g_oCurrentLangIndex.get()-1]);
				}
			}
			
			// Value field clicked
			if ((iFieldIndex == 3 || m_oFrameGratuityList.getFieldValue(iSectionIndex, iItemIndex, 3).isEmpty()) && bIsOpenValue){
				// Raise the event to parent
				dInputValue = listener.frameAskGratuity_askAmount(bIsPercentage);
				if (dInputValue.compareTo(BigDecimal.ZERO) <= 0)
					break;
				dInputValue = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToBigDecimal(dInputValue);
				
				if (bIsPercentage == true){
					dTipsAmount = m_oSubTotal.multiply(dInputValue.divide(new BigDecimal("100.0")));
					sTipsAmount = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dInputValue) + "%";
				}else{
					dTipsAmount = dInputValue;
					sTipsAmount = AppGlobal.g_oFuncOutlet.get().getCurrencySign() + AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dInputValue);
				}
				
				dRoundedTipsAmount = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToBigDecimal(dTipsAmount);
				// Set Value for open value
				if (bIsOpenValue){
					sRounderTipsAmount = AppGlobal.g_oFuncOutlet.get().roundGratuityAmountToString(dTipsAmount);
					m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 3, sTipsAmount);
					m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 4, AppGlobal.g_oFuncOutlet.get().getCurrencySign() + sRounderTipsAmount);
				}
				m_oFrameGratuityList.setFieldForegroundColor(iSectionIndex, iItemIndex, 3, "0000FF");
				bRowSelected = true;
			}
			
			// Check if the row is selected or deselected
			if (m_oFrameGratuityList.getFieldValue(iSectionIndex, iItemIndex, 0).equals(m_sBasketDeselectedImageSource) ||
					((bIsOpenName || bIsAppendName) && !sInputName.isEmpty()) ){
				m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 0, m_sBasketSelectedImageSource);
				bRowSelected = true;
			}else if (m_oFrameGratuityList.getFieldValue(iSectionIndex, iItemIndex, 0).equals(m_sBasketSelectedImageSource) && !bRowSelected){
				m_oFrameGratuityList.setFieldValue(iSectionIndex, iItemIndex, 0, m_sBasketDeselectedImageSource);
				m_oFrameGratuityList.setAllFieldsBackgroundColor(iSectionIndex, iItemIndex, "FFFFFF");
				bRowDeselected = true;
			}
			
			// Update Check Gratuity List for any action
			if (bRowSelected){
				// Old record
				for (PosCheckGratuity oPosCheckGratuity: m_oCheckGratuityList){
					if (oPosCheckGratuity.getGratId() == AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getGratId()){
						// Set input name to Check Gratuity List
						if (!sInputName.isEmpty()){
							oPosCheckGratuity.setName(sName);
							oPosCheckGratuity.setShortName(sShortName);
						}
						// Set input amount to Check Gratuity List
						if (dInputValue.compareTo(BigDecimal.ZERO) > 0){
							oPosCheckGratuity.setRoundTotal(dRoundedTipsAmount);
							oPosCheckGratuity.setTotal(dTipsAmount);
							oPosCheckGratuity.setRoundAmount(oPosCheckGratuity.getRoundTotal().subtract(oPosCheckGratuity.getTotal()));
							if (bIsPercentage == true)
								oPosCheckGratuity.setRate(dInputValue.divide(new BigDecimal("100.0")));
						}
						oPosCheckGratuity.setStatus(PosCheckGratuity.STATUS_ACTIVE);
						bRecordFound = true;
						break;
					}
				}
				// New Record
				if (!bRecordFound){
					PosCheckGratuity oPosCheckGratuity = new PosCheckGratuity();
					oPosCheckGratuity.setGratId(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getGratId());
					// Set updated input name to Check Gratuity List
					if (!sInputName.isEmpty()){
						oPosCheckGratuity.setName(sName);
						oPosCheckGratuity.setShortName(sShortName);
					}else{
						// Without name updated
						oPosCheckGratuity.setName(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getName());
						oPosCheckGratuity.setShortName(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getShortName());
					}
					oPosCheckGratuity.setMethod(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getMethod());
					
					// Set updated input value to Check Gratuity List
					if (bIsOpenValue){
						if (dInputValue.compareTo(BigDecimal.ZERO) > 0){
							oPosCheckGratuity.setRoundTotal(dRoundedTipsAmount);
							oPosCheckGratuity.setTotal(dTipsAmount);
							oPosCheckGratuity.setRoundAmount(oPosCheckGratuity.getRoundTotal().subtract(oPosCheckGratuity.getTotal()));
							oPosCheckGratuity.setRate(dInputValue.divide(new BigDecimal("100.0")));
						}
					}else{
						// New gratuity selected with default value
						// Percentage
						if (bIsPercentage){
							oPosCheckGratuity.setTotal(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getRate().multiply(m_oSubTotal));
							oPosCheckGratuity.setRoundTotal(oPosCheckGratuity.getTotal());
						// Fix amount
						}else{
							oPosCheckGratuity.setTotal(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getFixAmount());
							oPosCheckGratuity.setRoundTotal(oPosCheckGratuity.getTotal());
						}
						oPosCheckGratuity.setRoundAmount(oPosCheckGratuity.getRoundTotal().subtract(oPosCheckGratuity.getTotal()));
						oPosCheckGratuity.setRate(AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getRate());
					}
					oPosCheckGratuity.setStatus(PosCheckGratuity.STATUS_ACTIVE);
					m_oCheckGratuityList.add(oPosCheckGratuity);
				}
			// Mark delete
			}else if (bRowDeselected){
				for (PosCheckGratuity oPosCheckGratuity: m_oCheckGratuityList){
					if (oPosCheckGratuity.getGratId() == AppGlobal.g_oFuncOutlet.get().getPosGratuityList().get(iItemIndex).getGratId() && oPosCheckGratuity.getStatus().equals(PosCheckGratuity.STATUS_ACTIVE)){
						oPosCheckGratuity.setStatus(PosCheckGratuity.STATUS_DELETED);
						bRecordFound = true;
						break;
					}
				}
			}
			// Update Sub. & Gratuity & Check Total Label Value
			UpdateTotalLabelValue();
		}
	}
	
	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId,
			int iSectionIndex, int iItemIndex, int iFieldIndex, String sNote) {
	}
	@Override
	public void FrameTitleHeader_close() {
		for (FrameAskGratuityListener listener : listeners) {
			// Raise the event to parent
			listener.frameAskGratuity_finishFrame();
		}
	}
}
