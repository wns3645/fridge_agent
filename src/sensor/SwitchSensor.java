package sensor;

import com.phidgets.*;
import com.phidgets.event.*;
import client.*;
import java.io.*;


public class SwitchSensor
{
	InterfaceKitPhidget ik;
	int value;
	Client client;
	ForceSensor force;
	static int photo_num;
	
	public SwitchSensor(int serial_number, Client client, ForceSensor force) throws Exception {
		//System.out.println(Phidget.getLibraryVersion());

		this.ik = new InterfaceKitPhidget();
		this.value = 0;
		this.client = client;
		this.force = force;
		this.photo_num = 0;
		
		//event handler
		//Override to SensorChangeListener 
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				//System.out.println("attachment of " + ae);
			}
		});
		ik.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				//System.out.println("detachment of " + ae);
			}
		});
		ik.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				//System.out.println(ee);
			}
		});
		ik.addInputChangeListener(new InputChangeListener() {
			public void inputChanged(InputChangeEvent oe) {
				//System.out.println(oe);
			}
		});
		ik.addOutputChangeListener(new OutputChangeListener() {
			public void outputChanged(OutputChangeEvent oe) {
				//System.out.println(oe);
			}
		});
		ik.addSensorChangeListener(new SensorChangeListener() {
			public void sensorChanged(SensorChangeEvent se)  {
				//System.out.println(se);
				
				if(se.getIndex() == 0){
					int curr_value = se.getValue();

					if(curr_value < 50)
					{
						System.out.println("Fridge opened.");
					}
					else if(curr_value >900)
					{
						if(value < 50)
						{
							System.out.println("Fridge closed");
							//do something...
							
							//make file_name --> naming convention : 00000001.png, 00000002.png, ... ??
							String zero = "";
							photo_num ++;
							for(int zero_num=0; zero_num<7-photo_num/10; zero_num++)
							{
								zero = zero + "0";
							}
							//take a photo by using web cam. --> access to shell command.  // 
							try {
								//Runtime.getRuntime().exec("sudo mplayer -vo png -frames 1 tv:///dev/video0");
								ik.setOutputState(0, true);
								Runtime.getRuntime().exec("sudo fswebcam --no-banner "+zero+String.valueOf(photo_num)+".png");
								Thread.sleep(3000);
								ik.setOutputState(0, false);
							} catch (Exception e)
							{
								e.printStackTrace();
							}
							//send it to server via socket.
							try{								
								client.send_file( zero+String.valueOf(photo_num)+".png");
								
								int[] result_section = force.forceSensing();
								//send HTTP request (POST, PUT, or DELETE)
								if(result_section[0]==-1)
								{
									if(result_section[1]!=-1)
									{
										HttpConnection.postFood(result_section[1], zero+String.valueOf(photo_num)+".png");
									}
								}
								else
								{
									if(result_section[1]!=-1)
									{
										HttpConnection.putFoodPosition(result_section[0], result_section[1]);
									}
									else
									{
										HttpConnection.deleteFoodPosition(result_section[0]);
									}
								}
							}
							catch(Exception e){
							}
						}
					}
					value = curr_value;
				}
			}
		});

		ik.open(serial_number);
		System.out.println("waiting for Switch attachment...");
		ik.waitForAttachment();
		ik.setOutputState(0, false);

		//System.out.println(ik.getDeviceName());

		Thread.sleep(500);
	}
	
	public void close() throws Exception{
		ik.close();
		ik = null;
		System.out.println("Switch Closed");
	}
}
