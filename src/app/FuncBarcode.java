package app;

import java.math.BigDecimal;
import java.util.ArrayList;

class ClsBarCodeFormat{
	private String m_sItemCode;
	private String m_sItemSKU;
	private BigDecimal m_dItemTotal;
	private BigDecimal m_dItemQty;
	
	private int iItemCodeStartPos;
	private int iItemCodeEndPos;
	private int iItemSKUStartPos;
	private int iItemSKUEndPos;
	private int iItemTotalIntegerStartPos;
	private int iItemTotalIntegerEndPos;
	private int iItemTotalDecimalStartPos;
	private int iItemTotalDecimalEndPos;
	private int iItemQtyStartPos;
	private int iItemQtyEndPos;
		
	public ClsBarCodeFormat(){		
		m_sItemCode = "";
		m_sItemSKU = "";
		m_dItemTotal = BigDecimal.ZERO;
		m_dItemQty = BigDecimal.ZERO;
		
		iItemCodeStartPos = 0;
		iItemCodeEndPos = 0;
		iItemSKUStartPos = 0;
		iItemSKUEndPos = 0;
		iItemTotalIntegerStartPos = 0;
		iItemTotalIntegerEndPos = 0;
		iItemTotalDecimalStartPos = 0;
		iItemTotalDecimalEndPos = 0;
		iItemQtyStartPos = 0;
		iItemQtyEndPos = 0;
	}
	
	public void clearValue(){
		m_sItemCode = "";
		m_sItemSKU = "";
		m_dItemTotal = BigDecimal.ZERO;
		m_dItemQty = BigDecimal.ZERO;
	}
	
	public void setItemCodeStartPos(int iItemCodeStartPos){
		this.iItemCodeStartPos = iItemCodeStartPos;
	}
	
	public int getItemCodeStartPos(){
		return iItemCodeStartPos;
	}
	
	public void setItemCodeEndPos(int iItemCodeEndPos){
		this.iItemCodeEndPos = iItemCodeEndPos;
	}
	
	public int getItemCodeEndPos(){
		return iItemCodeEndPos;
	}
	
	public void setItemSKUStartPos(int iItemSKUStartPos){
		this.iItemSKUStartPos = iItemSKUStartPos;
	}
	
	public int getItemSKUStartPos(){
		return iItemSKUStartPos;
	}
	
	public void setItemSKUEndPos(int iItemSKUEndPos){
		this.iItemSKUEndPos = iItemSKUEndPos;
	}
	
	public int getItemSKUEndPos(){
		return iItemSKUEndPos;
	}
	
	public void setItemTotalIntegerStartPos(int iItemTotalIntegerStartPos){
		this.iItemTotalIntegerStartPos = iItemTotalIntegerStartPos;
	}
	
	public int getItemTotalIntegerStartPos(){
		return iItemTotalIntegerStartPos;
	}
	
	public void setItemTotalIntegerEndPos(int iItemTotalIntegerEndPos){
		this.iItemTotalIntegerEndPos = iItemTotalIntegerEndPos;
	}
	
	public int getItemTotalIntegerEndPos(){
		return iItemTotalIntegerEndPos;
	}
	
	public void setItemTotalDecimalStartPos(int iItemTotalDecimalStartPos){
		this.iItemTotalDecimalStartPos = iItemTotalDecimalStartPos;
	}
	
	public int getItemTotalDecimalStartPos(){
		return iItemTotalDecimalStartPos;
	}
	
	public void setItemTotalDecimalEndPos(int iItemTotalDecimalEndPos){
		this.iItemTotalDecimalEndPos = iItemTotalDecimalEndPos;
	}
	
	public int getItemTotalDecimalEndPos(){
		return iItemTotalDecimalEndPos;
	}
	
	public void setItemQtyStartPos(int iItemQtyStartPos){
		this.iItemQtyStartPos = iItemQtyStartPos;
	}
	
	public int getItemQtyStartPos(){
		return iItemQtyStartPos;
	}
	
	public void setItemQtyEndPos(int iItemQtyEndPos){
		this.iItemQtyEndPos = iItemQtyEndPos;
	}
	
	public int getItemQtyEndPos(){
		return iItemQtyEndPos;
	}
	
	public void setItemCode(String sItemCode){
		this.m_sItemCode = sItemCode;
	}
	
	public String getItemCode(){
		return m_sItemCode;
	}
	
	public void setItemSKU(String sItemSKU){
		this.m_sItemSKU = sItemSKU;
	}
	
	public String getItemSKU(){
		return m_sItemSKU;
	}
	
	public void setItemTotal(BigDecimal dItemTotal){
		this.m_dItemTotal = dItemTotal;
	}
	
	public BigDecimal getItemTotal(){
		return m_dItemTotal;
	}
	
	public void setItemQty(BigDecimal dItemQty){
		this.m_dItemQty = dItemQty;
	}
	
	public BigDecimal getItemQty(){
		return m_dItemQty;
	}
	
}


public class FuncBarcode {
	
	private ArrayList<ClsBarCodeFormat> m_oBarCodeFormatsList;
	
	private static final int ITEM_CODE = 'I';
	private static final int ITEM_SKU = 'S';
	private static final int ITEM_TOTAL_INTEGER = 'A';
	private static final int ITEM_TOTAL_DECIMAL = 'D';
	private static final int ITEM_QTY = 'Q';
	
	public FuncBarcode(){
		m_oBarCodeFormatsList = new ArrayList<ClsBarCodeFormat>();
	}
	
	public void initBarcodeOrderingSetup(String sFormats){		
		int iItemCodeStartPos = 0;
		int iItemCodeEndPos = 0;
		int iItemSKUStartPos = 0;
		int iItemSKUEndPos = 0;
		int iItemTotalIntegerStartPos = 0;
		int iItemTotalIntegerEndPos = 0;
		int iItemTotalDecimalStartPos = 0;
		int iItemTotalDecimalEndPos = 0;
		int iItemQtyStartPos = 0;
		int iItemQtyEndPos = 0;
		
		String [] sFormatList = sFormats.split(",");
		if(sFormatList.length > 0){
			for (String sFormat : sFormatList) {
				sFormat = sFormat.toUpperCase();
				iItemCodeStartPos = sFormat.indexOf(ITEM_CODE);
				iItemCodeEndPos = sFormat.lastIndexOf(ITEM_CODE);
				
				iItemSKUStartPos = sFormat.indexOf(ITEM_SKU);
				iItemSKUEndPos = sFormat.lastIndexOf(ITEM_SKU);
				
				iItemTotalIntegerStartPos = sFormat.indexOf(ITEM_TOTAL_INTEGER);
				iItemTotalIntegerEndPos = sFormat.lastIndexOf(ITEM_TOTAL_INTEGER);
				
				iItemTotalDecimalStartPos = sFormat.indexOf(ITEM_TOTAL_DECIMAL);
				iItemTotalDecimalEndPos = sFormat.lastIndexOf(ITEM_TOTAL_DECIMAL);
				
				iItemQtyStartPos = sFormat.indexOf(ITEM_QTY);
				iItemQtyEndPos = sFormat.lastIndexOf(ITEM_QTY);
				
				ClsBarCodeFormat oBarCodeFormat = new ClsBarCodeFormat();
				oBarCodeFormat.setItemCodeStartPos(iItemCodeStartPos);
				oBarCodeFormat.setItemCodeEndPos(iItemCodeEndPos);
				oBarCodeFormat.setItemSKUStartPos(iItemSKUStartPos);
				oBarCodeFormat.setItemSKUEndPos(iItemSKUEndPos);
				oBarCodeFormat.setItemTotalIntegerStartPos(iItemTotalIntegerStartPos);
				oBarCodeFormat.setItemTotalIntegerEndPos(iItemTotalIntegerEndPos);
				oBarCodeFormat.setItemTotalDecimalStartPos(iItemTotalDecimalStartPos);
				oBarCodeFormat.setItemTotalDecimalEndPos(iItemTotalDecimalEndPos);
				oBarCodeFormat.setItemQtyStartPos(iItemQtyStartPos);
				oBarCodeFormat.setItemQtyEndPos(iItemQtyEndPos);
				
				m_oBarCodeFormatsList.add(oBarCodeFormat);
			}
		}
	}
	
	public int getBarCodeFormatCount(){
		return m_oBarCodeFormatsList.size();
	}
	
	public void processBarcodeOrdering(String sReadData, int iFormatListIndex){
		if(m_oBarCodeFormatsList.size()>0){
			
			// Clear previous value
			m_oBarCodeFormatsList.get(iFormatListIndex).clearValue();
			
			try{
				m_oBarCodeFormatsList.get(iFormatListIndex).setItemCode(sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemCodeStartPos(),
						m_oBarCodeFormatsList.get(iFormatListIndex).getItemCodeEndPos()+1));
			}catch(Exception e){}
			
			try{
				if (sReadData.length() < (m_oBarCodeFormatsList.get(iFormatListIndex).getItemSKUEndPos()+1)) {
					m_oBarCodeFormatsList.get(iFormatListIndex).setItemSKU(sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemSKUStartPos()));
				} else {
					m_oBarCodeFormatsList.get(iFormatListIndex).setItemSKU(sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemSKUStartPos(),
							m_oBarCodeFormatsList.get(iFormatListIndex).getItemSKUEndPos()+1));
				}
			}catch(Exception e){}
			
			String sTotal = "";
			try{
				sTotal = sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotalIntegerStartPos(), 
						m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotalIntegerEndPos()+1);
			}catch(Exception e){}
			
			if(m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotalDecimalEndPos() > 0){
				String sDecimal = "";
				try{
					sDecimal = sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotalDecimalStartPos(), 
							m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotalDecimalEndPos()+1);
					sTotal = sTotal + "." + sDecimal;
				}catch(Exception e){}
			}
			
			try{
				m_oBarCodeFormatsList.get(iFormatListIndex).setItemTotal(new BigDecimal(sTotal));
			}catch(Exception e){}
			
			try{
				m_oBarCodeFormatsList.get(iFormatListIndex).setItemQty(new BigDecimal(sReadData.substring(m_oBarCodeFormatsList.get(iFormatListIndex).getItemQtyStartPos(),
						m_oBarCodeFormatsList.get(iFormatListIndex).getItemQtyEndPos()+1)));
			}catch(Exception e){}	
		}
	}
	
	public String getItemCode(int iFormatListIndex){
		return m_oBarCodeFormatsList.get(iFormatListIndex).getItemCode();
	}
	
	public String getItemSKU(int iFormatListIndex){
		return m_oBarCodeFormatsList.get(iFormatListIndex).getItemSKU();
	}
	
	public BigDecimal getItemTotal(int iFormatListIndex){
		return m_oBarCodeFormatsList.get(iFormatListIndex).getItemTotal();
	}
	
	public BigDecimal getItemQty(int iFormatListIndex){
		return m_oBarCodeFormatsList.get(iFormatListIndex).getItemQty();
	}
	

}