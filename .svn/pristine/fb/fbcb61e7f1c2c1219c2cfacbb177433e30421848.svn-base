package externallib;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

//	Mark last edit: 20150813
public class HeroSecurity {
	final static String ENCRYPT_KEY = "852275016828522750168285227501682";
	
	public static String encryptString(String plainText) throws UnsupportedEncodingException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		int keySize = 256 / 8;
		
		byte[] cryptKey = ENCRYPT_KEY.substring(0, 32).getBytes();
		byte[] iv = generateIV(keySize);
		byte[] plain = plainText.getBytes("utf-8");
		
		byte[] encryptedData = mcryptEncrypt(cryptKey, iv, plain);

		int pos = 0;
		byte[] results = new byte[iv.length + 2 + encryptedData.length];
		System.arraycopy(iv, 0, results, 0, iv.length);
		pos += iv.length;
		results[pos] = '$';
		pos += 1;
		results[pos] = '$';
		pos += 1;
		System.arraycopy(encryptedData, 0, results, pos, encryptedData.length);
		pos += encryptedData.length;
		
		return Hex.toHexString(results);
	}
	
	@Deprecated
	public static String encryptStringFixIV(String plainText) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		byte[] cryptKey = ENCRYPT_KEY.substring(0, 32).getBytes();
		byte[] iv = ENCRYPT_KEY.substring((ENCRYPT_KEY.length() - 32), ENCRYPT_KEY.length()).getBytes();
		byte[] plain = plainText.getBytes();
		
		return Hex.toHexString(mcryptEncrypt(cryptKey, iv, plain));
	}
	
	public static String decryptString(String encryptedText) throws DataLengthException, IllegalStateException, InvalidCipherTextException, UnsupportedEncodingException {
		int ivSize = 256 / 8;
		
		if (encryptedText.length() < ivSize)
			throw new DataLengthException("too short, at lease " + ivSize + " bytes");
		
		byte[] encryptedData = fromHexString(encryptedText);
		byte[] cryptKey = ENCRYPT_KEY.substring(0, 32).getBytes("utf-8");
		byte[] iv = new byte[32];
		
		if (encryptedData.length <= 32 || encryptedData[32] != '$' || encryptedData[33] != '$') {	//	Fix IV decrypt
			iv = ENCRYPT_KEY.substring((ENCRYPT_KEY.length() - 32), ENCRYPT_KEY.length()).getBytes("utf-8");
			return mcryptDecrypt(cryptKey, iv, encryptedData);
		}
		
		//	Variable IV decrypt
		System.arraycopy(encryptedData, 0, iv, 0, ivSize);
		byte[] sEncryptedDataPart = new byte[encryptedData.length - 2 - ivSize];
		System.arraycopy(encryptedData, 2 + ivSize, sEncryptedDataPart, 0, sEncryptedDataPart.length);
		return mcryptDecrypt(cryptKey, iv, sEncryptedDataPart);
	}
	
	private static byte[] fromHexString(String input) {
		int n = input.length() / 2;
		byte[] output = new byte[n];
		int l = 0;
		for (int k = 0; k < n; k++) {
			char c = input.charAt(l++);
			byte b = (byte) ((c >= 'a' ? (c - 'a' + 10) : (c - '0')) << 4);
			c = input.charAt(l++);
			b |= (byte) (c >= 'a' ? (c - 'a' + 10) : (c - '0'));
			output[k] = b;
		}
		return output;
	}
	
	private static byte[] mcryptEncrypt(byte[] cryptKey, byte[] iv, byte[] plain) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		int keySize = 256 / 8;
		
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(cryptKey, 0, keySize), iv, 0, keySize);
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine(256)), new ZeroBytePadding());
		cipher.init(true, ivAndKey);
		
		byte[] encrypted  = new byte[cipher.getOutputSize(plain.length)];
		int oLen = cipher.processBytes(plain, 0, plain.length, encrypted, 0);
		cipher.doFinal(encrypted, oLen);
		
		return encrypted;
	}
	
	private static String mcryptDecrypt(byte[] cryptKey, byte[] iv, byte[] encrypted) throws DataLengthException, IllegalStateException, InvalidCipherTextException, UnsupportedEncodingException {
		int keySize = 256 / 8;
		
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(cryptKey, 0, keySize), iv, 0, keySize);
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new RijndaelEngine(256)), new ZeroBytePadding());
		cipher.init(false, ivAndKey);
		
		byte[] plainAll  = new byte[cipher.getOutputSize(encrypted.length)];
		int offset = cipher.processBytes(encrypted, 0, encrypted.length, plainAll, 0);
		int last = cipher.doFinal(plainAll, offset);
		
		final byte[] plain = new byte[offset + last];
		System.arraycopy(plainAll, 0, plain, 0, offset + last);
		return new String(plain, "utf-8");
	}
	
    private static byte[] generateIV(int size) {
        SecureRandom random = new SecureRandom();
        byte[] randomData = new byte[size];
        random.nextBytes(randomData);
        return randomData;
    }
}
