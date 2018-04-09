package client;

import java.io.*;
import java.net.*;

class Client {

	public static void main(String argv[]) throws Exception {

		getRequest("index.html", "localhost", 6789);

	}

	public static void getRequest(String fileName, String serverName, int serverPort) throws Exception{

		boolean success = false;
		Socket sock = new Socket(serverName, serverPort);

		DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());

		outToServer.writeBytes(fileName + "\n");


		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(sock.getInputStream()));

		String responce = inFromServer.readLine();

		if(responce.equals("ACK")){
	    byte[] mybytearray = new byte[1024];
	    InputStream is = sock.getInputStream();
	    FileOutputStream fos = new FileOutputStream(fileName);
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
