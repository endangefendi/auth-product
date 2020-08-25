package com.agus.hendrik.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KategoriModel implements Parcelable {

    public String title;

    public KategoriModel(String title) {
        this.title = title;
    }


    protected KategoriModel(Parcel in) {
        title = in.readString();
    }

    public static final Creator<KategoriModel> CREATOR = new Creator<KategoriModel>() {
        @Override
        public KategoriModel createFromParcel(Parcel in) {
            return new KategoriModel(in);
        }

        @Override
        public KategoriModel[] newArray(int size) {
            return new KategoriModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
    }
}
