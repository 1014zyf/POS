package app;

import java.util.ArrayList;

import commonui.FrameTitleHeader;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameVoidCheckDetailListener {
    void FrameVoidCheckDetail_confirm();
    void FrameVoidCheckDetail_cancel();
}

public class FrameVoidCheckDetail extends VirtualUIFrame {
	
	private TemplateBuilder m_oTemplateBuilder;
	private FrameTitleHeader m_oFrameTitleHeader;
	private VirtualUIFrame m_oFrameContent;
	private VirtualUILabel m_oLabelWaiterNameHeader;
	private VirtualUILabel m_oLabelCheckNoHeader;
	private VirtualUILabel m_oLabelTableNoHeader;
	private VirtualUILabel m_oLabelCoverHeader;
	private VirtualUILabel m_oLabelCheckTotalHeader;
	private VirtualUILabel m_oLabelWaiterName;
	private VirtualUILabel m_oLabelCheckNo;
	private VirtualUILabel m_oLabelTableNo;
	private VirtualUILabel m_oLabelCover;
	private VirtualUILabel m_oLabelCheckTotal;
	
	private VirtualUIButton m_oButtonConfirm;
	private VirtualUIButton m_oButtonCancel;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameVoidCheckDetailListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameVoidCheckDetailListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameVoidCheckDetailListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }

    public FrameVoidCheckDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameVoidCheckDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraVoidCheckDetail.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(false);
		this.attachChild(m_oFrameTitleHeader);
		
		m_oFrameContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		this.attachChild(m_oFrameContent);
		
		// Waiter Name Label
		m_oLabelWaiterNameHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiterNameHeader, "lblWaiterHeader");
		m_oLabelWaiterNameHeader.setValue(AppGlobal.g_oLang.get()._("open_check_employee"));
		m_oFrameContent.attachChild(m_oLabelWaiterNameHeader);
		
//KingsleyKwan20171013ByKing	-----Start-----
		VirtualUIFrame oFrameContentLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameContentLine, "fraContentLine");
		oFrameContentLine.setTop(m_oLabelWaiterNameHeader.getHeight() + m_oLabelWaiterNameHeader.getTop() + 7);
		m_oFrameContent.attachChild(oFrameContentLine);
//KingsleyKwan20171013ByKing	----- End -----

		// Check No. Label
		m_oLabelCheckNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckNoHeader, "lblCheckNoHeader");
		m_oLabelCheckNoHeader.setValue(AppGlobal.g_oLang.get()._("check_no"));
		m_oFrameContent.attachChild(m_oLabelCheckNoHeader);
		
//KingsleyKwan20171013ByKing	-----Start-----
		oFrameContentLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameContentLine, "fraContentLine");
		oFrameContentLine.setTop(m_oLabelCheckNoHeader.getHeight() + m_oLabelCheckNoHeader.getTop() + 7);
		m_oFrameContent.attachChild(oFrameContentLine);
//KingsleyKwan20171013ByKing	----- End -----

		// Table No. Label
		m_oLabelTableNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNoHeader, "lblTableNoHeader");
		m_oLabelTableNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no"));
		m_oFrameContent.attachChild(m_oLabelTableNoHeader);
		
//KingsleyKwan20171013ByKing	-----Start-----
		oFrameContentLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameContentLine, "fraContentLine");
		oFrameContentLine.setTop(m_oLabelTableNoHeader.getHeight() + m_oLabelTableNoHeader.getTop() + 7);
		m_oFrameContent.attachChild(oFrameContentLine);
//KingsleyKwan20171013ByKing	----- End -----

		// Cover Label
		m_oLabelCoverHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCoverHeader, "lblCoverHeader");
		m_oLabelCoverHeader.setValue(AppGlobal.g_oLang.get()._("cover"));
		m_oFrameContent.attachChild(m_oLabelCoverHeader);
		
//KingsleyKwan20171013ByKing	-----Start-----
		oFrameContentLine = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameContentLine, "fraContentLine");
		oFrameContentLine.setTop(m_oLabelCoverHeader.getHeight() + m_oLabelCoverHeader.getTop() + 7);
		m_oFrameContent.attachChild(oFrameContentLine);
//KingsleyKwan20171013ByKing	----- End -----

		// Check Total Label
		m_oLabelCheckTotalHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckTotalHeader, "lblCheckTotalHeader");
		m_oLabelCheckTotalHeader.setValue(AppGlobal.g_oLang.get()._("total"));
		m_oFrameContent.attachChild(m_oLabelCheckTotalHeader);
		
		// Waiter Name
		m_oLabelWaiterName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelWaiterName, "lblWaiter");
		m_oFrameContent.attachChild(m_oLabelWaiterName);
		
		// Check No.
    	m_oLabelCheckNo = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLabelCheckNo, "lblCheckNo");
    	m_oFrameContent.attachChild(m_oLabelCheckNo);
		
    	m_oLabelTableNo = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLabelTableNo, "lblTableNo");
    	m_oFrameContent.attachChild(m_oLabelTableNo);
		
    	m_oLabelCover = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLabelCover, "lblCover");
    	m_oFrameContent.attachChild(m_oLabelCover);
		
    	m_oLabelCheckTotal = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLabelCheckTotal, "lblCheckTotal");
    	m_oFrameContent.attachChild(m_oLabelCheckTotal);
		
		// Confirm button
    	m_oButtonConfirm = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonConfirm, "butConfirm");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		this.attachChild(m_oButtonConfirm);	
		
		// Confirm button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);	
    }

	public void setupValue(FuncCheck oFuncCheck){
   		m_oLabelWaiterName.setValue(oFuncCheck.getOpenUserName());
		m_oLabelCheckNo.setValue(oFuncCheck.getCheckPrefixNo());
		m_oLabelTableNo.setValue(oFuncCheck.getTableNoWithTableName());
		m_oLabelCover.setValue(Integer.toString(oFuncCheck.getCover()));
		m_oLabelCheckTotal.setValue(AppGlobal.g_oFuncOutlet.get().roundCheckAmountToString(oFuncCheck.getCheckTotal()));
	}
	
	public void setTitle(String sTitle){
		m_oFrameTitleHeader.setTitle(sTitle);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
    	if(iChildId == m_oButtonConfirm.getId()) {								//Exit button clicked
    		for(FrameVoidCheckDetailListener listener : listeners) {
    			listener.FrameVoidCheckDetail_confirm();
    			break;
    		}
    		return true;
    	}
    	else if(iChildId == m_oButtonCancel.getId()) {					//Previous button clicked
    		for(FrameVoidCheckDetailListener listener : listeners) {
    			listener.FrameVoidCheckDetail_cancel();
    			break;
    		}
    		return true;
    	}
    	
    	return false;
    }
	
}
