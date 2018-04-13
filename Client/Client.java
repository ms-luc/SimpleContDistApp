package client;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

class Client {

	static String message = "CLIENT: "; //client message

	DNSRecord hisCinemma = new DNSRecord("www.hiscinema.com", new InetSocketAddress("localhost",40300), "A");
	DNSRecord localDNS = new DNSRecord("dns.local", new InetSocketAddress("localhost", 40200), "NS");
	DNSRecord[] cache = new DNSRecord[]{hisCinemma};

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		client.connect("www.hiscinema.com");
		System.out.println(message+ "terminating" + " www.hiscinema.com");

		client.connect("abc/Video");
		System.out.println(message+ "terminating" + " abc/Video");

	}

	public void connect(String url) throws Exception{

		System.out.println("\n"+message+ "connecting to " + url);

		boolean cached = false;
		for(int i = 0; i < cache.length; i ++){
			if(cache[i].name.equals(url)){
				System.out.println(message+ "found url: " + url + " in cache, requesting");
				cached = true;

				//IGNORE THE INDEX.HTML
				getRequest("index.html", cache[i].value.getHostString(), cache[i].value.getPort());
			}
		}

		// IF NOT ACCESS THE LOCAL DNS
		if(cached == false){

			System.out.println(message+ " url " + url + " not cached, asking DNS");
			DNSRecord temp = askDNS(url, localDNS.value.getHostString(), localDNS.value.getPort());
			//askDNS(url, localDNS.value.getHostString(), localDNS.value.getPort());

			System.out.println(message+ "Fetched record: " + temp);
			//System.out.println("fin");


			getRequest("video.mp4", temp.value.getHostString(), temp.value.getPort());

		}



	}

	//ASKS LOCAL DNS FOR DNS RECORD
	public static DNSRecord askDNS(String url, String ip, int port) throws Exception{

		// string to determine type
		String type = determineType(url);

	System.out.println(message+ " request from DNS: URL - " + url + " Record Type - " + type);

	  DatagramSocket clientSocket = new DatagramSocket();

	  InetAddress IPAddress = InetAddress.getByName(ip);

		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		String sentence = type + "," + url;

		sendData = sentence.getBytes();

		DatagramPacket sendPacket =
			new DatagramPacket(sendData, sendData.length, IPAddress, port);
		  //new DatagramPacket(sendData, sendData.length, IPAddress, 9876);


		clientSocket.send(sendPacket);

		DatagramPacket receivePacket =
		  new DatagramPacket(receiveData, receiveData.length);

		clientSocket.receive(receivePacket);

		String returnedRecord = new String(receivePacket.getData());

		returnedRecord = returnedRecord.replaceAll("\0", "");

		System.out.println(message+"from local DNS:" + returnedRecord);
		clientSocket.close();

		return new DNSRecord().toRecord(returnedRecord);

	}

	public static String determineType(String url){

		String type = "";
		String[] splitUrl  = url.split("/");

		if ( splitUrl[splitUrl.length-1].equals("Video") ){
			type = "V";
		}
		else{
			type = "A";
		}

		return type;
	}

	public static void getRequest(String fileName, String serverName, int serverPort) throws Exception{

		System.out.println(message+ "fetching file");

		Socket sock = new Socket(serverName, serverPort);

		//DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());

		//outToServer.writeBytes(fileName + "\n");

		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(sock.getInputStream()));

		String[] fileSizeName = inFromServer.readLine().split("/");
		int fileSize = Integer.valueOf(fileSizeName[0]);
		String responce = fileSizeName[1];

		System.out.println(message+"getting file: " + responce + " (size: " + fileSize + ")");

		if(responce.equals(message+"404 NOT FOUND")){

			System.out.println(responce);
			sock.close();

		}
		else{
	    byte[] mybytearray = new byte[fileSize];
	    InputStream is = sock.getInputStream();
	    FileOutputStream fos = new FileOutputStream(responce);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
	    int current = bytesRead;

//		System.out.println("initial " + mybytearray);
		  do {
			 bytesRead =
				is.read(mybytearray, current, (mybytearray.length-current));
			 if(bytesRead > 0) current += bytesRead;
//			 System.out.println(bytesRead);
		  } while(bytesRead > 0);

		  bos.write(mybytearray, 0 , current);
		  bos.flush();
	    bos.close();
	    sock.close();

			System.out.println(message+"recieved: " + responce);
		}

	}

}
