package Wordle.Shared.Protocol;

public class InteractionProtocol {

    public static final String IpDivider = "\u00A2\u00A2";

    public static String LoginMessage(String User, String Ip, int port) {
        return User + IpDivider + Ip + IpDivider + port;
    }


}
