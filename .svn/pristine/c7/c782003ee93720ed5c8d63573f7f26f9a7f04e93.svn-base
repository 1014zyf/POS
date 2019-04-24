package app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.sql.Time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import app.model.MenuItem;
import app.model.PosOverrideCondition;
import app.model.PosOverrideConditionList;

public class FuncOverride {
	private PosOverrideConditionList m_oOverrideConditionList;
	private PosOverrideCondition m_oCurrentOverrideCondition;
	
	public FuncOverride() {
		m_oOverrideConditionList = new PosOverrideConditionList();	
	}
	
	public void readAllOverrideCondition(int iOutletId){
		m_oOverrideConditionList.readAllOverrideCondition(iOutletId);	
	}
	
	public void checkPriceOverrideForItem(FuncCheckItem oCheckItem, Time oCheckCreateTime, Time oOrderTime, int iTableNo, int iSphrId) {
		int iOutletPeriodId = 0;
		boolean bNeedPriceOverride = false, bNeedScTaxOverride = false;
		List<PosOverrideCondition> oOverrideConditions = null;
		m_oCurrentOverrideCondition = null;
		
		if(oCheckItem.isChangePriceLevelManually())
			return;
		
		//get business period id
		if(oCheckItem.getCheckItem().getBusinessPeriodId() == 0)
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		else
			iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(oCheckItem.getCheckItem().getBusinessPeriodId());
		
		//get the highest priority price level override rule current price level
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForPriceLevel(oCheckItem.getCheckItem().getOriginalPriceLevel());
		if(oOverrideConditions.size() > 0) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions) {
				m_oCurrentOverrideCondition = oOverrideCondition;
				if(this.checkOverrideCondition(oCheckCreateTime, oOrderTime, iTableNo, oCheckItem.isTakeoutItem(), iOutletPeriodId, iSphrId, false, false)) {
					// need to change price level	
					bNeedPriceOverride = true;
					int iTargetPriceLevel = m_oCurrentOverrideCondition.getToPriceLevel();
								
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
		m_oCurrentOverrideCondition = null;
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForScAndTax();
		if(oOverrideConditions.size() > 0) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions) {
				m_oCurrentOverrideCondition = oOverrideCondition;
				if(this.checkOverrideCondition(oCheckCreateTime, oOrderTime, iTableNo, oCheckItem.isTakeoutItem(), iOutletPeriodId, iSphrId, false, false)) {
					String[] sScOverrideSetup = m_oCurrentOverrideCondition.getAllChargeSc();
					String[] sTaxOverrideSetup = m_oCurrentOverrideCondition.getAllChargeTax();

					//check need to do sc/tax override
					bNeedScTaxOverride = true;
					boolean bChargeSc[] = new boolean[5], bWaiveSc[] = new boolean[5], bChargeTax[] = new boolean[25], bWaiveTax[] = new boolean[25];
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
						bChargeTax[i] = false;
						bWaiveTax[i] = false;
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_NO_CHANGE))
							continue;
						
						bChangeTax = true;
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_CHARGE))
							bChargeTax[i] = true;
						if(sTaxOverrideSetup[i].equals(PosOverrideCondition.CHARGE_TAX_WAIVE))
							bWaiveTax[i] = true;
					}
					
					if(bChangeSc || bChangeTax) {
						oCheckItem.addWaiveScTax(false, bWaiveSc, bWaiveTax, "");
						oCheckItem.addWaiveScTax(true, bChargeSc, bChargeTax, "");
					}
					
					break;
				}
			}
			
			if(!bNeedScTaxOverride) {
				// no override rule, need to change back to original SC/Tax setting
				MenuItem oMenuItem = oCheckItem.getMenuItem();
				for(int i=1; i<=5; i++)
					oCheckItem.getCheckItem().setChargeSc(i, oMenuItem.getChargeSc((i-1)));
				for(int i=1; i<=25; i++)
					oCheckItem.getCheckItem().setChargeTax(i, oMenuItem.getChargeTax((i-1)));
			}
			
		}else {
			// no override rule, need to change back to original SC/Tax setting
			MenuItem oMenuItem = oCheckItem.getMenuItem();
			for(int i=1; i<=5; i++)
				oCheckItem.getCheckItem().setChargeSc(i, oMenuItem.getChargeSc((i-1)));
			for(int i=1; i<=25; i++)
				oCheckItem.getCheckItem().setChargeTax(i, oMenuItem.getChargeTax((i-1)));
		}
	}
	
	private boolean checkOverrideCondition(Time oCheckCreateTime, Time oOrderTime, int iTableNo, boolean bTakeoutItem, int iPerdId, int iSphrId, boolean bIgnoreTimeBy, boolean bIgnoreOrderType) {
		boolean bOverride = false;
		int iWeekday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDayOfWeek();
		boolean bIsSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isSpecialDay();
		boolean bIsDayBeforeSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeSpecialDay();
		boolean bIsHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isHoliday();
		boolean bIsDayBeforeHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeHoliday();
		boolean bDateRangeChecking = false, bTimeRangeChecking = false, bTableRangeChecking = false, bStationGroupChecking = false, bOrderingTypeChecking = false;
		boolean bPeriodChecking = false, bSpecialHourChecking = false, bSpecialControlChecking = false;
		boolean bOrCaseFulfill = false;
		
		Date oBusinessDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate();
		Time oTime = null;
		
		//checking date range 
		if(m_oCurrentOverrideCondition.getStartDate() == null) {
			if(m_oCurrentOverrideCondition.getEndDate() == null || m_oCurrentOverrideCondition.getEndDate().compareTo(oBusinessDate) >= 0)
				bDateRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartDate().compareTo(oBusinessDate) == 0) {
			if(m_oCurrentOverrideCondition.getEndDate() == null || m_oCurrentOverrideCondition.getEndDate().compareTo(oBusinessDate) >= 0)
				bDateRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartDate().compareTo(oBusinessDate) < 0) {
			if(m_oCurrentOverrideCondition.getEndDate() == null || m_oCurrentOverrideCondition.getEndDate().compareTo(oBusinessDate) >= 0)
				bDateRangeChecking = true;
		}
		
		//checking time range
		if(bIgnoreTimeBy) {
			//ignore time by, using system time for checking time range
			DateTimeFormatter oTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
			SimpleDateFormat oSimpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
			DateTime oNowDateTime = new DateTime();
			try {
				oTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oNowDateTime)).getTime());
			}catch (ParseException exception) {
				exception.printStackTrace();
				AppGlobal.stack2Log(exception);
			}
		}else {
			//using the time by for checking time range
			oTime = oOrderTime;
			if(m_oCurrentOverrideCondition.isCheckCreatedTimeBy())
				oTime = oCheckCreateTime;
		}
		if(m_oCurrentOverrideCondition.getStartTime() == null) {
			if(m_oCurrentOverrideCondition.getEndTime() == null || m_oCurrentOverrideCondition.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartTime().compareTo(oTime) == 0) {
			if(m_oCurrentOverrideCondition.getEndTime() == null || m_oCurrentOverrideCondition.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartTime().compareTo(oTime) < 0) {
			if(m_oCurrentOverrideCondition.getEndTime() == null || m_oCurrentOverrideCondition.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		}
		
		//check table range
		if(m_oCurrentOverrideCondition.getStartTable() == 0) {
			if(m_oCurrentOverrideCondition.getEndTable() == 0 || m_oCurrentOverrideCondition.getEndTable() >= iTableNo)
				bTableRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartTable() == iTableNo) {
			if(m_oCurrentOverrideCondition.getEndTable() == 0 || m_oCurrentOverrideCondition.getEndTable() >= iTableNo)
				bTableRangeChecking = true;
		}else if(m_oCurrentOverrideCondition.getStartTable() < iTableNo) {
			if(m_oCurrentOverrideCondition.getEndTable() == 0 || m_oCurrentOverrideCondition.getEndTable() >= iTableNo)
				bTableRangeChecking = true;
		}
		
		//check station group
		if(m_oCurrentOverrideCondition.isNoRuleForStationGroup() || m_oCurrentOverrideCondition.getStgpId() == AppGlobal.g_oFuncStation.get().getStationGroupId())
			bStationGroupChecking = true;
		
		//check ordering type
		if(bIgnoreOrderType)
			bOrderingTypeChecking = true;
		else {
			if(m_oCurrentOverrideCondition.isNoRuleForOrderingType() || (m_oCurrentOverrideCondition.isTakeoutOrderingType() && bTakeoutItem))
				bOrderingTypeChecking = true;
		}
		
		//check period 
		if(m_oCurrentOverrideCondition.isNoRuleForPeriod() || m_oCurrentOverrideCondition.getPerdId() == iPerdId)
			bPeriodChecking = true;
		
		//check special hour
		if(m_oCurrentOverrideCondition.isNoRuleForSpecialHour() || m_oCurrentOverrideCondition.getSphrId() == iSphrId)
			bSpecialHourChecking = true;
		
		//check special condition
		if(!m_oCurrentOverrideCondition.isNoRuleOnSpeiclaDay()) {
			if(m_oCurrentOverrideCondition.isApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnSpecialDayWithoutWeekMask() && bIsSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnSpecialDayWithWeekMask() && bIsSpecialDay && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !m_oCurrentOverrideCondition.isNoRuleOnDayBeforeSpeiclaDay()) {
			if(m_oCurrentOverrideCondition.isApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnDayBeforeSpecialDayWithoutWeekMask() && bIsDayBeforeSpecialDay) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnDayBeforeSpecialDayWithWeekMask() && bIsDayBeforeSpecialDay && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !m_oCurrentOverrideCondition.isNoRuleForHoliday()) {
			if(m_oCurrentOverrideCondition.isApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isApplyOnHolidayWithWeekMask() && bIsHoliday && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnHolidayWithoutWeekMask() && bIsHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnHolidayWithWeekMask() && bIsHoliday && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && !m_oCurrentOverrideCondition.isNoRuleForDayBeforeHoliday()) {
			if(m_oCurrentOverrideCondition.isApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)){
				bSpecialControlChecking = true;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnDayBeforeHolidayWithoutWeekMask() && bIsDayBeforeHoliday) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}else if(m_oCurrentOverrideCondition.isNotApplyOnDayBeforeHolidayWithWeekMask() && bIsDayBeforeHoliday && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday)) {
				bSpecialControlChecking = false;
				bOrCaseFulfill = true;
			}
		}
		if(bOrCaseFulfill == false && m_oCurrentOverrideCondition.getWeekdayAllowance(iWeekday))
			bSpecialControlChecking = true;
		
		if(bDateRangeChecking && bTimeRangeChecking && bTableRangeChecking && bStationGroupChecking && bOrderingTypeChecking && bPeriodChecking && bSpecialHourChecking && bSpecialControlChecking)
			bOverride = true;
		
		return bOverride;		
	}
	
	public int panelOverride(int iTableNo, int iSphrId) {
		int iOutletPeriodId = 0, iTargetPanelId = 0;
		List<PosOverrideCondition> oOverrideConditions = null;
		m_oCurrentOverrideCondition = null;
		
		//get business period id
		iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());
		
		//set the target panel id as original one
		iTargetPanelId = AppGlobal.g_oFuncStation.get().getDisplayPanelId();
		
		//get the highest priority panel override rule current price level
		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForPanel();
		if(oOverrideConditions.size() > 0) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions){
				m_oCurrentOverrideCondition = oOverrideCondition;
				if(m_oCurrentOverrideCondition != null) {
					if(this.checkOverrideCondition(null, null, iTableNo, false, iOutletPeriodId, iSphrId, true, true)) {
						// need to change panel
						iTargetPanelId = m_oCurrentOverrideCondition.getDpanId();
						break;
					}
				}
			}
		}
		
		return iTargetPanelId;
	}
	
	public boolean checkOrderingTypeOverride(int iTableNo, int iSphrId) {
		List<PosOverrideCondition> oOverrideConditions = null;
		String sOrderingType = PosOverrideCondition.ORDERING_TYPE_NO_USE;

		//get business period id
		int iOutletPeriodId = AppGlobal.g_oFuncOutlet.get().getOutletPeriodIdByBusinessPeriodId(AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId());

		oOverrideConditions = m_oOverrideConditionList.getConditionWithPriorityForOvrrideOrderingType();
		if(oOverrideConditions.size() > 0) {
			for(PosOverrideCondition oOverrideCondition: oOverrideConditions){
				m_oCurrentOverrideCondition = oOverrideCondition;
				if(m_oCurrentOverrideCondition != null) {
					if(this.checkOverrideCondition(null, null, iTableNo, false, iOutletPeriodId, iSphrId, true, true)) {
						// need to change panel
						sOrderingType = m_oCurrentOverrideCondition.getCheckOrderingType();
						break;
					}
				}
			}
		}
		
		return sOrderingType.equals(PosOverrideCondition.ORDERING_TYPE_TAKEOUT);
	}
}
