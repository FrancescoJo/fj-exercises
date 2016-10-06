/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
// All private fields without set method, are written by Retrofit2
@SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
@SuppressWarnings("unused")
public class CounterDto {
    private int counter;

    private String lastAccessed;

    public int getCounter() {
        return counter;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }
}
