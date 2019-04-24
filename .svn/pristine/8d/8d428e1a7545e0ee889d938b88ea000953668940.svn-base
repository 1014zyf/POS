//Database: pos_access_right_group_lookup - Grouping of access right
package app.model;

public class PosAccessRightGroupLookup {
	private int rgluId;
	private int rgrpId;
	private int arigId;
	
	//init object with initialize value
	public PosAccessRightGroupLookup() {
		this.init();
	}
	
	//init object from database by rglu_id
	public PosAccessRightGroupLookup(int iRgluId) {
		this.init();
		
		this.rgluId = iRgluId;
	}
	
	// init value
	public void init() {
		this.rgluId = 0;
		this.rgrpId = 0;
		this.arigId = 0;
	}
	
	//read data from database by rglu_id
	public void readById(int iRgluId) {
		this.rgluId = iRgluId;
	}
	
	//add new access right group lookup record to database
	public boolean add() {
		return true;
	}
	
	//update access right group lookup record
	public boolean update() {
		return true;
	}
	
	//get rgluId
	protected int getRgluId() {
		return this.rgluId;
	}
	
	protected int getRgrpId() {
		return this.rgrpId;
	}
	
	protected int getArigId() {
		return this.arigId;
	}
}
