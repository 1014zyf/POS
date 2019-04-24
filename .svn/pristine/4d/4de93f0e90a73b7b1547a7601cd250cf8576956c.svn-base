package app.model;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosFunctionList {

	private HashMap<Integer, PosFunction> m_oFunctionList;
	
	public PosFunctionList(){
		m_oFunctionList = new HashMap<Integer, PosFunction>();
	}
	
	//read all function records
	public void readAll(int iUserId, List<Integer> oUserGroupList, int iOutletId, List<Integer> oOutletGroupList) {		
		PosFunction oFunctionList = new PosFunction();
		JSONArray responseJSONArray = oFunctionList.readAll(iUserId, oUserGroupList, iOutletId, oOutletGroupList);
		if (responseJSONArray != null) {
			for (int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oTempJSONObject = responseJSONArray.optJSONObject(i);
				if (oTempJSONObject == null)
					continue;
				PosFunction oPosFunction = new PosFunction(oTempJSONObject);
				
				// Add to function list
				m_oFunctionList.put(oPosFunction.getFuncId(), oPosFunction);
			}
		}
	}
	
	// Get function by function id
	public PosFunction getFunction(int iId){
		if(m_oFunctionList.containsKey(iId) == false)
			return null;
		
		return m_oFunctionList.get(iId);
	}
	
	// Get function by function key
	public int getFunctionIdByKey(String sFuncKey) {
		int iFuncId = 0;
		
		for(PosFunction oTempFunc:m_oFunctionList.values()) {
			if(oTempFunc.getKey().equals(sFuncKey)) {
				iFuncId = oTempFunc.getFuncId();
				break;
			}
		}
		
		return iFuncId;
	}
	
	// Get function object by function key
	public PosFunction getFunctionByKey(String sFuncKey) {
		PosFunction oFunction = null;
		
		for(PosFunction oTempFunc:m_oFunctionList.values()) {
			if(oTempFunc.getKey().equals(sFuncKey)) {
				oFunction = oTempFunc;				
				break;
			}
		}
		return oFunction;
	}
	
	public HashMap<Integer, PosFunction> getFunctionList() {
		return this.m_oFunctionList;
	}
}
