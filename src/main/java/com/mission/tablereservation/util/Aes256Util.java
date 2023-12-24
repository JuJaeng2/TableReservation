package com.mission.tablereservation.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Aes256Util {

    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String KEY = "ZEROBASEKEYISZEROBASEKEY";

    private static final String IV = KEY.substring(0, 16);

    public static String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(alg);

            SecretKey keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            String testEncode = Base64.encodeBase64String(encrypted);
            System.out.println("'" + text + "' Encoding => " + testEncode);
            return testEncode;
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKey keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decodedBytes = Base64.decodeBase64(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            String test = new String(decrypted, StandardCharsets.UTF_8);
            System.out.println("디코딩 된 값 : " + test);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}

