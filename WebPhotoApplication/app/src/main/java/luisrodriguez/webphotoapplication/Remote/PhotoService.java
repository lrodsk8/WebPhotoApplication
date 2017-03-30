package luisrodriguez.webphotoapplication.Remote;

import java.util.List;

import luisrodriguez.webphotoapplication.Model.Photo;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public interface PhotoService {

    String ENDPOINT = "https://photomaton.herokuapp.com/api/photo/";

    @GET(ENDPOINT)
    Observable <List<Photo>> getAllPhotos();
}
