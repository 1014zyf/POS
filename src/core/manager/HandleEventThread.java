package core.manager;

import core.*;
import core.manager.ActiveClient;

import org.json.JSONObject;

import core.externallib.Util;

public class HandleEventThread implements Runnable {
	ActiveClient m_oActiveClient;
	
	JSONObject m_oEventJsonObject;
	
	public HandleEventThread(ActiveClient oActiveClient, JSONObject oEventJsonObject) {
		System.out.println("new HandleEventThread");
		Util.showClock("event last", true);
		m_oActiveClient = oActiveClient;
		
		m_oEventJsonObject = oEventJsonObject;
	}
	
	@Override
	public void run() {
		// Thread to handle event from client
		System.out.println("HandleEventThread run");
		
		// Initialize global variables
		Core.g_oClientManager.registerCurrentThread(m_oActiveClient);
		ActiveClient oActiveClient = m_oActiveClient;
		
		// Go to logic part
		oActiveClient.g_oUIManager.handleEvent(m_oEventJsonObject);
		
		if (!oActiveClient.g_oUIManager.isCurrentThreadInIgnoreSendList()) {
			
			if (oActiveClient.g_oConnectionManager.getSocketCount() > 1) {
				System.out.println("sendPacket HandleEventThread 1");
				synchronized(oActiveClient.g_oResponsePacketManager) {
					oActiveClient.g_oConnectionManager.sendPacket(oActiveClient.g_oResponsePacketManager.getResponsePacket());
					oActiveClient.g_oResponsePacketManager.clear();
				}
			}
			else {
				//	Only one socket left
				try {
					//	Wait until all other thread die
					oActiveClient.g_oUIManager.waitUntilIgnoreSendListEmpty();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("sendPacket HandleEventThread 2");
				
				synchronized(oActiveClient.g_oResponsePacketManager) {
					oActiveClient.g_oUIManager.applyAllEdit();
					oActiveClient.g_oConnectionManager.sendPacket(oActiveClient.g_oResponsePacketManager.getResponsePacket());
					oActiveClient.g_oResponsePacketManager.clear();
				}
			}
		}
		else {
			oActiveClient.g_oUIManager.removeCurrentThreadToIgnoreSendList();
			System.out.println("Ignored thread dieing");
		}
	}
}
