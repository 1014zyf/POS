package app.controller;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class FuncSplitItem {
	private FuncCheckItem m_oFuncCheckItem;
	private String m_sOriTable;
	private int m_iOriSeatNo;
	private int m_iOriSeq;
	private BigDecimal m_oOriQty;
	private BigDecimal m_oOriTotal;
	private DateTime m_oOriOpenTime;
	private String m_sTable;
	private String m_sType;
	
	public static String TYPE_ORIGINAL = "";
	public static String TYPE_SPLITTED = "s";
	public static String TYPE_REPLACED = "r";
	
	FuncSplitItem() {
		m_oFuncCheckItem = null;
		m_iOriSeatNo = 0;
		m_iOriSeq = 0;
		m_oOriQty = null;
		m_sTable = "";
		m_sOriTable = "";
		m_oOriTotal = null;
		m_oOriOpenTime = null;
		m_sType = TYPE_ORIGINAL;
	}
	
	public FuncSplitItem(FuncCheckItem oFuncCheckItem, String sTable) {
		m_oFuncCheckItem = new FuncCheckItem(oFuncCheckItem, null);
		
		if(oFuncCheckItem.hasChildItem()) {
			for(FuncCheckItem oOriChildFuncCheckItem: oFuncCheckItem.getChildItemList()) {
				FuncCheckItem oChildChildFuncCheckItem = new FuncCheckItem(oOriChildFuncCheckItem, m_oFuncCheckItem);
			}
		}
		
		m_sOriTable = sTable;
		m_iOriSeatNo = m_oFuncCheckItem.getCheckItem().getSeatNo();
		m_iOriSeq = m_oFuncCheckItem.getCheckItem().getSeq();
		m_oOriQty = m_oFuncCheckItem.getCheckItem().getQty();
		m_oOriTotal = oFuncCheckItem.getCheckItem().getTotal();
		m_oOriOpenTime = oFuncCheckItem.getCheckItem().getOrderLocTime();
		m_sTable = sTable;
		m_sType = TYPE_ORIGINAL;
	}
	
	public FuncSplitItem(FuncSplitItem oSplitItem) {
		m_oFuncCheckItem = new FuncCheckItem(oSplitItem.m_oFuncCheckItem, null);
		m_oFuncCheckItem.resetAsNewItem();

		if(oSplitItem.m_oFuncCheckItem.hasChildItem()) {
			for(FuncCheckItem oOriChildFuncCheckItem: oSplitItem.m_oFuncCheckItem.getChildItemList()) {
				FuncCheckItem oChildChildFuncCheckItem = new FuncCheckItem(oOriChildFuncCheckItem, m_oFuncCheckItem);
				oChildChildFuncCheckItem.resetAsNewItem();
			}
		}
		
		m_iOriSeatNo = oSplitItem.m_iOriSeatNo;
		m_iOriSeq = oSplitItem.m_iOriSeq;
		m_oOriQty = oSplitItem.m_oOriQty;
		m_sTable = oSplitItem.m_sTable;
		m_sOriTable = oSplitItem.m_sOriTable;
		m_oOriTotal = oSplitItem.m_oOriTotal;
		m_oOriOpenTime = oSplitItem.m_oOriOpenTime;
		m_sType = oSplitItem.m_sType;
	}

	public void setOriSeatNo(int iOriSeatNo) {
		m_iOriSeatNo = iOriSeatNo;
	}
	
	public void setOriSeq(int iOriSeq) {
		m_iOriSeq = iOriSeq;
	}
	
	public void setOriTable(String sOriTable) {
		m_sOriTable = sOriTable;
	}
	
	public void setSeatNo(int iSeatNo) {
		m_oFuncCheckItem.getCheckItem().setSeatNo(iSeatNo);
	}
	
	public void setSeq(int iSeq) {
		m_oFuncCheckItem.getCheckItem().setSeq(iSeq);
	}
	
	public void setQty(BigDecimal oQty) {
		m_oFuncCheckItem.internalChangeQty(oQty);
		if(m_oFuncCheckItem.hasChildItem()) {
			for(FuncCheckItem oChildFuncCheckItem: m_oFuncCheckItem.getChildItemList()) {
				oChildFuncCheckItem.internalChangeQty(oQty);
			}
		}
	}
	
	public void setTotal (BigDecimal oTotal) {
		m_oFuncCheckItem.getCheckItem().setTotal(oTotal);
	}
	
	public void setType(String sType) {
		m_sType = sType;
	}
	
	public void setTable(String sTable) {
		m_sTable = sTable;
	}
	
	public int getOriSeatNo() {
		return m_iOriSeatNo;
	}
	
	public int getOriSeq() {
		return m_iOriSeq;
	}
	
	public String getOriTable() {
		return m_sOriTable;
	}
	
	public String getTable() {
		return m_sTable;
	}

	public BigDecimal getOriQty() {
		return m_oOriQty;
	}
	
	public BigDecimal getOriTotal() {
		return m_oOriTotal;
	}

	public FuncCheckItem getFuncCheckItem() {
		return m_oFuncCheckItem;
	}
	
	public BigDecimal getQty() {
		return m_oFuncCheckItem.getCheckItem().getQty();
	}

	public int getSeatNo() {
		return m_oFuncCheckItem.getCheckItem().getSeatNo();
	}
	
	public int getSeq() {
		return m_oFuncCheckItem.getCheckItem().getSeq();
	}
	
	public BigDecimal getTotal() {
		return m_oFuncCheckItem.getCheckItem().getTotal();
	}
	
	public String getName(int iIndex) {
		return m_oFuncCheckItem.getCheckItem().getName(iIndex);
	}
	
	public boolean isOriginal() {
		return m_sType.equals(TYPE_ORIGINAL);
	}
	
	public boolean isSplitted() {
		return m_sType.equals(TYPE_SPLITTED);
	}
	
	public boolean isOldItem() {
		return m_oFuncCheckItem.isOldItem();
	}
	
	public boolean haveSameOriItem(String sOriTable, int iOriSeatNo, int iOriSeq) {
		if(m_sOriTable.equals(sOriTable)
				&& m_iOriSeatNo == iOriSeatNo
				&& m_iOriSeq == iOriSeq)
			return true;
		
		return false;
	}
	
	public boolean isOldItemWithNoChange() {
		if(m_sOriTable.equals(m_sTable)
				&& m_iOriSeatNo == this.getSeatNo()
				&& m_iOriSeq == this.getSeq()
				&& m_oOriQty.compareTo(this.getQty()) == 0)
			return true;
		
		return false;
	}
}
