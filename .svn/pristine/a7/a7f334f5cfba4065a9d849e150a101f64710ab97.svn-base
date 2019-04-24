package core.externallib;

import java.util.Date;
import java.util.HashMap;

public class Profiling {
	private static HashMap<Object, Long> m_oStartTimesMap;
	
	static {
		m_oStartTimesMap = new HashMap<Object, Long>();
	}
	
	public static void start(Object o) {
		m_oStartTimesMap.put(o, (new Date()).getTime());
	}
	
	public static void end(Object o, String s) {
		Long lStartTime = m_oStartTimesMap.get(o);
		if (lStartTime == null)
			return;
		System.out.println("time taken " + s + ": " + ((new Date()).getTime() - lStartTime));
	}
}
