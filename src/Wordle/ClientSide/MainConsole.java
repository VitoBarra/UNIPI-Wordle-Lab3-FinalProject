package Wordle.ClientSide;


import VitoBarra.JavaUtil.Other.ConsoleStarter;

import java.io.IOException;

public class MainConsole {
    //Main per l avvio di una cosole se non è presente (apertura con doppio click) o utilizza quella corrente se il jar viene avviato da linea di comando
    public static void main(String[] args) throws IOException {

        if (ConsoleStarter.ConsoleStarter(MainConsole.class.getProtectionDomain())) {
            ClientMain.main(args);
            System.out.println("Program has ended, please type 'exit' to close the console");
        }
    }


}
