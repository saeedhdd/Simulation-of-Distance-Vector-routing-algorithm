/**
 * Created by hd on 2017/6/29 AD.
 */
public class Link {
    static int MAXIMUM_COST = 500;
    private int ID;
    private int cost;
    private IP[] connectedIPs = new IP[2];
    private Router[] routers = new Router[2];

    public Link() {
    }

    public Link(int ID, int cost, IP[] connectedIPs, Router[] routers) {
        this.ID = ID;
        this.cost = cost;
        this.connectedIPs = connectedIPs;
        this.routers = routers;
    }

    public int getID() {

        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public IP[] getConnectedIPs() {
        return connectedIPs;
    }

    public void setConnectedIPs(IP[] connectedIPs) {
        this.connectedIPs = connectedIPs;
    }

    public Router[] getRouters() {
        return routers;
    }
    public Integer thisRouterIndex(Router router){
        return routers[0]== router? 0:1;
    }
    public Integer thatRouterIndex(Router router){
        return routers[0]== router? 1:0;
    }
    public void setRouters(Router[] routers) {
        this.routers = routers;
    }

    public void rcvMsg(Message message, Router origin) {
        sendMsg(message,routers[thatRouterIndex(origin)]);

    }

    public void sendMsg(Message message, Router dst) {
        dst.rcvMsg(message,this);
    }

    @Override
    public String toString() {
        return "Link{" +
                "ID=" + ID +
                '}';
    }

    public void costChange(int i) {
        cost=i;
        boolean firstChanged = routers[0].costChange(routers[1],i);
        boolean secondChanged = routers[1].costChange(routers[0],i);
        if (firstChanged)routers[0].sendUpdateToAll(null);
        if (secondChanged)routers[1].sendUpdateToAll(null);
    }
}
