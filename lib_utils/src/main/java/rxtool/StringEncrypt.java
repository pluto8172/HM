package rxtool;


import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 数据加密类
 */
public  final class StringEncrypt {

    private static final String KEY_STRING = "@http://www.dquick.com#!";
    public static final String DESede = "DESede";

    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private SecretKey key;

    public volatile static StringEncrypt instance;

    public static StringEncrypt getInstance() {
        if (instance == null) {
            synchronized (StringEncrypt.class) {
                if (instance == null) {
                    instance = new StringEncrypt(KEY_STRING);
                }
            }
        }
        return instance;
    }


    public StringEncrypt(String encryptKey) {
        try {
            byte[] keyAsBytes = encryptKey.getBytes("UTF8");
            keySpec = new DESedeKeySpec(keyAsBytes);
            keyFactory = SecretKeyFactory.getInstance(DESede);
            key = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String unencryptedString) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(DESede);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = unencryptedString.getBytes("UTF8");
            byte[] encryptBytes = cipher.doFinal(bytes);
            return Base64.encodeBytes(encryptBytes);

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public String decrypt(String encryptedString) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(DESede);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = Base64.decode(encryptedString);
            byte[] decryptBytes = cipher.doFinal(bytes);
            return new String(decryptBytes, "utf-8");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
