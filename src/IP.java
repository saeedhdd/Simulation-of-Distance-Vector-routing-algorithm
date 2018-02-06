import java.util.Arrays;

/**
 * Created by hd on 2017/6/29 AD.
 */
public class IP {
    private short[] IPAddress = new short[4];
    private int ipIntValue =0 ;
    private int subnetMask;
    public IP(short[] IP) {
        this.IPAddress = IP;
    }
    public IP(String IPString) {
        this(IPString, 32);
    }

    public static void main(String[] args) {
        IP newIP= new IP("12.13.14.255", 24);
        IP newIP2= new IP("12.13.255.14", 24);
        System.out.println(newIP);
        System.out.println(newIP.hashCode());
        System.out.println(newIP2.hashCode());
        System.out.println(newIP.equals(newIP2));
        System.out.println(subbnetEquals(newIP, newIP2, 16));
    }


    public static boolean subbnetEquals(IP ip1 , IP ip2, int subnetMask){
        subnetMask =( 32 - subnetMask);
        int Mask= -1 - ((int)Math.pow(2, subnetMask)-1);
        int first= ip1.getIpIntValue()&Mask ;
        int second= ip2.getIpIntValue()&Mask ;
        return (first == second);
    }

    public IP(String IPString, int subnetMask) {
        String[] bytes = IPString.split("\\.");
        if (bytes.length!=4){
            throw new RuntimeException("Wrong Ip address!");
        }
        short[] newIP = new short[4];

        for (int i = 0; i < 4; i++) {
            newIP[i]= Short.parseShort(bytes[i]);
        }
        this.IPAddress = newIP;

        for (int j = 0; j < 4; j++) {
            ipIntValue = ipIntValue<<8;
            ipIntValue |= this.IPAddress[j];
        }

        this.subnetMask =  subnetMask;
    }

    public short[] getIPAddress() {
        return IPAddress;
    }




    @Override
    public String toString() {
        StringBuffer ipString = new StringBuffer();
        for (short byt: IPAddress) {
            ipString.append(byt);
            ipString.append(".");
        }
        ipString.deleteCharAt(ipString.length()-1);
        return ipString.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IP ip = (IP) o;

        if (subnetMask != ip.subnetMask) return false;
        return Arrays.equals(IPAddress, ip.IPAddress);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(IPAddress);
        result = 31 * result + subnetMask;
        return result;
    }

    public int getIpIntValue() {
        return ipIntValue;
    }

    public int getSubnetMask() {
        return subnetMask;
    }
}
