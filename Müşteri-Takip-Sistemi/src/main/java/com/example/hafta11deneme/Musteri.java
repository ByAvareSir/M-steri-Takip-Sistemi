package com.example.hafta11deneme;

public class Musteri {

    private String ad,soyad,mail;

    public Musteri(String ad, String soyad, String mail) {
        this.ad = ad;
        this.soyad = soyad;
        this.mail = mail;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
