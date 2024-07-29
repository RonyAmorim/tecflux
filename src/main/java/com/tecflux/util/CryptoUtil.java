package com.tecflux.util;

import org.jasypt.util.text.StrongTextEncryptor;

public class CryptoUtil {

        private static final StrongTextEncryptor encryptor;

        static{
            encryptor = new StrongTextEncryptor();
            encryptor.setPassword(System.getenv("CRYPTO_PASSWORD"));
        }

        private CryptoUtil() {
            // Construtor privado para impedir instâncias
        }

        public static String encrypt(String rawText){
            return encryptor.encrypt(rawText);
        }

        public static String decrypt(String encryptedText){
            return encryptor.decrypt(encryptedText);
        }

}
