package mohit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class SignalHandler {
	
	private OnSignal OnSignal;
	private volatile boolean IsRunnig=false;
	private volatile DatagramSocket ds;

	public SignalHandler(OnSignal OnSignal){
		this.OnSignal=OnSignal;
	}
	
	public boolean startHandler(){
		
		if (IsRunnig){
			return false;
		}
		new Thread(new Runnable() {
            @Override
            public void run() {
                try {
					ds= new DatagramSocket(9285);
					byte[] data = new byte[1];
					DatagramPacket dp = new DatagramPacket(data, data.length);
                    while(!ds.isClosed()) {
                        ds.receive(dp);
                        OnSignal.OnCatch(dp.getAddress().getHostName(),dp.getAddress().getHostAddress());
                        DatagramPacket dpp = new DatagramPacket(dp.getData(),dp.getLength(),dp.getAddress(),MyAppFinder.ListioningPORT);
                        System.out.println(dpp.getLength()+""+dp.getAddress());
                        int i=1;
                        while(i<5){
                            ds.send(dpp);
                            OnSignal.OnSignalSendback(i);
                            i++;
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                	IsRunnig=false;
                    System.out.println("SignalHandler: Stop");
                }
            }
        }).start();
		OnSignal.OnStart();
		IsRunnig=true;
        return true;
	}
	
	public void Shutdown(){
		ds.close();
		IsRunnig=false;
		OnSignal.OnClose();
	}
	public boolean IsRunnig(){
		return IsRunnig;
	}
}
