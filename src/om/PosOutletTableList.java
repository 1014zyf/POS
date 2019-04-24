package om;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOutletTableList {
	
	HashMap<PosOutletTable, PosCheck> m_oOutletTableList = new HashMap<PosOutletTable, PosCheck>();
	private String m_sLastGetRecordTime = "";
	
	//get active outlet table lists from database
	public boolean getActiveOutletTableList(int iOutletId) {
		boolean bResult = true;
		PosOutletTable oOutletTableList = new PosOutletTable();
		JSONArray responseJSONArray = oOutletTableList.getActiveOutletTableListByOutletId(iOutletId);
		
		if(oOutletTableList.getLastGetRecordTime() != null || !oOutletTableList.getLastGetRecordTime().isEmpty())
			m_sLastGetRecordTime =  oOutletTableList.getLastGetRecordTime();
		
		if(responseJSONArray == null)
			return false;
		
		for (int i = 0; i < responseJSONArray.length(); i++) {
			try {
				JSONObject outletTableJSONObject = responseJSONArray.getJSONObject(i).getJSONObject("PosOutletTable");
				PosCheck oCheck = null;
				if (responseJSONArray.getJSONObject(i).has("PosCheck"))
					oCheck = new PosCheck(responseJSONArray.getJSONObject(i));
				m_oOutletTableList.put(new PosOutletTable(outletTableJSONObject), oCheck);
			
			}catch (JSONException checkItemExcept) {
				checkItemExcept.printStackTrace();
				bResult = false;
				break;
			}
		}
		
		return bResult;
		
	}
	
	//get active outlet table lists with last modified time from database
	public boolean getActiveOutletTableListWithModified(int iOutletId, String sLastModifiedTime) {
		int i = 0;
		boolean bResult = true;
		PosOutletTable oOutletTableList = new PosOutletTable();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oOutletTableList.getActiveOutletTableListByOutletIdModifiedTime(iOutletId, sLastModifiedTime);
		
		if (oOutletTableList.getLastGetRecordTime() != null || !oOutletTableList.getLastGetRecordTime().isEmpty())
			m_sLastGetRecordTime =  oOutletTableList.getLastGetRecordTime();
		
		if(responseJSONArray == null)
			return false;
		
		for(i=0; i<responseJSONArray.length(); i++) {
			try {
				JSONObject outletTableJSONObject = responseJSONArray.getJSONObject(i).getJSONObject("PosOutletTable");
				PosCheck oCheck = null;
				if(responseJSONArray.getJSONObject(i).has("PosCheck")){
					oCheck = new PosCheck(responseJSONArray.getJSONObject(i));
				}
				m_oOutletTableList.put(new PosOutletTable(outletTableJSONObject), oCheck);
			
			}catch (JSONException checkItemExcept) {
				checkItemExcept.printStackTrace();
				bResult = false;
				break;
			}
		}
		
		return bResult;
		
	}
	
	//get active outlet table lists from database
	public boolean getActiveOutletTableListByTable(int iOutletId, int iTableNo) {
		int i = 0;
		boolean bResult = true;
		PosOutletTable oOutletTableList = new PosOutletTable();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oOutletTableList.getActiveOutletTableListByTableNo(iOutletId, iTableNo);
		if(responseJSONArray != null) {
			for(i=0; i<responseJSONArray.length(); i++) {
				try {
					JSONObject outletTableJSONObject = responseJSONArray.getJSONObject(i).getJSONObject("PosOutletTable");
					PosCheck oCheck = null;
					if(responseJSONArray.getJSONObject(i).has("PosCheck")){
						oCheck = new PosCheck(responseJSONArray.getJSONObject(i));
					}
					m_oOutletTableList.put(new PosOutletTable(outletTableJSONObject), oCheck);
				
				}catch (JSONException checkItemExcept) {
					checkItemExcept.printStackTrace();
					bResult = false;
					break;
				}
			}
		}
		
		return bResult;
		
	}
	
	//get cleaning status table with close check time from database
	public JSONArray getCleaningTableCloseCheckTimeByOutletId(int iOutletId, String sBusinessDayId) {
		PosOutletTable oOutletTableList = new PosOutletTable();
		JSONArray responseJSONObject = null;
		
		responseJSONObject = oOutletTableList.getCleaningTableCloseCheckTimeByOutletId(iOutletId, sBusinessDayId);
		
		return responseJSONObject;
		
	}
	
	public HashMap<PosOutletTable, PosCheck> getOutletTableList() {
		return this.m_oOutletTableList;
	}
	
	public String getLastGetRecordTime() {
		return this.m_sLastGetRecordTime;
	}
}
