package kr.co.dcon.taskserver.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptUtil {

    public static String alg = "AES/CBC/PKCS5Padding";
//    public static String iv = encryptKey.substring(0, 16); // 16byte

    public static String characterSetName = "UTF-8";

    public static String encrypt(String text, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        String iv = encryptKey.substring(0, 16);
        SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherText, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        String iv = encryptKey.substring(0, 16);
        SecretKeySpec keySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, characterSetName);
    }

}
