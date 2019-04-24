package commonui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;

public class FrameQwertyKeyboard extends VirtualUIFrame {
	
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUIFrame m_oFrameEnter;
	private VirtualUILabel m_oLabelEnter;
	private VirtualUIFrame m_oFrameCancel;
	private VirtualUIFrame m_oFrameBackspace;
	private HashMap<String, VirtualUIFrame> m_oFrameNumbers;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameQwertyKeyboardListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameQwertyKeyboardListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameQwertyKeyboardListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameQwertyKeyboard(){	
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameQwertyKeyboardListener>();
		m_oFrameNumbers = new HashMap<String, VirtualUIFrame>();
	}

	public void init(){
		int iSpacing = 6;
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraQwertyKeyboard.xml");
		int iButtonWidth = 0;
		int iButtonHeight = 0;
		
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
			iButtonWidth = this.getWidth() / 10;
			iButtonHeight = this.getHeight() / 4;		
		}else if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			iButtonWidth = this.getWidth() / 5;
			iButtonHeight = this.getHeight() / 8;
		}

		
		VirtualUIFrame oFrame = new VirtualUIFrame();
		VirtualUILabel oLabel = new VirtualUILabel();
		VirtualUIImage oImage = new VirtualUIImage();
		
		List<String> oWholeAlphanumericList = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","0",
																			"A","B","C","D","E","F","G","H","I","J",
																			"K","L","M","N","O","P","Q","R","S",
																			"T","U","V","W","X","Y","Z"));
		
		
//		List<String> oWholeAlphanumericList = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","0",
//																			"Q","W","E","R","T","Y","U","I","O","P",
//																			"A","S","D","F","G","H","J","K","L",
//																			"Z","X","C","V","B","N","M"));
		
		int iTop = 0, iLeft = 0;
		for(int i=0; i<oWholeAlphanumericList.size(); i++){
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonHeight * iTop + iSpacing);
			oFrame.setLeft(iButtonWidth * iLeft + iSpacing);
			oFrame.setWidth(iButtonWidth-iSpacing*2);
			oFrame.setHeight(iButtonHeight-iSpacing*2);
			oFrame.allowClick(true);
			String sReplaceString = "$1"+ oWholeAlphanumericList.get(i) + "<select></select>$2";
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", sReplaceString);
			oFrame.setClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(oWholeAlphanumericList.get(i), oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setTop(0);
			oLabel.setLeft(0);
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue(oWholeAlphanumericList.get(i));
			oFrame.attachChild(oLabel);
			
			iLeft ++;
			if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
				if(oWholeAlphanumericList.get(i) == "0" || oWholeAlphanumericList.get(i) == "J" || oWholeAlphanumericList.get(i) == "T"){
					iTop ++;
					iLeft = 0;
				}
			}else if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
				if(iLeft == 5){
					iTop ++;
					iLeft = 0;
				}
			}
		}
		
		// Backspace button
		m_oFrameBackspace = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameBackspace, "fraBackspace");

		m_oFrameBackspace.allowClick(true);
		m_oFrameBackspace.setClickReplaceValue(null, "^(.*).<select></select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setClickReplaceValue(null, "^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
		m_oFrameBackspace.setClickServerRequestBlockUI(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameBackspace.setVisible(false);
		this.attachChild(m_oFrameBackspace);

		oImage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(oImage, "ImgBackspace");
		// Image size 36 x 23
		oImage.setExist(true);
		oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/backspace_button.png");
		oImage.setWidth(36);
		oImage.setHeight(22);
		m_oFrameBackspace.attachChild(oImage);
				
		// Cancel button
		m_oFrameCancel = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameCancel, "fraCancel");

		m_oFrameCancel.allowClick(true);
		m_oFrameCancel.setClickServerRequestBlockUI(true);
		m_oFrameCancel.allowLongClick(true);
		m_oFrameCancel.setLongClickServerRequestBlockUI(true);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameCancel.setVisible(false);
		this.attachChild(m_oFrameCancel);
		oLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(oLabel, "lblCancel");
		oLabel.setTop(0);
		oLabel.setLeft(0);
		oLabel.setWidth(iButtonWidth);
		oLabel.setHeight(iButtonHeight);
		oLabel.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
		m_oFrameCancel.attachChild(oLabel);
		
		// Enter button
		m_oFrameEnter = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFrameEnter, "fraEnter");
		m_oFrameEnter.allowClick(true);
		m_oFrameEnter.setClickServerRequestBlockUI(false);
		if(!AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			m_oFrameEnter.setVisible(false);
		this.attachChild(m_oFrameEnter);
		m_oLabelEnter = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelEnter, "lblEnter");
		m_oLabelEnter.setTop(0);
		m_oLabelEnter.setLeft(0);
		m_oLabelEnter.setValue(AppGlobal.g_oLang.get()._("enter", ""));
		m_oFrameEnter.attachChild(m_oLabelEnter);
		
		
		//set backspace, cancel, enter position
		if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())){
			//backspace
			m_oFrameBackspace.setTop(iButtonHeight * 3 + iSpacing);
			m_oFrameBackspace.setLeft(iButtonWidth * 7 + iSpacing);
			m_oFrameBackspace.setWidth(iButtonWidth-iSpacing * 2);
			m_oFrameBackspace.setHeight(iButtonHeight-iSpacing * 2);
			
			iTop = (m_oFrameBackspace.getHeight() / 2) - 12;
			if(iTop < 0)
				iTop = 0;
			oImage.setTop(iTop);
			iLeft = (m_oFrameBackspace.getWidth() / 2) - 18;
			if(iLeft < 0)
				iLeft = 0;
			oImage.setLeft(iLeft);
			
			//cancel
			m_oFrameCancel.setTop(iButtonHeight * 3 + iSpacing);
			m_oFrameCancel.setLeft(iButtonWidth * 8 + iSpacing);
			m_oFrameCancel.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameCancel.setHeight(iButtonHeight - iSpacing * 2);
			
			//enter
			m_oFrameEnter.setTop(iButtonHeight * 2 + iSpacing);
			m_oFrameEnter.setLeft(iButtonWidth * 9 + iSpacing);
			m_oFrameEnter.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameEnter.setHeight(iButtonHeight * 2 - iSpacing * 2);
			m_oLabelEnter.setWidth(iButtonWidth - iSpacing * 2);
			m_oLabelEnter.setHeight(iButtonHeight * 2- iSpacing * 2);
		}else if(AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name())){
			//backspace
			m_oFrameBackspace.setTop(iButtonHeight * 7 + iSpacing);
			m_oFrameBackspace.setLeft(iButtonWidth * 1 + iSpacing);
			m_oFrameBackspace.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameBackspace.setHeight(iButtonHeight - iSpacing * 2);
			
			iTop = (m_oFrameBackspace.getHeight() / 2) - 12;
			if(iTop < 0)
				iTop = 0;
			oImage.setTop(iTop);
			iLeft = (m_oFrameBackspace.getWidth() / 2) - 18;
			if(iLeft < 0)
				iLeft = 0;
			oImage.setLeft(iLeft);
			//cancel
			m_oFrameCancel.setTop(iButtonHeight * 7 + iSpacing);
			m_oFrameCancel.setLeft(iButtonWidth * 2 + iSpacing);
			m_oFrameCancel.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameCancel.setHeight(iButtonHeight - iSpacing * 2);
			
			//enter
			m_oFrameEnter.setTop(iButtonHeight * 7 + iSpacing);
			m_oFrameEnter.setLeft(iButtonWidth * 3 + iSpacing);
			m_oFrameEnter.setWidth(iButtonWidth * 2 - iSpacing * 2);
			m_oFrameEnter.setHeight(iButtonHeight - iSpacing * 2);
			m_oLabelEnter.setWidth(iButtonWidth * 2 - iSpacing * 2);
			m_oLabelEnter.setHeight(iButtonHeight - iSpacing * 2);
		}
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

	public void setEnterDesc(String[] sDesc){
		m_oLabelEnter.setValue(sDesc);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		if(m_oFrameEnter.getId() == iChildId){
			for (FrameQwertyKeyboardListener listener : listeners) {
				// Raise the event to parent
				listener.FrameQwertyKeyboard_clickEnter();
				bMatchChild = true;
				break;
			}
		}else if(m_oFrameCancel.getId() == iChildId){
			for (FrameQwertyKeyboardListener listener : listeners) {
				// Raise the event to parent
				listener.FrameQwertyKeyboard_clickCancel();
				bMatchChild = true;
				break;
			}
		}else {
			for(VirtualUIFrame oFrame : m_oFrameNumbers.values()) {
				if(oFrame.getId() == iChildId) {
					for (FrameQwertyKeyboardListener listener : listeners) {
						listener.FrameQwertyKeyboard_clickNumber(oFrame.getChilds().get(0).getValue());
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

