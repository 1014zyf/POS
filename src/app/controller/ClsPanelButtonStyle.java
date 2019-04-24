package app.controller;

import org.json.JSONException;
import org.json.JSONObject;

public class ClsPanelButtonStyle {
	private int[] font_size;
	private String[] font_color;
	private String[] font_weight;
	private String[] font_decoration;
	private String background_color;
	private String border_color;
	private String border_style;
	private String background_media_url;
	private String icon_media_url;
	
	public ClsPanelButtonStyle(JSONObject oPanelButtonStyleJSONObject) {
		font_size = new int[10];
		font_color = new String[10];
		font_weight = new String[10];
		font_decoration = new String[10];

		try {
			for(int i = 0; i < 10; i++) {
				JSONObject oFontJSONObject = oPanelButtonStyleJSONObject.getJSONObject("font"+(i+1));
				font_size[i] = oFontJSONObject.getInt("size");
				font_color[i] = oFontJSONObject.getString("color");
				font_weight[i] = oFontJSONObject.getString("weight");
				font_decoration[i] = oFontJSONObject.getString("decoration");
			}
			
			background_color = oPanelButtonStyleJSONObject.getString("background_color");
			border_color = oPanelButtonStyleJSONObject.getString("border_color");
			border_style = oPanelButtonStyleJSONObject.getString("border_style");
		} catch (JSONException e) {
			AppGlobal.stack2Log(e);
		}
	}
	
	public String getBackgroundColor() {
		return this.background_color;
	}
	
	public String getBorderColor() {
		return this.border_color;
	}
	
	public String getBorderStyle() {
		return this.border_style;
	}
	
	public String getFontColor(int iIndex) {
		return this.font_color[(iIndex-1)];
	}
	
	public int getFontSize(int iIndex) {
		return this.font_size[(iIndex-1)];
	}
}
