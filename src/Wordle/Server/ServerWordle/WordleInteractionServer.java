package Wordle.Server.ServerWordle;

import VitoBarra.JavaUtil.ServerHelper.GsonSessionLogger;
import Wordle.Shared.Data.PlayerStatistic;
import Wordle.Shared.Data.Request.*;
import Wordle.Shared.Data.Response.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class WordleInteractionServer extends GsonSessionLogger
{



    public WordleInteractionServer(InputStream in, OutputStream out) {
        super(in, out);
    }


    public void SendGuessResponse(MakeGuessResponse e) {
        SendJson(e);
    }

    public void SendSharedStatResponse(SharedStatResponse e) {
        SendJson(e);
    }

    public void SendLogInResponse(LogInResponse e) {
        SendJson(e);
    }

    public void SendPreConditionMakeGuess(PreConditionMakeGuessResponse e) {
        SendJson(e);
    }

    public void SendPlayerStatistic(PlayerStatistic e) {
        SendJson(e);
    }

    public void SendRegisterResponse(SingInResponse e) {
        SendJson(e);
    }



    public ClientGuestRequest ReceiveGuestRequest() throws IOException {
        return  ReceiveJson(new TypeToken<>(){});
    }

    public ClientLoggedRequest ReceiveLoggedRequest() throws IOException {
      return ReceiveJson(new TypeToken<>(){});
    }
}
