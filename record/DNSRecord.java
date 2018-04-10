package record;

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

  public DNSRecord convertToRecord(ObjectOutPutStream recievedData){

    //some operation to convert a ObjectOutPutStream object to DNSrecord Object
    // basically retrieve the ObjectOutPutStream as a string
    // then set the values for name, value, type

    return DNSRecord
  }

  public ObjectOutPutStream convertToObjectOutputStream(DNSRecord toBeSent){

    //some operation to convert a DNSRecord Object to an ObjectOutPutStream Object

    return ObjectOutPutStream
  }

  public String toString(){
    //return "DNS Record, name: " + name + " IP: " + value.getHostString + " Port: " + value.getPort + " type: " + type;
    return "DNS Record, name: " + name + " IP: " + value + " type: " + type;
  }

}
