package om;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class PosDiscountTypeList {
	private ArrayList<PosDiscountType> m_oDiscountTypeList;
	
	public PosDiscountTypeList() {
		m_oDiscountTypeList = new ArrayList<PosDiscountType>();
	}
	
	//read all discount for single item
	public boolean readDiscountListByItemDiscAndOutletId(int iItemDiscGrpId, int iShopId, int iOutletId, int iStationId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday, List<Integer> oUserGrpIds) {
		PosDiscountType oDiscountTypeList = new PosDiscountType(), oDiscountType = null;
		JSONArray oDiscounTypeJSONArray = oDiscountTypeList.readAllByItemDiscGrpAndOutletId(iItemDiscGrpId, iShopId, iOutletId, iStationId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday, oUserGrpIds);
		if (oDiscounTypeJSONArray != null) {
			for(int i = 0; i < oDiscounTypeJSONArray.length(); i++) {
				if (oDiscounTypeJSONArray.isNull(i))
					continue;
				
				oDiscountType = new PosDiscountType(oDiscounTypeJSONArray.optJSONObject(i));
				m_oDiscountTypeList.add(oDiscountType);
			}
		}
		
		return true;
	}
	
	//read all discount for multiple items
	public boolean readDiscountListByOutletId(String sDiscountType, int iShopId, int iOutletId, int iStationId, String sBusinessDay, boolean bIsHoliday, boolean bIsDayBeforeHoliday, boolean bIsSpecialDay, boolean bIsDayBeforeSpecialDay, int iWeekday, List<Integer> oUserGrpIds, boolean bShowAllWithAllowance) {
		PosDiscountType oDiscountTypeList = new PosDiscountType(), oDiscountType = null;
		JSONArray oDiscounTypeJSONArray = oDiscountTypeList.readAllByIOutletId(sDiscountType, iShopId, iOutletId, iStationId, sBusinessDay, bIsHoliday, bIsDayBeforeHoliday, bIsSpecialDay, bIsDayBeforeSpecialDay, iWeekday, oUserGrpIds, bShowAllWithAllowance);
		if (oDiscounTypeJSONArray != null) {
			for (int i = 0; i < oDiscounTypeJSONArray.length(); i++) {
				oDiscountType = new PosDiscountType(oDiscounTypeJSONArray.optJSONObject(i));
				m_oDiscountTypeList.add(oDiscountType);
			}
		}
		
		return true;
	}

	//read all discount for employee discount
	public boolean readDiscountListWithEmployeeDiscount() {
		PosDiscountType oDiscountTypeList = new PosDiscountType(), oDiscountType = null;
		JSONArray oDiscounTypeJSONArray = oDiscountTypeList.readAllEmployeeDiscount();
		if (oDiscounTypeJSONArray != null) {
			for (int i = 0; i < oDiscounTypeJSONArray.length(); i++) {
				oDiscountType = new PosDiscountType(oDiscounTypeJSONArray.optJSONObject(i));
				m_oDiscountTypeList.add(oDiscountType);
			}
		}
		
		return true;
	}
	
	public PosDiscountType getDiscountTypeByIndex(int iIndex){
		return this.m_oDiscountTypeList.get(iIndex);
	}
	
	public PosDiscountType getDiscountTypeByCode(String sDiscCode) {
		PosDiscountType oResultDiscountType = null;
		for(PosDiscountType oDiscountType: m_oDiscountTypeList) {
			if (oDiscountType.getCode().equals(sDiscCode)) {
				oResultDiscountType = oDiscountType;
				break;
			}
		}
		
		return oResultDiscountType;
	}
	
	public ArrayList<PosDiscountType> getPosDiscountTypeList() {
		return this.m_oDiscountTypeList;
	}
}
