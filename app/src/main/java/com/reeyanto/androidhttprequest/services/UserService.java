package com.reeyanto.androidhttprequest.services;

import com.reeyanto.androidhttprequest.models.ResultUsers;
import com.reeyanto.androidhttprequest.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("users")
    Call<ResultUsers> getListUsers(@Query("page") int currentPage);

    @POST("users")
    Call<User> insertUsert(@Body User user);

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") int userId,
                          @Field("first_name") String firstName,
                          @Field("last_name") String lastName,
                          @Field("email") String email);

    @DELETE("users/{id}")
    Call<User> deleteUser(@Path("id") int userId);
}
