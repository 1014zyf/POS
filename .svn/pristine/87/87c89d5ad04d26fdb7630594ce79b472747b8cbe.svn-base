package app;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import externallib.StringLib;
import om.SystemDataProcessLog;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;

interface FrameCheckDataUpdateHistoryListiner {
	void frameCheckDataUpdateHistory_finishFrame();
}

public class FrameCheckDataUpdateHistory extends VirtualUIFrame implements FrameTitleHeaderListener ,FrameCommonBasketListener{
	private FrameCommonBasket m_oFrameDataUpdateHistoryList;
	private VirtualUIButton m_oButtonConfirm;
	private ArrayList<FrameCheckDataUpdateHistoryListiner> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FrameCheckDataUpdateHistoryListiner listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameCheckDataUpdateHistoryListiner listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FrameCheckDataUpdateHistory() {
		TemplateBuilder oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckDataUpdateHistoryListiner>();
		oTemplateBuilder.loadTemplate("fraCheckDataUpdateHistory.xml");
		
		// header
		FrameTitleHeader oFrameTitleHeader = new FrameTitleHeader();
		oTemplateBuilder.buildFrame(oFrameTitleHeader, "fraTitleHeader");
		oFrameTitleHeader.init(false);
		oFrameTitleHeader.setUnderlineShow(true);
		oFrameTitleHeader.addListener(this);
		oFrameTitleHeader.setTitle(AppGlobal.g_oLang.get()._("data_update_history"));
		this.attachChild(oFrameTitleHeader);
		
		// confirm button
		m_oButtonConfirm = new VirtualUIButton();
		oTemplateBuilder.buildButton(m_oButtonConfirm, "btnFinish");
		m_oButtonConfirm.setValue(AppGlobal.g_oLang.get()._("confirm"));
		this.attachChild(m_oButtonConfirm);
		
		// common basket
		m_oFrameDataUpdateHistoryList = new FrameCommonBasket();
		oTemplateBuilder.buildFrame(m_oFrameDataUpdateHistoryList, "fraHistoryList");
		m_oFrameDataUpdateHistoryList.init();
		m_oFrameDataUpdateHistoryList.addListener(this);
		this.attachChild(m_oFrameDataUpdateHistoryList);
		
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)+10);
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)+10);
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)-20);
		
		sFieldValues.add(AppGlobal.g_oLang.get()._("start_time"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("end_time"));
		sFieldValues.add(AppGlobal.g_oLang.get()._("result"));
		
		m_oFrameDataUpdateHistoryList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameDataUpdateHistoryList.setHeaderFormat(40, 20, "");
		m_oFrameDataUpdateHistoryList.addSection(0, StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, ""), false);
	}
	
	public void init (ArrayList<SystemDataProcessLog> oDataProcessLogsJSONArrayList ) {
		DateTimeFormatter oFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		for (int i = 0 ; i < oDataProcessLogsJSONArrayList.size() ; i++) {
			SystemDataProcessLog oSystemDataProcessLog = oDataProcessLogsJSONArrayList.get(i);
			if (oSystemDataProcessLog == null)
				continue;
			DateTime oStartDay = oSystemDataProcessLog.getLogStartLoctime();
			DateTime oEndDay = oSystemDataProcessLog.getLogEndLoctime();
			String sStartDay = "";
			String sEndDay = "";
			String sResult = oSystemDataProcessLog.getLogResult();
			if (oStartDay != null)
				sStartDay = oStartDay.toString(oFmt);
			if (oEndDay != null)
				sEndDay = oEndDay.toString(oFmt);
			if (sResult == null)
				sResult = "";
			this.addRecord(0, i, sStartDay, sEndDay, sResult);
		}
	}

	public void addRecord(int iSectionId, int iItemIndex, String sStartTime, String sEndTime , String sResult){
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		ArrayList<String> sFieldAligns = new ArrayList<String>();
		ArrayList<String> sFieldTypes = new ArrayList<String>();
		
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)+10);
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)+10);
		iFieldWidths.add((m_oFrameDataUpdateHistoryList.getWidth()/3)-20);
		
		sFieldValues.add(sStartTime);
		sFieldValues.add(sEndTime);
		sFieldValues.add(sResult);
		
		sFieldAligns.add("");
		sFieldAligns.add("");
		sFieldAligns.add("");
		
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		
		ArrayList<VirtualUIBasicElement> oSubmitIdElements = new ArrayList<VirtualUIBasicElement>();
		m_oFrameDataUpdateHistoryList.addItem(iSectionId, iItemIndex, 0, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, oSubmitIdElements);
		for (int i = 0 ; i < 3 ; i++)
			m_oFrameDataUpdateHistoryList.setFieldTextSize(iSectionId, iItemIndex, i, 20);
		
	}
	
	/***********************/
	/*  Override Function  */
	/***********************/
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if (m_oButtonConfirm.getId() == iChildId) {
			for (FrameCheckDataUpdateHistoryListiner listener : listeners) {
				// Raise the event to parent
				listener.frameCheckDataUpdateHistory_finishFrame();
			}
			bMatchChild = true;
		}
		return bMatchChild;
	}
	
	@Override
	public void FrameTitleHeader_close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		// TODO Auto-generated method stub
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

}
