package luisrodriguez.webphotoapplication.Views;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import luisrodriguez.webphotoapplication.Model.Photo;
import luisrodriguez.webphotoapplication.R;
import luisrodriguez.webphotoapplication.ViewModel.PhotoDetailViewModel;
import luisrodriguez.webphotoapplication.WebPhotoUtils.Utils;
import luisrodriguez.webphotoapplication.databinding.PhotoDetailActivityBinding;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    private PhotoDetailActivityBinding mPhotoDetailActivityBinding;
    private Photo mPhoto;
    private PhotoDetailViewModel mPhotoDetailViewModel;
    public static final String EXTRA_PHOTO =
            "luisrodriguez.webphotoapplication.Views.PhotoDetailActivity.EXTRA_PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
           mPhoto = savedInstanceState.getParcelable(EXTRA_PHOTO);
        }
        mPhotoDetailActivityBinding = DataBindingUtil.setContentView(this, R.layout.photo_detail_activity);
        mPhoto = getIntent().getParcelableExtra(EXTRA_PHOTO);
        if (mPhoto == null)
            throw new IllegalArgumentException("PhotoDetailActivity requires a Photo object!");
        mPhotoDetailViewModel = new PhotoDetailViewModel(this, mPhoto);
        mPhotoDetailActivityBinding.setPhotoDetailViewModel(mPhotoDetailViewModel);
    }

    public static Intent getStartIntent(Context context, Photo photo) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(EXTRA_PHOTO, photo);
        return intent;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PHOTO, mPhoto);
        setResult(Utils.PHOTO_DETAILS, intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPhoto != null) {
            outState.putParcelable(EXTRA_PHOTO, mPhoto);
        }
    }

}
