package app;

import externallib.StringLib;

public class FuncLookupButtonInfo {
	private int id;
	private String type;
	private String[] name;
	private int seq;
	private String image;
	private String iconImage;
	private String price;
	private int top;
	private int left;
	private int width;
	private int height;
	private int padding;
	private int fontSize;
	private String fontColor;
	private String backgroundColor;
	private String borderColor;
	private String borderStyle;
	private String parameter;
	private boolean blockUI;
	
	private String priceFontColor;
	private String textFontColor;
	private boolean bPriceInLeftTopCorner;
	private boolean bStockQtyInRIghtTopCorner;
	private boolean bHaveAddMinusBtn;
	private boolean bHaveImage;
	private boolean bHaveIconImage;
	private boolean bHaveStockQty;
	private boolean bAbleToClick;
	private boolean bDim;
	
	FuncLookupButtonInfo() {
		init();
	}
	
	private void init() {
		id = 0;
		type = "";
		name = StringLib.createStringArray(AppGlobal.LANGUAGE_COUNT, "");
		seq = -1;
		image = null;
		iconImage = null;
		price = null;
		top = 0;
		left = 0;
		width = 0;
		height = 0;
		padding = 0;
		fontSize = 0;
		fontColor = null;
		priceFontColor = null;
		textFontColor = null;
		backgroundColor = null;
		borderColor = null;
		borderStyle = null;
		parameter = null;
		blockUI = false;
		bPriceInLeftTopCorner = false;
		bStockQtyInRIghtTopCorner = false;
		bHaveAddMinusBtn = false;
		bHaveImage = false;
		bHaveIconImage = false;
		bHaveStockQty = false;
		bAbleToClick = true;
		bDim = false;
	}
	
	public void setId(int iId) {
		id = iId;
	}
	
	public void setType(String sType) {
		type = sType;
	}
	
	public void setName(String[] sName) {
		name = sName;
	}
	
	public void setSeq(int iSeq) {
		seq = iSeq;
	}
	
	public void setPrice(String sPrice) {
		price = sPrice;
	}
	
	public void setImage(String sImage) {
		image = sImage;
	}
	
	public void setIconImage(String sIconImage) {
		iconImage = sIconImage;
	}
	
	public void setTop(int iTop) {
		top = iTop;
	}
	
	public void setLeft(int iLeft) {
		left = iLeft;
	}
	
	public void setWidth(int iWidth) {
		width = iWidth;
	}
	
	public void setHeight(int iHeight) {
		height = iHeight;
	}
	
	public void setPadding(int iPadding) {
		padding = iPadding;
	}
	
	public void setFontSize(int iFontSize) {
		fontSize = iFontSize;
	}
	
	public void setFontColor(String sFontColor) {
		fontColor = sFontColor;
	}
	
	public void setBacgroundColor(String sBackgroundColor) {
		backgroundColor = sBackgroundColor;
	}
	
	public void setBorderColor(String sBorderColor) {
		borderColor = sBorderColor;
	}
	
	public void setBorderStyle(String sBorderStyle) {
		borderStyle = sBorderStyle;
	}
	
	public void setParameter(String sParameter) {
		parameter = sParameter;
	}
	
	public void setBlockUI(boolean bBlockUI) {
		blockUI = bBlockUI;
	}
	
	public void setPriceInLeftTopCorner(boolean bShow) {
		bPriceInLeftTopCorner = bShow;
	}
	
	public void setStockQtyInRightTopCorner(boolean bShow) {
		bStockQtyInRIghtTopCorner = bShow;
	}
	
	public void setAddMinusBtn(boolean bAddMinusBtn) {
		bHaveAddMinusBtn = bAddMinusBtn;
	}
	
	public void setAllowImage(boolean bAllowImage) {
		this.bHaveImage = bAllowImage;
	}
	
	public void setAllowIconImage(boolean bAllowIconImage) {
		this.bHaveIconImage = bAllowIconImage;
	}
	
	public void setHaveStockQty(boolean bHaveStockQty) {
		this.bHaveStockQty = bHaveStockQty;
	}
	
	public void setAbleToClick(boolean bAbleToClick) {
		this.bAbleToClick = bAbleToClick;
	}
	
	public void setDim(boolean bDim) {
		this.bDim = bDim;
	}

	public int getId() {
		return this.id;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String[] getName() {
		return this.name;
	}
	
	public int getSeq() {
		return this.seq;
	}
	
	public String getPrice() {
		return this.price;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public String getIconImage() {
		return this.iconImage;
	}
	
	public int getTop() {
		return this.top;
	}
	
	public int getLeft() {
		return this.left;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getPadding() {
		return this.padding;
	}
	
	public String getFontColor() {
		return this.fontColor;
	}
	
	public int getFontSize() {
		return this.fontSize;
	}
	
	public String getBackgroundColor() {
		return this.backgroundColor;
	}
	
	public String getBorderColor() {
		return this.borderColor;
	}
	
	public String getBorderStyle() {
		return this.borderStyle;
	}
	
	public String getParameter() {
		return this.parameter;
	}
	
	public boolean isBlockUI() {
		return this.blockUI;
	}

	public boolean getPriceInLeftTopCorner() {
		return bPriceInLeftTopCorner;
	}
	
	public boolean getStockQtyInRightTopCorner() {
		return bStockQtyInRIghtTopCorner;
	}
	
	public boolean haveAddMinusBtn() {
		return bHaveAddMinusBtn;
	}
	
	public boolean haveImage() {
		return bHaveImage;
	}
	
	public boolean haveIconImage() {
		return bHaveIconImage;
	}
		
	public boolean haveStockQty() {
		return bHaveStockQty;
	}
	
	public String getPriceFontColor() {
		return priceFontColor;
	}
	
	public void setPriceFontColor(String priceFontColor) {
		this.priceFontColor = priceFontColor;
	}
	
	public String getTextFontColor() {
		return textFontColor;
	}
	
	public void setTextFontColor(String textFontColor) {
		this.textFontColor = textFontColor;
	}
	
	public boolean getAbleToClick() {
		return bAbleToClick;
	}
	
	public boolean getDim() {
		return bDim;
	}
}
