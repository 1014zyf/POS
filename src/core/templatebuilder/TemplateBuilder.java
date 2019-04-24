package core.templatebuilder;

import core.virtualui.*;
import java.io.*;

import app.AppGlobal;

import nu.xom.*;

public class TemplateBuilder {
	
	private Element m_oRootElement;
	
	public void loadTemplate(String sTemplateFile){
		try {
			// Load the template file to xml variable
			String templateDir = "";
			
			// default use android tablet
			if (AppGlobal.g_sDisplayMode.get() != null && AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				templateDir = System.getProperty("user.dir") + java.io.File.separator + "cfg" + java.io.File.separator + "template" + java.io.File.separator + "mobile";
			else
				templateDir = System.getProperty("user.dir") + java.io.File.separator + "cfg" + java.io.File.separator + "template";
			
			File xmlFile = new File(templateDir + java.io.File.separator + sTemplateFile);
			
			// skip if target xml file not found
			if (!xmlFile.exists())
				return;
			
			Builder builder = new Builder();
			Document doc = builder.build(xmlFile);
			
			//<POS>
			Element pos = doc.getRootElement();
			//<TEMPLATE>
			Element template = pos.getFirstChildElement("TEMPLATE");
			//<FORM> / <FRAME> / <BUTTON>
			Elements elements = template.getChildElements();
			m_oRootElement = elements.get(0);
			
		}
		catch (ValidityException ve) {
			// TODO Auto-generated catch block
			ve.printStackTrace();
		}
		catch (ParsingException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
	
	public void setupBasicElement(VirtualUIBasicElement oObject, Element node) {
		oObject.setExist(true);
		
		oObject.setTop(0);
		if(node.getAttributeValue("top") != null) {
			oObject.setTop(Integer.parseInt(node.getAttributeValue("top")));
		}
		oObject.setLeft(0);
		if(node.getAttributeValue("left") != null) {
			oObject.setLeft(Integer.parseInt(node.getAttributeValue("left")));
		}
		oObject.setWidth(0);
		if(node.getAttributeValue("width") != null) {
			oObject.setWidth(Integer.parseInt(node.getAttributeValue("width")));
		}
		oObject.setHeight(0);
		if(node.getAttributeValue("height") != null) {
			oObject.setHeight(Integer.parseInt(node.getAttributeValue("height")));
		}
		oObject.setPaddingValue("");
		if(node.getAttributeValue("padding") != null) {
			oObject.setPaddingValue(node.getAttributeValue("padding"));
		}
		oObject.setBackgroundColor("");
		if(node.getAttributeValue("background") != null) {
			oObject.setBackgroundColor(node.getAttributeValue("background"));
		}
		oObject.setForegroundColor("");
		if(node.getAttributeValue("foreground") != null) {
			oObject.setForegroundColor(node.getAttributeValue("foreground"));
		}
		oObject.setStroke(0);
		if(node.getAttributeValue("stroke") != null) {
			oObject.setStroke(Integer.parseInt(node.getAttributeValue("stroke")));
		}
		oObject.setStrokeColor("");
		if(node.getAttributeValue("strokeColor") != null) {
			oObject.setStrokeColor(node.getAttributeValue("strokeColor"));
		}
	}
	
	public boolean buildFrame(VirtualUIFrame oFrame, String sFrameName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("FRAME");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sFrameName)) {
				this.setupBasicElement(oFrame, node);
				
				oFrame.setGradient(false);
				if(node.getAttributeValue("gradient") != null) {
					if(Integer.parseInt(node.getAttributeValue("gradient")) == 1)
						oFrame.setGradient(true);
				}
				
				oFrame.setCornerRadius("");
				if(node.getAttributeValue("cornerRadius") != null) {
					oFrame.setCornerRadius(node.getAttributeValue("cornerRadius"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildButton(VirtualUIButton oButton, String sButtonName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("BUTTON");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sButtonName)) {
				this.setupBasicElement(oButton, node);

				oButton.setValue("");
				if(node.getAttributeValue("value") != null) {
					oButton.setValue(node.getAttributeValue("value"));
				}
				oButton.setTextAlign("");
				if(node.getAttributeValue("textAlign") != null) {
					oButton.setTextAlign(node.getAttributeValue("textAlign"));
				}
				oButton.setTextSize(0);
				if(node.getAttributeValue("textSize") != null) {
					oButton.setTextSize(Integer.parseInt(node.getAttributeValue("textSize")));
				}
				oButton.setTextStyle("");
				if(node.getAttributeValue("textStyle") != null) {
					oButton.setTextStyle(node.getAttributeValue("textStyle"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildLabel(VirtualUILabel oLabel, String sLabelName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("LABEL");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sLabelName)) {
				this.setupBasicElement(oLabel, node);

				oLabel.setValue("");
				if(node.getAttributeValue("value") != null) {
					oLabel.setValue(node.getAttributeValue("value"));
				}
				oLabel.setTextAlign("");
				if(node.getAttributeValue("textAlign") != null) {
					oLabel.setTextAlign(node.getAttributeValue("textAlign"));
				}
				oLabel.setTextSize(0);
				if(node.getAttributeValue("textSize") != null) {
					oLabel.setTextSize(Integer.parseInt(node.getAttributeValue("textSize")));
				}
				oLabel.setTextStyle("");
				if(node.getAttributeValue("textStyle") != null) {
					oLabel.setTextStyle(node.getAttributeValue("textStyle"));
				}
				oLabel.setGradient(false);
				if(node.getAttributeValue("gradient") != null) {
					if(Integer.parseInt(node.getAttributeValue("gradient")) == 1)
						oLabel.setGradient(true);
				}
				oLabel.setCornerRadius("");
				if(node.getAttributeValue("cornerRadius") != null) {
					oLabel.setCornerRadius(node.getAttributeValue("cornerRadius"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}

	public boolean buildClockLabel(VirtualUIClockLabel oClockLabel, String sLabelName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("CLOCKLABEL");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sLabelName)) {
				this.setupBasicElement(oClockLabel, node);

				oClockLabel.setValue("");
				if(node.getAttributeValue("value") != null) {
					oClockLabel.setValue(node.getAttributeValue("value"));
				}
				oClockLabel.setTextAlign("");
				if(node.getAttributeValue("textAlign") != null) {
					oClockLabel.setTextAlign(node.getAttributeValue("textAlign"));
				}
				oClockLabel.setTextSize(0);
				if(node.getAttributeValue("textSize") != null) {
					oClockLabel.setTextSize(Integer.parseInt(node.getAttributeValue("textSize")));
				}
				oClockLabel.setTextStyle("");
				if(node.getAttributeValue("textStyle") != null) {
					oClockLabel.setTextStyle(node.getAttributeValue("textStyle"));
				}
				oClockLabel.setGradient(false);
				if(node.getAttributeValue("gradient") != null) {
					if(Integer.parseInt(node.getAttributeValue("gradient")) == 1)
						oClockLabel.setGradient(true);
				}
				oClockLabel.setCornerRadius("");
				if(node.getAttributeValue("cornerRadius") != null) {
					oClockLabel.setCornerRadius(node.getAttributeValue("cornerRadius"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildList(VirtualUIList oList, String sListName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("LIST");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sListName)) {
				this.setupBasicElement(oList, node);
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildHorizontalList(VirtualUIHorizontalList oHorList, String sListName) {
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("HORIZONTALLIST");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sListName)) {
				this.setupBasicElement(oHorList, node);
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildTextbox(VirtualUITextbox oTxtbox, String sTxtboxName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("TEXTBOX");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sTxtboxName)) {
				this.setupBasicElement(oTxtbox, node);
				
				oTxtbox.setTextSize(0);
				if(node.getAttributeValue("textSize") != null) {
					oTxtbox.setTextSize(Integer.parseInt(node.getAttributeValue("textSize")));
				}
				oTxtbox.setCornerRadius("");
				if(node.getAttributeValue("cornerRadius") != null) {
					oTxtbox.setCornerRadius(node.getAttributeValue("cornerRadius"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildScrollFrame(VirtualUIScrollFrame oScrollFrame, String sScrollFrameName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("SCROLLFRAME");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sScrollFrameName)) {
				this.setupBasicElement(oScrollFrame, node);

				oScrollFrame.setContentWidth(0);
				if(node.getAttributeValue("contentWidth") != null) {
					oScrollFrame.setContentWidth(Integer.parseInt(node.getAttributeValue("contentWidth")));
				}
				oScrollFrame.setContentHeight(0);
				if(node.getAttributeValue("contentHeight") != null) {
					oScrollFrame.setContentHeight(Integer.parseInt(node.getAttributeValue("contentHeight")));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildImage(VirtualUIImage oImage, String sImageName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("IMAGE");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sImageName)) {
				this.setupBasicElement(oImage, node);

				oImage.setSource("");
				if(node.getAttributeValue("source") != null) {
					oImage.setSource(node.getAttributeValue("source"));
				}
				oImage.setContentMode("");
				if(node.getAttributeValue("contentMode") != null) {
					oImage.setContentMode(node.getAttributeValue("contentMode"));
				}
				
				oImage.setCornerRadius("");
				if(node.getAttributeValue("cornerRadius") != null) {
					oImage.setCornerRadius(node.getAttributeValue("cornerRadius"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
	
	public boolean buildWebView(VirtualUIWebView oWebView, String sImageName){
		if (m_oRootElement == null)
			return false;
		
		Elements elements = m_oRootElement.getChildElements("WEBVIEW");
		for(int m = 0; m < elements.size(); m++) {
			Element node = elements.get(m);
			if(node.getAttributeValue("class").equals(sImageName)) {
				this.setupBasicElement(oWebView, node);

				oWebView.setSource("");
				if(node.getAttributeValue("source") != null) {
					oWebView.setSource(node.getAttributeValue("source"));
				}
				// quit looping if result found
				return true;
			}
		}
		return false;
	}
}
