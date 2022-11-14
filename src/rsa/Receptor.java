package rsa;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Receptor {

    public static void main(String args[]) {

        int PORT = 5000;
        try {

            //Autoridad Certificadora
            Scanner sc = new Scanner(System.in);

            System.out.println("Ingresa la IP de la autoridad certificadora: ");
            InetAddress ipC = InetAddress.getByName(sc.nextLine());

            Socket socketC = new Socket(ipC, PORT);

            byte[] lenb = new byte[4];
            socketC.getInputStream().read(lenb, 0, 4);
            ByteBuffer bb = ByteBuffer.wrap(lenb);
            int len = bb.getInt();
            byte[] serverPrivateKeyBytes = new byte[len];
            socketC.getInputStream().read(serverPrivateKeyBytes);
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(serverPrivateKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey serverPrivateKey = kf.generatePrivate(ks);
            socketC.close();
            System.out.println("Clave Privada Recibida");

            //Emisor
            System.out.println("Ingresa la IP del emisor: ");
            InetAddress ipE = InetAddress.getByName(sc.nextLine());

            Socket sE = new Socket(ipC, 5001);

            byte[] msjByte = new byte[128];
            sE.getInputStream().read(msjByte);

            sE.close();
            System.out.println("Mensaje Cifrado Recibido");

            String msj = mDecrypt(serverPrivateKey, msjByte);

        } catch (IOException ex) {
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
        } catch (InvalidKeySpecException ex) {
            System.out.println(ex);
        } catch (InvalidKeyException ex) {
            System.out.println(ex);
        } catch (NoSuchPaddingException ex) {
            System.out.println(ex);
        } catch (BadPaddingException ex) {
            System.out.println(ex);
        } catch (IllegalBlockSizeException ex) {
            System.out.println(ex);
        }
    }

    public static String mDecrypt(PrivateKey pk, byte[] msj) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(msj);

        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

        System.out.println("Decifrado: " + decryptedMessage);

        return decryptedMessage;
    }
}
