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

  static DNSRecord hisCinemaDNS = new DNSRecord("dns.hiscinema.com", new InetSocketAddress("localhost",6001), "");
  //static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");
  //static DNSRecord[] cache = { hisCinema,  herVideo};

  static DNSRecord[] cache = { hisCinemaDNS};

  static String message = "LOCAL DNS: "; //dns server message

  public static void main(String argv[]) throws Exception{

    DatagramSocket serverSocket = new DatagramSocket(6565);

    // current recieved packet
	  DatagramPacket receivePacket;

    // Currect client's IP
    InetAddress client_ipAddress;
    // Currect client's PORT
    int client_port;

    // SERVER WHILE LOOP
    // CONSTATNTLY WAITS FOR A CLIENT SO ASK FOR A DNS Record
    // FOR WAITS FOR A DNS RECORD FROM ANOTHER DNS SERVER
    while(true) {

      //RECIEVE PACKET
      receivePacket = recieveUDPPacket(serverSocket);

      // sets current client's IP and PORT
      client_ipAddress = receivePacket.getAddress();
      client_port = receivePacket.getPort();

      // convert URL to string
      String requestedURL = packetToString(receivePacket);

      // print url requested by client
      System.out.print(message+ requestedURL + " - Requested URL\n");

      //RECIEVE PACKET END

      // FIND URL IN CACHE
      boolean cached = false;
  		for(int i = 0; i < cache.length; i ++){
  			if(cache[i].name.equals(requestedURL)){

  				cached = true; //FOUND IN CACHE

          // get Client's IP
          // get Client's Port
          // send back a DNS record
          sendUDPPacket(serverSocket, requestedURL, client_ipAddress, client_port);
          System.out.print(message+ "Found url:" + requestedURL + " in cache " + "\n");

  			}
  		}// FIND URL IN CACHE END

      //IF NOT CACHED
      //REQUEST URL FROM ANOTHER DNS (first dns in list?)
      System.out.print(message+ "Requesting His DNS for Record");
  		if(cached == false){

        // send to another DNS a record Request
        sendUDPPacket(serverSocket, requestedURL, hisCinemaDNS.value.getAddress(), hisCinemaDNS.value.getPort() );

        // recieve a Responce from the DNS
        receivePacket = recieveUDPPacket(serverSocket);
        String dnsRecord = packetToString(receivePacket);
        // print recieved data
        System.out.print(message+ "Got dnsRecord from His DNS: " + dnsRecord + "\n");

        // return record to Client
        sendUDPPacket(serverSocket, dnsRecord, client_ipAddress, client_port);

  		}//REQUEST ANOTHER DNS FOR URL END

	   }

  }

  // sends a UDP packet
  // max data size: 512 bytes
  public static void sendUDPPacket(DatagramSocket serverSocket, String requestedURL, InetAddress ipAddress , int port)throws Exception{

    // convert data into a byteStream
    byte[] sendData = new byte[512];
    sendData = requestedURL.getBytes();

    // make packet
    DatagramPacket sendPacket =
      new DatagramPacket(sendData, sendData.length, ipAddress, port);

    // send packet
    serverSocket.send(sendPacket);

  }

  // recieves a UDP packet
  // max dataSize: 512 bytes
  // returns recieved data in string format
  public static DatagramPacket recieveUDPPacket(DatagramSocket serverSocket) throws Exception{

    // initialize a packet
    byte[] receiveData = new byte[512];
    DatagramPacket receivePacket =
      new DatagramPacket(receiveData, receiveData.length);

    // receive packet and fill recieveData with aquired data
    serverSocket.receive(receivePacket);

    // return recieved packet
    return receivePacket;

  }

  // retrieves packet data, clears the null characters
  // returns a String
  public static String packetToString(DatagramPacket receivePacket){

    // convert data into a string and remove NULL characters
    String string = new String(receivePacket.getData()); //RETRIEVE URL
    string = string.replaceAll("\0", "");  // remove null chars

    // return packet data as a string
    return string;

  }


  static void print(String string){System.out.println(string);}
  static void print(int integer){System.out.println(integer);}
  static void print(float floating){System.out.println(floating);}
  static void print(double doub){System.out.println(doub);}

}
