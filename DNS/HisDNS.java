package dns;
import java.io.*;
import java.net.*;

/*

  DNS Server

  Recieve Request. (UDP)
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class HisDNS extends DNS{

  //static DNS localDNS;

  static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6003), "");
  //static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");
  //static DNSRecord[] cache = { hisCinema,  herVideo};

  public static DNSRecord[] cache = new DNSRecord[] {herVideo};

  public static void main(String argv[]) throws Exception{

    DNS hisDNS = new DNS(cache, "Local DNS", 6001);

    hisDNS.launchServer();


  }


}
