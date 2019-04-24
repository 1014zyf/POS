package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameLargePopUpPanelListener {
	void frameLargePopUpPanel_CloseImageClicked();
	void FrameLargePopUpPanelListener_timeout();
}

public class FrameLargePopUpPanel extends VirtualUIFrame implements FrameTitleHeaderListener {
	private TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameContent;
	private VirtualUIFrame m_oFrameCover;
	private VirtualUILabel m_oLblDesc;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIImage m_oPaymentImage;
	private VirtualUIFrame m_oFrameImage;
	
	public static String FLOORPLANFUNC = "floor_plan_func";
	
	public int m_iPageRecordCount;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLargePopUpPanelListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLargePopUpPanelListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLargePopUpPanelListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	// constructor
	public FrameLargePopUpPanel(String[] sTitle, String[] sDesc) {
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLargePopUpPanelListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLargePopUpPanel.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.addListener(this);
		m_oFrameTitleHeader.setTitle(sTitle);
		this.attachChild(m_oFrameTitleHeader);
		
		m_oLblDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblDesc, "lblDesc");
		m_oLblDesc.setValue(sDesc);
		m_oLblDesc.setTextSize(24);
		this.attachChild(m_oLblDesc);

		m_oFrameContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		m_oFrameContent.setWidth(this.getWidth() - m_oFrameContent.getLeft() * 2);
		this.attachChild(m_oFrameContent);

		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverBackground");
		m_oFrameCover.setTop(100);
		m_oFrameCover.setHeight(this.getHeight());
		this.attachChild(m_oFrameCover);
		
		m_oFrameImage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameImage, "fraImage");
		m_oFrameImage.setVisible(false);
		this.attachChild(m_oFrameImage);
		
		m_oPaymentImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oPaymentImage, "imgPaymentImage");
		m_oPaymentImage.setVisible(false);
		m_oFrameImage.attachChild(m_oPaymentImage);
	}
	
	public <T> void setContentFrame(T oFrame) {
	m_oFrameContent = (VirtualUIFrame) oFrame;
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		m_oFrameContent.setWidth(this.getWidth() - m_oFrameContent.getLeft() * 2);
		this.attachChild(m_oFrameContent);
	}

	public void setPaymentImage(String sName) {
		m_oFrameContent.setVisible(false);
		m_oPaymentImage.setSource(sName);
		m_oPaymentImage.setVisible(true);
		m_oFrameImage.setVisible(true);
		VirtualUILabel oLblDesc = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLblDesc, "lblLargeDesc");
		m_oLblDesc.setWidth(oLblDesc.getWidth());
		m_oLblDesc.setLeft(oLblDesc.getLeft());
		m_oLblDesc.setTop(oLblDesc.getTop());
		m_oLblDesc.setHeight(oLblDesc.getHeight());
		m_oLblDesc.setTextAlign(oLblDesc.getTextAlign());
		m_oLblDesc.setTextSize(oLblDesc.getTextSize());
	}
	
	// Create auto quit pay result timer
	public void addFinishShowTimer(int iTimeout) {
		this.addTimer("timeout", iTimeout, false, "timeout", true, true, null);
	}

	// Start auto quit pay result
	public void startFinishShowTimer(boolean bStart) {
		this.controlTimer("timeout", bStart);
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		return bMatchChild;
	}

	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if(sNote.equals("timeout")){
				// Ordering timeout
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameLargePopUpPanelListener listener : listeners) {
					// Raise the event to parent
					listener.FrameLargePopUpPanelListener_timeout();
				}
				
				// Disable the timer
				startFinishShowTimer(false);
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
			}
			return true;
		}
		return false;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameLargePopUpPanelListener listener : listeners) {
			// Raise the event to parent
			listener.frameLargePopUpPanel_CloseImageClicked();
		}
	}
}
