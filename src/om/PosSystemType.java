//Database: pos_system_types - System types for POS module
package om;

public class PosSystemType {
	private int stypId;
	private String type;
	private String key;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String status;
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosSystemType () {
		this.init();
	}
	
	//Init object from database by styp_id
	public PosSystemType (int iStypId) {
		this.init();
		
		this.stypId = iStypId;
	}
	
	// init value
	public void init() {
		int i=0;
		
		this.stypId = 0;
		this.type = "";
		this.key = "";
		if(this.name == null)
			this.name = new String[5];
		for(i=0; i<5; i++)
			this.name[i] = "";
		if(this.shortName == null)
			this.shortName = new String[5];
		for(i=0; i<5; i++)
			this.shortName[i] = "";
		this.seq = 0;
		this.status = PosSystemType.STATUS_ACTIVE;
	}
	
	//read data from database by styp_id
	public void readById(int iStypId) {
		this.stypId = iStypId;
	}
	
	//add new system type record to database
	public boolean add() {
		return true;
	}
	
	//update system type record to database
	public boolean update() {
		return true;
	}
	
	//get stypId
	protected int getStypId() {
		return this.stypId;
	}
	
	//get type
	protected String getType() {
		return this.type;
	}
	
	//get key
	protected String getKey() {
		return this.key;
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
