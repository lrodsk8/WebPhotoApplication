package luisrodriguez.webphotoapplication.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.view.View;

import luisrodriguez.webphotoapplication.Model.Photo;

/**
 * Created by Luis.Rodriguez on 3/23/2017.
 */

public class PhotoDetailViewModel extends BaseObservable {

    private Context mContext;
    private Photo mPhoto;

    public PhotoDetailViewModel(Context context, Photo photo){
        this.mContext = context;
        this.mPhoto = photo;
    }

    @Bindable
    public String getPhotoComment(){
        return mPhoto.comment;
    }

    public View.OnClickListener onShareClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        };
    }

    private void shareImage(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        Uri phototUri = Uri.parse(mPhoto.photo);
        sharingIntent.putExtra(Intent.EXTRA_STREAM,phototUri);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
}
