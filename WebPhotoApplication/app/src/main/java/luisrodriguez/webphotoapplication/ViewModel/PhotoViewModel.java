package luisrodriguez.webphotoapplication.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import luisrodriguez.webphotoapplication.Model.Photo;
import luisrodriguez.webphotoapplication.Views.PhotoDetailActivity;
import luisrodriguez.webphotoapplication.R;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public class PhotoViewModel extends BaseObservable{

    private Context mContext;
    private Photo mPhoto;

    public PhotoViewModel(Context context){
        this.mContext = context;
    }

    public int getPhotoId(){
        return mPhoto.id;
    }

    public String getPhotoTitle(){
        return mPhoto.title;
    }

    public String getPhotoComment(){
        return mPhoto.comment;
    }

    @Bindable
    public String getPhotoUrl(){
        return mPhoto.photo;
    }

    @BindingAdapter({"android:src"})
    public static void setPhotoUrl(ImageView view, String imageUrl) {

        Glide.with(view.getContext()).load(imageUrl).asBitmap().error(R.drawable.ic_punisher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .placeholder(R.mipmap.ic_launcher).into(view);
    }

    public void setPhotoItem(Photo item) {
        this.mPhoto = item;
        notifyChange();
    }

    public View.OnClickListener onPhotoClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPhotoDetailActivity();
            }
        };
    }

    private void launchPhotoDetailActivity(){
        mContext.startActivity(PhotoDetailActivity.getStartIntent(mContext, mPhoto));
    }
}
