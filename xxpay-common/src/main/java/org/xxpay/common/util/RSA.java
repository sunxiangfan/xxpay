package org.xxpay.common.util;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * RSA 算法实现。
 *
 * 1. transformation: RSA/ECB/PKCS1Padding
 * 2. key size: 1024
 * 3. signature: SHA1withRSA
 *
 * @author PaoKing
 * @since 1.0
 */
public class RSA extends BaseCrypto {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSA.class);
    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    //private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public static PrivateKey loadPrivateKey(InputStream input) {
        PEMParser parser = null;
        try {
            parser = new PEMParser(new InputStreamReader(input, Charset.defaultCharset()));
            Object object = parser.readObject();

            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            return converter.getKeyPair((PEMKeyPair) object).getPrivate();
        } catch (IOException ex) {
            throw new CryptoException(ex);
        } finally {
            try { if (parser != null) parser.close(); }
            catch (IOException ignored) {}
        }
    }

    public static PublicKey loadPublicKey(InputStream input) {
        PEMParser parser = null;
        try {
            parser = new PEMParser(new InputStreamReader(input, Charset.defaultCharset()));
            SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) parser.readObject();
            return new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);
        } catch (IOException ex) {
            throw new CryptoException(ex);
        } finally {
            try { if (parser != null) parser.close(); }
            catch (IOException ignored) {}
        }
    }

    public static PublicKey toPublicKey(String keyText) {
        KeyFactory kf = getKeyFactory(ALGORITHM, BOUNCYCASTLE);
        byte[] key = Base64.decodeBase64(keyText);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        try {
            return kf.generatePublic(keySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("Invalid public key: {}", ex.getLocalizedMessage() == null ?
                    ex.getMessage() : ex.getLocalizedMessage());
            throw new CryptoException("The public key is invalid: \n" + Base64.encodeBase64String(key));
        }
    }

    public static PrivateKey toPrivateKey(String keyText) {
        KeyFactory kf = getKeyFactory(ALGORITHM, BOUNCYCASTLE);
        byte[] key = Base64.decodeBase64(keyText);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        try {
            return kf.generatePrivate(keySpec);
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("Invalid private key spec: {}", ex.getLocalizedMessage() == null ?
                    ex.getMessage() : ex.getLocalizedMessage());
            throw new CryptoException("The private key is invalid : \n" + Base64.encodeBase64String(key));
        }
    }

    public static KeyPair genKeyPair() {
        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance(ALGORITHM);

            kpGen.initialize(1024);
            return kpGen.genKeyPair();
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }

    public static byte[] sign(byte[] content, PrivateKey privateKey) {
        checkArgument(content != null, "The content must be not null");
        checkArgument(privateKey != null, "The privateKey must be not null");
        try {
            Signature signature = newSignatureInstance();

            signature.initSign(privateKey);
            signature.initSign(privateKey);
            signature.update(content);

            return signature.sign();
        } catch (InvalidKeyException ex) {
            throw new CryptoException("Invalid " + ALGORITHM + " private key(base64): \n" +
                    Base64.encodeBase64String(privateKey.getEncoded()), ex);
        } catch (SignatureException ex) {
            throw new CryptoException("Signature sign error: " +
                    (ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ex.getMessage()), ex);
        }
    }

    public static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) {
        if (content == null || sign == null || publicKey == null) return false;
        Signature signature = newSignatureInstance();
        try {
            signature.initVerify(publicKey);
            signature.update(content);
            return signature.verify(sign);
        } catch (InvalidKeyException ex) {
            throw new CryptoException("Invalid " + ALGORITHM + " public key(base64): \n" +
                    Base64.encodeBase64String(publicKey.getEncoded()), ex);
        } catch (SignatureException ex) {
            throw new CryptoException("Signature verify error: " +
                    (ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : ex.getMessage()), ex);
        }
    }

    /**
     * 使用私钥对原始内容字符串进行签名。
     *
     * @param content 待签名的内容串。
     * @param privateKey 私钥
     * @param charset 字符集(默认 UTF-8)。
     * @return 返回签名数据（Base64格式）
     */
    public static String signBase64(@NotNull String content, @NotNull PrivateKey privateKey, @Nullable Charset charset) {
        Charset c = charset == null ? Charsets.UTF_8 : charset;
        byte[] contentBytes = content.getBytes(c);
        byte[] signData = sign(contentBytes, privateKey);
        return Base64.encodeBase64URLSafeString(signData);
    }

    /**
     * 使用私钥对原始内容字符串进行签名。
     *
     * @param content 待签名的内容串。
     * @param privateKey 私钥
     * @return 返回签名数据（Base64格式）
     */
    public static String signBase64(@NotNull String content, @NotNull PrivateKey privateKey) {
        return signBase64(content, privateKey, Charsets.UTF_8);
    }

    /**
     * 校验字符串数据签名。
     *
     * @param content 要验证的源内容串。
     * @param signText 要验证的签名（Base64串）
     * @param publicKey 公钥。
     * @param charset 字符集。
     * @return 返回{@code true}，如果签名验证通过，否则{@code false}。
     */
    public static boolean verify(
            @NotNull String content, @NotNull String signText,
            @NotNull PublicKey publicKey, @Nullable Charset charset) {
        Charset c = charset == null ? Charsets.UTF_8 : charset;
        byte[] contentBytes = content.getBytes(c);
        byte[] targetSign = Base64.decodeBase64(signText);
        return verify(contentBytes, targetSign, publicKey);
    }

    public static boolean verify(
            @NotNull String content, @NotNull String signText,
            @NotNull PublicKey publicKey) {
        return verify(content, signText, publicKey, Charsets.UTF_8);
    }

    private static Signature newSignatureInstance() throws IllegalArgumentException {
        try {
            return Signature.getInstance(SIGNATURE_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("No such Algorithm('" + SIGNATURE_ALGORITHM +
                    "') on this platform.", ex);
        }
    }
}
