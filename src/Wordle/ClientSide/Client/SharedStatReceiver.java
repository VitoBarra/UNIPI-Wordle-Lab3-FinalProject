package Wordle.ClientSide.Client;


import Wordle.Shared.Data.SharedStatistic;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedStatReceiver implements Closeable {

    private final CopyOnWriteArrayList<SharedStatistic> SharedStatistics;
    String Username;

    private final MulticastSocket multiSocket;
    private final InetAddress GroupIp;
    private final byte[] Buffer;

    boolean Closed = false;

    public SharedStatReceiver(String username, String Group, int Port, int bufferDimension) {
        Username = username;
        SharedStatistics = new CopyOnWriteArrayList<>();
        try {
            GroupIp = InetAddress.getByName(Group);
            multiSocket = new MulticastSocket(Port);
            multiSocket.joinGroup(GroupIp);
            multiSocket.setSoTimeout(ClientManager.GetConfig().StatReceiverTimeOutSeconds());
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }

        Buffer = new byte[1024 * bufferDimension];

        var SharedStatReceiver = new Thread(this::StatReceiver);
        SharedStatReceiver.setDaemon(true);
        SharedStatReceiver.start();
    }

    public void StatReceiver() {
        while (!Closed) {
            var isDataPresent = true;

            var dp = new DatagramPacket(Buffer, Buffer.length);
            try {
                multiSocket.receive(dp);
            } catch (SocketTimeoutException ex) {
                isDataPresent = false;
            } catch (IOException ex) {
                close();
                return;
            }
            if (isDataPresent)
                ElaborateDataGram(new String(dp.getData(), 0, dp.getLength()));
        }
    }

    public void ElaborateDataGram(String str) {
        var SharedStat = SharedStatistic.CreateFromJson(str);
        if (SharedStat != null && !SharedStat.UserName.equals(Username))
            synchronized (SharedStatistics) {
                SharedStatistics.add(SharedStat);
            }
    }

    @Override
    public void close() {
        Closed = true;
        try {
            multiSocket.leaveGroup(GroupIp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SharedStatistic> getSharedStatistics() {
        List<SharedStatistic> CllonedList = (List<SharedStatistic>) SharedStatistics.clone();
        SharedStatistics.clear();
        return CllonedList;
    }
}
