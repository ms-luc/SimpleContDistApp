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

	public static void main(String[] args) throws Exception {

		Client client = new Client();

		if( args.length != 0 )
			client.connect(args[0]);
		//client.connect("www.hiscinema.com");
		//client.connect("www.hiscinema.com/index.html");
		//client.connect("www.hiscinema.com/home/index1.html");

		//client.connect("abc/Video");

	}

	public void connect(String url) throws Exception{

		// SPLIT URL
		String[] urlAndPath = urlSplit(url);

		url = urlAndPath[0];
		String filePath = urlAndPath[1];
		// SPLIT URL END

		System.out.println("\n"+message+ "connecting to " + url);

		// check if cached
		// if cached fectch record and connect
		boolean cached = false;
		for(int i = 0; i < cache.length; i ++){
			if(cache[i].name.equals(url)){
				System.out.println(message+ "found record for " + url + " in cache, sending GET Request");
				cached = true;

				System.out.println(message+"requesting: " + filePath);
				getRequest(filePath, cache[i].value.getHostString(), cache[i].value.getPort());
				System.out.println(message+"terminating connection to: " + url);
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

	// splits url to the domain URL and a file path(string)
	// for example
	// url: www.herCDN.com/media/videos/video.mp4
	// result: www.herCDN.com and /media/videos/video.mp4 split
	public static String[] urlSplit(String url){

		// split url into an array with delimiter /
		String[] temp = url.split("/");

		// to be returned array with url and path
		String[] urlAndPath = new String[2];

		// set url
		urlAndPath[0] = temp[0];

		//init urlAndPath[1]
		urlAndPath[1] = "";

		// set path
		for( int i = 1; i < temp.length; i++){
			urlAndPath[1] = urlAndPath[1] + "/" + temp[i];
		}

		if(urlAndPath[1].equals(""))
			urlAndPath[1] = "/index.html";

		// return url and path
		return urlAndPath;
	}

	public static void getRequest(String fileName, String serverIP, int serverPort) throws Exception{

		System.out.println(message+ "fetching:"+ fileName+"\n");

		Socket clientSocket = new Socket(serverIP, serverPort);


		// WRITE GET REQUEST to server

		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		outToServer.writeBytes("GET " + fileName + "\n");

		// END WRITE GET REQUEST

		// GET ACK FROM SERVER
		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		String originalMessage = inFromServer.readLine();
		String[] ack = originalMessage.split(" ");
		String serverMsg = ack[0];

		// if not ACKed fail
		if(!serverMsg.equals("ACK")){

			// print error message and close socket
			System.out.println(message+" from server: " + originalMessage);
			clientSocket.close();

			// break out of getRequest();
			return;
		}
		// if fail END

		int messageValue = Integer.valueOf(ack[1]); // aka file size or error value

		System.out.println(message+"getting file: " + fileName + " (size: " + messageValue + ")");

		InputStream fileFromServer = clientSocket.getInputStream();


		// FILE TRANFER BEINGS
			// rename messageValue to make it more undestandable
			int fileSize = messageValue;

			// create temp array for buffering in the file
	    byte[] fileBufferArray = new byte[fileSize];

			// create temp value to hold where the last data packet
			// wrote to the buffer
	    int temp = 0;

			// read from server data packet by data packet
			for(int i = 1; i > 0;){

				// write to buffer from current temp to the size of data packet
				i = fileFromServer.read(fileBufferArray, temp, (fileBufferArray.length-temp));
				// increment temp
				temp += i;
			}

			// finally get the array and write it to a file
			File recieved = new File("." + fileName);
			recieved.getParentFile().mkdirs();
			recieved.createNewFile();

			new FileOutputStream("." + fileName).write(fileBufferArray, 0 , fileSize);
	    // you can use .close() to close the file stream

		// FILE TRANSFER ENDS

			System.out.println(message+"download complete");

			// close the socket
	    clientSocket.close();




	}

}
