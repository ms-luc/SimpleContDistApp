package client;

public class DNSRecord{

  public String name;
  public String value;
  public String type;

  DNSRecord(String name, String value, String type){

      this.name = name;
      this. value = value;
      this. type = type;

  }

  public String toString(){
    return "DNS Record, name: " + name + " value: " + value + " type: " + type;
  }

}
