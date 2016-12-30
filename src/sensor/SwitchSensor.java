package sensor;

import com.phidgets.*;
import com.phidgets.event.*;
import client.*;


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
							//take a photo by using web cam. --> access to shell command.  // "sudo mplayer -vo png -frames 1 tv:///dev/video0"
							//send it to server via socket.
							try{
								//make file_name --> naming convention : 00000001.png, 00000002.png, ... ??
								String zero = "";
								photo_num ++;
								for(int zero_num=0; zero_num<7-photo_num/10; zero_num++)
								{
									zero = zero + "0";
								}
								//client.send_file( zero+String.valueOf(photo_num)+".png");
								client.send_file("00000001.png");
								int[] result_section = force.forceSensing();
								//send HTTP request (POST or PUT)
								//result_section[0] 은 원래의 위치, result_section[1]은 옮겨간 위치를 의미, 위치 값이 -1이면 냉장고에 없음을 의미
								//기존에 보관 중인 식품인 경우 먼저 원래 위치 값에 대하여 GET Request를 해서 id를 얻어낸 후, 해당 id에 대해서 PUT request로 position 값을 변경
								//새로 넣은 식품인 경우 POST request로 새로운 Data 저장
								//식품을 냉장고에서 제거한 경우 GET request로 id를 얻어내고, DELETE api/food/:food_id request로 data 삭제. 또는 DELETE api/foods/:food_position 을 새롭게 정의하여 사용할 수도 있음.
							
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

		//System.out.println(ik.getDeviceName());

		Thread.sleep(500);
	}
	
	public void close() throws Exception{
		ik.close();
		ik = null;
		System.out.println("Switch Closed");
	}
}
