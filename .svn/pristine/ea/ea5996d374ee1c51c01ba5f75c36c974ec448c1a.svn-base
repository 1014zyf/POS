package core.virtualui;

import java.util.HashMap;

public class VirtualUIList extends VirtualUIBasicElement {
	private HashMap<Integer, Object> m_oOptionMap;
	
	public VirtualUIList(){
		super(HeroActionProtocol.View.Type.LIST);
    }
	
	public HashMap<Integer, Object> getList(){
		return m_oOptionMap;
	}
	
	public void setList(HashMap<Integer, Object> mList){
		m_oOptionMap = mList;
	}
	
	public void addToList(Object oObject){
		m_oOptionMap.put(m_oOptionMap.size()+1 , oObject);
	}
}
