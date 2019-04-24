//Datebase: pos_discount_groups Discount groups
package om;

public class PosDiscountGroup {
	private int dgrpId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosDiscountGroup () {
		this.init();
		
	}
	
	//init object from database by dgrp_id
	public PosDiscountGroup (int iDgrpId) {
		this.init();
		
		this.dgrpId = iDgrpId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.dgrpId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<=5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<=5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosDiscountGroup.STATUS_ACTIVE;
	}
	
	//read data from database by dgrp_id
	public void readById (int iDgrpId) {
		this.dgrpId = iDgrpId;
	}
	
	//add new discount group record to database
	public boolean add() {
		return true;
	}
	
	//update discount group record to database
	public boolean update() {
		return true;
	}
	
	//get dgrpId
	protected int getDgrpId() {
		return this.dgrpId;
	}
	
	//get name by lang index
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	//get short name by lang index
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	//get seq
	protected int getSeq() {
		return this.seq;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
	
}
