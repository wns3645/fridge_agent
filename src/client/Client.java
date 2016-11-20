package client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.Scanner;
 
public class Client{
	Socket client_socket;
	
	public Client() throws Exception{
		 this.client_socket = new Socket("127.0.0.1",9027); // ip: 127.0.0.1, port: 9027
	     System.out.println("Client Start!");        
	}
	
    public void send_file() throws Exception{
        OutputStream out;
        FileInputStream fin;
        
        out =client_socket.getOutputStream();                 
        DataOutputStream dout = new DataOutputStream(out); 
        
        Scanner s = new Scanner(System.in);  
             
        while(true){
            String filename = s.next();   
            
            try{
            	
        	fin = new FileInputStream(new File(filename)); 
        
	        byte[] buffer = new byte[1024];     
	        int len;                              
	        int data=0;                           
	        
	        while((len = fin.read(buffer))>0){     
	            data++;                      
	        }
        
	        int datas = data;                     
	 
	        fin.close();
	        fin = new FileInputStream(filename);  
	        dout.writeInt(data);                  
	        dout.writeUTF(filename);               
	        
	         len = 0;
	        
	        for(;data>0;data--){                
	            len = fin.read(buffer);        
	            out.write(buffer,0,len);       
	        }
	        
	        System.out.println("Filename: " +filename + ", " +datas+" kbytes of files is sent.");
            }
            catch(FileNotFoundException e){
	        	System.out.println("Exception: " + e);
	        	this.client_socket.close();
	        	s.close();
	        }
     
        }      
    }
}
