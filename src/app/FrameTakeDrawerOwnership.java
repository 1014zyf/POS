package app;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.PosStation;
import om.PosUserDrawerOwnership;
import om.PosUserDrawerOwnershipList;
import om.UserUser;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameTakeDrawerOwnershipListener {
	void frameTakeDrawerOwnership_clickClose();
	void frameTakeDrawerOwnership_commonBasketFieldClicked(List<PosUserDrawerOwnership> oUserDrawerOwnershipList, int iItemIndex, int iFieldIndex, FuncUser oAuthorityFuncUser);
}

public class FrameTakeDrawerOwnership extends VirtualUIFrame implements FrameCommonBasketListener, FrameTitleHeaderListener {
	TemplateBuilder m_oTemplateBuilder;

	private FrameTitleHeader m_oFrameTitleHeader;
	private FrameCommonBasket m_oDrawerListCommonBasket;
	private VirtualUIFrame m_oFramePage;
	private VirtualUILabel m_oLblPage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private int m_iCurrentPageStartNo;
	public final int m_iPageRecordCount = 8;
	private int m_iScrollIndex = 1;
	
	private List<PosUserDrawerOwnership> m_oUserDrawerOwnershipList;
	private List<PosUserDrawerOwnership> m_oUserDrawerOwnershipListByOrder;
	private List<String> m_oDrawerUserName;
	private List<Integer> m_oDrawerList;
	
	private FuncUser m_oAuthorityFuncUser;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameTakeDrawerOwnershipListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameTakeDrawerOwnershipListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameTakeDrawerOwnershipListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameTakeDrawerOwnership(List<PosStation> oPosStationList, FuncUser oAuthorityUser) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTakeDrawerOwnershipListener>();
		m_oAuthorityFuncUser = oAuthorityUser;
		
		m_iCurrentPageStartNo = 0;
		
		m_oUserDrawerOwnershipListByOrder = new ArrayList<PosUserDrawerOwnership>(); 
		PosUserDrawerOwnership oTmpPosUserDrawerOwnership = new PosUserDrawerOwnership();
		m_oUserDrawerOwnershipListByOrder.add(oTmpPosUserDrawerOwnership);
		m_oUserDrawerOwnershipListByOrder.add(oTmpPosUserDrawerOwnership);
		
		m_oDrawerUserName = new ArrayList<String>();
		m_oDrawerUserName.add("");
		m_oDrawerUserName.add("");
		
		m_oDrawerList = new ArrayList<Integer>();
		m_oDrawerList.add(1);
		m_oDrawerList.add(2);
		
		PosUserDrawerOwnershipList oPosUserDrawerOwnershipList = new PosUserDrawerOwnershipList();
		oPosUserDrawerOwnershipList.findAllByOutletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
		m_oUserDrawerOwnershipList = oPosUserDrawerOwnershipList.getPosUserDrawerOwnership();
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTakeDrawerOwnership.xml");

		// Title Header
		m_oFrameTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oFrameTitleHeader, "fraTitleHeader");
		m_oFrameTitleHeader.init(true);
		m_oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("take_drawer_ownership"));
		m_oFrameTitleHeader.addListener(this);
		this.attachChild(m_oFrameTitleHeader);
		
		//Drawer list common basket
		m_oDrawerListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oDrawerListCommonBasket, "fraDrawerListCommonBasket");
		m_oDrawerListCommonBasket.init();
		m_oDrawerListCommonBasket.addListener(this);
		this.attachChild(m_oDrawerListCommonBasket);
		
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();
		
		//Fields Width
  		List<Integer> oFieldsWidth = new ArrayList<Integer>();
		oFieldsWidth.add(300);
		oFieldsWidth.add(128);
		oFieldsWidth.add(330);
		
		//Add Drawer List Common Basket Header
		iFieldWidth.add(oFieldsWidth.get(0));
		sFieldValue.add(AppGlobal.g_oLang.get()._("station_name", ""));
		iFieldWidth.add(oFieldsWidth.get(1));
		sFieldValue.add(AppGlobal.g_oLang.get()._("drawer", ""));
		iFieldWidth.add(oFieldsWidth.get(2));
		sFieldValue.add(AppGlobal.g_oLang.get()._("user", ""));
		
		m_oDrawerListCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oDrawerListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//Add Drawer List Common Basket Details
		for(int i=0 ; i<m_oUserDrawerOwnershipList.size(); i++){
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
			
			//get station name
			FuncStation oStation = new FuncStation();
			oStation.loadStationById(m_oUserDrawerOwnershipList.get(i).getStatId());
			
			iFieldWidths.add(oFieldsWidth.get(0));
			sFieldValues.add(oStation.getName(AppGlobal.g_oCurrentLangIndex.get()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(oFieldsWidth.get(1));
			sFieldValues.add(Integer.toString(m_oUserDrawerOwnershipList.get(i).getDrawer()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			//get user name
			UserUser oUser = new UserUser();
			oUser.readByUserId(m_oUserDrawerOwnershipList.get(i).getUserId());
			
			iFieldWidths.add(oFieldsWidth.get(2));
			sFieldValues.add(oUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			sFieldAligns.add(HeroActionProtocol.View.Attribute.TextAlign.CENTER_VERTICAL + "," + HeroActionProtocol.View.Attribute.TextAlign.LEFT);
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			m_oDrawerListCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		}
		
		
		//Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");
		
		VirtualUIImage oImagePage = new VirtualUIImage();
		oImagePage.setWidth(m_oFramePage.getWidth());
		oImagePage.setHeight(m_oFramePage.getHeight());
		oImagePage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_page_bg.png");
		m_oFramePage.attachChild(oImagePage);
		
    	m_oLblPage = new VirtualUILabel();
    	m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
    	m_oLblPage.setWidth(m_oFramePage.getWidth());
    	m_oLblPage.setHeight(m_oFramePage.getHeight()); 	
    	m_oFramePage.setVisible(true);
    	m_oFramePage.attachChild(m_oLblPage);
    	this.attachChild(m_oFramePage);
    	
		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.setVisible(true);
		this.attachChild(m_oImgButtonPrevPage);
		
		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.setVisible(true);
		this.attachChild(m_oImgButtonNextPage);
		
		updatePageUpDownVisibility();
		
	}

	public void updateDrawerOwnershipUserList(int iItemIndex, int iFieldIndex, int iUserId, String sUserName){
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oTakeTime = AppGlobal.getCurrentTime(false);
		
		if(iUserId != 0){
			//take ownership
			m_oDrawerListCommonBasket.setFieldValue(0, iItemIndex, 2, sUserName);
			PosUserDrawerOwnership oTmpPosUserDrawerOwnership = m_oUserDrawerOwnershipList.get(iItemIndex);
			m_oUserDrawerOwnershipList.get(iItemIndex).setTakeLocTime(oTakeTime);
			m_oUserDrawerOwnershipList.get(iItemIndex).setTakeTime(oFormatter.print(AppGlobal.convertTimeToUTC(oTakeTime)));
			m_oUserDrawerOwnershipList.get(iItemIndex).setUserId(iUserId);
			
			oTmpPosUserDrawerOwnership.addUpdate(true);
			
		}else{
			//unassign ownership
			m_oDrawerListCommonBasket.removeItem(0, iItemIndex);
			PosUserDrawerOwnership oTmpPosUserDrawerOwnership = m_oUserDrawerOwnershipList.get(iItemIndex);
			oTmpPosUserDrawerOwnership.deleteById(oTmpPosUserDrawerOwnership.getUdrwId(), oTmpPosUserDrawerOwnership.getDrawer());
			m_oUserDrawerOwnershipList.remove(iItemIndex);
		}
		updatePageUpDownVisibility();
	}
	
	public void updatePageUpDownVisibility() {
		boolean bShowPageUp = false;
		boolean bShowPageDown = false;
		int iPage = 0;
		int iCurrentPanelRecordCount = m_oDrawerListCommonBasket.getItemCount(0);
			if(iCurrentPanelRecordCount > m_iPageRecordCount)
				iPage = (m_iCurrentPageStartNo/m_iPageRecordCount)+1;
			
			if(m_iCurrentPageStartNo > 0)
				bShowPageUp = true;
			
			if(iCurrentPanelRecordCount > m_iPageRecordCount && m_iCurrentPageStartNo+m_iPageRecordCount < iCurrentPanelRecordCount)
				bShowPageDown = true;
			
		setPageNumber(iPage);
		showPageUp(bShowPageUp);
		showPageDown(bShowPageDown);
	}
	
	public void showPageUp(boolean bShow) {
		if(bShow)
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button.png");
		else
			m_oImgButtonPrevPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_prev_page_button_unclick.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
    }
    
    public void showPageDown(boolean bShow) {
		if(bShow)
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button.png");
		else
			m_oImgButtonNextPage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/set_menu_next_page_button_unclick.png");
		m_oImgButtonNextPage.setEnabled(bShow);
    }
    
    public void setPageNumber(int iNumber) {
    	int iTotalPage = 0;
    	
    	if(iNumber > 0) {
    		iTotalPage = (int)Math.ceil(1.0*m_oDrawerListCommonBasket.getItemCount(0)/m_iPageRecordCount);
		
    		m_oFramePage.setVisible(true);
    		m_oLblPage.setValue(iNumber+"/"+iTotalPage);
    		m_oLblPage.setVisible(true);
    		m_oImgButtonPrevPage.setVisible(true);
    		m_oImgButtonNextPage.setVisible(true);
    	} else {
    		m_oFramePage.setVisible(false);
    		m_oImgButtonPrevPage.setVisible(false);
    		m_oImgButtonNextPage.setVisible(false);
    	}
    	
    }
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oImgButtonPrevPage.getId()) {
			 // PAGE UP
			if(m_iCurrentPageStartNo-m_iPageRecordCount >= 0) {
				m_iCurrentPageStartNo -= m_iPageRecordCount;
				updatePageUpDownVisibility();
				m_iScrollIndex -= m_iPageRecordCount;
				
				m_oDrawerListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
			}
			
			bMatchChild = true;
		} else if (iChildId == m_oImgButtonNextPage.getId()) {
			 // PAGE DOWN
			if(m_iCurrentPageStartNo+m_iPageRecordCount < m_oDrawerListCommonBasket.getItemCount(0)) {
				m_iCurrentPageStartNo += m_iPageRecordCount;
				updatePageUpDownVisibility();
			}	
			m_iScrollIndex += m_iPageRecordCount;
			m_oDrawerListCommonBasket.moveScrollToItem(0, m_iScrollIndex);
			bMatchChild = true;
		}
		
		return bMatchChild;
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		for(FrameTakeDrawerOwnershipListener listener : listeners)
			listener.frameTakeDrawerOwnership_commonBasketFieldClicked(m_oUserDrawerOwnershipList, iItemIndex, iFieldIndex, m_oAuthorityFuncUser);
	}

	@Override
	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameTakeDrawerOwnershipListener listener : listeners) {
			// Raise the event to parent
			listener.frameTakeDrawerOwnership_clickClose();
		}
	}
}
