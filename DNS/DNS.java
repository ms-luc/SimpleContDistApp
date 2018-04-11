package dns;
import java.io.*;
import java.net.*;

/*

  DNS Server

  Recieve Request. (UDP)
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class DNS{

  // an Array for storing cached DNS records
  // these Records contain only DNS servers
  public static DNSRecord[] cacheDNS;

  public static DNSRecord[] cacheServers;

  // message for debugging
  public static String message; //Object message

  // server port variable
  public static int serverPort;

  DNS(){


  }

  DNS(DNSRecord[] dns, DNSRecord[] servers, String m, int port){

    // initialize all variables
    cacheDNS = dns;
    cacheServers = servers;
    message = m;
    serverPort = port;

  }


  public void launchServer() throws Exception{

    // a UDP server socket
    DatagramSocket serverSocket = new DatagramSocket(serverPort);

    // current recieved packet UDP
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

      // convert request to string
      String[] request = packetToString(receivePacket).split(",");

      // retrieve requested URL from request
      String requestedURL = request[1];

      // retrieve requested Type from request
      String requestedType = request[0];
      if(requestedType.equals(""))
        requestedType = "A";

      // print url requested by client
      System.out.print(message+ " got request for: " + requestedURL + " type: " + request[0] + "\n");

      //RECIEVE PACKET END

      // used in deciding if a Record is A or NS
      String tempRecord = "";

      // FIND URL IN cacheServers
      String cached = "false";
  		for(int i = 0; i < cacheServers.length; i ++){
  			if(cacheServers[i].name.equals(requestedURL)){
          // acks that the URL is cached
          cached = cacheServers[i].type;

          System.out.print(message+ "Found url:" + requestedURL + " type " + cacheServers[i].type + "\n");

          if(cacheServers[i].type.equals(requestedType)){
              // set DNS record to A even if a record with NS exists
  				    tempRecord = cacheServers[i].toString();
          }

          if(!cacheServers[i].type.equals(requestedType) && !cached.equals(requestedType)){
              // set DNS record to NS one
              tempRecord = cacheServers[i].toString();
          }

          if(cached.equals(requestedType)){
            break;
          }


  			}
  		}// FIND URL IN cacheServers END

      if(!cached.equals("false")){
        // get Client's IP
        // get Client's Port
        // send back a DNS record
        String dnsRecord = tempRecord;
        sendUDPPacket(serverSocket, dnsRecord, client_ipAddress, client_port);
        System.out.print(message+ "Found url:" + requestedURL + " in cacheServers " + "\n");
      }

      //IF NOT CACHED
      //REQUEST URL FROM ANOTHER DNS (first dns in list?)
  		if(cached.equals("false")){

        System.out.print(message+ requestedURL + " not found in cache\n");

        for(int i = 0; i < cacheDNS.length; i ++){

          System.out.print(message+ "asking " + cacheDNS[i] + " for a record\n");

          // send to other DNS a record Request
          sendUDPPacket(serverSocket, requestedType + "," + requestedURL, cacheDNS[i].value.getAddress(), cacheDNS[i].value.getPort() );

          // recieve a Responce from the DNS
          receivePacket = recieveUDPPacket(serverSocket);

          // initialize temp record
          DNSRecord tempRecord2 = new DNSRecord().toRecord(packetToString(receivePacket));

          // checks if type = V
          if( (tempRecord2).name.equals(requestedURL) && (tempRecord2).type.equals(requestedType) ){

            // send client the DNS record

            // return record to Client, but keep DNS record as a String
            sendUDPPacket(serverSocket, packetToString(receivePacket), client_ipAddress, client_port);

            // print recieved data
            System.out.print(message+ "Got correct DNS record from " + cacheDNS[i].name + ": " + packetToString(receivePacket) + "\n");
            System.out.print(message+ "Seinding to Client\n");

            // no need to loop more
            break;
          }
          // else if the URL got a matching from DNS but not the Type, ask the DNS which holds the wrong type Record
          else if( (tempRecord2).name.equals(requestedURL) && !(tempRecord2).type.equals(requestedType) ){

            System.out.print(message+ "Got NS type from " + cacheDNS[i].name + ": " + packetToString(receivePacket) + "\n");

            // ask NS DNS for an A type record
            sendUDPPacket(serverSocket, requestedType + "," + requestedURL, tempRecord2.value.getAddress() , tempRecord2.value.getPort());
            System.out.print(message+ " asking " + tempRecord2.name + ": " + requestedType + "," + requestedURL + "\n");
            System.out.print(message+ " asking " + tempRecord2.value.getAddress() + tempRecord2.value.getPort() + "\n");


            receivePacket = recieveUDPPacket(serverSocket);

            System.out.print(message+ " got " + packetToString(receivePacket) + "\n");
            // return record to Client, but keep DNS record as a String
            sendUDPPacket(serverSocket, packetToString(receivePacket), client_ipAddress, client_port);

            // print recieved data

            System.out.print(message+ "Seinding to Client\n");

            // no need to loop more
            break;
          }
          // indicate no matching
          System.out.print(message+ "No matching from: " + cacheDNS[i] + "\n");
        }

  		}//REQUEST ANOTHER DNS FOR URL END

	   }

  }


  // sends a UDP packet
  // max data size: 512 bytes
  public static void sendUDPPacket(DatagramSocket serverSocket, String data, InetAddress ipAddress , int port)throws Exception{

    // convert data into a byteStream
    byte[] sendData = new byte[512];
    sendData = data.getBytes();

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
