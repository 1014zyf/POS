package app;

import java.util.ArrayList;

import templatebuilder.TemplateBuilder;
import virtualui.VirtualUIFrame;
import virtualui.VirtualUILabel;
import virtualui.VirtualUIList;

/** interface for the listeners/observers callback method */
interface FrameCheckDiscountInfoListener {
}
 
public class FrameCheckDiscountInfo extends VirtualUIFrame{
	TemplateBuilder m_oTemplateBuilder;
	
	private VirtualUILabel m_oLabelDiscountItem;
	private VirtualUILabel m_oLabelCheckDiscount;
	private VirtualUIFrame m_oLabelCheckDiscountUnderLineTop;
	private VirtualUIFrame m_oLabelCheckDiscountUnderLineBottom;
	private VirtualUIList m_oListCheckDiscount;
	private VirtualUILabel m_oLabelExtraCharge;
	private VirtualUILabel m_oLabelCheckExtraCharge;
	private VirtualUIList m_oListCheckExtraCharge;
	private VirtualUIFrame m_oLabelCheckExtraChargeUnderLineTop;
	private VirtualUIFrame m_oLabelCheckExtraChargeUnderLineBottom;
	
	/** list of interested listeners (observers, same thing) */
    private ArrayList<FrameCheckDiscountInfoListener> listeners;
    
	/** add a new ModelListener observer for this Model */
    public void addListener(FrameCheckDiscountInfoListener listener) {
        listeners.add(listener);
    }
    
    /** remove a ModelListener observer for this Model */
    public void removeListener(FrameCheckDiscountInfoListener listener) {
        listeners.remove(listener);
    }
    
    /** remove all ModelListener observer for this Model */
    public void removeAllListener() {
    	listeners.clear();
    }
	
	public void init() {
		m_oTemplateBuilder = new TemplateBuilder();
		listeners = new ArrayList<FrameCheckDiscountInfoListener>();
		
		// Load form from template file
		m_oTemplateBuilder.loadTemplate("fraCheckDiscountInfo.xml");
		
		//Basic description label for check discount
		m_oLabelCheckDiscount = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckDiscount, "lblCheckDiscount");
		m_oLabelCheckDiscount.setValue(AppGlobal.g_oLang.get()._("check_discount", ""));
		this.attachChild(m_oLabelCheckDiscount);
		m_oLabelCheckDiscountUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oLabelCheckDiscountUnderLineTop, "lblCheckDiscountUnderLineTop");
		this.attachChild(m_oLabelCheckDiscountUnderLineTop);
		m_oLabelCheckDiscountUnderLineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oLabelCheckDiscountUnderLineBottom, "lblCheckDiscountUnderLineBottom");
		this.attachChild(m_oLabelCheckDiscountUnderLineBottom);
		
		//Check discount list
		m_oListCheckDiscount = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListCheckDiscount, "listCheckDiscount");	
		m_oListCheckDiscount.setTop(m_oLabelCheckDiscountUnderLineBottom.getTop()+m_oLabelCheckDiscountUnderLineBottom.getHeight()+5);
		m_oListCheckDiscount.setLeft(20);
		//m_oListCheckDiscount.setHeight(this.getHeight()-m_oLabelCheckDiscount.getTop()-m_oLabelCheckDiscount.getHeight()
		//								-m_oLabelCheckDiscountUnderLineTop.getTop()-m_oLabelCheckDiscountUnderLineTop.getHeight()
		//								-m_oLabelCheckDiscountUnderLineBottom.getHeight());
		m_oListCheckDiscount.setWidth(this.getWidth()-30);
		m_oListCheckDiscount.allowLongClick(true);
		m_oListCheckDiscount.allowClick(true);
		m_oListCheckDiscount.allowSwipeBottom(true);
		m_oListCheckDiscount.allowSwipeTop(true);
		this.attachChild(m_oListCheckDiscount);
		
		//Basic description label for extra charge
		m_oLabelCheckExtraCharge = new VirtualUILabel();
		m_oTemplateBuilder.buildLabel(m_oLabelCheckExtraCharge, "lblCheckExtraCharge");
		m_oLabelCheckExtraCharge.setValue(AppGlobal.g_oLang.get()._("check_extra_charge", ""));
		this.attachChild(m_oLabelCheckExtraCharge);
		m_oLabelCheckExtraChargeUnderLineTop = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oLabelCheckExtraChargeUnderLineTop, "lblCheckExtraChargeUnderLineTop");
		this.attachChild(m_oLabelCheckExtraChargeUnderLineTop);
		m_oLabelCheckExtraChargeUnderLineBottom = new VirtualUIFrame();
		m_oTemplateBuilder.buildFrame(m_oLabelCheckExtraChargeUnderLineBottom, "lblCheckExtraChargeUnderLineBottom");
		this.attachChild(m_oLabelCheckExtraChargeUnderLineBottom);
		
		//Check extra charge list
		m_oListCheckExtraCharge = new VirtualUIList();
		m_oTemplateBuilder.buildList(m_oListCheckExtraCharge, "listCheckExtraCharge");	
		m_oListCheckExtraCharge.setTop(m_oLabelCheckExtraChargeUnderLineBottom.getTop()+m_oLabelCheckExtraChargeUnderLineBottom.getHeight()+5);
		m_oListCheckExtraCharge.setLeft(20);
		
		m_oListCheckExtraCharge.setWidth(this.getWidth()-30);
		m_oListCheckExtraCharge.allowLongClick(true);
		m_oListCheckExtraCharge.allowClick(true);
		m_oListCheckExtraCharge.allowSwipeBottom(true);
		m_oListCheckExtraCharge.allowSwipeTop(true);
		this.attachChild(m_oListCheckExtraCharge);
	
	}
	
	public void addDiscountItem(String[] sDiscountInfo){
		m_oLabelDiscountItem = new VirtualUILabel();
		m_oLabelDiscountItem.setExist(true);
		m_oLabelDiscountItem.setTop(5);
		m_oLabelDiscountItem.setLeft(0);
		m_oLabelDiscountItem.setWidth(m_oListCheckDiscount.getWidth());
//KingsleyKwan20170918ByPaul		-----Start-----
		m_oLabelDiscountItem.setHeight(25);
		m_oLabelDiscountItem.setTextSize(15);
		m_oLabelDiscountItem.setForegroundColor("#FF333333");
//KingsleyKwan20170918ByPaul		----- End -----
		m_oLabelDiscountItem.setValue(sDiscountInfo);
		m_oListCheckDiscount.attachChild(m_oLabelDiscountItem);	
	}
	
	public void addExtraChargeItem(String[] sExtraChargeInfo){
		m_oLabelExtraCharge = new VirtualUILabel();
		m_oLabelExtraCharge.setExist(true);
		m_oLabelExtraCharge.setTop(5);
		m_oLabelExtraCharge.setLeft(0);
		m_oLabelExtraCharge.setWidth(m_oListCheckExtraCharge.getWidth());
		m_oLabelExtraCharge.setHeight(25);
		m_oLabelExtraCharge.setTextSize(15);
		m_oLabelExtraCharge.setForegroundColor("#FF333333");
		m_oLabelExtraCharge.setValue(sExtraChargeInfo);
		m_oListCheckExtraCharge.attachChild(m_oLabelExtraCharge);
	}
	
	public void removeAllDiscountItem(){
		m_oListCheckDiscount.removeAllChildren();
	}
	
	public void removeAllExtraChargeItem(){
		m_oListCheckExtraCharge.removeAllChildren();
	}
	
	public int getCheckListCount(){
		return m_oListCheckDiscount.getChildCount();
	}
	
	public int getExtraChargeListCount(){
		return m_oListCheckExtraCharge.getChildCount();
	}
}

