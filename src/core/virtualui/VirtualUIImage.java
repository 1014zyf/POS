package core.virtualui;

public class VirtualUIImage extends VirtualUIBasicElement {
	
	public VirtualUIImage(){
		super(HeroActionProtocol.View.Type.IMAGE);
    }
	
	public boolean setSource(String sSource) {
		setExist(true);		//	###################### for compatitable
		return super.setSource(sSource);
	}
}
