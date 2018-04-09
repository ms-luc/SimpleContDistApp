package localdns;
import java.io.*;
import java.net.*;

public class UDP{

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
