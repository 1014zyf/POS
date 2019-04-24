package om;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class WohAwardSettingList {
	private ArrayList<WohAwardSetting> m_oWohAwardSettingList;
	private ArrayList<Integer> m_oEligibleEarningItemDepartmentIds;
	private ArrayList<Integer> m_oEligibleEarningExtraChargeIds;
	private ArrayList<Integer> m_oEligibleEarningServiceChargeIds;
	private ArrayList<Integer> m_bEligibleEarningGratuityIds;
	private ArrayList<Integer> m_oEligibleAwdItemDepartmentIds;
	private ArrayList<Integer> m_oEligibleAwdExtraChargeIds;
	private ArrayList<Integer> m_oEligibleAwdServiceChargeIds;
	private ArrayList<Integer> m_oEligibleAwdGratuityIds;
	private ArrayList<Integer> m_oIneligbileEarningPaymentMethodIds;
	
	public WohAwardSettingList() {
		m_oWohAwardSettingList = new ArrayList<WohAwardSetting>();
		m_oEligibleEarningItemDepartmentIds = new ArrayList<>();
		m_oEligibleEarningExtraChargeIds  = new ArrayList<>();
		m_oEligibleEarningServiceChargeIds = new ArrayList<>();
		m_bEligibleEarningGratuityIds = new ArrayList<>();
		m_oEligibleAwdItemDepartmentIds = new ArrayList<>();
		m_oEligibleAwdExtraChargeIds  = new ArrayList<>();
		m_oEligibleAwdServiceChargeIds = new ArrayList<>();
		m_oEligibleAwdGratuityIds = new ArrayList<>();
		m_oIneligbileEarningPaymentMethodIds = new ArrayList<>();
	}
	
	//read award setting 
	public boolean readAwardSettingListByShopOutlet(int iShopId, int iOutletId) {
		boolean bResult = true;
		WohAwardSetting oWohAwardSettingsList = new WohAwardSetting(), oWohAwardSettings = null;
		JSONArray oWohAwardSettingsJSONArray = new JSONArray();
		
		oWohAwardSettingsJSONArray = oWohAwardSettingsList.readAllByShopOutlet(iShopId, iOutletId);
		if (oWohAwardSettingsJSONArray != null) {
			for (int i = 0; i < oWohAwardSettingsJSONArray.length(); i++) {
				try {
						oWohAwardSettings = new WohAwardSetting(oWohAwardSettingsJSONArray.getJSONObject(i));
						m_oWohAwardSettingList.add(oWohAwardSettings);
						if(oWohAwardSettings.getEarningEligible().equals(oWohAwardSettings.EARNINGELIGIBLE_YES)) {
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_ITEM_DEPARTMENT))
								m_oEligibleEarningItemDepartmentIds.add(oWohAwardSettings.getItemDepartmentId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_EXTRA_CHANGE))
								m_oEligibleEarningExtraChargeIds.add(oWohAwardSettings.getDiscountId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_SERVICE_CHANGE))
								m_oEligibleEarningServiceChargeIds.add(oWohAwardSettings.getTaxScId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_GRATUITY))
								m_bEligibleEarningGratuityIds.add(oWohAwardSettings.getGratuityId());
						}
						if(oWohAwardSettings.getAwardPayable().equals(oWohAwardSettings.AWARDPAYABLE_YES)) {
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_ITEM_DEPARTMENT))
								m_oEligibleAwdItemDepartmentIds.add(oWohAwardSettings.getItemDepartmentId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_EXTRA_CHANGE))
								m_oEligibleAwdExtraChargeIds.add(oWohAwardSettings.getDiscountId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_SERVICE_CHANGE))
								m_oEligibleAwdServiceChargeIds.add(oWohAwardSettings.getTaxScId());
							if(oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_GRATUITY))
								m_oEligibleAwdGratuityIds.add(oWohAwardSettings.getGratuityId());
						}
						if(oWohAwardSettings.getEarningEligible().equals(oWohAwardSettings.EARNINGELIGIBLE_NO)
								&& oWohAwardSettings.getType().equals(WohAwardSetting.TYPE_PAYMENT_METHOD))
							m_oIneligbileEarningPaymentMethodIds.add(oWohAwardSettings.getPaymentMethodId());
				}catch(JSONException jsone) {
					jsone.printStackTrace();
					bResult = false;
				}
			}
		}
		return bResult;
	}
	
	public ArrayList<WohAwardSetting> getWohAwardSettingList() {
		return this.m_oWohAwardSettingList;
	}
	
	public ArrayList<Integer> getEligibleEarningItemDepartmentIds() {
		return this.m_oEligibleEarningItemDepartmentIds;
	}
	
	public ArrayList<Integer> getEligibleEarningExtraChargeIds() {
		return this.m_oEligibleEarningExtraChargeIds;
	}
	
	public ArrayList<Integer> getEligibleEarningServiceChargeIds() {
		return this.m_oEligibleEarningServiceChargeIds;
	}
	
	public ArrayList<Integer> getEligibleEarningGratuityIds() {
		return this.m_bEligibleEarningGratuityIds;
	}
	
	public ArrayList<Integer> getEligibleAwdItemDepartmentIds() {
		return this.m_oEligibleAwdItemDepartmentIds;
	}
	
	public ArrayList<Integer> getEligibleAwdExtraChargeIds() {
		return this.m_oEligibleAwdExtraChargeIds;
	}
	
	public ArrayList<Integer> getEligibleAwdServiceChargeIds() {
		return this.m_oEligibleAwdServiceChargeIds;
	}
	
	public ArrayList<Integer> getEligibleAwdGratuityIds() {
		return this.m_oEligibleAwdGratuityIds;
	}
	
	public ArrayList<Integer> getIneligibleEarningPaymentMethodIds() {
		return this.m_oIneligbileEarningPaymentMethodIds;
	}
}
