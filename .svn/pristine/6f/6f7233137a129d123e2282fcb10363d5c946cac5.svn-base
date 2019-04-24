package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralStyle {
	private String sName;
	private int iTextSize;
	private String sTextAlign;
	private String sBackground;
	private String sForeground;
	private String sDescription;
	private String sDescription_chi;
	private String sDescription_cn;
	private JSONArray child;
	
	//init object with initialize value
	public GeneralStyle() {
		this.init();
	}
	public GeneralStyle(JSONObject oGeneralStyleJSONObject) {
		this.init();
		if(oGeneralStyleJSONObject != null)
		{
			try {
				if(!oGeneralStyleJSONObject.isNull("name"))
					sName = oGeneralStyleJSONObject.getString("name");
				if(!oGeneralStyleJSONObject.isNull("textSize"))
					iTextSize = oGeneralStyleJSONObject.getInt("textSize");
				if(!oGeneralStyleJSONObject.isNull("textAlign"))
					sTextAlign = oGeneralStyleJSONObject.getString("textAlign");
				if(!oGeneralStyleJSONObject.isNull("background"))
					sBackground = oGeneralStyleJSONObject.getString("background");
				if(!oGeneralStyleJSONObject.isNull("foreground"))
					sForeground = oGeneralStyleJSONObject.getString("foreground");
				if(!oGeneralStyleJSONObject.isNull("description"))
					sDescription = oGeneralStyleJSONObject.getString("description");
				if(!oGeneralStyleJSONObject.isNull("description_chi"))
					sDescription_chi = oGeneralStyleJSONObject.getString("description_chi");
				if(!oGeneralStyleJSONObject.isNull("description_cn"))
					sDescription_cn = oGeneralStyleJSONObject.getString("description_cn");
				if(!oGeneralStyleJSONObject.isNull("child"))
				{
					child = oGeneralStyleJSONObject.getJSONArray("child");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}
	
	// read all style
	public JSONArray getAllStyle() {
		JSONArray responseJSONArray = null;
		responseJSONArray = loadFile("style.txt");
		return responseJSONArray;
	}	
//testing method for testing to read default 
	public JSONArray loadFile(String sStyleFile){
		
		JSONArray responseJSONArray = new JSONArray();
		String line = "";	
		String templateDir = "";		
		try {
			// default use android tablet
			if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.vertical_mobile.name()))
				templateDir = System.getProperty("user.dir") + java.io.File.separator + "cfg" + java.io.File.separator + "template" + java.io.File.separator + "mobile";
			else
				templateDir = System.getProperty("user.dir") + java.io.File.separator + "cfg" + java.io.File.separator + "template";
			
			File styleFile = new File(templateDir + java.io.File.separator + sStyleFile);	

			// skip if target style file not found
			if (!styleFile.exists())
				return responseJSONArray;					
			BufferedReader br = new BufferedReader(new FileReader(styleFile));	
		    while ((line = br.readLine()) != null) {
	    		try {
	    			responseJSONArray = new JSONArray(line);
	    		}
	    		catch (JSONException e)
	    		{
	    			
	    		}
		    }
		    br.close();
		    return responseJSONArray;
			
		}
		catch (IOException ioe) {	
			ioe.printStackTrace();
		}
		 return responseJSONArray;
	}
	
	//init object
	public void init() {
		this.sName = "";
		this.iTextSize = 0;
		this.sTextAlign = "";
		this.sBackground = "";
		this.sForeground = "";
		this.sDescription = "";
		this.sDescription_chi = "";
		this.sDescription_cn = "";
		this.child = new JSONArray();
	}
	
	public String getName() {
		return sName;
	}

	public void setName(String name) {
		this.sName = name;
	}

	public int getTextSize() {
		return iTextSize;
	}

	public void setTextSize(int textSize) {
		this.iTextSize = textSize;
	}

	public String getTextAlign() {
		return sTextAlign;
	}

	public void setTextAlign(String textAlign) {
		this.sTextAlign = textAlign;
	}

	public String getBackground() {
		return sBackground;
	}

	public void setBackground(String background) {
		this.sBackground = background;
	}

	public String getForeground() {
		return sForeground;
	}

	public void setForeground(String foreground) {
		this.sForeground = foreground;
	}
	
	public String getsDescription() {
		return sDescription;
	}
	
	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}
	
	public String getsDescription_chi() {
		return sDescription_chi;
	}
	
	public void setsDescription_chi(String sDescription_chi) {
		this.sDescription_chi = sDescription_chi;
	}
	
	public String getsDescription_cn() {
		return sDescription_cn;
	}
	
	public void setsDescription_cn(String sDescription_cn) {
		this.sDescription_cn = sDescription_cn;
	}
	
	public JSONArray getChild() {
		return child;
	}
	
	public void setChild(JSONArray child) {
		this.child = child;
	}
	@Override
	public String toString() {
		return "GeneralStyle [sName=" + sName + ", iTextSize=" + iTextSize + ", sTextAlign=" + sTextAlign  + ", sBackground=" + sBackground + ", sForeground=" + sForeground + ", sDescription="
				+ sDescription + ", sDescription_chi=" + sDescription_chi + ", sDescription_cn=" + sDescription_cn
				+ ", child=" + child + "]";
	}	
}
	

