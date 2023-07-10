package Wordle.Server.ServerWordle;

import Data.Config.ServerConfig;
import VitoBarra.JavaUtil.Other.ThreadPoolUtil;
import Wordle.Server.Session.Session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class ServerWordle {


    ServerSocket WelcomeSocket;
    ExecutorService ClientConnections;
    volatile Boolean ServerAlive = true;
    ServerConfig config;

    public ServerWordle(ServerConfig _config) throws IOException {

        //Sessioni impostate come demoni per permettere la chiusura al servere ed evitare che un client ne impedisca la chiusura
        ClientConnections = Executors.newCachedThreadPool(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });

        config = _config;
        WelcomeSocket = new ServerSocket(config.WelcomePort());
    }


    public void StartAccept() {

        try {
            WelcomeSocket.setSoTimeout(config.WelcomeTimeoutSeconds());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        do {
            try {
                ClientConnections.execute(new Session(WelcomeSocket.accept()));
            } catch (RejectedExecutionException e) {
                System.out.println("can't handle connection");
            } catch (SocketTimeoutException ex) {

            } catch (IOException e) {
                System.out.println("Closed!");
            }
        } while (ServerAlive);

    }


    public void Close() {

        ServerAlive = false;

        ThreadPoolUtil.TryAwaitTermination(ClientConnections, 30, TimeUnit.SECONDS, "WelcomeServer");

        try {
            WelcomeSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
