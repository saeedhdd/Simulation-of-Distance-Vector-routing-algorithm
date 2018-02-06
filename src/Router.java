import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;


/**
 * Created by hd on 2017/6/29 AD.
 */
public class Router {


    int ID = 0;
    boolean isPoisonedOn = false;

    public Router(int ID, boolean isPoisonedOn) {
        this.ID = ID;
        this.isPoisonedOn = isPoisonedOn;
    }


    public boolean costChange(Router neigh, int newCost) {
        int oldCost = distanceVector.get(neigh);
        if (newCost<= getCostTo(neigh) | destToNeighbour.get(neigh).equals(neigh) ){
            distanceVector.put(neigh,newCost);
            addToRoutingTable(neigh,neigh);

        }
        updateDistanceVector();
        return distanceVector.get(neigh)!= oldCost ||  updateDistanceVector();



    }

    //Correlated
    private BiMap<Router, Link> neighbours = HashBiMap.create();
    private HashMap<Router, HashMap<Router, Integer>> neighboursDVs = new HashMap<>();
    private ArrayList<IP> interfaces = new ArrayList<>();
    //

    private ArrayList<IP> customers = new ArrayList<>();

    //Correlated
    private HashMap<Router, Router> destToNeighbour = new HashMap<>();
    private Map<Router, Integer> distanceVector = new HashMap<>();
    //

//    HashMap<Router, Boolean> isUsedAsAGuide = new HashMap<>();
    private void addToRoutingTable(Router neighbour, Router destination) {
        destToNeighbour.put(destination, neighbour);
//        boolean IsRemoved = destToNeighbour.remove(neighbour, neighbour);
//
//        if (destToNeighbour.containsValue(neighbour)) isUsedAsAGuide.put(neighbour, true);
//        else isUsedAsAGuide.put(neighbour,false);
//        if (IsRemoved) destToNeighbour.put(neighbour,neighbour);
//        printRoutingTable();

    }
//    public void initializeDV(){
//
//        for (Router neigh:neighbours.keySet()) {
//            this.addToRoutingMaps(neigh, neigh,neighbours.get(neigh).getCost());
//        }
//    }


    public void addLinks(Router neighbour, Link link, IP interfceIP, int time) {
        neighbours.inverse().forcePut(link, neighbour);
        neighboursDVs.put(neighbour, new HashMap<>());
        interfaces.add(interfceIP);
        distanceVector.put(neighbour, link.getCost());
    }
    public void fuck(Router neighbour){
        addToRoutingTable(neighbour,neighbour);
        updateNeighboursDistanceVector(neighbour);
        addNewDsts(neighbour);
        updateDistanceVector();
    }

    public void youAreIntroduced(Router r2) {
        sendUpdateToAll(r2);
    }

    private void updateNeighboursDistanceVector(Router neighbour) {
        Map<Router, Integer> nDV = neighbour.getDistanceVector();
        HashMap<Router, Integer> outDatedNeighbourDV = neighboursDVs.get(neighbour);
        outDatedNeighbourDV.clear();
        for (Router dest : nDV.keySet()) {
            outDatedNeighbourDV.put(dest, nDV.get(dest).intValue());
        }
    }

    private boolean updateDistanceVector() {
        boolean isChanged = false;

        for (Router dest : distanceVector.keySet()) {
            Router neighbour = destToNeighbour.get(dest);
            int newCost =0 ;
            if (dest.equals(neighbour)) {
                newCost = this.neighbours.get(neighbour).getCost();
            }
            else {
                if (neighboursDVs.get(neighbour).get(dest)== null) continue;
                newCost = this.neighbours.get(neighbour).getCost() + neighboursDVs.get(neighbour).get(dest);
            }
                if (getCostTo(dest) != newCost)isChanged= true;
                distanceVector.put(dest, newCost);
                addToRoutingTable(neighbour, dest);

        }


        for (Router dest : distanceVector.keySet()) {
            for (Router neighbour:neighbours.keySet()) {
                int newCost =0 ;
                if (dest.equals(neighbour)) {
                     newCost = this.neighbours.get(neighbour).getCost();
                }
                else {
                    if (neighboursDVs.get(neighbour).get(dest)== null) continue;
                    newCost = this.neighbours.get(neighbour).getCost() + neighboursDVs.get(neighbour).get(dest);
                }
                if (getCostTo(dest) > newCost || destToNeighbour.get(dest).equals(neighbour) ) {
                    if (getCostTo(dest) != newCost)isChanged= true;
                    distanceVector.put(dest, newCost);
                    addToRoutingTable(neighbour, dest);
                }
            }
        }
        return isChanged;

    }
//    private boolean updateDistanceVector2() {
//        boolean isChanged = false;
//        for (Router dest : distanceVector.keySet()) {
//            for (Router neighbour:neighbours.keySet()) {
//                if (dest.equals(neighbour)) continue;
//                if (neighboursDVs.get(neighbour).get(dest)== null) continue;
//                int newCost = this.neighbours.get(neighbour).getCost() + neighboursDVs.get(neighbour).get(dest);
//                if (getCostTo(dest) > newCost) {
//                    distanceVector.put(dest, newCost);
//                    addToRoutingTable(neighbour, dest);
//                    isChanged= true;
//                }
//            }
//        }
//        System.out.println(isChanged);
//        return isChanged;
//
//    }

    public void rcvUpdate(Router neighbour) {
        boolean isAdded = addNewDsts(neighbour);
        updateNeighboursDistanceVector(neighbour);
        boolean isUpdated = updateDistanceVector();
        if (isAdded){
            Log.printtt("Router "+ neighbour + "Added a new destination to Router ", "" + this.ID);
//            this.printRoutingTable();
        }
        if (isUpdated){
            Log.printtt("Router "+ neighbour +  " Changed the Routing table of Router ", "" + this.ID);
            this.printRoutingTable(0);
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
        }
        if (isAdded) sendUpdateToAll(neighbour);
        if (isUpdated)   sendUpdateToAll(null);


    }

    private boolean addNewDsts(Router neighbour) {
        boolean isAdded = false;
        for (Router dst : neighbour.getDistanceVector().keySet()) {
            if (dst.equals(this)) continue;
            if (destToNeighbour.get(dst) == null) {
                addToRoutingTable(neighbour, dst);
                distanceVector.put(dst,Link.MAXIMUM_COST);
                isAdded = true;
            }
        }
        return isAdded;
    }


    void sendUpdateToAll(Router neighbour) {
            for (Router n: neighbours.keySet()) {
                if (n.equals(neighbour)) continue;
                sendUpdate(n);
            }
    }


    private void sendUpdate(Router neighbour) {
        if (isPoisonedOn){
            HashMap<Router , Integer> realCosts = new HashMap<>();
            for (Router dst:distanceVector.keySet()) {
                if (!dst.equals(neighbour) && destToNeighbour.get(dst).equals(neighbour)){
                    realCosts.put(dst, distanceVector.get(dst).intValue());
                    distanceVector.put(dst, Link.MAXIMUM_COST);
                }

            }
            neighbour.rcvUpdate(this);
            if (realCosts.size()==0) return;
            for (Router poisionNeigh:realCosts.keySet()) {
                distanceVector.put(poisionNeigh, realCosts.get(poisionNeigh).intValue());
            }
            return;
        }
        neighbour.rcvUpdate(this);

//        distanceVector.put(destination ,realCost);
    }


    public void sendMsg(Message message, Link dst){
        message.srcIP = message.srcIP==null?getThisInterfaceIP(dst):message.srcIP;
        Log.printtt(message.toString() , " Sent To Router: " , dst.getRouters()[dst.thatRouterIndex(this)].toString() , " to Interface: " ,dst.getConnectedIPs()[dst.thatRouterIndex(this)].toString());
        dst.rcvMsg(message,this);

    }
    public void rcvMsg(Message message, Link origin){
        message.timeToLive--;
        if (message.timeToLive<0){
            Log.printtt(message.toString(), " Dropped ");
            return;
        }
        IP receiverInterface = getThisInterfaceIP(origin);
        String toPrintReceiverIP = receiverInterface==null?"null":receiverInterface.toString();
        Log.printtt(message.toString() , " Rcvd By Router: " , this.toString() , " In Interface: " , toPrintReceiverIP);
        IP destination = message.dstIP;
        if (isForInterfaces(message, destination)){return;}
        if (isForCustomers(message, destination)){return;}
        if (isInRoutingTable(message, destination)){return;}
        if (neighbours.size()!=0){sendMsg(message, neighbours.values().iterator().next());}
    }

    private boolean isInRoutingTable(Message message, IP destination) {
        int max = 0;
        Link correlatedLink= new Link();
        for (Router candidateDst:destToNeighbour.keySet()) {
            Router routingNeigh = destToNeighbour.get(candidateDst);
            for (IP record : candidateDst.customers) {
                if (IP.subbnetEquals(destination, record, record.getSubnetMask())) {
                    if (record.getSubnetMask() > max) {
                        correlatedLink = neighbours.get(routingNeigh);
                        max = record.getSubnetMask();
                    }
                }
            }

            for (IP record : candidateDst.interfaces) {
                if (IP.subbnetEquals(destination, record, record.getSubnetMask())) {
                    if (record.getSubnetMask() > max) {
                        correlatedLink = neighbours.get(routingNeigh);
                        max = record.getSubnetMask();
                    }
                }
            }
        }
        if (max > 0) {
            this.sendMsg(message, correlatedLink);
            return true;
        }
        return false;
    }



    private boolean isForInterfaces(Message message, IP destination) {
        for (IP interfaceIP:interfaces) {
            if (interfaceIP==null) continue;
            if (interfaceIP.equals(destination)){
                Log.printtt(message.toString() , " Was For Router: " , this.toString() );
                return true;
            }
        }return false;
    }

    private boolean isForCustomers(Message message, IP destination) {
        for (int i = 0; i < customers.size(); i++) {
            IP iCustomer= customers.get(i);
            if (iCustomer==null) continue;
            if (IP.subbnetEquals(iCustomer,destination, iCustomer.getSubnetMask())){
                Log.printtt(message.toString() , " delivered to Customer: " , iCustomer.toString());
                return true;
            }
        }return false;
    }

    public Router getConnectedRouter(Link link ){
    return null;
    }


    public IP getThatInterfaceIP(Link link ){
       int thatIndex = link.thatRouterIndex(this);
       return link.getConnectedIPs()[thatIndex];
    }
    public IP getThisInterfaceIP(Link link){
        if (link == null) {
            return null;
        }
        return link.getConnectedIPs()[link.thisRouterIndex(this)];
    }

    public void printRoutingTable(int i) {
        String a = "";
        switch (i){
            case -1:
                a = "start ";
                break;
            case 0:
                a= "while ";
                break;
            case 1:
                a= "end ";
                break;

        }
        System.out.println("*****  t= " +a+DVAlgorithm.time+ " Routing Table of Router: " + this.ID + "   *****");
        String[] row = new String[4];
        row[0] = "    ";
        for (Router Dst : destToNeighbour.keySet()) {
            Router routingNeigh = destToNeighbour.get(Dst);
            System.out.println("Router : " + Dst.toString());
            System.out.println("      cost : " + distanceVector.get(Dst));
            for (IP record : Dst.customers) {
                row[1] = record.toString();
                row[2] = record.getSubnetMask() + "";
                row[3] = neighbours.get(routingNeigh).getID() + "";
                System.out.format("%15s%15s%15s%15s\n", row);

            }
            for (IP record : Dst.interfaces) {
                row[1] = record.toString();
                row[2] = record.getSubnetMask() + "";
                row[3] = neighbours.get(routingNeigh).getID() + "";
                System.out.format("%15s%15s%15s%15s\n", row);

            }
        }
    }

    public BiMap<Router, Link> getNeighbours() {
        return neighbours;
    }

    public ArrayList<IP> getInterfaces() {
        return interfaces;
    }

    public ArrayList<IP> getCustomers() {
        return customers;
    }

//    public HashMap<IP, Link> getRoutingTable() {
//        return routingTable;
//    }


    private int getCostTo(Router dst) {
        if (distanceVector.get(dst)==null) return Link.MAXIMUM_COST;
        return distanceVector.get(dst);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Router router = (Router) o;

        return ID == router.ID;

    }

    public Map<Router, Integer> getDistanceVector() {
        return distanceVector;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public String toString() {
        return this.ID+"";
    }
}

