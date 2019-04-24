package app.controller;

import java.util.HashMap;

import core.Core;
import core.manager.ActiveClient;

public class VariableManager<T> {
	
	HashMap<ActiveClient, T> m_oClientVariableList;
	
	public VariableManager() {
		m_oClientVariableList = new HashMap<ActiveClient, T>();
	}
	
	public void set(T t) {
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		m_oClientVariableList.put(oActiveClient, t);
	}
	
	public T get() {
		ActiveClient oActiveClient = Core.g_oClientManager.getActiveClient();
		
		if (m_oClientVariableList.containsKey(oActiveClient)) {
			return (T) m_oClientVariableList.get(oActiveClient);
		} else {
			return null;
		}
	}
}
