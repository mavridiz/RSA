package Proyect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Comunication {

    public void sendBytes( int PORT, byte[] message ) {
        try {
            
            ServerSocket socketSecAdd = new ServerSocket(PORT);
            System.out.println("Esperanding al Receptor...");
            
            Socket socketSecAddData = socketSecAdd.accept();
            System.out.println("Connection Accepted mf");
            
            socketSecAddData.getOutputStream().write(message);
            socketSecAddData.getOutputStream().flush();
            socketSecAddData.close();
            socketSecAdd.close();
            
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
