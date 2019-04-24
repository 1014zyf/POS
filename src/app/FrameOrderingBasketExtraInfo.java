package app;

import java.util.ArrayList;

import org.json.JSONObject;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameOrderingBasketExtraInfoListener {
	void frameOrderingBasketExtraInfoPull_Clicked();
}

public class FrameOrderingBasketExtraInfo extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;
	private VirtualUIList m_oOrderingBasketExtraInfoListShrink;
	private VirtualUIList m_oOrderingBasketExtraInfoListExpand;
	private VirtualUIButton m_oExtraInfoPullButton;
	private VirtualUIImage m_oExtraInfoPullDownImg;
	private VirtualUIImage m_oExtraInfoPullUpImg;
	private VirtualUIFrame m_oExtraInfoBottomLineFrame;
	private ArrayList<JSONObject> m_oOrderingBasketExtraInfoContents;
	private int m_iRowSpacing;
	private int m_iListHeight;
	private int m_iListSize;
	
	private static int WORD_SPACING = 5;
	
	//Supporting key
	public static enum KEY_LIST{account_name, account_number, card_no, member_name, member_number, points_balance, total_points_balance};
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameOrderingBasketExtraInfoListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameOrderingBasketExtraInfoListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameOrderingBasketExtraInfoListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameOrderingBasketExtraInfo() {
		
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameOrderingBasketExtraInfoListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraOrderingBasketExtraInfo.xml");
		
	}
	
	public void updateList(ArrayList<JSONObject> oOrderingBasketExtraInfoContents) {
		m_oOrderingBasketExtraInfoContents = oOrderingBasketExtraInfoContents;
		m_oExtraInfoBottomLineFrame.bringToTop();
		editContent();
	}
	
	public void init(ArrayList<JSONObject> oOrderingBasketExtraInfoContents) {
		m_oOrderingBasketExtraInfoContents = oOrderingBasketExtraInfoContents;
		m_iListSize = m_oOrderingBasketExtraInfoContents.size();
		//init List with 1 row
		m_oExtraInfoPullButton = new VirtualUIButton();
		m_oOrderingBasketExtraInfoListShrink = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oOrderingBasketExtraInfoListShrink, "listOrderingBasketExtraInfo");
		m_oOrderingBasketExtraInfoListShrink.setWidth(this.getWidth());
		m_oOrderingBasketExtraInfoListShrink.setTop(0);
		//iRowSpacing Reflects the height of a row and controls the button height
		m_iRowSpacing = m_oOrderingBasketExtraInfoListShrink.getHeight();
		createRows(m_oOrderingBasketExtraInfoListShrink, 1);
		
		this.setHeight(m_iRowSpacing);
		this.attachChild(m_oOrderingBasketExtraInfoListShrink);
		
		//Pull button init
		if(m_iListSize > 1 ) {
			if(m_iListSize > 3)
				m_iListHeight = 4 * m_iRowSpacing;
			else 
				m_iListHeight = m_iListSize * m_iRowSpacing;
			//init List with >1 rows
			m_oOrderingBasketExtraInfoListExpand = new VirtualUIList();
			m_oTemplateBuilder.buildList(m_oOrderingBasketExtraInfoListExpand, "listOrderingBasketExtraInfo");
			m_oOrderingBasketExtraInfoListExpand.setHeight(m_iListHeight);
			m_oOrderingBasketExtraInfoListExpand.setWidth(this.getWidth());
			m_oOrderingBasketExtraInfoListExpand.setTop(0);
			createRows(m_oOrderingBasketExtraInfoListExpand, m_iListSize);
			
			m_oTemplateBuilder.buildButton(m_oExtraInfoPullButton, "btnExtraInfoTitlePull");
			m_oExtraInfoPullButton.setClickServerRequestBlockUI(false);
			m_oExtraInfoPullButton.setLeft(this.getWidth() - m_oExtraInfoPullButton.getWidth());
			m_oExtraInfoPullButton.setHeight(m_iRowSpacing);
			m_oExtraInfoPullButton.allowClick(true);
			
			m_oExtraInfoPullDownImg = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oExtraInfoPullDownImg, "imgExtraInfoTitlePull");
			int iButtonLeft = this.getWidth() - m_oExtraInfoPullButton.getWidth() / 2 - m_oExtraInfoPullDownImg.getWidth() / 2;
			m_oExtraInfoPullDownImg.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowdown.png");
			m_oExtraInfoPullDownImg.setLeft(iButtonLeft);
			this.attachChild(m_oExtraInfoPullDownImg);
			
			m_oExtraInfoPullUpImg = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(m_oExtraInfoPullUpImg, "imgExtraInfoTitlePull");
			m_oExtraInfoPullUpImg.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_nav_arrowup.png");
			m_oExtraInfoPullUpImg.setLeft(iButtonLeft);
			m_oExtraInfoPullUpImg.setVisible(false);
			this.attachChild(m_oExtraInfoPullUpImg);
			
			this.attachChild(m_oExtraInfoPullButton);
		}
		
		editContent();
		
		//Bottom Line init
		m_oExtraInfoBottomLineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oExtraInfoBottomLineFrame, "fraExtraInfoBottomLine");
		m_oExtraInfoBottomLineFrame.setWidth(this.getWidth());
		m_oExtraInfoBottomLineFrame.setTop(this.getTop() + this.getHeight() - 1);
		m_oExtraInfoBottomLineFrame.setVisible(true);
		this.attachChild(m_oExtraInfoBottomLineFrame);
		
	}
	
	private void editContent() {
		m_oOrderingBasketExtraInfoListShrink.getChilds().get(0).getChilds().get(1).setValue(m_oOrderingBasketExtraInfoContents.get(0).optJSONArray("value").optString(0));
		if(m_iListSize > 1 ) {
			for(int i = 0; i < m_iListSize; i++)
				m_oOrderingBasketExtraInfoListExpand.getChilds().get(i).getChilds().get(1).setValue(m_oOrderingBasketExtraInfoContents.get(i).optJSONArray("value").optString(0));
		}
	}
	
	private void createRows(VirtualUIList oList, int iSize) {
		for(int i = 0; i < iSize; i++) {
			VirtualUIFrame oExtraInfoContentRow = new VirtualUIFrame();
			VirtualUILabel oExtraInfoColKey = new VirtualUILabel();
			VirtualUILabel oExtraInfoColValue = new VirtualUILabel();
			
			m_oTemplateBuilder.buildFrame(oExtraInfoContentRow, "fraExtraInfoContentRow");
			oExtraInfoContentRow.setWidth(this.getWidth());
			oExtraInfoContentRow.setHeight(m_iRowSpacing);
			oExtraInfoContentRow.setVisible(true);
			oList.attachChild(oExtraInfoContentRow);
			
			//create col for key
			m_oTemplateBuilder.buildLabel(oExtraInfoColKey, "lblExtraInfoCols");
			oExtraInfoColKey.setWidth(145);
			oExtraInfoColKey.setHeight(m_iRowSpacing);
			oExtraInfoColKey.setLeft(FrameOrderingBasketExtraInfo.WORD_SPACING);
			oExtraInfoColKey.setValue(getKeyDescription(m_oOrderingBasketExtraInfoContents.get(i).optString("check_extra_info", "")));
			oExtraInfoContentRow.attachChild(oExtraInfoColKey);
			
			//create col for content
			m_oTemplateBuilder.buildLabel(oExtraInfoColValue, "lblExtraInfoCols");
			oExtraInfoColValue.setLeft(oExtraInfoColKey.getWidth() + FrameOrderingBasketExtraInfo.WORD_SPACING);
			oExtraInfoColValue.setWidth(this.getWidth() - oExtraInfoColKey.getWidth() - oExtraInfoColKey.getLeft() - FrameOrderingBasketExtraInfo.WORD_SPACING);
			oExtraInfoColValue.setHeight(m_iRowSpacing);
			oExtraInfoContentRow.attachChild(oExtraInfoColValue);
		}
	}
	
	private String[] getKeyDescription(String sKey) {
		String[] sKeyDesc;
		if(sKey.equals(KEY_LIST.account_name.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("account_name","");
		else if(sKey.equals(KEY_LIST.account_number.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("account_number", "");
		else if(sKey.equals(KEY_LIST.card_no.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("card_no", "");
		else if(sKey.equals(KEY_LIST.member_name.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("member_name", "");
		else if(sKey.equals(KEY_LIST.member_number.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("member_number", "");
		else if(sKey.equals(KEY_LIST.points_balance.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("points_balance", "");
		else if(sKey.equals(KEY_LIST.total_points_balance.name()))
			sKeyDesc = AppGlobal.g_oLang.get()._("total_points_balance", "");
		else
			sKeyDesc = new String[AppGlobal.LANGUAGE_COUNT];
		return sKeyDesc;
	}
	
	public void shrink() {
		if(m_oExtraInfoPullUpImg != null) {
			//shrink
			this.setHeight(m_iRowSpacing);
			m_oExtraInfoBottomLineFrame.setTop(this.getTop() + this.getHeight() - 1);
			this.removeChild(m_oOrderingBasketExtraInfoListExpand.getId());
			this.attachChild(m_oOrderingBasketExtraInfoListShrink);
			m_oExtraInfoPullUpImg.setVisible(false);
			m_oExtraInfoPullDownImg.setVisible(true);
			m_oExtraInfoPullDownImg.bringToTop();
			m_oExtraInfoPullButton.bringToTop();
		}
	}
	
	public int getListSize() {
		return m_iListSize;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		// Find the clicked button
		if (m_oExtraInfoPullButton.getId() == iChildId) {
			if(m_oExtraInfoPullDownImg.getVisible()) {
				//expand
				this.setHeight(m_iListHeight);
				m_oExtraInfoBottomLineFrame.setTop(this.getTop() + this.getHeight() - 1);
				this.removeChild(m_oOrderingBasketExtraInfoListShrink.getId());
				this.attachChild(m_oOrderingBasketExtraInfoListExpand);
				m_oExtraInfoPullDownImg.setVisible(false);
				m_oExtraInfoPullUpImg.setVisible(true);
				m_oExtraInfoPullUpImg.bringToTop();
			}else {
				//shrink
				this.setHeight(m_iRowSpacing);
				m_oExtraInfoBottomLineFrame.setTop(this.getTop() + this.getHeight() - 1);
				this.removeChild(m_oOrderingBasketExtraInfoListExpand.getId());
				this.attachChild(m_oOrderingBasketExtraInfoListShrink);
				m_oExtraInfoPullUpImg.setVisible(false);
				m_oExtraInfoPullDownImg.setVisible(true);
				m_oExtraInfoPullDownImg.bringToTop();
			}
			m_oExtraInfoPullButton.bringToTop();
			m_oExtraInfoBottomLineFrame.bringToTop();
			for (FrameOrderingBasketExtraInfoListener listener : listeners) 
				listener.frameOrderingBasketExtraInfoPull_Clicked();
			bMatchChild = true;
		}
		
		return bMatchChild;
	}
}
