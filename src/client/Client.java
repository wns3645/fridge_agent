package client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
 
public class Client{
	Socket client_socket;
	
	public Client() throws Exception{
		 this.client_socket = new Socket("143.248.57.88",9030);//connect to local.--> 나중에 ip addr 바꾸어야함.
	     System.out.println("Client Start! Server connected");        
	}
	
    public void send_file(String file_name) throws Exception{
        OutputStream out;
        FileInputStream fin;
                
        out =client_socket.getOutputStream();                 
        DataOutputStream dout = new DataOutputStream(out); 
            
        try{
        	//open file
        	File file = new File(file_name);              
        	fin = new FileInputStream(file); 
        
        	int fsize = (int) file.length();
	        byte[] buffer = new byte[1024];    
	     
	        //send file size and file name
	        dout.writeInt(fsize);        
	        dout.writeUTF(file_name);               
	        
	        //send file contents
	       while(fin.available() > 0){
	    	   int readsize = fin.read(buffer);
	    	   out.write(buffer, 0, readsize);
	       }
	       
	       fin.close();
	       
	       System.out.println("Filename: " +file_name + ", " +fsize+" kbytes of files is sent.");
        }
        catch(FileNotFoundException e){
        	System.out.println("Exception: " + e);
        	client_socket.close();
        }
   
    }
}
