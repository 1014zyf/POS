package om;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PosPaymentGatewayTransactionsList {
	private List<PosPaymentGatewayTransactions> m_oPaymentGatewayTransactionsList;
	
	public PosPaymentGatewayTransactionsList() {
		m_oPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
	}
	
	public PosPaymentGatewayTransactionsList(JSONArray payGatewayTransJSONArray) {
		m_oPaymentGatewayTransactionsList = new ArrayList<PosPaymentGatewayTransactions>();
		PosPaymentGatewayTransactions oTempPaymentGatewayTransactions = null;
		
		for (int i = 0; i < payGatewayTransJSONArray.length(); i++) {
			if (payGatewayTransJSONArray.isNull(i))
				continue;
		
			JSONObject pantryMessageJSONObject = payGatewayTransJSONArray.optJSONObject(i);
			oTempPaymentGatewayTransactions = new PosPaymentGatewayTransactions(pantryMessageJSONObject);
			m_oPaymentGatewayTransactionsList.add(oTempPaymentGatewayTransactions);
		}
	}
	
	// Clear the payment gateway transactions list
	public void clearList(){
		m_oPaymentGatewayTransactionsList.clear();
	}

	// Add payment gateway transaction to list
	public void add(PosPaymentGatewayTransactions oPosPaymentGatewayTransactions){
		m_oPaymentGatewayTransactionsList.add(oPosPaymentGatewayTransactions);
	}
	
	// Remove payment gateway transaction from list
	public void remove(int iIndex){
		m_oPaymentGatewayTransactionsList.remove(iIndex);
	}
	
	// Get payment gateway transactions by index
	public PosPaymentGatewayTransactions getPosPaymentGatewayTransactionsByIndex(int iIndex){
		return this.m_oPaymentGatewayTransactionsList.get(iIndex);
	}
	
	// Get whole payment gateway transactions list
	public List<PosPaymentGatewayTransactions> getPosPaymentGatewayTransactionsList() {
		return this.m_oPaymentGatewayTransactionsList;
	}
	
	// find the active payment transaction amount on specific parent_auth_code and authorized type
	public boolean checkExistActivePaymentGatewayTransactionByParentAuthCodeAndType(String sParentAuthCode, String sAuthType) {
		boolean bFound = false;
		
		if (sAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH) || sAuthType.equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
			sAuthType = PosPaymentGatewayTransactions.TYPE_AUTH;
		
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList()) {
			String sTempParentAuthCode = oPosPaymentGatewayTrans.getParentAuthCode();
			String sTempAuthType = oPosPaymentGatewayTrans.getType();
			
			// grouped by 2 auth type : (1) TYPE_AUTH + TYPE_TOPUP_AUTH (2) TYPE_COMPLETE_AUTH
			if (sTempAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH) || sTempAuthType.equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
				sTempAuthType = PosPaymentGatewayTransactions.TYPE_AUTH;

			if (sTempParentAuthCode.equals(sParentAuthCode) && sTempAuthType.equals(sAuthType)) {
				bFound = true;
				break;
			}
		}
		
		return bFound;
	}
	
	// add the active payment transaction amount on specific parent_auth_code and authorized type
	public void updateActivePaymentGatewayTransactionAmountByAuthAuthCodeAndType(String sParentAuthCode, String sAuthCode, String sAuthType, BigDecimal dAddAmount) {
		BigDecimal dTempAmount = BigDecimal.ZERO;
		
		if (sAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH) || sAuthType.equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
			sAuthType = PosPaymentGatewayTransactions.TYPE_AUTH;
		
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList()) {
			String sTempParentAuthCode = oPosPaymentGatewayTrans.getParentAuthCode();
			String sTempAuthType = oPosPaymentGatewayTrans.getType();
			
			// grouped by 2 auth type : (1) TYPE_AUTH + TYPE_TOPUP_AUTH (2) TYPE_COMPLETE_AUTH
			if (sTempAuthType.equals(PosPaymentGatewayTransactions.TYPE_AUTH) || sTempAuthType.equals(PosPaymentGatewayTransactions.TYPE_TOPUP_AUTH))
				sTempAuthType = PosPaymentGatewayTransactions.TYPE_AUTH;
			
			if (sTempParentAuthCode.equals(sParentAuthCode) && sTempAuthType.equals(sAuthType)) {
				dTempAmount = oPosPaymentGatewayTrans.getAmount();
				dTempAmount = dTempAmount.add(dAddAmount);
				oPosPaymentGatewayTrans.setAmount(dTempAmount);
				// update the latest auth_code when grouping the gateway transaction node
				oPosPaymentGatewayTrans.setAuthCode(sAuthCode);
				break;
			}
		}
		
		return;
	}
	
	// count the remain active node in the list
	public int getRemainActivePaymentGatewayTransactionsCount(){
		int iRetCount=0;
		
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList()) {
			if (!oPosPaymentGatewayTrans.getDefaultPayAdded()  && oPosPaymentGatewayTrans.getStatus().equals(PosPaymentGatewayTransactions.STATUS_ACTIVE))
				iRetCount++;
		}
		
		return iRetCount;
	}
	
	// count the active node in the list
	public int getActivePaymentGatewayTransactionsCount(){
		int iCount=0;
		
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: m_oPaymentGatewayTransactionsList)
			if (!oPosPaymentGatewayTrans.getStatus().equals(PosPaymentGatewayTransactions.STATUS_VOIDED))
				iCount++;
		
		return iCount;
	}
	
	// show the payment gateway node in the transaction list
//	public void showActivePaymentGatewayTransactionsNode(){
//		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList())
//			System.out.println("PgtxPayId="+oPosPaymentGatewayTrans.getPgtxPayId()+":MaskedPan="+oPosPaymentGatewayTrans.getMaskedPan()+":Amount="+oPosPaymentGatewayTrans.getAmount()+":DefaultPayAdded="+oPosPaymentGatewayTrans.getDefaultPayAdded()+" getPgtxId="+oPosPaymentGatewayTrans.getPgtxId()+" getParentAuthCode="+oPosPaymentGatewayTrans.getParentAuthCode()+" getAuthCode="+oPosPaymentGatewayTrans.getAuthCode());
//	}
	
	// get the payment gateway transaction node by PgtxPayId
	public PosPaymentGatewayTransactions getPosPaymentGatewayTransactionByPgtxPayId(int iPgtxPayId) {
		PosPaymentGatewayTransactions oRetTrans = new PosPaymentGatewayTransactions();
		
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList()) {
			int iTempPgtxPayId = oPosPaymentGatewayTrans.getPgtxPayId();
			if (iTempPgtxPayId == iPgtxPayId) {
				oRetTrans = oPosPaymentGatewayTrans;
				break;
			}
		}
		
		return oRetTrans;
	}
	
	// check the payment gateway transaction exist by PgtxPayId
	public boolean isPosPaymentGatewayTransactionExistByPgtxPayId(int iPgtxPayId) {
		for (PosPaymentGatewayTransactions oPosPaymentGatewayTrans: this.getPosPaymentGatewayTransactionsList()) {
			if (iPgtxPayId == oPosPaymentGatewayTrans.getPgtxPayId())
				return true;
		}
		
		return false;
	}
	
	// get payment gateway transaction by cpayId (pos_check_payment record id)
	public PosPaymentGatewayTransactions getPosPaymentGatewayTransactionByCpayId(String sCpayId){
		for(PosPaymentGatewayTransactions oPaymentGatewayTransaction : m_oPaymentGatewayTransactionsList){
			if(oPaymentGatewayTransaction.getCpayId().equals(sCpayId))
				return oPaymentGatewayTransaction;
		}
		return null;
	}
}
