package virtualui;

import java.util.ArrayList;

import org.json.JSONArray;

public class VirtualUITimer {
	private String m_sId;
	private int m_iInterval;
	private boolean m_bRepeat;
	private boolean m_bEnabled;
	
	private String m_sNote;
	private ArrayList<VirtualUIBasicElement> m_oSubmitElement;
	private boolean m_bBlockUI;
	private boolean m_bAsync;
	private int m_iClientTimeout;
	
	public VirtualUITimer(){
		m_sId = "";
		m_iInterval = 0;
		m_bRepeat = false;
		m_sNote = "";
		m_oSubmitElement = new ArrayList<VirtualUIBasicElement>();
		m_bBlockUI = true;		// Default block UI
		m_bEnabled = false;
		m_bAsync = false;
		m_iClientTimeout = 0;
	}
	
	public void setId(String sId){
		m_sId = sId;
	}
	
	public String getId(){
		return m_sId;
	}
	
	public void setInterval(int iInterval){
		m_iInterval = iInterval;
	}
	
	public Integer getInterval(){
		return m_iInterval;
	}
	
	public void setRepeat(boolean bRepeat){
		m_bRepeat = bRepeat;
	}
	
	public boolean isRepeat(){
		return m_bRepeat;
	}
	
	public void setEnable(boolean bEnable){
		m_bEnabled = bEnable;
	}
	
	public boolean isEnabled(){
		return m_bEnabled;
	}
	
	public void setServerRequestNote(String sNote){
		m_sNote = sNote;
	}
		
	public String getServerRequestNote(){
		return m_sNote;
	}
	
	public void setServerRequestBlockUI(boolean bBlockUI){
		m_bBlockUI = bBlockUI;
	}
	
	public boolean getServerRequestBlockUI(){
		return m_bBlockUI;
	}
	
	public void addServerRequestSubmitElement(VirtualUIBasicElement oElement){
		m_oSubmitElement.add(oElement);
	}
	
	public JSONArray getServerRequestSubmitId(){
		JSONArray oJSONArray = new JSONArray();
		
		for(VirtualUIBasicElement oElement : m_oSubmitElement)
			oJSONArray.put(oElement.getIDForPosting());
		
		return oJSONArray;
	}
	
	public void setAsync(boolean bAsync){
		m_bAsync = bAsync;
	}
	
	public boolean getAsync(){
		return m_bAsync;
	}
	
	public void setClientTimeout(int iClientTimeout) {
		m_iClientTimeout = iClientTimeout;
	}
	
	public int getClientTimeout() {
		return m_iClientTimeout;
	}
}
