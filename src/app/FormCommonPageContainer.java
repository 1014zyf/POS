package app;

import java.util.ArrayList;

import commonui.FormSelectionBox;
import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

interface FormCommonPageContainerListener {
	void FormCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName);
}

public class FormCommonPageContainer extends VirtualUIForm implements FrameCommonPageContainerListener {
	private FrameCommonPageContainer m_oCommonPageContainer;
	private VirtualUIFrame m_oCoverFrame;
	private VirtualUIFrame m_oContentFrame;
	private ArrayList<Integer> m_oResultList;
	
	private FormSelectionBox m_oSelectionBox;
	
	private boolean m_bUserCancel;
	private boolean m_bMultiSelect;

	private int m_iContainerWidth;
	private int m_iContainerHeight;
	
	public static int TAG_WIDTH = 100;
	public static int TAG_HEIGHT = 100;
	public static int MAX_TAG = 5;
	public static int MAX_MARGING = 200;
	
	public static String SELECT_FONT_COLOR = "#0055B8";
	public static String UNSELECT_FONT_COLOR = "#999999";
	public static String SELECT_BACKGROUND_COLOR = "";
	public static String UNSELECT_BACKGROUND_COLOR = "";

	TemplateBuilder m_oTemplateBuilder;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormCommonPageContainerListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FormCommonPageContainerListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FormCommonPageContainerListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FormCommonPageContainer(Controller oParentController) {
		super(oParentController);
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oSelectionBox = new FormSelectionBox(oParentController);
		}
		else{
			m_oTemplateBuilder = new TemplateBuilder();
			m_oCommonPageContainer = new FrameCommonPageContainer();
			m_oResultList = new ArrayList<Integer>();
			
			m_bUserCancel = false;
			m_bMultiSelect = false;
			
			listeners = new ArrayList<FormCommonPageContainerListener>();

			// Load form from template file
									   
			m_oTemplateBuilder.loadTemplate("frmCommonPageContainer.xml");
								  

			// Background Cover Page
			m_oCoverFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oCoverFrame, "fraCoverFrame");
			this.attachChild(m_oCoverFrame);

			m_oContentFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oContentFrame, "fraContentFrame");
			m_oCoverFrame.attachChild(m_oContentFrame);
			m_oCommonPageContainer = new FrameCommonPageContainer();
			m_oTemplateBuilder.buildFrame(m_oCommonPageContainer, "fraCommonPageContainerFrame");
			//m_oCommonPageContainer.addListener(this);
			m_oContentFrame.attachChild(m_oCommonPageContainer);

			m_iContainerWidth = m_oCommonPageContainer.getWidth();
			m_iContainerHeight = m_oCommonPageContainer.getHeight();
			
			init(m_oCommonPageContainer.getWidth(), m_oCommonPageContainer.getHeight(), FormCommonPageContainer.TAG_WIDTH, FormCommonPageContainer.TAG_HEIGHT, FormCommonPageContainer.MAX_TAG, FormCommonPageContainer.SELECT_FONT_COLOR, FormCommonPageContainer.UNSELECT_FONT_COLOR, FormCommonPageContainer.SELECT_BACKGROUND_COLOR, FormCommonPageContainer.UNSELECT_BACKGROUND_COLOR, MAX_MARGING, false, false);
			m_oCommonPageContainer.addListener(this);
		}
	}

	// init the common page container frame
	public void init(int iFrameWidth, int iFrameHeight, int iTagWidth, int iTagHeight, int iMaxTag,
			String sFontColorSelected, String sFontColorUnselected, String sBgColorSelected, String sBgColorUnselected,
			int iMaxMargin, boolean bShownUpperline, boolean bShowUnderline) {
		if (iFrameWidth != 0)
			m_iContainerWidth = iFrameWidth;

		if (iFrameHeight != 0)
			m_iContainerHeight = iFrameHeight;

		m_oContentFrame.setWidth(m_iContainerWidth);
		m_oContentFrame.setHeight(m_iContainerHeight);
		m_oContentFrame.setLeft((m_oCoverFrame.getWidth() - m_iContainerWidth) / 2);
		m_oContentFrame.setTop((m_oCoverFrame.getHeight() - m_iContainerHeight) / 2);

		m_oCommonPageContainer.setWidth(m_iContainerWidth);
		m_oCommonPageContainer.setHeight(m_iContainerHeight);
		m_oCommonPageContainer.init(m_iContainerWidth, m_iContainerHeight, iTagWidth, iTagHeight, iMaxTag,
				sFontColorSelected, sFontColorUnselected, sBgColorSelected, sBgColorUnselected, iMaxMargin,
				bShownUpperline, bShowUnderline);

		m_oCommonPageContainer.addListener(this);
	}

	// add tag button to frame
	public <T> void addButton(String[] sName, T oFrame) {
		m_oCommonPageContainer.addButton(sName, oFrame);
	}
	
	public void addButton(String sDesc, String sKey) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.addButton(sDesc, sKey);
	}
	
	public void setTitle(String sTitle, Boolean bHiddenCancelButton) {
		m_oCommonPageContainer.setTitle(sTitle, bHiddenCancelButton);
	}
	
	public void setTitle(String sTitle) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.setTitle(sTitle);
	}
	
	public void initWithSingleSelection(String sTitleValue, ArrayList<String> oOptionList, Boolean bHiddenCancelButton){
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oSelectionBox.initWithSingleSelection(sTitleValue, oOptionList, bHiddenCancelButton);
			m_oSelectionBox.show();
		}
		else{
			m_bMultiSelect = false;
			
			this.setTitle(sTitleValue, bHiddenCancelButton);
			this.setOption(oOptionList);
			
			super.show();
		}
	}
	
	public void initWithMultiSelection(String sTitleValue, ArrayList<String> oOptionList) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			m_oSelectionBox.initWithMultiSelection(sTitleValue, oOptionList);
			m_oSelectionBox.show();
		}
		else{
			m_bMultiSelect = true;
			
			this.setTitle(sTitleValue, false);
			this.setOption(oOptionList);
			super.show();
		}
	}
	
	public void setOption(ArrayList<String> oOptionList) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.setOption(oOptionList);
		else
			m_oCommonPageContainer.addButtonInSingleTag(oOptionList);
	}
	
	public void setOptionTextAlign(String sAlign) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.setOptionTextAlign(sAlign);
		else
			m_oCommonPageContainer.setButtonTextAlign(sAlign);
	}
		
	public boolean isMultiSelect() {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			return m_oSelectionBox.isMultiSelect();
		else
			return m_bMultiSelect;
	}
		
	public ArrayList<Integer> getResultList() {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			ArrayList<Integer> oResult = m_oSelectionBox.getResultList();
			m_oSelectionBox.finishShow();
			return oResult;
		}
		else
			return m_oResultList;
	}
	
	public void setResultList(ArrayList<Integer> oResultList) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.setResultList(oResultList);
		else
			m_oResultList = oResultList;
	}
	
	public boolean isUserCancel() {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			return m_oSelectionBox.isUserCancel();
		else
			return m_bUserCancel;
	}
	
	/*@Override
	public void show(){
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oSelectionBox.show();
	}*/

	@Override
	public void frameCommonPageContainer_changeFrame() {

	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		for (FormCommonPageContainerListener listener : listeners)
			listener.FormCommonPageContainer_updateFrame(iOrigIndex, iIndex, sFrameName);
	}

	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void frameCommonPageContainer_CloseImageClicked() {
		// TODO Auto-generated method stub
		m_bUserCancel = true;
		this.finishShow();
	}
	
	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
		if(m_bMultiSelect){
			m_oResultList.add(iIndex);
		}
		else{
			m_oResultList.add(iIndex);
			this.finishShow();
		}
	}
	
	@Override
	public void show() {
		
	}
}
