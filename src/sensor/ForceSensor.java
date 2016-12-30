package sensor;

import com.phidgets.*;
import com.phidgets.event.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ForceSensor {
	InterfaceKitPhidget ik;
	String output_file;
	String result_file;
	ForceSensorChangeListener sensor_listener;
	
	//constructor
	public 	ForceSensor(int serial_number, String output_file, String result_file) throws Exception{
	    
	    System.out.println(Phidget.getLibraryVersion()); 

	    this.ik = new InterfaceKitPhidget();
	    this.result_file = result_file;
	    //make output file
	    this.output_file = output_file;
	    BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
	    
	    String data = "time, sensor0, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7";

	    initialize_output(output, data);

	  	event_declare(this.ik); 
		sensor_listener = new ForceSensorChangeListener();
		ik.addSensorChangeListener(sensor_listener);
		
		ik.open(serial_number);

	    System.out.println("waiting for Forcesensor attachment...");
	    ik.waitForAttachment();

	    System.out.println(Arrays.toString(sensor_listener.value_list));
	    //System.out.println(ik.getDeviceName());

	    Thread.sleep(500);
		
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
        BufferedWriter result = new BufferedWriter(new FileWriter(result_file));

        initialize_result(result, this.sensor_listener, data, sdf, time);
        result.close();
        
	}
	
	public int[] forceSensing() throws Exception{
       
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
    	long current_time = System.currentTimeMillis();
		
    	Thread.yield();
        BufferedWriter result = new BufferedWriter(new FileWriter(result_file, true));

		result.write(sdf.format(current_time).toString() + ", ");
		int[] from_to_section = new int[2];
		from_to_section=find_section(sensor_listener.value_list, sensor_listener.prev_value_list);
        for(int i=0; i<7; i++)
        {
        	result.write(String.valueOf(sensor_listener.value_list[i]));
        	result.write(", ");
        }
        result.write(String.valueOf(sensor_listener.value_list[7]));
        result.write(", " + from_to_section[0]);
        result.write(", " + from_to_section[1]);
        result.newLine();
        sensor_listener.state=false;
		System.out.println("result = " + Arrays.toString(sensor_listener.value_list) + ", from secton = " + from_to_section[0] + ", to section = " + from_to_section[1]);
        System.arraycopy(sensor_listener.value_list, 0, sensor_listener.prev_value_list, 0, 8);			
	
    	
    	result.close();
    	
    	return from_to_section;
    	
	}
	
	
	public static int[] find_section(int[] value_list, int[] prev_value){
		
		int total_delta=0;
		int max_pos=0;
		int min_pos=0;
		int[] delta_value = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] ret_value = {-1, -1};
		
		for (int i=1; i<9; i++)
		{
			total_delta += Math.abs(value_list[i-1]-prev_value[i-1]);
			delta_value[i] = value_list[i-1] - prev_value[i-1];
		}
		
		System.out.println("total delta: "+total_delta);
		if(total_delta < 50)
		{
			return ret_value;
		}
		
		for (int i=0; i<9; i++)
		{
			if(delta_value[max_pos]<delta_value[i])
			{
				if(delta_value[i] > 50)
					max_pos = i;
			}
			
			if(delta_value[min_pos]>delta_value[i])
			{
				if(delta_value[i] < -50)
					min_pos = i;
			}
		}
		ret_value[0] = min_pos-1;
		ret_value[1] = max_pos-1;
		return ret_value;
	}
	
	//이거는 이제 필요 없음. 신경쓰지 마3
	public static void make_result (BufferedWriter result, ForceSensorChangeListener sensor_listener, SimpleDateFormat sdf) throws IOException
	{
	    long current_time = System.currentTimeMillis();
		
    	Thread.yield();

    	if(sensor_listener.state==true)
    	{
	    		if(current_time-sensor_listener.complete_time>=2000)  //if 2secs are passed from completion
	    		{
	    			result.write(sdf.format(current_time).toString() + ", ");
	    			//sensor_listener.curr_section=find_section(sensor_listener.value_list, sensor_listener.prev_value_list, sensor_listener.curr_section);
	    	        for(int i=0; i<7; i++)
	    	        {
	    	        	result.write(String.valueOf(sensor_listener.value_list[i]));
	    	        	result.write(", ");
	    	        }
	    	        result.write(String.valueOf(sensor_listener.value_list[7]));
	    	        result.write(", " + sensor_listener.curr_section);
	    	        result.newLine();
	    	        sensor_listener.state=false;
	    			System.out.println("result = " + Arrays.toString(sensor_listener.value_list) + ", secton = " + sensor_listener.curr_section);
	    	        System.arraycopy(sensor_listener.value_list, 0, sensor_listener.prev_value_list, 0, 8);
	    		}
	    			
    	}
    	
	}
	
	public static void initialize_output (BufferedWriter output, String data) throws IOException{

	    output.write(data);
	    output.newLine();
	    
	    output.flush();
	    output.close();
	}
	
	
	public static void initialize_result (BufferedWriter result, ForceSensorChangeListener sensor_listener, String data, SimpleDateFormat sdf, Date time) throws IOException{
		result.write(data);
        result.write(", from section#, to section#");
        result.newLine();
	           
        result.write(sdf.format(time).toString() + ", ");
        for(int i=0; i<7; i++)
        {
        	result.write(String.valueOf(sensor_listener.value_list[i]));
        	result.write(", ");
        }
        result.write(String.valueOf(sensor_listener.value_list[7]));
        result.newLine();
        
	}
	
	public static void event_declare(InterfaceKitPhidget ik){
		
		   ik.addAttachListener(new AttachListener() {
		      public void attached(AttachEvent ae) {
		        System.out.println("attachment of " + ae);
		      }
		    });
		    ik.addDetachListener(new DetachListener() {
		      public void detached(DetachEvent ae) {
		        System.out.println("detachment of " + ae);
		      }
		    });

		    ik.addErrorListener(new ErrorListener() {
		      public void error(ErrorEvent ee) {
		        System.out.println(ee);
		      }
		    });

		    ik.addInputChangeListener(new InputChangeListener() {
		      public void inputChanged(InputChangeEvent oe) {
		        //System.out.println(oe);
		      }
		    });
		    ik.addOutputChangeListener(new OutputChangeListener() {
		      public void outputChanged(OutputChangeEvent oe) {
		       // System.out.println(oe);
		      }
		    });
		    //SensorChangeListener에 대한 정의는 ForceSensorChangeListener.java에 새롭게 정의되어 있음.
//
//		    ik.addSensorChangeListener(new SensorChangeListener() {
//		      public void sensorChanged(SensorChangeEvent se) {
//		        System.out.println(se);
//		      }
//		    });		    
		
	}
	
	public void close() throws Exception{
		System.out.println("Closing...");
	    ik.close();
	    ik = null;
	}
	
}


