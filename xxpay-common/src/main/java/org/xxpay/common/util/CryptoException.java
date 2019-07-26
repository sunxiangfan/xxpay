package org.xxpay.common.util;

/**
 * @author PaoKing
 * @since 1.0
 */
public class CryptoException extends RuntimeException {

    public CryptoException() {
        super();
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
}
