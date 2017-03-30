package luisrodriguez.webphotoapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Luis.Rodriguez on 3/20/2017.
 */

public class Photo implements Parcelable{

    public int id;

    public String title;

    public String comment;

    public String publishedAt;

    public String photo;

    public Photo(int id, String title, String comment, String date, String url){
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.publishedAt = date;
        this.photo = url;
    }

    public Photo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.comment = in.readString();
        this.publishedAt = in.readString();
        this.photo = in.readString();
    }


    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo1 = (Photo) o;

        if (id != photo1.id) return false;
        if (!title.equals(photo1.title)) return false;
        if (!comment.equals(photo1.comment)) return false;
        if (!publishedAt.equals(photo1.publishedAt)) return false;
        return photo.equals(photo1.photo);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + comment.hashCode();
        result = 31 * result + publishedAt.hashCode();
        result = 31 * result + photo.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.comment);
        parcel.writeString(this.publishedAt);
        parcel.writeString(this.photo);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
