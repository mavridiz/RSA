package Proyect;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CifrarEmisor {
    
    public byte[] secretAddendum( String addendum, SecretKey Key ) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException{
               
        Cipher cipher = Cipher.getInstance("AES");
        byte[] addBytes = addendum.getBytes("UTF8");
            
        cipher.init(Cipher.ENCRYPT_MODE, Key);
        byte[] cipherBytes = cipher.doFinal(addBytes);
            
        return cipherBytes;    
    }
    
    public byte[] secretPublic( SecretKey Key, PublicKey publicKey ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        
        byte[] key = Key.getEncoded();
        
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedSecretKey = encryptCipher.doFinal(key);
        return encryptedSecretKey;
        
    }
}