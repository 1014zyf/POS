package om;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosCoverDiscountMapping {
	private int cvdmId;
	private int covdId;
	private int minCover;
	private int maxCover;
	private int dtypeId;
	
	public PosCoverDiscountMapping() {
		this.init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		this.setCvdmId(0);
		this.setCovdId(0);
		this.setMinCover(0);
		this.setMaxCover(0);
		this.setDtypeId(0);
	}

	public PosCoverDiscountMapping(JSONObject jsonObject) {
		readDataFromJson(jsonObject);
	}

	private void readDataFromJson(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		JSONObject resultCoverDiscountMappings = null;
		resultCoverDiscountMappings = jsonObject.optJSONObject("PosCoverDiscountMapping");
		if(resultCoverDiscountMappings == null)
			resultCoverDiscountMappings = jsonObject;
		
		this.init();
		this.setCvdmId(resultCoverDiscountMappings.optInt("cvdm_id"));
		this.setCovdId(resultCoverDiscountMappings.optInt("cvdm_covd_id"));
		this.setMinCover(resultCoverDiscountMappings.optInt("cvdm_min_cover"));
		this.setMaxCover(resultCoverDiscountMappings.optInt("cvdm_max_cover"));
		this.setDtypeId(resultCoverDiscountMappings.optInt("cvdm_dtyp_id"));
	}
	
	//read data from POS API
	private JSONArray readDataListFromApi(String sWsInterface, String sModule, String sFcnName, String sParam) {
		JSONArray coverDiscountMappingsJSONArray = null;
		
		if (OmWsClientGlobal.g_oWsClient.get().call(sWsInterface, sModule, sFcnName, sParam, false) == false) {
			return null;
		}else {
			try {
				if(OmWsClientGlobal.g_oWsClient.get().getResponse() == null)
					return null;
				
				if(!OmWsClientGlobal.g_oWsClient.get().getResponse().has("coverDiscountMappings")) {
					OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.g_oWsClient.get().getResponse().toString());
					return null;
				}
				
				if(OmWsClientGlobal.g_oWsClient.get().getResponse().isNull("coverDiscountMappings"))
					return null;
				
				coverDiscountMappingsJSONArray = OmWsClientGlobal.g_oWsClient.get().getResponse().getJSONArray("coverDiscountMappings");
				
			}catch(JSONException jsone) {
				OmWsClientGlobal.writeErrorLog(this.getClass().getSimpleName(), new Exception().getStackTrace()[0].getMethodName(), "", OmWsClientGlobal.stackToString(jsone));
				return null;
			}
		}
		
		return coverDiscountMappingsJSONArray;
	}
	
	public JSONArray readAllMappingsByCoverDiscountId(int iCoverDiscountId, int iCover) {
		JSONObject requestJSONObject = new JSONObject();
		JSONArray responseJSONArray = null;
		
		try {
			requestJSONObject.put("coverDiscountId", iCoverDiscountId);
			requestJSONObject.put("cover", iCover);
		}catch (JSONException jsone) {
			jsone.printStackTrace();
		}
		
		responseJSONArray = this.readDataListFromApi("gm", "pos", "getCoverDiscountMappingsByCoverDiscountIdAndCover", requestJSONObject.toString());
		return responseJSONArray;
	}

	/**
	 * @return the covdId
	 */
	public int getCovdId() {
		return covdId;
	}

	/**
	 * @param covdId the covdId to set
	 */
	public void setCovdId(int covdId) {
		this.covdId = covdId;
	}

	/**
	 * @return the cvdmId
	 */
	public int getCvdmId() {
		return cvdmId;
	}

	/**
	 * @param cvdmId the cvdmId to set
	 */
	public void setCvdmId(int cvdmId) {
		this.cvdmId = cvdmId;
	}

	/**
	 * @return the minCover
	 */
	public int getMinCover() {
		return minCover;
	}

	/**
	 * @param minCover the minCover to set
	 */
	public void setMinCover(int minCover) {
		this.minCover = minCover;
	}

	/**
	 * @return the maxCover
	 */
	public int getMaxCover() {
		return maxCover;
	}

	/**
	 * @param maxCover the maxCover to set
	 */
	public void setMaxCover(int maxCover) {
		this.maxCover = maxCover;
	}

	/**
	 * @return the dtypeId
	 */
	public int getDtypeId() {
		return dtypeId;
	}

	/**
	 * @param dtypeId the dtypeId to set
	 */
	public void setDtypeId(int dtypeId) {
		this.dtypeId = dtypeId;
	}
	
	public boolean checkCoverMapping(int iCover){
		if(this.minCover == 0 && this.maxCover == 0)
			return true;
		else if(this.minCover == 0 && iCover <= this.maxCover)
			return true;
		else if(this.maxCover == 0 && iCover >= this.minCover)
			return true;
		else if(iCover >= this.minCover && iCover <= this.maxCover)
			return true;
		return false;
	}
	
}
