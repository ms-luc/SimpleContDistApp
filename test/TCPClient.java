import java.io.*;
import java.net.*;

class TCPClient {

	public static void main(String argv[]) throws Exception
	{
		String sentence;
		String modifiedSentence;

		 

		BufferedReader inFromUser =
		new BufferedReader(new InputStreamReader(System.in));


		System.out.println(InetAddress.getLocalHost());

		Socket clientSocket = new Socket(InetAddress.getLocalHost(), 40200);


		
		DataOutputStream outToServer =
		new DataOutputStream(clientSocket.getOutputStream());



		BufferedReader inFromServer =
		new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		sentence = inFromUser.readLine();


		for(;;)
			outToServer.writeBytes(sentence + '\n');



		//modifiedSentence = inFromServer.readLine();

		//System.out.println("FROM SERVER: " + modifiedSentence);
		//clientSocket.close();
		//clientSocket.close();

	} 
} 
