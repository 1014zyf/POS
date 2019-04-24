package app;

import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import commonui.FormDialogBox;
import core.Controller;
import om.PreorderCheck;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormPreOrderList extends VirtualUIForm implements FramePreOrderListListener{
	TemplateBuilder m_oTemplateBuilder;
	
	private FramePreOrderList m_oFramePreOrderList;
	
	private String m_sTableNo;
	private String m_sTableExtension;
	private boolean m_bUserCancel;
	private boolean m_bUsedRecordTagActive;
	private JSONArray m_oNonUsePreOrderList;
	private JSONArray m_oUsedPreOrderList;
	private JSONObject m_oChosenPreOrder;
	private DateTimeFormatter m_oDTFormatterYMDHIS;
	
	private int RETRIEVE_MAX_PREORDER = 0;
	
	public FormPreOrderList(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("frmPreOrderList.xml");
		
		m_sTableNo = "";
		m_sTableExtension = "";
		m_bUserCancel = false;
		m_bUsedRecordTagActive = false;
		m_oNonUsePreOrderList = null;
		m_oUsedPreOrderList = null;
		m_oChosenPreOrder = null;
	}
	
	public boolean init(String sTable, String sTableExtension) {
		m_sTableNo = sTable;
		m_sTableExtension = sTableExtension;
		m_oDTFormatterYMDHIS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		
		VirtualUIFrame oCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCover, "fraCoverFrame");
		this.attachChild(oCover);
		
		m_oFramePreOrderList = new FramePreOrderList();
		m_oTemplateBuilder.buildFrame(m_oFramePreOrderList, "fraPreOrderList");
		m_oFramePreOrderList.init(m_sTableNo, m_sTableExtension);
		m_oFramePreOrderList.addListener(this);
		this.attachChild(m_oFramePreOrderList);
		
		// get non-read pre-order by table number
		PreorderCheck oPreorderCheck = new PreorderCheck();
		m_oNonUsePreOrderList = oPreorderCheck.retrievePreorderByTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), m_sTableNo, sTableExtension, RETRIEVE_MAX_PREORDER, false);
		
		if (m_oNonUsePreOrderList != null && m_oNonUsePreOrderList.length() > 0) {
			for (int i = 0; i < m_oNonUsePreOrderList.length(); i++) {
				JSONObject oNonUsePreOrderJSONObject = m_oNonUsePreOrderList.optJSONObject(i);
				if (oNonUsePreOrderJSONObject == null)
					continue;
				
				HashMap<String, String> oPreOrder = new HashMap<String, String>();
				String sCreateTime = "";
				if (oNonUsePreOrderJSONObject.has("create_time") && !oNonUsePreOrderJSONObject.isNull("create_time"))
					sCreateTime = getCreateTimeString(oNonUsePreOrderJSONObject.optString("create_time"));
				oPreOrder.put("createTime", sCreateTime);
				oPreOrder.put("refno", oNonUsePreOrderJSONObject.optString("refno"));
				if(oNonUsePreOrderJSONObject.has("ordering_type") && oNonUsePreOrderJSONObject.optString("ordering_type").equals("t"))
					oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("yes"));
				else
					oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("no"));
				m_oFramePreOrderList.addPreOrderToList(0, i, oPreOrder);
			}
		}else {
			m_oUsedPreOrderList = oPreorderCheck.retrievePreorderByTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), m_sTableNo, m_sTableExtension, RETRIEVE_MAX_PREORDER, true);
			
			if(m_oUsedPreOrderList != null && m_oUsedPreOrderList.length() > 0) {
				m_oFramePreOrderList.cleanupPreOrderList();
				for (int i = 0; i < m_oUsedPreOrderList.length(); i++) {
					JSONObject oUsedPreOrderJSONObject = m_oUsedPreOrderList.optJSONObject(i);
					HashMap<String, String> oPreOrder = new HashMap<String, String>();
					String sCreateTime = "";
					if (oUsedPreOrderJSONObject.has("create_time") && !oUsedPreOrderJSONObject.isNull("create_time"))
						sCreateTime = getCreateTimeString(oUsedPreOrderJSONObject.optString("create_time"));
					oPreOrder.put("createTime", sCreateTime);
					oPreOrder.put("refno", oUsedPreOrderJSONObject.optString("refno"));
					if(oUsedPreOrderJSONObject.has("ordering_type") && oUsedPreOrderJSONObject.optString("ordering_type").equals("t"))
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("yes"));
					else
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("no"));
					m_oFramePreOrderList.addPreOrderToList(0, i, oPreOrder);
				}
				
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_new_pre_order_found"));
				oFormDialogBox.show();
				
				m_oFramePreOrderList.switchTag(2);
				m_bUsedRecordTagActive = true;
			}else {
				framePreOrderList_clickBack();
				FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
				oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("attention"));
				oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_pre_order_found"));
				oFormDialogBox.show();
				return false;
			}
		}
		
		return true;
	}
	
	private String getCreateTimeString(String sCreateDateTime) {
		String sCreateTime = "";
		try{
			DateTime dtCreateTime = AppGlobal.convertTimeToLocal(m_oDTFormatterYMDHIS.withZoneUTC().parseDateTime(sCreateDateTime));
			sCreateTime = dtCreateTime.toString(m_oDTFormatterYMDHIS);
		} catch (Exception e) {}
		return sCreateTime;
	}
	
	public boolean isUserCancel() {
		return m_bUserCancel;
	}
	
	public JSONObject getChosenPreOrder() {
		return m_oChosenPreOrder;
	}
	
	/***********************/
	/*  Override Function  */
	/***********************/
	@Override
	public void framePreOrderList_clickBack() {
		m_bUserCancel = true;
		this.finishShow();
	}
	
	@Override
	public void framePreOrderList_updatePreOrderList(boolean bUsedRecord) {
		m_bUsedRecordTagActive = bUsedRecord;
		PreorderCheck oPreorderCheck = new PreorderCheck();
		
		m_oFramePreOrderList.cleanupPreOrderList();
		if (bUsedRecord && m_oUsedPreOrderList == null)
			m_oUsedPreOrderList = oPreorderCheck.retrievePreorderByTable(AppGlobal.g_oFuncOutlet.get().getOutletId(), m_sTableNo, m_sTableExtension, RETRIEVE_MAX_PREORDER, bUsedRecord);
		
		if (bUsedRecord) {
			if (m_oUsedPreOrderList != null && m_oUsedPreOrderList.length() > 0) {
				for (int i = 0; i < m_oUsedPreOrderList.length(); i++) {
					if (m_oUsedPreOrderList.isNull(i))
						continue;
					
					JSONObject oUsedPreOrderJSONObject = m_oUsedPreOrderList.optJSONObject(i);
					HashMap<String, String> oPreOrder = new HashMap<String, String>();
					String sCreateTime = "";
					if (oUsedPreOrderJSONObject.has("create_time") && !oUsedPreOrderJSONObject.isNull("create_time"))
						sCreateTime = getCreateTimeString(oUsedPreOrderJSONObject.optString("create_time"));
					oPreOrder.put("createTime", sCreateTime);
					oPreOrder.put("refno", oUsedPreOrderJSONObject.optString("refno"));
					if(oUsedPreOrderJSONObject.has("ordering_type") && oUsedPreOrderJSONObject.optString("ordering_type").equals("t"))
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("yes"));
					else
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("no"));
					m_oFramePreOrderList.addPreOrderToList(0, i, oPreOrder);
				}
			}
			
		}else {
			if (m_oNonUsePreOrderList != null && m_oNonUsePreOrderList.length() > 0) {
				for (int i = 0; i < m_oNonUsePreOrderList.length(); i++) {
					if (m_oNonUsePreOrderList.isNull(i))
						continue;
					
					JSONObject oNonUsePreOrderJSONObject = m_oNonUsePreOrderList.optJSONObject(i);
					HashMap<String, String> oPreOrder = new HashMap<String, String>();
					String sCreateTime = "";
					if (oNonUsePreOrderJSONObject.has("create_time") && !oNonUsePreOrderJSONObject.isNull("create_time"))
						sCreateTime = getCreateTimeString(oNonUsePreOrderJSONObject.optString("create_time"));
					oPreOrder.put("createTime", sCreateTime);
					oPreOrder.put("refno", oNonUsePreOrderJSONObject.optString("refno"));
					if(oNonUsePreOrderJSONObject.has("ordering_type") && oNonUsePreOrderJSONObject.optString("ordering_type").equals("t"))
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("yes"));
					else
						oPreOrder.put("takeout", AppGlobal.g_oLang.get()._("no"));
					m_oFramePreOrderList.addPreOrderToList(0, i, oPreOrder);
				}
			}
		}
	}
	
	@Override
	public void framePreOrderList_clickPreOrder(int iPreOrderIndex) {
		 if (!m_bUsedRecordTagActive)
			 m_oChosenPreOrder = m_oNonUsePreOrderList.optJSONObject(iPreOrderIndex);
		 else
			 m_oChosenPreOrder = m_oUsedPreOrderList.optJSONObject(iPreOrderIndex);
		 
		 this.finishShow();
	}
}
