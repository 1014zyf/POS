package app;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import om.PosDiscountAclList;
import om.PosDiscountType;
import om.MenuItem;
import om.PosDiscountAcl;

public class FuncDiscountAcl {
	private PosDiscountAclList m_oDiscountAclList;
	
	public FuncDiscountAcl() {
		m_oDiscountAclList = new PosDiscountAclList();
	}
	
	public void readAllDiscountAclByOutlet(int iOutletId) {
		m_oDiscountAclList.readAllDiscountAclByOutlet(iOutletId);
	}
	
	public boolean checkDiscountAcl(MenuItem oMenuItem, PosDiscountType oDiscountType) {
		boolean bAllow = false;
		int iWeekday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDayOfWeek();
		boolean bIsSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isSpecialDay();
		boolean bIsDayBeforeSpecialDay = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeSpecialDay();
		boolean bIsHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isHoliday();
		boolean bIsDayBeforeHoliday = AppGlobal.g_oFuncOutlet.get().getBusinessDay().isDayBeforeHoliday();
		boolean bDateRangeChecking = false;
		boolean bTimeRangeChecking = false;
		boolean bSpecialControlChecking = false;
		boolean bOrCaseFulfill = false;
		boolean bHaveMatchAcl = false;
		
		DateTime oBusinessDate = AppGlobal.g_oFuncOutlet.get().getBusinessDay().getDate();
		
		for (PosDiscountAcl oPosDiscountAcl: m_oDiscountAclList.getDiscountAclList()) {
			if (oPosDiscountAcl.getDgrpId() != 0 && oPosDiscountAcl.getDgrpId() != oDiscountType.getDgrpId())
				continue;
			
			if (oPosDiscountAcl.getDigpId() != 0 && oPosDiscountAcl.getDigpId() != oMenuItem.getDiscountItemGroupId())
				continue;
			
			bHaveMatchAcl = true;
			if (oPosDiscountAcl.isAllow()) {
				//checking date range
				if (oPosDiscountAcl.getStartDate() == null) {
					if (oPosDiscountAcl.getEndDate() == null || !(oPosDiscountAcl.getEndDate().compareTo(oBusinessDate) < 0))// end date >= business date
						bDateRangeChecking = true;
				} else if(!(oPosDiscountAcl.getStartDate().compareTo(oBusinessDate) > 0)) {	// start date <= business date
					if (oPosDiscountAcl.getEndDate() == null || !(oPosDiscountAcl.getEndDate().compareTo(oBusinessDate) < 0))	// end date >= business date
						bDateRangeChecking = true;
				}

				//checking time range
				bTimeRangeChecking = this.timeRangechecking(oPosDiscountAcl);
				
				//check special condition
				if (bOrCaseFulfill == false && bIsSpecialDay && !oPosDiscountAcl.isNoRuleOnSpeiclaDay()) {
					if (oPosDiscountAcl.isApplyOnSpecialDayWithoutWeekMask()) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isApplyOnSpecialDayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnSpecialDayWithoutWeekMask()) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnSpecialDayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					}
				}
				
				if (bOrCaseFulfill == false && bIsDayBeforeSpecialDay && !oPosDiscountAcl.isNoRuleOnDayBeforeSpeiclaDay()) {
					if (oPosDiscountAcl.isApplyOnDayBeforeSpecialDayWithoutWeekMask()) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isApplyOnDayBeforeSpecialDayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnDayBeforeSpecialDayWithoutWeekMask()) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnDayBeforeSpecialDayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					}
				}
				
				if (bOrCaseFulfill == false && bIsHoliday && !oPosDiscountAcl.isNoRuleForHoliday()) {
					if (oPosDiscountAcl.isApplyOnHolidayWithoutWeekMask()) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isApplyOnHolidayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnHolidayWithoutWeekMask()) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnHolidayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					}
				}
				
				if (bOrCaseFulfill == false && bIsDayBeforeHoliday && !oPosDiscountAcl.isNoRuleForDayBeforeHoliday()) {
					if (oPosDiscountAcl.isApplyOnDayBeforeHolidayWithoutWeekMask()) {
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isApplyOnDayBeforeHolidayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)){
						bSpecialControlChecking = true;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnDayBeforeHolidayWithoutWeekMask()) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					} else if (oPosDiscountAcl.isNotApplyOnDayBeforeHolidayWithWeekMask() && oPosDiscountAcl.getWeekdayAllowance(iWeekday)) {
						bSpecialControlChecking = false;
						bOrCaseFulfill = true;
					}
				}
				
				if (bOrCaseFulfill == false && oPosDiscountAcl.getWeekdayAllowance(iWeekday))
					bSpecialControlChecking = true;
				
				if (bDateRangeChecking && bTimeRangeChecking && bSpecialControlChecking)
					bAllow = true;
			}
			break;
		}
		
		if (!bHaveMatchAcl)
			bAllow = true;
		
		return bAllow;
	}
	
	public boolean checkDiscountAclTime(MenuItem oMenuItem, PosDiscountType oDiscountType) {
		boolean bTimeAllow = false;
		boolean bHaveMatchAcl = false;
		
		for (PosDiscountAcl oPosDiscountAcl: m_oDiscountAclList.getDiscountAclList()) {
			//check current discount type whether match the rule
			if (oPosDiscountAcl.getDgrpId() != 0 && oPosDiscountAcl.getDgrpId() != oDiscountType.getDgrpId())
				continue;
			
			//check current discount type whether match discount item group
			if (oPosDiscountAcl.getDigpId() != 0 && oPosDiscountAcl.getDigpId() != oMenuItem.getDiscountItemGroupId())
				continue;
			
			bHaveMatchAcl = true;
			
			bTimeAllow = timeRangechecking(oPosDiscountAcl);
			break;
		}
		
		if (!bHaveMatchAcl)
			bTimeAllow = true;
		
		return bTimeAllow;
	}

	public boolean timeRangechecking(PosDiscountAcl oPosDiscountAcl) {
		// no start time and end time setup
		if (oPosDiscountAcl.getStartTime() == null && oPosDiscountAcl.getEndTime() == null)
			return true;
		
		boolean bTimeRangeChecking = false;
		DateTime oNowDateTime = AppGlobal.getCurrentTime(false);
		DateTimeFormatter oTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
		SimpleDateFormat oSimpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		
		// get current time
		Time oTime = null;
		try {
			oTime = new Time(oSimpleTimeFormat.parse(oTimeFormatter.print(oNowDateTime)).getTime());
		}catch (ParseException exception) {
			exception.printStackTrace();
			AppGlobal.stack2Log(exception);
		}
		
		// time checking
		if (oPosDiscountAcl.getStartTime() == null || oPosDiscountAcl.getStartTime().compareTo(oTime) <= 0) 
			if (oPosDiscountAcl.getEndTime() == null || oPosDiscountAcl.getEndTime().compareTo(oTime) >= 0)
				bTimeRangeChecking = true;
		
		return bTimeRangeChecking;
	}
}
