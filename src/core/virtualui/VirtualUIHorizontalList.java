package core.virtualui;

import java.util.HashMap;

public class VirtualUIHorizontalList extends VirtualUIBasicElement {
	private HashMap<Integer, Object> m_oOptionMap;
	
	public VirtualUIHorizontalList(){
		super(HeroActionProtocol.View.Type.HORIZONTAL_LIST);
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
