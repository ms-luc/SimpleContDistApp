package serverhis;
import java.io.*;
import java.net.*;

class Server {

  static String message = "WEB SERVER: "; //his web server message

  public static void main(String argv[]) throws Exception{

    //init socket
  ServerSocket servsock = new ServerSocket(40300);

  System.out.println("\n");

    while(true) {

      Socket serverSocket = servsock.accept();

      System.out.println("\n"+message+"Connected");

      BufferedReader inFromClient =
        new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      String getRequest = inFromClient.readLine();
      System.out.print(message+"from client: "+ getRequest + "\n");

      // fetch file name out of GET request
      String fileName = getRequest.split(" ")[1];

      File file = new File("." + fileName);

      //File file = new File("./index.html");

      if(file.exists()){

        System.out.println(message+"Sending index.html \n");

        DataOutputStream outToClient = new DataOutputStream(serverSocket.getOutputStream());

		    System.out.println(message+"file length:" + file.length()+ "\n");

        // ACK, ack number == file size
        outToClient.writeBytes("ACK " + file.length() + "\n");
        //outToClient.writeBytes(file.length() + "/index.html\n");

        System.out.println(message+"sent ACK\n");

        byte[] fileBufferArray = new byte[(int) file.length()];

        new FileInputStream(file).read(fileBufferArray, 0, fileBufferArray.length);;
        serverSocket.getOutputStream().write(fileBufferArray, 0, fileBufferArray.length);;
        serverSocket.close();

        System.out.println(message+"Sent\n");
      }
      else{
        //ERROR 404
        DataOutputStream outToClient = new DataOutputStream(serverSocket.getOutputStream());
        outToClient.writeBytes("404 NOT FOUND\n");

        System.out.println(message+"404 NOT FOUND\n");
      }

    }
  }
}
