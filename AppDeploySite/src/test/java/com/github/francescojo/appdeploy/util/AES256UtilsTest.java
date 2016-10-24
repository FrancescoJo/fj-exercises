package com.github.francescojo.appdeploy.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.francescojo.appdeploy.util.AES256Utils.KeyLengthSpec;

public class AES256UtilsTest {
	@Test
	public void testAES256() throws Exception {
		String plainText = "ABCDEFG";
		String key = AES256Utils.generateKey(KeyLengthSpec.AES_256, "myKey");

		String encoded = AES256Utils.encode(key, plainText);
		String decoded = AES256Utils.decode(key, encoded);

		assertEquals(plainText, decoded);
	}
}
