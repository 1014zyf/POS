package app.commonui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import om.OutFloorPlanTable;
import om.PosOutletTable;
import app.controller.AppGlobal;
import app.FrameTableButton;
import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIButton;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUILabel;
import core.virtualui.VirtualUIList;
import core.virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameAskTableListener {
    void FrameAskTable_clickOK();
    void FrameAskTable_clickCancel();
    void FrameAskTable_longClicked(String sTableNo, String sTableExtension);
}

public class FrameAskTable extends VirtualUIFrame implements FrameNumberPadListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelTitle;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUILabel m_oLabelTableNoHeader;
	private VirtualUILabel m_oLabelTableExtensionHeader;
	private VirtualUIList m_oListTableName;
	private HashMap<String, VirtualUIFrame> m_oFrameTableNameList;
	private boolean m_bShowTableNameList;
	private VirtualUITextbox m_oTextboxTableNo;
	private VirtualUILabel m_oLabelExtension;
	private VirtualUILabel m_oLabelTableNo;
	private VirtualUIList m_oListTableExtension;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonCancel;
	private FrameNumberPad m_oFrameNumberPad;
	private HashMap<String, FrameTableButton> m_oButtonTableExtension;

	private FrameTableButton m_oButtonSelectedTableExtension;
	private String m_sTableNo;
	private String m_sTableExtension;
	
	private HashMap<String, String> m_sTableForegroundColor;
	private HashMap<String, String> m_sTableBackgroundColor;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameAskTableListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameAskTableListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameAskTableListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
    public void init(int iTableNo, boolean bNeedDefaultExtension){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameAskTableListener>();
		m_sTableForegroundColor = new HashMap<String, String>();
		m_sTableBackgroundColor = new HashMap<String, String>();
		// Init extension list
		m_oButtonTableExtension = new HashMap<String, FrameTableButton>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAskTable.xml");
		
		// Title
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth() - (m_oLabelTitle.getLeft() * 2));
		this.attachChild(m_oLabelTitle);
		
		// Message
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);
		
		// Table No. Header
		m_oLabelTableNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNoHeader, "lblTableNoHeader");
		m_oLabelTableNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		this.attachChild(m_oLabelTableNoHeader);

		VirtualUIFrame oFrameListTableExtension = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameListTableExtension, "fraListTableExtension");
		this.attachChild(oFrameListTableExtension);
		
		// Table Extension Header
		m_oLabelTableExtensionHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableExtensionHeader, "lblTableExtensionHeader");
		m_oLabelTableExtensionHeader.setValue(AppGlobal.g_oLang.get()._("table_extension"));
		oFrameListTableExtension.attachChild(m_oLabelTableExtensionHeader);
		
		// Table Name List
		m_oLabelExtension = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelExtension, "lblExtension");	
		this.attachChild(m_oLabelExtension);
		m_oFrameTableNameList = new HashMap<String, VirtualUIFrame>();
		m_bShowTableNameList = false;
		
		// Table No Input Box
		m_oTextboxTableNo = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_oTextboxTableNo, "txtTableNo");
		m_oTextboxTableNo.setFocusWhenShow(true);
		this.attachChild(m_oTextboxTableNo);
		
		// Table Extension Label
		m_oLabelExtension = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelExtension, "lblExtension");	
		this.attachChild(m_oLabelExtension);
		
		// Table No Label
		m_oLabelTableNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNo, "lblTableNo");
		this.attachChild(m_oLabelTableNo);
		
		// Table Extension List
		m_oListTableExtension = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListTableExtension, "listTableExtension");
		oFrameListTableExtension.attachChild(m_oListTableExtension);
		
		// Resize the frame
		oFrameListTableExtension.setHeight(m_oListTableExtension.getTop()+m_oListTableExtension.getHeight() + 10);
		this.setHeight(oFrameListTableExtension.getTop() + oFrameListTableExtension.getHeight()+1);
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("ok"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		this.attachChild(m_oButtonOK);
		
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oButtonCancel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oButtonCancel);
		
		// Get table color
		VirtualUIFrame oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNewTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_NEW_TABLE, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_NEW_TABLE, oFrame.getBackgroundColor());
		m_oTemplateBuilder.buildFrame(oFrame, "fraCleaningTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_VACANT, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_VACANT, oFrame.getBackgroundColor());
		m_oTemplateBuilder.buildFrame(oFrame, "fraOccupiedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_OCCUPIED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_OCCUPIED, oFrame.getBackgroundColor());
		m_oTemplateBuilder.buildFrame(oFrame, "fraPrintedTable");
		m_sTableForegroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, oFrame.getForegroundColor());
		m_sTableBackgroundColor.put(PosOutletTable.STATUS_CHECK_PRINTED, oFrame.getBackgroundColor());
		
		if(bNeedDefaultExtension){
			addTableExtension("-", PosOutletTable.STATUS_NEW_TABLE, false, true, false);
			
			// Create extension list
			for(char alphabet = AppGlobal.TABLE_EXTENSION_START_LETTER; alphabet <= AppGlobal.TABLE_EXTENSION_END_LETTER;alphabet++){
				addTableExtension(String.valueOf(alphabet), PosOutletTable.STATUS_NEW_TABLE, false, false, false);
			}
		}
		
		if(iTableNo > 0){
			// Set Table No Input Box
			m_oTextboxTableNo.setValue(iTableNo + "");
			m_oTextboxTableNo.setVisible(false);
			
			m_oLabelTableNo.setValue(AppGlobal.g_oFuncOutlet.get().getTableNameWithTableNo(Integer.toString(iTableNo), ""));
			m_oLabelTableNo.setVisible(true);	
		}else{
			m_oButtonOK.setVisible(false);
			m_oButtonCancel.setVisible(false);
			m_oLabelTableNo.setVisible(false);
			
			// Number pad
			m_oFrameNumberPad = new FrameNumberPad();
			m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
			m_oFrameNumberPad.init();
			
			m_oFrameNumberPad.setTop(m_oTextboxTableNo.getTop() + m_oTextboxTableNo.getHeight() + 10);
			
			m_oFrameNumberPad.addListener(this);
			m_oFrameNumberPad.setEnterBlockUI(true);
			this.attachChild(m_oFrameNumberPad);
			
			m_oFrameNumberPad.clearEnterSubmitId();
			m_oFrameNumberPad.setEnterSubmitId(m_oTextboxTableNo);
			
			//Check list common basket
			VirtualUIFrame oFrameListTableName = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameListTableName, "fraListTableName");
			oFrameListTableName.setVisible(false);
			this.attachChild(oFrameListTableName);
			
			m_oListTableName = new VirtualUIList();
			m_oTemplateBuilder.buildList(m_oListTableName, "listTableName");
			m_oListTableName.setVisible(false);
			oFrameListTableName.attachChild(m_oListTableName);
			
			if(this.setTableNamelist()){
				this.setWidth(this.getWidth()+oFrameListTableName.getWidth());
				m_oLabelTitle.setWidth(m_oLabelTitle.getWidth()+oFrameListTableName.getWidth());			
				m_oLabelTableNoHeader.setLeft(m_oLabelTableNoHeader.getLeft()+oFrameListTableName.getWidth());
				m_oLabelExtension.setLeft(m_oLabelExtension.getLeft()+oFrameListTableName.getWidth());
				m_oTextboxTableNo.setLeft(m_oTextboxTableNo.getLeft()+oFrameListTableName.getWidth());
				m_oFrameNumberPad.setLeft(m_oFrameNumberPad.getLeft()+oFrameListTableName.getWidth());
				oFrameListTableExtension.setLeft(oFrameListTableExtension.getLeft()+oFrameListTableName.getWidth());
				m_oListTableName.setHeight(m_oFrameTableNameList.size()*50);
				if(m_oListTableName.getHeight()>400){
					m_oListTableName.setHeight(400);
				}
				oFrameListTableName.setVisible(true);
				m_oListTableName.setVisible(true);
				m_bShowTableNameList = true;
			}
		}

	}
    
    public boolean setTableNamelist(){
    	List<HashMap<String, String>> m_oListTablesInfo = AppGlobal.g_oFuncOutlet.get().getTableNameList();
    	if(m_oListTablesInfo.isEmpty()){
    		return false;
    	}
    	
		VirtualUIFrame oFrameListTableNameItem = new VirtualUIFrame();
		oFrameListTableNameItem.setExist(true);
		oFrameListTableNameItem.setTop(0);
		oFrameListTableNameItem.setLeft(0);
		oFrameListTableNameItem.setHeight(40);
		oFrameListTableNameItem.setWidth(260);
		oFrameListTableNameItem.allowClick(false);
		oFrameListTableNameItem.setBackgroundColor("#B2CBDA");
		oFrameListTableNameItem.setForegroundColor("#015384");
		m_oListTableName.attachChild(oFrameListTableNameItem);
		
		VirtualUILabel oLabelTableNameHeader = new VirtualUILabel();
		oLabelTableNameHeader.setExist(true);
		oLabelTableNameHeader.setTop(0);
		oLabelTableNameHeader.setLeft(0);
		oLabelTableNameHeader.setHeight(40);
		oLabelTableNameHeader.setWidth(130);
		oLabelTableNameHeader.allowClick(false);
		oLabelTableNameHeader.setTextAlign("center");
		oLabelTableNameHeader.setValue(AppGlobal.g_oLang.get()._("table_name"));
		oFrameListTableNameItem.attachChild(oLabelTableNameHeader);
		
		VirtualUILabel oLabelTabelNoHeader = new VirtualUILabel();
		oLabelTabelNoHeader.setExist(true);
		oLabelTabelNoHeader.setTop(0);
		oLabelTabelNoHeader.setLeft(130);
		oLabelTabelNoHeader.setHeight(40);
		oLabelTabelNoHeader.setWidth(130);
		oLabelTabelNoHeader.allowClick(false);
		oLabelTabelNoHeader.setTextAlign("center");
		oLabelTabelNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		oFrameListTableNameItem.attachChild(oLabelTabelNoHeader);
		
		for(HashMap<String, String> oFloorPlanTable:m_oListTablesInfo) {
			if(!(oFloorPlanTable.get("tableName").isEmpty())) {
				
				oFrameListTableNameItem = new VirtualUIFrame();
				oFrameListTableNameItem.setExist(true);
				oFrameListTableNameItem.setTop(0);
				oFrameListTableNameItem.setLeft(0);
				oFrameListTableNameItem.setHeight(40);
				oFrameListTableNameItem.setWidth(260);
				oFrameListTableNameItem.allowClick(true);
				oFrameListTableNameItem.setBackgroundColor("#F0FAFF");
				oFrameListTableNameItem.setForegroundColor("#d34f00");
				m_oListTableName.attachChild(oFrameListTableNameItem);
				
				oLabelTableNameHeader = new VirtualUILabel();
				oLabelTableNameHeader.setExist(true);
				oLabelTableNameHeader.setTop(0);
				oLabelTableNameHeader.setLeft(0);
				oLabelTableNameHeader.setHeight(40);
				oLabelTableNameHeader.setWidth(130);
				oLabelTableNameHeader.allowClick(false);
				oLabelTableNameHeader.setTextAlign("center");
				oLabelTableNameHeader.setValue(oFloorPlanTable.get("tableName"));
				oFrameListTableNameItem.attachChild(oLabelTableNameHeader);
				
				oLabelTabelNoHeader = new VirtualUILabel();
				oLabelTabelNoHeader.setExist(true);
				oLabelTabelNoHeader.setTop(0);
				oLabelTabelNoHeader.setLeft(130);
				oLabelTabelNoHeader.setHeight(40);
				oLabelTabelNoHeader.setWidth(130);
				oLabelTabelNoHeader.allowClick(false);
				oLabelTabelNoHeader.setTextAlign("center");
				oLabelTabelNoHeader.setValue(oFloorPlanTable.get("table")+oFloorPlanTable.get("tableExt"));
				oFrameListTableNameItem.attachChild(oLabelTabelNoHeader);
				
				m_oFrameTableNameList.put(oFloorPlanTable.get("table")+"_"+oFloorPlanTable.get("tableExt"), oFrameListTableNameItem);
			}
		}
		return true;
    }
    
    public void addTableExtension(String alphabet, String sStatus, boolean bLocked, boolean bSelect, boolean bAllowLongClick){
    	FrameTableButton oFrameTableButton = new FrameTableButton();
		
    	if(m_oButtonTableExtension.isEmpty())
    		oFrameTableButton.setTop(0);
    	else
    		oFrameTableButton.setTop(5);
    	oFrameTableButton.setWidth(m_oListTableExtension.getWidth());
    	oFrameTableButton.setHeight(m_oListTableExtension.getWidth());
    	oFrameTableButton.setExist(true);
//    	oFrameTableButton.setupTableButton(alphabet, OutFloorPlanTable.SHAPE_RHOMBUS, 0, "");
    	oFrameTableButton.setCornerRadius("5");
		oFrameTableButton.allowClick(true);
//		oFrameTableButton.addClickServerRequestSubmitElement(this);
		if(bAllowLongClick){
			oFrameTableButton.allowLongClick(true);
			oFrameTableButton.setLongClickServerRequestBlockUI(false);
		}
		
		if(sStatus.equals(PosOutletTable.STATUS_NEW_TABLE)){
    		if(m_sTableForegroundColor.containsKey(sStatus)){
    			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
    			oFrameTableButton.setBackgroundColor(m_sTableBackgroundColor.get(sStatus));
    		}else{	        		
    			oFrameTableButton.setBackgroundColor("#FFFFFF");
    		}
    		oFrameTableButton.setPrinted(false);
    		oFrameTableButton.setLocked(false);
    	}else
    	if(sStatus.equals(PosOutletTable.STATUS_VACANT)){
    		if(m_sTableForegroundColor.containsKey(sStatus)){
    			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
    			oFrameTableButton.setBackgroundColor(m_sTableBackgroundColor.get(sStatus));
    		}else{
    			oFrameTableButton.setBackgroundColor("#FFFFFF");
    		}
    		oFrameTableButton.setPrinted(false);
    		oFrameTableButton.setLocked(false);
    	}else
    	if(sStatus.equals(PosOutletTable.STATUS_OCCUPIED)){
    		if(m_sTableForegroundColor.containsKey(sStatus)){
    			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
    			oFrameTableButton.setBackgroundColor(m_sTableBackgroundColor.get(sStatus));
    		}else{
    			oFrameTableButton.setBackgroundColor("#FF0000");
    		}
    		oFrameTableButton.setPrinted(false);
    		oFrameTableButton.setLocked(false);
    	}else
    	if(sStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED)){
    		if(m_sTableForegroundColor.containsKey(sStatus)){
    			oFrameTableButton.setLabelForegroundColor(m_sTableForegroundColor.get(sStatus));
    			oFrameTableButton.setBackgroundColor(m_sTableBackgroundColor.get(sStatus));
    		}else{
    			oFrameTableButton.setBackgroundColor("#00FF00");
    		}
    		oFrameTableButton.setPrinted(true);
    		oFrameTableButton.setLocked(false);
    	}
        if(bLocked){
        	oFrameTableButton.setLocked(true);
        }
        
        oFrameTableButton.setViewSeq(m_oButtonTableExtension.size() + 1);
		
		m_oButtonTableExtension.put(alphabet, oFrameTableButton);
//		m_oListTableExtension.attachChild(oFrameTableButton);
		
		if(bSelect){
			m_oButtonSelectedTableExtension = oFrameTableButton;	
			selectTableExtensionButton(m_oButtonSelectedTableExtension, true);
			m_sTableExtension = alphabet;
		}
			
    }
    
    public void addTableDetailType(String sKey, String sIconURL){
    	for(FrameTableButton oFrameTableButton:m_oButtonTableExtension.values()){
//			oFrameTableButton.addTableDetailType(sKey, sIconURL);
		}
    }
    
    public void updateTableDetail(String alphabet, String sKey, String sDetail){
		FrameTableButton oFrameTableButton;
		
        if(m_oButtonTableExtension.containsKey(alphabet)){
        	oFrameTableButton = m_oButtonTableExtension.get(alphabet);
//        	oFrameTableButton.setTableDetail(sKey, sDetail);
        	oFrameTableButton.setLabelForegroundColor(oFrameTableButton.getLabelForegroundColor());
        }
	}
    
    public void setTableDetailByIndex(int iIndex){
    	for(FrameTableButton oFrameTableButton:m_oButtonTableExtension.values()){
			oFrameTableButton.setTableDetailByIndex(iIndex);
		}
    }
    
    public void showTableDetail(){
    	for(FrameTableButton oFrameTableButton:m_oButtonTableExtension.values()){
//			oFrameTableButton.showTableDetail();
		}
    }
    
    private void selectTableExtensionButton(FrameTableButton oFrameTableButton, boolean bSelected){
    	if(bSelected){
    		oFrameTableButton.setStroke(1);
    		oFrameTableButton.setStrokeColor("#FF0000");
    	}else{
    		oFrameTableButton.setStroke(0);
    	}
    }
    
	public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
	}
	
	public void setMessage(String sMessage){
		m_oLabelMessage.setValue(sMessage);
	}
	
	public void setDefaultTableNo(String sTableNo) {
		m_oTextboxTableNo.setValue(sTableNo);
	}
	
	public void setDefaultTableExtension(String sTableExtension) {
		for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
    		if (sTableExtension.equals(entry.getKey())) {
				m_sTableExtension = entry.getKey();
	    		FrameTableButton oButton = entry.getValue();

				if(m_oButtonSelectedTableExtension != null){
					// Resume the color
					selectTableExtensionButton(m_oButtonSelectedTableExtension, false);
				}
				
				m_oButtonSelectedTableExtension = oButton;
				selectTableExtensionButton(m_oButtonSelectedTableExtension, true);
				
				break;
    		}
		}
		
		m_oLabelExtension.setValue(sTableExtension);
	}
	
	public String getTableNo(){
		return m_sTableNo;
	}
	
	public String getTableExtension(){
		if(m_sTableExtension.equals("-"))
			return "";
		else
			return m_sTableExtension;
	}
	
	public boolean isShowTableNameList(){
		return m_bShowTableNameList;
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
        if (iChildId == m_oButtonOK.getId()) {      	
        	
        	m_sTableNo = m_oTextboxTableNo.getValue();
        	
        	for (FrameAskTableListener listener : listeners) {
        		// Raise the event to parent
           		listener.FrameAskTable_clickOK();
            }
        	
        	bMatchChild = true;
        }
        else if (iChildId == m_oButtonCancel.getId()) {
        	for (FrameAskTableListener listener : listeners) {        		
        		// Raise the event to parent
           		listener.FrameAskTable_clickCancel();
            }
        	
        	bMatchChild = true;       	
        }else{
        	for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
        		FrameTableButton oButton = entry.getValue();
        		if (iChildId == oButton.getId()){
					m_sTableExtension = entry.getKey();
					
					if(m_oButtonSelectedTableExtension != null){
						// Resume the color
						selectTableExtensionButton(m_oButtonSelectedTableExtension, false);
					}
					m_oButtonSelectedTableExtension = oButton;
					selectTableExtensionButton(m_oButtonSelectedTableExtension, true);
					
					if(m_sTableExtension.equals("-"))
			    		m_oLabelExtension.setValue("");
			    	else
			    		m_oLabelExtension.setValue(m_sTableExtension);
					
					if(m_oLabelTableNo.getVisible()){
						if(m_sTableExtension.equals("-"))
							m_oLabelTableNo.setValue(m_oTextboxTableNo.getValue());
						else
							m_oLabelTableNo.setValue(m_oTextboxTableNo.getValue() + m_sTableExtension);
					}
					
					m_sTableNo = m_oTextboxTableNo.getValue();
					if(m_sTableNo.length() > 0){
						for (FrameAskTableListener listener : listeners) {
			        		// Raise the event to parent
			           		listener.FrameAskTable_clickOK();
			            }
					}
					
					bMatchChild = true; 
        		}
        	}
        	
        	for(Map.Entry<String, VirtualUIFrame> entry:m_oFrameTableNameList.entrySet()){
        		VirtualUIFrame oFrame = entry.getValue();
        		String [] sTable = entry.getKey().split("_");
        		if (iChildId == oFrame.getId()){
        			if(sTable.length>0){
        				String sTableNo = sTable[0];
        				String sTableExt = "";
        				if(sTable.length == 2)
        					sTableExt = sTable[1];
            			m_oTextboxTableNo.setValue(sTableNo);
            			this.setDefaultTableExtension(sTableExt);
        			}
        			
        			bMatchChild = true; 
        		}
        	}
        }
        
    	return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		for(Map.Entry<String, FrameTableButton> entry:m_oButtonTableExtension.entrySet()){
    		FrameTableButton oButton = entry.getValue();
    		if (iChildId == oButton.getId()){
    			String sTableNo = m_oTextboxTableNo.getValue();
    			String sTableExtension = entry.getKey();
				if(sTableExtension.equals("-")){
					for (FrameAskTableListener listener : listeners) {
		        		// Raise the event to parent
		           		listener.FrameAskTable_longClicked(sTableNo, "");
		            }
				}else{
					for (FrameAskTableListener listener : listeners) {
		        		// Raise the event to parent
		           		listener.FrameAskTable_longClicked(sTableNo, sTableExtension);
		            }
				}
    		}
    	}
		
		return bMatchChild;
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {

		m_sTableNo = m_oTextboxTableNo.getValue();
		
		for (FrameAskTableListener listener : listeners) {        		
    		// Raise the event to parent
       		listener.FrameAskTable_clickOK();
        }
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		for (FrameAskTableListener listener : listeners) {        		
    		// Raise the event to parent
       		listener.FrameAskTable_clickCancel();
        }
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
		// TODO Auto-generated method stub
		
	}

}
