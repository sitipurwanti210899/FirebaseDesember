package com.gosigitgo.firebasedesember;

import android.os.Parcel;
import android.os.Parcelable;

//1 getter and setter untuk string , lalu implement ke parcelable

public class Biodata implements Parcelable {

    //simpan id untuk update dan delete
    String id, nama, alamat, gender, pendidikan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nama);
        dest.writeString(this.alamat);
        dest.writeString(this.gender);
        dest.writeString(this.pendidikan);
    }

    public Biodata() {
    }

    protected Biodata(Parcel in) {
        this.id = in.readString();
        this.nama = in.readString();
        this.alamat = in.readString();
        this.gender = in.readString();
        this.pendidikan = in.readString();
    }

    public static final Parcelable.Creator<Biodata> CREATOR = new Parcelable.Creator<Biodata>() {
        @Override
        public Biodata createFromParcel(Parcel source) {
            return new Biodata(source);
        }

        @Override
        public Biodata[] newArray(int size) {
            return new Biodata[size];
        }
    };
}
