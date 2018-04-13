package serverhis;
import java.io.*;
import java.net.*;

class Server {

  static String message = "WEB SERVER: "; //his web server message

  public static void main(String argv[]) throws Exception{

	//MulticastSocket servsock = new MulticastSocket();
	//servsock.setBroadcast(true);

	//netAddress variable = InetAddress.getByName("141.117.232.53");

  //ServerSocket servsock = new ServerSocket(40200, 2, variable );
  ServerSocket servsock = new ServerSocket(40300);

  System.out.println("\n");

    while(true) {

      Socket sock = servsock.accept();

      System.out.println("\n"+message+"Connected");

      //BufferedReader inFromClient =
        //new BufferedReader(new InputStreamReader(sock.getInputStream()));

      //System.out.println("looking");
      //File file = new File(inFromServer.readLine());
      //System.out.println("not found");

      File file = new File("index.html");

      if(file.exists()){

        System.out.println(message+"Sending index.html \n");

        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());

		System.out.println(message+"file length:" + file.length()+ "\n");
        outToClient.writeBytes(file.length() + "/index.html\n");

        System.out.println(message+"sent file name\n");

        byte[] mybytearray = new byte[(int) file.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = sock.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        sock.close();

        System.out.println(message+"Sent\n");
      }
      else{
        //ERROR 404
        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());
        outToClient.writeBytes("404 NOT FOUND\n");

        System.out.println(message+"404 NOT FOUND\n");
      }

    }
  }
}
