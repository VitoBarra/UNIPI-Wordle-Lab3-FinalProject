package Wordle.Server;

import VitoBarra.JavaUtil.ServerHelper.CommandReader;
import VitoBarra.JavaUtil.String.StringUtil;
import Wordle.Server.Data.IO.MemorizationFactory.JsonMemorizationFactory;
import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Server.ServerWordle.ServerWordle;
import Wordle.Server.ServerWordle.SharedStatSender;
import Wordle.Server.ServerWordle.WordleGameChanger;

import java.io.IOException;

public class ServerMain {

    static volatile boolean Closed = false;
    static ServerWordle WelcomeServer;

    static boolean IsValid;

    public static void main(String[] args) {


        Runtime.getRuntime().addShutdownHook(new Thread(ServerMain::CloseServer));

        try {
            InitializeServer();
            if (!IsValid) return;
            CreateCommandReader();
            WelcomeServer.StartAccept();
        } catch (IOException e) {
            CloseServer();
            throw new RuntimeException(e);
        }

    }

    private static void InitializeServer() throws IOException {
        new ServerManager(new JsonMemorizationFactory());

        if (!ServerManager.IsValid()) {
            CloseServer();
            return;
        }

        var config = ServerManager.GetConfiguration();
        new SharedStatSender(config.MultiCastGroup(), config.MulticastPort(), true);
        new WordleGameChanger(config.NewGameAfterMinutes());

        WelcomeServer = new ServerWordle(config);

        IsValid = true;
    }

    private static void CreateCommandReader() {
        new CommandReader(System.in, System.out) {
            @Override
            public String getMenuString() {
                return "exit or quit: close server\nchange or new: to force a new game\nhelp: To see command list\n>";
            }

            @Override
            public boolean CommandHandler(String CommandString) {
                if (CommandString == null) return false;


                if (StringUtil.IsEqualToAny(CommandString, "exit", "quit")) {
                    CloseServer();
                    return true;
                } else if (StringUtil.IsEqualToAny(CommandString, "change", "new")) {
                    WordleGameChanger.getInstance().ForceCreateNewGame();
                    writer.print(">");
                } else if (StringUtil.IsEqualToAny(CommandString, "Help", "help")) {
                    writer.print(getMenuString());
                } else writer.print("Command Not Found\n>");

                return false;
            }
        };
    }

    private static void CloseServer() {
        if (Closed) return;
        Closed = true;
        System.out.println();
        ServerManager.Close();


        if (!IsValid) return;

        SharedStatSender.Close();
        WordleGameChanger.Close();
        WelcomeServer.Close();


    }
}
