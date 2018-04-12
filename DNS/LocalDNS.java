package dns;
import java.io.*;
import java.net.*;

/*

  DNS Server

  Recieve Request. (UDP)
  Process and forward to the requested site's DNS
  All IP's are known and stored

*/

public class LocalDNS extends DNS{

  //static DNS localDNS;

  static DNSRecord hisDNS = new DNSRecord("dns.hiscinema.com", new InetSocketAddress("141.117.232.50",40200), "NS");
  static DNSRecord herDNS = new DNSRecord("dns.herCDN.com", new InetSocketAddress("141.117.232.48",40200), "NS");
  //static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");
  //static DNSRecord[] cache = { hisCinema,  herVideo};

  public static DNSRecord[] cacheDNS = new DNSRecord[] {hisDNS, herDNS};
  public static DNSRecord[] cacheServers = new DNSRecord[] {};

  public static void main(String argv[]) throws Exception{

    DNS localDNS = new DNS(cacheDNS, cacheServers, "Local DNS: ", 40200);

    localDNS.launchServer();


  }


}
