package ru.maltsev.langhunt.network.service;

import static ru.maltsev.langhunt.network.client.ApiUtilities.API_KEY;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ru.maltsev.langhunt.network.model.images.ImageModel;
import ru.maltsev.langhunt.network.model.images.SearchModel;

public interface ApiInterfaceImages {

    @Headers("Authorization: Client-ID "+ API_KEY)
    @GET("/photos")
    Call<List<ImageModel>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @Headers("Authorization: Client-ID "+ API_KEY)
    @GET("/search/photos")
    Call<SearchModel> searchImage(
            @Query("query") String query
    );
}