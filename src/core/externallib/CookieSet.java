package core.externallib;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CookieSet {
	public ConcurrentHashMap<String, String> entrySet;
	
	public CookieSet() {
		init();
	}
	
	public CookieSet(String cookieString) {
		init();
		join(cookieString);
	}
	
	public void reset() {
		init();
	}
	
	private void init() {
		entrySet = new ConcurrentHashMap<String, String>();
	}
	
	public void join(String cookieString) {
		if (cookieString == null || cookieString.isEmpty())
			return;
		for (String cookiePair : cookieString.split(";")) {
			String[] keyValue = cookiePair.split("=");
			if (keyValue.length == 2) {
				String key = keyValue[0].trim();
				String value = keyValue[1].trim();
				if (!key.isEmpty())
					entrySet.put(key, value);
			}
		}
	}
	
	public void join(CookieSet cookieSet) {
		entrySet.putAll(cookieSet.entrySet);
	}
	
	public void setValue(String key, String value) {
		if (value != null)
			entrySet.put(key, value);
		else
			entrySet.remove(key);
	}
	
	public String getValue(String key) {
		if (key == null)
			return null;
		return entrySet.get(key);
	}
	
	public void remove(String key) {
		if (key != null)
			entrySet.remove(key);
	}
	
	public String getCookieString() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String,String> pair : entrySet.entrySet()) {
			sb.append(pair.getKey()).append("=").append(pair.getValue()).append(";");
		}
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return entrySet.isEmpty();
	}
}
