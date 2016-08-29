/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication._base;

/**
 * Value container class for lazy-initialisation. Not very useful on production.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class ValueReference<T> {
    private T obj;

    public ValueReference(T initialValue) {
        this.obj = initialValue;
    }

    public void set(T value) {
        this.obj = value;
    }

    public T get() {
        return obj;
    }

    /**
     * NOTE: This method tests the equality of wrapped value. Not this class itself.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ValueReference)) {
            return false;
        }

        ValueReference<?> that = (ValueReference) obj;
        if (null == this.obj && null == that.obj) {
            // Means both ValueReference holds null reference
            return true;
        } else if (null == this.obj || null == that.obj) {
            return false;
        }

        return this.obj.equals(that.obj);
    }

    /**
     * NOTE: This method returns the hash code of wrapped value. Not this class itself.
     * @return  a hash code of wrapped value. 0 if wrapped value is <code>null</code>.
     */
    @Override
    public int hashCode() {
        if (null == obj) {
            return 0;
        }

        return obj.hashCode();
    }
}
