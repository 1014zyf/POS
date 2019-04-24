package app;

import java.util.ArrayList;

import externallib.StringLib;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIClockLabel;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameStationInfoBarListener {
    void frameStationInfoBar_ShowStationDetail();
    void frameStationInfoBar_SwitchOutlet();
    void frameStationInfoBar_ShowFunction();
    void frameStationInfoBar_ShowAskTableScreen();
    void frameStationInfoBar_LogoutClicked();
    void frameStationInfoBar_BackClicked();
}

public class FrameStationInfoBar extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIImage m_oImageChangeOutlet;
	private VirtualUILabel m_oLabelUserName;
	private VirtualUILabel m_oLabelOutletName;
	private VirtualUIClockLabel m_oClockLabelBuzDate;
	private VirtualUIFrame m_oFrameOutletStroke;
	private VirtualUIFrame m_oFrameAdminStroke;
	private VirtualUIFrame m_oFrameClockStroke;
	private VirtualUIFrame m_oFrameInfoStroke;
	private VirtualUIFrame m_oFrameInfos;
	private VirtualUIFrame m_oFrameBack;
	private VirtualUIImage m_oImageBack;
	private VirtualUILabel m_oLabelBack;
	private VirtualUIFrame m_oFrameOutlet;
	private VirtualUIFrame m_oFrameSearchTable;
	private VirtualUIImage m_oImageSearch;
	private VirtualUILabel m_oLabelSearchTable;
	private VirtualUIFrame m_oFrameAdmin;
	private VirtualUIImage m_oImageAdmin;
	private VirtualUILabel m_oLabelAdminMode;
	private VirtualUIFrame m_oFrameInfo;
	private VirtualUIImage m_oImageInfo;
	private VirtualUILabel m_oLabelInfo;
	private VirtualUIFrame m_oFrameExit;
	private VirtualUIImage m_oImageExit;
	private VirtualUILabel m_oLabelExit;
	
	private VirtualUILabel m_oLabelOperator;
	private boolean m_bShowAdminModeButtonOnly;		// Use for checking whether show admin mode only in kiosk station device (by PosConfig setup "display_admin_mode_only")

	private boolean m_bIsNewAlertMessageExist;

	public static String MODE_ADMIN = "admin";
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameStationInfoBarListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameStationInfoBarListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameStationInfoBarListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public FrameStationInfoBar(boolean bShowAdminModeButtonOnly) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameStationInfoBarListener>();
		m_bShowAdminModeButtonOnly = bShowAdminModeButtonOnly;
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraStationInfoBar.xml");
		
		// Outlet Name
		m_oFrameOutlet = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameOutlet, "frmOutlet");
		m_oFrameOutlet.allowClick(true);
		this.attachChild(m_oFrameOutlet);
		
		m_oImageChangeOutlet = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageChangeOutlet, "ImgChangeOutlet");
		m_oImageChangeOutlet.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/change_outlet_button.png");
		m_oImageChangeOutlet.allowClick(true);
		m_oFrameOutlet.attachChild(m_oImageChangeOutlet);
		
		m_oLabelOutletName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOutletName, "lblOutletName");
		m_oLabelOutletName.allowClick(true);
		m_oFrameOutlet.attachChild(m_oLabelOutletName);
		
		m_oFrameOutletStroke = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameOutletStroke, "frmOutletStroke");
		
		m_oFrameOutlet.attachChild(m_oFrameOutletStroke);
		
		// Back Session
		m_oFrameBack = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBack, "frmBack");
		m_oFrameBack.addClickServerRequestSubmitElement(this);
		m_oFrameBack.addClickServerRequestSubmitElement(this);
		m_oFrameBack.allowClick(true);
		this.attachChild(m_oFrameBack);
		
		m_oImageBack = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageBack, "ImgBack");
		m_oImageBack.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_back.png");
		m_oFrameBack.attachChild(m_oImageBack);
		
		m_oLabelBack = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelBack, "lblBack");
		m_oFrameBack.attachChild(m_oLabelBack);
		
		m_oFrameBack.setVisible(false);

		// Search table
		m_oFrameSearchTable = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameSearchTable, "frmSearhTable");
		m_oFrameSearchTable.allowClick(true);
		this.attachChild(m_oFrameSearchTable);
		
		m_oImageSearch = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageSearch, "ImgSearch");
		m_oImageSearch.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_search.png");
		m_oImageSearch.allowClick(true);
		m_oFrameSearchTable.attachChild(m_oImageSearch);
		
		m_oLabelSearchTable = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSearchTable, "lblSearchTable");
		m_oLabelSearchTable.allowClick(true);
		m_oLabelSearchTable.setValue(AppGlobal.g_oLang.get()._("search_table", ""));
		m_oFrameSearchTable.attachChild(m_oLabelSearchTable);
		
		//Information Frame
		m_oFrameInfos = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInfos, "frmInfos");
		this.attachChild(m_oFrameInfos);
		
		//Admin Mode
		m_oFrameAdmin = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameAdmin, "frmAdminMode");
		m_oFrameAdmin.allowClick(true);
		m_oFrameInfos.attachChild(m_oFrameAdmin);
		
		m_oImageAdmin = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageAdmin, "ImgAdmin");
		m_oImageAdmin.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_adminmode.png");
		m_oImageAdmin.allowClick(true);
		m_oFrameAdmin.attachChild(m_oImageAdmin);
		
		m_oLabelAdminMode = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelAdminMode, "lblAdminMode");
		m_oLabelAdminMode.allowClick(true);
		m_oLabelAdminMode.setValue(AppGlobal.g_oLang.get()._("admin", ""));
		m_oFrameAdmin.attachChild(m_oLabelAdminMode);
		
		m_oFrameAdminStroke = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameAdminStroke, "frmAdminStroke");
		m_oFrameAdmin.attachChild(m_oFrameAdminStroke);
		
		m_oLabelOperator = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelOperator, "lblOperator");
		m_oLabelOperator.setValue(AppGlobal.g_oLang.get()._("operator", ""));
		m_oFrameInfos.attachChild(m_oLabelOperator);
		
		// User Name
		m_oLabelUserName = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUserName, "lblUserName");
		m_oFrameInfos.attachChild(m_oLabelUserName);
		
		// Business Date
		m_oClockLabelBuzDate = new VirtualUIClockLabel();
		m_oTemplateBuilder.buildClockLabel(m_oClockLabelBuzDate, "lblBuzDate");
		m_oFrameInfos.attachChild(m_oClockLabelBuzDate);
		
		m_oFrameClockStroke = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameClockStroke, "frmBuzDateStroke");
		m_oFrameInfos.attachChild(m_oFrameClockStroke);

		// Station Info Name
		m_oFrameInfo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInfo, "frmInfo");
		m_oFrameInfo.allowClick(true);
		m_oFrameInfos.attachChild(m_oFrameInfo);
		
		m_oImageInfo = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageInfo, "ImgInfo");
		m_oImageInfo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_info.png");
		m_oImageInfo.allowClick(true);
		m_oFrameInfo.attachChild(m_oImageInfo);
		
		m_oLabelInfo = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelInfo, "lblInfo");
		m_oLabelInfo.allowClick(true);
		m_oLabelInfo.setValue(AppGlobal.g_oLang.get()._("info", ""));
		m_oFrameInfo.attachChild(m_oLabelInfo);
		
		m_oFrameInfoStroke = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameInfoStroke, "frmInfoStroke");
		m_oFrameInfo.attachChild(m_oFrameInfoStroke);

		// Exit Button
		m_oFrameExit = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameExit, "frmExit");
		m_oFrameExit.allowClick(true);
		m_oFrameInfos.attachChild(m_oFrameExit);
		
		m_oImageExit = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageExit, "ImgExit");
		m_oImageExit.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_logout.png");
		m_oImageExit.allowClick(true);
		m_oFrameExit.attachChild(m_oImageExit);
		
		m_oLabelExit = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelExit, "lblExit");
		m_oLabelExit.allowClick(true);
		m_oLabelExit.setValue(AppGlobal.g_oLang.get()._("exit", ""));
		m_oFrameExit.attachChild(m_oLabelExit);
		
		if(m_bShowAdminModeButtonOnly)
			this.hideButtons();

		m_bIsNewAlertMessageExist = false;
	}

	public void setUserName(String[] sUserName) {
		m_oLabelUserName.setValue(sUserName);
	}
	
	public void setOutletName(String[] sOutletName) {
		m_oLabelOutletName.setValue(sOutletName);
	}
	
	public void setBusinessDay(String[] sBusinessDay) {
		m_oClockLabelBuzDate.setValue(StringLib.appendStringArray("HH:mm '", sBusinessDay, "'"));
	}

	public void changeButtonLanguage(){
		m_oLabelAdminMode.setValue(AppGlobal.g_oLang.get()._("admin", ""));
	}
	
	public void showFunctionButton(boolean bVisible){
		if(!m_bShowAdminModeButtonOnly) {
			m_oFrameAdmin.setVisible(bVisible);
			m_oImageAdmin.setVisible(bVisible);
			m_oLabelAdminMode.setVisible(bVisible);
			
			if(bVisible == true) {
				m_oFrameAdmin.bringToTop();
				m_oImageAdmin.bringToTop();
				m_oLabelAdminMode.bringToTop();
			}
		}
	}
	
	// Display the back button
	public void displayMode(boolean bDisplay, String sModeName){
		if(!m_bShowAdminModeButtonOnly) {
			if(bDisplay){
				String sTitleName = "";
				if(sModeName.equals(FrameStationInfoBar.MODE_ADMIN)){
					sTitleName = AppGlobal.g_oLang.get()._("exit_admin_mode");
					m_oFrameSearchTable.setVisible(false);
				}
				else
					sTitleName = sModeName;
				m_oFrameAdmin.setVisible(true);
				m_oLabelBack.setValue(sTitleName);
				m_oFrameBack.setVisible(true);
				m_oFrameOutlet.setVisible(false);
			}
			else{
				m_oFrameAdmin.setVisible(true);
				m_oFrameBack.setVisible(false);
				m_oFrameOutlet.setVisible(true);
				if (m_oFrameInfos.getLeft() != 230)
					m_oFrameSearchTable.setVisible(true);
			}
		} else {
			if(bDisplay && sModeName.equals(FrameStationInfoBar.MODE_ADMIN))
				m_oFrameBack.setVisible(true);
			else
				m_oFrameBack.setVisible(false);
		}
	}
	
	public void changeInfosPosition(int iInforsLeft, int iBarWidth, boolean bSearch){
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			return;
		
		if(!m_bShowAdminModeButtonOnly)
			m_oFrameSearchTable.setVisible(bSearch);
		
		m_oFrameInfos.setLeft(iInforsLeft);
		this.setWidth(iBarWidth);
	}
	
	public void setSearchTableVisible(boolean bShow){
		if(!m_bShowAdminModeButtonOnly)
			m_oFrameSearchTable.setVisible(bShow);
	}
	
	public void setUserNameColor(String sColor) {
		m_oLabelUserName.setForegroundColor(sColor);
	}

	public boolean isAlertMessageIconShow() {
		return m_bIsNewAlertMessageExist;
	}
	
	private void setAlertImageInfo() {
		m_oImageInfo.setTop(m_oImageInfo.getTop() - 3);
		m_oImageInfo.setLeft(m_oImageInfo.getLeft() - 3);
		m_oImageInfo.setHeight(m_oImageInfo.getHeight() + 6);
		m_oImageInfo.setWidth(m_oImageInfo.getWidth() + 6);
			
		m_oImageInfo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/error_icon.png");
			
		m_bIsNewAlertMessageExist = true;
	}
	
	private void setOriginalImageInfo() {
		m_oImageInfo.setTop(m_oImageInfo.getTop() + 3);
		m_oImageInfo.setLeft(m_oImageInfo.getLeft() + 3);
		m_oImageInfo.setHeight(m_oImageInfo.getHeight() - 6);
		m_oImageInfo.setWidth(m_oImageInfo.getWidth() - 6);
		
		m_oImageInfo.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_info.png");
		
		m_bIsNewAlertMessageExist = false;
	}
	
	public synchronized void updateStationInfoIcon(boolean bIsAlertUpdate) {
		if (!bIsAlertUpdate) {
			if (isAlertMessageIconShow())
				setOriginalImageInfo();
		} else {
			if (!isAlertMessageIconShow())
				setAlertImageInfo();
		}
	}
	

	//show admin mode button only
	public void hideButtons() {
		m_oFrameBack.setVisible(false);
		m_oFrameOutlet.setVisible(false);
		m_oFrameSearchTable.setVisible(false);
		
		m_oLabelOperator.setVisible(false);
		m_oLabelUserName.setVisible(false);
		m_oClockLabelBuzDate.setVisible(false);
		
		m_oFrameInfo.setVisible(false);
		m_oImageInfo.setVisible(false);
		m_oLabelInfo.setVisible(false);
		
		m_oFrameExit.setVisible(false);
		
		m_oFrameAdminStroke.setVisible(false);
		m_oFrameClockStroke.setVisible(false);
		m_oFrameInfoStroke.setVisible(false);
		
		//set the admin mode button to the right
		m_oFrameAdmin.setLeft(m_oFrameInfos.getWidth() - m_oFrameAdmin.getWidth());
		m_oLabelBack.setValue( AppGlobal.g_oLang.get()._("exit_admin_mode") );
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oFrameInfo.getId() == iChildId || m_oImageInfo.getId() == iChildId || m_oLabelInfo.getId() == iChildId) {
			for (FrameStationInfoBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameStationInfoBar_ShowStationDetail();
			}

			// If Alert Icon is show, change back to original
			updateStationInfoIcon(false);

			bMatchChild = true;
		}else if (m_oFrameOutlet.getId() == iChildId || m_oImageChangeOutlet.getId() == iChildId || m_oLabelOutletName.getId() == iChildId) {
			if(m_oFrameAdmin.getVisible()){
				for (FrameStationInfoBarListener listener : listeners) {
					// Raise the event to parent
					listener.frameStationInfoBar_SwitchOutlet();
				}
			}
			bMatchChild = true;
		} else if (m_oFrameAdmin.getId() == iChildId || m_oImageAdmin.getId() == iChildId || m_oLabelAdminMode.getId() == iChildId) {
			// Function button
			for (FrameStationInfoBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameStationInfoBar_ShowFunction();
			}
			bMatchChild = true;
		} else if(m_oFrameSearchTable.getId() == iChildId || m_oImageSearch.getId() == iChildId || m_oLabelSearchTable.getId() == iChildId) {
			for (FrameStationInfoBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameStationInfoBar_ShowAskTableScreen();
			}
			bMatchChild = true;
		} else if(m_oFrameExit.getId() == iChildId || m_oImageExit.getId() == iChildId || m_oLabelExit.getId() == iChildId) {
			for (FrameStationInfoBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameStationInfoBar_LogoutClicked();
			}
			bMatchChild = true;
		} else if(m_oFrameBack.getId() == iChildId){
			// Back
			for (FrameStationInfoBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameStationInfoBar_BackClicked();
			}
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
