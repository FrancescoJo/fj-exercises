/*
 * @(#)AndroidHttpClient.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package android.net.http;

/**
 * Compiler hack for avoiding unsatisfied dependency over Android M
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @see <a href="https://github.com/robolectric/robolectric/issues/1862">Robolectric: Need to separate out org.apache.http shadows to compile against M developer preview</a>
 */
@SuppressWarnings("unused")
public class AndroidHttpClient {
}
