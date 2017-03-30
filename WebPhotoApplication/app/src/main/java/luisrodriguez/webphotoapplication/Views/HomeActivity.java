package luisrodriguez.webphotoapplication.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import luisrodriguez.webphotoapplication.Model.Photo;
import luisrodriguez.webphotoapplication.R;
import luisrodriguez.webphotoapplication.Remote.PhotoService;
import luisrodriguez.webphotoapplication.Remote.RetrofitHelper;
import luisrodriguez.webphotoapplication.ViewModel.PhotoViewModel;
import luisrodriguez.webphotoapplication.WebPhotoUtils.Utils;
import luisrodriguez.webphotoapplication.databinding.ActivityHomeBinding;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Luis.Rodriguez on 3/21/2017.
 */

public class HomeActivity extends AppCompatActivity {

    private final String TAG =  HomeActivity.class.getSimpleName();
    private PhotoService mPhotoService;
    private Subscription mSubscriber;
    List<Photo> mPhotoResults = new ArrayList<>();
    private ActivityHomeBinding mHomeActivityBinding;
    private PhotoViewModel mPhotoViewModel;
    private RecyclerView mRecyclerView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final Gson gson = new GsonBuilder().create();
    List<Photo> mTempPhotoResults = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefresher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        if (savedInstanceState != null) {
            String jsonString = savedInstanceState.getString(Utils.PHOTO_ITEMS_DATA, null);
            if (jsonString != null) {
                List<Photo> photos = gson.fromJson(jsonString, new TypeToken<List<Photo>>() {
                }.getType());
                for (Photo p : photos) {
                    mTempPhotoResults.add(p);
                }
            }
        }
        mHomeActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        loadImages();
        initDataBinding();
        handleImageTaking();
    }

    private void loadImages() {
        mPhotoService = RetrofitHelper.getPhotos();
        mSubscriber = mPhotoService.getAllPhotos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Photo>>() {
                               @Override
                               public void onCompleted() {
                                   //  Toast.makeText(HomeActivity.this, "Completed..", Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   Log.e(TAG, "Caught: " + e.getMessage(), e);
                                   Utils.createSimpleOkErrorDialog(
                                           HomeActivity.this,
                                           getString(R.string.dialog_comments)
                                   ).show();
                               }

                               @Override
                               public void onNext(List<Photo> photos) {
                                   HomeActivity.this.mPhotoResults = photos;
                                   addTempPhotos();
                                   initAdapter();
                                   onCompleted();
                               }
                           }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscriber != null && !mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
        }
    }

    private void addTempPhotos() {
        for (Photo p : mTempPhotoResults) {
            if (!mPhotoResults.contains(p)) {
                mPhotoResults.add(p);
            }
        }
    }

    private void initDataBinding() {
        mPhotoViewModel = new PhotoViewModel(this);
        mHomeActivityBinding.setPhotoViewModel(mPhotoViewModel);

        mRecyclerView = mHomeActivityBinding.imageList;
        mSwipeRefresher = mHomeActivityBinding.activityMainSwipeRefreshLayout;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }


    private void handleImageTaking() {
        mHomeActivityBinding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                saveImage(imageBitmap);
            }
        } else if (requestCode == Utils.PHOTO_DETAILS) {
            Bundle extras = data.getExtras();
            Photo photo = extras.getParcelable(PhotoDetailActivity.EXTRA_PHOTO);
            if (photo != null) {
                mTempPhotoResults.add(photo);
            } else {
                Log.i(TAG, "Oops, Recent image taken did not load");
            }
        }
    }

    private void addImage(String uri) {
        Photo photo = new Photo(mPhotoResults.get(mPhotoResults.size() - 1).id + 1, "imageTaken", "Recent Image from memory", Utils.getCurrentDateTimeFormat(), uri);
        mPhotoResults.add(photo);
        mTempPhotoResults.add(photo);
        mRecyclerView.getAdapter().notifyItemInserted(mPhotoResults.size() - 1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTempPhotoResults != null && mTempPhotoResults.size() > 0) {

            Type listOfTestObject = new TypeToken<List<Photo>>() {
            }.getType();
            String s = gson.toJson(mTempPhotoResults, listOfTestObject);
            outState.putString(Utils.PHOTO_ITEMS_DATA, s);
            mTempPhotoResults = null;
        }
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Utils.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Utils.WRITE_EXTERNAL_STORAGE:
                break;

            default:
                break;
        }
    }


    private void saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Utils.getCurrentDateTimeFormat() + ".jpg");
        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
            addImage(Uri.fromFile(file).toString());
        } catch (IOException e) {
            Log.e(TAG, "Caught: " + e.getMessage(), e);
        }
    }

    private void initAdapter() {
        mRecyclerView.setAdapter(new PhotoAdapter(this, mPhotoResults));
        mSwipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reFetchPhotos();
            }
        });
    }

    private void reFetchPhotos() {
        loadImages();
        mSwipeRefresher.setRefreshing(false);
    }
}
