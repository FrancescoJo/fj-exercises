/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.api;

import com.fj.android.template.dto.CounterDto;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public interface ApiService {
    @GET("/")
    Observable<CounterDto> getCounter();
}
