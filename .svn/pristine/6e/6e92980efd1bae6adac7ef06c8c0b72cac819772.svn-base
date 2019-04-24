package app;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import externallib.StringLib;
import om.PosUserDrawerOwnership;
import om.PosUserDrawerOwnershipList;
import om.UserUser;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
//JohnLiu 06112017 -- start
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
//JohnLiu 06112017 -- end

/** interface for the listeners/observers callback method */
interface FrameAssignDrawerOwnershipListener {
	void frameAssignDrawerOwnership_clickClose();
	void frameAssignDrawerOwnership_commonBasketClicked(List<PosUserDrawerOwnership> oPosUserDrawerOwnership, int iItemIndex, int iFieldIndex);
}
//JohnLiu 06112017 -- start
public class FrameAssignDrawerOwnership extends VirtualUIFrame implements FrameCommonBasketListener,FrameTitleHeaderListener{
//JohnLiu 06112017 -- end
	TemplateBuilder m_oTemplateBuilder;

	private FrameCommonBasket m_oDrawerListCommonBasket;
	private List<PosUserDrawerOwnership> m_oUserDrawerOwnershipListByOrder;
	private List<Integer> m_oDrawerList;
	private boolean m_obUpdateAction;
//JohnLiu 06112017 -- start
	private FrameTitleHeader m_oTitleHeader;
//JohnLiu 06112017 -- end
	private int m_iMaxDrawerOwnership;

	private VirtualUIButton m_oButtonCancel;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameAssignDrawerOwnershipListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameAssignDrawerOwnershipListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameAssignDrawerOwnershipListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameAssignDrawerOwnership (int iMaxDrawerOwnership) {
		m_oTemplateBuilder = new TemplateBuilder();
		m_obUpdateAction = false;
		listeners = new ArrayList<FrameAssignDrawerOwnershipListener>();
		m_iMaxDrawerOwnership = iMaxDrawerOwnership;
		
		m_oUserDrawerOwnershipListByOrder = new ArrayList<PosUserDrawerOwnership>(); 
		PosUserDrawerOwnership oTmpPosUserDrawerOwnership = new PosUserDrawerOwnership();
		m_oUserDrawerOwnershipListByOrder.add(oTmpPosUserDrawerOwnership);
		m_oUserDrawerOwnershipListByOrder.add(oTmpPosUserDrawerOwnership);
		
		//drawer user name list
		List<String> oDrawerUserNameList = new ArrayList<String>();
		oDrawerUserNameList.add("");
		oDrawerUserNameList.add("");
		
		//drawer list
		m_oDrawerList = new ArrayList<Integer>();
		m_oDrawerList.add(1);
		m_oDrawerList.add(2);
		
		PosUserDrawerOwnershipList oPosUserDrawerOwnershipList = new PosUserDrawerOwnershipList();
		oPosUserDrawerOwnershipList.findAllByConditions(AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncStation.get().getStationId(), 0);
		List<PosUserDrawerOwnership> oUserDrawerOwnershipList = oPosUserDrawerOwnershipList.getPosUserDrawerOwnership();
		
		//get the drawer ownership list order by drawer
		
		for(int i=0; i<oUserDrawerOwnershipList.size(); i++){
			if(oUserDrawerOwnershipList.get(i).getDrawer() == 1){
				m_oUserDrawerOwnershipListByOrder.set(0, oUserDrawerOwnershipList.get(i));
			}else if(oUserDrawerOwnershipList.get(i).getDrawer() == 2){
				m_oUserDrawerOwnershipListByOrder.set(1, oUserDrawerOwnershipList.get(i));
			}
		}
		
		
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraAssignDrawerOwnership.xml");
//JohnLiu 06112017 -- start		
        m_oTitleHeader = new FrameTitleHeader();
        m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
        m_oTitleHeader.init(true);
        m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("assign_drawer_ownership"));
        m_oTitleHeader.addListener(this);
        this.attachChild(m_oTitleHeader);
//JohnLiu 06112017 -- end		
		
		//Voucher list common basket
		m_oDrawerListCommonBasket = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oDrawerListCommonBasket, "fraDrawerListCommonBasket");
		m_oDrawerListCommonBasket.init();
		m_oDrawerListCommonBasket.addListener(this);
		this.attachChild(m_oDrawerListCommonBasket);
		
		UserUser oUser = new UserUser();
		for(int i=0 ;i<m_oUserDrawerOwnershipListByOrder.size(); i++){
			if(m_oUserDrawerOwnershipListByOrder.get(i).getDrawer() !=0 ){
				oUser.readByUserId(m_oUserDrawerOwnershipListByOrder.get(i).getUserId());
				oDrawerUserNameList.set(i, oUser.getFirstName(AppGlobal.g_oCurrentLangIndex.get())+" "+oUser.getLastName(AppGlobal.g_oCurrentLangIndex.get()));
			}
		}
		
		//Add the check list title
		ArrayList<Integer> iFieldWidth = new ArrayList<Integer>();
		ArrayList<String[]> sFieldValue = new ArrayList<String[]>();
		
  		List<Integer> oFieldsWidth = new ArrayList<Integer>();
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())) {
			oFieldsWidth.add(200);
			oFieldsWidth.add(258);
		}else {
			oFieldsWidth.add(366);
			oFieldsWidth.add(332);
		}
		
		//Add Drawer List Common Basket Header
		iFieldWidth.add(oFieldsWidth.get(0));
		sFieldValue.add(AppGlobal.g_oLang.get()._("drawer", ""));
		iFieldWidth.add(oFieldsWidth.get(1));
		sFieldValue.add(AppGlobal.g_oLang.get()._("user", ""));
		
		m_oDrawerListCommonBasket.addHeader(iFieldWidth, sFieldValue);
		m_oDrawerListCommonBasket.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
		
		//Add Drawer List Common Basket Details
		for(int i=0 ; i<2; i++){
			ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
			ArrayList<String> sFieldValues = new ArrayList<String>();
			ArrayList<String> sFieldAligns = new ArrayList<String>();
			ArrayList<String> sFieldTypes = new ArrayList<String>();
		
			iFieldWidths.add(oFieldsWidth.get(0));
			sFieldValues.add(m_oDrawerList.get(i).toString());
//JohnLiu 06112017 -- start
			sFieldAligns.add("");
//JohnLiu 06112017 -- end
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			
			iFieldWidths.add(oFieldsWidth.get(1));
			sFieldValues.add(oDrawerUserNameList.get(i));	
//JohnLiu 06112017 -- start
			sFieldAligns.add("");
//JohnLiu 06112017 -- end
			sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
			m_oDrawerListCommonBasket.addItem(0, i, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		}
	
		// Create Cancel Button
		m_oButtonCancel = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonCancel, "btnCancel");
		m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		this.attachChild(m_oButtonCancel);
		
	}

	public void updateDrawerOwnershipUserList(int iItemIndex, int iFieldIndex, int iUserId, String sUserName){
		m_oDrawerListCommonBasket.setFieldValue(0, iItemIndex, 1, sUserName);
		
		DateTimeFormatter oFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime oTakeTime = AppGlobal.getCurrentTime(false);
		if(m_obUpdateAction){
			//unassign ownership
			PosUserDrawerOwnership oPosUserDrawerOwnership = m_oUserDrawerOwnershipListByOrder.get(iItemIndex);
			oPosUserDrawerOwnership.deleteById(oPosUserDrawerOwnership.getUdrwId(), oPosUserDrawerOwnership.getDrawer());
			
			//reset the list by index
			PosUserDrawerOwnership oTmpPosUserDrawerOwnership = new PosUserDrawerOwnership();
			m_oUserDrawerOwnershipListByOrder.set(iItemIndex, oTmpPosUserDrawerOwnership);
			m_obUpdateAction = false;
			
		}else{
			//add new ownership
			PosUserDrawerOwnership oPosUserDrawerOwnership = new PosUserDrawerOwnership();
			oPosUserDrawerOwnership.setShopId(AppGlobal.g_oFuncOutlet.get().getShopId());
			oPosUserDrawerOwnership.setOletId(AppGlobal.g_oFuncOutlet.get().getOutletId());
			oPosUserDrawerOwnership.setTakeTime(oFormatter.print(AppGlobal.convertTimeToUTC(oTakeTime)));
			oPosUserDrawerOwnership.setTakeLocTime(oTakeTime);
			oPosUserDrawerOwnership.setUserId(iUserId);
			oPosUserDrawerOwnership.setStatId(AppGlobal.g_oFuncStation.get().getStationId());
			oPosUserDrawerOwnership.setDrawer(m_oDrawerList.get(iItemIndex));
			
			oPosUserDrawerOwnership.addUpdate(false);
			
			m_oUserDrawerOwnershipListByOrder.set(iItemIndex, oPosUserDrawerOwnership);
		}
	}	
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
//JohnLiu 06112017 -- start
		boolean bMatchChild = false;
		return bMatchChild;
//JohnLiu 06112017 -- end
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
		for(FrameAssignDrawerOwnershipListener listener : listeners)
			listener.frameAssignDrawerOwnership_commonBasketClicked(m_oUserDrawerOwnershipListByOrder, iItemIndex, iFieldIndex);
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
	
	public boolean isUpdateAction() {
		return m_obUpdateAction;
	}

	public void setUpdateAction(boolean m_obUpdateAction) {
		this.m_obUpdateAction = m_obUpdateAction;
	}
	
	public int getMaxDrawerOwnership() {
		return m_iMaxDrawerOwnership;
	}
//JohnLiu 06112017 -- start
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
		for (FrameAssignDrawerOwnershipListener listener : listeners){
	       	listener.frameAssignDrawerOwnership_clickClose();
		}
	}
//JohnLiu 06112017 -- end
}
