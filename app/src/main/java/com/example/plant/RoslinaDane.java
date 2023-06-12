package com.example.plant;

import android.widget.Switch;

import com.example.plant.exceptions.RoslinaDaneException;

import java.io.Serializable;
import java.util.Date;

public class RoslinaDane implements Serializable {
    private String nazwa;
    private int czestotliwoscPodlewaniaWDniach;
    private boolean zraszanie;
    private String notatki;
    private String zdjecie;
    private String dataNastępnegoPodlania;

    //dane
    public RoslinaDane(String nazwa, int czestotliwoscPodlewaniaWDniach, boolean zraszanie, String notatki, String zdjecie, String dataNastępnegoPodlania) {
        this.nazwa = nazwa;
        this.czestotliwoscPodlewaniaWDniach = czestotliwoscPodlewaniaWDniach;
        this.zraszanie = zraszanie;
        this.notatki = notatki;
        this.zdjecie = zdjecie;
        this.dataNastępnegoPodlania = dataNastępnegoPodlania;


        //exceptions
        if (nazwa.length() > 30) {
            throw new RoslinaDaneException("Nazwa rosliny za dluga");
        }
        if (czestotliwoscPodlewaniaWDniach > 30 ){
            throw new RoslinaDaneException("Podaj prawidłową liczbę dni");
        }
        if (czestotliwoscPodlewaniaWDniach <= 0){
            throw new RoslinaDaneException("Podaj prawidłową liczbę dni");}
        if (notatki.length() > 300){
            throw new RoslinaDaneException("zbyt długa notatka");
        }
    }
    //tworzenie akcesorów get i set
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getCzestotliwoscPodlewaniaWDniach() {
        return czestotliwoscPodlewaniaWDniach;
    }

    public void setCzestotliwoscPodlewaniaWDniach(int czestotliwoscPodlewaniaWDniach) {
        this.czestotliwoscPodlewaniaWDniach = czestotliwoscPodlewaniaWDniach;
    }

    public boolean isZraszanie() {
        return zraszanie;
    }

    public void setZraszanie(boolean zraszanie) {
        this.zraszanie = zraszanie;
    }

    public String getNotatki() {
        return notatki;
    }

    public void setNotatki(String notatki) {
        this.notatki = notatki;
    }

    public void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }

    public String getDataNastępnegoPodlania() {
        return dataNastępnegoPodlania;
    }

    public void setDataNastępnegoPodlania(String dataNastępnegoPodlania) {
        this.dataNastępnegoPodlania = dataNastępnegoPodlania;
    }

    public int getZdjecie() {
        switch(zdjecie) {
            case "Kaktus":
                return R.drawable.item1;
            case "Dracena":
                return R.drawable.item2;
            case "Paproć":
                return R.drawable.item3;
            case "Drzewo liściaste":
                return R.drawable.item4;
            default:
                return R.drawable.item5;
        }
    }

    public String getZdjecieStr() {
        return zdjecie;
    }

    @Override
    public String toString() {
        return "RoslinaDane{" +
                "nazwa='" + nazwa + '\'' +
                ", czestotliwoscPodlewaniaWDniach=" + czestotliwoscPodlewaniaWDniach +
                ", zraszanie=" + zraszanie +
                ", notatki='" + notatki + '\'' +
                '}';
    }
}
