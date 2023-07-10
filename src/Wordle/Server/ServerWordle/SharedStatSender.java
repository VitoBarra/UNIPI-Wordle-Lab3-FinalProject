package Wordle.Server.ServerWordle;

import Wordle.Shared.Data.SharedStatistic;
import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SharedStatSender implements Closeable {

    static SharedStatSender Instance;
    private final Gson JW;
    MulticastSocket MultiCast;

    InetAddress Address;

    int Port;

    public SharedStatSender(InetAddress _Address, int port, boolean reuseFlag) throws IOException {

        MultiCast = new MulticastSocket();
        MultiCast.setReuseAddress(reuseFlag);
        MultiCast.joinGroup(_Address);
        Address = _Address;
        Port = port;
        JW = new Gson();
        Instance = this;
    }

    public SharedStatSender(String multiCastGroup, int port, boolean reuseflag) throws IOException {
        this(InetAddress.getByName(multiCastGroup), port, reuseflag);
    }


    public static SharedStatSender getInstance() {
        return Instance;
    }

    public static void Close() {
        getInstance().close();
    }

    public void Share(SharedStatistic SharedStatistic) {
        var e = JW.toJson(SharedStatistic);

        try {
            MultiCast.send(new DatagramPacket(e.getBytes(), e.length(), Address, Port));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            MultiCast.leaveGroup(Address);
        } catch (IOException e) {
        }
    }
}
