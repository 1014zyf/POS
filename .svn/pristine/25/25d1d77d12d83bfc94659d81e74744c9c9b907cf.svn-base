package app.commonui;

import java.util.ArrayList;
import java.util.HashMap;

import app.controller.AppGlobal;

import core.templatebuilder.TemplateBuilder;
import core.virtualui.VirtualUIBasicElement;
import core.virtualui.VirtualUIFrame;
import core.virtualui.VirtualUIImage;
import core.virtualui.VirtualUILabel;

public class FrameNumberPad extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameEnter;
	private VirtualUILabel m_oLabelEnter;
	private VirtualUIFrame m_oFrameCancel;
	private VirtualUIFrame m_oFrameBackspace;
	private VirtualUIFrame m_oFrameDot;
	private HashMap<Integer, VirtualUIFrame> m_oFrameNumbers;

	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameNumberPadListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameNumberPadListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameNumberPadListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
    public FrameNumberPad(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameNumberPadListener>();
		m_oFrameNumbers = new HashMap<Integer, VirtualUIFrame>();
	}
	
    public void init(){
    	int iSpacing = 2;
    	// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraNumberPad.xml");
		
		int iButtonWidth = this.getWidth() / 4;
		int iButtonHeight = this.getHeight() / 4;
		
		VirtualUIFrame oFrame = new VirtualUIFrame();
		VirtualUILabel oLabel = new VirtualUILabel();
		VirtualUIImage oImage = new VirtualUIImage();
		// 7 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(0+iSpacing);
		oFrame.setLeft(0+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$17<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$17<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(7, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("7");
		oFrame.attachChild(oLabel);
		
		// 8 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(0+iSpacing);
		oFrame.setLeft(iButtonWidth+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$18<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$18<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(8, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("8");
		oFrame.attachChild(oLabel);
		
		// 9 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(0+iSpacing);
		oFrame.setLeft(iButtonWidth * 2+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$19<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$19<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(9, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("9");
		oFrame.attachChild(oLabel);
		
		// Cancel button
		m_oFrameCancel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCancel, "fraCancel");
		m_oFrameCancel.setTop(0+iSpacing);
		m_oFrameCancel.setLeft(iButtonWidth * 3+iSpacing);
		m_oFrameCancel.setWidth(iButtonWidth-iSpacing*2);
		m_oFrameCancel.setHeight(iButtonHeight * 2-iSpacing*2);
		m_oFrameCancel.allowClick(true);
		m_oFrameCancel.setClickServerRequestBlockUI(false);
		m_oFrameCancel.allowLongClick(true);
		m_oFrameCancel.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameCancel);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblCancel");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight * 2);
		oLabel.setValue(AppGlobal.g_oLang.get()._("cancel"));
		m_oFrameCancel.attachChild(oLabel);
		
		// 4 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight+iSpacing);
		oFrame.setLeft(0+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$14<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$14<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(4, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("4");
		oFrame.attachChild(oLabel);
		
		// 5 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight+iSpacing);
		oFrame.setLeft(iButtonWidth+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$15<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$15<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(5, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("5");
		oFrame.attachChild(oLabel);
		
		// 6 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight+iSpacing);
		oFrame.setLeft(iButtonWidth * 2+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$16<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$16<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(6, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("6");
		oFrame.attachChild(oLabel);
		
		// 1 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight * 2+iSpacing);
		oFrame.setLeft(0+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$11<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$11<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(1, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("1");
		oFrame.attachChild(oLabel);
		
		// 2 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight * 2+iSpacing);
		oFrame.setLeft(iButtonWidth+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$12<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$12<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(2, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("2");
		oFrame.attachChild(oLabel);
		
		// 3 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight * 2+iSpacing);
		oFrame.setLeft(iButtonWidth * 2+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$13<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$13<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(3, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("3");
		oFrame.attachChild(oLabel);
		
		// Enter button
		m_oFrameEnter = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameEnter, "fraEnter");
		m_oFrameEnter.setTop(iButtonHeight * 2+iSpacing);
		m_oFrameEnter.setLeft(iButtonWidth * 3+iSpacing);
		m_oFrameEnter.setWidth(iButtonWidth-iSpacing*2);
		m_oFrameEnter.setHeight(iButtonHeight * 2-iSpacing*2);
		m_oFrameEnter.allowClick(true);
		m_oFrameEnter.setClickServerRequestBlockUI(false);
		m_oFrameEnter.allowLongClick(true);
		m_oFrameEnter.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameEnter);
		m_oLabelEnter = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelEnter, "lblEnter");
		m_oLabelEnter.setTop(0);
		m_oLabelEnter.setLeft(0);
		m_oLabelEnter.setWidth(iButtonWidth);
		m_oLabelEnter.setHeight(iButtonHeight * 2);
		m_oLabelEnter.setValue(AppGlobal.g_oLang.get()._("enter"));
		m_oFrameEnter.attachChild(m_oLabelEnter);
		
		// Backspace button
		m_oFrameBackspace = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBackspace, "fraBackspace");
		m_oFrameBackspace.setTop(iButtonHeight * 3+iSpacing);
		m_oFrameBackspace.setLeft(0+iSpacing);
		m_oFrameBackspace.setWidth(iButtonWidth-iSpacing*2);
		m_oFrameBackspace.setHeight(iButtonHeight-iSpacing*2);
		m_oFrameBackspace.allowClick(true);
		m_oFrameBackspace.setClickReplaceValue("^(.*).<select></select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setClickReplaceValue("^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setClickServerRequestBlockUI(false);
		m_oFrameBackspace.allowLongClick(true);
		m_oFrameBackspace.setLongClickReplaceValue("^(.*).<select></select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setLongClickReplaceValue("^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameBackspace);
		/*
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblBackspace");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("<-");
		m_oFrameBackspace.attachChild(oLabel);
		*/
		oImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImage, "ImgBackspace");
		// Image size 36 x 23
		oImage.setExist(true);
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/backspace_button.png");
		int iTop = (m_oFrameBackspace.getHeight() / 2) - 12;
		if(iTop < 0)
			iTop = 0;
		oImage.setTop(iTop);
		int iLeft = (m_oFrameBackspace.getWidth() / 2) - 18;
		if(iLeft < 0)
			iLeft = 0;
		oImage.setLeft(iLeft);
		oImage.setWidth(36);
		oImage.setHeight(23);
		m_oFrameBackspace.attachChild(oImage);
		
		// 0 button
		oFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
		oFrame.setTop(iButtonHeight * 3+iSpacing);
		oFrame.setLeft(iButtonWidth+iSpacing);
		oFrame.setWidth(iButtonWidth-iSpacing*2);
		oFrame.setHeight(iButtonHeight-iSpacing*2);
		oFrame.allowClick(true);
		oFrame.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$10<select></select>$2");
		oFrame.setClickServerRequestBlockUI(false);
		oFrame.allowLongClick(true);
		oFrame.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$10<select></select>$2");
		oFrame.setLongClickServerRequestBlockUI(false);
		this.attachChild(oFrame);
		m_oFrameNumbers.put(0, oFrame);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue("0");
		oFrame.attachChild(oLabel);
		
		// Dot button
		m_oFrameDot = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameDot, "fraDot");
		m_oFrameDot.setTop(iButtonHeight * 3+iSpacing);
		m_oFrameDot.setLeft(iButtonWidth * 2+iSpacing);
		m_oFrameDot.setWidth(iButtonWidth-iSpacing*2);
		m_oFrameDot.setHeight(iButtonHeight-iSpacing*2);
		m_oFrameDot.allowClick(true);
		m_oFrameDot.setClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$1\\.<select></select>$2");
		m_oFrameDot.setClickServerRequestBlockUI(false);
		m_oFrameDot.allowLongClick(true);
		m_oFrameDot.setLongClickReplaceValue("^(.*)<select>.*</select>(.*)$", "$1\\.<select></select>$2");
		m_oFrameDot.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oFrameDot);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblDot");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue(".");
		m_oFrameDot.attachChild(oLabel);
    }
    
    public void setEnterSubmitId(VirtualUIBasicElement oElement){
    	m_oFrameEnter.addClickServerRequestSubmitElement(oElement);
    	m_oFrameEnter.addLongClickServerRequestSubmitElement(oElement);
    }
    
    public void clearEnterSubmitId(){
    	m_oFrameEnter.clearClickServerRequestSubmitId();
    	m_oFrameEnter.clearLongClickServerRequestSubmitId();
    }
    
    public void setEnterBlockUI(boolean bBlockUI){
    	m_oFrameEnter.setClickServerRequestBlockUI(bBlockUI);
    	m_oFrameEnter.setLongClickServerRequestBlockUI(bBlockUI);
    }
    
    public void setEnterDesc(String sDesc){
    	m_oLabelEnter.setValue(sDesc);
    }
    
    public void supportNumberReplaceValueWithClickEvent() {
        for(int i = 0; i < m_oFrameNumbers.size(); i++) {
        	m_oFrameNumbers.get(i).supportReplaceValueWithClickEvent(true);
        }
    }
    
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oFrameEnter.getId() == iChildId){
			for (FrameNumberPadListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameNumberPad_clickEnter();
	       		bMatchChild = true;
	       		break;
    		}
        }else
    	if(m_oFrameCancel.getId() == iChildId){
			for (FrameNumberPadListener listener : listeners) {
				// Raise the event to parent
	       		listener.FrameNumberPad_clickCancel();
	       		bMatchChild = true;
	       		break;
    		}
        }else {
	        for(int i = 0; i < m_oFrameNumbers.size(); i++) {
	            if(m_oFrameNumbers.get(i).getId() == iChildId) {
	    			for (FrameNumberPadListener listener : listeners) {
	    				listener.FrameNumberPad_clickNumber(m_oFrameNumbers.get(i).getChilds().get(0).getValue());
	    				bMatchChild = true;
	    				break;
	    			}
	            }
	        }
        }
    	
    	return bMatchChild;
    }
	
	@Override
	public boolean longClicked(int iChildId, String sNote) {
		return this.clicked(iChildId, sNote);
	}
	
	// ***** DEBUG *****
	public VirtualUIFrame getEnterButton(){
		return m_oFrameEnter;
	}
}
