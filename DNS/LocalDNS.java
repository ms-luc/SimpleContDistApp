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

  static DNSRecord hisCinemaDNS = new DNSRecord("dns.hiscinema.com", new InetSocketAddress("localhost",6001), "");
  //static DNSRecord herVideo = new DNSRecord("abc/Video", new InetSocketAddress("localhost",6000), "");
  //static DNSRecord[] cache = { hisCinema,  herVideo};

  public static DNSRecord[] cache = new DNSRecord[] {hisCinemaDNS};

  public static void main(String argv[]) throws Exception{

    DNS localDNS = new DNS(cache, "Local DNS", 6000);

    localDNS.launchServer();


  }


}
