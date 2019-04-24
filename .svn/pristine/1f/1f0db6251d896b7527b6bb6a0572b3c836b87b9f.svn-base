package app;

import java.util.ArrayList;

import virtualui.VirtualUIBasicElement;

public class ClsGlobalElement {
	
	VirtualUIBasicElement m_oElement;
	ArrayList<ClsGlobalUIRunnable> m_oRegisterRunnables;
	
	public ClsGlobalElement(VirtualUIBasicElement oElement) {
		m_oElement = oElement;
		m_oRegisterRunnables = new ArrayList<>();
	}
	
	public VirtualUIBasicElement getElement() {
		return m_oElement;
	}
	
	public void registerRunnable(ClsGlobalUIRunnable oRunnable) {
		m_oRegisterRunnables.add(oRunnable);
	}
	
	public void performRunnable(int iClientSockId) {
		for(ClsGlobalUIRunnable oRunnable:m_oRegisterRunnables) {
			oRunnable.setClientSockId(iClientSockId);
			oRunnable.run();
		}
	}
}
