package Proyect;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decifrar {

    public String secretAddendumDecypher(SecretKey sKey, byte[] msjAddSec) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv =generateIv();
        Cipher decryptionCipher = Cipher.getInstance("AES");
        decryptionCipher.init(Cipher.DECRYPT_MODE, sKey, iv);
        byte[] decryptedBytes = decryptionCipher.doFinal(msjAddSec);
        
        return new String(decryptedBytes);
    }

    public SecretKey secretPublicDecypher(PrivateKey pk, byte[] msjSecPub) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(msjSecPub);
        SecretKey sKey = new SecretKeySpec(decryptedMessageBytes, 0, decryptedMessageBytes.length, "AES");
        
        return sKey;
    }

    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}

