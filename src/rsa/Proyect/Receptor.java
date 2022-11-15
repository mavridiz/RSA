package Proyect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Receptor {

    public static void main(String args[]) {

        int PORT = 5000;
        try {
            Encoder e = new Encoder();
            Scanner sc = new Scanner(System.in);
            Comunication comunicator = new Comunication();
            Decifrar decifrador = new Decifrar();
            
            
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

            
            //Addendum  SecretKey
            Socket socketSecAdd = new Socket(ipE, PORT);

            byte[] msjSecAddByte = new byte[128];
            socketSecAdd.getInputStream().read(msjSecAddByte);

            socketSecAdd.close();
            
            //SecretKey Public
            Socket socketPubSec = new Socket(ipE, PORT);

            byte[] msjPubSecByte = new byte[128];
            socketPubSec.getInputStream().read(msjPubSecByte);

            socketPubSec.close();
            
            //SecretKey y Addendum
            
            SecretKey sKey = decifrador.secretPublicDecypher(PrivateKeyReceptor, msjPubSecByte);
            
            String addendum = decifrador.secretAddendumDecypher(sKey, msjSecAddByte);

        } 
        catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException ex) {
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