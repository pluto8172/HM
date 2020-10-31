

package rxtool;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class test {
    private static final String DESede = "DESede";
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static final String KEY_STRING = "@http://www.dquick.com#!";
    private static test INSTANCE = new test();

    private test() {
        try {
            byte[] keyAsBytes = KEY_STRING.getBytes(StandardCharsets.UTF_8);
            keySpec = new DESedeKeySpec(keyAsBytes);
            keyFactory = SecretKeyFactory.getInstance(DESede);
            cipher = Cipher.getInstance(DESede);// DESede/ECB/PKCS5Padding
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static test getStringEncrypter() {
        return INSTANCE;
    }


    public String decrypt(String encryptedString) {
        try {

            SecretKey key = keyFactory.generateSecret(keySpec);
            //RxLogTool.i("StringEncrypter", "key = " + key);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = Base64.decode(encryptedString);
            byte[] ciphertext = cipher.doFinal(cleartext);
            return bytes2String(ciphertext, 2);
        } catch (Exception e) {
            //throw new Exception(e);
            e.printStackTrace();
            return encryptedString;
        }
    }

    public String encrypt(String unencryptedString) throws Exception {
        if (unencryptedString == null || unencryptedString.trim().length() == 0)
            throw new IllegalArgumentException("unencrypted string was null or empty");

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(cleartext);
            return Base64.encodeBytes(ciphertext);
            // BASE64Encoder base64encoder = new BASE64Encoder();
            // return base64encoder.encode( ciphertext );
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private String bytes2String(byte[] bytes, int type) {
        if (type == 1) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                stringBuffer.append((char) bytes[i]);
            }
            return stringBuffer.toString();
        } else if (type == 2) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

}
