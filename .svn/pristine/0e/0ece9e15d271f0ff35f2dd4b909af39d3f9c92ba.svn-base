//Database: pos_discount_acls - Access control lists for discount groups, item discount groups, outlets & dates
package app.model;

import java.util.Date;

public class PosDiscountAcl {
	private int daclId;
	private int dgrpId;
	private int digpId;
	private int oletId;
	private String allow;
	private Date startDate;
	private Date endDate;
	private String weekMask;
	private String holiday;
	private String dayBeforeHoliday;
	private String specialDay;
	private String dayBeforeSpecialDay;
	
	//init object with initialize value
	public PosDiscountAcl () {
		this.daclId = 0;
		this.dgrpId = 0;
		this.digpId = 0;
		this.oletId = 0;
		this.allow = "";
		this.startDate = null;
		this.endDate = null;
		this.weekMask = "0000000";
		this.holiday = "";
		this.dayBeforeHoliday = "";
		this.specialDay = "";
		this.dayBeforeSpecialDay = "";
	}
	
	//init object from database by dacl_id
	public PosDiscountAcl (int iDaclId) {
		this.daclId = iDaclId;
	}
	
	//read data from database by dacl_id
	public void readById (int iDaclId) {
		this.daclId = iDaclId;
	}
	
	//add new system configuration record to database
	public boolean add() {
		return true;
	}
	
	//update system configuration record to database
	public boolean update() {
		return true;
	}
	
	//get daclId
	protected int getDaclId() {
		return this.daclId;
	}
	
	//get dgrp id
	protected int getDgrpId() {
		return this.dgrpId;
	}
	
	//get digp id
	protected int getDigpId() {
		return this.digpId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get allow
	protected String getAllow() {
		return this.allow;
	}
	
	//get start date
	protected Date getStartDate() {
		return this.startDate;
	}
	
	//get end date
	protected Date getEndDate() {
		return this.endDate;
	}
	
	//get week mask
	protected String getWeekMask() {
		return this.weekMask;
	}
	
	//get holiday
	protected String getHoliday() {
		return this.holiday;
	}
	
	//get day before holiday
	protected String getDayBeforeHoliday() {
		return this.dayBeforeHoliday;
	}
	
	//get special day
	protected String getSpecialDay() {
		return this.specialDay;
	}
	
	//get day before special day
	protected String getDayBeforeSpecialDay() {
		return this.dayBeforeSpecialDay;
	}
}
