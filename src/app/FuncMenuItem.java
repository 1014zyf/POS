package app;

import java.math.BigDecimal;

import om.MenuItem;
import om.MenuSetMenuLookup;

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
	public BigDecimal getBasicItemPrice(int iPriceLevel) {
		if(m_oMenuItem.getBasicPriceByPriceLevel(iPriceLevel) != null)
			return m_oMenuItem.getBasicPriceByPriceLevel(iPriceLevel);
		else
			return BigDecimal.ZERO;
	}
	
	//check set menu has self select menu lookup
	public boolean isSelfSelectedSetMenu() {
		boolean bHasSelfSelectLookup = false;
		if (m_oMenuItem.getChildCount() > 0) {
			for (MenuSetMenuLookup oMenuSetMenuLookup : m_oMenuItem.getSetMenuLookup()) {
				if (oMenuSetMenuLookup.isSelfSelectMenu()) {
					bHasSelfSelectLookup = true;
					break;
				}
			}
		}
		
		return bHasSelfSelectLookup;
	}
	
	//check whether set menu is valid or not
	public boolean isValidSetMenu() {
		for (MenuSetMenuLookup oMenuSetMenuLookup : m_oMenuItem.getSetMenuLookup()) {
			if (oMenuSetMenuLookup.isSelfSelectMenu())
				continue;
			if (oMenuSetMenuLookup.getChildItem().isDisableMode() || oMenuSetMenuLookup.getChildItem().isInactiveMode() || oMenuSetMenuLookup.getChildItem().isSuspended())
				return false;
		}
		return true;
	}
}
