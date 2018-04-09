package client;

import java.io.*;
import java.net.*;

class Client {

	DNSRecord hisCinemma = new DNSRecord("www.hiscinema.com", new InetSocketAddress("localhost",6789), "");
	DNSRecord localDNS = new DNSRecord("dns.local", new InetSocketAddress("localhost",6565), "");
	DNSRecord[] cache = new DNSRecord[]{hisCinemma};

	public static void main(String argv[]) throws Exception {

		Client client = new Client();
		System.out.println("connecting");
		client.connect("www.hiscinema.com");
		System.out.println("fin");

		client.connect("www.hiscinema.com/Video");
	}

	public void connect(String url) throws Exception{

		for(int i = 0; i < cache.length; i ++){
			if(cache[i].name.equals(url)){
				System.out.println("found, requesting");
				getRequest("index.html", cache[i].value.getHostString(), cache[i].value.getPort());
			}
		}
		// IF NOT ACCESS THE LOCAL DNS

	}

	public static void getRequest(String fileName, String serverName, int serverPort) throws Exception{

		Socket sock = new Socket(serverName, serverPort);

		//DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());

		//outToServer.writeBytes(fileName + "\n");

		System.out.println("initialized");
		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(sock.getInputStream()));

		String responce = inFromServer.readLine();

		System.out.println("getting file: " + responce);

		if(responce.equals("404 NOT FOUND")){

			System.out.println(responce);
			sock.close();

		}
		else{
	    byte[] mybytearray = new byte[1024];
	    InputStream is = sock.getInputStream();
	    FileOutputStream fos = new FileOutputStream(responce);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
	    bos.write(mybytearray, 0, bytesRead);
	    bos.close();
	    sock.close();
		}

	}

}
