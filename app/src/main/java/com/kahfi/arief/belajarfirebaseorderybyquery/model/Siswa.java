package com.kahfi.arief.belajarfirebaseorderybyquery.model;

/**
 * Created by Arief on 9/4/2017.
 */

public class Siswa {

    private int noAbsen;
    private String nama;
    private int umur;


    public Siswa(){}
    public Siswa(int noAbsen,String nama,int umur){
        this.noAbsen=noAbsen;
        this.nama=nama;
        this.umur=umur;
    }

    public void setNoAbsen(int noAbsen){
        this.noAbsen=noAbsen;
    }
    public void setNama(String nama){
        this.nama=nama;
    }
    public void setUmur(int umur){
        this.umur=umur;
    }

    public int getNoAbsen(){
        return noAbsen;
    }
    public String getNama(){
        return nama;
    }
    public int getUmur(){
        return umur;
    }

    @Override
    public String toString() {
        return "Siswa{" +
                "noAbsen=" + noAbsen +
                ", nama='" + nama + '\'' +
                ", umur=" + umur +
                '}';
    }
}
