/**
 * Created by hd on 2017/6/29 AD.
 */
public class Message {
    static final int DEFAULT_VALUE_FOR_TIME_TO_LIVE = 50;
     String content = new String("");
     IP dstIP , srcIP;
     int timeToLive;

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", dstIP=" + dstIP +
                ", srcIP=" + srcIP +
                '}';
    }

    public Message(String content, IP dstIP, IP srcIP ) {
        this.content = content;
        this.dstIP = dstIP;
        this.srcIP = srcIP;
        this.timeToLive = DEFAULT_VALUE_FOR_TIME_TO_LIVE;
    }

    public static int getDefaultValueForTimeToLive() {
        return DEFAULT_VALUE_FOR_TIME_TO_LIVE;
    }

    public String getContent() {
        return content;
    }

    public IP getDstIP() {
        return dstIP;
    }

    public IP getSrcIP() {
        return srcIP;
    }

    public int getTimeToLive() {
        return timeToLive;
    }
}
