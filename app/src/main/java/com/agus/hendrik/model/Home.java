package com.agus.hendrik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Home implements Parcelable {

    public int image;
    public String title;
    public int level;

    public Home(int image, String title, int level) {
        this.image = image;
        this.title = title;
        this.level = level;
    }


    protected Home(Parcel in) {
        image = in.readInt();
        title = in.readString();
        level = in.readInt();
    }

    public static final Creator<Home> CREATOR = new Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(image);
        parcel.writeString(title);
        parcel.writeInt(level);
    }
}
