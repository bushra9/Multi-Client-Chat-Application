import java.net.*; 
import java.io.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.awt.*; 
import java.util.Calendar;

class clientunibroad
{
 static int localport;
 static int serverport=900;

public static void main(String args[])
{

  DatagramSocket ds;
  
 
try
	{
	
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Enter your localport");
	localport=Integer.parseInt(br.readLine());
	
     ds=new DatagramSocket(localport);
	 String msg="0"+localport;
	 
	DatagramPacket packet = new DatagramPacket(msg.getBytes(), 0, msg.length(),InetAddress.getLocalHost(),serverport);
	
	ds.send(packet);
    

new Thread(new McastReceiver(serverport,ds), "McastReceiver").start();

    // start new thread to send multicasts

    new Thread(new write(serverport,ds), 

               "McastRepeater").start();
			   
	}
  catch(Exception e)
  {
  }  

}
}

class write implements Runnable
{
  
  private DatagramSocket ds;
  
  int serverport,clientport;
  
  write (int port1,DatagramSocket ds2)
  {
    serverport=port1;
	ds=ds2;
	
	
	}
	
	
	public void run() {

    DatagramPacket packet = null;

    //int count = 0;
	

    // send multicast msg once per second 

    while (true) {

      // careate the packet to sned.

      try {

       
		
		
		
		
		String msg="";
		BufferedReader br = new BufferedReader(new
 InputStreamReader(System.in));
 System.out.print("enter msg: ");
 msg = br.readLine();
 System.out.print("enter port: ");
 clientport=Integer.parseInt(br.readLine());
 if(clientport==0)
 {
   String msg1='1'+msg;
 packet = new DatagramPacket(msg1.getBytes(), 0, msg1.length(),InetAddress.getLocalHost(),serverport);
 
 ds.send(packet);
 System.out.println("sending broadcast msg");
 }
 else
 {
 
 packet = new DatagramPacket(msg.getBytes(), 0, msg.length(),InetAddress.getLocalHost(),clientport);
 
 ds.send(packet);
 System.out.println("sending unicast message");
}

      } 
	  
	  catch (IOException ioe) {

        System.out.println("error sending multicast");

        ioe.printStackTrace(); System.exit(1);

      }

    }



  }



}
  
  
  
  
  
  
  
  
  
  
  
  
class McastReceiver implements Runnable {

  int serverport = 0;
  int clientport=0;
  
  private DatagramSocket ds;
  

   public McastReceiver(int port,DatagramSocket ds2) {
   serverport=port;
  ds=ds2;
  


  }
  
 


  public void run() {

   


    DatagramPacket packet;
 
    System.out.println("unicast receiver set up ");

    while (true) {

      try {

        byte[] buf = new byte[1024];

        packet = new DatagramPacket(buf,buf.length);
         
        ds.receive(packet);
		  String msg = new String(packet.getData()).trim();
  
    if(packet.getPort()==serverport)


          System.out.println("Received broadcast packet: "+msg+ "      from: " + packet.getAddress());
		  
		else  
          System.out.println("Received unicast packet: "+msg+ "      from: " + packet.getAddress());


      } catch(IOException ioe) {

        System.out.println("Trouble reading multicast message");

        ioe.printStackTrace();  System.exit(1);

      } 

    }

  }

}

  





	