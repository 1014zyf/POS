package app;

import core.Controller;

import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormEditTaiwanGUIConfig extends VirtualUIForm implements FrameEditTaiwanGUIConfigListener {

	TemplateBuilder m_oTemplateBuilder;
		
	private FrameEditTaiwanGUIConfig m_oFrameEditTaiwanGUIConfig;

	VirtualUIFrame m_oFrameCover;

	public FormEditTaiwanGUIConfig(Controller oParentController){
		super(oParentController);
	}
	
	public boolean init(){	
		m_oTemplateBuilder = new TemplateBuilder();
		
		m_oTemplateBuilder.loadTemplate("frmEditTaiwanGUIConfig.xml");
		
		// Background Cover Page
		m_oFrameCover = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCover, "fraCoverFrame");
		m_oFrameCover.setVisible(false);
		this.attachChild(m_oFrameCover);
		
		m_oFrameEditTaiwanGUIConfig = new FrameEditTaiwanGUIConfig();
		m_oTemplateBuilder.buildFrame(m_oFrameEditTaiwanGUIConfig, "fraEditTaiwanGUIConfig");
		m_oFrameEditTaiwanGUIConfig.addListener(this);
		this.attachChild(m_oFrameEditTaiwanGUIConfig);
		
		return true;
	}


	@Override
	public void frameEditTaiwanGUIConfig_clickExit() {
		// Finish showing this form
		this.finishShow();
	}

}