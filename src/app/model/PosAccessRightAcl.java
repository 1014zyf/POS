//Database: pos_access_right_acls - Access Control List for the access right, users and outlets
package app.model;

public class PosAccessRightAcl {
	private int raclId;
	private int ugrpId;
	private int userId;
	private int ogrpId;
	private int oletId;
	private int rgrpId;
	private int arigId;
	private String create;
	private String read;
	private String update;
	private String delete;
	
	// create
	public static String CREATE_DENY = "";
	public static String CREATE_ALLOW = "y";
	
	// read
	public static String READ_DENY = "";
	public static String READ_ALLOW = "y";
	
	// update
	public static String UPDATE_DENY = "";
	public static String UPDATE_ALLOW = "y";
	
	// delete
	public static String DELETE_DENY = "";
	public static String DELETE_ALLOW = "y";
	
	//init object with initialize value
	public PosAccessRightAcl() {
		this.init();
	}
	
	//init object with record Id
	public PosAccessRightAcl(int iRaclId) {
		this.init();
		
		this.raclId = iRaclId;
	}
	
	// init value
	public void init() {
		this.raclId = 0;
		this.ugrpId = 0;
		this.userId = 0;
		this.ogrpId = 0;
		this.oletId = 0;
		this.rgrpId = 0;
		this.arigId = 0;
		this.create = PosAccessRightAcl.CREATE_DENY;
		this.read = PosAccessRightAcl.READ_DENY;
		this.update = PosAccessRightAcl.UPDATE_DENY;
		this.delete = PosAccessRightAcl.DELETE_DENY;
	}
	
	public void readById(int iRaclId) {
		this.raclId = iRaclId;
	}
	
	//add access right control list record to database 
	public boolean add() {
		return true;
	}
	
	//update access right control list record to database
	public boolean update() {
		return true;
	}
	
	//get raclId
	protected int getRaclId() {
		return this.raclId;
	}
	
	protected int getUgrpId() {
		return this.ugrpId;
	}
	
	protected int getUserId() {
		return this.userId;
	}
	
	protected int getOgrpId() {
		return this.ogrpId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	protected int getRgrpId() {
		return this.rgrpId;
	}
	
	protected int getArigId() {
		return this.arigId;
	}
	
	protected String getCreate() {
		return this.create;
	}
	
	protected String getRead() {
		return this.read;
	}
	
	protected String getUpdate() {
		return this.update;
	}
	
	protected String getDelete() {
		return this.delete;
	}
}
