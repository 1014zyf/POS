//Database: pos_access_right_groups - POS access right group
package app.model;

public class PosAccessRightGroup {
	private int rgrpId;
	private String[] name;
	private String[] shortName;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosAccessRightGroup() {
		this.init();
	}
	
	//init object with record ID
	public PosAccessRightGroup(int iRgrpId) {
		this.init();
		
		this.rgrpId = iRgrpId;
	}

	// init value
	public void init() {
		int i=0;
		
		this.rgrpId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.status = PosAccessRightGroup.STATUS_ACTIVE;
	}
	
	//read data to object by record ID
	public void readById(int iRgrpId) {
		this.rgrpId = iRgrpId;
	}
	
	//add record to database
	public boolean add() {
		return true;
	}
	
	//update record to database
	public boolean update() {
		return true;
	}
	
	//get getRgrpId
	protected int getRgrpId() {
		return this.rgrpId;
	}
	
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected String getStatus() {
		return this.status;
	}
}
