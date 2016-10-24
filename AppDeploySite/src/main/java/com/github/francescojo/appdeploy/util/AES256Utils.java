/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.util;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AES256Utils {
	public enum KeyLengthSpec {
		AES_128(128), AES_192(192), AES_256(256);

		final int keyLength;

		KeyLengthSpec(int i) {
			this.keyLength = i;
		}
	}

	private static final Charset UTF8 = Charset.forName("UTF-8");
	/**
	 * CBC Initialization Vector(same as cipher block size)
	 */
	private static final int AES_NIVBITS = 128;

	public static String encode(String key, String plainText) throws GeneralSecurityException {
		/*
		 * Generate 128 bits of random data for use as the IV. It is important
		 * to use a different IV for each encrypted block of text, to ensure
		 * that the same string encrypted by two different people does not give
		 * the same encrypted text string - that leads to obvious attack
		 * possibilities.
		 * 
		 * Note however that the IV does not need to be kept secret; it is a
		 * little bit like a 'salt' for a password, which improves security even
		 * when the salt is stored in plaintext in a database or prefixed to the
		 * encrypted file.
		 */
		byte[] ivData = new byte[AES_NIVBITS / 8];
		Random r = new SecureRandom();
		r.nextBytes(ivData);

		// Select encryption algorithm and padding : AES with CBC and PKCS#7
		BlockCipherPadding padding = new PKCS7Padding();
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), padding);

		// Encrypt the input string using key + iv
		KeyParameter keyParam = wrapAesKey(key);
		CipherParameters params = new ParametersWithIV(keyParam, ivData);

		cipher.reset();
		// first param = encode/decode
		cipher.init(true, params);

		byte[] bytesDec = plainText.getBytes(UTF8);
		byte[] bytesEnc;
		try {
			int buflen = cipher.getOutputSize(bytesDec.length);
			bytesEnc = new byte[buflen];
			int nBytesEnc = cipher.processBytes(bytesDec, 0, bytesDec.length, bytesEnc, 0);
			nBytesEnc += cipher.doFinal(bytesEnc, nBytesEnc);

			if (nBytesEnc != bytesEnc.length) {
				throw new IllegalStateException("Unexpected behaviour : getOutputSize value incorrect");
			}
		} catch (InvalidCipherTextException e) {
			throw new GeneralSecurityException("encryption failed");
		} catch (RuntimeException e) {
			throw new GeneralSecurityException("encryption failed");
		}

		// Return a base-64-encoded string containing IV + encrypted input
		// string
		byte[] bytesAll = new byte[ivData.length + bytesEnc.length];
		System.arraycopy(ivData, 0, bytesAll, 0, ivData.length);
		System.arraycopy(bytesEnc, 0, bytesAll, ivData.length, bytesEnc.length);
		return DatatypeConverter.printHexBinary(bytesAll);
	}

	/**
	 * Decode a string which has first been encrypted with AES.
	 */
	public static String decode(String key, String encryptedText) throws GeneralSecurityException {
		byte[] bytesEnc = DatatypeConverter.parseHexBinary(encryptedText);

		// Extract the IV, which is stored in the next N bytes
		int nIvBytes = AES_NIVBITS / 8;
		byte[] ivBytes = new byte[nIvBytes];
		System.arraycopy(bytesEnc, 0, ivBytes, 0, nIvBytes);

		/*
		 * Select encryption algorithm and padding : AES with CBC and PKCS#7.
		 * Note that the "encryption strength" (128 or 256 bit key) is set by
		 * the KeyParameter object.
		 */
		KeyParameter keyParam = wrapAesKey(key);
		CipherParameters params = new ParametersWithIV(keyParam, ivBytes);
		BlockCipherPadding padding = new PKCS7Padding();
		BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), padding);

		// Decrypt all bytes that follow the IV
		cipher.reset();
		// first param = encode/decode
		cipher.init(false, params);

		byte[] bytesDec;

		try {
			int buflen = cipher.getOutputSize(bytesEnc.length - nIvBytes);
			byte[] workingBuffer = new byte[buflen];
			int len = cipher.processBytes(bytesEnc, nIvBytes, bytesEnc.length - nIvBytes, workingBuffer, 0);
			len += cipher.doFinal(workingBuffer, len);

			/*
			 * Note that getOutputSize returns a number which includes space for
			 * "padding" bytes to be stored in.
			 * 
			 * However we don't want these padding bytes; the "len" variable
			 * contains the length of the *real* data - which is always less
			 * than the return value of getOutputSize.
			 */
			bytesDec = new byte[len];
			System.arraycopy(workingBuffer, 0, bytesDec, 0, len);
		} catch (InvalidCipherTextException e) {
			throw new GeneralSecurityException("decode failed");
		} catch (RuntimeException e) {
			throw new GeneralSecurityException("encryption failed");
		}

		// And convert the result to a string
		return new String(bytesDec, UTF8);
	}

	public static String generateKey(KeyLengthSpec keySpec, String oldKey) {
		int keyLength = keySpec.keyLength / 8;
		byte[] oldKeyBytes = oldKey.getBytes(UTF8);
		int paddingSize = keyLength - oldKeyBytes.length;
		if (paddingSize < 0) {
			throw new IllegalArgumentException("Given key " + oldKey + " exceeds keyspec length " + keyLength);
		}
		
		byte[] paddings = new byte[paddingSize];
		Random r = new SecureRandom();
		r.nextBytes(paddings);
		
		byte[] newKeyBytes = new byte[keyLength];
		System.arraycopy(oldKeyBytes, 0, newKeyBytes, 0, oldKeyBytes.length);
		System.arraycopy(paddings, 0, newKeyBytes, oldKeyBytes.length, paddings.length);

		return DatatypeConverter.printHexBinary(newKeyBytes);
	}

	private static KeyParameter wrapAesKey(String key) throws GeneralSecurityException {
		byte[] rawKeyData = DatatypeConverter.parseHexBinary(key);
		// Wrap the key data in an appropriate holder type
		return new KeyParameter(rawKeyData);
	}
}
