package app.controller;

import java.math.BigDecimal;

import app.model.MenuItem;

public class FuncMenuItem {
	
	private MenuItem m_oMenuItem;
	
	// Last error message
	private String m_sErrorMessage;
	
	// Return the error message
	public String getLastErrorMessage(){
		return m_sErrorMessage;
	}
	
	public FuncMenuItem(MenuItem oMenuItem){
		m_oMenuItem = oMenuItem;
	}
	
	//get menu item
	public MenuItem getMenuItem() {
		return this.m_oMenuItem;
	}
	
	//check item availabilty
	public boolean isItemAvailable() {
		if(m_oMenuItem.isDisableMode() || m_oMenuItem.isInactiveMode())
			return false;
		else
			return true;
	}
	
	// Get normal item's price
	public BigDecimal getBasicItemPrice(int iPriceLevel){
		if(m_oMenuItem.getBasicPrice(iPriceLevel) != null)
			return m_oMenuItem.getBasicPrice(iPriceLevel);
		else if(iPriceLevel != 0 && m_oMenuItem.getBasicPrice(0) != null)
			return m_oMenuItem.getBasicPrice(0);
		else
			return BigDecimal.ZERO;
	}
}
