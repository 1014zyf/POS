package app;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import virtualui.VirtualUIFrame;
import virtualui.VirtualUISwipeCardReader;

public class ClsActiveClient {
	
	private long m_lCurrentThreadId;
	private String m_sUDID;
	private int m_iCurrentOutletId;
	private int m_iCurrentStationId;
	private boolean m_bAutoStation;
	private boolean m_bPortalStation;
	private VirtualUISwipeCardReader m_oSwipeCardReader;
	private VirtualUIFrame m_oGlobalTimerFrame;
	private boolean m_bNeedUpdateSoldoutStatus;
	private boolean m_bNeedUpdateItemStockQtyStatus;
	
	private boolean m_bIsShowBlockedFrame;
	
	private static int CLIENT_DEFAULT_TIMEOUT_IN_MILLISECOND = 30000;
	private static int CLIENT_MAX_TIMEOUT_IN_MILLISECOND = 300000;
	
	private static String UPDATE_CLIENT_STATUS_TIMER = "update_client_status";
	private static int UPDATE_CLIENT_STATUS_INTERVAL = 3000;

	// Station Info Bar Alert Message Variable
	private long m_lLastAlertMessageSeqNo = -1;
	private List<Long> m_oStationAlertMessageIndexList;
	private long m_lLastReadAlertMessageNumber = -1;

	public ClsActiveClient(){
		m_lCurrentThreadId = Thread.currentThread().getId();
		m_sUDID = "";
		m_iCurrentOutletId = 0;
		m_iCurrentStationId = 0;
		m_bAutoStation = false;
		m_bPortalStation = false;
		// Default true to make the station update at least once
		m_bNeedUpdateSoldoutStatus = true;
		m_bNeedUpdateItemStockQtyStatus = true;
		
		m_oStationAlertMessageIndexList = Collections.synchronizedList(new ArrayList<Long>());
	}
	
	public long getCurrentThreadId(){
		return m_lCurrentThreadId;
	}
	
	public String getUDID(){
		return m_sUDID;
	}
	
	public void setUDID(String sUDID){
		m_sUDID = sUDID;
	}
	
	public int getCurrentOutletId(){
		return m_iCurrentOutletId;
	}
	
	public void setCurrentOutletId(int iOutletId){
		m_iCurrentOutletId = iOutletId;
	}
	
	public int getCurrentStationId(){
		return m_iCurrentStationId;
	}
	
	public void setCurrentStationId(int iStationId){
		m_iCurrentStationId = iStationId;
	}
	
	public boolean isAutoStation(){
		return m_bAutoStation;
	}
	
	public void setAutoStation(boolean bAutoStation){
		m_bAutoStation = bAutoStation;
	}
	
	public boolean isPortalStation(){
		return m_bPortalStation;
	}
	
	public void setPortalStation(boolean bPortalStation) {
		m_bPortalStation = bPortalStation;
	}
	
	public void assignGlobalElement() {
		// Set a element for swipe card / barcode
		m_oSwipeCardReader = new VirtualUISwipeCardReader();
		m_oSwipeCardReader.allowValueChanged(true);
		m_oSwipeCardReader.addValueChangedServerRequestSubmitElement(m_oSwipeCardReader);
		m_oSwipeCardReader.setValueChangedServerRequestTimeout(CLIENT_DEFAULT_TIMEOUT_IN_MILLISECOND);
		
		AppGlobal.g_oTerm.get().addGlobalElement(m_oSwipeCardReader);
		
		// Add a global timer
		m_oGlobalTimerFrame = new VirtualUIFrame();
		m_oGlobalTimerFrame.addTimer(UPDATE_CLIENT_STATUS_TIMER, UPDATE_CLIENT_STATUS_INTERVAL, true, UPDATE_CLIENT_STATUS_TIMER, false, true, null);
		m_oGlobalTimerFrame.controlTimer(UPDATE_CLIENT_STATUS_TIMER, true);
		AppGlobal.g_oTerm.get().addGlobalElement(m_oGlobalTimerFrame);
	}
	
	public void registerGlobalTimerRunnable(ClsGlobalUIRunnable oRunnable) {
		AppGlobal.g_oTerm.get().registerGlobalElementRunnable(m_oGlobalTimerFrame, oRunnable);
	}

	public VirtualUISwipeCardReader getSwipeCardReaderElement() {
		return m_oSwipeCardReader;
	}
	
	// Set client timeout to maximum for swipe card event
	public void setMaximumClientTimeoutForSwipeCardEvent() {
		m_oSwipeCardReader.setValueChangedServerRequestTimeout(CLIENT_MAX_TIMEOUT_IN_MILLISECOND);
	}
	
	// Resume client timeout to default for swipe card event
	public void resumeClientTimeoutForSwipeCardEvent() {
		m_oSwipeCardReader.setValueChangedServerRequestTimeout(CLIENT_DEFAULT_TIMEOUT_IN_MILLISECOND);
	}
	
	public int getMaximumClientTimeout() {
		return CLIENT_MAX_TIMEOUT_IN_MILLISECOND;
	}
	
	public void setNeedUpdateSoldoutStatus(boolean bNeedUpdate) {
		m_bNeedUpdateSoldoutStatus = bNeedUpdate;
	}
	
	public boolean getNeedUpdateSoldoutStatus() {
		return m_bNeedUpdateSoldoutStatus;
	}
	
	public void setNeedUpdateItemStockQtyStatus(boolean bNeedUpdate) {
		m_bNeedUpdateItemStockQtyStatus = bNeedUpdate;
	}
	
	public boolean getNeedUpdateItemStockQtyStatus() {
		return m_bNeedUpdateItemStockQtyStatus;
	}
	
	public long getLastAlertMessageReadSeqNum() {
		return this.m_lLastReadAlertMessageNumber;
	}

	public void setLastAlertMessageReadSeqNum(long lSeqNum) {
		this.m_lLastReadAlertMessageNumber = lSeqNum;
	}
	// Station Info Bar Timer Function
	public long getLastAlertMessageSeq() {
		return m_lLastAlertMessageSeqNo;
	}
	
	public void setLastAlertMessageSeq(long lSeqNum) {
		m_lLastAlertMessageSeqNo = lSeqNum;
	}
	
	public List<Long> getStationAlertMessageIndexList() {
		return m_oStationAlertMessageIndexList;
	}
	
	public ArrayList<Long> getNewStationAlertMessageIndexList() {
		synchronized (m_oStationAlertMessageIndexList) {
			return new ArrayList<Long>(m_oStationAlertMessageIndexList);
		}
	}
	
	// Close active client handling
	public void close() {
	}
	
	public void setIsShowBlockFrame(boolean bIsShowBlockedFrame) {
		this.m_bIsShowBlockedFrame = bIsShowBlockedFrame;
	}
	
	public boolean isShowBlockedFrame() {
		return this.m_bIsShowBlockedFrame;
	}
}
