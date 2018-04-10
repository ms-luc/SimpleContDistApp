package serverhis;
import java.io.*;
import java.net.*;

class Server {

  static String message = "WEB SERVER: "; //his web server message

  public static void main(String argv[]) throws Exception{

  ServerSocket servsock = new ServerSocket(6789);

  System.out.println("\n");
  System.out.println(message+ "initialized");
    while(true) {

      Socket sock = servsock.accept();

      System.out.println(message+"Connected");

      //BufferedReader inFromClient =
        //new BufferedReader(new InputStreamReader(sock.getInputStream()));

      //System.out.println("looking");
      //File file = new File(inFromServer.readLine());
      //System.out.println("not found");

      File file = new File("index.html");

      if(file.exists()){

        System.out.println(message+"Sending index.html ");

        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());

        outToClient.writeBytes("index.html\n");

        System.out.println(message+"sent file name");

        byte[] mybytearray = new byte[(int) file.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = sock.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        sock.close();

        System.out.println(message+"Sent");
      }
      else{
        //ERROR 404
        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
        outToClient.writeBytes("404 NOT FOUND\n");

        System.out.println(message+"404 NOT FOUND");
      }

    }
  }
}
