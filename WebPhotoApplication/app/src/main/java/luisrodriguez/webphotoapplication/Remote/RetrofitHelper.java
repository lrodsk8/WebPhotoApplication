package luisrodriguez.webphotoapplication.Remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public class RetrofitHelper {

    public static PhotoService getPhotos(){
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(PhotoService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return restAdapter.create(PhotoService.class);
    };
}
