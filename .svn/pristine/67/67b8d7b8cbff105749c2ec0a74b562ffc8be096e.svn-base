package virtualui;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

public class VirtualUIButton extends VirtualUIFrame {
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUILabel m_oLblText;
	private VirtualUIImage m_oImgBackground;
	
	public VirtualUIButton(){
		m_oTemplateBuilder = new TemplateBuilder();
		m_oTemplateBuilder.loadTemplate("fraButton.xml");
		
		this.setCornerRadius("2");
		
		m_oImgBackground = new VirtualUIImage();
		m_oImgBackground.setExist(true);
		m_oImgBackground.setVisible(false);
		m_oImgBackground.setContentMode("scale_aspect_fill");
		this.attachChild(m_oImgBackground);
		
		m_oLblText = new VirtualUILabel();
		m_oLblText.setExist(true);
		m_oLblText.setBackgroundColor("#00FFFFFF");
		this.attachChild(m_oLblText);
		this.allowClick(true);
	}
	
	public VirtualUILabel getLabel() {
		return m_oLblText;
	}
	
	@Override
	public void setForegroundColor(String sValue){
		m_oLblText.setForegroundColor(sValue);
	}
	
	@Override
	public void setBackgroundColor(String sValue){
		m_oImgBackground.setSource("");
		super.setBackgroundColor(sValue);
		// In order to replace the VirtualUIButton, need the following special handling 
		processButtonFeature();
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
	// Set value for LangResource
	public void setValue(String[] oValueList) {
		// In order to replace the VirtualUIButton, need the following special handling 
		processButtonFeature();
		
		// Set Value
		m_oLblText.setValue(oValueList);
	}
	
	@Override
	public void setValue(String sValue){
		// In order to replace the VirtualUIButton, need the following special handling 
		processButtonFeature();
		
		// Set Value
		m_oLblText.setValue(sValue);
	}
	
	void processButtonFeature() {
		m_oImgBackground.setWidth(this.getWidth());
		m_oImgBackground.setHeight(this.getHeight());
		
		m_oLblText.setWidth(this.getWidth());
		m_oLblText.setHeight(this.getHeight());
		
		String sImageName = "";
		if ((this.getWidth() == 68 && this.getHeight() == 40) ||
				(this.getWidth() == 100 && this.getHeight() == 40) ||
				(this.getWidth() == 116 && this.getHeight() == 40) ||
				(this.getWidth() == 124 && this.getHeight() == 40) ||
				(this.getWidth() == 136 && this.getHeight() == 96) ||
				(this.getWidth() == 140 && this.getHeight() == 58) ||
				(this.getWidth() == 144 && this.getHeight() == 56) ||
				(this.getWidth() == 150 && this.getHeight() == 58) ||
				(this.getWidth() == 172 && this.getHeight() == 58)) {
			sImageName = "btn_" + this.getWidth() + "_" + this.getHeight();
			
			switch (this.getBackgroundColor()) {
			case "#FFFFFF":
			case "#FFFFFFFF":
				sImageName = sImageName + "_w_n.png";
				break;
			case "#66A6F1":
			case "#FF66A6F1":
				sImageName = sImageName + "_w_h.png";
				break;
			case "#0055B8":
			case "#FF0055B8":
			case "#02428B":
			case "#FF02428B":
			case "#0461CD":
			case "#FF0461CD":
				sImageName = sImageName + "_b_n.png";
				break;
			case "#5B6F73":
			case "#FF5B6F73":
				sImageName = sImageName + "_g_h.png";
				break;
			case "#A0B3B7":
			case "#FFA0B3B7":
				sImageName = sImageName + "_g_n.png";
				break;
			case "#031E3E":
			case "#FF031E3E":
				sImageName = sImageName + "_b_h.png";
				break;
			default:
				sImageName = "";
			}
			
			if(!sImageName.isEmpty()) {
				m_oImgBackground.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/" + sImageName);
				m_oImgBackground.setVisible(true);
			}
		}
	}
}
