import java.io.*;
import java.net.*;

class TCPClient {

	public static void main(String argv[]) throws Exception {

		getRequest("foo.txt", "localhost", 6789);

	}

	public static void getRequest(String fileName, String serverName, int serverPort) throws Exception{

		boolean success = false;
		Socket sock = new Socket(serverName, serverPort);

		DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());

		outToServer.writeBytes("foo.txt\n");


		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(sock.getInputStream()));

		String responce = inFromServer.readLine();

		if(responce.equals("ACK")){
	    byte[] mybytearray = new byte[1024];
	    InputStream is = sock.getInputStream();
	    FileOutputStream fos = new FileOutputStream(fileName + "1");
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
	    bos.write(mybytearray, 0, bytesRead);
	    bos.close();
	    sock.close();
		}
		else{

			System.out.println(responce);
			sock.close();

		}

	}

}
