package org.main.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GmCryptoUtil {

    private static final String BC = BouncyCastleProvider.PROVIDER_NAME;

    static {
        if (Security.getProvider(BC) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private GmCryptoUtil() {
    }

    public static String sm3Hex(String content) {
        byte[] input = content.getBytes(StandardCharsets.UTF_8);
        SM3Digest digest = new SM3Digest();
        digest.update(input, 0, input.length);
        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);
        return toHex(output);
    }

    public static String sm4Encrypt(String plainText, String key) {
        return Base64.getEncoder().encodeToString(doSm4(Cipher.ENCRYPT_MODE, plainText.getBytes(StandardCharsets.UTF_8), key));
    }

    public static String sm4Decrypt(String cipherText, String key) {
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        return new String(doSm4(Cipher.DECRYPT_MODE, decoded, key), StandardCharsets.UTF_8);
    }

    public static Map<String, String> generateSm2KeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BC);
            keyPairGenerator.initialize(new ECGenParameterSpec("sm2p256v1"));
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            Map<String, String> keyMap = new LinkedHashMap<>();
            keyMap.put("publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
            keyMap.put("privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            return keyMap;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2密钥对生成失败", e);
        }
    }

    public static String sm2Encrypt(String plainText, String publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("SM2", BC);
            cipher.init(Cipher.ENCRYPT_MODE, parsePublicKey(publicKey));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2加密失败", e);
        }
    }

    public static String sm2Decrypt(String cipherText, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("SM2", BC);
            cipher.init(Cipher.DECRYPT_MODE, parsePrivateKey(privateKey));
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2解密失败", e);
        }
    }

    public static String sm2Sign(String content, String privateKey) {
        try {
            Signature signature = Signature.getInstance("SM3withSM2", BC);
            signature.initSign(parsePrivateKey(privateKey));
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2签名失败", e);
        }
    }

    public static boolean sm2Verify(String content, String sign, String publicKey) {
        try {
            Signature signature = Signature.getInstance("SM3withSM2", BC);
            signature.initVerify(parsePublicKey(publicKey));
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2验签失败", e);
        }
    }

    private static byte[] doSm4(int mode, byte[] data, String key) {
        try {
            Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS7Padding", BC);
            cipher.init(mode, new SecretKeySpec(normalizeSm4Key(key), "SM4"));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM4处理失败", e);
        }
    }

    private static byte[] normalizeSm4Key(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("SM4密钥不能为空");
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("SM4密钥长度必须为16字节");
        }
        return keyBytes;
    }

    private static PublicKey parsePublicKey(String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", BC);
            return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2公钥解析失败", e);
        }
    }

    private static PrivateKey parsePrivateKey(String privateKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", BC);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("SM2私钥解析失败", e);
        }
    }

    private static String toHex(byte[] data) {
        StringBuilder builder = new StringBuilder(data.length * 2);
        for (byte datum : data) {
            builder.append(String.format("%02x", datum));
        }
        return builder.toString();
    }
}
