import client.*;
import sensor.*;

public class Fridge {
		public static void main(String[] args) throws Exception {
			System.out.println("Fridege Start!");
			ForceSensor force1 = new ForceSensor(172121, "./output.csv", "./result.csv");							//initialize ForceSensor
			Client client = new Client();																			//initialize Client socket
			SwitchSensor switch1 = new SwitchSensor(28975, client, force1);											//initialize FridgeDoorSwitch 
		
			while(true){
				
			}

		}
			
}
