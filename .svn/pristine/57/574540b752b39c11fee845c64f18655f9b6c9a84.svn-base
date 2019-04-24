package app;

import java.util.ArrayList;


import templatebuilder.TemplateBuilder;
import virtualui.*;

/** interface for the listeners/observers callback method */
interface FrameCheckDetailListener {
	void frameCheckDetail_Clicked(String sFuncValue);
}

public class FrameCheckDetail extends VirtualUIFrame {
	
	public static String FOR_FORM_MAIN = "form_main";
	public static String FOR_FRAME_CHECK_FUNCTION = "frame_check_function";
	
	TemplateBuilder m_oTemplateBuilder;
	
	VirtualUIFrame m_oFrameTable;
	VirtualUIFrame m_oFrameCover;
	VirtualUIFrame m_oFrameDetail;
	VirtualUIImage m_oImageTableBg;
	VirtualUIImage m_oImageCoverBg;
	VirtualUIImage m_oImageDetailBg;
	VirtualUIImage m_oImageDetailIcon;
	VirtualUILabel m_oLabelTableNoHeader;
	VirtualUILabel m_oLabelCoverNoHeader;
	VirtualUILabel m_oLabelTableNo;
	VirtualUILabel m_oLabelCover;
	VirtualUILabel m_oLabelTableDetail;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCheckDetailListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCheckDetailListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCheckDetailListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
    //Constructor
   	public FrameCheckDetail() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckDetailListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckDetail.xml");
		m_oFrameTable = new VirtualUIFrame();
   	}
   	
   	//function to create buttons
   	//params - sForFrame (where is fame will be located / for, use static variable FrameCheckDetail.FOR_XXXXXX)
   	public void createFunctionButtons(String sForFrame) {
		// Load child elements from template
		/////////////////////////////////////////////////////////////////
		// For Table Frame
		m_oFrameTable = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTable, "fraTable");
		m_oFrameTable.allowClick(true);
		m_oFrameTable.setEnabled(true);
		m_oFrameTable.setClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameTable);
		
		m_oImageTableBg = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageTableBg, "ImgTableBg");
		m_oImageTableBg.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageTableBg.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "buttons/ordering_table_button.png");
		m_oImageTableBg.setEnabled(true);
		m_oImageTableBg.allowClick(false);
		m_oFrameTable.attachChild(m_oImageTableBg);

		// Table No. Header
		m_oLabelTableNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNoHeader, "lblTableNoHeader");
		m_oLabelTableNoHeader.setEnabled(true);
		m_oLabelTableNoHeader.allowClick(false);
		m_oLabelTableNoHeader.setValue(AppGlobal.g_oLang.get()._("table_no", ":"));
		m_oFrameTable.attachChild(m_oLabelTableNoHeader);
		
		m_oLabelTableNo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableNo, "lblTableNum");
		m_oLabelTableNo.setEnabled(true);
		m_oLabelTableNo.allowClick(false);
		m_oFrameTable.attachChild(m_oLabelTableNo);
		/////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////
		// For Cover Frame
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCover");
		m_oFrameCover.allowClick(true);
		m_oFrameCover.setEnabled(true);
		m_oFrameCover.setClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameCover);
		
		m_oImageCoverBg = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageCoverBg, "ImgCoverBg");
		m_oImageCoverBg.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageCoverBg.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "buttons/ordering_cover_button.png");
		m_oImageCoverBg.setEnabled(true);
		m_oImageCoverBg.allowClick(false);
		m_oFrameCover.attachChild(m_oImageCoverBg);
		
		// Table No. Header
		m_oLabelCoverNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCoverNoHeader, "lblCoverHeader");
		m_oLabelCoverNoHeader.setEnabled(true);
		m_oLabelCoverNoHeader.allowClick(false);
		m_oLabelCoverNoHeader.setValue(AppGlobal.g_oLang.get()._("cover2", ":"));
		m_oFrameCover.attachChild(m_oLabelCoverNoHeader);
		
		m_oLabelCover = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCover, "lblCoverNum");
		m_oLabelCover.setEnabled(true);
		m_oLabelCover.allowClick(false);
		m_oFrameCover.attachChild(m_oLabelCover);
		/////////////////////////////////////////////////////////////////
		
		/////////////////////////////////////////////////////////////////
		// For Detail Frame
		m_oFrameDetail = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDetail, "fraDetail");
		m_oFrameDetail.allowClick(true);
		m_oFrameDetail.setEnabled(true);
		this.attachChild(m_oFrameDetail);
		
		m_oImageDetailBg = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDetailBg, "ImgDetailBg");
		m_oImageDetailBg.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageDetailBg.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "buttons/ordering_check_detail_button.png");
		m_oImageDetailBg.setEnabled(true);
		m_oImageDetailBg.allowClick(false);
		m_oFrameDetail.attachChild(m_oImageDetailBg);
		
		//Table Detail label
		m_oLabelTableDetail = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableDetail, "lblTableDetail");
		m_oLabelTableDetail.setEnabled(true);
		m_oLabelTableDetail.allowClick(false);
		m_oLabelTableDetail.setValue(AppGlobal.g_oLang.get()._("whole_check", ""));
		if(sForFrame.equals(FrameCheckDetail.FOR_FRAME_CHECK_FUNCTION))
			m_oLabelTableDetail.setValue(AppGlobal.g_oLang.get()._("back", ""));
		m_oFrameDetail.attachChild(m_oLabelTableDetail);
		
		m_oImageDetailIcon = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDetailIcon, "ImgDetailIcon");
		m_oImageDetailBg.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_TO_FIT);
		m_oImageDetailIcon.setEnabled(true);
		m_oImageDetailIcon.allowClick(false);
		m_oFrameDetail.attachChild(m_oImageDetailIcon);
		/////////////////////////////////////////////////////////////////
	}
	
   	public void setTableBgImage(String sImageUrl) {
   		m_oImageTableBg.setSource(sImageUrl);
   	}
   	
   	public void setCoverBgImage(String sImageUrl) {
   		m_oImageCoverBg.setSource(sImageUrl);
   	}
   	
   	public void setDetailBgImage(String sImageUrl) {
   		m_oImageDetailBg.setSource(sImageUrl);
   	}
   	
	public void setDetailIconImage(String sImageUrl) {
		m_oImageDetailIcon.setSource(sImageUrl);
	}
   	
   	public void setTableFrameVisible(boolean bVisible) {
   		m_oFrameTable.setVisible(bVisible);
   	}
   	
   	public void setTableNo(String[] sTableNames){
   		m_oLabelTableNo.setValue(sTableNames);
   	}
   	
   	public String getTableNo(){
   		return m_oLabelTableNo.getValue();
   	}
   	
   	public void setCover(int iCover){
   		m_oLabelCover.setValue(iCover + "");
   	}
   	
   	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oFrameTable.getId()) {
        	for (FrameCheckDetailListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameCheckDetail_Clicked("Table");
           		break;
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oFrameCover.getId()) {
        	for (FrameCheckDetailListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameCheckDetail_Clicked("Cover");
           		break;
            }
			bMatchChild = true;
		}
		else if (iChildId == m_oFrameDetail.getId()) {
        	for (FrameCheckDetailListener listener : listeners) {
        		// Raise the event to parent
           		listener.frameCheckDetail_Clicked("Detail");
           		break;
            }
			bMatchChild = true;
		}

        return bMatchChild;
    }
}
