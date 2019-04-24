package core.virtualui;

public class VirtualUIKeyboardReader extends VirtualUIBasicElement {
	
	public VirtualUIKeyboardReader(){
		super(HeroActionProtocol.View.Type.KEYBOARD_READER);
		
		// Must show
		this.setExist(true);
    }
}
