package externaldevice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import om.PosStation;
import om.PosStationDevice;
import om.PosStationList;
import om.PreorderStation;
import app.*;

public class Main {
	
	public static boolean checkAppleDevice(String sUDID) {
		//Get number of external device, station address
		ArrayList<String> oStationAddressArrayList = new ArrayList<String>(); 
		
		PreorderStation oPreorderStation = new PreorderStation();
		JSONArray oStationJSONArray = oPreorderStation.getStationListByType("i");		//iPad only
		
		if(oStationJSONArray == null || oStationJSONArray.length() <= 0) {
			//no records in pre-order module
			return false;
		}
		else {
			try {
				for(int i = 0; i < oStationJSONArray.length(); i++) {
					oStationAddressArrayList.add(oStationJSONArray.get(i).toString());
				}
			}
			catch (JSONException jsone) {
				AppGlobal.stack2Log(jsone);
				return false;
			}
		}
		
		for(String sAddress : oStationAddressArrayList) {
			if(sUDID.equals(sAddress)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkAutoStationDevice(String sAddress) {
		PosStation oPosStation = new PosStation(sAddress);
		if(oPosStation.getStatId() == 0)
			return false;
		
		PosStationDevice oPosStationDevice = oPosStation.getStationDevice();
		if(!oPosStationDevice.getKey().equals(PosStationDevice.KEY_AUTO_STATION))
			return false;
		
		return true;
	}
	
	public static boolean checkPortalStationDevice(String sAddress) {
		PosStation oPosStation = new PosStation(sAddress);
		if(oPosStation.getStatId() == 0)
			return false;
		
		PosStationDevice oPosStationDevice = oPosStation.getStationDevice();
		if(!oPosStationDevice.getKey().equals(PosStationDevice.KEY_PORTAL_STATION))
			return false;
		
		return true;
	}
	
	public static boolean checkThirdPartyStationDevice(String sAddress) {
		PosStation oPosStation = new PosStation(sAddress);
		if(oPosStation.getStatId() == 0)
			return false;
		
		PosStationDevice oPosStationDevice = oPosStation.getStationDevice();
		if(!oPosStationDevice.getKey().equals(PosStationDevice.KEY_THIRD_PARTY_STATION))
			return false;
		
		return true;
	}
	
	public static List<PosStation> getAllPortalStations() {
		PosStationList oPosStationList = new PosStationList();
		oPosStationList.readStationListByDeviceKey(PosStationDevice.KEY_PORTAL_STATION);
		
		return oPosStationList.getPosStationList();
	}
}