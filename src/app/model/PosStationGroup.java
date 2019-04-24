//Database: pos_station_groups - Workstation Groups
package app.model;

public class PosStationGroup {
	private int stgpId;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;

	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	public static String STATUS_SUSPENDED = "s";
	
	//init object with initialize value
	public PosStationGroup () {
		this.init();
	}
	
	//init object from database by stgp_id
	public PosStationGroup (int iStgpId) {
		this.init();
		
		this.stgpId = iStgpId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.stgpId = 0;
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosStationGroup.STATUS_ACTIVE;
	}
	
	//read data from database by stgp_id
	public void readById(int iStgpId) {
		this.stgpId = iStgpId;
	}
	
	//add new station group record to database
	public boolean add() {
		return true;
	}
	
	//update station group record to database
	public boolean update() {
		return true;
	}
	
	//get stgpId
	protected int getStgpId() {
		return this.stgpId;
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
