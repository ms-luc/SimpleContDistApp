package client;

import java.io.*;
import java.net.*;

class Client {

	DNSRecord hisCinemma = new DNSRecord("www.hiscinema.com", new InetSocketAddress("localhost",6789), " ");
	DNSRecord localDNS = new DNSRecord("dns.local", new InetSocketAddress("localhost", 6000), " ");
	DNSRecord[] cache = new DNSRecord[]{hisCinemma};

	static String message = "CLIENT: "; //client message

	public static void main(String argv[]) throws Exception {

		Client client = new Client();

		client.connect("abc/Video");
		System.out.println(message+ "terminating");

	}

	public void connect(String url) throws Exception{

		System.out.println("\n"+message+ "connecting to " + url);

		boolean cached = false;
		for(int i = 0; i < cache.length; i ++){
			if(cache[i].name.equals(url)){
				System.out.println(message+ "found url: " + url + " in cahce, requesting");
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

	  DatagramSocket clientSocket = new DatagramSocket();

	  InetAddress IPAddress = InetAddress.getByName(ip);

	  byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    String sentence = url;

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

	public static void getRequest(String fileName, String serverName, int serverPort) throws Exception{

		System.out.println(message+ "fetching file");

		Socket sock = new Socket(serverName, serverPort);

		//DataOutputStream outToServer = new DataOutputStream(sock.getOutputStream());

		//outToServer.writeBytes(fileName + "\n");

		BufferedReader inFromServer =
			new BufferedReader(new InputStreamReader(sock.getInputStream()));

		String responce = inFromServer.readLine();

		System.out.println(message+"getting file: " + responce);

		if(responce.equals(message+"404 NOT FOUND")){

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

			System.out.println(message+"recieved: " + responce);
		}

	}

}
