package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketModifierCellListener {
    void frameOrderingBasketModifierCell_QtyClicked(int iCellId, String sNote);
    void frameOrderingBasketModifierCell_DescClicked(int iCellId, String sNote);
    void frameOrderingBasketModifierCell_PriceClicked(int iCellId, String sNote);
}

public class FrameOrderingBasketModifierCell extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelQty;
	private VirtualUILabel m_oLabelDesc;
	private VirtualUILabel m_oLabelPrice;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameOrderingBasketModifierCellListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameOrderingBasketModifierCellListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameOrderingBasketModifierCellListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingBasketModifierCellListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketModifierCell.xml");
		
		// Section quantity
		m_oLabelQty = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelQty, "lblQuantity");
		m_oLabelQty.setVisible(false);
		this.attachChild(m_oLabelQty);
		
		// Section description
		m_oLabelDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelDesc, "lblDescription");
		this.attachChild(m_oLabelDesc);
		
		// Section price
		m_oLabelPrice = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPrice, "lblPrice");
		m_oLabelPrice.setVisible(false);
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

	public void setDescription(String[] sDesc) {
		m_oLabelDesc.setValue(sDesc);
	}
	
	public String getDescription() {
		return m_oLabelDesc.getValue();
	}
	
	public void setPrice(String sPrice) {
		m_oLabelPrice.setValue(sPrice);
	}
	
	public String getPrice() {
		return m_oLabelPrice.getValue();
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oLabelQty.getId()) {
        	for (FrameOrderingBasketModifierCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketModifierCell_QtyClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelDesc.getId()) {
        	for (FrameOrderingBasketModifierCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketModifierCell_DescClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oLabelPrice.getId()) {
        	for (FrameOrderingBasketModifierCellListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameOrderingBasketModifierCell_PriceClicked(this.getId(), sNote);
            }
			bMatchChild = true;
		}
        return bMatchChild;
    }
}
