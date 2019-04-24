package commonui;
//KingsleyKwan20171016NewCommonUIButtonByJack (new .java)
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

public class FrameButton extends VirtualUIFrame {
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUILabel m_oLblText;
	
	public FrameButton(){
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("fraButton.xml");
		
		m_oLblText = new VirtualUILabel();
		m_oLblText.setExist(true);
		this.attachChild(m_oLblText);
		this.allowClick(true);
	}
	
	@Override
	public void setForegroundColor(String sValue){
		m_oLblText.setForegroundColor(sValue);
	}
	
	@Override
	public void setTextSize(int iTextSize) {
		m_oLblText.setTextSize(iTextSize);
	}
	
	@Override
	public void setTextAlign(String sTextAlign) {
		// *** Text align for button always "center"
		m_oLblText.setTextAlign("center");
	}
	
	@Override
	public void setValue(String sValue){
		m_oLblText.setWidth(this.getWidth());
		m_oLblText.setHeight(this.getHeight());
		
		m_oLblText.setValue(sValue);
	}
//end KingsleyKwan20171016NewCommonUIButtonByJack
}
