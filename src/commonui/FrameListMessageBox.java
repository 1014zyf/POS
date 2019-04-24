package commonui;
import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIKeyboardReader;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;
/** interface for the listeners/observers callback method */
interface FrameListMessageBoxListener {
	void FrameListMessageBox_click(String sResult);
}

public class FrameListMessageBox extends VirtualUIFrame implements FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameColumnHeader;
	private VirtualUIFrame m_oFrameMessage;
	private VirtualUIList m_oListMessage;
	private VirtualUIFrame m_oColumnHeaderUnderline;
	private ArrayList<VirtualUIButton> m_aButtonArray;
	private ArrayList<VirtualUILabel> m_aColumnHeaderArray;
	private float m_fDelayTime;
	private VirtualUIKeyboardReader m_oKeyboardReaderForOK;
	private int m_iHeaderTextSize;
	private int m_iMessageTextSize;
	private String m_sTextAlignForAll;			//For all context, especially for cells which the column alignment is unspecified in m_oColumnAligns
	private ArrayList<String> m_oColumnAligns;	//For aligning context in columns specifically
	private String m_sMessagePaddingForAll;		//For all context, especially for cells which the column padding is unspecified in m_oColumnPaddings
	private ArrayList<String> m_oColumnPaddings;//For setting the padding of each column specifically
	private int m_iMessageIndex;
	private String m_sMessageBackground;
	private String m_sMessageForeground;
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameListMessageBoxListener> listeners;

	private VirtualUIFrame m_oUnderlineFrame;
	private boolean m_obColumnHeaderDisplay;
	
	private FrameTitleHeader m_oTitleHeader;
	
	private int m_iMessageHeight;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameListMessageBoxListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameListMessageBoxListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameListMessageBox(){
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameListMessageBoxListener>();
		m_oFrameColumnHeader = new VirtualUIFrame();
		m_oListMessage = new VirtualUIList();
		m_aButtonArray = new ArrayList<VirtualUIButton>();
		m_aColumnHeaderArray = new ArrayList<VirtualUILabel>();
		m_fDelayTime = 0;
		m_iHeaderTextSize = 24;
		m_iMessageTextSize = 24;
		m_sTextAlignForAll = "";
		m_oColumnAligns = new ArrayList<String>();
		m_sMessagePaddingForAll = "";
		m_oColumnPaddings = new ArrayList<String>();
		m_iMessageIndex = -1;
		m_sMessageBackground = "";
		m_sMessageForeground = "";
		m_iMessageHeight = 0;
	}

	public void init(boolean bHeaderDisplay){
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraListMessageBox.xml");
		
		m_obColumnHeaderDisplay = bHeaderDisplay;
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.setWidth(this.getWidth()- m_oTitleHeader.getLeft());
		m_oTitleHeader.init(false);
		m_oTitleHeader.addListener(this);
		this.attachChild(m_oTitleHeader);
		
		// ColumnHeaders
		m_oFrameColumnHeader = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameColumnHeader, "fraColumnHeader");
		m_oFrameColumnHeader.setTop(m_oTitleHeader.getTop() + m_oTitleHeader.getHeight());
		m_oFrameColumnHeader.setWidth(this.getWidth() - m_oFrameColumnHeader.getLeft()*2);
		this.attachChild(m_oFrameColumnHeader);
//KingsleyKwan20171018ByJohn	----- End -----
		m_oListMessage = new VirtualUIList();
		m_oListMessage.setExist(true);
		m_oListMessage.setTop(m_oFrameColumnHeader.getTop() + m_oFrameColumnHeader.getHeight());
		m_oListMessage.setLeft(m_oFrameColumnHeader.getLeft());
		m_oListMessage.setWidth(this.getWidth() - m_oListMessage.getLeft()*2);
		this.attachChild(m_oListMessage);
		
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderlineFrame, "fraSectionBarUnderline");
		m_oUnderlineFrame.setWidth(this.getWidth() - (m_oUnderlineFrame.getLeft()*2));
		this.attachChild(m_oUnderlineFrame);
		
		m_oListMessage.setHeight(m_oUnderlineFrame.getTop() - m_oTitleHeader.getHeight() - m_oFrameColumnHeader.getHeight());
		
		// Keyboard
		m_oKeyboardReaderForOK = new VirtualUIKeyboardReader();
		m_oKeyboardReaderForOK.addKeyboardKeyCode(13);
		this.attachChild(m_oKeyboardReaderForOK);
	}

	/*
	 * width total can not be more than 600
	 */
//KingsleyKwan20171101		-----Start-----
	public void addColumnHeader(String sColumnHeader , int iWidth){
//KingsleyKwan20171101		----- End -----
		int iLeft = 0;
		if(m_aColumnHeaderArray.size() > 0){
			for(VirtualUILabel oLabel :m_aColumnHeaderArray){
				iLeft += oLabel.getWidth();
			}
		}
		VirtualUILabel m_oLabelHeader = new VirtualUILabel();
		m_oLabelHeader.setExist(true);
		m_oLabelHeader.setTop(0);
		m_oLabelHeader.setLeft(iLeft);
		m_oLabelHeader.setWidth(iWidth);
		m_oLabelHeader.setHeight(m_oFrameColumnHeader.getHeight());
		m_oLabelHeader.setValue(sColumnHeader);

//KingsleyKwan20170918ByNick		-----Start-----
		m_oLabelHeader.setTextSize(m_iHeaderTextSize);

		// Set alignment: use m_sTextAlignForAll as the text align only if the alignment is unspecified in m_oColumnAligns
		if (!m_oColumnAligns.isEmpty() && m_oColumnAligns.size() > m_aColumnHeaderArray.size()
				&& !m_oColumnAligns.get(m_aColumnHeaderArray.size()).isEmpty())
			m_oLabelHeader.setTextAlign(m_oColumnAligns.get(m_aColumnHeaderArray.size()));
		else if (!m_sTextAlignForAll.isEmpty())
			m_oLabelHeader.setTextAlign(m_sTextAlignForAll);
		else
			m_oLabelHeader.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

		// Set padding: use m_sMessagePaddingForAll as the padding value only if the padding value is unspecified in m_oColumnPaddings
		if (!m_oColumnPaddings.isEmpty() && m_oColumnPaddings.size() > m_aColumnHeaderArray.size()
				&& !m_oColumnPaddings.get(m_aColumnHeaderArray.size()).isEmpty())
			m_oLabelHeader.setPaddingValue(m_oColumnPaddings.get(m_aColumnHeaderArray.size()));
		else if (!m_sMessagePaddingForAll.isEmpty())
			m_oLabelHeader.setPaddingValue(m_sMessagePaddingForAll);

		m_oFrameColumnHeader.attachChild(m_oLabelHeader);
		m_aColumnHeaderArray.add(m_oLabelHeader);
		if (!m_obColumnHeaderDisplay) {
			m_oFrameColumnHeader.setHeight(m_oFrameColumnHeader.getHeight() / 4);
			m_oListMessage.setTop(m_oFrameColumnHeader.getTop() + m_oFrameColumnHeader.getHeight());
		} else{
			m_oColumnHeaderUnderline = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oColumnHeaderUnderline, "fraUnderline");
			m_oColumnHeaderUnderline.setWidth(m_oFrameColumnHeader.getWidth());
			m_oColumnHeaderUnderline.setTop(m_oFrameColumnHeader.getTop() + m_oFrameColumnHeader.getHeight());
			m_oColumnHeaderUnderline.setLeft(m_oFrameColumnHeader.getLeft());
			m_oColumnHeaderUnderline.setWidth(this.getWidth() - m_oColumnHeaderUnderline.getLeft() * 2);
			this.attachChild(m_oColumnHeaderUnderline);
		}
	}
	
//KingsleyKwan20171018ByJohn	-----Start-----
	public void setTitleUnderLineVisible(boolean bVisible){
		m_oColumnHeaderUnderline.setVisible(bVisible);
	}
	
	public void setTitleDisable(){
		m_oTitleHeader.setTitleShow(false);
		m_oFrameColumnHeader.setTop(0);
		m_oListMessage.setTop(m_oFrameColumnHeader.getHeight());
		m_oUnderlineFrame.setTop(m_oFrameColumnHeader.getTop()+m_oFrameColumnHeader.getHeight());
		m_oColumnHeaderUnderline.setTop(50);
	}
	
	public void setTitleUnderlineDisable(){
	//	m_oTitleHeader.setUnderlineShow(false);
	}
//KingsleyKwan20171018ByJohn	----- End -----

	public void setHeaderTextSize(int iTextSize) {
		m_iHeaderTextSize = iTextSize;
	}

	public void setMessageTextSize(int iTextSize) {
		m_iMessageTextSize = iTextSize;
	}

	public void setMessageTextAlign(String sAlign) {
		m_sTextAlignForAll = sAlign;
	}

	public void setMessageTextAlign(ArrayList<String> sAligns) {
		m_oColumnAligns = sAligns;
	}

	public void setMessagePadding(String sPadding) {
		m_sMessagePaddingForAll = sPadding;
	}

	public void setMessagePadding(ArrayList<String> sPaddings) {
		m_oColumnPaddings = sPaddings;
	}

	public void setMessageColor(int iIndex, String sBackground, String sForeground){
		m_iMessageIndex = iIndex;
		m_sMessageBackground = sBackground;
		m_sMessageForeground = sForeground;
	}

	public void addMessage(ArrayList<String> sMessage){
		m_oFrameMessage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameMessage, "fraMessage");
		m_oFrameMessage.setWidth(m_oFrameColumnHeader.getWidth());
		m_oFrameMessage.setExist(true);
		
		if(m_iMessageHeight != 0)
			m_oFrameMessage.setHeight(m_iMessageHeight);
		
		for (int i = 0; i < m_aColumnHeaderArray.size(); i++) {
			VirtualUILabel m_oLabelMessage = new VirtualUILabel();
			m_oLabelMessage.setExist(true);
			m_oLabelMessage.setTop(0);
			m_oLabelMessage.setLeft(m_aColumnHeaderArray.get(i).getLeft());
			m_oLabelMessage.setWidth(m_aColumnHeaderArray.get(i).getWidth());
			m_oLabelMessage.setHeight(m_oFrameMessage.getHeight());
			m_oLabelMessage.setValue(sMessage.get(i));
			if(i== m_iMessageIndex){
				m_oLabelMessage.setBackgroundColor(m_sMessageBackground);
				m_oLabelMessage.setForegroundColor(m_sMessageForeground);
			}
			m_oLabelMessage.setTextSize(m_iMessageTextSize);

			// Set alignment: use m_sTextAlignForAll as the text align only if the alignment is unspecified in m_oColumnAligns
			if (!m_oColumnAligns.isEmpty() && m_oColumnAligns.size() > i && !m_oColumnAligns.get(i).isEmpty())
				m_oLabelMessage.setTextAlign(m_oColumnAligns.get(i));
			else if(!m_sTextAlignForAll.isEmpty())
				m_oLabelMessage.setTextAlign(m_sTextAlignForAll);
			else
				m_oLabelMessage.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);

			// Set padding: use m_sMessagePaddingForAll as the padding value only if the padding value is unspecified in m_oColumnPaddings
			if (!m_oColumnPaddings.isEmpty() && m_oColumnPaddings.size() > i && !m_oColumnPaddings.get(i).isEmpty())
				m_oLabelMessage.setPaddingValue(m_oColumnPaddings.get(i));
			else if (!m_sMessagePaddingForAll.isEmpty())
				m_oLabelMessage.setPaddingValue(m_sMessagePaddingForAll);
			m_oFrameMessage.attachChild(m_oLabelMessage);
		}
//KingsleyKwan20171018ByJohn	-----Start-----
		VirtualUIFrame oUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oUnderlineFrame, "fraUnderline");
		oUnderlineFrame.setTop(0);
		oUnderlineFrame.setWidth(m_oFrameMessage.getWidth() - 4);
		if(m_oListMessage.getChildCount() > 0)
			m_oFrameMessage.attachChild(oUnderlineFrame);
//KingsleyKwan20171018ByJohn	----- End -----
//KingsleyKwan20170918ByNick		----- End -----
		m_oListMessage.attachChild(m_oFrameMessage);
	}

	public void addSingleButton(String sBtnValue) {
		VirtualUIButton oNewBtn = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(oNewBtn, "singleBtn");
		oNewBtn.setValue(sBtnValue);
		oNewBtn.setTop(this.getHeight() - oNewBtn.getHeight() - 20);
		oNewBtn.setLeft((this.getWidth() / 2) - (oNewBtn.getWidth() / 2));
		oNewBtn.setClickServerRequestBlockUI(false);
		this.attachChild(oNewBtn);

		// Resize the message label
//KingsleyKwan20170918ByNick		-----Start-----
		m_oListMessage.setHeight(this.getHeight() - m_oTitleHeader.getHeight() - m_oFrameColumnHeader.getHeight() - (oNewBtn.getHeight() + 39) - 5);
		m_oUnderlineFrame.setTop(m_oListMessage.getTop() + m_oListMessage.getHeight());
//KingsleyKwan20170918ByNick		----- End -----
		m_aButtonArray.add(oNewBtn);
	}

	public void setTitle(String sTitle){
		m_oTitleHeader.setTitle(sTitle);
	}

	public float getDelayTime(){
		return m_fDelayTime;
	}

	public void setDelayTime(float fDelayTime){
		m_fDelayTime = fDelayTime;
	}
	
	public void clearColumnHeaderArray() {
		m_aColumnHeaderArray.clear();
	}
	
	public void setCloseButtonVisible(boolean bShow) {
		m_oTitleHeader.setButtonShow(bShow);
	}
	
	public void setFrameMessageHeight(int iHeight) {
		m_iMessageHeight = iHeight;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		for (FrameListMessageBoxListener listener : listeners) {
			// Find the clicked button
			for (VirtualUIButton oBtn : m_aButtonArray) {
//KingsleyKwan20170918ByNick		-----Start-----
				if (oBtn.getId() == iChildId) {
//KingsleyKwan20170918ByNick		----- End -----
					// Raise the event to parent
					listener.FrameListMessageBox_click(oBtn.getLabel().getValue());
					bMatchChild = true;
					break;
				}
			}
		}
		
		return bMatchChild;
	}

	@Override
	public boolean keyboard(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (iChildId == m_oKeyboardReaderForOK.getId()) {
			for (FrameListMessageBoxListener listener : listeners) {
				// Raise the event to parent
				listener.FrameListMessageBox_click("");
			}
			
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameListMessageBoxListener listener : listeners) {
			// Raise the event to parent
			listener.FrameListMessageBox_click("close");
			break;
		}
	}
}

