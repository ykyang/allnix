package org.allnix.simple.ch1;

import java.util.Random;

/**
 * 
 * <pre>
 * A ^ B = C
 * C ^ B = A
 * C ^ A = B
 * </pre> 
 * 
 * @author ykyang@gmail.com
 *
 */
public class UnbreakableEncryption {

	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch1.UnbreakableEncryption runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch1.UnbreakableEncryption runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		KeyPair kp = encrypt("One Time Pad!");
		String result = decrypt(kp);
		
		System.out.println(result);
	}
	
	static public String decrypt(KeyPair kp) {
		int length = kp.key1.length;
		byte[] decrypted = new byte[length];
		
		for (int i = 0; i < length; i++) {
			decrypted[i] = (byte) (kp.key1[i] ^ kp.key2[i]);
		}
		
		return new String(decrypted);
	}
	
	static public KeyPair encrypt(String originalString) {
		byte[] originalBytes = originalString.getBytes();
		int length = originalBytes.length;
		byte[] dummyBytes = randomKey(length);
		byte[] encryptedBytes = new byte[length];
		
		for (int i = 0; i < length; i++) {
			encryptedBytes[i] = (byte) (originalBytes[i] ^ dummyBytes[i]);
		}
		
		return new KeyPair(dummyBytes, encryptedBytes);
	}
	
	/**
	 * Generate a random key of given length.
	 * 
	 * @param length
	 * @return Random key
	 */
	static private byte[] randomKey(int length) {
		byte[] dummy = new byte[length];
		
		Random random = new Random();
		random.nextBytes(dummy);
				
		return dummy;
	}
}
