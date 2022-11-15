package Proyect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
            Encoder e = new Encoder();
            Scanner sc = new Scanner(System.in);
            Comunication comunicator = new Comunication();
            
            //Autoridad Certificadora

            System.out.println("Ingresa la IP de la autoridad certificadora: ");
            InetAddress ipC = InetAddress.getByName(sc.nextLine());

            //Clave Publica Emisor
            
            Socket socketPublicKeyEmisor = new Socket(ipC, PORT);
            PublicKey PublicKeyEmisor = e.publicKey(socketPublicKeyEmisor);
            
            //Clave Privada Receptor
            
            Socket socketPrivateKeyReceptor = new Socket(ipC, PORT);
            PrivateKey PrivateKeyReceptor = e.privateKey(socketPrivateKeyReceptor);
            System.out.println("Claves Recibidas");

            //Emisor
            System.out.println("Ingresa la IP del emisor: ");
            InetAddress ipE = InetAddress.getByName(sc.nextLine());

            Socket socketMessage = new Socket(ipE, PORT);

            byte[] msjByte = new byte[128];
            socketMessage.getInputStream().read(msjByte);

            socketMessage.close();
            System.out.println("Mensaje Cifrado Recibido");

            String msj = mDecrypt(serverPrivateKey, msjByte);

        } catch (IOException ex) {
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
        } catch (InvalidKeySpecException ex) {
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