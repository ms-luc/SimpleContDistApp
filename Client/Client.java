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
		client.connect("abc/Video");
		System.out.println("fin");

	}

	public void connect(String url) throws Exception{

		boolean cached = false;
		for(int i = 0; i < cache.length; i ++){
			if(cache[i].name.equals(url)){
				System.out.println("found, requesting");
				cached = true;

				//IGNORE THE INDEX.HTML
				getRequest("index.html", cache[i].value.getHostString(), cache[i].value.getPort());
			}
		}

		if(cached == false){

			System.out.println("working on it");
			DNSRecord temp = askDNS(url, localDNS.value.getHostString(), localDNS.value.getPort());
			System.out.println("fin");


			//getRequest("index.html", localDNS.value.getHostString(), localDNS.value.getPort());

		}

		// IF NOT ACCESS THE LOCAL DNS

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
		/*
    DatagramPacket receivePacket =
      new DatagramPacket(receiveData, receiveData.length);

    clientSocket.receive(receivePacket);

    String returnedRecord = new String(receivePacket.getData());

    System.out.println("FROM DNS:" + returnedRecord);*/
    clientSocket.close();

		return new DNSRecord("", new InetSocketAddress("localhost",0), "");

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
