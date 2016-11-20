import forcesensor.*;
import client.*;


//key pressed event handling tomporary
import java.awt.event.*;
import javax.swing.*;

public class Fridge {
		public static void main(String[] args) throws Exception {
			
			ForceSensor sensor1 = new ForceSensor(172121, "./output.csv", "./result.csv");							//initialize ForceSensor instance
			//Client client = new Client();																			//initialize Client socket instance
			
			//�ӽ÷� key event handling �����Ѱ���...��.��
			JFrame frame = new JFrame();
			JPanel p = new JPanel();
	        JLabel label = new JLabel("Key Listener!");
	        p.add(label);
	        frame.add(p);
			frame.addKeyListener(new MyKeyListener());
			frame.setSize(200, 100);
			frame.setVisible(true);
		
			//sensor1.forceSensing();
			//client.send_file();
			
		}
			
}

//�ӽ÷� ����� ����... ���߿� �������...
class MyKeyListener implements KeyListener{
	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();

		switch(keyCode){
		case KeyEvent.VK_RIGHT:
			System.out.println("p pressed");
		}

	}
	public void keyReleased(KeyEvent e){
		
	}
	public void keyTyped(KeyEvent e){
		
	}
}
