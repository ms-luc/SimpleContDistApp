package serverhis;

import java.net.*;

public class DNSRecord{

  public String name;
  public InetSocketAddress value;
  public String type;

  DNSRecord(String name, InetSocketAddress value, String type){

      this.name = name;
      this. value = value;
      this. type = type;

  }

  public String toString(){
    //return "DNS Record, name: " + name + " IP: " + value.getHostString + " Port: " + value.getPort + " type: " + type;
    return "DNS Record, name: " + name + " IP: " + value + " type: " + type;
  }

}
