/*
 * This software is distributed under no licenses and warranty.
 * Use this software at your own risk.
 */
package com.github.francescojo.gsondbg;

import com.github.francescojo.test.JavaTestBase;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Apr - 2016
 */
public class GsonDebuggableParseTest extends JavaTestBase {
	private GsonDebuggable gsonWrapper;

	@Before
	public void setUp() throws Exception {
		this.gsonWrapper = GsonDebuggable.getInstance();
	}

	@Test
	public void testNullInput() throws Exception {
		Assert.assertNull(gsonWrapper.fromJson((String) null, TestJsonDeserializerImpl.class));
	}

	@Test
	public void testOptimal() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testOptimal.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		TestOptimal actual = gsonWrapper.fromJson(o, TestOptimal.class);

		Assert.assertEquals("hello, world!", actual.strValue);
		Assert.assertEquals(1, actual.intValue);
		Assert.assertEquals(1.5, actual.doubleValue, 0.0d);
		Assert.assertEquals(false, actual.boolValue);
		Assert.assertNull(actual.obj);
		Assert.assertArrayEquals(new int[] { 1, 2, 3 }, actual.intArray);
	}

	@Test
	public void testJsonDeserializerImpl() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testJsonDeserializerImpl.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		TestJsonDeserializerImpl actual = gsonWrapper.fromJson(o, TestJsonDeserializerImpl.class);

		Assert.assertEquals("hello, world!", actual.strValue);
		Assert.assertEquals(1, actual.intValue);
		Assert.assertEquals(1.5, actual.doubleValue, 0.0d);
		Assert.assertEquals(false, actual.boolValue);
		Assert.assertNull(actual.obj);
	}

	@Test
	public void testUnreadableJson() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testUnreadableJson.json");
		Exception actual = null;
		try {
			gsonWrapper.fromJson(jsonStr, TestJsonDeserializerImpl.class);
		} catch (Exception e) {
			actual = e;
		}
		Assert.assertNotNull(actual);
		Assert.assertTrue(actual instanceof JsonSyntaxException);
		Assert.assertEquals("Gson 내부에서 파싱 실패시 발생시키는 예외", IllegalStateException.class, actual.getCause().getClass());
	}

	@Test
	public void testParseJsonArray() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseJsonArray.json");
		String[] expected = new String[]{
				"a", "b", "c", "d"
		};
		String[] actual = gsonWrapper.fromJson(jsonStr, String[].class);

		Assert.assertArrayEquals(expected, actual);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testParseJsonArrayError() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseJsonArray.json");
		gsonWrapper.fromJson(jsonStr, int[].class);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testParseErrorSimple() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseErrorSimple.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		gsonWrapper.fromJson(o, TestIncompatibleSerializedName.class);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testParseErrorMultiple() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseErrorMultiple.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		gsonWrapper.fromJson(o, TestIncompatibleSerializedName.class);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testParseErrorWithSerializedName() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseErrorWithSerializedName.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		gsonWrapper.fromJson(o, TestIncompatibleSerializedName.class);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testParseJsonComplexValue() throws Exception {
		String jsonStr = getResourceAsString(GsonDebuggableParseTest.class.getPackage(), "GsonDebuggableTest_testParseErrorComplexValue.json");
		JsonObject o = new JsonParser().parse(jsonStr).getAsJsonObject();
		gsonWrapper.fromJson(o, TestIncompatibleComplex.class);
	}

	private static class TestOptimal implements Serializable {
		String strValue;
		int intValue;
		double doubleValue;
		boolean boolValue;
		int[] intArray;
		JsonObject obj;
	}

	@SuppressWarnings("unused")
	public static class TestIncompatible implements Serializable {
		private String strValue;
		private int intValue;
		private double doubleValue;
		private boolean boolValue;
		private JsonObject someObject;
	}

	@SuppressWarnings("unused")
	private static class TestIncompatibleSerializedName implements Serializable {
		@SerializedName("strValue")
		private String str;
		@SerializedName("shortValue")
		private short sh;
		@SerializedName("intValue")
		private int i;
		@SerializedName("longValue")
		private long l;
		@SerializedName("floatValue")
		private float f;
		@SerializedName("doubleValue")
		private double d;
		@SerializedName("boolValue")
		private boolean bool;
		@SerializedName("byteValue")
		private byte b;
		@SerializedName("charValue")
		private char c;
		@SerializedName("someObject")
		private JsonElement jsonObj;
	}

	@SuppressWarnings("unused")
	private static class TestIncompatibleComplex implements Serializable {
		private String strValue;
		private short shortValue;
		private int intValue;
		private long longValue;
		private float floatValue;
		private double doubleValue;
		private boolean boolValue;
		private byte byteValue;
		private char charValue;
		private NestedClass object;

		static class NestedClass {
			public String str;
			public int numInt;
			public int[] intArray;
		}
	}

	static class TestJsonDeserializerImpl implements Serializable, JsonDeserializer<TestJsonDeserializerImpl> {
		String strValue;
		int intValue;
		double doubleValue;
		boolean boolValue;
		JsonObject obj;

		public TestJsonDeserializerImpl deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			TestJsonDeserializerImpl instance = new TestJsonDeserializerImpl();
			JsonObject jsonObj = json.getAsJsonObject();
			instance.strValue = jsonObj.get("strValue").getAsString();
			instance.intValue = jsonObj.get("intValue").getAsInt();
			instance.doubleValue = jsonObj.get("doubleValue").getAsDouble();
			instance.boolValue = jsonObj.get("boolValue").getAsBoolean();
			instance.obj = jsonObj.get("obj").getAsJsonObject();

			return instance;
		}
	}
}
