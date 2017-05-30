package mohit;

public interface OnSignal {

	void OnCatch(String name,String ip);
	void OnClose();
	void OnStart();
	void OnSignalSendback(int i);

}
