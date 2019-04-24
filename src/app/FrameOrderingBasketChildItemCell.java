package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketChildItemCellListener {
    void frameOrderingBasketChildItemCell_QtyClicked(int iCellId, String sNote);
    void frameOrderingBasketChildItemCell_DescClicked(int iCellId, String sNote);
    void frameOrderingBasketChildItemCell_PriceClicked(int iCellId, String sNote);
}

public class FrameOrderingBasketChildItemCell extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelQty;
	private VirtualUILabel m_oLabelDesc;
	private VirtualUILabel m_oLabelInfo;
	private VirtualUILabel m_oLabelPrice;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameOrderingBasketChildItemCellListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameOrderingBasketChildItemCellListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameOrderingBasketChildItemCellListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingBasketChildItemCellListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketChildItemCell.xml");
		
		// Section quantity
		m_oLabelQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblQuantity");
		m_oLabelQty.setVisible(false);
		this.attachChild(m_oLabelQty);
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDescription");
		this.attachChild(m_oLabelDesc);
		
		// Section Information (e.g. modifier)
		m_oLabelInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInfo, "lblInfo");
		m_oLabelInfo.setVisible(false);
		this.attachChild(m_oLabelInfo);
		
		// Section price
		m_oLabelPrice = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPrice, "lblPrice");
		this.attachChild(m_oLabelPrice);
	}

	public void setCellWidth(int iWidth) {
		this.setWidth(iWidth);
		m_oLabelPrice.setLeft(this.getWidth() - m_oLabelPrice.getWidth());
	}
	
	public void setQuantity(String sQty) {
		m_oLabelQty.setValue(sQty);
	}
	
	public String getQuantity() {
		return m_oLabelQty.getValue();
	}

	public void setDescription(String sDesc) {
		m_oLabelDesc.setValue(sDesc);
	}
	
	public String getDescription() {
		return m_oLabelDesc.getValue();
	}
	
	public void setInformation(String sInfo){
		m_oLabelInfo.setValue(sInfo);
		if(sInfo.length() > 0){
			m_oLabelInfo.setVisible(true);
			m_oLabelDesc.setHeight(this.getHeight() - m_oLabelInfo.getHeight());
		}else{
			m_oLabelInfo.setVisible(false);
			m_oLabelDesc.setHeight(this.getHeight());
		}
	}
	
	public String getInformation(){
		return m_oLabelInfo.getValue();
	}
	
	public void setPrice(String sPrice) {
		m_oLabelPrice.setValue(sPrice);
	}
	
	public String getPrice() {
		return m_oLabelPrice.getValue();
	}
	
	public void setLabelForegroundColor(String sForegroundColor){
		m_oLabelQty.setForegroundColor(sForegroundColor);
		m_oLabelDesc.setForegroundColor(sForegroundColor);
		m_oLabelInfo.setForegroundColor(sForegroundColor);
		m_oLabelPrice.setForegroundColor(sForegroundColor);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oLabelQty.getId()) {
        	for (FrameOrderingBasketChildItemCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketChildItemCell_QtyClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelDesc.getId()) {
        	for (FrameOrderingBasketChildItemCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketChildItemCell_DescClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelPrice.getId()) {
        	for (FrameOrderingBasketChildItemCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketChildItemCell_PriceClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
        return bMatchChild;
    }
}
