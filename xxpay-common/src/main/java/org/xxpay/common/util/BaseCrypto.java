package org.xxpay.common.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;


/**
 * @author PaoKing
 * @since 1.0
 */
public abstract class BaseCrypto {

    protected static final String BOUNCYCASTLE = "BC";
    protected static final String SUN_JCE = "SunJCE";

    static {
        Provider bc = Security.getProvider("BC");
        if (bc == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 初始化指定的 {@code Cipher}。
     *
     * @param cipher 要初始化的 {@code Cipher}
     * @param mode   更新模式 (Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE)
     * @param key    密钥。
     * @param spec   算法的相关参数。
     * @throws CryptoException 如果密钥不合法，或者算法参数不合法。
     */
    protected static void initCipher(
            Cipher cipher, @MagicConstant(intValues = {Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE}) int mode,
            Key key, AlgorithmParameterSpec spec) {
        try {
            if (spec != null) {
                cipher.init(mode, key, spec);
            } else {
                cipher.init(mode, key);
            }
        } catch (InvalidKeyException ex) {
            throw new CryptoException("The key [" + Base64.encodeBase64String(key.getEncoded()) + "] is invalid " +
                    "for '" + cipher.getAlgorithm() + "' transformation.", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new CryptoException("The algorithm '" + cipher.getAlgorithm() + "' is invalid or inappropriate parameter.");
        }
    }

    /**
     * 获取一个新的指定{@code transformation} 和默认{@code `BC`} provider 的{@code Cipher} 实例。
     *
     * @param transformation Jca Cipher transformation string.
     * @return a new `Cipher`。
     */
    protected static Cipher newBcCipher(String transformation) {
        return newCipherInstance(transformation, BOUNCYCASTLE);
    }

    /** Default provider(SunJCE) cipher */
    protected static Cipher newCipher(String transformation) {
        return newCipherInstance(transformation, SUN_JCE);
    }

    /**
     * 获取一个新的 `Cipher` 实例。
     *
     * @param transformation Jca Cipher transformation string.
     * @param provider       cipher implementation provider.
     * @return a new `Cipher`。
     * @throws CryptoException the {@code transformation} or {@code provider} is invalid.
     */
    protected static Cipher newCipherInstance(String transformation, String provider) throws CryptoException {
        try {
            return Cipher.getInstance(transformation, provider);
        } catch (Exception ex) {
            // 一般情况下，只要指定的 provider 存在，则不会发生此异常
            // 例如：指定了 "BC" 作为密钥算法提供者，但是运行库里没有 bouncy-castle，则会抛出此异常
            String msg = "Unable to acquire a Java JCA Cipher instance using " +
                    "Cipher.getInstance(\"" + transformation + "\", \"" +
                    provider + "\"). ";
            throw new CryptoException(msg, ex);
        }
    }

    protected static KeyFactory getKeyFactory(String algorithm, @Nullable String provider) {
        try {
            if (provider == null) {
                return KeyFactory.getInstance(algorithm);
            } else {
                return KeyFactory.getInstance(algorithm, provider);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
