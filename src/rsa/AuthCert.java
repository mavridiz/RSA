package rsa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;

public class AuthCert {

    public static void main(String args[]) throws NoSuchAlgorithmException {

        int PORT = 5000;

        // Se generan las llaves
        PublicKey publicKey;
        PrivateKey privateKey;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();

        try {
            Scanner sc = new Scanner(System.in);

            // Socket Emisor
            
            ServerSocket socketConexion = new ServerSocket(PORT);
            System.out.println("Esperanding al Emisor...");

            Socket socketDatos = socketConexion.accept();
            System.out.println("Conection Accepted mf");

            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(publicKey.getEncoded().length);
            socketDatos.getOutputStream().write(bb.array());
            socketDatos.getOutputStream().write(publicKey.getEncoded());
            socketDatos.getOutputStream().flush();
            socketDatos.close();
            socketConexion.close();
            System.out.println("Se envió corerectamente la llave pública");

            // Socket Receptor
            
            ServerSocket sC = new ServerSocket(PORT);
            System.out.println("Esperanding al Receptor...");
            Socket socketR = sC.accept();
            System.out.println("Conection Accepted mf");

            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(privateKey.getEncoded().length);
            socketR.getOutputStream().write(b.array());
            socketR.getOutputStream().write(privateKey.getEncoded());
            socketR.getOutputStream().flush();
            socketR.close();
            sC.close();
            System.out.println("Se envió corerectamente la llave privada");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
