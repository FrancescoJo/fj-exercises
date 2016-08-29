/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.api;

import com.example.hwan.myapplication.dto.CounterDto;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public interface ApiService {
    @GET("/")
    Observable<CounterDto> getCounter();
}
