package app;

import java.util.ArrayList;

import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import om.OutMediaObject;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameLoginListener {
	void FrameLogin_clickTimeInOut();
	void FrameLogin_clickOK();
	void FrameLogin_clickExit();
	void FrameLogin_clickSelectBox();
	void FrameLogin_swipeCard(String sSwipeCardValue);
	void FrameLogin_clickShowSmartCard();
}

public class FrameLogin extends VirtualUIFrame implements FrameNumberPadListener {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameLoginSection;
	private VirtualUIFrame m_oFrameTabBackground;
	private VirtualUIFrame m_oFrameIdPwd;
	private VirtualUIImage m_oImageOutlet;
	private VirtualUILabel m_oLabelOutlet;
	private VirtualUILabel m_oLabelId;
	private VirtualUILabel m_oLabelPwd;
	private VirtualUILabel m_oLabelMsg;
	private VirtualUITextbox m_otxtboxId;
	private VirtualUITextbox m_otxtboxPwd;
	private VirtualUIButton m_oButtonOK;
	private VirtualUIButton m_oButtonExit;
	private VirtualUIButton m_oButtonTimeInOut;
	private VirtualUIFrame m_oButtonSmartCard;
	private VirtualUILabel m_oLabelVersion;
	private FrameNumberPad m_oFrameNumberPad;
	private VirtualUIImage m_oImageSelected;
	private VirtualUIImage m_oImageDeselected;
	private VirtualUILabel m_oLabelHoldId;
	private VirtualUIFrame m_oFrameScreenSaver;
	private VirtualUIImage m_oFrameScreenSaverImage;
	private VirtualUILabel m_oLabelOutletTab;
	private int m_iOutletNameAndLogoHeight;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameLoginListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameLoginListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameLoginListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public void init(boolean bSupportSmartCardAsEmployeeCard){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameLoginListener>();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraLogin.xml");
		
		// Background Cover Page
		m_oFrameScreenSaver = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameScreenSaver, "fraScreenSaver");
		m_oFrameScreenSaver.setVisible(false);
		m_oFrameScreenSaver.allowClick(true);
		m_oFrameScreenSaver.setBackgroundColor(AppGlobal.g_oFuncStation.get().getScreenSaverColor());
		this.attachChild(m_oFrameScreenSaver);
		
		// Screen Saver Image
		m_oFrameScreenSaverImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oFrameScreenSaverImage, "fraScreenSaverImage");
		m_oFrameScreenSaverImage.setVisible(false);
		m_oFrameScreenSaverImage.allowClick(true);
		m_oFrameScreenSaverImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
		m_oFrameScreenSaverImage.setSource(AppGlobal.g_oFuncOutlet.get().getMediaUrl(OutMediaObject.USED_FOR_PHOTO_GALLERY));
		this.attachChild(m_oFrameScreenSaverImage);	
		
		//Login Background
		VirtualUIFrame oFrameLoginBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrameLoginBackground, "fraLoginBackground");
		this.attachChild(oFrameLoginBackground);
		
		// Login Frame
		m_oFrameLoginSection = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameLoginSection, "fraLoginSection");
		this.attachChild(m_oFrameLoginSection);
		
		// Tab background
		m_oFrameTabBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameTabBackground, "fraTabBackground");
		m_oFrameLoginSection.attachChild(m_oFrameTabBackground);
		
		// Number pad
		m_oFrameIdPwd = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameIdPwd, "fraIdPwd");
		m_oFrameLoginSection.attachChild(m_oFrameIdPwd);

		// Create outlet Label
		m_oLabelOutlet = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOutlet, "lblOutlet");
		m_oFrameIdPwd.attachChild(m_oLabelOutlet);
		m_iOutletNameAndLogoHeight = m_oLabelOutlet.getHeight();
		
		// Create ID Label
		m_oLabelId = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelId, "lblID");
		m_oLabelId.setValue(AppGlobal.g_oLang.get()._("user_id"));
		m_oFrameIdPwd.attachChild(m_oLabelId);
		
		// Create Pwd Label
		m_oLabelPwd = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelPwd, "lblPwd");
		m_oLabelPwd.setValue(AppGlobal.g_oLang.get()._("password"));
		m_oFrameIdPwd.attachChild(m_oLabelPwd);
		
		// Create Message Label
		m_oLabelMsg = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMsg, "lblMessage");
		m_oFrameIdPwd.attachChild(m_oLabelMsg);
		
		// Create ID Textbox
		m_otxtboxId = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_otxtboxId, "txtboxID");
		m_otxtboxId.setFocusWhenShow(true);
		m_oFrameIdPwd.attachChild(m_otxtboxId);
		
		// Create Pwd Textbox
		m_otxtboxPwd = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_otxtboxPwd, "txtboxPwd");
		m_otxtboxPwd.setInputType(HeroActionProtocol.View.Attribute.InputType.PASSWORD);
		m_oFrameIdPwd.attachChild(m_otxtboxPwd);
		
		// Number pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "fraNumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setEnterSubmitId(m_otxtboxId);
		m_oFrameNumberPad.setEnterSubmitId(m_otxtboxPwd);
		m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
		m_oFrameLoginSection.attachChild(m_oFrameNumberPad);
		
		// Create OK Button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("login"));
		m_oButtonOK.addClickServerRequestSubmitElement(this);
		m_oFrameIdPwd.attachChild(m_oButtonOK);
		
		// Create Exit Button
		m_oButtonExit = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonExit, "btnExit");
		m_oButtonExit.setValue(AppGlobal.g_oLang.get()._("exit"));
		m_oFrameIdPwd.attachChild(m_oButtonExit);
		// Outlet Label Frame
		m_oLabelOutletTab = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOutletTab, "lblOutletTab");
		m_oFrameLoginSection.attachChild(m_oLabelOutletTab);
		
		// Create Time In/Out Button
		m_oButtonTimeInOut = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonTimeInOut, "btnTimeInOut");
		m_oButtonTimeInOut.setValue(AppGlobal.g_oLang.get()._("time_in_out"));
		m_oButtonTimeInOut.addClickServerRequestSubmitElement(this);
		m_oButtonTimeInOut.setCornerRadius("8,8,0,0");
		m_oFrameLoginSection.attachChild(m_oButtonTimeInOut);
		
		// Selected image
		m_oImageSelected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSelected, "ImgSelected");
		m_oImageSelected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_h.png");
		m_oImageSelected.setVisible(false);
		m_oImageSelected.allowClick(true);
		m_oImageSelected.setClickServerRequestBlockUI(false);
		m_oFrameIdPwd.attachChild(m_oImageSelected);
		
		// Deselected image
		m_oImageDeselected = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageDeselected, "ImgDeselected");
		m_oImageDeselected.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/checkbox_n.png");
		m_oImageDeselected.setVisible(false);
		m_oImageDeselected.allowClick(true);
		m_oImageDeselected.setClickServerRequestBlockUI(false);
		m_oFrameIdPwd.attachChild(m_oImageDeselected);
		
		// Hold ID Label
		m_oLabelHoldId = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelHoldId, "lblHoldId");
		m_oLabelHoldId.setValue(AppGlobal.g_oLang.get()._("hold_id"));
		m_oLabelHoldId.setVisible(false);
		m_oLabelHoldId.setEnabled(true);
		m_oLabelHoldId.allowClick(true);
		m_oLabelHoldId.setClickServerRequestBlockUI(false);
		m_oLabelHoldId.setLongClickServerRequestBlockUI(true);
		m_oFrameIdPwd.attachChild(m_oLabelHoldId);
		
		// Create Version Label
		m_oLabelVersion = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelVersion, "lblVersion");
		m_oLabelVersion.setValue("POS Version" + ":" + AppGlobal.g_sVersion + " , " + "Hero Platform Version" + ":" + AppGlobal.g_sHeroPlatformVersion);
		m_oFrameLoginSection.attachChild(m_oLabelVersion);
		
		// Smart Card button
		m_oButtonSmartCard = new VirtualUIFrame();
		if(bSupportSmartCardAsEmployeeCard){
			m_oTemplateBuilder.buildFrame(m_oButtonSmartCard, "btnSmartCard");
			m_oButtonSmartCard.allowClick(true);
			m_oButtonSmartCard.setClickServerRequestBlockUI(false);
			
			VirtualUIImage oImage = new VirtualUIImage();
			oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/ask_smart_card_button.png");
			oImage.setTop(40);
			oImage.setLeft(19);
			oImage.setWidth(62);
			oImage.setHeight(50);
			oImage.setVisible(true);
			m_oButtonSmartCard.attachChild(oImage);
			
			m_oFrameLoginSection.attachChild(m_oButtonSmartCard);
			
			m_otxtboxId.setWidth(m_otxtboxId.getWidth() - 110);
			m_otxtboxPwd.setWidth(m_otxtboxPwd.getWidth() - 110);
			
			addShowReadSmartCardScreenTimer();
			startShowReadSmartCardScreen();
		}
		
		if (AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0){
			addScreenSaverTimer();
			startScreenSaverTimer(true);
		}
	}
	
	// Create a timer to show read smart card screen
	private void addShowReadSmartCardScreenTimer(){
		this.addTimer("show_smart_card_screen", 0, false, "", false, false, null);
	}
	
	public void startShowReadSmartCardScreen(){
		this.controlTimer("show_smart_card_screen", true);
	}
	
	// Create update basket timer
	public void addScreenSaverTimer(){
		this.addTimer("screen_saver", AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() * 60 * 1000, true, "screen_saver", false, false, null);
	}
	
	// Start to update basket
	public void startScreenSaverTimer(boolean bStart){
		this.controlTimer("screen_saver", bStart);
	}
	
	public String getLoginId() {
		//return m_sId;
		return m_otxtboxId.getValue();
	}

	public void setLoginId(String sId) {
		m_otxtboxId.setValue(sId);
	}
	
	public String getLoginPassword() {
		//return m_sPassword;
		return m_otxtboxPwd.getValue();
	}
	
	public void setLoginPassword(String spwd) {
		m_otxtboxPwd.setValue(spwd);
	}
	
	public void setMessage(String sMessage) {
		m_oLabelMsg.setValue(sMessage);
	}
	
	public void setVisibleScreenSaver() {
		if (AppGlobal.g_oFuncStation.get().getScreenSaverImage()) {
			m_oFrameScreenSaverImage.bringToTop();
			m_oFrameScreenSaverImage.setVisible(true);
		} else {
			m_oFrameScreenSaver.bringToTop();
			m_oFrameScreenSaver.setVisible(true);
		}
	}
	
	public void setOutletLogo(String sImageURL){
		if(sImageURL.length() > 0){
			// Create Logo
			m_oImageOutlet = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oImageOutlet, "imgLogo");
			m_oImageOutlet.setSource(sImageURL);
			m_oFrameIdPwd.attachChild(m_oImageOutlet);

			// Move down the label
			m_oLabelOutlet.setTop(m_oImageOutlet.getTop() + m_oImageOutlet.getHeight());
			m_oLabelOutlet.setHeight(m_iOutletNameAndLogoHeight - m_oLabelOutlet.getTop());
		}
	}
	
	public void setOutletDesc(String sOutletDesc){
		m_oLabelOutlet.setValue(sOutletDesc);
	}

	public void setFrameOutletDesc(String sOutletDesc){
		m_oLabelOutletTab.setValue(sOutletDesc);
	}

	public void setEnterBlockUI(boolean bBlockUI){
		m_oFrameNumberPad.setClickServerRequestBlockUI(bBlockUI);
	}
	
	public void setEnterDesc(String[] sDesc){
		m_oFrameNumberPad.setEnterDesc(sDesc);
	}
	
	// iShowType	0 - Not show
	//				1 - Show selected
	//				2 - Show deselected
	public void setSelected(int iShowType){
		switch(iShowType){
		case 1:
			m_oImageSelected.setVisible(true);
			m_oImageDeselected.setVisible(false);
			m_oLabelHoldId.setVisible(true);
			break;
		case 2:
			m_oImageSelected.setVisible(false);
			m_oImageDeselected.setVisible(true);
			m_oLabelHoldId.setVisible(true);
			break;
		default:
			m_oImageSelected.setVisible(false);
			m_oImageDeselected.setVisible(false);
			m_oLabelHoldId.setVisible(false);
			break;
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		setMessage("");
		m_otxtboxId.setFocus();
		
		if (AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0)
			startScreenSaverTimer(true);
		
		if (iChildId == m_oButtonOK.getId()) {       
			for (FrameLoginListener listener : listeners) {
				// Raise the event to parent
				listener.FrameLogin_clickOK();
			}

			bMatchChild = true;
		}
		else if (iChildId == m_oButtonExit.getId()) {
			for (FrameLoginListener listener : listeners) {
				// Raise the event to parent
				listener.FrameLogin_clickExit();
			}

			bMatchChild = true;
		}
		else if (iChildId == m_oLabelHoldId.getId() || iChildId == m_oImageSelected.getId() || iChildId == m_oImageDeselected.getId()) {
			for (FrameLoginListener listener : listeners) {
				// Raise the event to parent
				// Use the same event as click desc
				listener.FrameLogin_clickSelectBox();
			}
			bMatchChild = true;
		}
		else if (iChildId == m_oButtonSmartCard.getId()) {
			for (FrameLoginListener listener : listeners) {
				// Raise the event to parent
				listener.FrameLogin_clickShowSmartCard();
			}
			bMatchChild = true;
		}
		else if(iChildId == m_oButtonTimeInOut.getId()) {
			for (FrameLoginListener listener : listeners) {
				// Raise the event to parent
				listener.FrameLogin_clickTimeInOut();
			}

			bMatchChild = true;
		}
		else if(iChildId == m_oFrameScreenSaver.getId() || iChildId == m_oFrameScreenSaverImage.getId()) {
			m_oFrameScreenSaver.setVisible(false);
			m_oFrameScreenSaverImage.setVisible(false);
			startScreenSaverTimer(true);
			bMatchChild = true;
		}

		return bMatchChild;
	}

	@Override
	public boolean valueChanged(int iChildId, String sNote) {
		boolean bMatchChild = false;

		ClsActiveClient oActiveClient = AppGlobal.getActiveClient();
		if (oActiveClient != null) {
			if (iChildId == oActiveClient.getSwipeCardReaderElement().getId()
					&& oActiveClient.getSwipeCardReaderElement().getValueChangedServerRequestNote()
							.equals(FuncMSR.FRAME_SWIPE_CARD_DEFAULT)) {
				for (FrameLoginListener listener : listeners) {
					// Raise the event to parent
					listener.FrameLogin_swipeCard(oActiveClient.getSwipeCardReaderElement().getValue());
				}

				bMatchChild = true;
			}
		}

		return bMatchChild;
	}
	
	@Override
	public boolean timer(int iClientSockId, int iId, String sNote) {
		if(iId == super.getIDForPosting().hashCode()) {
			if(sNote.equals("screen_saver")){
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				startScreenSaverTimer(false);
				setVisibleScreenSaver();
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
				
				return true;
			}
			else {
				// Ask close form
				//Set the last client socket ID
				AppGlobal.g_oTerm.get().setClientSocketId(iClientSockId);
				
				for (FrameLoginListener listener : listeners) {
					// Raise the event to parent
					listener.FrameLogin_clickShowSmartCard();
				}
				
				// Send the UI packet to client and the thread is finished
				super.getParentForm().finishUI(true);
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void FrameNumberPad_clickEnter() {
		if (AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0)
			startScreenSaverTimer(true);
		
		setMessage("");
		
		if(m_otxtboxId.getValue().length() > 0){
			if(m_otxtboxPwd.getValue().length() > 0){
				for (FrameLoginListener listener : listeners) {
					// Raise the event to parent
					listener.FrameLogin_clickOK();
				}
				m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
				m_oFrameNumberPad.setEnterBlockUI(false);
			}else{
				m_otxtboxPwd.setFocus();
				m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("login", ""));
				m_oFrameNumberPad.setEnterBlockUI(true);
			}
		}else{
			m_otxtboxId.setFocus();
			m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
			m_oFrameNumberPad.setEnterBlockUI(false);
		}
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		if (AppGlobal.g_oFuncStation.get().getScreenSaverTimeout() > 0)
			startScreenSaverTimer(true);
		
		setMessage("");
		setLoginId("");
		setLoginPassword("");
		m_otxtboxId.setFocus();
		m_oFrameNumberPad.setEnterDesc(AppGlobal.g_oLang.get()._("next", ""));
		m_oFrameNumberPad.setEnterBlockUI(false);
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
	}
}