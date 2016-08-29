/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.dto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class CounterDto {
    // This field is used by Retrofit2
    @SuppressWarnings("unused")
    private int counter;

    // This field is used by Retrofit2
    @SuppressWarnings("unused")
    private String lastAccessed;

    public int getCounter() {
        return counter;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }
}
