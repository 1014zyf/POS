package app;

import java.util.ArrayList;

import org.bouncycastle.util.Arrays;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import om.PrtPrintQueue;

public class ClsAlertMessage {
	class alertMsgInfo{
		int iSubjectId;
		String [] oSubjectNameArray;
		String sSubjectType;
		String sAction;
		String sAdditionalInfo;
		String [] oObjectNameArray;
	}
	
	private String sModule;
	private String sKey;
	private Boolean bShow;
	private DateTime oMessageArrivalTime;
	private ArrayList <Integer> oStationList;
	
	private alertMsgInfo oAlertMessage;

	// Alert message module type
	public final static String MODULE_PRINTING = "printing";
	
	// Alert message message key type
	public final static String MESSAGE_KEY_PRINT_QUEUE_STATUS = "print_queue_status";
	
	// Alert message status type (for key type - print_queue_status)
	public final static String PRINT_QUEUE_STATUS_PAPER_OUT = "e";
	public final static String PRINT_QUEUE_STATUS_PAPER_NEAR_END = "p";
	public final static String PRINT_QUEUE_STATUS_OFFLINE = "o";
	public final static String PRINT_QUEUE_STATUS_NORMAL = "n";
	
	// Alert message additional information
	public final static String ADDITIONAL_INFO_REDIRECT_TO = "redirect_to";
	
	public final static String UNREAD_MESSAGE_COLOR = "#DC143C";
	
	public ClsAlertMessage() {
		oMessageArrivalTime = null;
		bShow = false;
		sModule = "";
		sKey = "";
		oAlertMessage = new alertMsgInfo();
		oAlertMessage.oSubjectNameArray = null;
		oAlertMessage.sAction = "";
		oAlertMessage.oObjectNameArray = null;
		oAlertMessage.sAdditionalInfo = "";
		oAlertMessage.iSubjectId = 0;
		oAlertMessage.sSubjectType = "";
		oStationList = new ArrayList<Integer>();
	}
	
	public ClsAlertMessage(ClsAlertMessage oCurrentMessage) {
		this.bShow = oCurrentMessage.bShow;
		this.sKey = oCurrentMessage.sKey;
		this.sModule = oCurrentMessage.sModule;
		if (oCurrentMessage.oMessageArrivalTime != null) 
			this.oMessageArrivalTime = new DateTime(oCurrentMessage.oMessageArrivalTime);
		else
			this.oMessageArrivalTime = null;
		
		this.oAlertMessage = new alertMsgInfo();
		this.oAlertMessage.sAction = oCurrentMessage.oAlertMessage.sAction;
		this.oAlertMessage.sAdditionalInfo = oCurrentMessage.oAlertMessage.sAdditionalInfo;
		this.oAlertMessage.sSubjectType = oCurrentMessage.oAlertMessage.sSubjectType;
		
		if (oCurrentMessage.oAlertMessage.oSubjectNameArray != null && oCurrentMessage.oAlertMessage.oSubjectNameArray.length > 0) {
			this.oAlertMessage.oSubjectNameArray = new String[oCurrentMessage.oAlertMessage.oSubjectNameArray.length];
			for (int i = 0; i<oCurrentMessage.oAlertMessage.oSubjectNameArray.length; i++)
				this.oAlertMessage.oSubjectNameArray[i] = oCurrentMessage.oAlertMessage.oSubjectNameArray[i];
		}else
			this.oAlertMessage.oSubjectNameArray = null;
		
		if (oCurrentMessage.oAlertMessage.oObjectNameArray != null && oCurrentMessage.oAlertMessage.oObjectNameArray.length >0) {
			this.oAlertMessage.oObjectNameArray = new String[oCurrentMessage.oAlertMessage.oObjectNameArray.length];
			for (int i = 0; i<oCurrentMessage.oAlertMessage.oObjectNameArray.length; i++) 
				this.oAlertMessage.oObjectNameArray[i] = oCurrentMessage.oAlertMessage.oObjectNameArray[i];
		}else
			this.oAlertMessage.oObjectNameArray = null;
		
		if(oCurrentMessage.oStationList.size() > 0)
			this.oStationList = new ArrayList<Integer>(oCurrentMessage.oStationList);
	}
	
	public void readDataFromJSON(JSONObject oAlertMessageJSON) {
		if (oAlertMessageJSON.has("module"))
			this.sModule = oAlertMessageJSON.optString("module","");
		if (oAlertMessageJSON.has("key"))
			this.sKey = oAlertMessageJSON.optString("key","");
		if (oAlertMessageJSON.has("message")) {
			JSONObject oMessageJSON = oAlertMessageJSON.optJSONObject("message");
			if (oMessageJSON != null) {
				// handle print queue type message (key = print_queue_status)
				if(sKey.equals(MESSAGE_KEY_PRINT_QUEUE_STATUS)) {
					if (oMessageJSON.has("prtq_id")) {
						// get subject id
						int iSubPrintQueueId = oMessageJSON.optInt("prtq_id",0);
						if (iSubPrintQueueId == 0)
							return;
						this.oAlertMessage.iSubjectId = iSubPrintQueueId;
						// get subject type
						this.oAlertMessage.sSubjectType = AppGlobal.g_oLang.get()._("print_queue");
						// get message status
						if (!oMessageJSON.has("status"))
							return ;
						this.oAlertMessage.sAction = oMessageJSON.optString("status","");
						// get Subject name
						if (AppGlobal.g_oPrintQueueList.size() != 0 && AppGlobal.g_oPrintQueueList.containsKey(iSubPrintQueueId)) 
							this.oAlertMessage.oSubjectNameArray = AppGlobal.g_oPrintQueueList.get(iSubPrintQueueId).getName();
						else{
							PrtPrintQueue sSubjectPrintQue = new PrtPrintQueue();
							if(sSubjectPrintQue.readById(iSubPrintQueueId)) 
								this.oAlertMessage.oSubjectNameArray = sSubjectPrintQue.getName();
						}
						// get additional info 
						if (oMessageJSON.has("redirect")) {
							JSONObject oTempObjectJSON = oMessageJSON.optJSONObject("redirect");
							if (oTempObjectJSON != null) {
								oAlertMessage.sAdditionalInfo = ADDITIONAL_INFO_REDIRECT_TO;
								int iObjectPrintQueueId = oTempObjectJSON.optInt("prtq_id",0);
								// get object name
								if (iObjectPrintQueueId != 0) {
									if(AppGlobal.g_oPrintQueueList.size() != 0 && AppGlobal.g_oPrintQueueList.containsKey(iObjectPrintQueueId))
										this.oAlertMessage.oObjectNameArray = AppGlobal.g_oPrintQueueList.get(iObjectPrintQueueId).getName();
									else {
										PrtPrintQueue sObjectPrintQueue = new PrtPrintQueue();
										if(sObjectPrintQueue.readById(iObjectPrintQueueId))
											this.oAlertMessage.oObjectNameArray = sObjectPrintQueue.getName();
									}
								}
							}
						}
					}
					this.searchStationListByKeyAndId(sKey, this.oAlertMessage.iSubjectId);
				}
			}
		}
		this.oMessageArrivalTime = new DateTime();
	}
	
	public void searchStationListByKeyAndId(String sKeyType, int iSubjectId) {
		if(AppGlobal.g_oAlertMessageElementIdToStationIdsSettingTable.containsKey(sKeyType)) {
			if (AppGlobal.g_oAlertMessageElementIdToStationIdsSettingTable.get(sKeyType).containsKey(iSubjectId))
				this.addStationIdList(AppGlobal.g_oAlertMessageElementIdToStationIdsSettingTable.get(sKeyType).get(iSubjectId));
		}
	}
	
	public void addStationIdList(ArrayList<Integer> oStationList) {
		this.oStationList.addAll(oStationList);
	}
	
	public ArrayList<Integer> getStationList(){
		return this.oStationList;
	}
	
	public DateTime getMessageArrivalTime() {
		return oMessageArrivalTime;
	}
	
	public String getModule() {
		return sModule;
	}
	
	public String getKey() {
		return sKey;
	}
	
	public String getSubjectName(int iLangId) {
		if (oAlertMessage.oSubjectNameArray != null) {
			return oAlertMessage.oSubjectNameArray[iLangId-1];
		}
		return "";
	}
	
	public int getSubjectId() {
		return oAlertMessage.iSubjectId;
	}
	
	public String getSubjectType() {
		return oAlertMessage.sSubjectType;
	}
	
	public String getAction(){
		return oAlertMessage.sAction;
	}
	
	public String getObjectName(int iLangId) {
		if (oAlertMessage.oObjectNameArray != null) {
			return oAlertMessage.oObjectNameArray[iLangId-1];
		}
		return "";
	}
	
	public Boolean getVisible() {
		return bShow;
	}
	
	public void setVisible(Boolean bShow) {
		this.bShow = bShow;
	}
	
	public String getAdditionalInfo() {
		return oAlertMessage.sAdditionalInfo;
	} 
	
	@Override
	public String toString() {
		StringBuilder sString = new StringBuilder();
		sString.append("[");
		sString.append("sKey : "+ sKey + ", ");
		sString.append("sModule : "+ sModule + ", ");
		sString.append("oMessageArrivalTime : "+ oMessageArrivalTime + ", ");
		sString.append("sSubjectId : "+ getSubjectId() + ", ");
		sString.append("]");
		return sString.toString();
	}
}
