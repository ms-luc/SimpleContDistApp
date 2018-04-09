package localdns;
import java.io.*;
import java.net.*;

/*

  Local DNS

  Recieve Request. (UDP)
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class UDP{

  public DNSRecord hisCinema = new DNSRecord("hisCinema.com","192.168.1.1","");
  public DNSRecord herCDN = new DNSRecord("herCDN.com","192.168.1.1","");
  public DNSRecord[] records = { hisCinema, herCDN };

  public static void main(String argv[]) throws Exception{

    DatagramSocket serverSocket = new DatagramSocket(9876);

	  byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    while(true) {

      DatagramPacket receivePacket =
        new DatagramPacket(receiveData, receiveData.length);

      serverSocket.receive(receivePacket);

      String sentence = new String(receivePacket.getData());

      InetAddress IPAddress = receivePacket.getAddress();

      int port = receivePacket.getPort();

	    String capitalizedSentence = sentence.toUpperCase();

      sendData = capitalizedSentence.getBytes();

      DatagramPacket sendPacket =
        new DatagramPacket(sendData, sendData.length, IPAddress, port);

      serverSocket.send(sendPacket);

	   }

  }

  static void print(String string){System.out.println(string);}
  static void print(int integer){System.out.println(integer);}
  static void print(float floating){System.out.println(floating);}
  static void print(double doub){System.out.println(doub);}

}

class DNSRecord{

  public String name;
  public String value;
  public String type;

  DNSRecord(String name, String value, String type){

      this.name = name;
      this. value = value;
      this. type = type;

  }

  public String toString(){
    return "DNS Record, name: " + name + " value: " + value + " type: " + type;
  }

}
