/**
 * Created by hd on 2017/6/30 AD.
 */
public class TestRouter {
    public static void main(String[] args) {
//        Router r1 = new Router();
//        Router r2 = new Router();
//        Router r3 = new Router();
//        r1.ID=1;
//        r2.ID=2;
//        r3.ID=3;
//        IP N1 = new IP("192.168.2.0", 24);
//        IP N2 = new IP("192.168.1.0", 24);
//        r1.customers.add(N1);
//        r2.customers.add(N2);
//
//
//        IP[] ips12= {new IP("1.1.2.2"),new IP("1.1.2.1")};
//        Router[] routers12= {r1,r2};
//        Link l12 = new Link(12,5,ips12,routers12);
//
//
//        IP[] ips13= {new IP("1.1.1.1"),new IP("1.1.1.2")};
//        Router[] routers13= {r1,r3};
//        Link l13 = new Link(13,6,ips13,routers13);
//
//
//        IP[] ips23= {new IP("1.1.3.2"),new IP("1.1.3.1")};
//        Router[] routers23= {r2,r3};
//        Link l23 = new Link(23,70,ips23,routers23);
//
//
//
//        r1.routingTable.put(N2,l12.getID());
//        r1.routingTable.put(r1.getThatInterfaceIP(l13),l13.getID());
//        r1.routingTable.put(r2.getThisInterfaceIP(l23),l12.getID());
//        r1.routingTable.put(r1.getThatInterfaceIP(l12),l12.getID());
//        r2.routingTable.put(N1,l12.getID());
//        r2.routingTable.put(r2.getThatInterfaceIP(l23),l12.getID());
//        r2.routingTable.put(r2.getThatInterfaceIP(l12),l12.getID());
//        r3.routingTable.put(N1,l13.getID());
//        r3.routingTable.put(N2,l13.getID());
//        r3.routingTable.put(r3.getThatInterfaceIP(l13),l13.getID());
//        r3.routingTable.put(r3.getThatInterfaceIP(l23),l13.getID());
//
//        r1.printRoutingTable();
//        r2.printRoutingTable();
//        r3.printRoutingTable();
//
//        r3.rcvMsg(new Message("Hello",new IP("1.1.3.2") ,null),null);
//
//    }
    IP ip1 = new IP("192.168.1.0",24);
    IP ip2 = new IP("192.168.1.1",32);
        System.out.println(IP.subbnetEquals(ip1,ip2,ip1.getSubnetMask()));
    }
}
