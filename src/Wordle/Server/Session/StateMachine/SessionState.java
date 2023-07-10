package Wordle.Server.Session.StateMachine;

import java.io.IOException;

public interface SessionState {

    void MenageOperation(SessionContext context) throws IOException;
}
