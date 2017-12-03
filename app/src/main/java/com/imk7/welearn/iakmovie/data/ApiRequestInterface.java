package com.imk7.welearn.iakmovie.data;

import com.imk7.welearn.iakmovie.data.dao.MovieResponseDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by septiandrd on 01/12/17.
 */

public interface ApiRequestInterface {

    @GET("movie/popular")
    Call<MovieResponseDao> getMovieList(@Query("api_key") String api_key);

}
