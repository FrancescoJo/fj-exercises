/*
 * This software is distributed under no licenses and warranty.
 * Use this software at your own risk.
 */
package com.francescojo.simplehttpsvr.util;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An utility class which helps tracking cause of ClassCastException <em>at one glance</em> while using Google's gson.
 * <p>
 * Most methods except {@code fromJson} variations are simple composition methods for source code compatibility.
 * </p>
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Apr - 2016
 */
@SuppressWarnings("all")
public class GsonWrapper {
    private static final boolean IS_DEBUG = true;
    private final static Logger LOGGER = Logger.getLogger(GsonWrapper.class.getName());

    private static Callable<GsonWrapper> instanceFactory = null;
    private GsonBuilder gsonBuilder = null;
    /**
     * <a href="https://github.com/google/gson/issues/613">GSON is thread safe</a>
     */
    private Gson gson = new Gson();

    /**
     * Provides an global instance factory to GsonDebuggable. This method is explicitly designed for unit testing.
     * You really don't need to use this in your business logic.
     *
     * @param instanceFactory an GsonDebuggable instance producer.
     */
    protected static void setInstanceFactory(Callable<GsonWrapper> instanceFactory) {
        GsonWrapper.instanceFactory = instanceFactory;
    }

    /**
     * Gets a {@code GsonDebuggable} which is created and cached by VM. Json deserialisation is done by default
     * Gson instance.
     *
     * @return a {@code GsonDebuggable} instance.
     * @see com.google.gson.Gson#Gson()
     */
    public static GsonWrapper getInstance() {
        return getInstanceWith(null);
    }

    /**
     * Gets a {@code GsonDebuggable} which is created and cached by VM. Json deserialisation is done by
     *
     * @param builder your own {@link com.google.gson.GsonBuilder} instance.
     * @return a {@code GsonDebuggable} instance.
     */
    public static GsonWrapper getInstanceWith(GsonBuilder builder) {
        if (null == GsonWrapper.instanceFactory) {
            // No equals method on GsonBuilder :(
            if (null != builder && InstanceHolder.INSTANCE.gsonBuilder != builder) {
                InstanceHolder.INSTANCE.gson = builder.create();
            }

            return InstanceHolder.INSTANCE;
        } else {
            try {
                return instanceFactory.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(String, Class)} for its original documentation.
     */
    public <T> T fromJson(final String jsonString, final Class<T> klass) {
        if (IS_DEBUG) {
            return fromJsonToClassDebug(jsonString, klass, new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return gson.fromJson(jsonString, klass);
                }
            });
        } else {
            return gson.fromJson(jsonString, klass);
        }
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(String, Type)} for its original documentation.
     */
    public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return fromJson(json, (Class<T>) typeOfT.getClass());
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(Reader, Class)} for its original documentation.
     * <p>
     * Parse error inspection on this method is currently disabled.
     * Please read 'Limitations of GsonDebuggable' for details.
     * </p>
     */
    public <T> T fromJson(final Reader json, final Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return gson.fromJson(json, classOfT);
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(Reader, Type)} for its original documentation.
     * <p>
     * Parse error inspection on this method is currently disabled.
     * Please read 'Limitations of GsonDebuggable' for details.
     * </p>
     */
    public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(json, typeOfT);
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(JsonReader, Type)} for its original documentation.
     * <p>
     * Parse error inspection on this method is currently disabled.
     * Please read 'Limitations of GsonDebuggable' for details.
     * </p>
     */
    public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, typeOfT);
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(JsonElement, Class)} for its original documentation.
     */
    public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        if (IS_DEBUG) {
            try {
                return gson.fromJson(json, classOfT);
            } catch (JsonSyntaxException e) {
                return inspectJson(json.toString(), classOfT, e);
            }
        } else {
            return gson.fromJson(json, classOfT);
        }
    }

    /**
     * See {@link com.google.gson.Gson#fromJson(JsonElement, Type)} for its original documentation.
     */
    public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
        return fromJson(json, (Class<T>) typeOfT.getClass());
    }

    private <T> T fromJsonToClassDebug(String jsonString, Class<T> klass, Callable<T> fromJsonJob) {
        try {
            return fromJsonJob.call();
        } catch (JsonSyntaxException e1) {
            return inspectJson(jsonString, klass, e1);
        } catch (Exception ignore) {
            // Unreachable in normal - except any mistakes in <code>fromJsonJob</code>.
            throw new RuntimeException(ignore);
        }
    }

    private <T> T inspectJson(String jsonString, Class<T> klass, RuntimeException e) {
        JsonElement json;
        try {
            json = new JsonParser().parse(jsonString);
        } catch (JsonSyntaxException e2) {
            log("Unable to parse given string as JsonElement. Given string:\n%s", jsonString);
            throw e;
        }

        if (json instanceof JsonArray) {
            log("Error while mapping given JsonString as %s. Given string:\n%s", klass.getCanonicalName(), jsonString);
        } else if (json instanceof JsonObject) {
            JsonObject jsonObj = json.getAsJsonObject();
            inspectJsonObject(jsonObj, klass);
        }

        throw e;
    }

    private void inspectJsonObject(JsonObject jsonObject, Class<?> klass) {
        Map<String, Field> mappingInfo = new HashMap<String, Field>();
        for (Field classField : klass.getDeclaredFields()) {
            Annotation[] annotations = classField.getAnnotations();
            if (null == annotations || 0 == annotations.length) {
                mappingInfo.put(classField.getName(), classField);
            } else {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof SerializedName) {
                        String customName = ((SerializedName) annotation).value();
                        mappingInfo.put(customName, classField);
                        try {
                            String[] alternateNames = ((SerializedName) annotation).alternate();
                            for (String alternateName : alternateNames) {
                                mappingInfo.put(alternateName, classField);
                            }
                        } catch (NoSuchMethodError ignore) {
                        }
                    }
                }
            }
        }

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (!mappingInfo.containsKey(key)) {
                /*
                 * Gson ignores fields that are not found in json by default - following same rule in here.
                 */
                continue;
            }

            Field javaField = mappingInfo.get(key);
            Class<?> javaType = javaField.getType();

            if (WellKnownTypeCastingRules.isWellKnown(javaType)) {
                if (!(value instanceof JsonPrimitive)) {
                    log("%s#%s is declared as ''%s''; however JSON is: { \"%s\": %s }",
                            javaField.getDeclaringClass().getName(), javaField.getName(),
                            javaField.getType().getCanonicalName(), key, value);
                    continue;
                }

                WellKnownTypeCastingRules rule = WellKnownTypeCastingRules.byJavaType(javaType);
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (rule.isAcceptable(primitive)) {
                    continue;
                }

                log("%s#%s is declared as ''%s''; however JSON is: { \"%s\": %s }",
                        javaField.getDeclaringClass().getName(), javaField.getName(),
                        javaField.getType().getCanonicalName(), key, value);
            } else if (javaType.isArray()) {
                try {
                    gson.fromJson(value, javaType);
                } catch (JsonSyntaxException e) {
                    log("%s#%s is declared as ''%s''; however JSON is: { \"%s\": %s }",
                            javaField.getDeclaringClass().getName(), javaField.getName(),
                            javaField.getType().getCanonicalName(), key, value);
                }
            } else if (value.isJsonObject()) {
                inspectJsonObject(value.getAsJsonObject(), javaType);
            }
        }
    }

    private static void log(String msg, Object... args) {
        LOGGER.log(Level.WARNING, String.format(msg, args));
    }

    enum WellKnownTypeCastingRules {
        INT(int.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsInt();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        LONG(long.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsLong();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        SHORT(short.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsShort();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        FLOAT(float.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsFloat();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        DOUBLE(double.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsDouble();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        BYTE(byte.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                try {
                    input.getAsByte();
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }),
        BOOLEAN(boolean.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                return input.isBoolean();
            }
        }),
        CHAR(char.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                /*
                 * There is no Character type in Json-Javascript; however gson will not convert string literals that
                 * are longer than 2 letters as char.
                 */
                return input.isString() && input.getAsString().length() == 1;
            }
        }),
        BOXED_INT(Integer.class, INT.predicate),
        BOXED_LONG(Long.class, LONG.predicate),
        BOXED_SHORT(Short.class, SHORT.predicate),
        BOXED_FLOAT(Float.class, FLOAT.predicate),
        BOXED_DOUBLE(Double.class, DOUBLE.predicate),
        BOXED_BYTE(Byte.class, BYTE.predicate),
        BOXED_BOOLEAN(Boolean.class, BOOLEAN.predicate),
        BOXED_CHAR(Character.class, CHAR.predicate),
        STRING(String.class, new Predicate<JsonPrimitive>() {
            @Override
            public boolean evaluate(JsonPrimitive input) {
                return input.isString();
            }
        });

        final Class<?> type;
        final Predicate<JsonPrimitive> predicate;

        WellKnownTypeCastingRules(Class<?> c, Predicate<JsonPrimitive> p) {
            this.type = c;
            this.predicate = p;
        }

        static WellKnownTypeCastingRules byJavaType(Class<?> klass) {
            for (WellKnownTypeCastingRules r : WellKnownTypeCastingRules.values()) {
                if (r.type == klass) {
                    return r;
                }
            }

            return STRING;
        }

        static boolean isWellKnown(Class<?> klass) {
            for (WellKnownTypeCastingRules r : WellKnownTypeCastingRules.values()) {
                if (r.type == klass) {
                    return true;
                }
            }

            return false;
        }

        boolean isAcceptable(JsonPrimitive value) {
            return predicate.evaluate(value);
        }
    }

    private static class InstanceHolder {
        private static final GsonWrapper INSTANCE = new GsonWrapper();
    }

    /**
     * See {@link com.google.gson.Gson#getAdapter(TypeToken)} for its original documentation.
     */
    public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
        return gson.getAdapter(type);
    }

    /**
     * See {@link com.google.gson.Gson#getDelegateAdapter(TypeAdapterFactory, TypeToken)}
     * for its original documentation.
     */
    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type) {
        return gson.getDelegateAdapter(skipPast, type);
    }

    /**
     * See {@link com.google.gson.Gson#getAdapter(Class)} for its original documentation.
     */
    public <T> TypeAdapter<T> getAdapter(Class<T> type) {
        return gson.getAdapter(type);
    }

    /**
     * See {@link com.google.gson.Gson#toJsonTree(Object)} for its original documentation.
     */
    public JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    /**
     * See {@link com.google.gson.Gson#toJsonTree(Object, Type)} for its original documentation.
     */
    public JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return gson.toJsonTree(src, typeOfSrc);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(Object)} for its original documentation.
     */
    public String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(Object, Type)} for its original documentation.
     */
    public String toJson(Object src, Type typeOfSrc) {
        return gson.toJson(src, typeOfSrc);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(Object, Appendable)} for its original documentation.
     */
    public void toJson(Object src, Appendable writer) throws JsonIOException {
        gson.toJson(src, writer);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(Object, Type, Appendable)} for its original documentation.
     */
    public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(Object, Type, JsonWriter)} for its original documentation.
     */
    public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
        gson.toJson(src, typeOfSrc, writer);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(JsonElement)} for its original documentation.
     */
    public String toJson(JsonElement jsonElement) {
        return gson.toJson(jsonElement);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(JsonElement, Appendable)} for its original documentation.
     */
    public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }

    /**
     * See {@link com.google.gson.Gson#toJson(JsonElement, JsonWriter)} for its original documentation.
     */
    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
        gson.toJson(jsonElement, writer);
    }

    @Override
    public String toString() {
        return gson.toString();
    }

    private interface Predicate<I> {
        boolean evaluate(I input);
    }
}