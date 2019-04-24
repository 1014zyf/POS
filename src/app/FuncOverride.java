package app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.sql.Time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.MenuItem;
import om.MenuItemDeptGroupList;
import om.PosOverrideCondition;
import om.PosOverrideConditionList;

public class FuncOverride {
	private PosOverrideConditionList m_oOverrideConditionList;
	private HashMap<Integer, ArrayList<Integer>> m_oMenuItemDeptGroupAndDeptIdList;
	
	public FuncOverride() {
		m_oOverrideConditionList = new PosOverrideConditionList();	
		MenuItemDeptGroupList oMenuItemDeptGroupList = new MenuItemDeptGroupList();
		this.m_oMenuItemDeptGroupAndDeptIdList = oMenuItemDeptGroupList.readAndGetAllItemDeptGroupList();
	}
	
	public void readAllOverrideCondition(int iOutletId){
		m_oOverrideConditionList.readAllOverrideCondition(iOutletId);
		
		List<PosOverrideCondition> oOverrideConditionList = m_oOverrideConditionList.getOverrideConditionList();
		int iMaxPriority = 0;
		for (PosOverrideCondition oOverrideCondition: oOverrideConditionList) {
			if (oOverrideCondition.getPriority() > iMaxPriority)
				iMaxPriority = oOverrideCondition.getPriority();
		}
		
		// Add "Waive Service Charge For Takeout Item" override condition
		PosOverrideCondition oOverrideCondition2 = new PosOverrideCondition();
		for (int i = 1; i <= 5; i++)
			oOverrideCondition2.setName(i, "Waive Service Charge For Takeout Item");
		oOverrideCondition2.setPriority(++iMaxPriority);
		for (int i = 1; i <= 5; i++)
			oOverrideCondition2.setChargeSc(i, PosOverrideCondition.CHARGE_SC_WAIVE);
		oOverrideCondition2.setOrderingType(PosOverrideCondition.ORDERING_TYPE_TAKEOUT);
		oOverrideCondition2.setWeekMask("1111111");
		m_oOverrideConditionList.addOverrideCondition(oOverrideCondition2);
		
		// Add "Auto Waive Service Charge For Fast Food Mode" override condition
		if (!AppGlobal.g_oFuncStation.get().getFastFoodNotAutoWaiveSerCharge()) {
			PosOverrideCondition oOverrideCondition = new PosOverrideCondition();
			for (int i = 1; i <= 5; i++)
				oOverrideCondition.setName(i, "Auto Waive Service Charge For Fast Food Mode");
			oOverrideCondition.setPriority(++iMaxPriority);
			oOverrideCondition.setCheckOrderingMode(PosOverrideCondition.CHECK_ORDERING_MODE_FAST_FOOD);
			for (int i = 1; i <= 5; i++)
				oOverrideCondition.setChargeSc(i, PosOverrideCondition.CHARGE_SC_WAIVE);
			oOverrideCondition.setWeekMask("1111111");
			m_oOverrideConditionList.addOverrideCondition(oOverrideCondition);
		}
	}
	
	public void checkPriceOverrideForItem(FuncCheckItem oCheckItem, boolean bFastFoodOrderingMode, Time oCheckCreateTime, Time oOrderTime,
			int iTableNo, String sTableExt, int iSphrId, int iCtypId, List<Integer> oDtypeIdList) {
		int iOutletPeriodId = 0;
		boolean bNeedPriceOverride = false, bNeedScTaxOverride = false;
		List<PosOverrideCondition> oOverrideConditions = null;
		PosOverrideCondition oCurrentOverrideCondition = null;
		
		if(oCheckItem.isChangePriceLevelManually())
			return;
		
		MenuItem oMenuItem = oCheckItem.getMenuItem();
		if (oMenuItem == null)
			return;
		
		int iItemDepId = oCheckItem.getCheckItem().getDepartmentId();
		//get business period id
		if(oCheckItem.getCheckItem().getBusinessPeriodId().compareTo("") == 0)
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		else
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(oCheckItem.getCheckItem().getBusinessPeriodId());
		
		//get the highest priority price level override rule current price level
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForPriceLevel(oCheckItem.getCheckItem().getOriginalPriceLevel());
		if(!oOverrideConditions.isEmpty()) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions) {
				oCurrentOverrideCondition = oOverrideCondition;
				if(this.checkOverrideCondition(oCurrentOverrideCondition, oCheckCreateTime, oOrderTime, iTableNo, sTableExt, oCheckItem.isTakeoutItem(),
						bFastFoodOrderingMode, iOutletPeriodId, iSphrId, iCtypId, false, false, false, null, iItemDepId)) {
					// need to change price level	
					bNeedPriceOverride = true;
					int iTargetPriceLevel = oCurrentOverrideCondition.getToPriceLevel();
								
					//check need to do price level override
					if(oCheckItem.getCheckItem().getPriceLevel() != iTargetPriceLevel) {
						//no need to change price level override if item is open price item
						if(oCheckItem.isOpenPrice())
							return;
						
						oCheckItem.changePriceLevel(iTargetPriceLevel, true);
						
						//for child item
						if(oCheckItem.hasChildItem()) {
							for(FuncCheckItem oChildCheckItem:oCheckItem.getChildItemList()) {
								oChildCheckItem.changePriceLevel(iTargetPriceLevel, true);
								
								//for child modifier item
								if(oChildCheckItem.hasModifier()) {
									for(FuncCheckItem oChildModifierCheckItem:oChildCheckItem.getModifierList())
										oChildModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
								}
							}
						}
						
						//for modifier
						if(oCheckItem.hasModifier()) {
							for(FuncCheckItem oModifierCheckItem:oCheckItem.getModifierList())
								oModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
						}
					}
					break;
				}
			}
			
			if(!bNeedPriceOverride) {
				if(oCheckItem.getCheckItem().getOriginalPriceLevel() != oCheckItem.getCheckItem().getPriceLevel()) {
					// no override rule for item and citm_original_price != citm_price_level (price override rule applied at ordering time)
					// need to change back to original price level
					int iTargetPriceLevel = oCheckItem.getCheckItem().getOriginalPriceLevel();
					
					oCheckItem.changePriceLevel(iTargetPriceLevel, true);
					
					//for child item
					if(oCheckItem.hasChildItem()) {
						for(FuncCheckItem oChildCheckItem:oCheckItem.getChildItemList()) {
							oChildCheckItem.changePriceLevel(iTargetPriceLevel, true);
							
							//for child modifier item
							if(oChildCheckItem.hasModifier()) {
								for(FuncCheckItem oChildModifierCheckItem:oChildCheckItem.getModifierList())
									oChildModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
							}
						}
					}
					
					//for modifier
					if(oCheckItem.hasModifier()) {
						for(FuncCheckItem oModifierCheckItem:oCheckItem.getModifierList())
							oModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
					}
				}
			}
			
		}else {
			// no any override exist
			if(oCheckItem.getCheckItem().getOriginalPriceLevel() != oCheckItem.getCheckItem().getPriceLevel()) {
				// no override rule for item and citm_original_price != citm_price_level (price override rule applied at ordering time)
				// need to change back to original price level
				int iTargetPriceLevel = oCheckItem.getCheckItem().getOriginalPriceLevel();
				
				oCheckItem.changePriceLevel(iTargetPriceLevel, true);
				
				//for child item
				if(oCheckItem.hasChildItem()) {
					for(FuncCheckItem oChildCheckItem:oCheckItem.getChildItemList()) {
						oChildCheckItem.changePriceLevel(iTargetPriceLevel, true);
						
						//for child modfiier item
						if(oChildCheckItem.hasModifier()) {
							for(FuncCheckItem oChildModifierCheckItem:oChildCheckItem.getModifierList())
								oChildModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
						}
					}
				}
				
				//for modifier
				if(oCheckItem.hasModifier()) {
					for(FuncCheckItem oModifierCheckItem:oCheckItem.getModifierList())
						oModifierCheckItem.changePriceLevel(iTargetPriceLevel, true);
				}
			}
		}
		
		// Re-calculate all target item's modifiers' price
		oCheckItem.setAllModifiersPrice(false);
		
		//get the highest priority sc/tax override rule
		//Check if the service charge is waived by other function
		if(oCheckItem.isWaiveServiceChargeNotByOverride()){
			// If yes, cannot perform override
			return;
		}
		
		oOverrideConditions = null;
		oCurrentOverrideCondition = null;
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForScAndTax();
		boolean bWaiveScFormFastFoodOrTakeoutCheck = false;
		if(!oOverrideConditions.isEmpty()) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions) {
				oCurrentOverrideCondition = oOverrideCondition;
				if(this.checkOverrideCondition(oCurrentOverrideCondition, oCheckCreateTime, oOrderTime, iTableNo, sTableExt, oCheckItem.isTakeoutItem(), bFastFoodOrderingMode, iOutletPeriodId, iSphrId, iCtypId, false, false, false, oDtypeIdList, iItemDepId)) {
					// For override condition "Auto Waive Service Charge For Fast Food Mode" and "Waive Service Charge For Takeout Item", only need to perform waive service charge once
					if (oCurrentOverrideCondition.getName(2).equals("Auto Waive Service Charge For Fast Food Mode") || oCurrentOverrideCondition.getName(2).equals("Waive Service Charge For Takeout Item")) {
						if (bWaiveScFormFastFoodOrTakeoutCheck)
							continue;
						else
							bWaiveScFormFastFoodOrTakeoutCheck = true;
					}
					String[] sScOverrideSetup = oCurrentOverrideCondition.getAllChargeSc();
					String[] sTaxOverrideSetup = oCurrentOverrideCondition.getAllChargeTax();
					
					//check need to do sc/tax override
					bNeedScTaxOverride = true;
					boolean bChargeSc[] = new boolean[5], bWaiveSc[] = new boolean[5];
					String sChargeTax[] = new String[25], sWaiveTax[] = new String[25];
					boolean bChangeSc = false, bChangeTax = false;
					for(int i=0; i<5; i++) {
						bChargeSc[i] = false;
						bWaiveSc[i] = false;
						if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_NO_CHANGE))
							continue;
						
						bChangeSc = true;
						if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_CHARGE))
							bChargeSc[i] = true;
						if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_WAIVE))
							bWaiveSc[i] = true;
					}
					for(int i=0; i<25; i++) {
						sChargeTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
						sWaiveTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_NO_CHANGE))
							continue;
						
						bChangeTax = true;
						
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_CHARGE))
							sChargeTax[i] = PosOverrideCondition.CHARGE_TAX_CHARGE;
						
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_WAIVE))
							sWaiveTax[i] = PosOverrideCondition.CHARGE_TAX_WAIVE;
						
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.INCLUSIVE_TAX_NO_BREAKDOWN))
							sChargeTax[i] = PosOverrideCondition.INCLUSIVE_TAX_NO_BREAKDOWN;
					}
					
					if(bChangeSc || bChangeTax) {
						oCheckItem.addWaiveScTax(false, bWaiveSc, sWaiveTax, "");
						oCheckItem.addWaiveScTax(true, bChargeSc, sChargeTax, "");
					}
				}
			}

			if(!bNeedScTaxOverride) {
				// no override rule, need to change back to original SC/Tax setting
				for(int i=1; i<=5; i++)
					oCheckItem.getCheckItem().setChargeSc(i, oMenuItem.getChargeSc((i-1)));
				for(int i=1; i<=25; i++) 
					oCheckItem.getCheckItem().setChargeTax(i, oMenuItem.getChargeTax((i-1)));
				oCheckItem.removeAddWaivedScTaxExtraInfo();
			}
		}else {
			// no override rule, need to change back to original SC/Tax setting
			for(int i=1; i<=5; i++)
				oCheckItem.getCheckItem().setChargeSc(i, oMenuItem.getChargeSc((i-1)));
			for(int i=1; i<=25; i++)
				oCheckItem.getCheckItem().setChargeTax(i, oMenuItem.getChargeTax((i-1)));
		}
	}
	
	public void checkDiscountOverrideForItemCheck(boolean bAddDiscount, FuncCheckItem oCheckItem, Time oCheckCreateTime, Time oOrderTime,
			int iTableNo, String sTableExt, int iSphrId, int iCtypId, List<Integer> oDtypeIdList) {
		//Check if the service charge is waived by other function
		if(oCheckItem.isWaiveServiceChargeNotByOverride())
			// If yes, cannot perform override
			return;
		//get business period id
		int iOutletPeriodId = 0;
		
		int iItemDepId = oCheckItem.getCheckItem().getDepartmentId();
		
		if(oCheckItem.getCheckItem().getBusinessPeriodId().compareTo("") == 0)
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		else
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(oCheckItem.getCheckItem().getBusinessPeriodId());
		
		List<PosOverrideCondition> oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForScAndTax();
		
		if(!oOverrideConditions.isEmpty()) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions) {
				//skip default override rules
				if (oOverrideCondition.getName(2).equals("Auto Waive Service Charge For Fast Food Mode") || oOverrideCondition.getName(2).equals("Waive Service Charge For Takeout Item"))
					continue;
				
				boolean bDiscountTypeChecking = false;
				//discount type checking
				if(oOverrideCondition.getDtypIds() == null)
					continue;
				else {
					if(oDtypeIdList == null)
						continue;
					else {
						for(Integer iDtypeId : oDtypeIdList) {
							for(String sId : oOverrideCondition.getDtypIds()) {
								if(sId.equals(Integer.toString(iDtypeId))) {
									bDiscountTypeChecking = true;
									break;
								}
							}
							if(bDiscountTypeChecking)
								break;
						}
					}
				}
				
				//get the highest priority sc/tax override rule
				if(this.checkOverrideCondition(oOverrideCondition, oCheckCreateTime, oOrderTime, iTableNo, sTableExt, oCheckItem.isTakeoutItem(), false, iOutletPeriodId, iSphrId, iCtypId, false, false, true, oDtypeIdList, iItemDepId)) {
					if(bAddDiscount) {
						String[] sScOverrideSetup = oOverrideCondition.getAllChargeSc();
						String[] sTaxOverrideSetup = oOverrideCondition.getAllChargeTax();
						
						//check need to do sc/tax override
						boolean bChargeSc[] = new boolean[5], bWaiveSc[] = new boolean[5];
						String sChargeTax[] = new String[25], sWaiveTax[] = new String[25];
						
						boolean bChangeSc = false, bChangeTax = false;
						for(int i=0; i<5; i++) {
							bChargeSc[i] = false;
							bWaiveSc[i] = false;
							if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_NO_CHANGE))
								continue;
							
							bChangeSc = true;
							if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_CHARGE))
								bChargeSc[i] = true;
							if(sScOverrideSetup[i].equals(PosOverrideCondition.CHARGE_SC_WAIVE))
								bWaiveSc[i] = true;
						}
						for(int i=0; i<25; i++) {
							sChargeTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
							sWaiveTax[i] = PosOverrideCondition.CHARGE_TAX_NO_CHANGE;
							if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_NO_CHANGE))
								continue;
							
							bChangeTax = true;
							if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_CHARGE))
								sChargeTax[i] = PosOverrideCondition.CHARGE_TAX_CHARGE;
							if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_WAIVE))
								sWaiveTax[i] = PosOverrideCondition.CHARGE_TAX_WAIVE;
						}
						
						if(bChangeSc || bChangeTax) {
							oCheckItem.addWaiveScTax(false, bWaiveSc, sWaiveTax, "");
							oCheckItem.addWaiveScTax(true, bChargeSc, sChargeTax, "");
						}
					}else {
						MenuItem oMenuItem = oCheckItem.getMenuItem();
						for(int i=1; i<=5; i++)
							oCheckItem.getCheckItem().setChargeSc(i, oMenuItem.getChargeSc((i-1)));
						for(int i=1; i<=25; i++)
							oCheckItem.getCheckItem().setChargeTax(i, oMenuItem.getChargeTax((i-1)));
					}
					break;
				}
			}
		}
	}
	
	private boolean checkOverrideCondition(PosOverrideCondition oOverrideConditions, Time oCheckCreateTime, Time oOrderTime,
			int iTableNo, String sTableExt, boolean bTakeoutItem, boolean bFastFoodMode, int iPerdId, int iSphrId, int iCtypId,
			boolean bIgnoreTimeBy, boolean bIgnoreOrderType, boolean bIgnoreOrderingMode, List<Integer> oDtypeIdList, int iItemDepId) {
		boolean bOverride = false;
		int iWeekday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDayOfWeek();
		boolean bIsSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isSpecialDay();
		boolean bIsDayBeforeSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeSpecialDay();
		boolean bIsHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isHoliday();
		boolean bIsDayBeforeHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeHoliday();
		boolean bDateRangeChecking = false, bTimeRangeChecking = false, bTableRangeChecking = false, bStationGroupChecking = false, bOrderingTypeChecking = false, bCheckOrderingModeChecking  = false;
		boolean bPeriodChecking = false, bSpecialHourChecking = false, bSpecialControlChecking = false;
		boolean bOrCaseFulfill = false;
		boolean bDiscountTypeChecking = false;
		boolean bCustomTypeChecking = false;
		boolean bItemDepGroupChecking = false;
		DateTime oBusinessDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate();
		DateTime oBuinessDateTime = new DateTime(oBusinessDate);
		Time oTime = null;
		
		//checking date range
		if(oOverrideConditions.getStartDate() == null) {
			if(oOverrideConditions.getEndDate() == null || DateTimeComparator.getInstance().compare(oOverrideConditions.getEndDate(), oBuinessDateTime) >= 0)
				bDateRangeChecking = true;
		}else if(DateTimeComparator.getInstance().compare(oOverrideConditions.getStartDate(), oBuinessDateTime) == 0) {
			if(oOverrideConditions.getEndDate() == null || DateTimeComparator.getInstance().compare(oOverrideConditions.getEndDate(), oBuinessDateTime) >= 0)
				bDateRangeChecking = true;
		}else if(DateTimeComparator.getInstance().compare(oOverrideConditions.getStartDate(), oBuinessDateTime) < 0) {
			if(oOverrideConditions.getEndDate() == null || DateTimeComparator.getInstance().compare(oOverrideConditions.getEndDate(), oBuinessDateTime) >= 0)
				bDateRangeChecking = true;
		}
		
		//checking time range
		if(bIgnoreTimeBy) {
			//ignore time by, using system time for checking time range
			DateTimeFormatter oTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
			SimpleDateFormat oSimpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
			DateTime oNowDateTime = AppGlobal.getCurrentTime(false);
			try {
				oTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oNowDateTime)).getTime());
			}catch (ParseException exception) {
				exception.printStackTrace();
				AppGlobal.stack2Log(exception);
			}
		}else {
			//using the time by for checking time range
			oTime = oOrderTime;
			if(oOverrideConditions.isCheckCreatedTimeBy())
				oTime = oCheckCreateTime;
		}
		if(oOverrideConditions.getStartTime() == null) {
			if(oOverrideConditions.getEndTime() == null || oOverrideConditions.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}else if(oOverrideConditions.getStartTime().compareTo(oTime) == 0) {
			if(oOverrideConditions.getEndTime() == null || oOverrideConditions.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}else if(oOverrideConditions.getStartTime().compareTo(oTime) < 0) {
			if(oOverrideConditions.getEndTime() == null || oOverrideConditions.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}
		
		//check table range
		if(oOverrideConditions.getStartTable() > 0 || oOverrideConditions.getEndTable() > 0 || !oOverrideConditions.getStartTableExt().isEmpty() || !oOverrideConditions.getEndTableExt().isEmpty()){
			boolean bFilterTableNo = this.filterTableByNo(oOverrideConditions.getStartTable(), iTableNo, oOverrideConditions.getEndTable());
			boolean bFilterTableExt = this.filterTableByExt(oOverrideConditions.getStartTableExt(), sTableExt, oOverrideConditions.getEndTableExt());
			if(bFilterTableNo && bFilterTableExt)
				bTableRangeChecking = true;
		}else if(oOverrideConditions.getStartTable() == 0 && oOverrideConditions.getEndTable() == 0 && oOverrideConditions.getStartTableExt().equals("") && oOverrideConditions.getEndTableExt().equals(""))
			bTableRangeChecking = true;
		
		//check station group
		if(oOverrideConditions.isNoRuleForStationGroup() || oOverrideConditions.getStgpId() == AppGlobal.g_oFuncStation.get().getStationGroupId())
			bStationGroupChecking = true;
		
		//check ordering type
		if(bIgnoreOrderType)
			bOrderingTypeChecking = true;
		else {
			if(oOverrideConditions.isNoRuleForOrderingType() || (oOverrideConditions.isTakeoutOrderingType() && bTakeoutItem))
				bOrderingTypeChecking = true;
		}
		
		if(bIgnoreOrderingMode)
			bCheckOrderingModeChecking = true;
		else {
			if (oOverrideConditions.isFineDiningOrderingMode() || oOverrideConditions.isFastFoodOrderingMode() && bFastFoodMode)
				bCheckOrderingModeChecking = true;
		}
		
		//check period 
		if(oOverrideConditions.isNoRuleForPeriod() || oOverrideConditions.getPerdId() == iPerdId)
			bPeriodChecking = true;
		
		//check special hour
		if(oOverrideConditions.isNoRuleForSpecialHour() || oOverrideConditions.getSphrId() == iSphrId)
			bSpecialHourChecking = true;
		
		//check special condition
		if(!oOverrideConditions.isNoRuleOnSpeiclaDay()) {
			if(oOverrideConditions.isApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !oOverrideConditions.isNoRuleOnDayBeforeSpeiclaDay()) {
			if(oOverrideConditions.isApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !oOverrideConditions.isNoRuleForHoliday()) {
			if(oOverrideConditions.isApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isApplyOnHolidayWithWeekMask() && bIsHoliday && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnHolidayWithWeekMask() && bIsHoliday && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !oOverrideConditions.isNoRuleForDayBeforeHoliday()) {
			if(oOverrideConditions.isApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && oOverrideConditions.getWeekdayAllowance(iWeekday)){
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(oOverrideConditions.isNotApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && oOverrideConditions.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && oOverrideConditions.getWeekdayAllowance(iWeekday))
			bSpecialControlChecking = true;
		
		//discount type checking
		if(oOverrideConditions.getDtypIds() == null)
			bDiscountTypeChecking = true;
		else {
			if(oDtypeIdList == null)
				bDiscountTypeChecking = true;
			else {
				for(Integer iDtypeId : oDtypeIdList) {
					for(String sId : oOverrideConditions.getDtypIds()) {
						if(sId.equals(Integer.toString(iDtypeId))) {
							bDiscountTypeChecking = true;
							break;
						}
					}
					if(bDiscountTypeChecking)
						break;
				}
			}
		}
		
		//item department group checking
		ArrayList<Integer> oItemDepIdList = new ArrayList<Integer>();
		ArrayList<Integer> oItemDepGpIdlist = new ArrayList<Integer>();
		
		if(oOverrideConditions.getIdgpIds() == null)
			bItemDepGroupChecking = true;
		else{
			if(iItemDepId == 0)
				bItemDepGroupChecking = true;
			else{
				for(String sId : oOverrideConditions.getIdgpIds()){
					if(this.m_oMenuItemDeptGroupAndDeptIdList.containsKey(Integer.parseInt(sId))){
						ArrayList<Integer> oItemDptIds = this.m_oMenuItemDeptGroupAndDeptIdList.get(Integer.parseInt(sId));
						if(oItemDptIds.contains(iItemDepId)){
							bItemDepGroupChecking = true;
							break;
						}
					}
				}
			}
		}
		
		if(oOverrideConditions.isNoRuleForCustomType() || oOverrideConditions.getCtypId() == iCtypId)
			bCustomTypeChecking = true;
		if(bDateRangeChecking && bTimeRangeChecking && bTableRangeChecking && bStationGroupChecking && bOrderingTypeChecking
				&& bCheckOrderingModeChecking && bPeriodChecking && bSpecialHourChecking
				&& bSpecialControlChecking && bDiscountTypeChecking && bCustomTypeChecking && bItemDepGroupChecking)
			bOverride = true;
		return bOverride;
	}
	
	private boolean filterTableByNo(int iStartTableNo, int iCurTableNo, int iEndTableNo){
		if (iStartTableNo == 0 && iEndTableNo == 0 && iCurTableNo != 0)
			return false;
		//	no upper limit for table no
		if (iEndTableNo == 0){
			if (iStartTableNo <= iCurTableNo)
				return true;
		}
		//	with or without lower limit for table no
		else{
			if (iStartTableNo <= iCurTableNo && iCurTableNo <= iEndTableNo)
				return true;
		}
		return false;
	}
	
	private boolean filterTableByExt(String sStartTableExt, String sCurTableExt, String sEndTableExt){
		//no upper limit for table extension
		if (sEndTableExt.equals("")){
			if (sStartTableExt.compareTo(sCurTableExt) <= 0)
				return true;
		}
		//	with or without lower limit for table extension
		else{
			if (sStartTableExt.compareTo(sCurTableExt) <= 0 && sCurTableExt.compareTo(sEndTableExt) <= 0)
				return true;
		}
		return false;
	}
	
	public int panelOverride(int iTableNo, String sTableExt, int iSphrId) {
		int iOutletPeriodId = 0, iTargetPanelId = 0;
		List<PosOverrideCondition> oOverrideConditions = null;
		PosOverrideCondition oCurrentOverrideCondition = null;
		
		//get business period id
		iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		
		//set the target panel id as original one
		iTargetPanelId = AppGlobal.g_oFuncStation.get().getDisplayPanelId();
		
		//get the highest priority panel override rule current price level
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForPanel();
		if(!oOverrideConditions.isEmpty()) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions){
				oCurrentOverrideCondition = oOverrideCondition;
				if(oCurrentOverrideCondition != null) {
					if(this.checkOverrideCondition(oCurrentOverrideCondition, null, null, iTableNo, sTableExt, false, false, iOutletPeriodId, iSphrId, 0, true, true, true, null, 0)) {
						// need to change panel
						iTargetPanelId = oCurrentOverrideCondition.getDpanId();
						break;
					}
				}
			}
		}
		
		return iTargetPanelId;
	}
	
	public boolean checkOrderingTypeOverride(int iTableNo, String sTableExt, int iSphrId, int iCtypId) {
		List<PosOverrideCondition> oOverrideConditions = null;
		String sOrderingType = PosOverrideCondition.ORDERING_TYPE_NO_USE;
		PosOverrideCondition oCurrentOverrideCondition = null;

		//get business period id
		int iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());

		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForOvrrideOrderingType();
		if(!oOverrideConditions.isEmpty()) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions){
				oCurrentOverrideCondition = oOverrideCondition;
				if(oCurrentOverrideCondition != null) {
					if(this.checkOverrideCondition(oCurrentOverrideCondition, null, null, iTableNo, sTableExt, false, false, iOutletPeriodId, iSphrId, iCtypId, true, true, true, null, 0)) {
						// need to change panel
						sOrderingType = oCurrentOverrideCondition.getCheckOrderingType();
						
						break;
					}
				}
			}
		}
		
		return sOrderingType.equals(PosOverrideCondition.ORDERING_TYPE_TAKEOUT);
	}
	
	public List<PosOverrideCondition> getOverrideConditionList() {
		return m_oOverrideConditionList.getOverrideConditionList();
	}
}
