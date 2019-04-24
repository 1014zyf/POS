package app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;

interface FrameTableFloorPlanFunctionBarListener {
	void frameFloorPlanFunctionBar_openPanelImageClicked();
	void frameFloorPlanFunctionBar_buttonClicked(String sNote);
}

public class FrameTableFloorPlanFunctionBar extends VirtualUIFrame {
	TemplateBuilder m_oTemplateBuilder;

	private boolean m_bExtendBar;
	List<VirtualUILabel> m_oFunctionLabelList;
	VirtualUILabel m_oFunctionLabel;

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameTableFloorPlanFunctionBarListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameTableFloorPlanFunctionBarListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameTableFloorPlanFunctionBarListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public void init(List<FuncLookupButtonInfo> oData, int iTotalPageNum){
		// Load form from template file
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameTableFloorPlanFunctionBarListener>();
		m_oFunctionLabelList = new ArrayList<VirtualUILabel>();
		m_bExtendBar = false;

		m_oTemplateBuilder.loadTemplate("fraTableFloorPlanFunctionBar.xml");

		this.createMenuLookupButtons(oData, iTotalPageNum);

		this.updateBasketExtendBarImage(false);
	}

	public void updateBasketExtendBarImage(boolean bExtendBar) {
		m_bExtendBar = bExtendBar;
	}

	//Create Lookup buttons
	public void createMenuLookupButtons(List<FuncLookupButtonInfo> oData, int iTotalPageNum){
		int iOffset = 0;
		int iCnt = 0;

		for(FuncLookupButtonInfo oTempMap : oData) {
			int iId = oTempMap.getId();
			
			JSONObject oJSONObject = new JSONObject();
			try {
				oJSONObject.put(FrameLookupButton.BUTTON_NOTE_ID, iId);
				if(oTempMap.getParameter() != null) {
					oJSONObject.put(FrameLookupButton.BUTTON_NOTE_NAME, oTempMap.getName());
					String sParameter = oTempMap.getParameter();
					JSONObject oParameter = new JSONObject(sParameter);
					oJSONObject.put(FrameLookupButton.BUTTON_NOTE_PARAMETER, oParameter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			VirtualUILabel oFunctionLabel = new VirtualUILabel();
			m_oTemplateBuilder.buildLabel(oFunctionLabel, "lblFunction");
			oFunctionLabel.setLeft(iOffset);
			oFunctionLabel.allowClick(true);
			oFunctionLabel.setClickServerRequestNote(oJSONObject.toString());
			oFunctionLabel.setValue(oTempMap.getName());
			this.attachChild(oFunctionLabel);
			
			iOffset += oFunctionLabel.getWidth();

			VirtualUIFrame oFrameWhiteCol = new VirtualUIFrame();
			m_oTemplateBuilder.buildFrame(oFrameWhiteCol, "fraWhiteCol");
			oFrameWhiteCol.setLeft(iOffset);
			this.attachChild(oFrameWhiteCol);
			
			iOffset += oFrameWhiteCol.getWidth();

			m_oFunctionLabelList.add(oFunctionLabel);
			
			iCnt++;
			if (iCnt == 3)
				break;
		}

		if (iTotalPageNum < 2 && oData.size() <= 3) 
			return;

		m_oFunctionLabel = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oFunctionLabel, "lblFunction");
		m_oFunctionLabel.setLeft(iOffset);
		m_oFunctionLabel.allowClick(true);
		m_oFunctionLabel.setValue(AppGlobal.g_oLang.get()._("more", ""));
		m_oFunctionLabel.setClickServerRequestBlockUI(false);
		this.attachChild(m_oFunctionLabel);

		m_oFunctionLabelList.add(m_oFunctionLabel);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;

		if (m_oFunctionLabel != null && m_oFunctionLabel.getId() == iChildId) {
			for (FrameTableFloorPlanFunctionBarListener listener : listeners) {
				// Raise the event to parent
				listener.frameFloorPlanFunctionBar_openPanelImageClicked();
			}

			boolean bShow = !m_bExtendBar;
			updateBasketExtendBarImage(bShow);
			bMatchChild = true;
		} else {
			for(VirtualUILabel oButtonClicked : m_oFunctionLabelList) {
				if(oButtonClicked.getId() == iChildId) {
					for(FrameTableFloorPlanFunctionBarListener listener : listeners) {
						listener.frameFloorPlanFunctionBar_buttonClicked(sNote);
						break;
					}
	
					return true;
				}
			}
		}

		return bMatchChild;
	}
}
