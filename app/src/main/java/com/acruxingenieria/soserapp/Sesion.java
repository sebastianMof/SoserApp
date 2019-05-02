package com.acruxingenieria.soserapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Sesion implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Sesion createFromParcel(Parcel in) {
            return new Sesion(in);
        }

        public Sesion[] newArray(int size) {
            return new Sesion[size];
        }
    };

    private String user;
    private String posicion;
    private String bodega;
    private String token;
    private String positionSelected;
    private String bodegaSelected;

    // Constructor
    public Sesion(String user, String posicion, String bodega, String token, String positionSelected, String bodegaSelected){
        this.user = user;
        this.posicion = posicion;
        this.bodega = bodega;
        this.token = token;
        this.positionSelected = positionSelected;
        this.bodegaSelected = bodegaSelected;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(String positionSelected) {
        this.positionSelected = positionSelected;
    }

    public String getBodegaSelected() {
        return bodegaSelected;
    }

    public void setBodegaSelected(String bodegaSelected) {
        this.bodegaSelected = bodegaSelected;
    }


    // Parcelling part
    public Sesion(Parcel in){
        this.user = in.readString();
        this.posicion =  in.readString();
        this.bodega =  in.readString();
        this.token = in.readString();
        this.positionSelected = in.readString();
        this.bodegaSelected = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user);
        dest.writeString(this.posicion);
        dest.writeString(this.bodega);
        dest.writeString(this.token);
        dest.writeString(this.positionSelected);
        dest.writeString(this.bodegaSelected);
    }

    @Override
    public String toString() {
        return "Sesion{" +
                " user='" + user + '\'' +
                ", posicion='" + posicion + '\'' +
                ", bodega='" + bodega + '\'' +
                ", token='" + token + '\'' +
                ", positionSelected='" + positionSelected + '\'' +
                ", bodegaSelected='" + bodegaSelected + '\'' +
                '}';
    }

}