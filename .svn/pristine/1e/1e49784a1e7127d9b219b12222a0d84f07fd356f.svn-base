package core.controller;

import java.util.ArrayList;

import core.Controller;
import core.listener.MSRListener;
import core.virtualui.HeroActionProtocol;
import core.virtualui.VirtualUIBasicElement;

public class MSRController extends Controller {
	
	VirtualUIMSR m_oVirtualUIMSR;
    ArrayList<MSRListener> m_oListeners;
	
	public MSRController(Controller oParentController) {
		super(oParentController);
		
		this.setCoverWidth(0);
		this.setCoverHeight(0);
		
		m_oListeners = new ArrayList<MSRListener>();
		
		m_oVirtualUIMSR = new VirtualUIMSR();
		//m_oVirtualUIMSR.allowValueChanged(true);
		//m_oVirtualUIMSR.addValueChangedServerRequestSubmitElement(m_oVirtualUIMSR);
		this.attachChild(m_oVirtualUIMSR);
	}
	
    public void addListener(MSRListener listener) {
    	m_oListeners.add(listener);
    }
    
    public void removeListener(MSRListener listener) {
    	m_oListeners.remove(listener);
    }
    
    public void removeAllListener() {
    	m_oListeners.clear();
    }
	
	class VirtualUIMSR extends VirtualUIBasicElement {
		
		public VirtualUIMSR(){
			super(HeroActionProtocol.View.Type.SWIPE_CARD_READER);
	    }
		
		@Override
	    public void valueChanged(int iChildId) {
        	for (MSRListener oListener : m_oListeners) {
        		// Raise the event to parent
           		oListener.onSwipeCard(m_oVirtualUIMSR.getValue());
            }
		}
	}
}
