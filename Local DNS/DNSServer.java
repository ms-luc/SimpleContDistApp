package localdns;
import java.io.*;
import java.net.*;

/*

  Local DNS

  Recieve Request. (UDP)
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class DNSServer{

  static DNSRecord hisCinema = new DNSRecord("dns.hiscinema.com", new InetSocketAddress("localhost",6789), "");

  static DNSRecord[] cache = { hisCinema };

  public static void main(String argv[]) throws Exception{

    DatagramSocket serverSocket = new DatagramSocket(6565);

	  byte[] receiveData = new byte[512];
    byte[] sendData = new byte[512];

    while(true) {

      //RECIEVE PACKET
      DatagramPacket receivePacket =
        new DatagramPacket(receiveData, receiveData.length);

      serverSocket.receive(receivePacket);

      String requestedURL = new String(receivePacket.getData());
      requestedURL = requestedURL.replaceAll("\0", ""); //RETRIEVE URL

      System.out.print(requestedURL + " - Requested URL\n");

      //RECIEVE PACKET END

      // FIND URL IN CACHE
      boolean cached = false;
  		for(int i = 0; i < cache.length; i ++){
  			if(cache[i].name.equals(requestedURL)){
  				cached = true; //FOUND IN CACHE

          InetAddress IPAddress = receivePacket.getAddress();

          int port = receivePacket.getPort();

          sendData = new byte[512];
          sendData = requestedURL.getBytes();

          DatagramPacket sendPacket =
            new DatagramPacket(sendData, sendData.length, IPAddress, port);

          serverSocket.send(sendPacket);

  			}
  		}// FIND URL IN CACHE END

      //IF NOT CACHED
      //REQUEST URL FROM ANOTHER DNS (first dns in list?)
  		if(cached == false){

        InetAddress IPAddress = hisCinema.value.getAddress();

        int port = hisCinema.value.getPort();

        sendData = new byte[512];
        sendData = requestedURL.getBytes();

        DatagramPacket sendPacket =
          new DatagramPacket(sendData, sendData.length, IPAddress, port);

        serverSocket.send(sendPacket);

  		}//REQUEST ANOTHER DNS FOR URL END

	   }

  }

  static void print(String string){System.out.println(string);}
  static void print(int integer){System.out.println(integer);}
  static void print(float floating){System.out.println(floating);}
  static void print(double doub){System.out.println(doub);}

}
