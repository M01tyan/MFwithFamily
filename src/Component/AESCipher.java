package Component;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
	public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

	public IvParameterSpec generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

	public byte[] encrypto(String plainText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {

        // 書式:"アルゴリズム/ブロックモード/パディング方式"
        Cipher encrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypter.init(Cipher.ENCRYPT_MODE, key, iv);

        return encrypter.doFinal(plainText.getBytes());
    }

	public String decrypto(byte[] cryptoText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
        Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decrypter.init(Cipher.DECRYPT_MODE, key, iv);

        return new String(decrypter.doFinal(cryptoText));
    }

	public SecretKey decodeSecretKey(String encodeKey) {
		byte[] decodedKey = Base64.getDecoder().decode(encodeKey);
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		return originalKey;
	}

	public IvParameterSpec decodeIvParameter(String iv) {
		byte[] decodedIv = Base64.getDecoder().decode(iv);
		return new IvParameterSpec(decodedIv);
	}
}
