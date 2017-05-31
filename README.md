<div align="center">
  <img src="https://media.licdn.com/mpr/mpr/AAEAAQAAAAAAAAcoAAAAJDk1NDU4YWVhLTdjMjctNDEwYi1iZjVkLTE1Y2M3ZTFiMzE3MA.png" heigh="100" width="100"><br><br>
</div>

-----------------
# AppFinder

**AppFinder** is an open source java library which can find which divices is connected, that give you the **ip** of divices and divice **name** too at that time. AppFinder execution is asynchronously.  

## HOW IT;S WORK

   * I Use UDP protocol to finding the devices without any ip pinging.
   * That Send UDPSignal To All ip based on your LocalBased IP.
   * If There is in your network SignalHandler of AppFinder Available so they send signal back and AppFinder Catch that signal.
     
## on server 

```java 

MyAppFinder myAppFinder=new MyAppFinder(InetAddress.getLocalHost(),new OnMyAppFind() {
			
			@Override
			public void Onresponce(String name, String ip) {
			}
			
			@Override
			public void OnSignalSended() {
			}
			
			@Override
			public void OnListioningStop() {
			}
			
			@Override
			public void OnListioning() {
			}
});

myAppFinder.ExecuteCommand("command");
.....
....

myAppFinder.ExecuteCommand("command");


```
After executing command that send signal to all ips

## Command

    * start - to start send signal and listion signal of Signalhandler.
    * shutdown - shutdown sending signal and listioning.
    * sendsignal - if You wnat recheck so,execute sendsiganl command.
    * StartListion - if you just want to start listioning not sending signal. 
    
## on Client 

```java

SignalHandler s=new SignalHandler(new OnSignal() {
			
			@Override
			public void OnStart() {
				
			}
			
			@Override
			public void OnSignalSendback(int i) {
				
			}
			
			@Override
			public void OnClose() {
				
			}
			
			@Override
			public void OnCatch(String name, String ip) {
				
			}
      
		});
    s.startHandler();
```
This will listion signal of server and it send back 5 times with delay 2000 seconnds.

## try 

 [TensorFlow website](https://github.com/Mohit-Mamtora/AppFinder-Android/blob/master/AppFinderAndroid1.0/demo.java)
