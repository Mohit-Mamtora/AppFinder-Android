/*
 * @author MOHIT MAMTORA
 *   
 *  
 */

package mohit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class MyAppFinder {

	private String subnet="";
	private boolean IsShutdown=true;
	private volatile boolean SIGNALSEING=false;
	private volatile boolean  IsListioning=false;
	public static final int PORT =9285;
	public static final int ListioningPORT =9286;
	private volatile DatagramSocket dss=null;
	private volatile DatagramSocket ds;
	private final OnMyAppFind OnMyAppFind;
	
	public MyAppFinder(InetAddress i,OnMyAppFind OnMyAppFind){
		String t =i.getHostAddress();
		this.subnet=t.substring(0,(t.indexOf('.',t.indexOf(".")+1)));
		this.OnMyAppFind=OnMyAppFind;
	}
	
	public void ExecuteCommand(String cmd){
            switch (cmd) {
                case "shutdown":
                    stop();
                    break;
                case "start":
                    IsShutdown=false;
                    sendSignal();
        			SignalListioning();
                    break;
                case "sendsignal":
                    IsShutdown=false;
                    sendSignal();
                    
                    break;
                case "StartListion":
                    IsShutdown=false;
                    SignalListioning();
                    
                    break;
                default:
                    System.err.println("MyAppFinder: COMMAND NOT FOUND");
                    break;
            }
	}
	
	private void sendSignal(){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
			
			try {
				
				ds=new DatagramSocket();
				byte[] data =new byte[1];
				
				DatagramPacket dp= new DatagramPacket(data, data.length);
				dp.setPort(PORT);
				
				StringBuilder sub=new StringBuilder(subnet);
				int subnetSize=sub.length();
				
				for (int i = 0; i <=254; i++) {
					for (int j = 0; j <=254; j++) {
							dp.setAddress(InetAddress.getByName(sub.append('.').append(i).append('.').append(j).toString()));
							ds.send(dp);
							sub.delete(subnetSize, sub.length());
							Thread.sleep(0,5);
					}
				}
				System.gc();
				OnMyAppFind.OnSignalSended();
				
				} catch (IOException | InterruptedException  e) {
					SIGNALSEING=false;
				}
			}
		},"Signal Sending").start();
		SIGNALSEING=true;
		System.err.println("MyAppFinder: SENDING SIGNALS");
	}
	
	private void SignalListioning() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
						dss=new DatagramSocket(ListioningPORT);
						byte[]responceData=new byte[1];
						DatagramPacket dp=new DatagramPacket(responceData, responceData.length);
						while(!dss.isClosed()){
								dss.receive(dp);
								OnMyAppFind.Onresponce(dp.getAddress().getHostName(),dp.getAddress().getHostAddress());
						}
					} catch (IOException e) {
						if (e.getMessage().contains("Cannot bind")){
							System.err.println(e.getMessage());
						}
						IsListioning=false;
						OnMyAppFind.OnListioningStop();
					}
				}
			},"Listioning").start();
		IsListioning=true;
		OnMyAppFind.OnListioning();
		System.err.println("MyAppFinder: START LISTIONING ON "+ListioningPORT);
	}
	
	private void stop(){
		dss.close();
		ds.close();
		IsShutdown=true;
		System.err.println("MyAppFinder: SHUTINGDOWN");
	}
	
	public boolean IsShutdown(){
		return IsShutdown;
	}
	
	public boolean IsSignalSendingRunning(){
		return SIGNALSEING;
	}
	
	public boolean IsListioning(){
		return IsListioning;
	}
}
