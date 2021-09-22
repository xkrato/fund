package com.xkrato.fund.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class DESedeUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DESedeUtil.class);

    private static final String KEY_ALGORITEM = "DESede";
    private static final String CIPHER_ALGORITEM = "DESede/ECB/PKCS5Padding";

    private static Key getKey(String key) throws Exception {
        byte[] keyByte = HexUtil.hex2byte(key);
        DESedeKeySpec dks = new DESedeKeySpec(keyByte);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITEM);
        return skf.generateSecret(dks);
    }

    /**
     * 加密
     *
     * @param key  密钥
     * @param data 业务参数
     * @return 加密后的业务参数
     */
    public static String encrypt(String key, String data) {
        try {
            byte[] encryptByte = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return HexUtil.byte2hex(cipher.doFinal(encryptByte));
        } catch (Exception e) {
            LOGGER.error("# 3DES加密失败", e);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param key  密钥
     * @param data 业务参数
     * @return 解密后业务参数
     */
    public static String decrypt(String key, String data) {
        try {
            byte[] hexByte = HexUtil.hex2byte(data);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            return new String(cipher.doFinal(hexByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("# 3DES解密失败",e);
            return null;
        }
    }

    public static void main(String[] args)  {
        String key = "326a23480c26bc89246f85f3e8012345e782654bd8712509";// 48个长度的字符串即可
        String param = "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";
        String encr = encrypt(key, param);
        String decr = decrypt(key, encr);
        LOGGER.error("key: {}", key);
        LOGGER.error("{} ==>> {}",param, encr);
        LOGGER.error("{} ==>> {}", encr, decr);
    }
}