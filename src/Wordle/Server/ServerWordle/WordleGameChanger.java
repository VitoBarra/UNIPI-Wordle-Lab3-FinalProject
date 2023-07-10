package Wordle.Server.ServerWordle;


import VitoBarra.JavaUtil.Other.ThreadPoolUtil;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WordleGameChanger implements Closeable {
    static WordleGameChanger Instance;
    ScheduledFuture<?> NewWordTask;
    ScheduledExecutorService ChangeWordle;

    int Delay;

    public WordleGameChanger(int delay) {
        Instance = this;
        Delay = delay;
        ChangeWordle = Executors.newSingleThreadScheduledExecutor();
        NewWordTask = ChangeWordle.scheduleAtFixedRate(this::CreateNewSecureWordLogged, 0, Delay, TimeUnit.MINUTES);
    }

    public static WordleGameChanger getInstance() {
        return Instance;
    }


    private void CreateNewSecureWordLogged() {
        System.out.print("\nAutomatically Created New secret word created: " + ServerManager.NewWordleGame() + "\n>");
    }

    public void ForceCreateNewGame() {
        System.out.println("\nNew secret word created: " + ServerManager.NewWordleGame());
        Reset();
    }

    private void Reset() {
        NewWordTask.cancel(false);
        NewWordTask = ChangeWordle.scheduleAtFixedRate(this::CreateNewSecureWordLogged, Delay, Delay, TimeUnit.MINUTES);
    }

    @Override
    public void close() {
        ThreadPoolUtil.TryAwaitTermination(ChangeWordle, 3, TimeUnit.SECONDS, "WGameChanger");
    }

    public static void Close() {
        getInstance().close();
    }
}
