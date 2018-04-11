package dns;

import java.net.*;

public class DNSRecord{

  public String name;
  public InetSocketAddress value;
  public String type;

  DNSRecord(){

  }

  DNSRecord(String name, InetSocketAddress value, String type){

      this.name = name;
      this. value = value;
      this. type = type;

  }

  public DNSRecord toRecord(String input){

    // splits recieved data into an array, data is delimited with ","
    // split into data[0] which is name, data[1] which is InetSock.toString
    //  and data[2] which is type
    String[] data = input.split(",");

    // splits the data[1] into Ip and Port
    String[] ipp = data[1].split("[/:]"); //IP and PORT, 0 and 1 respectively

    // create a new Inet Sock Address and fill in value and type
    if( ipp[0].equals("localhost")) // if the ip is local host reset ip to local host
      ipp[1] = ipp[0];
      //return new DNSRecord(data[0], new InetSocketAddress(ipp[0], Integer.valueOf(ipp[2])), data[2].replaceAll("\0", ""));

    return new DNSRecord(data[0], new InetSocketAddress(ipp[1], Integer.valueOf(ipp[2])), data[2].replaceAll("\0", ""));
  }


  public String toString(){
    //return "DNS Record, name: " + name + " IP: " + value.getHostString + " Port: " + value.getPort + " type: " + type;
    return name + "," + value + "," + type;
  }

}
