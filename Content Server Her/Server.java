package hercdn;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;


class Server {

  static String message = "Her CDN SERVER: "; //his web server message

  public static void main(String argv[]) throws Exception{

  ServerSocket servsock = new ServerSocket(40200);

  System.out.println("\n");

    while(true) {

      Socket sock = servsock.accept();

      System.out.println("\n"+message+"Connected");

      //BufferedReader inFromClient =
        //new BufferedReader(new InputStreamReader(sock.getInputStream()));

      //System.out.println("looking");
      //File file = new File(inFromServer.readLine());
      //System.out.println("not found");

      File file = new File("video.mp4");

      if(file.exists()){

        System.out.println(message+"Sending video.mp4 + size:" +file.length()+ "\n");

        DataOutputStream outToClient = new DataOutputStream(sock.getOutputStream());

        outToClient.writeBytes(file.length() + "/video.mp4\n");

        System.out.println(message+"sent file name\n");
		
		for(int i=0;i<10000;i++){
			for(int b=0;b<100000;b++){
				for(int a=0;a<100000;a++){
			String temp = "djsaiojflkmdakljisdjaofmklsfafadfsde341fdagda";
			String temp2 = "djsaiojflkmdakljisdjaofmklsfafadfsde341fdagdb";
			temp2.equals(temp);
				}
			}
		}
		
		System.out.println(message+"sending buffer\n");

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
