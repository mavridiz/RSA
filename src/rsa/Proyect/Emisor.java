package Proyect;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Emisor {

    public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        int PORT = 5000;

        try {
            Encoder e = new Encoder();
            Scanner sc = new Scanner(System.in);
            Addendum add = new Addendum();
            sKey secretKeyEmisor = new sKey();
            CifrarEmisor cifrador = new CifrarEmisor();
            Comunication comunicator = new Comunication();
            
            //Autoridad
            
            System.out.println("Ingresa la IP de la autoridad certificadora: ");
            InetAddress ipC = InetAddress.getByName(sc.nextLine());

            //Clave Publica Receptor
            
            Socket socketPublicKeyReceptor = new Socket(ipC, PORT);
            PublicKey PublicKeyReceptor = e.publicKey(socketPublicKeyReceptor);
            
            //Clave Privada Emisor
            
            Socket socketPrivateKeyEmisor = new Socket(ipC, PORT);
            PrivateKey PrivateKeyEmisor = e.privateKey(socketPrivateKeyEmisor);
            
            System.out.println("Claves Recibidas");
            
            System.out.println("Escriba su mensaje: ");
            String msj = sc.nextLine();
            
            //Creación y encriptacion de clave y addendum
            
            String addendum = add.Addendum(msj);
            SecretKey secKey = secretKeyEmisor.generateKey();
            
            byte[] secAddEncrypted = cifrador.secretAddendum(addendum, secKey);
            byte[] pubSecEncrypted = cifrador.secretPublic(secKey, PublicKeyReceptor);
            
            //Receptor SecretKey encrypted Addendum
            
            comunicator.sendBytes(PORT, secAddEncrypted);

            //Receptor PublicKey encrypted SecretKey
            
            comunicator.sendBytes(PORT, pubSecEncrypted);
            
            System.out.println("Mensajes enviados");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
