package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
import virtualui.VirtualUIWebView;
//JohnLiu 06112017 -- start
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
//JohnLiu 06112017 -- end
/** interface for the listeners/observers callback method */
interface FrameDirectReportListener {
	void frameDirectReport_clickExit();
	void frameDirectReport_clickPrint();
	void frameDirectReport_clickReport(int iReportIndex);
}
//JohnLiu 06112017 -- start
public class FrameDirectReport extends VirtualUIFrame implements FrameTitleHeaderListener{
//JohnLiu 06112017 -- end
	TemplateBuilder m_oTemplateBuilder;
//JohnLiu 06112017 -- start
/*	private VirtualUIFrame m_oFrameHeader;
	private VirtualUILabel m_oLabelHeader;
	private VirtualUIFrame m_oFraButtonClose;*/
//JohnLiu 06112017 -- end
	private VirtualUIFrame m_oFrameContent;
	private VirtualUIWebView m_oWebViewReport;
	private VirtualUIList m_oListReportType;
	private VirtualUIHorizontalList m_oHorizontalTabList;
	private VirtualUIButton m_oButtonPrint;
	//private VirtualUIButton m_oButtonExit;
//JohnLiu 06112017 -- start
	private FrameTitleHeader m_oTitleHeader;
//JohnLiu 06112017 -- end
	
	private ArrayList<VirtualUILabel> m_oReportTypeLabel;
	private ArrayList<VirtualUIFrame> m_oReportTypeFrames;
	private int m_iCurrentReportTypeIndex;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameDirectReportListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameDirectReportListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameDirectReportListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FrameDirectReport(List<HashMap<String, String>> oReportTypes) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameDirectReportListener>();
		m_oReportTypeLabel = new ArrayList<VirtualUILabel>();
		m_oReportTypeFrames = new ArrayList<VirtualUIFrame>();
		m_iCurrentReportTypeIndex = 0;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraDirectReport.xml");
//JohnLiu 06112017 -- start
		m_oTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
        m_oTitleHeader.init(true);
        m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("direct_report"));
        m_oTitleHeader.addListener(this);
        this.attachChild(m_oTitleHeader);
        
		// Content
		m_oFrameContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		this.attachChild(m_oFrameContent);
//JohnLiu 06112017 -- end			
		
		// Web view for report
		m_oWebViewReport = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewReport, "wbvReport");
		m_oFrameContent.attachChild(m_oWebViewReport);
		
		// Report type list
		m_oListReportType = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListReportType, "listReportType");
		m_oFrameContent.attachChild(m_oListReportType);
		
		m_oHorizontalTabList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalTabList, "horListTab");
		m_oFrameContent.attachChild(m_oHorizontalTabList);
		
		VirtualUIFrame oFramePanelLookupSeparator = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFramePanelLookupSeparator, "fraPanelTabSeparator");
		oFramePanelLookupSeparator.setEnabled(false);
		m_oFrameContent.attachChild(oFramePanelLookupSeparator);
		addReportType(oReportTypes);
		changeReportTypeLabelBackground();
		
		// Print Button
		m_oButtonPrint = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonPrint, "butPrint");
		m_oButtonPrint.setValue(AppGlobal.g_oLang.get()._("print"));
		m_oButtonPrint.setVisible(true);
		m_oFrameContent.attachChild(m_oButtonPrint);
	}
	

//JohnLiu 06112017 -- end
	private void addReportType(List<HashMap<String, String>> oReportTypes) {
		VirtualUILabel oLabelOption;
		VirtualUILabel oLblPanelLabel;
		VirtualUIFrame oFraPageTabUnderline;
		VirtualUIFrame oFraPageTabBase;
		
		if(m_oListReportType != null) {
			for(HashMap<String, String> oReportType:oReportTypes) {
				oLabelOption = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabelOption, "listOption");
				oLabelOption.setValue(oReportType.get("Name"));
				oLabelOption.setWidth(144);
				oLabelOption.setEnabled(true);
				oLabelOption.allowClick(true);
				m_oListReportType.attachChild(oLabelOption);
				m_oReportTypeLabel.add(oLabelOption);
			}
		}
		
		if(m_oHorizontalTabList != null) {
			for(HashMap<String, String> oReportType:oReportTypes) {
				oLblPanelLabel = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLblPanelLabel, "lblPanelTab");
				oLblPanelLabel.setValue(oReportType.get("Name"));
				oLblPanelLabel.setEnabled(true);
				oLblPanelLabel.allowClick(false);
				
				oFraPageTabUnderline = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(oFraPageTabUnderline, "fraPanelTabUnderline");
				oFraPageTabUnderline.setVisible(false);
				oFraPageTabUnderline.setEnabled(false);
				oFraPageTabUnderline.allowClick(false);
				
				oFraPageTabBase = new VirtualUIFrame();
				m_oTemplateBuilder.buildFrame(oFraPageTabBase, "fraPanelTabBase");
				oFraPageTabBase.setEnabled(true);
				oFraPageTabBase.allowClick(true);
				oFraPageTabBase.setClickServerRequestBlockUI(true);
				oFraPageTabBase.attachChild(oLblPanelLabel);
				oFraPageTabBase.attachChild(oFraPageTabUnderline);
				
				m_oHorizontalTabList.attachChild(oFraPageTabBase);
				m_oReportTypeFrames.add(oFraPageTabBase);
			}
		}
	}
	
	private void changeReportTypeLabelBackground() {
		if(m_oReportTypeLabel != null) {
			for(int i=0; i<m_oReportTypeLabel.size(); i++) {
				if(i == m_iCurrentReportTypeIndex) {
//JohnLiu 06112017 -- start
					m_oReportTypeLabel.get(i).setBackgroundColor("#0055B8");
					m_oReportTypeLabel.get(i).setForegroundColor("#FFFFFF");
					m_oReportTypeLabel.get(i).setStrokeColor("#005080");
				} else {
					m_oReportTypeLabel.get(i).setBackgroundColor("#FFFFFF");
					m_oReportTypeLabel.get(i).setForegroundColor("#333333");
					m_oReportTypeLabel.get(i).setStrokeColor("#868686");
				}
//JohnLiu 06112017 -- end
			}
		}
		
		if(m_oHorizontalTabList != null) {
			VirtualUIFrame oFrame;
			for(int i=0; i<m_oReportTypeFrames.size(); i++) {
				oFrame = m_oReportTypeFrames.get(i);
				if(i == m_iCurrentReportTypeIndex) {
					for(VirtualUIBasicElement oChild : oFrame.getChilds()) {
						if(oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
							VirtualUILabel oLabel = (VirtualUILabel)oChild;
							oLabel.setForegroundColor("#005389");
						}else {
							VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
							oUnderlineFrame.setVisible(true);
						}
					}
				} else {
					for(VirtualUIBasicElement oChild : oFrame.getChilds()) {
						if(oChild.getUIType() == HeroActionProtocol.View.Type.LABEL) {
							VirtualUILabel oLabel = (VirtualUILabel)oChild;
							oLabel.setForegroundColor("#66a1c1");
						}else {
							VirtualUIFrame oUnderlineFrame = (VirtualUIFrame)oChild;
							oUnderlineFrame.setVisible(false);
						}
					}
				}
			}
		}
	}
	
	public String getReportURL() {
		return m_oWebViewReport.getSource();
	}
	
	public void setReportURL(String sURL) {
		m_oWebViewReport.setSource(sURL);
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
//JohnLiu 06112017 -- start 	
		
		// Find the clicked button
		if(m_oButtonPrint.getId() == iChildId) {
			for (FrameDirectReportListener listener : listeners) {
				// Raise the event to parent
				listener.frameDirectReport_clickPrint();
			}
			bMatchChild = true;
		}
//JohnLiu 06112017 -- end
		
		if(bMatchChild == false){
			// Find the clicked label
			if(m_oReportTypeLabel != null) {
				for (int iCount=0; iCount<m_oReportTypeLabel.size(); iCount++) {
					VirtualUILabel oLbl = m_oReportTypeLabel.get(iCount);
					if (oLbl.getId() == iChildId) {
						m_iCurrentReportTypeIndex = iCount;
						changeReportTypeLabelBackground();
						for(FrameDirectReportListener listener : listeners) {
							// Raise the event to parent
							listener.frameDirectReport_clickReport(iCount);
						}
						
			       		bMatchChild = true;
			       		break;
					}
				}
			}
			
			if(m_oHorizontalTabList != null) {
				for (int iCount=0; iCount<m_oReportTypeFrames.size(); iCount++) {
					VirtualUIFrame oFrame = m_oReportTypeFrames.get(iCount);
					if (oFrame.getId() == iChildId) {
						m_iCurrentReportTypeIndex = iCount;
						changeReportTypeLabelBackground();
						for(FrameDirectReportListener listener : listeners) {
							// Raise the event to parent
							listener.frameDirectReport_clickReport(iCount);
						}
						
			       		bMatchChild = true;
			       		break;
					}
				}
			}
		}
		
    	return bMatchChild;
    }
//JohnLiu 06112017 -- start
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameDirectReportListener listener : listeners){
	       	listener.frameDirectReport_clickExit();
		}
	}
//JohnLiu 06112017 -- end
}
