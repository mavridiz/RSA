package rsa;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Emisor {

    public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        int PORT = 5000;

        try {

            //Autoridad Certificadora
            Scanner sc = new Scanner(System.in);

            System.out.println("Ingresa la IP de la autoridad certificadora: ");
            InetAddress ipC = InetAddress.getByName(sc.nextLine());

            Socket sC = new Socket(ipC, PORT);

            byte[] lenb = new byte[4];
            sC.getInputStream().read(lenb, 0, 4);
            ByteBuffer bb = ByteBuffer.wrap(lenb);
            int len = bb.getInt();
            byte[] servPubKeyBytes = new byte[len];
            sC.getInputStream().read(servPubKeyBytes);
            X509EncodedKeySpec ks = new X509EncodedKeySpec(servPubKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey serverPubKey = kf.generatePublic(ks);
            sC.close();
            System.out.println("Clave Pública Recibida");
            System.out.println("Escriba su mensaje: ");
            String msj = sc.nextLine();

            byte[] msjEncrypt = mEncrypt(serverPubKey, msj);

            //Receptor
            ServerSocket socketConexion = new ServerSocket(5001);
            System.out.println("Esperanding al Receptor...");

            Socket socketDatos = socketConexion.accept();
            System.out.println("Conection Accepted mf");

            socketDatos.getOutputStream().write(msjEncrypt);
            socketDatos.getOutputStream().flush();
            socketDatos.close();
            socketConexion.close();
            System.out.println("Se envió corerectamente el Mensaje Cifrado");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static byte[] mEncrypt(PublicKey pk, String msj) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, pk);

        byte[] secretMessageBytes = msj.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

        System.out.println("Mensaje: " + msj);

        return encryptedMessageBytes;
    }
}
