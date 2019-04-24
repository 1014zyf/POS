package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import om.OutFloorPlanTable;
import om.PosOutletTable;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameTableButtonListener {
	void frameTableFloorPlan_TableClicked(String sTable, String sTableExtension);
	void frameTableFloorPlan_TableLongClicked(String sTable, String sTableExtension);
}

public class FrameTableButton extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;

	VirtualUIFrame m_oFrameBackground;
	VirtualUIFrame m_oFrameContent;
	VirtualUIFrame m_oFrameCover;
	VirtualUILabel m_oLabelTable;
	VirtualUIImage m_oImageLockedIcon;
	VirtualUILabel m_oLabelTableExtensionCount;
	ArrayList <String> m_oDescKey;
	HashMap<String, VirtualUILabel> m_oDetailLabels;
	HashMap<String, VirtualUIImage> m_oDetailIcons;

	private boolean m_isPrinted;
	private Integer m_iShowDetailIndex;
	private String m_sCurrentKey;
	private boolean m_bForceIconVisible;
	private String[] m_sDefaultTableDetail;
	private boolean m_bShowFullDetail;

	public static final String STATUS_OPEN_TIME = "open_time";
	public static final String STATUS_COVER_NO = "cover_no";
	public static final String STATUS_CHECK_TOTAL = "check_total";
	public static final String STATUS_TABLE_SIZE = "table_size";
	public static final String STATUS_MEMBER_NUMBER = "member_number";
	public static final String STATUS_MEMBER_NAME = "member_name";
	public static final String STATUS_OWNER_NAME = "owner_name";
	public static final String STATUS_CHECK_INFO_ONE = "check_info_one";
	public static final String STATUS_CHECK_INFO_TWO = "check_info_two";
	public static final String STATUS_CHECK_INFO_THREE = "check_info_three";
	public static final String STATUS_CHECK_INFO_FOUR = "check_info_four";
	public static final String STATUS_CHECK_INFO_FIVE = "check_info_five";
	public static final String STATUS_EMPTY = "";
	
	private final int CONTENT_PADDING = 3;

	VirtualUILabel m_oCoverNumber;
	VirtualUILabel m_oOpenTime;
	VirtualUILabel m_oStatus;
	VirtualUIFrame m_oFrameHorizontalLine;
	VirtualUIImage m_oImageTableCoverIcon;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameTableButtonListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameTableButtonListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameTableButtonListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameTableButton() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTableButtonListener>();

		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraTableButton.xml");

		m_oDetailLabels = new HashMap<String, VirtualUILabel>();
		m_oDetailIcons = new HashMap<String, VirtualUIImage>();
		m_oDescKey = new ArrayList<String>();

		m_iShowDetailIndex = 0;
		m_bForceIconVisible = true;
		m_sDefaultTableDetail = new String[] {"", "", "", "", ""};

		m_oCoverNumber = new VirtualUILabel();
		m_oOpenTime = new VirtualUILabel();
		m_oStatus = new VirtualUILabel();
		m_oFrameHorizontalLine = new VirtualUIFrame();
		m_oImageTableCoverIcon = new VirtualUIImage();
	}
	
	public void setupTableButton(String[] sName, String sTableStyle, int iTable, String sTableExtension) {
		
		// Create background
		m_oFrameBackground = new VirtualUIFrame();
		m_oFrameBackground.setExist(true);
		m_oFrameBackground.setWidth(this.getWidth());
		m_oFrameBackground.setHeight(this.getHeight());
		if(sTableStyle.equals(OutFloorPlanTable.SHAPE_CIRCLE))
			m_oFrameBackground.setCornerRadius(Integer.toString(this.getHeight()/2));
		this.attachChild(m_oFrameBackground);
		
		// Get all elements from XML first
		// Content frame
		m_oFrameContent = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameContent, "fraContent");
		
		// Get the show area for the table button
		m_bShowFullDetail = true;
		if (this.getWidth() < m_oFrameContent.getWidth() || this.getHeight() < m_oFrameContent.getHeight()) {
			m_bShowFullDetail = false;
			
			m_oFrameContent.setWidth(this.getWidth());
			m_oFrameContent.setHeight(this.getHeight());
		} else {
			// Calculate the correct position of the content frame
			m_oFrameContent.setTop((this.getHeight() - m_oFrameContent.getHeight()) / 2);
			m_oFrameContent.setLeft((this.getWidth() - m_oFrameContent.getWidth()) / 2);
		}
		
		// Table no label
		m_oLabelTable = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTable, "lblTable");
		m_oLabelTable.setValue(sName);
		m_oFrameContent.attachChild(m_oLabelTable);
		
		// Lock Icon
		m_oImageLockedIcon = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImageLockedIcon, "imgLockedIcon");
		m_oImageLockedIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_lock.png");
		m_oImageLockedIcon.setVisible(false);
		m_oFrameContent.attachChild(m_oImageLockedIcon);
		
		// Table Extension Label
		m_oLabelTableExtensionCount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelTableExtensionCount, "lblTableExtensionCount");
		m_oLabelTableExtensionCount.setVisible(false);
		m_oFrameContent.attachChild(m_oLabelTableExtensionCount);
		
		// Cover Frame
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCover");
		m_oFrameContent.attachChild(m_oFrameCover);
		
		// Cover Icon
		m_oImageTableCoverIcon = new VirtualUIImage();
		if (m_bShowFullDetail) {
			m_oTemplateBuilder.buildImage(m_oImageTableCoverIcon, "imgCoverIcon");
			m_oImageTableCoverIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cover.png");
			m_oImageTableCoverIcon.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
			m_oFrameCover.attachChild(m_oImageTableCoverIcon);
		}
		
		// Cover label
		m_oCoverNumber = new VirtualUILabel();
		if (m_bShowFullDetail) {
			m_oTemplateBuilder.buildLabel(m_oCoverNumber, "lblCover");
			m_oCoverNumber.setValue("0");
			m_oCoverNumber.setTop(m_oImageTableCoverIcon.getTop() + ((m_oImageTableCoverIcon.getHeight() - m_oCoverNumber.getHeight()) / 2));
			m_oFrameCover.attachChild(m_oCoverNumber);
		}
		
		// Check open time label
		m_oOpenTime = new VirtualUILabel();
		if (m_bShowFullDetail) {
			m_oTemplateBuilder.buildLabel(m_oOpenTime, "lblOpenTime");
			m_oFrameContent.attachChild(m_oOpenTime);
		}
		
		// Table status label
		m_oStatus = new VirtualUILabel();
		if (m_bShowFullDetail) {
			m_oTemplateBuilder.buildLabel(m_oStatus, "lblTableStatus");
			m_oStatus.setValue(AppGlobal.g_oLang.get()._("vacant", ""));
			m_oFrameContent.attachChild(m_oStatus);
		}
		
		// Horizontal line with shadow
		m_oFrameHorizontalLine = new VirtualUIFrame();
		if (m_bShowFullDetail) {
			m_oTemplateBuilder.buildFrame(m_oFrameHorizontalLine, "fraHorizontalLine");
			m_oFrameContent.attachChild(m_oFrameHorizontalLine);
		}
		
		if (!m_bShowFullDetail) {
			// Cannot show all message
			m_oImageLockedIcon.setTop(CONTENT_PADDING);
			m_oImageLockedIcon.setLeft(m_oFrameContent.getWidth() - m_oImageLockedIcon.getWidth() - CONTENT_PADDING);
			m_oLabelTableExtensionCount.setTop(m_oImageLockedIcon.getTop() + m_oImageLockedIcon.getHeight());
			m_oLabelTableExtensionCount.setLeft(m_oFrameContent.getWidth() - m_oLabelTableExtensionCount.getWidth() - CONTENT_PADDING);
			
			m_oLabelTable.setTop(0);
			m_oLabelTable.setLeft(CONTENT_PADDING);
			m_oLabelTable.setWidth(m_oImageLockedIcon.getLeft() - CONTENT_PADDING);
			m_oLabelTable.setTextStyle(HeroActionProtocol.View.Attribute.TextStyle.AUTO_SIZE);
			m_oLabelTable.setTextAlign(HeroActionProtocol.View.Attribute.TextAlign.CENTER);
		}
		
		this.attachChild(m_oFrameContent);
	}
	
	public void addTableDetailType(String sKey, String sIconURL, boolean bIsUpdateDetail) {
		boolean bFound = false;
		for (String sCurrentKey:m_oDescKey) {
			if (sCurrentKey.equals(sKey)) {
				bFound = true;
				break;
			}
		}

		if (!bFound || bIsUpdateDetail) {
			// Table Detail label
			VirtualUILabel oLabelTableDetail = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabelTableDetail, "lblTableDetail");
			m_oFrameContent.attachChild(oLabelTableDetail);
			
			// Table Detail Icon
			VirtualUIImage oImageTableDetailIcon = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(oImageTableDetailIcon, "imgTableDetailIcon");
			oImageTableDetailIcon.setSource(sIconURL);
			m_oFrameContent.attachChild(oImageTableDetailIcon);
			
			if (!m_bShowFullDetail) {
				// Cannot show all message
				oLabelTableDetail.setLeft(CONTENT_PADDING);
				oLabelTableDetail.setTop(m_oFrameContent.getHeight() - CONTENT_PADDING - oLabelTableDetail.getHeight());
				oLabelTableDetail.setWidth(m_oFrameContent.getWidth() - CONTENT_PADDING - CONTENT_PADDING);
				
				oImageTableDetailIcon.setLeft(CONTENT_PADDING);
				oImageTableDetailIcon.setTop(m_oFrameContent.getHeight() - CONTENT_PADDING - oImageTableDetailIcon.getHeight());
				
				// Rearrange the table label height
				m_oLabelTable.setHeight(m_oFrameContent.getHeight() - CONTENT_PADDING - oLabelTableDetail.getHeight());
			}
			
			oLabelTableDetail.setVisible(false);
			oImageTableDetailIcon.setVisible(false);
			
			m_oDetailLabels.put(sKey, oLabelTableDetail);
			m_oDetailIcons.put(sKey, oImageTableDetailIcon);
			
			m_oDescKey.add(sKey);
		}
	}
	
	public void setTableDetail(String sKey, String[] sDetail){
		try{
			if(m_oDetailLabels.containsKey(sKey)){
				if (sDetail == null)
					sDetail = m_sDefaultTableDetail;

				if(sKey.equals(FrameTableButton.STATUS_OPEN_TIME)) {
					if(!sDetail[0].equals(""))
						m_oOpenTime.setValue(sDetail);
					else
						m_oOpenTime.setValue("");
				} else if(sKey.equals(FrameTableButton.STATUS_COVER_NO)) {
					if(!sDetail[0].equals(""))
						m_oCoverNumber.setValue(sDetail);
					else
						m_oCoverNumber.setValue("0");
				}

				m_oDetailLabels.get(sKey).setValue(sDetail);

				if(m_sCurrentKey == null)
					m_sCurrentKey = m_oDescKey.get(0);

				if(sKey.equals(m_sCurrentKey)){
					m_oDetailLabels.get(sKey).setVisible(true);
					if(sDetail[AppGlobal.g_oCurrentLangIndex.get()-1].length() > 0){
						if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize())
							m_oDetailIcons.get(sKey).setVisible(m_bForceIconVisible);
						else
							m_oDetailIcons.get(sKey).setVisible(true); 
					}else
						m_oDetailIcons.get(sKey).setVisible(false);
				}else{
					m_oDetailLabels.get(sKey).setVisible(false);
					m_oDetailIcons.get(sKey).setVisible(false);
				}
			}
		} catch (Exception e) {
			AppGlobal.stack2Log(e);
		}
	}
		
	boolean isOnlySkipInfo(){
		boolean bIsOnlySkipInfo = false;
		int iCountSkipInfo = 0;
		for(int i = 0; i < m_oDescKey.size(); i++){
			if(m_oDescKey.get(i).equals(this.STATUS_COVER_NO) || m_oDescKey.get(i).equals(this.STATUS_OPEN_TIME) || m_oDescKey.get(i).equals(this.STATUS_TABLE_SIZE))
				iCountSkipInfo++;
		}
		if(iCountSkipInfo == m_oDescKey.size())
			bIsOnlySkipInfo = true;
		return bIsOnlySkipInfo;
	}
	
	int getCorrectNextIndex(int iNextIndex){
		int iOriginalIndex = iNextIndex - 1;
		int iTraversalCounter = 0;
		boolean bFound = false;
		do{
			if(iNextIndex >= m_oDescKey.size() || iNextIndex < 0)
				iNextIndex = 0;
			if(m_oDescKey.get(iNextIndex).equals(STATUS_COVER_NO) || m_oDescKey.get(iNextIndex).equals(STATUS_OPEN_TIME)
					|| m_oDescKey.get(iNextIndex).equals(STATUS_TABLE_SIZE))
				iNextIndex++;
			else{
				bFound = true;
				break;
			}
			iTraversalCounter++;
		}while(iTraversalCounter < m_oDescKey.size());
		if(!bFound)
			return iOriginalIndex;
			
		return iNextIndex;
	}
	
	public void setTableDetailByIndex(int iIndex){
		if(m_oDetailLabels.isEmpty())
			return;
		
		String sKey;
		if(m_iShowDetailIndex >= 0){
			sKey = m_oDescKey.get(m_iShowDetailIndex);
			if(m_oDetailLabels.containsKey(sKey))
				m_oDetailLabels.get(sKey).setVisible(false);
			if(m_oDetailIcons.containsKey(sKey))
				m_oDetailIcons.get(sKey).setVisible(false);
		}

		m_iShowDetailIndex = iIndex;
		if(m_iShowDetailIndex >= m_oDescKey.size())
			m_iShowDetailIndex = 0;

		if(m_iShowDetailIndex >= 0){
			sKey = m_oDescKey.get(m_iShowDetailIndex);
			if(m_oDetailLabels.containsKey(sKey)){
				m_oDetailLabels.get(sKey).setVisible(true);
				if(m_oDetailIcons.containsKey(sKey) && m_oDetailLabels.get(sKey).getValue().length() > 0){
					if(AppGlobal.g_oFuncStation.get().isAllowShowTableSize())
						m_oDetailIcons.get(sKey).setVisible(m_bForceIconVisible);
					else
						m_oDetailIcons.get(sKey).setVisible(true);
				}
			}

			m_sCurrentKey = sKey;
		}
	}

	public Integer getCurrentTableDetailIndex(){
		return m_iShowDetailIndex;
	}
	
	public void setPrinted(boolean bPrinted){
		m_isPrinted = bPrinted;
	}

	public boolean isPrinted(){
		return m_isPrinted;
	}

	public void setLocked(boolean bLocked){
		m_oImageLockedIcon.setVisible(bLocked);
	}

	public boolean isLocked(){
		return m_oImageLockedIcon.getVisible();
	}

	public void setLabelForegroundColor(String sForegroundColor){
		for(Map.Entry<String, VirtualUILabel> entry:m_oDetailLabels.entrySet()){
			VirtualUILabel oLabel = entry.getValue();
			oLabel.setForegroundColor(sForegroundColor);
		}

		m_oLabelTable.setForegroundColor(sForegroundColor);
	}

	public String getLabelForegroundColor(){
		return m_oLabelTable.getForegroundColor();
	}

	public void setTableColor(String sBackgroundColor, String sTableStatus, String[] sStatusName){
		m_oFrameBackground.setBackgroundColor(sBackgroundColor);
		m_oStatus.setValue(sStatusName);
		
		if(sTableStatus.equals(PosOutletTable.STATUS_OCCUPIED) || sTableStatus.equals(PosOutletTable.STATUS_CHECK_PRINTED) || sTableStatus.equals(PosOutletTable.STATUS_COOKING_OVERTIME) || sTableStatus.equals(PosOutletTable.STATUS_CLEANING_TABLE) || sTableStatus.equals(PosOutletTable.STATUS_TABLE_RESERVATION)) {
			m_oCoverNumber.setForegroundColor("#ffffff");
			m_oImageTableCoverIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cover.png");
			m_oOpenTime.setForegroundColor("#ffffff");
			m_oStatus.setForegroundColor("#ffffff");
			m_oFrameHorizontalLine.setBackgroundColor(sBackgroundColor);
			m_oImageLockedIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_lock.png");
			m_oLabelTableExtensionCount.setForegroundColor("#ffffff");
			m_oLabelTableExtensionCount.setStrokeColor("#ffffff");
		}else {
			m_oCoverNumber.setForegroundColor("#9D9D9D");
			m_oImageTableCoverIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_main_cover_grey.png");
			m_oOpenTime.setForegroundColor("#9D9D9D");
			m_oStatus.setForegroundColor("#9D9D9D");
			m_oFrameHorizontalLine.setBackgroundColor(sBackgroundColor);
			m_oImageLockedIcon.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/icons/icon_list_lock.png");
			m_oLabelTableExtensionCount.setForegroundColor("#9D9D9D");
			m_oLabelTableExtensionCount.setStrokeColor("#9D9D9D");
		}
	}

	public String getTableColor(){
		return m_oFrameBackground.getBackgroundColor();
	}

	public void setTableExtensionCount(int iCount){
		if(iCount > 0){
			m_oLabelTableExtensionCount.setValue(iCount + "");

			m_oLabelTableExtensionCount.setVisible(true);
		}else{
			m_oLabelTableExtensionCount.setValue("");

			m_oLabelTableExtensionCount.setVisible(false);
		}
	}

	public int getTableExtensionCount(){
		try{
			return Integer.parseInt(m_oLabelTableExtensionCount.getValue());
		}catch(Exception e){
			return 0;
		}
	}

	public void setTableName(String[] sName){
		m_oLabelTable.setValue(sName);
	}

	public String getTableNames(){
		return m_oLabelTable.getValue();
	}
	public boolean getShowFUllDetail() {
		return m_bShowFullDetail;
	}

	// Force to set icon visible status for label value = table_size value
	public void setIconVisible(boolean bForceIconVisible){    	
		m_bForceIconVisible = bForceIconVisible;
	}

	public void setDefaultTableDetail(String[] sDefaultDetail) {
		m_sDefaultTableDetail = Arrays.copyOf(sDefaultDetail, sDefaultDetail.length);
	}
	
	public void setCurrentDetailByKey(String sKey) {
		if(sKey != null && m_oDescKey.contains(sKey)){
			m_sCurrentKey = sKey;
			m_iShowDetailIndex = m_oDescKey.indexOf(sKey);
		}
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		// Find the clicked button
		if(this.getId() == iChildId) {
			String splitedNote[] = sNote.split("_");
			if(splitedNote.length >= 0) {
				String sTable = splitedNote[0];
				String sTableExtension = "";
				if(splitedNote.length == 2)
					sTableExtension = splitedNote[1];

				for (FrameTableButtonListener listener : listeners) {
					// Raise the event to parent
					listener.frameTableFloorPlan_TableClicked(sTable, sTableExtension);
				}
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}

	@Override
	public boolean longClicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		// Find the clicked button
		if(this.getId() == iChildId) {
			String splitedNote[] = sNote.split("_");
			if(splitedNote.length >= 0) {
				String sTable = splitedNote[0];
				String sTableExtension = "";
				if(splitedNote.length == 2)
					sTableExtension = splitedNote[1];

				for (FrameTableButtonListener listener : listeners) {
					// Raise the event to parent
					listener.frameTableFloorPlan_TableLongClicked(sTable, sTableExtension);
				}
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
}
