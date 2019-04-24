package app;

import java.math.BigDecimal;

import org.json.JSONObject;

public class FuncBenefit {
	private String code;
	private String desc;
	private String type;
	private String conditionDesc;
	private String condition;
	private String discountCode;
	private BigDecimal value;
	private int valueType;
	private int bonusDebit;
	private String posCode;
	private int useCount;
	private int avaCount;
	private int maxCount;
	private String itemRef;
	
	public static final String TYPE_PERCENT_DISCOUNT = "PD";
	public static final String TYPE_ITEM_DISCOUNT = "ID";
	public static final String TYPE_ITEM_FREE = "IF";
	public static final String TYPE_ITEM_REBATE = "IR";
	public static final String TYPE_ITEM_PRICE = "IP";
	public static final String TYPE_CASH_REBATE = "CR";
	public static final String TYPE_CASH = "C";
	public static final String TYPE_ITEM = "I";
	public static final String TYPE_PERCENT = "P";
	
	public static final String CONDITION_NO_CONDITION = "N";
	public static final String CONDITION_FAIL = "F";
	public static final String CONDITION_PASS = "P";
	public static final String CONDITION_UNKNOWN = "U";
	
	public FuncBenefit(JSONObject oBenefitJSONObject) {
		readDataFromJson(oBenefitJSONObject);
	}
	
	private void readDataFromJson(JSONObject benefitJSONObject) {
		JSONObject resultBenefit = benefitJSONObject.optJSONObject("benefit");
		if(resultBenefit == null)
			resultBenefit = benefitJSONObject;
		
		init();
		code = resultBenefit.optString("benefitCode");
		desc = resultBenefit.optString("benefitDesc");
		type = resultBenefit.optString("benefitType");
		conditionDesc = resultBenefit.optString("conditionDesc");
		condition = resultBenefit.optString("condition");
		discountCode = resultBenefit.optString("discountCode");
		String sValue = resultBenefit.optString("value");
		if (!sValue.isEmpty())
			value = new BigDecimal(sValue);
		valueType = resultBenefit.optInt("valueType");
		bonusDebit = resultBenefit.optInt("bonusDebit");
		posCode = resultBenefit.optString("posCode");
		avaCount = resultBenefit.optInt("avaCount");
		useCount = resultBenefit.optInt("useCount");
		maxCount = resultBenefit.optInt("maxCount");
		itemRef = resultBenefit.optString("itemRef");
	}
	
	private void init() {
		code = "";
		desc = "";
		type = "";
		condition = "";
		discountCode = "";
		value = BigDecimal.ZERO;
		valueType = 0;
		conditionDesc = "";
		bonusDebit = 0;
		posCode = "";
		avaCount = 0;
		useCount = 0;
		maxCount = 0;
		itemRef = "0";
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public int getValueType() {
		return valueType;
	}
	
	public String getItemCode() {
		return posCode;
	}
	
	public String getDiscountCode() {
		return discountCode;
	}
	
	public int getBonusDebit() {
		return bonusDebit;
	}
	
	public int getUseCount() {
		return useCount;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public int getAvaCount() {
		return avaCount;
	}
	
	public String getBenefitType() {
		return type;
	}
	
	public String getItemRef() {
		return itemRef;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public boolean isPercentDiscount() {
		return type.equals(FuncBenefit.TYPE_PERCENT_DISCOUNT);
	}
	
	public boolean isCashRebate() {
		return type.equals(FuncBenefit.TYPE_CASH_REBATE);
	}

	public boolean isFreeItem() {
		return type.equals(FuncBenefit.TYPE_ITEM_FREE);
	}

	public boolean isItemDiscount() {
		return type.equals(FuncBenefit.TYPE_ITEM_DISCOUNT);
	}
	
	public boolean isFailCondition() {
		return condition.equals(FuncBenefit.CONDITION_FAIL);
	}
	
	public boolean isNoCondition() {
		return condition.equals(FuncBenefit.CONDITION_NO_CONDITION);
	}
}
