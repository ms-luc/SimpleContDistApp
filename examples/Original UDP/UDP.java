
import java.io.*;
import java.net.*;

public class UDP{

  public static void main(String argv[]) throws Exception{

    print("Input: ");

    BufferedReader inFromUser =
      new BufferedReader(new InputStreamReader(System.in));

	  DatagramSocket clientSocket = new DatagramSocket();

	  InetAddress IPAddress = InetAddress.getByName("localhost");

	  byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    String sentence = inFromUser.readLine();

    sendData = sentence.getBytes();

	  DatagramPacket sendPacket =
      new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

    clientSocket.send(sendPacket);

    DatagramPacket receivePacket =
      new DatagramPacket(receiveData, receiveData.length);

    clientSocket.receive(receivePacket);

    String modifiedSentence = new String(receivePacket.getData());

    System.out.println("FROM SERVER:" + modifiedSentence);
    clientSocket.close();

  }

  static void print(String string){System.out.println(string);}
  static void print(int integer){System.out.println(integer);}
  static void print(float floating){System.out.println(floating);}
  static void print(double doub){System.out.println(doub);}

}
