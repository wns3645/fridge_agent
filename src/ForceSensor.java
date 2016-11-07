
import com.phidgets.*;
import com.phidgets.event.*;

import java.io.*;
import java.text.SimpleDateFormat;

import listener.*;

import java.util.Arrays;
import java.util.Date;

public class ForceSensor {

	
	public static final void main(String args[]) throws Exception{
	    InterfaceKitPhidget ik;
	    InterfaceKitPhidget ik2;
	    
	    System.out.println(Phidget.getLibraryVersion()); 

	    ik = new InterfaceKitPhidget();
	    ik2 = new InterfaceKitPhidget();
	    
	    //make output file
	    BufferedWriter output1 = new BufferedWriter(new FileWriter("./output1.csv"));
	    BufferedWriter output2 = new BufferedWriter(new FileWriter("./output2.csv"));
	    
	    String data = "time, sensor0, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, sensor7";

	    initialize_output(output1, data);
	    initialize_output(output2, data);

	  	event_declare(ik); 
		ForceSensorChangeListener sensor_listener1 = new ForceSensorChangeListener();
		ik.addSensorChangeListener(sensor_listener1);

	    event_declare(ik2);
		ForceSensorChangeListener sensor_listener2 = new ForceSensorChangeListener();
		ik2.addSensorChangeListener(sensor_listener2);

		
		ik.open(172121);
		ik2.open(28975);
		
	    System.out.println("waiting for Interfacekit attachment...");
	    ik.waitForAttachment();
	    ik2.waitForAttachment();

	    System.out.println(Arrays.toString(sensor_listener1.value_list));
	    System.out.println(ik.getDeviceName());

	    Thread.sleep(500);
		
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
        BufferedWriter result1 = new BufferedWriter(new FileWriter("./result1.csv"));
        BufferedWriter result2 = new BufferedWriter(new FileWriter("./result2.csv"));

        initialize_result(result1, sensor_listener1, data, sdf, time);
        initialize_result(result2, sensor_listener2, data, sdf, time);
	    
        long current_time = System.currentTimeMillis();
	    
	    while(true){
	    	
	    	Thread.yield();
    		current_time = System.currentTimeMillis();
 
    		make_result(result1, sensor_listener1, sdf);
    		make_result(result2, sensor_listener2, sdf);
    	
	    	if(current_time-sensor_listener1.complete_time>=5000)
    		{
	    		break;
    		}
	    }
	    
	    System.out.println("Closing...");
	    result1.close();   //result close
	    result2.close();
	    ik.close();
	    ik2.close();
	    ik = null;
	    ik2 = null;   	//deallocate
	}
	
	public static int find_section(int[] value_list, int[] prev_value, int highest){
		
		int total_delta=0;
		for (int i=0; i<8; i++)
		{
			total_delta += Math.abs(value_list[i]-prev_value[i]);
		}
		
		if(total_delta < 50)
		{
			return highest;
		}
		else if(highest == -1)
			highest = 0;
		
		for (int i=0; i<8; i++)
		{
			if(value_list[highest]-prev_value[highest]<value_list[i]-prev_value[i])
			{
				highest = i;
			}
		}
		
		return highest;
	}
	
	public static void make_result (BufferedWriter result, ForceSensorChangeListener sensor_listener, SimpleDateFormat sdf) throws IOException
	{
	    long current_time = System.currentTimeMillis();
		
    	Thread.yield();

    	if(sensor_listener.state==true)
    	{
	    		if(current_time-sensor_listener.complete_time>=2000)  //if 2secs are passed from completion
	    		{
	    			result.write(sdf.format(current_time).toString() + ", ");
	    			sensor_listener.highest=find_section(sensor_listener.value_list, sensor_listener.prev_value_list, sensor_listener.highest);
	    	        for(int i=0; i<7; i++)
	    	        {
	    	        	result.write(String.valueOf(sensor_listener.value_list[i]));
	    	        	result.write(", ");
	    	        }
	    	        result.write(String.valueOf(sensor_listener.value_list[7]));
	    	        result.write(", " + sensor_listener.highest);
	    	        result.newLine();
	    	        sensor_listener.state=false;
	    			System.out.println("result = " + Arrays.toString(sensor_listener.value_list) + ", secton = " + sensor_listener.highest);
	    	        System.arraycopy(sensor_listener.value_list, 0, sensor_listener.prev_value_list, 0, 8);
	    		}
	    			
    	}
    	
//    	if(current_time-ForceSensorChangeListener.complete_time>=5000)
//		{
//    		break;
//		}
	}
	
	public static void initialize_output (BufferedWriter output, String data) throws IOException{

	    output.write(data);
	    output.newLine();
	    
	    output.flush();
	    output.close();
	}
	
	
	public static void initialize_result (BufferedWriter result, ForceSensorChangeListener sensor_listener, String data, SimpleDateFormat sdf, Date time) throws IOException{
		result.write(data);
        result.write(", section#");
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
//
//		    ik.addSensorChangeListener(new SensorChangeListener() {
//		      public void sensorChanged(SensorChangeEvent se) {
//		        System.out.println(se);
//		      }
//		    });		    
		
	}
	
}


