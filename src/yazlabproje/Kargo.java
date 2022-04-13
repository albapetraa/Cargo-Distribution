/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlabproje;

/**
 *
 * @author Hp
 */
public class Kargo {

    public double kargoLat;
    public double kargoLng;
    public int kargoId;
    public int sahteKargoId;
    public boolean teslimEdilme = false;

    public Kargo(double kargoLat, double kargoLng) {
        this.kargoLat = kargoLat;
        this.kargoLng = kargoLng;
    }

    public boolean isTeslimEdilme() {
        return teslimEdilme;
    }

    public void setTeslimEdilme(boolean teslimEdilme) {
        this.teslimEdilme = teslimEdilme;
    }

    public double getKargoLat() {
        return kargoLat;
    }

    public void setKargoLat(double kargoLat) {
        this.kargoLat = kargoLat;
    }

    public double getKargoLng() {
        return kargoLng;
    }

    public void setKargoLng(double kargoLng) {
        this.kargoLng = kargoLng;
    }

    public int getKargoId() {
        return kargoId;
    }

    public void setKargoId(int kargoId) {
        this.kargoId = kargoId;
    }

    public int getSahteKargoId() {
        return sahteKargoId;
    }

    public void setSahteKargoId(int sahteKargoId) {
        this.sahteKargoId = sahteKargoId;
    }

}
