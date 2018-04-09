package serverhis;
import java.io.*;
import java.net.*;

class Server {


  public static void main(String argv[]) throws Exception{

  ServerSocket servsock = new ServerSocket(6789);


    while(true) {

      Socket sock = servsock.accept();

      BufferedReader inFromServer =
        new BufferedReader(new InputStreamReader(sock.getInputStream()));

      System.out.println("looking");
      File file = new File(inFromServer.readLine());
      System.out.println("not found");

      if(file.exists()){

        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());

        outToClient.writeBytes("ACK\n");

        byte[] mybytearray = new byte[(int) file.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = sock.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        sock.close();

        System.out.println(mybytearray);
      }
      else{
        //ERROR 404
        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
        outToClient.writeBytes("404 NOT FOUND\n");

        System.out.println("404 NOT FOUND");
      }

    }
  }
}
