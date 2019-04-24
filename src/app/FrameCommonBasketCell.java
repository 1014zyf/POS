package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIBasicElement;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;

/** interface for the listeners/observers callback method */
interface FrameCommonBasketCellListener {
    void frameCommonBasketCell_FieldClicked(int iCellId, int iFieldIndex, String sNote);
    void frameCommonBasketCell_FieldLongClicked(int iCellId, int iFieldIndex, String sNote);
}

public class FrameCommonBasketCell extends VirtualUIFrame{
	TemplateBuilder m_oTemplateBuilder;
	
	private HashMap<Integer, VirtualUIBasicElement> m_oField;
	private HashMap<Integer, VirtualUILabel> m_oLabelFieldInfo1;
	private HashMap<Integer, VirtualUILabel> m_oLabelFieldInfo2;
	private VirtualUITextbox m_oTextboxField;	// Single edit field for step input
	private VirtualUIFrame m_oUnderlineFrame;
	private ArrayList<VirtualUILabel> m_oLabelField;
	public static int CELL_PADDING = 2;
	private VirtualUILabel m_oExtendRowLabel ;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCommonBasketCellListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCommonBasketCellListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCommonBasketCellListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
    
	public void init(int iFieldCount, int iFieldHeight,int iLineWidth, ArrayList<String> sFieldType, ArrayList<VirtualUIBasicElement> oSubmitIdElements) {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCommonBasketCellListener>();
		m_oField = new HashMap<Integer, VirtualUIBasicElement>();
		m_oLabelFieldInfo1 = new HashMap<Integer, VirtualUILabel>();
		m_oLabelFieldInfo2 = new HashMap<Integer, VirtualUILabel>();
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oLabelField = new ArrayList<VirtualUILabel>();
		// Edit field
		m_oTextboxField = new VirtualUITextbox();
		m_oTextboxField.setExist(true);
		m_oTextboxField.setVisible(false);
		this.attachChild(m_oTextboxField);
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCommonBasketCell.xml");
		
		// Field
		for(int i=0; i<iFieldCount; i++){
			String sType = "";
			if(sFieldType != null){
				if(i < sFieldType.size()){
					sType = sFieldType.get(i);
				}
			}
			
			if(sType.equals(HeroActionProtocol.View.Type.TEXTBOX)){
				VirtualUITextbox oTextbox = new VirtualUITextbox();
				m_oTemplateBuilder.buildTextbox(oTextbox, "editField");
				if(iFieldHeight > 0)
					oTextbox.setHeight(iFieldHeight);
				this.attachChild(oTextbox);
				
				m_oField.put(i, oTextbox);
			}else if (sType.equals(HeroActionProtocol.View.Type.BUTTON)) {
				VirtualUIButton oButton = new VirtualUIButton();
				m_oTemplateBuilder.buildButton(oButton, "btnField");
				oButton.setClickServerRequestNote(String.valueOf(i));
				if(iFieldHeight > 0)
					oButton.setHeight(iFieldHeight);
				this.attachChild(oButton);
				
				m_oField.put(i, oButton);
			}else if (sType.equals(HeroActionProtocol.View.Type.IMAGE)) {
				VirtualUIImage oImage = new VirtualUIImage();
				m_oTemplateBuilder.buildImage(oImage, "imageField");
				oImage.allowClick(true);
				oImage.setClickServerRequestBlockUI(false);
				this.attachChild(oImage);
				
				m_oField.put(i, oImage);
			}else{
				VirtualUILabel oLabel = new VirtualUILabel();
				m_oTemplateBuilder.buildLabel(oLabel, "field");
				oLabel.allowClick(true);
				oLabel.clearClickServerRequestSubmitId();
				if(oSubmitIdElements != null){
					for(VirtualUIBasicElement oElement:oSubmitIdElements){
						oLabel.addClickServerRequestSubmitElement(oElement);
					}
				}
				oLabel.allowLongClick(true);
				oLabel.clearLongClickServerRequestSubmitId();
				if(oSubmitIdElements != null){
					for(VirtualUIBasicElement oElement:oSubmitIdElements){
						oLabel.addLongClickServerRequestSubmitElement(oElement);
					}
				}				
				if(iFieldHeight > 0)
					oLabel.setHeight(iFieldHeight);
				
				this.attachChild(oLabel);
				
				m_oField.put(i, oLabel);
				m_oLabelField.add(oLabel);
			}
		}
		
		//Cell Underline
		if(iFieldHeight == 0)
			iFieldHeight = 47;
		m_oUnderlineFrame = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oUnderlineFrame, "fraUnderline");
		m_oUnderlineFrame.setWidth(iLineWidth);
		m_oUnderlineFrame.setTop(iFieldHeight-1);
		this.attachChild(m_oUnderlineFrame);
		
		m_oExtendRowLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oExtendRowLabel, "lblExtendRowForPaymentInfo");
		
	}
	
	public void setFieldValue(int iFieldIndex, String[] sValue) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setValue(sValue);
			else if(oElement instanceof VirtualUIImage){
				((VirtualUIImage) oElement).setSource(sValue[0]);
				((VirtualUIImage) oElement).setTop(this.getHeight() / 6);
				((VirtualUIImage) oElement).setHeight(this.getHeight() / 3 * 2);
			}
		}
	}
	
	public String getFieldValue(int iFieldIndex) {
		if(m_oField.containsKey(iFieldIndex)){
			if (!m_oField.get(iFieldIndex).getUIType().equals(HeroActionProtocol.View.Type.IMAGE)){
				VirtualUILabel oLabel = (VirtualUILabel) m_oField.get(iFieldIndex);
				return oLabel.getValue();
			}else{	
				// Get source for VirtualUIImage
				VirtualUIImage oImage = (VirtualUIImage) m_oField.get(iFieldIndex);
				return oImage.getSource();
			}
		}
		return "";
	}
	
	public void setFieldInfo1(int iFieldIndex, String sValue) {
		if(m_oLabelFieldInfo1.containsKey(iFieldIndex)){
			VirtualUILabel oLabel = m_oLabelFieldInfo1.get(iFieldIndex);
			oLabel.setValue(sValue);
		}else{
			// Add a label for info
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "info");
			oLabel.setValue(sValue);
			
			// Resize the information label
			if(m_oField.containsKey(iFieldIndex)){
				VirtualUILabel oFieldLabel = (VirtualUILabel) m_oField.get(iFieldIndex);
				int iFieldHeight = oFieldLabel.getHeight();
				oLabel.setTop(oFieldLabel.getTop() + iFieldHeight);
				oLabel.setLeft(oFieldLabel.getLeft());
				oLabel.setWidth(oFieldLabel.getWidth());
			}
			this.attachChild(oLabel);
			
			m_oLabelFieldInfo1.put(iFieldIndex, oLabel);
			
			// Resize the current line
			this.setHeight(this.getHeight() + oLabel.getHeight() + m_oExtendRowLabel.getHeight());
			m_oUnderlineFrame.setTop(this.getHeight() - m_oExtendRowLabel.getTop());
		}
	}
	
	public void setFieldInfo2(int iFieldIndex, String sValue) {
		if(m_oLabelFieldInfo2.containsKey(iFieldIndex)){
			VirtualUILabel oLabel = m_oLabelFieldInfo2.get(iFieldIndex);
			oLabel.setValue(sValue);
		}else{
			// Add a label for info
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "info");
			oLabel.setValue(sValue);
			
			// Resize the information label
			if(m_oField.containsKey(iFieldIndex)){
				VirtualUILabel oFieldLabel = (VirtualUILabel) m_oField.get(iFieldIndex);
				int iFieldHeight = oFieldLabel.getHeight();
				if(m_oLabelFieldInfo1.containsKey(iFieldIndex)){
					VirtualUILabel oFieldInfo1Label = m_oLabelFieldInfo1.get(iFieldIndex);
					iFieldHeight += oFieldInfo1Label.getHeight();
				}
				oLabel.setTop(oFieldLabel.getTop() + iFieldHeight);
				oLabel.setLeft(oFieldLabel.getLeft());
				oLabel.setWidth(oFieldLabel.getWidth());
			}
			this.attachChild(oLabel);
			m_oLabelFieldInfo2.put(iFieldIndex, oLabel);
			
			// Resize the current line
			this.setHeight(this.getHeight() + oLabel.getHeight() + m_oExtendRowLabel.getHeight());
			m_oUnderlineFrame.setTop(this.getHeight() - m_oExtendRowLabel.getTop());
		}
	}
	
	public void setFieldInfo1BackgroundColor(int iFieldIndex, String sBackgroundColor){
		if(m_oLabelFieldInfo1.containsKey(iFieldIndex)){
			VirtualUILabel oLabel = m_oLabelFieldInfo1.get(iFieldIndex);
			oLabel.setBackgroundColor(sBackgroundColor);
			this.setBackgroundColor(sBackgroundColor);
		}
	}
	
	public void setFieldInfo2BackgroundColor(int iFieldIndex, String sBackgroundColor){
		if(m_oLabelFieldInfo2.containsKey(iFieldIndex)){
			VirtualUILabel oLabel = m_oLabelFieldInfo2.get(iFieldIndex);
			oLabel.setBackgroundColor(sBackgroundColor);
			this.setBackgroundColor(sBackgroundColor);
		}
	}
	
	public void setFieldLeft(int iFieldIndex, int iLeft) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIImage){
				oElement.setLeft(iLeft);
			}
			else
				oElement.setLeft(iLeft+CELL_PADDING);
		} else {
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "field");
			oLabel.setLeft(iLeft);
			this.attachChild(oLabel);
			
			m_oField.put(iFieldIndex, oLabel);
		}
	}
	
	public void setFieldWidth(int iFieldIndex, int iWidth) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIImage)
				oElement.setWidth(iWidth);
			else
				oElement.setWidth(iWidth-CELL_PADDING*2);
		} else {
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "field");
			oLabel.setWidth(iWidth);
			this.attachChild(oLabel);
			
			m_oField.put(iFieldIndex, oLabel);
		}
	}
	
	public void setFieldTop(int iFieldIndex, int iTop) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIImage){
				oElement.setTop(iTop);
			}
			else
				oElement.setTop(iTop+CELL_PADDING);
		} else {
			VirtualUILabel oLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oLabel, "field");
			oLabel.setTop(iTop);
			this.attachChild(oLabel);
			
			m_oField.put(iFieldIndex, oLabel);
		}
	}

	public void setFieldAlign(int iFieldIndex, String sAlign) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setTextAlign(sAlign);
		}
	}

	public void setFieldPadding(int iFieldIndex, String sPadding) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setPaddingValue(sPadding);
		}
	}

	public void setFieldClickServerRequestNote(int iFieldIndex, String sNote) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton || oElement instanceof VirtualUIImage)
				oElement.setClickServerRequestNote(sNote);
		}
	}
	
	public VirtualUIBasicElement setEditField(int iFieldIndex, boolean bEdit) {
		
		if(bEdit == false){
			VirtualUILabel oLabel = (VirtualUILabel) m_oField.get(iFieldIndex);
			oLabel.setVisible(true);
			m_oTextboxField.setVisible(false);
		}else{	
			if(m_oField.containsKey(iFieldIndex)){
				VirtualUILabel oLabel = (VirtualUILabel) m_oField.get(iFieldIndex);
				oLabel.setVisible(false);
				
				m_oTextboxField.setValue(oLabel.getValue());
				m_oTextboxField.setTop(oLabel.getTop());
				m_oTextboxField.setLeft(oLabel.getLeft());
				m_oTextboxField.setWidth(oLabel.getWidth());
				m_oTextboxField.setHeight(oLabel.getHeight());
				m_oTextboxField.setBackgroundColor(oLabel.getBackgroundColor());
				m_oTextboxField.setForegroundColor(oLabel.getForegroundColor());
				m_oTextboxField.setTextSize(oLabel.getTextSize());
				m_oTextboxField.setKeyboardType(HeroActionProtocol.View.Attribute.KeyboardType.DECIMAL);
				m_oTextboxField.setClickHideKeyboard(true);
				m_oTextboxField.setVisible(true);
				m_oTextboxField.bringToTop();
				m_oTextboxField.setFocus();
			}
		}
		
		return m_oTextboxField;
	}
	
	public String getEditFieldValue(){
		return m_oTextboxField.getValue();
	}
	
	public int getFieldInfo1Size() {
		return m_oLabelFieldInfo1.size();
	}
	
	public int getFieldInfo2Size() {
		return m_oLabelFieldInfo2.size();
	}
	
	public void setFieldBackgroundColor(int iFieldIndex, String sBackgroundColor) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton || oElement instanceof VirtualUIImage)
				oElement.setBackgroundColor(sBackgroundColor);
		}
	}

	public void setFieldForegroundColor(int iFieldIndex, String sForegroundColor) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setForegroundColor(sForegroundColor);
		}
	}
	
	public void setFieldTextSize(int iFieldIndex, int iTextSize) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setTextSize(iTextSize);
		}
	}
	
	public void setFieldSubmitId(VirtualUIBasicElement oElement) {
		for(VirtualUIBasicElement oLabelField:m_oField.values()) {
			oLabelField.addClickServerRequestSubmitElement(oElement);
			oLabelField.addLongClickServerRequestSubmitElement(oElement);
		}
	}

	public void setFieldEnabled(int iFieldIndex, boolean bEnabled) {
		if(m_oField.containsKey(iFieldIndex)) {
			VirtualUIBasicElement oElement = m_oField.get(iFieldIndex);
			if(oElement instanceof VirtualUILabel || oElement instanceof VirtualUITextbox || oElement instanceof VirtualUIButton)
				oElement.setEnabled(bEnabled);
			else if(oElement instanceof VirtualUIImage)
				((VirtualUIImage) oElement).setEnabled(bEnabled);
		}
	}
	
	public HashMap<Integer, VirtualUIBasicElement> getFieldList() {
		return m_oField;
	}
	
	@Override
    public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		int iFieldIndex = 0;
		for(Entry<Integer, VirtualUIBasicElement> entry: m_oField.entrySet()){
			VirtualUIBasicElement oLabel = entry.getValue();
			if (iChildId == oLabel.getId()) {
				for (FrameCommonBasketCellListener listener : listeners) {
	        		// Raise the event to parent
	           		listener.frameCommonBasketCell_FieldClicked(this.getId(), iFieldIndex, sNote);
	            }
				
				bMatchChild = true;
				break;
			}
			iFieldIndex++;
		}
			
        return bMatchChild;
    }
	
	@Override
    public boolean longClicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		
		int iFieldIndex = 0;
		for(Entry<Integer, VirtualUIBasicElement> entry: m_oField.entrySet()){
			VirtualUIBasicElement oLabel = entry.getValue();
			if (iChildId == oLabel.getId()) {
				for (FrameCommonBasketCellListener listener : listeners) {
	        		// Raise the event to parent
	           		listener.frameCommonBasketCell_FieldLongClicked(this.getId(), iFieldIndex, sNote);
	            }
				
				bMatchChild = true;
				break;
			}
			iFieldIndex++;
		}
			
        return bMatchChild;
    }
	
}
