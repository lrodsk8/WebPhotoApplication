package luisrodriguez.webphotoapplication.Views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import luisrodriguez.webphotoapplication.Model.Photo;
import luisrodriguez.webphotoapplication.R;
import luisrodriguez.webphotoapplication.ViewModel.PhotoViewModel;
import luisrodriguez.webphotoapplication.databinding.PhotoItemBinding;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> mPhotoList;
    private Context mContext;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        this.mContext = context;
        this.mPhotoList = photoList;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.photo_item,
                parent, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bindPhoto(mPhotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        PhotoItemBinding mPhotoBinding;
        PhotoViewModel mViewModel;

        public PhotoViewHolder(PhotoItemBinding photoItemBinding) {
            super(photoItemBinding.getRoot());
            this.mPhotoBinding = photoItemBinding;
            mViewModel = new PhotoViewModel (mContext);
            mPhotoBinding.setPhotoViewModel(mViewModel);

        }

        void bindPhoto(Photo photo) {
            if (mPhotoBinding.getPhotoViewModel() == null) {
                mPhotoBinding.setPhotoViewModel(
                        new PhotoViewModel(itemView.getContext()));
            } else {
                mPhotoBinding.getPhotoViewModel().setPhotoItem(photo);
            }
            mPhotoBinding.executePendingBindings();
        }
    }
}
