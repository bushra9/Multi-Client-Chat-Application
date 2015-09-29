import java.net.*; 
import java.io.*; 
import javax.swing.*; 
import java.awt.event.*; 
import java.awt.*; 
import java.util.Calendar;

class networkfinal
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
	System.out.println("Enter the group multicast address");
	String multicast=br.readLine();
	
	int mcastPort = 6000;
    InetAddress mcastAddr = null;
    
	
     // mcastAddr = InetAddress.getByName("230.0.0.1");
      mcastAddr = InetAddress.getByName(multicast);
    
	    new Thread(new McastReceiver(mcastPort, mcastAddr), "McastReceiver").start();

    // start new thread to send multicasts

   // new Thread(new McastRepeater(mcastPort, mcastAddr), 

    //           "McastRepeater").start();
	
     ds=new DatagramSocket(localport);
	 String msg="0"+localport;
	 
	DatagramPacket packet = new DatagramPacket(msg.getBytes(), 0, msg.length(),InetAddress.getLocalHost(),serverport);
	
	ds.send(packet);
    

new Thread(new Receiver(serverport,ds), "McastReceiver").start();

    // start new thread to send multicasts

    new Thread(new write(serverport,ds,mcastPort, mcastAddr), 

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
  
  InetAddress mcastAddr=null;
  int mcastPort=0;
  
  write (int port1,DatagramSocket ds2,int port,InetAddress addr)
  {
    serverport=port1;
	ds=ds2;
	mcastAddr=addr;
	mcastPort=port;
	
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
 else if(clientport==-1)
 {
    packet = new DatagramPacket(msg.getBytes(), 0, msg.length(),mcastAddr,mcastPort);
	ds.send(packet);
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
  
  
  
  
  
  
  
  
  
  
  
  
class Receiver implements Runnable {

  int serverport = 0;
  int clientport=0;
  
  private DatagramSocket ds;
  

   public Receiver(int port,DatagramSocket ds2) {
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

  




class McastReceiver implements Runnable {

  int mcastPort = 0;
  InetAddress mcastAddr = null;
  InetAddress localHost = null;

   public McastReceiver(int port, InetAddress addr) {
  mcastPort = port;
  mcastAddr = addr;

    try {

      localHost = InetAddress.getLocalHost();

    } catch (UnknownHostException uhe) {

      System.out.println("Problems identifying local host");

      uhe.printStackTrace();  System.exit(1);

    }

  }



  public void run() {

    MulticastSocket mSocket = null;

    try {

      System.out.println("Setting up multicast receiver");

      mSocket = new MulticastSocket(mcastPort);

      mSocket.joinGroup(mcastAddr);

    } catch(IOException ioe) {

      System.out.println("Trouble opening multicast port");

      ioe.printStackTrace();      System.exit(1);

    }



    DatagramPacket packet;

    System.out.println("Multicast receiver set up ");

    while (true) {

      try {

        byte[] buf = new byte[1024];

        packet = new DatagramPacket(buf,buf.length);

        //System.out.println("McastReceiver: waiting for packet");

        mSocket.receive(packet);
		
		
		
		//byte buf[] = new byte[1024];
 // DatagramPacket data = new DatagramPacket(buf, buf.length);
 // server.receive(data);
  String msg = new String(packet.getData()).trim();
 // System.out.println(msg);

        

        /*ByteArrayInputStream bistream = 

          new ByteArrayInputStream(packet.getData());

        ObjectInputStream ois = new ObjectInputStream(bistream);

        Integer value = (Integer) ois.readObject();*/
		
		
       


        // ignore packets from myself, print the rest

       // if (!(packet.getAddress().equals(localHost))) {

          System.out.println("Received multicast packet: "+msg+ "      from: " + packet.getAddress());

     //   } 

     

      } catch(IOException ioe) {

        System.out.println("Trouble reading multicast message");

        ioe.printStackTrace();  System.exit(1);

      } 

    }

  }

}

  
  
  
  
  
/*  

class McastRepeater implements Runnable {



  private DatagramSocket dgramSocket = null;

  int mcastPort = 0;

  InetAddress mcastAddr = null;

  InetAddress localHost = null;



  

  McastRepeater (int port, InetAddress addr) {

    mcastPort = port;

    mcastAddr = addr;

    try {

      dgramSocket = new DatagramSocket();

    } catch (IOException ioe){

      System.out.println("problems creating the datagram socket.");

      ioe.printStackTrace(); System.exit(1);

    }

    try {

      localHost = InetAddress.getLocalHost();

    } catch (UnknownHostException uhe) {

      System.out.println("Problems identifying local host");

      uhe.printStackTrace();  System.exit(1);

    }



  }



  public void run() {

    DatagramPacket packet = null;

    int count = 0;



    // send multicast msg once per second 

    while (true) {

      // careate the packet to sned.

      try {

        // serialize the multicast message

       // ByteArrayOutputStream bos = new ByteArrayOutputStream();

       // ObjectOutputStream out = new ObjectOutputStream (bos);

        //out.writeObject(new Integer(count++));

        //out.flush();

        //out.close();

        

        // Create a datagram packet and send it

       // packet = new DatagramPacket(bos.toByteArray(),bos.size(),mcastAddr,mcastPort);
		
		
		
		String msg="";
		BufferedReader br = new BufferedReader(new
 InputStreamReader(System.in));
 msg = br.readLine();
 packet = new DatagramPacket(msg.getBytes(), 0, msg.length(),mcastAddr,mcastPort);



        // send the packet

        dgramSocket.send(packet);

        System.out.println("sending multicast message");

       // Thread.sleep(1000);

      } 
	  
	  catch (IOException ioe) {

        System.out.println("error sending multicast");

        ioe.printStackTrace(); System.exit(1);

      }

    }



  }



}
  
  
  
  
  
  
*/
	