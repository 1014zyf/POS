package commonui;

import java.util.ArrayList;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */


public class FrameTitleHeader extends VirtualUIFrame {
	
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUILabel m_oLabelTitle;
	private VirtualUIFrame m_oFraButtonClose;
	private VirtualUIFrame m_oFrameUnderline;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameTitleHeaderListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameTitleHeaderListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameTitleHeaderListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

    public FrameTitleHeader() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTitleHeaderListener>();
    }
    
    public void init(boolean bShowClose) {
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTitleHeader.xml");

		// Header
		m_oLabelTitle = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTitle, "lblTitle");
		m_oLabelTitle.setWidth(this.getWidth());
		this.attachChild(m_oLabelTitle);
		
		m_oFrameUnderline = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameUnderline, "fraUnderline");
		m_oFrameUnderline.setWidth(this.getWidth() - 48);
		this.attachChild(m_oFrameUnderline);

		//Create Close button
    	m_oFraButtonClose = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFraButtonClose, "fraButClose");
		VirtualUIImage oImage = new VirtualUIImage();
		oImage.setWidth(m_oFraButtonClose.getWidth());
		oImage.setHeight(m_oFraButtonClose.getHeight());
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_esc_200-200.png");
		oImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
		m_oFraButtonClose.attachChild(oImage);

		m_oFraButtonClose.setClickServerRequestNote("butClose");
		m_oFraButtonClose.allowClick(true);
		m_oFraButtonClose.setClickServerRequestBlockUI(false);
		this.attachChild(m_oFraButtonClose);
		
		m_oFraButtonClose.setLeft(m_oFrameUnderline.getWidth() + m_oFrameUnderline.getLeft() - m_oFraButtonClose.getWidth() - 3);
		
		if(bShowClose)
			m_oFraButtonClose.setVisible(true);
		else
			m_oFraButtonClose.setVisible(false);
    }
	
    public void setTitle(String sTitle){
		m_oLabelTitle.setValue(sTitle);
	}
	
    public void setTitle(String[] sTitle){
		m_oLabelTitle.setValue(sTitle);
	}
	
    public void setTitleShow(boolean bShow){
		m_oLabelTitle.setVisible(bShow);
	}
	
    public void setButtonShow(boolean bShow){
		m_oFraButtonClose.setVisible(bShow);
		m_oFraButtonClose.setEnabled(bShow);
	}
	
    public void setUnderlineShow(boolean bShow){
		m_oFrameUnderline.setVisible(bShow);
	}
	
    public void resetPostion(){
		m_oLabelTitle.setWidth(this.getWidth());
		m_oFrameUnderline.setWidth(this.getWidth() - 48);
		m_oFraButtonClose.setLeft(m_oFrameUnderline.getWidth() + m_oFrameUnderline.getLeft() - m_oFraButtonClose.getWidth() + 5);
	}
	
    public VirtualUIFrame getCloseButtonElement() {
    	return m_oFraButtonClose;
    }
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bChild = false;
		if(iChildId == m_oFraButtonClose.getId()) {					//Previous button clicked
    		for(FrameTitleHeaderListener listener : listeners) {
    			listener.FrameTitleHeader_close();
    			bChild = true;
    			break;
    		}
    	}
    	
    	return bChild;
    }
	
}
