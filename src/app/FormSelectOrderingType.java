package app;

import java.util.ArrayList;

import core.Controller;
import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIForm;

/** interface for the listeners/observers callback method */
interface FormSelectOrderingTypeListener {
	void formSelectOrderingType_findDining();
	void formSelectOrderingType_takeAway();
	void formSelectOrderingType_changeLanguage();
}

public class FormSelectOrderingType extends VirtualUIForm implements FrameSelectOrderingTypeListener {
	TemplateBuilder m_oTemplateBuilder;
	
	private FrameSelectOrderingType m_oFrameSelectOrderingType;
	
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FormSelectOrderingTypeListener> listeners;
	
	/** add a new ModelListener observer for this Model */
	public void addListener(FormSelectOrderingTypeListener listener) {
		listeners.add(listener);
	}
	
	public FormSelectOrderingType(Controller oParentController) {
		super(oParentController);
		m_oTemplateBuilder = new TemplateBuilder();
		
		listeners = new ArrayList<FormSelectOrderingTypeListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("frmSelectOrderingType.xml");
		
		// Background Cover Page
		m_oFrameSelectOrderingType = new FrameSelectOrderingType();
		m_oTemplateBuilder.buildFrame(m_oFrameSelectOrderingType, "fraSelectOrderingType");
		m_oFrameSelectOrderingType.addListener(this);
		this.attachChild(m_oFrameSelectOrderingType);
	}

	@Override
	public void FrameSelectOrderingType_fineDining() {
		for (FormSelectOrderingTypeListener listener : listeners) {
			// Raise the event to parent
			listener.formSelectOrderingType_findDining();
		}
		this.finishShow();
	}

	@Override
	public void FrameSelectOrderingType_takeaway() {
		for (FormSelectOrderingTypeListener listener : listeners) {
			listener.formSelectOrderingType_takeAway();
		}
		this.finishShow();
	}

	@Override
	public void FrameSelectOrderingType_changeLanguage() {
		for (FormSelectOrderingTypeListener listener : listeners) {
			listener.formSelectOrderingType_changeLanguage();
		}
	}
}
