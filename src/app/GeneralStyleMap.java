package app;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralStyleMap {

	private HashMap<String, GeneralStyle> m_oStyleList;
	
	public GeneralStyleMap(){
		m_oStyleList = new HashMap<String, GeneralStyle>();
	}
	
	//read all from style txt or pos config
	public void getAllStyleFromSources(JSONArray responseJSONArray) {		
		try {
			for(int i = 0; i < responseJSONArray.length(); i++) {
				JSONObject oHeroStyle = responseJSONArray.getJSONObject(i);
				if(oHeroStyle != null)
				{
					GeneralStyle oStyle = new GeneralStyle(oHeroStyle);
					//Edit or create style object to memory
					if(m_oStyleList.get(oStyle.getName()) != null)
					{
						if(!oStyle.getBackground().equals(""))
							m_oStyleList.get(oStyle.getName()).setBackground(oStyle.getBackground());
						if(!oStyle.getForeground().equals(""))
							m_oStyleList.get(oStyle.getName()).setForeground(oStyle.getForeground());
						if(oStyle.getTextSize() != 0)
							m_oStyleList.get(oStyle.getName()).setTextSize(oStyle.getTextSize());
						if(!oStyle.getTextAlign().equals(""))
							m_oStyleList.get(oStyle.getName()).setTextAlign(oStyle.getTextAlign());
					}
					else
						m_oStyleList.put(oStyle.getName(), oStyle);
					
					this.getChildStyle(oStyle);
					
//					if(oStyle.getChild().length() > 0)
//					{
//						for(int j = 0; j < oStyle.getChild().length(); j++)
//						{
//							JSONObject oChildStyle = oStyle.getChild().getJSONObject(j);
//							if(oChildStyle != null)
//							{
//								GeneralStyle oChildrenStyle = new GeneralStyle(oChildStyle);
//								if(oChildrenStyle.getName() != null)
//								{
//									if(oChildrenStyle.getBackground().equals(""))
//										oChildrenStyle.setBackground(oStyle.getBackground());
//									if(oChildrenStyle.getForeground().equals(""))
//										oChildrenStyle.setForeground(oStyle.getForeground());
//									if(oChildrenStyle.getTextSize() == 0)
//										oChildrenStyle.setTextSize(oStyle.getTextSize());
//									if(oChildrenStyle.getTextAlign().equals(""))
//										oChildrenStyle.setTextAlign(oStyle.getTextAlign());
//									m_oStyleList.put(oChildrenStyle.getName(), oChildrenStyle);
//									if(oChildrenStyle.getChild().length() > 0)
//									{
//										for(int k = 0; k < oChildrenStyle.getChild().length(); k++)
//										{									
//											JSONObject oLayerChildStyle = oChildrenStyle.getChild().getJSONObject(k);
//											if(oLayerChildStyle != null)
//											{
//												GeneralStyle oLayerChildrenStyle = new GeneralStyle(oLayerChildStyle);
//												if(oLayerChildrenStyle.getName() != null)
//												{
//													if(oLayerChildrenStyle.getBackground().equals(""))
//														oLayerChildrenStyle.setBackground(oChildrenStyle.getBackground());
//													if(oLayerChildrenStyle.getForeground().equals(""))
//														oLayerChildrenStyle.setForeground(oChildrenStyle.getForeground());
//													if(oLayerChildrenStyle.getTextSize() == 0)
//														oLayerChildrenStyle.setTextSize(oChildrenStyle.getTextSize());
//													if(oLayerChildrenStyle.getTextAlign().equals(""))
//														oLayerChildrenStyle.setTextAlign(oChildrenStyle.getTextAlign());	
//													m_oStyleList.put(oLayerChildrenStyle.getName(), oLayerChildrenStyle);
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
				}
			}
		}catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	// recursive for getting childs styling
	public void getChildStyle(GeneralStyle oParentStyle){
		if(oParentStyle.getChild().length() > 0){
			for(int i = 0; i < oParentStyle.getChild().length(); i++)
			{
				JSONObject oLayerChildStyle = new JSONObject();
				try {
					oLayerChildStyle = oParentStyle.getChild().getJSONObject(i);
				} catch (JSONException e) {
					AppGlobal.stack2Log(e);
				}
				if(oLayerChildStyle != null)
				{
					GeneralStyle oLayerChildrenStyle = new GeneralStyle(oLayerChildStyle);
					if(oLayerChildrenStyle.getName() != null)
					{
						if(oLayerChildrenStyle.getBackground().equals(""))
							oLayerChildrenStyle.setBackground(oParentStyle.getBackground());
						if(oLayerChildrenStyle.getForeground().equals(""))
							oLayerChildrenStyle.setForeground(oParentStyle.getForeground());
						if(oLayerChildrenStyle.getTextSize() == 0)
							oLayerChildrenStyle.setTextSize(oParentStyle.getTextSize());
						if(oLayerChildrenStyle.getTextAlign().equals(""))
							oLayerChildrenStyle.setTextAlign(oParentStyle.getTextAlign());	
						m_oStyleList.put(oLayerChildrenStyle.getName(), oLayerChildrenStyle);
						this.getChildStyle(oLayerChildrenStyle);
					}
				}
			}
		}
	}

	public HashMap<String, GeneralStyle> getStyleList(){
		if(m_oStyleList == null)
			return null;
		else
			return m_oStyleList;
	}
}
