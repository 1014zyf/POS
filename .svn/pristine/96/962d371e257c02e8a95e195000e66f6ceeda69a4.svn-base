package om;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosTipsTrackTransactionsList {
	boolean m_bLastUpdateSuccess;
	private BigDecimal m_iTipsInFromChecks;
	private BigDecimal m_iServiceChargeInFromChecks;
	private BigDecimal m_iDirectTipsIn;
	private BigDecimal m_iTipsTotalFromOthers;
	private BigDecimal m_iServiceChargeTotalFromOthers;
	private BigDecimal m_iDirectTipsTotalFromOthers;
	private HashMap<Integer, PosTipsTrackTransactions> m_oPosTipsTrackTransactionsList;
	
	public PosTipsTrackTransactionsList(){
		init();
	}
	
	public void init(){
		m_bLastUpdateSuccess = false;
		m_iTipsInFromChecks = BigDecimal.ZERO;
		m_iServiceChargeInFromChecks = BigDecimal.ZERO;
		m_iDirectTipsIn = BigDecimal.ZERO;
		m_iTipsTotalFromOthers = BigDecimal.ZERO;
		m_iServiceChargeTotalFromOthers = BigDecimal.ZERO;
		m_iDirectTipsTotalFromOthers = BigDecimal.ZERO;
		m_oPosTipsTrackTransactionsList = new HashMap<Integer, PosTipsTrackTransactions>();
	}
	
	public void readByTypeBdayUserShopOlet(String sBusinessDayId, int iUserId, int iShopId, int iOutletId) {
		PosTipsTrackTransactions oPosTipsTrackTransactionsList = new PosTipsTrackTransactions();
		JSONObject responseJSONObject = oPosTipsTrackTransactionsList.readByTypeBdayUserShopOlet(sBusinessDayId, iUserId, iShopId, iOutletId);
		JSONArray tempJSONArray = new JSONArray();
		
		this.init();
		
		if (responseJSONObject != null) {
			m_iTipsInFromChecks = new BigDecimal(responseJSONObject.optString("inTipsTotalFromCheck", "0.0"));
			m_iServiceChargeInFromChecks = new BigDecimal(responseJSONObject.optString("inServiceChargeTotalFromCheck", "0.0"));
			m_iDirectTipsIn = new BigDecimal(responseJSONObject.optString("directTipsIn", "0.0"));
			m_iTipsTotalFromOthers = new BigDecimal(responseJSONObject.optString("tipsTotalFromOthers", "0.0"));
			m_iServiceChargeTotalFromOthers = new BigDecimal(responseJSONObject.optString("serviceChargeTotalFromOthers", "0.0"));
			m_iDirectTipsTotalFromOthers = new BigDecimal(responseJSONObject.optString("directTipsTotalFromOthers", "0.0"));
			
			tempJSONArray = responseJSONObject.optJSONArray("tipsTrackTransactions");
			if(tempJSONArray != null) {
				for (int i = 0; i < tempJSONArray.length(); i++) {
					if (tempJSONArray.isNull(i))
						continue;
					PosTipsTrackTransactions oPosTipsTrackTransactions = new PosTipsTrackTransactions(tempJSONArray.optJSONObject(i));
					m_oPosTipsTrackTransactionsList.put(i, oPosTipsTrackTransactions);
				}
			}
		}
	}
	
	public void addUpdateWithMutlipleTransactions(ArrayList<PosTipsTrackTransactions> oPosMutliTipsTrackTransactions) {
		PosTipsTrackTransactions oPosTipsTrackTransactionArray = new PosTipsTrackTransactions();
		m_bLastUpdateSuccess = oPosTipsTrackTransactionArray.addUpdateWithMultipleTransactions(oPosMutliTipsTrackTransactions);
	}
	
	public boolean isLastUpdateSuccess() {
		return m_bLastUpdateSuccess;
	}
	
	public BigDecimal getTipsInFromChecks() {
		return m_iTipsInFromChecks;
	}
	
	public BigDecimal getServiceChargeInFromChecks() {
		return m_iServiceChargeInFromChecks;
	}
	
	public BigDecimal getDirectTipsIn() {
		return m_iDirectTipsIn;
	}
	
	public BigDecimal getTipsTotalFromOthers() {
		return m_iTipsTotalFromOthers;
	}
	
	public BigDecimal getServiceChargeTotalFromOthers() {
		return m_iServiceChargeTotalFromOthers;
	}
	
	public BigDecimal getDirectTipsTotalFromOthers() {
		return m_iDirectTipsTotalFromOthers;
	}
	
	public HashMap<Integer, PosTipsTrackTransactions> getPosTipsTrackTransactionsList() {
		return m_oPosTipsTrackTransactionsList;
	}
}
