package app;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

public class FrameCover extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameBackground;
	private VirtualUILabel m_oLabelMessage;
	
	public FrameCover() {
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load child elements from template
		m_oTemplateBuilder.loadTemplate("fraCover.xml");
		
		m_oFrameBackground = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBackground, "fraBackground");
		this.attachChild(m_oFrameBackground);
		
		m_oLabelMessage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		this.attachChild(m_oLabelMessage);
	}
	
	public void setMessage(String sMessage) {
		m_oLabelMessage.setValue(sMessage);
	}
}
