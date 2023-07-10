package Wordle.ClientSide.UI;


import VitoBarra.JavaUtil.ColoredOutput.Color;
import VitoBarra.JavaUtil.Decorator.StringColorDecoretor;
import VitoBarra.JavaUtil.Decorator.StringWrapper;
import VitoBarra.JavaUtil.ServerHelper.SessionLogger;
import Wordle.ClientSide.Data.Config.ConsoleUIConfig;
import Wordle.Shared.Data.HintedWord;
import Wordle.Shared.Data.PlayerStatistic;
import Wordle.Shared.Data.Request.*;
import Wordle.Shared.Data.Response.*;
import Wordle.Shared.Data.SharedStatistic;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static VitoBarra.JavaUtil.Decorator.DecoretedStringHelper.ColoredAndCentered;


//Classe che gestisce tutte le stampe del Programma client
public class ConsoleUI implements Closeable {
    ConsoleUIConfig UiConfig;
    SessionLogger ConsoleInteraction;

    public ConsoleUI(ConsoleUIConfig c) {
        UiConfig = c;
        ConsoleInteraction = new SessionLogger(System.in, System.out, UiConfig.getPaddingSpace(), UiConfig.getPaddingCharacter());
    }

    public void Clear() {
        ConsoleInteraction.ClearScreen();
    }

    public ClientGuestRequest GuessMenu() {

        ConsoleInteraction.SendCenteredln("WELCOME IN WORDLE");
        ConsoleInteraction.Sendln("1-Log In \n2-sign in \n3-Close");
        var e = ConsoleInteraction.Prompt("What do you do");


        while (true) {
            switch (e) {
                case "1" -> {
                    return ClientGuestRequest.LogIn;
                }
                case "2" -> {
                    return ClientGuestRequest.SignIn;
                }
                case "3" -> {
                    return ClientGuestRequest.Close;
                }
                default -> {
                    ConsoleInteraction.SendCenteredln("command Not found");
                    e = ConsoleInteraction.Prompt("What do you do");
                }
            }
        }
    }

    public ClientLoggedRequest LoggedMenu(String user) {

        ConsoleInteraction.SendCenteredln(String.format("HELLO %s", user.toUpperCase()));
        ConsoleInteraction.Sendln("1-Make Guess \n2-Visualize Stat \n3-Share Stat \n4-Show Shared Stat\n5-Log Out\n6-Close");
        var e = ConsoleInteraction.Prompt("What do you do");
        while (true) {

            switch (e) {
                case "1" -> {
                    return ClientLoggedRequest.MakeGuess;
                }
                case "2" -> {
                    return ClientLoggedRequest.VisualizeStat;
                }
                case "3" -> {
                    return ClientLoggedRequest.ShareStat;
                }
                case "4" -> {
                    return ClientLoggedRequest.VisualizeSharedStat;
                }
                case "5" -> {
                    return ClientLoggedRequest.LogOut;
                }
                case "6" -> {
                    return ClientLoggedRequest.Close;
                }
                default -> {
                    ConsoleInteraction.SendCenteredln("command Not found");
                    e = ConsoleInteraction.Prompt("What do you do");
                }
            }
            ;
        }
    }

    public String getUserName() {
        return ConsoleInteraction.Prompt("Username");
    }

    public String getPassword() {
        return ConsoleInteraction.Prompt("Password");
    }


    public String PreMakeGuess(PreConditionMakeGuessResponse response) {
        Clear();
        ConsoleInteraction.SendCenteredln(String.format("Guessing Word-%d\\%d", response.Attempts, response.MaxAttempt), Color.Blue);
        ConsoleInteraction.Sendln("");

        PrintAllCurrentHintedWord(response.hintedWords);


        String r = "";
        switch (response.Code) {

            case AlreadyWon -> {
                ConsoleInteraction.Sendln("");
                ConsoleInteraction.SendCenteredln("You won this game", Color.Green);
            }
            case Closed -> {
                ConsoleInteraction.Sendln("");

                ConsoleInteraction.SendCenteredln("You lost this game", Color.Red);
            }
            case CanGuess -> r = ConsoleInteraction.Prompt("Enter word");
        }
        return r;

    }

    public void PrintAllCurrentHintedWord(List<HintedWord> hintedWords) {
        if (hintedWords == null) return;
        for (var currentHintedWord : hintedWords)
            PrintHintedWord(currentHintedWord);
    }


    public void LogOut() {
        Clear();
        ConsoleInteraction.SendCenteredln("Successfully logged out", Color.RedBackground);
    }

    public void PrintStat(PlayerStatistic stat) {
        Clear();
        ConsoleInteraction.SendCenteredln("YOUR STATISTIC");
        int sp = 15;
        String S = "%-" + sp + "s|";
        String StatSpace = S.repeat(4);

        ConsoleInteraction.Send(
                String.format("|" + StatSpace + "\n|" + StatSpace + "\n",
                        ColoredAndCentered("GamePlayed", Color.Reset, sp),
                        ColoredAndCentered("GameWon", Color.Reset, sp),
                        ColoredAndCentered("MaxStreak", Color.Reset, sp),
                        ColoredAndCentered("Streak", Color.Reset, sp),
                        ColoredAndCentered(stat.GamePlayed, Color.Reset, sp),
                        ColoredAndCentered(stat.GamedWon, Color.Reset, sp),
                        ColoredAndCentered(stat.MaxStreak, Color.Green, sp),
                        ColoredAndCentered(stat.Streak, stat.Streak > stat.MaxStreak - 3 ? Color.Green : Color.Yellow, sp)));

        ConsoleInteraction.SendCenteredln("");
        ConsoleInteraction.Send(String.format("%15s: \n%s", "Won Distribution", FormatDistribution(stat.WonDistribution)));
    }

    private String FormatDistribution(Map<Integer, Integer> Map) {
        var stringBuilder = new StringBuilder();
        for (var element : Map.entrySet()) {

            var Value = element.getValue();

            stringBuilder.append(
                    String.format("%5s : %s\n", element.getKey(),
                            new StringColorDecoretor(new StringWrapper(Value + "#".repeat(Value)),
                                    element.getKey() < 5 ? Color.Green : element.getKey() < 10 ? Color.Yellow : Color.Red).Composit()));
        }
        return stringBuilder.toString();
    }

    public void ServerUnexpectedlyClosed() {
        ConsoleInteraction.SendCenteredln("Wordle.Server Closed unexpectedly", Color.Red);
        ConsoleInteraction.Sendln("Closing Wordle.Client...", Color.Red);
    }


    public void ElaborateLogInResponse(LogInResponse response) {
        Clear();
        switch (response.Code) {
            case Logged -> ConsoleInteraction.SendCenteredln("Successfully logged in", Color.GreenBackground);
            case UserDontExist -> ConsoleInteraction.SendCenteredln("User doesn't exist", Color.Red);
            case AlreadyLogged -> ConsoleInteraction.SendCenteredln("Already logged elsewhere", Color.Red);
            case WrongPassword -> ConsoleInteraction.SendCenteredln("Wrong Password", Color.Red);
        }
    }

    public void ElaborateSingInResponse(SingInResponse response) {
        Clear();
        switch (response.Code) {
            case Logged -> ConsoleInteraction.SendCenteredln("Successfully logged in", Color.GreenBackground);
            case AlreadyExist -> ConsoleInteraction.SendCenteredln("User already exist", Color.Red);
            case PasswordEmpty -> ConsoleInteraction.SendCenteredln("Password can't be empty", Color.Red);
        }

    }


    public void ElaborateGuessResponse(MakeGuessResponse response) {
        Clear();
        switch (response.Code) {
            case Valid -> {
                ConsoleInteraction.Sendln();
                PrintHintedWord(response.HintedWord);
                ConsoleInteraction.Sendln();
            }
            case Invalid -> ConsoleInteraction.SendCenteredln("Word not in list", Color.Red);
            case Won -> ConsoleInteraction.SendCenteredln("Word was correct", Color.Green);
            case AttemptsEnded -> ConsoleInteraction.SendCenteredln("Can't do more attempts", Color.Yellow);
            case AlreadyTried -> ConsoleInteraction.SendCenteredln("Word was already tried", Color.Red);
        }
    }

    public void ElaborateSharStatResponse(SharedStatResponse response) {
        Clear();
        switch (response.code) {
            case DidntJoin -> ConsoleInteraction.SendCenteredln("you didn't Join this game yet", Color.Red);
            case AlreadyFinished -> ConsoleInteraction.SendCenteredln("you didn't finish this game yet", Color.Red);
            case Shared -> ConsoleInteraction.SendCenteredln("Shared Successfully", Color.Green);
        }

    }

    public void PrintHintedWord(HintedWord e) {
        ConsoleInteraction.Send(String.format("%" + UiConfig.CalculateMiddle() + "s%10s\n", "", e.toString()));
    }

    public void PrintReceivedStat(List<SharedStatistic> SharedStatistics) {
        Clear();
        ConsoleInteraction.Sendln("");
        if (SharedStatistics.size() == 0)
            ConsoleInteraction.SendCenteredln("You didn't receive nothing yet", Color.Red);

        for (var SharedStat : SharedStatistics) {
            ConsoleInteraction.SendCenteredln((SharedStat.UserName + "'S Play").toUpperCase());
            for (var e : SharedStat.HintedWords)
                PrintHintedWord(e);
        }
    }


    @Override
    public void close() throws IOException {
        ConsoleInteraction.close();
    }

    public void ServerWasClosed() {
        ConsoleInteraction.Sendln("\nWordle.Server was cosed", Color.Red);
    }
}

