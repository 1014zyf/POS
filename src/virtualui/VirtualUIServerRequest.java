package virtualui;

import java.util.ArrayList;

import org.json.JSONArray;

public class VirtualUIServerRequest {
	
	private String m_sNote;
	private ArrayList<VirtualUIBasicElement> m_oSubmitElement;
	private boolean m_bBlockUI;
	private int m_iTimeout;
	
	// Constructor
	public VirtualUIServerRequest(){
	//	System.out.println("VirtualUIServerRequest: "+m_sNote);
		m_sNote = "";
		m_oSubmitElement = new ArrayList<VirtualUIBasicElement>();
		m_bBlockUI = true;
		m_iTimeout = 30000;
	}
	
	public void setServerRequestNote(String sNote){
		m_sNote = sNote;
	}
		
	public String getServerRequestNote(){
		return m_sNote;
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
	
	public void clearServerRequestSubmitId(){
		m_oSubmitElement.clear();
	}
	
	public void setServerRequestBlockUI(boolean bBlockUI){
		m_bBlockUI = bBlockUI;
	}
	
	public boolean getServerRequestBlockUI(){
		return m_bBlockUI;
	}
	
	public void setServerRequestTimeout(int iTimeout){
		m_iTimeout = iTimeout;
	}
	
	public int getServerRequestTimeout(){
		return m_iTimeout;
	}
}
