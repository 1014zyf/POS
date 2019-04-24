package core.virtualui;


//Ref protocol ver: 2015/10/08
public class HeroActionProtocol {
public class View {
	public static final String KEY = "v";

	public class Id {
		public static final String KEY = "i";
		
		public static final String ROOT = "_ROOT";
		public static final String FOCUS = "_FOCUS";
	}
	public class Type {
		public static final String KEY = "t";
		
		public static final String FRAME = "frame";
		public static final String SCROLL_FRAME = "scroll_frame";
		public static final String LABEL = "label";
		public static final String TEXTBOX = "textbox";
		public static final String BUTTON = "button";
		public static final String LIST = "list";
		public static final String HORIZONTAL_LIST = "horizontal_list";
		public static final String IMAGE = "image";
		public static final String WEB_VIEW = "web_view";
		public static final String SWIPE_CARD_READER = "swipe_card_reader";
		public static final String KEYBOARD_READER = "keyboard_reader";
		public static final String FORWARDER = "forwarder";
		public static final String CLOCK_LABEL = "clock_label";
		public static final String SELECT = "select";
	}
	public class ParentId {
		public static final String KEY = "pi";
	}
	public class ValueMirrorId {
		public static final String KEY = "vm";
	}
	
	public class Attribute {
		public static final String KEY = "a";
		
		public class Width {
			public static final String KEY = "w";
		}
		public class Height {
			public static final String KEY = "h";
		}
		public class Padding {
			public static final String KEY = "p";
		}
		public class ContentWidth {
			public static final String KEY = "cw";
		}
		public class ContentHeight {
			public static final String KEY = "ch";
		}
		public class Top {
			public static final String KEY = "t";
		}
		public class Left {
			public static final String KEY = "l";
		}
		public class Value {
			public static final String KEY = "v";
		}
		public class Background {
			public static final String KEY = "b";
		}
		public class Gradient {
			public static final String KEY = "g";
		}
		public class CornerRadius {
			public static final String KEY = "cr";
		}
		public class Stroke {
			public static final String KEY = "s";
		}
		public class StrokeColor {
			public static final String KEY = "sc";
		}
		public class Foreground {
			public static final String KEY = "f";
		}
		public class TextAlign {
			public static final String KEY = "ta";
			
			public static final String TOP = "top";
			public static final String BOTTOM = "bottom";
			public static final String LEFT = "left";
			public static final String RIGHT = "right";
			public static final String CENTER = "center";
			public static final String CENTER_VERTICAL = "center_vertical";
			public static final String CENTER_HORIZONTAL = "center_horizontal";
		}
		public class TextStyle {
			public static final String KEY = "tst";
			
			public static final String BOLD = "bold";
			public static final String ITALIC = "italic";
			public static final String AUTO_SIZE = "auto_size";
		}
		public class TextSize {
			public static final String KEY = "ts";
		}
		public class Visible {
			public static final String KEY = "vi";
		}
		public class Enabled {
			public static final String KEY = "e";
		}
		public class ViewSeq {
			public static final String KEY = "vs";
		}
		public class InputType {
			public static final String KEY = "it";

			public static final String DEFAULT = "default";
			public static final String PASSWORD = "password";
		}
		public class KeyboardType {
			public static final String KEY = "kt";

			public static final String DEFAULT = "default";
			public static final String URL = "url";
			public static final String NUMBER = "number";
			public static final String PHONE = "phone";
			public static final String EMAIL = "email";
			public static final String DECIMAL = "decimal";
			public static final String PLAIN = "plain";
			public static final String KEYCODE = "keycode";
		}
		public class Multiline {
			public static final String KEY = "m";
		}
		public class Source {
			public static final String KEY = "so";
		}
		public class ContentMode {
			public static final String KEY = "cm";
			
			public static final String SCALE_TO_FIT = "scale_to_fit";
			public static final String SCALE_ASPECT_FIT = "scale_aspect_fit";
			public static final String SCALE_ASPECT_FIT_CENTER = "scale_aspect_fit_center";
			public static final String SCALE_ASPECT_FILL = "scale_aspect_fill";
		}
		public class Hint {
			public static final String KEY = "hi";
		}
		public class Selection {
			public static final String KEY = "se";
		}
		public class SelectionAllowMultiple {
			public static final String KEY = "sem";
		}
	}
	
	public class Event {
		public static final String KEY = "e";
		
		public class Click {
			public static final String KEY = "c";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class LongClick {
			public static final String KEY = "lc";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class SwipeRight {
			public static final String KEY = "sr";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class SwipeLeft {
			public static final String KEY = "sl";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class SwipeTop {
			public static final String KEY = "st";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class SwipeBottom {
			public static final String KEY = "sb";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class ValueChanged {
			public static final String KEY = "vc";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class Keyboard {
			public static final String KEY = "k";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class KeyCode {
				public static final String KEY = "k";
			} 
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class Timer {
			public static final String KEY = "t";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			public class Time {
				public static final String KEY = "t";
			}
			public class Repeat {
				public static final String KEY = "r";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class Idle {
			public static final String KEY = "i";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			public class Time {
				public static final String KEY = "t";
			}
			public class Repeat {
				public static final String KEY = "r";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
		}
		
		public class Forward {
			public static final String KEY = "f";
			
			public class Id {
				public static final String KEY = "i";
			}
			public class Cancel {
				public static final String KEY = "c";
			}
			public class Enabled {
				public static final String KEY = "e";
			}
			public class Action {
				public static final String KEY = "a";
			}
			
			public class ServerRequest {
				public static final String KEY = "sr";
				
				public class Note {
					public static final String KEY = "n";
				}
				public class SubmitId {
					public static final String KEY = "si";
				}
				public class BlockUI {
					public static final String KEY = "bu";
				}
				public class Async {
					public static final String KEY = "a";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
			}
			
			public class ForwardRequest {
				public static final String KEY = "fr";
				
				public class Type {
					public static final String KEY = "t";
					
					public static final String SOCKET = "socket";
				}
				public class Address {
					public static final String KEY ="a";
				}
				public class Port {
					public static final String KEY ="p";
				}
				public class Value {
					public static final String KEY = "v";
				}
				public class ValueType {
					public static final String KEY = "vt";
					
					public static final String STRING = "string";
					public static final String KEYCODE = "keycode";
				}
				public class Timeout {
					public static final String KEY = "to";
				}
				public class Delay {
					public static final String KEY = "d";
				}
//				public class BlockUI {
//					public static final String KEY = "bu";
//				}
			}
		}
	}
	
	public class Do {
		public static final String KEY = "d";
		
		public class Remove {
			public static final String KEY = "r";
		}
		public class RemoveChildren {
			public static final String KEY = "rc";
		}
		public class Top {
			public static final String KEY = "t";
		}
		public class Bottom {
			public static final String KEY = "b";
		}
		public class Focus {
			public static final String KEY = "f";
		}
		
		public class ReplaceValue {
			public static final String KEY = "rv";
			
			public class Regex {
				public static final String KEY = "r";
			}
			public class Value {
				public static final String KEY = "v";
			}
			
			public static final String SELECT_START_TAG = "<select>";
			public static final String SELECT_END_TAG = "</select>";
		}
		public class ScrollTo {
			public static final String KEY = "st";
			
			public class Position {
				public static final String KEY = "p";
				
				public static final String TOP = "top";
				public static final String BOTTOM = "bottom"; 
			}
			public class Index {
				public static final String KEY = "i";
			}
		}
		public class TriggerEvent {
			public static final String KEY = "te";
			
			public class Type {
				public static final String KEY = "t";
				
				public static final String CLICK = Event.Click.KEY;
				public static final String LONG_CLICK = Event.LongClick.KEY;
				public static final String SWIPE_RIGHT = Event.SwipeRight.KEY;
				public static final String SWIPE_LEFT = Event.SwipeLeft.KEY;
				public static final String SWIPE_TOP = Event.SwipeTop.KEY;
				public static final String SWIPE_BOTTOM = Event.SwipeBottom.KEY;
				public static final String VALUE_CHANGED = Event.ValueChanged.KEY;
				public static final String TIMER = Event.Timer.KEY;
				public static final String IDLE = Event.Idle.KEY;
				public static final String FORWARD = Event.Forward.KEY;
			}
			
			public class Delay {
				public static final String KEY = "d";
			}
		}
	}
}

public class System {
	public static final String KEY = "s";
	
	public class Logout {
		public static final String KEY = "l";
	}
	public class HideKeyboard {
		public static final String KEY = "hk";
	}
	public class ShowKeyboard {
		public static final String KEY = "sk";
	}
	public class SetSetting {
		public static final String KEY = "ss";
		
		public static final String NAME = "n";
		public static final String VALUE = "v";
	}
	public class CopyToClipboard {
		public static final String KEY = "c";
		
		public static final String VALUE = "v";
	}
	public class RefreshUdid {
		public static final String KEY = "r";
	}
}
}

