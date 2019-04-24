package commonui;

import java.util.ArrayList;

import org.json.JSONObject;

import core.Controller;
import app.AppGlobal;
import commonui.FrameOptionBox.FrameOptionBoxListener;
import templatebuilder.TemplateBuilder;
import virtualui.*;

public class FormOptionBox extends VirtualUIForm implements FrameOptionBoxListener {
	/** interface for the listeners/observers callback method */
	public interface FormOptionBoxListener {
		void formOptionBox_LabelSelected(JSONObject oSelectedKeyJsonObject);
		void formOptionBox_CancelSelected();
	}
	
	TemplateBuilder m_oTemplateBuilder;
	
	private static FrameOptionBox m_oFrameOptionBox;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormOptionBoxListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormOptionBoxListener listener) {
		listeners.add(listener);
	}
	
	/** remove a ModelListener observer for this Model */
	public void removeListener(FormOptionBoxListener listener) {
		listeners.remove(listener);
	}
	
	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}
	
	public FormOptionBox(Controller oParentController) {
		super(oParentController);
		
		m_oTemplateBuilder = new TemplateBuilder();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmOptionBox.xml");
		
		listeners = new ArrayList<FormOptionBoxListener>();
		
		// Option box
		m_oFrameOptionBox = new FrameOptionBox();
		m_oTemplateBuilder.buildFrame(m_oFrameOptionBox, "fraOptionBox");
		m_oFrameOptionBox.addButton(AppGlobal.g_oLang.get()._("cancel"), "KEY_CANCEL");
		m_oFrameOptionBox.addListener(this);
		m_oFrameOptionBox.setVisible(false);
		this.attachChild(m_oFrameOptionBox);
	}
	
	public void addButton(String sButtonDesc, String sKey) {
		m_oFrameOptionBox.addButton(sButtonDesc, sKey);
	}
	
	public void addHalfButton(String sButtonValue, String sKey) {
		m_oFrameOptionBox.addHalfButton(sButtonValue, sKey);
	}
	
	public void addOption(String sOptionValue) {
		m_oFrameOptionBox.addOption(sOptionValue);
	}
	
	public void addOption(String sOptionValue, JSONObject oKeyValueJSONObject) {
		m_oFrameOptionBox.addOption(sOptionValue, oKeyValueJSONObject);
	}
	
	public void setTitle(String sValue) {
		m_oFrameOptionBox.setTitle(sValue);
	}
	
	public void setButtonDesc(int iId, String sValue) {
		m_oFrameOptionBox.setButtonDesc(iId,sValue);
	}
	
	public void removeOptionList() {
		m_oFrameOptionBox.removeOptionList();
	}
	
	public int getOptionListSize() {
		return m_oFrameOptionBox.getOptionListSize();
	}
	
	public void selectAllOptions() {
		m_oFrameOptionBox.selectAllOptions();
	}
	
	public void selectOption(int iOptIndex) {
		m_oFrameOptionBox.selectOption(iOptIndex);
	}
	
	public void unselectAllOptions() {
		m_oFrameOptionBox.unselectAllOptions();
	}
	
	public void unselectOption(int iOptIndex) {
		m_oFrameOptionBox.unselectOption(iOptIndex);
	}
	
	public JSONObject getOptKey(int iIndex) {
		return m_oFrameOptionBox.getOptKey(iIndex);
	}
	
	public void setVisible(boolean bVisible) {
		m_oFrameOptionBox.setVisible(bVisible);
	}
	
	public void bringToTop() {
		m_oFrameOptionBox.bringToTop();
	}
	
	@Override
	public void frameOptionBox_LabelSelected(int iOptIndex) {
		for (FormOptionBoxListener listener : listeners) {
			listener.formOptionBox_LabelSelected(m_oFrameOptionBox.getOptKey(iOptIndex));
		}
		this.finishShow();
	}

	@Override
	public void frameOptionBox_ButtonClicked(int iId, String sValue) {
		if (sValue.equals("KEY_CANCEL")) {
			for (FormOptionBoxListener listener : listeners) {
				listener.formOptionBox_CancelSelected();
			}
			this.finishShow();
		}
	}
}
