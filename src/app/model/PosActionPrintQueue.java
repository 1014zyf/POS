//Database: pos_action_print_queues - print queue setting for special actions
package app.model;

public class PosActionPrintQueue {
	private int acpqId;
	private int shopId;
	private int oletId;
	private String key;
	private String[] name;
	private String[] shortName;
	private int seq;
	private String method;
	private int prtqId;
	private int pfmtId;
	private String status;
	
	// method
	public static String METHOD_ORIGINAL_PRINT_QUEUE = "";
	public static String METHOD_FIRST_PRINT_QUEUE = "f";
	public static String METHOD_LAST_PRINT_QUEUE = "l";
	public static String METHOD_SPECIFIED_PRINT_QUEUE = "s";
	
	// status
	public static String STATUS_ACTIVE = "";
	public static String STATUS_DELETED = "d";
	
	//init object with initialize value
	public PosActionPrintQueue() {
		this.init();
	}
	
	//init object from database by acpq_id
	public PosActionPrintQueue(int iAcpqId) {
		this.init();
		
		this.acpqId = iAcpqId;
	}
	
	//init value
	public void init() {
		int i=0;
		
		this.acpqId = 0;
		this.shopId = 0;
		this.oletId = 0;
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
		this.method = PosActionPrintQueue.METHOD_ORIGINAL_PRINT_QUEUE;
		this.prtqId = 0;
		this.pfmtId = 0;
		this.status = PosActionPrintQueue.STATUS_ACTIVE;
	}
	
	//read data form database by acpq_id
	public void readById(int iAcpqId) {
		this.acpqId = iAcpqId;
	}
	
	//add record to database
	public boolean add() {
		return true;
	}
	
	//update record to database
	public boolean update() {
		return true;
	}
	
	//get acpqId
	protected int getAcpqId() {
		return this.acpqId;
	}
	
	protected int getShopId() {
		return this.shopId;
	}
	
	protected int getOletId() {
		return this.oletId;
	}
	
	protected String getKey() {
		return this.key;
	}
	
	protected String getName(int iIndex) {
		return this.name[(iIndex-1)];
	}
	
	protected String getShortName(int iIndex) {
		return this.shortName[(iIndex-1)];
	}
	
	protected int getSeq() {
		return this.seq;
	}
	
	protected String getMethod() {
		return this.method;
	}
	
	protected int getPrtqId() {
		return this.prtqId;
	}
	
	protected int getPfmtId() {
		return this.pfmtId;
	}
	
	protected String getStatus() {
		return this.status;
	}
}
