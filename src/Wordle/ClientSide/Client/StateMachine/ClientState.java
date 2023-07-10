package Wordle.ClientSide.Client.StateMachine;

import java.io.IOException;

public interface ClientState {
    void MenageOperation(ClientContext context) throws IOException;

}
