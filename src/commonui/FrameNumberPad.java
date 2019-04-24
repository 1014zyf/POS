package commonui;

import java.util.ArrayList;
import java.util.HashMap;
import app.AppGlobal;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FrameNumberPad extends VirtualUIFrame {
	private TemplateBuilder m_oTemplateBuilder;
	private VirtualUIButton m_oButtonEnter;
	private VirtualUIButton m_oButtonCancel;
	private VirtualUIButton m_oButtonClear;
	private VirtualUIFrame m_oFrameBackspace;
	private VirtualUIFrame m_oFrameDot;
	private HashMap<Integer, VirtualUIFrame> m_oFrameNumbers;
	
	private Integer m_iHeight;
	private Integer m_iNumPadLeft;
	private boolean m_bSetCancelAndEnterButtonToLeftAndRight;
	private boolean m_bWithCancelEnter;
	private boolean m_bHideNumberPad;
	private boolean m_bClearReplaceCancelEnter;

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

	public FrameNumberPad() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameNumberPadListener>();
		m_oFrameNumbers = new HashMap<Integer, VirtualUIFrame>();
		m_iHeight = 0;
		m_iNumPadLeft = 0;
		m_bSetCancelAndEnterButtonToLeftAndRight = false;
		m_bWithCancelEnter = true;
		m_bHideNumberPad = false;
		m_bClearReplaceCancelEnter = false;
	}
	
	public void hideNumberPad() {
		m_bHideNumberPad = true;
	}

	public void init() {
		int iSpacing = 2;
		int iWidthSpace = 16;
		int iHeightSpace = 18;
		int iButtonLeft = m_iNumPadLeft;
		int iButtonTop = 0;
		// Load child elements from template
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraNumberPad.xml");

		int iButtonWidth = 0;
		int iButtonHeight = 0;
		int iCancelEnterWidth = 140;
		int iCancelEnterHeight = 58;
		int iCancelEnterSpace = 28;

		if (this.getWidth() < 312) {
			if (this.getWidth() >= 276) {
				iButtonWidth = 92;
				iWidthSpace = (this.getWidth() - (iButtonWidth * 3)) / 2;
			} else {
				iButtonWidth = this.getWidth() / 3;
				iWidthSpace = 2;
			}
			iCancelEnterWidth = this.getWidth() / 2 - 1;
			iCancelEnterSpace = iWidthSpace;
		} else {
			iButtonWidth = 92;
			iSpacing = 0;
			if (m_iNumPadLeft > 0) {
				iButtonLeft = m_iNumPadLeft;
				int iButtonRigth = this.getWidth() - 312 - m_iNumPadLeft;
				iCancelEnterSpace = this.getWidth() - (iCancelEnterWidth * 2) - iButtonRigth - m_iNumPadLeft;
			} else {
				iButtonLeft = (this.getWidth() - 312) / 2;
				iCancelEnterSpace = this.getWidth() - (iCancelEnterWidth + iButtonLeft) * 2;
			}
		}

		if ((this.getHeight() < 330 && m_bWithCancelEnter) || (this.getHeight() < 252 && !m_bWithCancelEnter)) {
			if (m_bWithCancelEnter)
				iButtonHeight = this.getHeight() / 5;
			else
				iButtonHeight = this.getHeight() / 4;
			iHeightSpace = 2;
			iCancelEnterHeight = iButtonHeight;
		} else {
			iButtonHeight = 50;
			iSpacing = 0;
			if (m_bWithCancelEnter)
				iButtonTop = (this.getHeight() - 330) / 2;
			else
				iButtonTop = (this.getHeight() - 252) / 2;
		}
		
		// Custom Height of Number Pad
		if (m_iHeight != 0)
			iButtonHeight = m_iHeight / 4;
		
		VirtualUIFrame oFrame;
		VirtualUILabel oLabel;
		VirtualUIImage oImage;
		
		if (!m_bHideNumberPad) {
			// 7 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iSpacing);
			oFrame.setLeft(iButtonLeft + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$17<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$17<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(7, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("7");
			oFrame.attachChild(oLabel);
	
			// 8 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iSpacing);
			oFrame.setLeft(iButtonLeft + iButtonWidth + iWidthSpace + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$18<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$18<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(8, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("8");
			oFrame.attachChild(oLabel);
	
			// 9 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iSpacing);
			oFrame.setLeft(iButtonLeft + (iButtonWidth + iWidthSpace) * 2 + (iSpacing * 2));
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$19<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$19<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(9, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("9");
			oFrame.attachChild(oLabel);
	
			// 4 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iButtonHeight + iHeightSpace + iSpacing * 2);
			oFrame.setLeft(iButtonLeft + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$14<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$14<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(4, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("4");
			oFrame.attachChild(oLabel);
	
			// 5 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iButtonHeight + iHeightSpace + iSpacing * 2);
			oFrame.setLeft(iButtonLeft + iButtonWidth + iWidthSpace + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$15<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$15<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(5, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("5");
			oFrame.attachChild(oLabel);
	
			// 6 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + iButtonHeight + iHeightSpace + iSpacing * 2);
			oFrame.setLeft(iButtonLeft + (iButtonWidth + iWidthSpace) * 2 + (iSpacing * 2));
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$16<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$16<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(6, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("6");
			oFrame.attachChild(oLabel);
	
			// 1 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 2 + iSpacing * 3);
			oFrame.setLeft(iButtonLeft + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$11<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$11<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(1, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("1");
			oFrame.attachChild(oLabel);
	
			// 2 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 2 + iSpacing * 3);
			oFrame.setLeft(iButtonLeft + iButtonWidth + iWidthSpace + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$12<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$12<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(2, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("2");
			oFrame.attachChild(oLabel);
	
			// 3 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 2 + iSpacing * 3);
			oFrame.setLeft(iButtonLeft + (iButtonWidth + iWidthSpace) * 2 + (iSpacing * 2));
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$13<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$13<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(3, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("3");
			oFrame.attachChild(oLabel);
	
			// Backspace button
			m_oFrameBackspace = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameBackspace, "fraBackspace");
			m_oFrameBackspace.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 3 + iSpacing * 4);
			m_oFrameBackspace.setLeft(iButtonLeft + (iButtonWidth + iWidthSpace) * 2 + (iSpacing * 2));
			m_oFrameBackspace.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameBackspace.setHeight(iButtonHeight - iSpacing * 2);
			m_oFrameBackspace.allowClick(true);
			m_oFrameBackspace.setClickReplaceValue(null, "^(.*).<select></select>(.*)$", "$1<select></select>$2");
			m_oFrameBackspace.setClickReplaceValue(null, "^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
			m_oFrameBackspace.setClickServerRequestBlockUI(false);
			m_oFrameBackspace.allowLongClick(true);
			m_oFrameBackspace.setLongClickReplaceValue(null, "^(.*).<select></select>(.*)$", "$1<select></select>$2");
			m_oFrameBackspace.setLongClickReplaceValue(null, "^(.*)<select>.+</select>(.*)$", "$1<select></select>$2");
			m_oFrameBackspace.setLongClickServerRequestBlockUI(false);
			this.attachChild(m_oFrameBackspace);
			
			oImage = new VirtualUIImage();
			m_oTemplateBuilder.buildImage(oImage, "ImgBackspace");
			// Image size 36 x 23
			oImage.setExist(true);
			oImage.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/keyboard_arrow_sh.png");
			oImage.setContentMode(HeroActionProtocol.View.Attribute.ContentMode.SCALE_ASPECT_FIT_CENTER);
			int iTop = (m_oFrameBackspace.getHeight() / 2) - 12;
			if (iTop < 0)
				iTop = 0;
			oImage.setTop(iTop);
			int iLeft = (m_oFrameBackspace.getWidth() / 2) - 18;
			if (iLeft < 0)
				iLeft = 0;
			oImage.setLeft(iLeft);
			oImage.setWidth(36);
			oImage.setHeight(23);
			m_oFrameBackspace.attachChild(oImage);
	
			// 0 button
			oFrame = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrame, "fraNumber");
			oFrame.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 3 + iSpacing * 4);
			oFrame.setLeft(iButtonLeft + iButtonWidth + iWidthSpace + iSpacing);
			oFrame.setWidth(iButtonWidth - iSpacing * 2);
			oFrame.setHeight(iButtonHeight - iSpacing * 2);
			oFrame.allowClick(true);
			oFrame.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$10<select></select>$2");
			oFrame.setClickServerRequestBlockUI(false);
			oFrame.allowLongClick(true);
			oFrame.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$10<select></select>$2");
			oFrame.setLongClickServerRequestBlockUI(false);
			this.attachChild(oFrame);
			m_oFrameNumbers.put(0, oFrame);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblNumber");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue("0");
			oFrame.attachChild(oLabel);
	
			// Dot button
			m_oFrameDot = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(m_oFrameDot, "fraDot");
			m_oFrameDot.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 3 + iSpacing * 4);
			m_oFrameDot.setLeft(iButtonLeft + iSpacing);
			m_oFrameDot.setWidth(iButtonWidth - iSpacing * 2);
			m_oFrameDot.setHeight(iButtonHeight - iSpacing * 2);
			m_oFrameDot.allowClick(true);
			m_oFrameDot.setClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$1\\.<select></select>$2");
			m_oFrameDot.setClickServerRequestBlockUI(false);
			m_oFrameDot.allowLongClick(true);
			m_oFrameDot.setLongClickReplaceValue(null, "^(.*)<select>.*</select>(.*)$", "$1\\.<select></select>$2");
			m_oFrameDot.setLongClickServerRequestBlockUI(false);
			this.attachChild(m_oFrameDot);
			oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "lblDot");
			oLabel.setWidth(iButtonWidth);
			oLabel.setHeight(iButtonHeight);
			oLabel.setValue(".");
			m_oFrameDot.attachChild(oLabel);
		}

		if (m_bWithCancelEnter) {
			// Cancel button
			m_oButtonCancel = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonCancel, "butCancel");
			if (m_bHideNumberPad)
				m_oButtonCancel.setTop(iButtonTop + iSpacing);
			else
				m_oButtonCancel.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 4 + iSpacing * 5);

			if (m_bSetCancelAndEnterButtonToLeftAndRight) {
				m_oButtonCancel.setLeft(0);
				m_oButtonCancel.setBackgroundColor("#5B6F73");
			} else
				m_oButtonCancel.setLeft(iButtonLeft + iSpacing);

			m_oButtonCancel.setWidth(iCancelEnterWidth);
			m_oButtonCancel.setHeight(iCancelEnterHeight);

			if (m_bSetCancelAndEnterButtonToLeftAndRight)
				m_oButtonCancel.setForegroundColor("#FFFFFF");

			m_oButtonCancel.setValue(AppGlobal.g_oLang.get()._("cancel", ""));
			m_oButtonCancel.allowClick(true);
			m_oButtonCancel.setClickServerRequestBlockUI(true);
			m_oButtonCancel.allowLongClick(true);
			m_oButtonCancel.setLongClickServerRequestBlockUI(true);
			this.attachChild(m_oButtonCancel);

			// Enter button
			m_oButtonEnter = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonEnter, "butEnter");
			if (m_bHideNumberPad)
				m_oButtonEnter.setTop(iButtonTop + iSpacing);
			else
				m_oButtonEnter.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 4 + iSpacing * 5);
			if (m_bSetCancelAndEnterButtonToLeftAndRight)
				m_oButtonEnter.setLeft(this.getWidth() - iCancelEnterWidth);
			else
				m_oButtonEnter.setLeft(iButtonLeft + iCancelEnterWidth + iCancelEnterSpace);
			m_oButtonEnter.setWidth(iCancelEnterWidth);
			m_oButtonEnter.setHeight(iCancelEnterHeight);
			m_oButtonEnter.setValue(AppGlobal.g_oLang.get()._("enter", ""));
			m_oButtonEnter.allowClick(true);
			m_oButtonEnter.setClickServerRequestBlockUI(false);
			m_oButtonEnter.allowLongClick(true);
			m_oButtonEnter.setLongClickServerRequestBlockUI(false);
			this.attachChild(m_oButtonEnter);
		}

		// Custom Height of Number Pad
		if (m_iHeight != 0) {
			if (m_bWithCancelEnter)
				this.setHeight(iButtonTop + (iButtonHeight + iHeightSpace) * 4 + iSpacing * 5 + iCancelEnterHeight);
			else
				this.setHeight(iButtonTop + (iButtonHeight + iHeightSpace) * 4 + iSpacing * 5);
		}
		if (m_bClearReplaceCancelEnter) {
			m_oButtonClear = new VirtualUIButton();
			m_oTemplateBuilder.buildButton(m_oButtonClear, "butClear");
			m_oButtonClear.setTop(iButtonTop + (iButtonHeight + iHeightSpace) * 4 + iSpacing * 5);
			m_oButtonClear.setWidth(iCancelEnterWidth * 2 + iCancelEnterSpace - iSpacing);
			m_oButtonClear.setHeight(iCancelEnterHeight);
			m_oButtonClear.setLeft(iButtonLeft + iSpacing);
			m_oButtonClear.setValue(AppGlobal.g_oLang.get()._("clear", ""));
			m_oButtonClear.allowClick(true);
			m_oButtonClear.setClickServerRequestBlockUI(true);
			m_oButtonClear.allowLongClick(true);
			m_oButtonClear.setLongClickServerRequestBlockUI(true);
			this.attachChild(m_oButtonClear);
			m_oButtonCancel.setVisible(false);
			m_oButtonEnter.setVisible(false);
		}
	}

	public void setNumPadLeft(int iLeft) {
		// Check does the numpad contain enough space to set left
		if (this.getWidth() > 312 && iLeft <= this.getWidth() - 312)
			m_iNumPadLeft = iLeft;
	}

	public void setCancelAndEnterToLeftAndRigth(boolean bSet) {
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
			return;
		m_bSetCancelAndEnterButtonToLeftAndRight = bSet;
	}

	public void setWithCancelAndEnterButton(boolean bSet) {
		m_bWithCancelEnter = bSet;
	}

	public void setEnterSubmitId(VirtualUIBasicElement oElement) {
		if (m_oButtonEnter != null) {
			m_oButtonEnter.addClickServerRequestSubmitElement(oElement);
			m_oButtonEnter.addLongClickServerRequestSubmitElement(oElement);
		}
	}

	public void clearEnterSubmitId() {
		if (m_oButtonEnter != null) {
			m_oButtonEnter.clearClickServerRequestSubmitId();
			m_oButtonEnter.clearLongClickServerRequestSubmitId();
		}
	}

	public void setEnterBlockUI(boolean bBlockUI) {
		if (m_oButtonEnter != null) {
			m_oButtonEnter.setClickServerRequestBlockUI(bBlockUI);
			m_oButtonEnter.setLongClickServerRequestBlockUI(bBlockUI);
		}
	}

	public void setEnterDesc(String[] sDesc) {
		if (m_oButtonEnter != null)
			m_oButtonEnter.setValue(sDesc);
	}

	public void setCustomHeight(Integer iHeight) {
		m_iHeight = iHeight;
	}
	
	public int getCustomHeight() {
		return m_iHeight;
	}
	
	public void setCancelButtonVisible(boolean bShow) {
		m_oButtonCancel.setVisible(bShow);
	}
	
	public void setClearReplaceCancelEnter(boolean bSet) {
		m_bClearReplaceCancelEnter = bSet;
	}
	
	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (m_oButtonEnter != null && m_oButtonEnter.getId() == iChildId) {
			for (FrameNumberPadListener listener : listeners) {
				// Raise the event to parent
				listener.FrameNumberPad_clickEnter();
				bMatchChild = true;
				break;
			}
		} else if ((m_oButtonCancel != null && m_oButtonCancel.getId() == iChildId) || (m_oButtonClear != null && m_oButtonClear.getId() == iChildId)) {
			for (FrameNumberPadListener listener : listeners) {
				// Raise the event to parent
				listener.FrameNumberPad_clickCancel();
				bMatchChild = true;
				break;
			}
		} else {
			for (int i = 0; i < m_oFrameNumbers.size(); i++) {
				if (m_oFrameNumbers.get(i).getId() == iChildId) {
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

	public void clearFocusElementValueOnCancelClick(boolean bEnable) {
		if (bEnable)
			m_oButtonCancel.setClickReplaceValue(null, ".+", "");
		else
			m_oButtonCancel.setClickReplaceValue(null, "", "");
	}

	// ***** DEBUG *****
	public VirtualUIButton getEnterButton() {
		return m_oButtonEnter;
	}
}
