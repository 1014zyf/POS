package om;

import java.util.HashMap;

import org.json.JSONArray;

public class MedMediaList {
	private HashMap<Integer, MedMedia> m_oMedMediaList;
	
	public MedMediaList() {
		m_oMedMediaList = new HashMap<Integer, MedMedia>();
	}
	
	//read medias by ids
	public void readMediasByIdList(HashMap<Integer, Integer> oMediaIdList) {
		MedMedia oMediaList = new MedMedia();
		JSONArray responseJSONArray = null;
		
		responseJSONArray = oMediaList.readMediasByIdList(oMediaIdList);
		for (int i = 0; i < responseJSONArray.length(); i++) {
			if (responseJSONArray.isNull(i))
				continue;
			
			MedMedia oMedia = new MedMedia(responseJSONArray.optJSONObject(i));
			m_oMedMediaList.put(oMedia.getMediId(), oMedia);
		}
		
		return;
	}
	
	//get media form list
	public MedMedia getMediaById(int iMediaId) {
		return m_oMedMediaList.get(iMediaId);
	}
}
