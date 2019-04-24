package app.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;

public class PosOverrideConditionList {
	private List<PosOverrideCondition> m_oPosOverrideConditionList;
	
	public PosOverrideConditionList() {
		m_oPosOverrideConditionList = new ArrayList<PosOverrideCondition>();
	}
	
	public void readAllOverrideCondition(int iOutletId) {
		PosOverrideCondition oPosOverrideConditionList = new PosOverrideCondition();
		JSONArray responseArray = null;
		
		responseArray = oPosOverrideConditionList.readAllByOutletId(iOutletId);
		if(responseArray == null) 
			return;
		
		for (int i = 0; i < responseArray.length(); i++) {
			if (responseArray.isNull(i) || !responseArray.optJSONObject(i).has("PosOverrideCondition"))
				continue;
			
			PosOverrideCondition oPosOverrideCondition = new PosOverrideCondition(responseArray.optJSONObject(i).optJSONObject("PosOverrideCondition"));
			m_oPosOverrideConditionList.add(oPosOverrideCondition);
		}
	}
	
	//get the override condition with sorted by priority for price level override
	public List<PosOverrideCondition> getConditionWithPriorityForPriceLevel(int iPriceLevel) {
		List<PosOverrideCondition> oSortedCondition = new ArrayList<PosOverrideCondition>();
		HashMap<Integer, List<PosOverrideCondition>> oConditions = new HashMap<Integer, List<PosOverrideCondition>>();
		for(PosOverrideCondition oOverrideCondition:m_oPosOverrideConditionList) {
			if(oOverrideCondition.getFromPriceLevel() == iPriceLevel && oOverrideCondition.getToPriceLevel() != 0 && oOverrideCondition.getFromPriceLevel() != oOverrideCondition.getToPriceLevel()) {
				if(oConditions.containsKey(oOverrideCondition.getPriority()) == false) 
					oConditions.put(oOverrideCondition.getPriority(), new ArrayList<PosOverrideCondition>());
				oConditions.get(oOverrideCondition.getPriority()).add(oOverrideCondition);
			}
		}
		
		//Sorting the conditions by priority
		Map<Integer, List<PosOverrideCondition>> oSortedMap = new TreeMap<Integer, List<PosOverrideCondition>>(
				new Comparator<Integer>() {
	 
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}
	 
			});
		oSortedMap.putAll(oConditions);
		for(Map.Entry<Integer, List<PosOverrideCondition>> entry: oSortedMap.entrySet()){
			for(PosOverrideCondition oOverrideCondition: entry.getValue()) 
				oSortedCondition.add(oOverrideCondition);
		}
		
		return oSortedCondition;
	}
	
	//get the override condition with sorted by priority for sc/tax override
	public List<PosOverrideCondition> getConditionWithPriorityForScAndTax() {
		List<PosOverrideCondition> oSortedCondition = new ArrayList<PosOverrideCondition>();
		HashMap<Integer, List<PosOverrideCondition>> oOverrideConditions = new HashMap<Integer, List<PosOverrideCondition>>();
		boolean bScTaxOverride = false;
		
		for(PosOverrideCondition oOverrideCondition:m_oPosOverrideConditionList) {
			bScTaxOverride = false;
			
			for(int i=1; i<=5; i++) {
				if(!oOverrideCondition.getChargeSc(i).equals(PosOverrideCondition.CHARGE_SC_NO_CHANGE)) {
					if(oOverrideConditions.containsKey(oOverrideCondition.getPriority()) == false) 
						oOverrideConditions.put(oOverrideCondition.getPriority(), new ArrayList<PosOverrideCondition>());
					oOverrideConditions.get(oOverrideCondition.getPriority()).add(oOverrideCondition);
					bScTaxOverride = true;
					break;
				}
			}
			if(bScTaxOverride)
				continue;
			
			for(int i=1; i<=25; i++) {
				if(!oOverrideCondition.getChargeTax(i).equals(PosOverrideCondition.CHARGE_TAX_NO_CHANGE)) {
					if(oOverrideConditions.containsKey(oOverrideCondition.getPriority()) == false) 
						oOverrideConditions.put(oOverrideCondition.getPriority(), new ArrayList<PosOverrideCondition>());
					oOverrideConditions.get(oOverrideCondition.getPriority()).add(oOverrideCondition);
					bScTaxOverride = true;
					break;
				}
			}
			if(bScTaxOverride)
				continue;
		}
		
		//Sorting the conditions by priority
		Map<Integer, List<PosOverrideCondition>> oSortedMap = new TreeMap<Integer, List<PosOverrideCondition>>(
				new Comparator<Integer>() {
	 
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}
	 
			});
		oSortedMap.putAll(oOverrideConditions);
		for(Map.Entry<Integer, List<PosOverrideCondition>> entry: oSortedMap.entrySet()){
			for(PosOverrideCondition oOverrideCondition: entry.getValue()) 
				oSortedCondition.add(oOverrideCondition);
		}
		
		return oSortedCondition;
	}
	
	//get the override condition with sorted by priority for panel override
	public List<PosOverrideCondition> getConditionWithPriorityForPanel() {
		List<PosOverrideCondition> oSortedCondition = new ArrayList<PosOverrideCondition>();
		HashMap<Integer, List<PosOverrideCondition>> oOverrideConditions = new HashMap<Integer, List<PosOverrideCondition>>();
		for(PosOverrideCondition oOverrideCondition:m_oPosOverrideConditionList) {
			if(oOverrideCondition.getDpanId() != 0) {
				if(oOverrideConditions.containsKey(oOverrideCondition.getPriority()) == false) 
					oOverrideConditions.put(oOverrideCondition.getPriority(), new ArrayList<PosOverrideCondition>());
				oOverrideConditions.get(oOverrideCondition.getPriority()).add(oOverrideCondition);
			}
		}
		
		//Sorting the conditions by priority
		Map<Integer, List<PosOverrideCondition>> oSortedMap = new TreeMap<Integer, List<PosOverrideCondition>>(
				new Comparator<Integer>() {
	 
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}
	 
			});
		oSortedMap.putAll(oOverrideConditions);
		for(Map.Entry<Integer, List<PosOverrideCondition>> entry: oSortedMap.entrySet()){
			for(PosOverrideCondition oOverrideCondition: entry.getValue()) 
				oSortedCondition.add(oOverrideCondition);
		}
		
		return oSortedCondition;
	}
	
	//get the override condition with sorted by priority for takeout ordering type override
	public List<PosOverrideCondition> getConditionWithPriorityForOvrrideOrderingType() {
		List<PosOverrideCondition> oSortedCondition = new ArrayList<PosOverrideCondition>();
		HashMap<Integer, List<PosOverrideCondition>> oOverrideConditions = new HashMap<Integer, List<PosOverrideCondition>>();
		for(PosOverrideCondition oOverrideCondition:m_oPosOverrideConditionList) {
			if(oOverrideCondition.getCheckOrderingType().equals(PosOverrideCondition.CHECK_ORDERING_TYPE_TAKEOUT)) {
				if(oOverrideConditions.containsKey(oOverrideCondition.getPriority()) == false) 
					oOverrideConditions.put(oOverrideCondition.getPriority(), new ArrayList<PosOverrideCondition>());
				oOverrideConditions.get(oOverrideCondition.getPriority()).add(oOverrideCondition);
			}
		}
		
		//Sorting the conditions by priority
		Map<Integer, List<PosOverrideCondition>> oSortedMap = new TreeMap<Integer, List<PosOverrideCondition>>(
				new Comparator<Integer>() {
	 
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}
	 
			});
		oSortedMap.putAll(oOverrideConditions);
		for(Map.Entry<Integer, List<PosOverrideCondition>> entry: oSortedMap.entrySet()){
			for(PosOverrideCondition oOverrideCondition: entry.getValue()) 
				oSortedCondition.add(oOverrideCondition);
		}
		
		return oSortedCondition;
	}
}
