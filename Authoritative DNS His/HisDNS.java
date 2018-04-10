package hisdns;
import java.io.*;
import java.net.*;

/*

  His  DNS

  Recieve Request. (UDP)

  Look in cache, if not found
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class HisDNS{

  static DNSRecord someLocalDNS = new DNSRecord("dns.local", new InetSocketAddress("localhost",6565), "");
  static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");

  static DNSRecord[] cache = { someLocalDNS,  herVideo};

  static String message = "HIS DNS: "; //dns server message

  public static void main(String argv[]) throws Exception{

    DatagramSocket serverSocket = new DatagramSocket(6001);

	  byte[] receiveData = new byte[512];
    byte[] sendData = new byte[512];

    while(true) {

      //RECIEVE PACKET
      DatagramPacket receivePacket =
        new DatagramPacket(receiveData, receiveData.length);

      serverSocket.receive(receivePacket);

      String requestedURL = new String(receivePacket.getData());
      requestedURL = requestedURL.replaceAll("\0", ""); //RETRIEVE URL

      System.out.print(message+ requestedURL + " - Requested URL\n");

      //RECIEVE PACKET END

      // FIND URL IN CACHE
      boolean cached = false;
  		for(int i = 0; i < cache.length; i ++){
  			if(cache[i].name.equals(requestedURL)){
  				cached = true; //FOUND IN CACHE

          System.out.print(message+ "Found url:" + requestedURL + " in cache " + "\n");

          InetAddress IPAddress = receivePacket.getAddress();

          int port = receivePacket.getPort();

          sendData = new byte[512];
          sendData = "dnsRecord(www.herCDN, IP, tag)".getBytes();

          DatagramPacket sendPacket =
            new DatagramPacket(sendData, sendData.length, IPAddress, port);

          serverSocket.send(sendPacket);

  			}
  		}// FIND URL IN CACHE END

      //IF NOT CACHED
      //REQUEST URL FROM ANOTHER DNS (first dns in list?)
      cached = true;
  		if(cached == false){

        InetAddress IPAddress = someLocalDNS.value.getAddress();

        int port = someLocalDNS.value.getPort();

        sendData = new byte[512];
        sendData = "dnsRecord(www.herCDN, IP, tag)".getBytes();

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
