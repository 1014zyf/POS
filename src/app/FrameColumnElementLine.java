package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameColumnElementLineListener {
    
}

public class FrameColumnElementLine extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	/*
	private VirtualUIImage m_oImageSelect;
	private VirtualUIImage m_oImageDeselect;
	*/
	
	private VirtualUILabel m_oLabelQty;
	private VirtualUILabel m_oLabelDesc;
	private VirtualUILabel m_oLabelInfo;
	
	private VirtualUILabel m_oLabelDiscountInfo;
	private VirtualUILabel m_oLabelPantryMessage;
	private VirtualUILabel m_oLabelDisplayInformation;
	
	private String m_sQty;
	private String m_sDesc;
	
	private int m_iSectionIndex;
	private int m_iItemIndex;
	private String m_sLineType;		// i: item; s:section
	private boolean m_bSelect;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameColumnElementListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameColumnElementListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameColumnElementListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public FrameColumnElementLine(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameColumnElementListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraColumnElementLine.xml");
	}
	
	public void addSection(int iSectionIndex, String sSectionDesc){
		
		VirtualUILabel oLabelDesc;
		
		// Section description
		oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabelDesc, "sectionDescription");
		oLabelDesc.setValue(sSectionDesc);
		this.attachChild(oLabelDesc);

		m_iSectionIndex = iSectionIndex;
		m_sLineType = "s";
	}
	
	public void addItem(int iSectionIndex, int iItemIndex, String sQty, String sDesc){
		
		// Section quantity
		m_oLabelQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQty, "quantity");
		m_oLabelQty.setValue(sQty);
		this.attachChild(m_oLabelQty);
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "description");
		m_oLabelDesc.setValue(sDesc);
		this.attachChild(m_oLabelDesc);
		
		/*
		// Selected image
		m_oImageSelect = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSelect, "ImgSelected");
		m_oImageSelect.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/basket_selected_image.png");
		m_oImageSelect.setVisible(false);
		this.attachChild(m_oImageSelect);
		
		// Deselected image
		m_oImageDeselect = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDeselect, "ImgDeselected");
		m_oImageDeselect.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/basket_deselected_image.png");
		m_oImageDeselect.setVisible(true);
		this.attachChild(m_oImageDeselect);
		*/
		
		m_iSectionIndex = iSectionIndex;
		m_iItemIndex = iItemIndex;
		m_sLineType = "i";
		m_sQty = sQty;
		m_sDesc = sDesc;
	}
		
	public void setInformation(String sInfo, String sFontColor){
//KingsleyKwan20170918ByPaul		-----Start-----
		String sTmpString;
//KingsleyKwan20170918ByPaul		----- End -----
		sInfo = sInfo.trim();
		if(sInfo.length() > 0){
			// Section Information (e.g. modifier)
			m_oLabelInfo = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelInfo, "info");
//KingsleyKwan20170918ByPaul		-----Start-----
			// item information length limit to 40 char
			if (sInfo.length() > 40)
				sTmpString = sInfo.substring(0,39);
			else
				sTmpString = sInfo;
			m_oLabelInfo.setValue(sTmpString);
//KingsleyKwan20170918ByPaul		----- End -----
			m_oLabelInfo.setTop(m_oLabelDesc.getHeight());
			this.attachChild(m_oLabelInfo);
			this.setHeight(this.getHeight() + m_oLabelInfo.getHeight());
			
			if(sFontColor.length() > 0)
				m_oLabelInfo.setForegroundColor(sFontColor);
		}
	}
	
	public void setDiscount(String sDiscountInfo, String sFontColor){
		sDiscountInfo = sDiscountInfo.trim();
		if(sDiscountInfo.length() > 0){
			// Discount
			m_oLabelDiscountInfo = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelDiscountInfo, "discount");
			m_oLabelDiscountInfo.setValue(sDiscountInfo);
			if(m_oLabelInfo != null){
				m_oLabelDiscountInfo.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight());
			}else{
				m_oLabelDiscountInfo.setTop(m_oLabelDesc.getHeight());
			}
			this.attachChild(m_oLabelDiscountInfo);
			this.setHeight(this.getHeight() + m_oLabelDiscountInfo.getHeight());
			
			if(sFontColor.length() > 0)
				m_oLabelInfo.setForegroundColor(sFontColor);
		}
	}
	
	public void setPantryMessage(String sPantryMessage, String sFontColor) {
		sPantryMessage = sPantryMessage.trim();
		if (!sPantryMessage.isEmpty()) {
			// Discount
			m_oLabelPantryMessage = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelPantryMessage, "discount");
			m_oLabelPantryMessage.setValue(sPantryMessage);
			if (m_oLabelInfo != null){
				if (m_oLabelDiscountInfo != null)
					m_oLabelPantryMessage.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight() + m_oLabelDiscountInfo.getHeight());
				else
					m_oLabelPantryMessage.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight());
			} else {
				if(m_oLabelDiscountInfo != null)
					m_oLabelPantryMessage.setTop(m_oLabelDesc.getHeight() + m_oLabelDiscountInfo.getHeight());
				else
					m_oLabelPantryMessage.setTop(m_oLabelDesc.getHeight());
			}
			this.attachChild(m_oLabelPantryMessage);
			this.setHeight(this.getHeight() + m_oLabelPantryMessage.getHeight());
			
			if(sFontColor.length() > 0)
				m_oLabelInfo.setForegroundColor(sFontColor);
		}
	}
	
	//Column Set Display Information
	public void setDisplayInformation(String sItemAttribute, String sFontColor) {
		sItemAttribute = sItemAttribute.trim();
		if (!sItemAttribute.isEmpty()) {
			// Discount
			m_oLabelDisplayInformation = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(m_oLabelDisplayInformation, "discount");
			m_oLabelDisplayInformation.setValue(sItemAttribute);
			if (m_oLabelInfo != null){
				if (m_oLabelDiscountInfo != null)
					if (m_oLabelPantryMessage != null)
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight() + m_oLabelDiscountInfo.getHeight()+m_oLabelPantryMessage.getHeight());
					else
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight() + m_oLabelDiscountInfo.getHeight());
				else
					if (m_oLabelPantryMessage != null)
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight() + m_oLabelPantryMessage.getHeight());
					else 
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelInfo.getHeight());
			} else {
				if(m_oLabelDiscountInfo != null)
					if (m_oLabelPantryMessage != null)
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelDiscountInfo.getHeight()+m_oLabelPantryMessage.getHeight());
					else 
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight() + m_oLabelDiscountInfo.getHeight());
				else
					if (m_oLabelPantryMessage != null)
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight()+m_oLabelPantryMessage.getHeight());
					else 
						m_oLabelDisplayInformation.setTop(m_oLabelDesc.getHeight());
			}
			this.attachChild(m_oLabelDisplayInformation);
			this.setHeight(this.getHeight() + m_oLabelDisplayInformation.getHeight());
			
			if(sFontColor.length() > 0)
				m_oLabelInfo.setForegroundColor(sFontColor);
		}
	}
	
	public void setItemIndex(int iItemIndex){
		m_iItemIndex = iItemIndex;
	}
	
	public Integer getSectionIndex(){
		return m_iSectionIndex;
	}
	
	public Integer getItemIndex(){
		return m_iItemIndex;
	}
	
	public String getLineType(){
		return m_sLineType;
	}
	
	public boolean isSelected(){
		return m_bSelect;
	}

	public String getQty(){
		return m_sQty;
	}
	
	public String getDesc(){
		return m_sDesc;
	}
}
