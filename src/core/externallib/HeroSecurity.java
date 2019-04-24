package core.externallib;

import java.io.UnsupportedEncodingException;

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

public class HeroSecurity {
	
	private static String ENCRYPT_KEY = "852275016828522750168285227501682";
	
	public static byte[] fromHexString(String input) {
		int n = input.length()/2;
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
	
	public static String encryptString(String sPlainText){
		byte[] sessionKey = ENCRYPT_KEY.substring(0, 32).getBytes();
		byte[] iv = ENCRYPT_KEY.substring((ENCRYPT_KEY.length()-32), ENCRYPT_KEY.length()).getBytes();
		byte[] plaintext = sPlainText.getBytes();
	
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
		    new CBCBlockCipher(new RijndaelEngine(256)), new ZeroBytePadding());
	
		int keySize = 256 / 8;
	
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(sessionKey, 0, keySize), iv, 0, keySize);
	
		cipher.init(true, ivAndKey);
		byte[] encrypted  = new byte[cipher.getOutputSize(plaintext.length)];
		int oLen = cipher.processBytes(plaintext, 0, plaintext.length, encrypted, 0);
		try {
			cipher.doFinal(encrypted, oLen);
		} catch (DataLengthException e) {
			return "";
		} catch (IllegalStateException e) {
			return "";
		} catch (InvalidCipherTextException e) {
			return "";
		}
		
		return Hex.toHexString(encrypted);
	}
	
	public static String decryptString(String sEncryptedText){
		byte[] sessionKey = ENCRYPT_KEY.substring(0, 32).getBytes();
		byte[] iv = ENCRYPT_KEY.substring((ENCRYPT_KEY.length()-32), ENCRYPT_KEY.length()).getBytes();
		byte[] encrypted = fromHexString(sEncryptedText);
		
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
		    new CBCBlockCipher(new RijndaelEngine(256)), new ZeroBytePadding());
	
		int keySize = 256 / 8;
	
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(sessionKey, 0, keySize), iv, 0, keySize);
	
		cipher.init(false, ivAndKey);
		byte[] decrypttext  = new byte[cipher.getOutputSize(encrypted.length)];
		int offset = cipher.processBytes(encrypted, 0, encrypted.length, decrypttext, 0);
		int last;
		try {
			last = cipher.doFinal(decrypttext, offset);
		} catch (DataLengthException e) {
			return "";
		} catch (IllegalStateException e) {
			return "";
		} catch (InvalidCipherTextException e) {
			return "";
		}
		
		final byte[] plaintext = new byte[offset + last];
		System.arraycopy(decrypttext, 0, plaintext, 0, plaintext.length);
		
		try {
			return new String(plaintext, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
