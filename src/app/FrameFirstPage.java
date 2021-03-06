package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import commonui.FormConfirmBox;
import commonui.FrameNumberPad;
import commonui.FrameNumberPadListener;
import commonui.FrameTitleHeader;
import commonui.FrameTitleHeaderListener;
import core.virtualui.HeroActionProtocol.View.Attribute.KeyboardType;
import externallib.StringLib;
import templatebuilder.TemplateBuilder;
import virtualui.HeroActionProtocol;
import virtualui.VirtualUIButton;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUIImage;
import virtualui.VirtualUILabel;
import virtualui.VirtualUITextbox;
import virtualui.VirtualUIWebView;

/** interface for the listeners/observers callback method */
interface FrameFirstPageListener {
	void frameFirstPage_clickOK();
}

public class FrameFirstPage extends VirtualUIFrame
		implements FrameTitleHeaderListener, FrameNumberPadListener, FrameCommonBasketListener {
	TemplateBuilder m_oTemplateBuilder;

	private VirtualUIFrame m_oFrameLeftContent;
	private VirtualUILabel m_oLabelMessage;
	private VirtualUIWebView m_oWebViewCheck;
	private VirtualUIButton m_oButtonOK;
	private FrameTitleHeader m_oTitleHeader;
	private VirtualUITextbox m_otxtboxId;
	private FrameNumberPad m_oFrameNumberPad;
	private FrameCommonBasket m_oFrameItemList;
	private VirtualUIFrame m_oFramePage;
	private VirtualUIImage m_oImgButtonPrevPage;
	private VirtualUIImage m_oImgButtonNextPage;
	private VirtualUILabel m_oLblPage;
	private HashMap<String, Object> m_oiLineCount = null;
	private HashMap<String, ArrayList> m_oiLine;
	private utilArray m_outilArray = null;
	// 当前页
	int m_oiCurrentPage = 1;
	private String m_oTxtboxIdValue = "";
	/** list of interested listeners (observers, same thing) */
	private ArrayList<FrameFirstPageListener> listeners;

	/** add a new ModelListener observer for this Model */
	public void addListener(FrameFirstPageListener listener) {
		listeners.add(listener);
	}

	/** remove a ModelListener observer for this Model */
	public void removeListener(FrameFirstPageListener listener) {
		listeners.remove(listener);
	}

	/** remove all ModelListener observer for this Model */
	public void removeAllListener() {
		listeners.clear();
	}

	public FrameFirstPage() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameFirstPageListener>();

		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraFirstPage.xml");

		// Header
		m_oTitleHeader = new FrameTitleHeader();
		m_oTemplateBuilder.buildFrame(m_oTitleHeader, "fraTitleHeader");
		m_oTitleHeader.addListener(this);
		m_oTitleHeader.init(false);
		m_oTitleHeader.setTitle(AppGlobal.g_oLang.get()._("report"));
		this.attachChild(m_oTitleHeader);

		// InputBox Message
		/*
		 * m_oLabelMessage = new VirtualUILabel();
		 * m_oTemplateBuilder.buildLabel(m_oLabelMessage, "lblMessage");
		 * m_oLabelMessage.setTop(10); m_oLabelMessage.setWidth(this.getWidth() -
		 * ((m_oLabelMessage.getLeft() + this.getStroke()) * 2));
		 * this.attachChild(m_oLabelMessage);
		 */

		// Review Area
		m_oWebViewCheck = new VirtualUIWebView();
		m_oTemplateBuilder.buildWebView(m_oWebViewCheck, "wbvReport");
		this.attachChild(m_oWebViewCheck);

		// OK button
		m_oButtonOK = new VirtualUIButton();
		m_oTemplateBuilder.buildButton(m_oButtonOK, "btnOK");
		m_oButtonOK.setValue(AppGlobal.g_oLang.get()._("exit"));
		m_oButtonOK.setVisible(true);
		this.attachChild(m_oButtonOK);

		// Create Textbox
		m_otxtboxId = new VirtualUITextbox();
		m_oTemplateBuilder.buildTextbox(m_otxtboxId, "txtBoxID");
		m_otxtboxId.setKeyboardType(KeyboardType.NUMBER);
		/* m_otxtboxId.setInputType(InputType.PASSWORD); */
		/* 光标定位 */
		m_otxtboxId.setFocusWhenShow(true);
		this.attachChild(m_otxtboxId);

		// create Number Pad
		m_oFrameNumberPad = new FrameNumberPad();
		m_oTemplateBuilder.buildFrame(m_oFrameNumberPad, "NumberPad");
		m_oFrameNumberPad.init();
		m_oFrameNumberPad.addListener(this);
		m_oFrameNumberPad.setEnterSubmitId(m_otxtboxId);
		m_oFrameNumberPad.setVisible(true);
		this.attachChild(m_oFrameNumberPad);

		// create left
		/*
		 * m_oFrameLeftContent = new VirtualUIFrame();
		 * m_oTemplateBuilder.buildFrame(m_oFrameLeftContent, "LeftContent");
		 * this.attachChild(m_oFrameLeftContent);
		 */
		// Result list
		m_oFrameItemList = new FrameCommonBasket();
		m_oTemplateBuilder.buildFrame(m_oFrameItemList, "fraItemList");
		m_oFrameItemList.init();
		m_oFrameItemList.addListener(this);
		/* m_oFrameLeftContent.attachChild(m_oFrameItemList); */
		// Add header
		ArrayList<Integer> iFieldWidths = new ArrayList<Integer>();
		ArrayList<String> sFieldValues = new ArrayList<String>();
		iFieldWidths.add(100);
		sFieldValues.add("Line no.");
		iFieldWidths.add(100);
		sFieldValues.add("Input");
		iFieldWidths.add(400);
		sFieldValues.add("Response");
		m_oFrameItemList.addHeader(iFieldWidths, sFieldValues);
		m_oFrameItemList.setHeaderFormat(35, 18, "5,0,0,5");
		m_oFrameItemList.setBottomUnderlineVisible(true);
		/* 设置上层线可见 */
		m_oFrameItemList.setUpperlineVisible(false);
		m_oFrameItemList.setVisible(true);
		this.attachChild(m_oFrameItemList);
		// Create Page label
		m_oFramePage = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oFramePage, "fraPage");

		// Create prev page button
		m_oImgButtonPrevPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonPrevPage, "ImgPrevPage");
		m_oImgButtonPrevPage.allowClick(true);
		m_oImgButtonPrevPage.setClickServerRequestBlockUI(false);
		m_oImgButtonPrevPage.allowLongClick(true);
		m_oImgButtonPrevPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonPrevPage);

		// Create next page button
		m_oImgButtonNextPage = new VirtualUIImage();
		m_oTemplateBuilder.buildImage(m_oImgButtonNextPage, "ImgNextPage");
		m_oImgButtonNextPage.allowClick(true);
		m_oImgButtonNextPage.setClickServerRequestBlockUI(false);
		m_oImgButtonNextPage.allowLongClick(true);
		m_oImgButtonNextPage.setLongClickServerRequestBlockUI(false);
		this.attachChild(m_oImgButtonNextPage);

		m_oLblPage = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLblPage, "lblPage");
		m_oLblPage.setWidth(m_oFramePage.getWidth());
		m_oLblPage.setHeight(m_oFramePage.getHeight());
		m_oFramePage.attachChild(m_oLblPage);
		this.attachChild(m_oFramePage);

		String[] arr = { "Line no.", "Input", "Response" };
		m_oFrameItemList.addSection(0, StringLib.createStringArray(3, arr), false);
		m_outilArray = new utilArray();
	}

	@Override
	public boolean clicked(int iChildId, String sNote) {
		boolean bMatchChild = false;
		showPageDown(true);
		showPageUp(true);
		m_oLblPage.setValue((m_oiCurrentPage) + " / " + m_outilArray.MaxPage());
		for (FrameFirstPageListener frameFirstPageListener : listeners) {
			if (iChildId == m_oImgButtonPrevPage.getId()) {
				if (m_oiCurrentPage <= m_outilArray.MaxPage()) {
					System.out.println("oldPage"+m_oiCurrentPage);
					addOrDeductionPage("-");
					System.out.println("Page"+m_oiCurrentPage);
					this.NextPage(m_oiCurrentPage);
					bMatchChild = true;
				}
			} else if (iChildId == m_oImgButtonNextPage.getId()) {
				if (m_outilArray.MaxPage() != 1) {
					addOrDeductionPage("+");
					System.out.println("newPage"+m_oiCurrentPage);
					this.NextPage(m_oiCurrentPage);
					bMatchChild = true;
				}
			} else if (iChildId == m_oButtonOK.getId()) {
				frameFirstPageListener.frameFirstPage_clickOK();
				bMatchChild = true;
				break;
			}
		}

		return bMatchChild;
	}

	public void addOrDeductionPage(String type) {
		if (type.equals("+")) {
			if (!(m_oiCurrentPage + 1 > m_outilArray.MaxPage())) {
				m_oiCurrentPage++;
			}
		} else if (!(m_oiCurrentPage - 1 < 1)) {
			--m_oiCurrentPage;
		}
		m_oLblPage.setValue((m_oiCurrentPage) + " / " + m_outilArray.MaxPage());
	}

	@Override
	public void FrameTitleHeader_close() {
		for (FrameFirstPageListener listener : listeners)
			listener.frameFirstPage_clickOK();
	}

	@Override
	public void FrameNumberPad_clickEnter() {
		/*
		 * m_oTxtboxIdValue = m_otxtboxId.getValue(); String setValue = "";
		 * if(Integer.parseInt(m_oTxtboxIdValue)%2==0) { setValue = "EVEN"; }else
		 * setValue = "ODD"; // TODO Auto-generated method stub //每增加十行，换一页
		 * if(m_oiLineCount.size()>=10&&m_oiLineCount.size()%10==0) {
		 * removeCheckListRecord(); m_oLblPage.setValue((++m_oiCurrentPage) + " / " +
		 * getMaxPage()); } addItem(setValue,LineNo); if(LineNo>=10) { int iMaxNumber =
		 * LineNo/10; setPageNumber(m_oiCurrentPage,iMaxNumber); showPageUp(true);
		 * showPageDown(true); }
		 * 
		 * LineNo++;
		 */
		String m_oValueTypu = "";
		m_oTxtboxIdValue = m_otxtboxId.getValue();
		m_oValueTypu = m_otxtboxId.getValue();
		if (Integer.parseInt(m_oTxtboxIdValue) % 2 == 0)
			m_oValueTypu = "EVEN";
		else
			m_oValueTypu = "ODD";
		if (m_outilArray.KeySize() >= 10 && m_outilArray.KeySize() % 10 == 0) {
			m_oiCurrentPage++;
			m_oFrameItemList.removeAllItems();
			this.addItem(m_oValueTypu);
		} else if (m_outilArray.KeySize() % 10 != 0) {
			this.addItem(m_oValueTypu);
		} else if (m_outilArray.KeySize() <= 10) {
			this.addItem(m_oValueTypu);
		}

	}

	public void addItem(String setValue) {
		m_oiLine = new HashMap<String, ArrayList>();
		int LineNo = m_outilArray.KeySize() + 1;

		ArrayList<Object> iFieldWidths = new ArrayList<Object>();
		ArrayList<Object> sFieldValues = new ArrayList<Object>();
		ArrayList<Object> sFieldAligns = new ArrayList<Object>();
		ArrayList<Object> sFieldTypes = new ArrayList<Object>();
		iFieldWidths.add(100);
		sFieldValues.add(LineNo + "");
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(100);
		sFieldValues.add(m_oTxtboxIdValue);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);
		iFieldWidths.add(400);
		sFieldValues.add(setValue);
		sFieldAligns.add("");
		sFieldTypes.add(HeroActionProtocol.View.Type.LABEL);

		m_oiLine.put("iFieldWidths", iFieldWidths);
		m_oiLine.put("sFieldValues", sFieldValues);
		m_oiLine.put("sFieldAligns", sFieldAligns);
		m_oiLine.put("sFieldTypes", sFieldTypes);

		m_outilArray.add(LineNo, m_oiLine);

		
		if(m_oiCurrentPage == m_outilArray.MaxPage()) {
			int itemIndex = m_outilArray.getKeyIndex(LineNo) % 10;
			m_oFrameItemList.addItem(0, itemIndex, 45, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		}
		

	}

	// 翻页专用
	public void NextPage(int page) {
		utilArray array = m_outilArray.getValueByPage(page);
		m_oFrameItemList.removeAllItems();
		System.out.println(array.KeySize());
		for (int i = 0; i < array.KeySize(); i++) {
			HashMap<String, ArrayList> map = array.getValueByIndex(i);
			ArrayList<Integer> iFieldWidths = map.get("iFieldWidths");
			ArrayList<String> sFieldValues = map.get("sFieldValues");
			ArrayList<String> sFieldAligns = map.get("sFieldAligns");
			ArrayList<String> sFieldTypes = map.get("sFieldTypes");
			m_oFrameItemList.addItem(0, i, 45, iFieldWidths, sFieldValues, sFieldAligns, sFieldTypes, null);
		}

	}

	public ArrayList parsingArray(String type, ArrayList<Object> list) {
		ArrayList array = null;
		if ("iFieldWidths".equals(type)) {
			for (Object object : list) {
				array = new ArrayList<Integer>();
				array.add(object);
			}
		} else {
			for (Object object : list) {
				array = new ArrayList<String>();
				array.add(object);
			}
		}
		return array;
	}

	@Override
	public void FrameNumberPad_clickCancel() {
		// TODO Auto-generated method stub
	}

	@Override
	public void FrameNumberPad_clickNumber(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void frameCommonBasketSection_SectionClicked(int iSectionId, String sNote) {
		// TODO Auto-generated method stub
		System.out.println("iSectionId:" + iSectionId);
	}

	@Override

	public void frameCommonBasketCell_FieldClicked(int iBasketId, int iSectionIndex, int iItemIndex, int iFieldIndex,
			String sNote) {
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 0, "#707070");
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 1, "#707070");
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 2, "#707070");
		FormConfirmBox confirmBox = new FormConfirmBox("YES", "NO", super.getParentForm());
		confirmBox.setTitle("Confirmation");
		confirmBox.setMessage("confirm to delete?");
		confirmBox.show();
		if (confirmBox.isOKClicked()) {
			m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 0, "#FFFFFF");
			m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 1, "#FFFFFF");
			m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 2, "#FFFFFF");
			int key = 0;
			if (m_outilArray.MaxPage() == 1)
				key = iItemIndex + 1;
			else
				key = (m_oiCurrentPage - 1) * 10 + iItemIndex + 1;

			m_outilArray.deleteAndUpdate(key);

			if (m_outilArray.KeySize() % 10 == 0)
				this.addOrDeductionPage("-");

			this.NextPage(m_oiCurrentPage);
		}
		if (confirmBox.isTimeout()) {

		}
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 0, "#FFFFFF");
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 1, "#FFFFFF");
		m_oFrameItemList.setFieldBackgroundColor(iSectionIndex, iItemIndex, 2, "#FFFFFF");
	}

	public void frameCommonBasketCell_FieldLongClicked(int iBasketId, int iSectionIndex, int iItemIndex,
			int iFieldIndex, String sNote) {
		// TODO Auto-generated method stub
	}

	@Override
	public void frameCommonBasketCell_HeaderClicked(int iFieldIndex) {
		// TODO Auto-generated method stub

	}

	public void showPageUp(boolean bShow) {
		if (bShow)
			m_oImgButtonPrevPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_left_button.png");
		else
			m_oImgButtonPrevPage.setSource(
					AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pageprevious_disabled.png");
		m_oImgButtonPrevPage.setEnabled(bShow);
	}

	public void showPageDown(boolean bShow) {
		if (bShow)
			m_oImgButtonNextPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/swipe_right_button.png");
		else
			m_oImgButtonNextPage
					.setSource(AppGlobal.g_oTerm.get().getClientImageURLPath() + "/buttons/icon_pagenext_disabled.png");
		m_oImgButtonNextPage.setEnabled(bShow);
	}

	public void setPageNumber(int iNumber, int iMaxNumber) {
		if (iNumber > 0) {
			m_oFramePage.setVisible(true);
			m_oLblPage.setValue(iNumber + " / " + getMaxPage());
			m_oLblPage.setVisible(true);
			m_oImgButtonPrevPage.setVisible(true);
			m_oImgButtonNextPage.setVisible(true);
		} else {
			m_oFramePage.setVisible(false);
			m_oImgButtonPrevPage.setVisible(false);
			m_oImgButtonNextPage.setVisible(false);
		}
	}

	public void updateCheckListRecord() {
		if (m_oiCurrentPage != 1) {
			m_oiCurrentPage--;
		}
		removeCheckListRecord();
		// readMapAddItem();
		m_oLblPage.setValue((m_oiCurrentPage) + " / " + getMaxPage());
	}

	public void updatePageUpDownVisibility() {
		if (m_oiCurrentPage <= getMaxPage()) {
			m_oiCurrentPage++;
			removeCheckListRecord();
			// readMapAddItem();
			m_oLblPage.setValue(m_oiCurrentPage + " / " + getMaxPage());
		}

	}

	public void removeCheckListRecord() {
		m_oFrameItemList.removeAllItems();
	}

	public int getMaxPage() {
		if (m_oiLineCount.size() / 10 == 0) {
			return 1;
		} else if (m_oiLineCount.size() % 10 != 0) {
			return m_oiLineCount.size() / 10 + 1;
		} else
			return m_oiLineCount.size() / 10;
	}
	// After deleting the row,update m_oiLineCount
	/*
	 * public void UpdateMap() { HashMap<Integer, Object> map = new HashMap<Integer,
	 * Object>(); Set set = m_oiLineCount.keySet(); Iterator<Integer> it =
	 * set.iterator(); for (int i = 1; i <= m_oiLineCount.size(); i++) { int j =
	 * it.next(); map.put(i, m_oiLineCount.get(j)); HashMap m = (HashMap)
	 * m_oiLineCount.get(j); ArrayList<String> list = (ArrayList<String>)
	 * m.get("sFieldValues"); System.out.println("line:"+list.get(0)); }
	 * m_oiLineCount.clear(); m_oiLineCount = map;
	 * 
	 * }
	 */

	class utilArray {
		private Vector<Integer> Key = null;
		private Vector<HashMap<String, ArrayList>> Value = null;

		utilArray() {
			Key = new Vector<Integer>();
			Value = new Vector<HashMap<String, ArrayList>>();
		}

		private void add(Integer key, HashMap<String, ArrayList> value) {
			Key.add(key);
			Value.add(value);
		}

		// Deletes the corresponding key and value by subscript of the key, and updates
		// the sort
		private void deleteAndUpdate(int key) {
			int i = Key.indexOf(key);
			Key.remove(i);
			Value.remove(i);
			for (Integer in : Key) {
				if(Key.indexOf(in)>=i) {
					int index = Key.indexOf(in);
					Key.set(index, in - 1);
					HashMap<String, ArrayList> map = Value.get(index);
					ArrayList<String> list = map.get("sFieldValues");
					list.set(0, in-1+"");
				}
			}
			
		}

		// Get Key Size
		private int KeySize(utilArray Key) {
			return Key.Key.size();
		}

		private int KeySize() {
			return Key.size();
		}

		// The corresponding value is obtained by the key
		private HashMap<String, ArrayList> getValueByKeyIndex(int keyIndex) {
			int index = Key.indexOf(keyIndex);
			if (Value.get(index).isEmpty()) {
				return Value.get(index);
			} else
				return null;
		}

		// I get the index of the key
		private int getKeyIndex(int key) {
			return Key.indexOf(key);
		}

		private int getKeyByindex(int index) {
			return Key.get(index);
		}

		private HashMap<String, ArrayList> getValueByIndex(int index) {
			HashMap<String, ArrayList> map = Value.get(index);
			return map;
		}

		private utilArray getValueByPage(int page) {
			utilArray returnArray = new utilArray();
			if (1 == page && KeySize() <= 10) {
				for (int i = 0; i < KeySize(); i++) {
					int key = getKeyByindex(i);
					HashMap<String, ArrayList> value = getValueByIndex(i);
					returnArray.add(key, value);
				}
			} else if (MaxPage() == page) {
				for (int i = page*10-10; i < KeySize(); i++) {
					int key = getKeyByindex(i);
					HashMap<String, ArrayList> value = getValueByIndex(i);
					returnArray.add(key, value);
				}
			} else  {
				for (int i = 0; i < 10; i++) {
					int key = getKeyByindex(page * 10 - 10 + i);
					HashMap<String, ArrayList> value = getValueByIndex(page * 10 - 10 + i);
					returnArray.add(key, value);
				}
			}
			return returnArray;
		}

		private int MaxPage() {
			int maxPage = 0;
			if (KeySize() % 10 == 0)
				maxPage = KeySize() / 10;
			else
				maxPage = KeySize() / 10 + 1;
			return maxPage;
		}
	}
}
