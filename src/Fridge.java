import client.*;
import sensor.*;

public class Fridge {
		public static void main(String[] args) throws Exception {
			
			ForceSensor force1 = new ForceSensor(172121, "./output.csv", "./result.csv");							//initialize ForceSensor instance
			Client client = new Client();																			//initialize Client socket instance
			SwitchSensor switch1 = new SwitchSensor(28975, client, force1);
		
			//force1.forceSensing();
			//client.send_file();
			while(true){
				
			}

		}
			
}
