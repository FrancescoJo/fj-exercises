/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.util;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Source inspired from <a href="https://dzone.com/articles/java-regex-matching-hashmap">
 * Java regex matching hashmap</a> of <a href="https://dzone.com">dzone.com</a>.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public class RegexHashMap<V> extends HashMap<String, V> {
    /**
     * Map of input to pattern (To save another lookup time & prevent memory leak)
     */
    private final Map<String, String>  ref;
    private final List<PatternMatcher> matchers;

    public RegexHashMap() {
        ref = new WeakHashMap<>();
        matchers = new ArrayList<>();
    }

    /**
     * Returns the value to which the specified key pattern is mapped,
     * or null if this map contains no mapping for the key pattern.
     */
    // Map#get does not supports generic argument
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public V get(Object weakKey) {
        if (!ref.containsKey(weakKey)) {
            for (PatternMatcher matcher : matchers) {
                if (matcher.matched((String) weakKey)) {
                    break;
                }
            }
        }
        if (ref.containsKey(weakKey)) {
            return super.get(ref.get(weakKey));
        }
        return null;
    }

    @Override
    public V put(String key, V value) {
        V v = super.put(key, value);
        if (v == null) {
            matchers.add(new PatternMatcher(key));
        }
        return v;
    }

    @Override
    public V remove(Object key) {
        V v = super.remove(key);
        if (v != null) {
            for (Iterator<PatternMatcher> iter = matchers.iterator(); iter.hasNext(); ) {
                PatternMatcher matcher = iter.next();
                if (matcher.regex.equals(key)) {
                    iter.remove();
                    break;
                }
            }
            for (Iterator<Map.Entry<String, String>> iter = ref.entrySet().iterator(); iter.hasNext(); ) {
                Entry entry = iter.next();
                if (entry.getValue().equals(key)) {
                    iter.remove();
                }
            }
        }
        return v;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        for (Entry<? extends String, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns true if this map contains a mapping
     * for the specified regular expression matched pattern.
     */
    public boolean containsKeyPattern(String key) {
        return ref.containsKey(key);
    }

    @Override
    public void clear() {
        super.clear();
        matchers.clear();
        ref.clear();
    }

    /**
     * Returns a Set view of the regex matched patterns contained in this map.
     * The set is backed by the map, so changes to the map are reflected
     * in the set, and vice-versa.
     */
    public Set<String> keySetPattern() {
        return ref.keySet();
    }

    /**
     * Produces a map of patterns to values, based on the regex put in this map.
     */
    public Map<String, V> transform(List<String> patterns) {
        patterns.forEach(this::get);
        Map<String, V> transformed = new HashMap<>();
        for (Entry<String, String> entry : ref.entrySet()) {
            transformed.put(entry.getKey(), super.get(entry.getValue()));
        }
        return transformed;
    }

    @Override
    public String toString() {
        return "RegexHashMap [ref=" + ref + ", map=" + super.toString() + "]";
    }

    private class PatternMatcher {
        private final String  regex;
        private final Pattern compiled;

        PatternMatcher(String name) {
            regex = name;
            compiled = Pattern.compile(regex);
        }

        boolean matched(String string) {
            if (compiled.matcher(string).matches()) {
                ref.put(string, regex);
                return true;
            }
            return false;
        }
    }
}