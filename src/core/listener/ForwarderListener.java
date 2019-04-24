package core.listener;

public interface ForwarderListener {
	void onForward(String sValue);
	void onFinish();
	void onDisconnect();
	void onTimeout();
	void onCancel();
}
