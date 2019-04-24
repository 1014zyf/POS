package app;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import commonui.FormDialogBox;
import core.Controller;
import om.PosActionLog;
import om.PosReport;
import om.PrtPrintJob;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;
import virtualui.VirtualUIFrame;

public class FormDirectReport extends VirtualUIForm implements FrameDirectReportListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameDirectReport m_oFrameDirectReport;	
	private List<HashMap<String, String>> m_oDirectReportType;
	private String m_sReportPrintUrl;
	private String m_sPrintingMediaType;
	
	public FormDirectReport(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		m_oDirectReportType = new ArrayList<HashMap<String, String>>();
		m_sReportPrintUrl = "";
		m_sPrintingMediaType = "";
		
		// Form a list of report type
		HashMap<String, String> oReportTypeHashMap = new HashMap<String, String>();
		oReportTypeHashMap.put("Name", AppGlobal.g_oLang.get()._("revenue_report"));
		oReportTypeHashMap.put("type", "revenue_report");
		m_oDirectReportType.add(oReportTypeHashMap);
		
		oReportTypeHashMap = new HashMap<String, String>();
		oReportTypeHashMap.put("Name", AppGlobal.g_oLang.get()._("payment_and_discount_report"));
		oReportTypeHashMap.put("type", "cashier_report");
		m_oDirectReportType.add(oReportTypeHashMap);
		
		m_oTemplateBuilder.loadTemplate("frmDirectReport.xml");

		// Background Cover Page
		VirtualUIFrame oCoverFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oCoverFrame, "fraCoverFrame");
		this.attachChild(oCoverFrame);
		
//JohnLiu 06112017 -- start
		// Direct report frame
		m_oFrameDirectReport = new FrameDirectReport(m_oDirectReportType);
		m_oTemplateBuilder.buildFrame(m_oFrameDirectReport, "fraDirectReport");
		m_oFrameDirectReport.addListener(this);
		this.attachChild(m_oFrameDirectReport);
//JohnLiu 06112017 -- end
	}
	
	public void generateReport(String sReportType) {
		PosReport oPosReport = new PosReport();
		HashMap<String, String> oReportDetails = oPosReport.generateDirectReport(AppGlobal.g_oFuncOutlet.get().getOutletId(), AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oCurrentLangIndex.get(), sReportType);
		String sURL = "";
		if(oReportDetails.containsKey("url"))
			sURL = oReportDetails.get("url");
		
		if(sURL.contentEquals("")) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("cannot_generate_report"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			
			m_oFrameDirectReport.setReportURL("");
			return;
		}
		
		m_sReportPrintUrl = oReportDetails.get("printUrl");
		m_sPrintingMediaType = oReportDetails.get("mediaType");
		m_oFrameDirectReport.setReportURL(sURL);
		
		// Add log to action log list
		AppGlobal.g_oActionLog.get().addActionLog(AppGlobal.FUNC_LIST.direct_report.name(), PosActionLog.ACTION_RESULT_SUCCESS, "", AppGlobal.g_oFuncUser.get().getUserId(), AppGlobal.g_oFuncOutlet.get().getShopId(), AppGlobal.g_oFuncOutlet.get().getOutletId() , AppGlobal.g_oFuncOutlet.get().getBusinessDay().getBdayId(), AppGlobal.g_oFuncOutlet.get().getBusinessPeriod().getBperId(), AppGlobal.g_oFuncStation.get().getStationId(), "", "", "", "", "", "");
		//handle action log
		AppGlobal.g_oActionLog.get().handleActionLog(false);
	}
	
	@Override
	public void frameDirectReport_clickExit() {
		// Finish showing this form 
		this.finishShow();
	}
	
	@Override
	public void frameDirectReport_clickPrint() {
		if(AppGlobal.g_oFuncStation.get().getReportSlipPrtqId() == 0) {
			FormDialogBox oFormDialogBox = new FormDialogBox(AppGlobal.g_oLang.get()._("ok"), this);
			oFormDialogBox.setTitle(AppGlobal.g_oLang.get()._("error"));
			oFormDialogBox.setMessage(AppGlobal.g_oLang.get()._("no_print_queue_is_defined"));
			oFormDialogBox.show();
			oFormDialogBox = null;
			return;
		}
		
		PrtPrintJob oPrintJob = new PrtPrintJob();
		oPrintJob.addUpdate(AppGlobal.g_oFuncStation.get().getReportSlipPrtqId(), m_sReportPrintUrl, m_sPrintingMediaType, PrtPrintJob.FILE_TYPE_OTHER);
		this.finishShow();
	}
	
	@Override
	public void frameDirectReport_clickReport(int iReportIndex) {
		generateReport(m_oDirectReportType.get(iReportIndex).get("type"));
	}
}
