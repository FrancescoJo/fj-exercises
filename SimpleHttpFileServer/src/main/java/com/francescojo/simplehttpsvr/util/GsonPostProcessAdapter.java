/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public class GsonPostProcessAdapter implements TypeAdapterFactory {
    public interface PostProcessHook {
        void onPostProcessGson();
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {
                T obj = delegate.read(in);
                if (obj instanceof PostProcessHook) {
                    ((PostProcessHook) obj).onPostProcessGson();
                }
                return obj;
            }
        };
    }
}
