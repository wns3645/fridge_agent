package sensor;

import com.phidgets.event.SensorChangeListener;
import com.phidgets.event.SensorChangeEvent;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ForceSensorChangeListener implements SensorChangeListener  {

	public int[] prev_value_list = {0, 0, 0, 0, 0, 0, 0, 0};
	public int[] value_list = {0, 0, 0, 0, 0, 0, 0, 0};		
	
	public int curr_section = -1;
	
    public boolean state=false;
	public long complete_time=0;
    
	public void sensorChanged(SensorChangeEvent se){
		
		int index = se.getIndex();
		int value = se.getValue();
		
		state=false;
		
		value_list[index] = value;
		        
	    System.out.println(Arrays.toString(value_list));
		
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); 
        
        try{
	        BufferedWriter output = new BufferedWriter(new FileWriter("./output.csv", true));
	        
	        output.write(sdf.format(time).toString() + ", ");
	        for(int i=0; i<7; i++)
	        {
	        	output.write(String.valueOf(value_list[i]));
	        	output.write(", ");
	        }
	        output.write(String.valueOf(value_list[7]));
	        output.newLine();
	        output.flush();
	        
	        output.close();
	        
        }
        catch (IOException e){
        	e.printStackTrace();
        }
        
        state=true;
        complete_time = System.currentTimeMillis();
	}
}
