package err.ro.yourtourism3310project;

import com.google.android.gms.maps.model.LatLng;

public class Attraction {
    //attid int PRIMARY KEY,date text,name text,rating real,experience text,lat real, lng real
    int attid = -1;
    String date;
    String name;
    Float rating = 3.0f;
    String experience;
    double lat = 0.0;
    double lng = 0.0;

    public Attraction(String name, Float rating, String experience, double lat, double lng) {
        this.attid = -1;
        this.date = "";
        this.name = name;
        this.rating = rating;
        this.experience = experience;
        this.lat = lat;
        this.lng = lng;
    }


    public Attraction(int attid, String date, String name, Float rating, String experience, double lat, double lng) {
        this.attid = attid;
        this.date = date;
        this.name = name;
        this.rating = rating;
        this.experience = experience;
        this.lat = lat;
        this.lng = lng;
    }


    public Attraction() {

    }

    public LatLng getLatLng(){
        return new LatLng(lat,lng);
    }
}
