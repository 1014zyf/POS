//Database: pos_item_stocks - POS menu item stocks
package app.model;

import java.util.Date;

public class PosItemStock {
	private int istkId;
	private int shopId;
	private int oletId;
	private int itemId;
	private Date date;
	private String checkStock;
	private int originalStock;
	private int stockCount;
	private String status;
	
	//init object with initialize value 
	public PosItemStock () {
		this.istkId = 0;
		this.shopId = 0;
		this.oletId = 0;
		this.date = null;
		this.checkStock = "";
		this.originalStock = 0;
		this.stockCount = 0;
		this.status = "";
		
	}
	
	//init object from database by istk_id
	public PosItemStock (int iIstkId) {
		this.istkId = iIstkId;
	}
	
	//read data from database by istk_id
	public void readById (int iIstkId) {
		this.istkId = iIstkId;
	}
	
	//add new item stock record to database
	public boolean add() {
		return true;
	}
	
	//update item stock record to database
	public boolean update() {
		return true;
	}
	
	//get istkId
	protected int getIstkId() {
		return this.istkId;
	}
	
	//get shop id
	protected int getShopId() {
		return this.shopId;
	}
	
	//get olet id
	protected int getOletId() {
		return this.oletId;
	}
	
	//get item id
	protected int getItemId() {
		return this.itemId;
	}
	
	//get date
	protected Date getDate() {
		return this.date;
	}
	
	//get check stock
	protected String getCheckStock() {
		return this.checkStock;
	}
	
	//get original stock
	protected int getOriginalStock() {
		return this.originalStock;
	}
	
	//get stock count
	protected int getStockCount() {
		return this.stockCount;
	}
	
	//get status
	protected String getStatus() {
		return this.status;
	}
}
