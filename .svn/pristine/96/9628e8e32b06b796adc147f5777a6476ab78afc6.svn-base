package app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosOutletItemList {
	private List<PosOutletItem> m_oOutletItemList;
	
	public PosOutletItemList() {
		this.m_oOutletItemList = new ArrayList<PosOutletItem>();
	}
	
	public void readOutletItemList(int iOutletId, String sStockControlLevel) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONArray resultJSONArray = oOutletItem.readAllByStockControlLevel(iOutletId, sStockControlLevel);
		if (resultJSONArray != null) {
			for (int i = 0; i < resultJSONArray.length(); i++) {
				if (resultJSONArray.isNull(i))
					continue;
				
				PosOutletItem oTempOutletItem = new PosOutletItem(resultJSONArray.optJSONObject(i));
				m_oOutletItemList.add(oTempOutletItem);
			}
		}
	}

	public void readOutletItemListBySoldout(int iOutletId, String sSoldout) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONArray resultJSONArray = oOutletItem.readAllBySoldout(iOutletId, sSoldout);
		if (resultJSONArray != null) {
			for (int i = 0; i < resultJSONArray.length(); i++) {
				if (resultJSONArray.isNull(i))
					continue;
				
				PosOutletItem oTempOutletItem = new PosOutletItem(resultJSONArray.optJSONObject(i));
				m_oOutletItemList.add(oTempOutletItem);
			}
		}
	}

	public void readOutletItemListByCheckStockSoldout(int iOutletId, String sCheckStock, String sSoldout) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONArray resultJSONArray = oOutletItem.readAllByCheckStockSoldout(iOutletId, sCheckStock, sSoldout);
		m_oOutletItemList.clear();
		if (resultJSONArray != null) {
			for (int i = 0; i < resultJSONArray.length(); i++) {
				if (resultJSONArray.isNull(i))
					continue;
				
				PosOutletItem oTempOutletItem = new PosOutletItem(resultJSONArray.optJSONObject(i));
				m_oOutletItemList.add(oTempOutletItem);
			}
		}
	}
	
	public void readOutletItemListByItemIds(int iOutletId, List<Integer> oItemIdList) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONArray resultJSONArray = oOutletItem.readAllByItemIds(iOutletId, oItemIdList);
		if (resultJSONArray != null) {
			for(int i = 0; i < resultJSONArray.length(); i++) {
				if (resultJSONArray.isNull(i))
					continue;
			
				PosOutletItem oTempOutletItem = new PosOutletItem(resultJSONArray.optJSONObject(i));
				m_oOutletItemList.add(oTempOutletItem);
			}
		}
	}
	
	public void updateMultipleStockQty(List<HashMap<String, String>> oUpdateStockInfos, boolean bSubtract, boolean bForceUpdate) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONObject resultJSONObject = null, oTempOutletItem = null;
		JSONArray oUpdatedOutletItem = null, oAvailabilities = null;
		HashMap<Integer, String> oItemAvailability = new HashMap<Integer, String>();
		
		resultJSONObject = oOutletItem.updateMultipleStockQty(oUpdateStockInfos, bSubtract, bForceUpdate);
		if(resultJSONObject != null) {
			try {
				if(resultJSONObject.has("availability") && !resultJSONObject.isNull("availability")){
					oAvailabilities = resultJSONObject.getJSONArray("availability");
					for (int i = 0; i < oAvailabilities.length(); i++) 
						oItemAvailability.put(oAvailabilities.getJSONObject(i).getInt("oitmId"), oAvailabilities.getJSONObject(i).getString("availability"));
				}
								
				if(resultJSONObject.has("outletItems") && !resultJSONObject.isNull("outletItems")) {
					oUpdatedOutletItem = resultJSONObject.getJSONArray("outletItems");
					for(int i=0; i<oUpdatedOutletItem.length(); i++) {
						if(oUpdatedOutletItem.getJSONObject(i).has("PosOutletItem"))
							oTempOutletItem = oUpdatedOutletItem.getJSONObject(i).getJSONObject("PosOutletItem");
						else
							oTempOutletItem = oUpdatedOutletItem.getJSONObject(i);
						for(PosOutletItem oTargetOutletItem:m_oOutletItemList) {
							if(oTargetOutletItem.getOitmId() == oTempOutletItem.getInt("oitm_id")) {
								oTargetOutletItem.setStockControlLevel(oTempOutletItem.optString("oitm_stock_control_level", PosOutletItem.STOCK_CONTROL_LEVEL_NO));
								oTargetOutletItem.setStockQty(new BigDecimal(oTempOutletItem.optString("oitm_stock_qty", "0")));
								oTargetOutletItem.setCheckStock(oTempOutletItem.optString("oitm_check_stock", PosOutletItem.CHECK_STOCK_NO));
								oTargetOutletItem.setSoldout(oTempOutletItem.optString("oitm_soldout", PosOutletItem.SOLDOUT_NO));
								
								if(oItemAvailability.containsKey(Integer.valueOf(oTargetOutletItem.getOitmId()))) 
									oTargetOutletItem.setStockAvailability(oItemAvailability.get(Integer.valueOf(oTargetOutletItem.getOitmId())));
								break;
							}
						}
					}
				}
				
			}catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
	}

	public void saveMultipleRecordWithMenuAndItemIds(int iOutletId, List<Integer> oMenuIds, List<Integer> oItemIds, String sSoldout) {
		PosOutletItem oOutletItem = new PosOutletItem();
		JSONArray resultJSONArray = oOutletItem.addUpdateWithMultipleRecordWithMenuAndItemIds(iOutletId, oMenuIds, oItemIds, sSoldout);
		if (resultJSONArray != null) {
			for (int i = 0; i < resultJSONArray.length(); i++) {
				if (resultJSONArray.isNull(i))
					continue;
				
				PosOutletItem oTempOutletItem = new PosOutletItem(resultJSONArray.optJSONObject(i));
				m_oOutletItemList.add(oTempOutletItem);
			}
		}
	}

	public boolean deleteOutletItemListByItemIds(List<Integer> oItemIdList) {
		PosOutletItem oOutletItemList = new PosOutletItem();
		boolean bResponse;
		
		bResponse = oOutletItemList.deleteOutletItemListByIds(oItemIdList);
		
		return bResponse;
	}
	
	public PosOutletItem getOutletItemByItemId(int iItemId) {
		PosOutletItem resultOutletItem = null;
		
		for (int i = 0; i < m_oOutletItemList.size(); i++) {
			if (m_oOutletItemList.get(i).getItemId() == iItemId) {
				resultOutletItem = m_oOutletItemList.get(i);
				break;
			}
		}
		
		return resultOutletItem;
	}
	
	public void cleanupOutletItemList() {
		m_oOutletItemList.clear();
	}
	
	public void addOutletItemToOutletItemList(PosOutletItem oPosOutletItem) {
		m_oOutletItemList.add(oPosOutletItem);
	}
	
	public List<PosOutletItem> getOutletItemList() {
		return this.m_oOutletItemList;
	}
}
