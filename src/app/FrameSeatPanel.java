package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIHorizontalList;
import virtualui.VirtualUILabel;

/** interface for the listeners/observers callback method */
interface FrameSeatPanelListener {
	void frameSeatPanel_selectedSeat(int iSeatId);
	void frameSeatPanel_clickCourse();
}

public class FrameSeatPanel extends VirtualUIFrame implements FrameCommonPageContainerListener {
	TemplateBuilder m_oTemplateBuilder;

	private int m_iSelectedSeatNo;

	private VirtualUILabel m_oLabelSeatNoHeader;

	private FrameCommonPageContainer m_oSeatListContainer;
	private VirtualUILabel m_oLabelSelectedSeat;
	private VirtualUILabel m_oLabelUnselectedSeat;
	private VirtualUIHorizontalList m_oHorizontalSeatList;
	private ArrayList<VirtualUIFrame> m_oSeatNumList;
	private ArrayList<VirtualUILabel> m_oSeatNumLabelList;
	private VirtualUILabel m_oLabelCourse;

	private String m_sPanelType;

	public static String TYPE_SEAT_PANEL = "s";
	public static String TYPE_NUMPAD = "n";
	public static String TYPE_SEAT_PANEL_NUMPAD = "sn";

	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameSeatPanelListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameSeatPanelListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameSeatPanelListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameSeatPanel(String sType) {
		m_sPanelType = sType;

		m_oTemplateBuilder = new TemplateBuilder();
		m_oSeatNumList = new ArrayList<VirtualUIFrame>();
		m_oSeatNumLabelList = new ArrayList<VirtualUILabel>();
		listeners = new ArrayList<FrameSeatPanelListener>();

		m_iSelectedSeatNo = 0;

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraSeatPanel.xml");

		// Add Seat No. Header
		m_oLabelSeatNoHeader = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSeatNoHeader, "lblSeatNoHeader");
		m_oLabelSeatNoHeader.setValue(AppGlobal.g_oLang.get()._("seat_no", ""));
		this.attachChild(m_oLabelSeatNoHeader);

		m_oLabelSelectedSeat = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelSelectedSeat, "lblSelectedSeatNo");
		this.attachChild(m_oLabelSelectedSeat);

		m_oLabelUnselectedSeat = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelUnselectedSeat, "lblUnselectSeatNo");
		this.attachChild(m_oLabelUnselectedSeat);

		VirtualUIFrame oFraSeatNo = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(oFraSeatNo, "fraSeatNo");

		// Common Page Container
		m_oSeatListContainer = new FrameCommonPageContainer();
		m_oTemplateBuilder.buildFrame(m_oSeatListContainer, "fraListSeats");

		int iContainerWidth = m_oSeatListContainer.getWidth();
		
		if (AppGlobal.g_sDisplayMode.get().equals(AppGlobal.DISPLAY_MODE.horizontal_desktop.name())) {
			if (sType.equals(TYPE_SEAT_PANEL_NUMPAD))
				iContainerWidth = 350;
			m_oSeatListContainer.init(iContainerWidth, m_oSeatListContainer.getHeight(), oFraSeatNo.getWidth(),
					m_oSeatListContainer.getHeight(), 8, m_oLabelSelectedSeat.getForegroundColor(),
					m_oLabelUnselectedSeat.getForegroundColor(), m_oLabelSelectedSeat.getBackgroundColor(),
					m_oLabelUnselectedSeat.getBackgroundColor(), 38, false, false);
			m_oSeatListContainer.setTagTextSize(m_oLabelSelectedSeat.getTextSize(),
					m_oLabelUnselectedSeat.getTextSize());
			m_oSeatListContainer.showBackGroundImage(true);
		} else {
			if (sType.equals(TYPE_SEAT_PANEL_NUMPAD))
				iContainerWidth = 380;
			m_oSeatListContainer.init(iContainerWidth, oFraSeatNo.getHeight(), oFraSeatNo.getWidth(),
					m_oSeatListContainer.getHeight(), 6, m_oLabelSelectedSeat.getForegroundColor(),
					m_oLabelUnselectedSeat.getForegroundColor(), m_oLabelSelectedSeat.getBackgroundColor(),
					m_oLabelUnselectedSeat.getBackgroundColor(), 0, false, false);
		}
		m_oSeatListContainer.addListener(this);
		this.attachChild(m_oSeatListContainer);

		// Add Seat horizontal list
		m_oHorizontalSeatList = new VirtualUIHorizontalList();
		m_oTemplateBuilder.buildHorizontalList(m_oHorizontalSeatList, "horListSeats");
		m_oHorizontalSeatList.setVisible(false);
		this.attachChild(m_oHorizontalSeatList);

		// Course description
		m_oLabelCourse = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCourse, "lblCourse");
		m_oLabelCourse.allowClick(true);
		m_oLabelCourse.setClickServerRequestBlockUI(false);
		this.attachChild(m_oLabelCourse);
	}

	public void addNumberOfSeat(int iNumberOfSeat) {
		int iStartNum = 0;
		if (m_sPanelType.equals(FrameSeatPanel.TYPE_NUMPAD)	|| m_sPanelType.equals(FrameSeatPanel.TYPE_SEAT_PANEL_NUMPAD))
			iStartNum = 1;

		for (int i = iStartNum; i <= iNumberOfSeat; i++) {
			VirtualUIFrame oFraSeatNo = new VirtualUIFrame();
			VirtualUILabel oLabelSeatNo = new VirtualUILabel();

			m_oTemplateBuilder.buildFrame(oFraSeatNo, "fraSeatNo");
			m_oTemplateBuilder.buildLabel(oLabelSeatNo, "lblSeatNo");

			oLabelSeatNo.setWidth(0);
			oLabelSeatNo.setHeight(0);

			if (i == 0) {
				oLabelSeatNo.setValue("S");
			} else {
				oLabelSeatNo.setValue("" + i);
			}
			oFraSeatNo.attachChild(oLabelSeatNo);

			oFraSeatNo.allowClick(true);
			oFraSeatNo.setClickServerRequestBlockUI(false);

			m_oSeatNumList.add(oFraSeatNo);
			m_oSeatNumLabelList.add(oLabelSeatNo);
			m_oHorizontalSeatList.attachChild(oFraSeatNo);
			VirtualUIFrame oFraSeatNo2 = new VirtualUIFrame();
			m_oSeatListContainer.addButton(oLabelSeatNo.getValue(), oFraSeatNo2);
		}
	}

	public void setSeatLabelColor(int iSeatNum) {
		if (m_sPanelType.equals(FrameSeatPanel.TYPE_NUMPAD)
				|| m_sPanelType.equals(FrameSeatPanel.TYPE_SEAT_PANEL_NUMPAD))
			return;
		m_oSeatListContainer.clickTag(iSeatNum);

		m_iSelectedSeatNo = iSeatNum;
	}

	public void setQtySeatLabelColor(int iSeatNum) {
		m_oSeatListContainer.clickTag(iSeatNum);
		m_iSelectedSeatNo = iSeatNum;
	}

	public void resetSelectedSeat() {
		// Reset all label color
		VirtualUILabel oLabelSelectedSeatNo = m_oSeatNumLabelList.get(m_iSelectedSeatNo);
		oLabelSelectedSeatNo.setForegroundColor(m_oLabelUnselectedSeat.getForegroundColor());
		// Reset all label background
		VirtualUIFrame oFrameSelectedSeatNo = m_oSeatNumList.get(m_iSelectedSeatNo);
		oFrameSelectedSeatNo.setBackgroundColor(m_oLabelUnselectedSeat.getBackgroundColor());
		m_oSeatListContainer.showFirstTag();

		m_iSelectedSeatNo = 0;
	}

	public void setHeaderValue(String[] sValue) {
		m_oLabelSeatNoHeader.setValue(sValue);
	}

	public void setCourseDescription(String[] sDesc) {
		m_oLabelCourse.setValue(sDesc);
	}

	public String getPanelType() {
		return this.m_sPanelType;
	}

	public int getSelectedSeatNo() {
		return m_iSelectedSeatNo;
	}

	public int getSeatLabel(int iIndex) {
		return Integer.valueOf(m_oSeatNumLabelList.get(iIndex).getValue());
	}

	public void setShowHorizontalSeatListOnly(int iTop, int iLeft, int iHeight, int iWidth) {
		m_oSeatListContainer.setTop(iTop);
		m_oSeatListContainer.setLeft(iLeft);
		m_oSeatListContainer.setHeight(iHeight);
		m_oSeatListContainer.setWidth(iWidth);
		m_oSeatListContainer.bringToTop();
		m_oLabelCourse.setVisible(false);
		m_oLabelSeatNoHeader.setVisible(false);
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		if (iChildId == m_oLabelCourse.getId()) {
			for (FrameSeatPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameSeatPanel_clickCourse();
			}
			bMatchChild = true;
		} else {
			// Find the selected Seat No
			int iSeatId = -1;
			for (VirtualUIFrame oFraSeatNumber : m_oSeatNumList) {
				if (oFraSeatNumber.getId() == iChildId) {
					iSeatId = m_oSeatNumList.indexOf(oFraSeatNumber);
					bMatchChild = true;
					break;
				}
			}

			if (iSeatId > -1) {
				for (FrameSeatPanelListener listener : listeners) {
					// Raise the event to parent
					listener.frameSeatPanel_selectedSeat(iSeatId);
				}
			}
		}
		return bMatchChild;
	}

	@Override
	public void frameCommonPageContainer_changeFrame() {
	}

	@Override
	public void frameCommonPageContainer_updateFrame(int iOrigIndex, int iIndex, String sFrameName) {
		// TODO Auto-generated method stub
		if (iIndex > -1 && iOrigIndex != iIndex) {
			for (FrameSeatPanelListener listener : listeners) {
				// Raise the event to parent
				listener.frameSeatPanel_selectedSeat(iIndex);
			}
		}
	}
	
	@Override
	public boolean frameCommonPageContainer_swipe(boolean bLeft, String sNote, int iChildId) {
		return false;
	}

	@Override
	public void frameCommonPageContainer_CloseImageClicked() {

	}

	@Override
	public void frameCommonPageContainer_ClickButtonIndex(int iIndex) {
	}
}
