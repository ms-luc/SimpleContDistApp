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

  static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6002), "NS");
  //static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");
  //static DNSRecord[] cache = { hisCinema,  herVideo};

  public static DNSRecord[] cacheDNS = new DNSRecord[] {};
  public static DNSRecord[] cacheServers = new DNSRecord[] {herVideo};

  public static void main(String argv[]) throws Exception{

    DNS hisDNS = new DNS(cacheDNS, cacheServers, "His DNS: ", 6001);

    hisDNS.launchServer();


  }


}
