import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by hd on 2017/6/29 AD.
 */
public class DVAlgorithm {
    static int time;
    static HashMap<Integer , Router> allRouters = new HashMap<>();
    static HashMap<Integer , Link> allLinks = new HashMap<>();
    static boolean poisonOn = false;
    public static void main(String[] args) {
        System.out.println("Default time to live : " + Message.DEFAULT_VALUE_FOR_TIME_TO_LIVE);
        System.out.println("Default Maximum Cost : " + Link.MAXIMUM_COST);
        System.out.println("Please enter subnet mask as an integer! ");
        System.out.println("The update process isn't synchronous. \n" +
                "Each time a node changes it will send an update to the neighbours. ");
        Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
        if (command.toLowerCase().equals("poison on")) poisonOn = true;
        ArrayList<String[]> commandsByTime = new ArrayList<>();
        while (true){
            command= scanner.nextLine();
            String[] splitted = command.split(" ");
            if (splitted.length<=1) continue;
            if (splitted[1].toLowerCase().equals("endsim"))break;
            commandsByTime.add(splitted);
        }

        Collections.sort(commandsByTime, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o1[0])<Integer.parseInt(o2[0])?-1:1;
            }
        });
        for (String[] com:commandsByTime) {
            int newtime = Integer.parseInt(com[0]);
            int oldTime = time;
            if (newtime>time){
                time = newtime;
                for (Router router:allRouters.values()) {
                    router.printRoutingTable(-1);
                }
            }
            try {

                Method methode = DVAlgorithm.class.getDeclaredMethod(com[1].toLowerCase(), String[].class);
                methode.invoke(null, new Object[] {com});
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (newtime>oldTime){
                for (Router router:allRouters.values()) {
                    router.printRoutingTable(1);
                }
            }
        }
    }
    static void newrouter(String[] command){
        if (command.length<3){return;}
        int ID = Integer.parseInt(command[2]);
        allRouters.put(ID, new Router (ID, poisonOn));
        Log.printtt("Router : ", ID+"" ," created. ");
    }
    static void newcustomer(String[] command){
        if (command.length<3){return;}
        String ipAddress = (command[2]);
        int mask = Integer.parseInt(command[3]);
        int routerID = Integer.parseInt(command[4]);
        allRouters.get(routerID).getCustomers().add(new IP(ipAddress, mask));
        Log.printtt("Customer : ", ipAddress+"/"+ mask ," added to ", "router : "+ routerID+"");
    }
    static void newlink(String[] command){
        if (command.length<8){return;}
        int linkID = Integer.parseInt(command[2]);
        Router r1 = allRouters.get(Integer.parseInt(command[3]));
        IP ip1 = new IP(command[4], 32);
        Router r2 = allRouters.get(Integer.parseInt(command[5]));
        IP ip2 = new IP(command[6], 32);
        int cost = Integer.parseInt(command[7]);
        IP[] ips = {ip1, ip2};
        Router[] routers = {r1, r2};
        Link link = new Link(linkID,cost,ips,routers);
        allLinks.put(linkID , link);
        Log.printtt("Link : ",linkID+"" ," between Routers ",r1.ID+" : "+ ip1,r2.ID+" : "+ ip2 ," created. ");
        r1.addLinks(r2,link,ip1 , time);
        r2.addLinks(r1,link,ip2, time);
        r2.fuck(r1);
        r1.fuck(r2);
        r1.youAreIntroduced(r2);
        r2.youAreIntroduced(r1);

    }
    static void sendmsg(String[] command){
        if (command.length<5){return;}
        Router sendingRooter = allRouters.get(Integer.parseInt(command[2]));
        Message message = new Message(command[4], new IP(command[3],32) , null);
        Log.printtt("Message"+message.getContent() ,"sent to Router :" ,sendingRooter.toString() );
        sendingRooter.rcvMsg(message, null);
    }
    static void costchange(String[] command){
        if (command.length<4){return;}
        Link link = allLinks.get(Integer.parseInt(command[2]));
        Log.printtt("Cost : ",link.getID()+" changed to ", ""+ Integer.parseInt(command[3]));
        link.costChange(Integer.parseInt(command[3]));
    }
}


