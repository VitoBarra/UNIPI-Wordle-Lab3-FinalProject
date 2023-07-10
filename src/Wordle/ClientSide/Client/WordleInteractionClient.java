package Wordle.ClientSide.Client;

import VitoBarra.JavaUtil.ServerHelper.GsonSessionLogger;
import Wordle.Shared.Data.PlayerStatistic;
import Wordle.Shared.Data.Request.ClientGuestRequest;
import Wordle.Shared.Data.Request.ClientLoggedRequest;
import Wordle.Shared.Data.Response.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WordleInteractionClient extends GsonSessionLogger
{


    public WordleInteractionClient(InputStream in, OutputStream out) {
        super(in, out);
    }


    public MakeGuessResponse ReceiveGuessResponse() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }

    public SharedStatResponse ReceiveSharedStatResponse() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }


    public LogInResponse ReceiveLogInResponse() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }

    public SingInResponse ReceiveSingInResponse() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }


    public PreConditionMakeGuessResponse ReceivePreConditionMakeGuess() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }

    public PlayerStatistic ReceivePlayerStatistic() throws IOException {
        return ReceiveJson(new TypeToken<>() {
        });
    }


    public void SendClientGuestRequest(ClientGuestRequest e) {
        SendJson(e);
    }

    public void SendClientLoggedRequest(ClientLoggedRequest e) {
        SendJson(e);
    }

    public void SendUserAndPassword(String UserName, String password) {
        Sendln(UserName);
        Sendln(password);
    }
}
