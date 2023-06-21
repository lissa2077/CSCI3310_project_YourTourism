package err.ro.yourtourism3310project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AttractionCardViewAdapter extends ArrayAdapter<Attraction> {

    TextView title;
    TextView experience;
    TextView rating;
    ImageView image;
    Button viewMapBtn;
    TextView date;

    ImageButton modifyBtn, deleteBtn,shareBtn;

    Activity a;

    public AttractionCardViewAdapter(Context context, ArrayList<Attraction> arrayList, Activity a) {
        super(context, 0, arrayList);
        this.a = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //recyclable view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.attraction_card, parent, false);
        }
        Attraction attraction = getItem(position);

        title = view.findViewById(R.id.attraction_card_title);
        experience = view.findViewById(R.id.attraction_card_experience);
        rating = view.findViewById(R.id.attraction_card_rating);
        viewMapBtn = view.findViewById(R.id.view_map_btn);
        date = view.findViewById(R.id.attraction_card_date);

        modifyBtn = view.findViewById(R.id.modify_btn);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(a,InsertActivity.class);
                i.putExtra("title",attraction.name);
                i.putExtra("experience",attraction.experience);
                i.putExtra("rating",attraction.rating);
                i.putExtra("date",attraction.date);
                i.putExtra("attrid",attraction.attid);
                i.putExtra("lat",attraction.lat);
                i.putExtra("lng",attraction.lng);
                a.startActivity(i);
            }
        });
        deleteBtn = view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogShow cdd = new DialogShow(getContext(), "Delete","Are you sure to detele this item?",0,attraction.attid);
                cdd.setCanceledOnTouchOutside(false);
                cdd.show();
            }
        });
        shareBtn = view.findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(a,QRcodeActivity.class);
                i.putExtra("title",attraction.name);
                /*i.putExtra("experience",attraction.experience);
                i.putExtra("rating",attraction.rating);
                i.putExtra("date",attraction.date);
                i.putExtra("attrid",attraction.attid);*/
                i.putExtra("lat",attraction.lat);
                i.putExtra("lng",attraction.lng);
                a.startActivity(i);
            }
        });

        title.setText(attraction.name);
        experience.setText(attraction.experience);
        rating.setText("Rating: "+attraction.rating+"/5");
        date.setText("Creation Date: "+attraction.date);

        viewMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapUtil.viewByMap = new LatLng(attraction.lat,attraction.lng);
                MapUtil.viewByMapAttraction = attraction;
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.map);
            }
        });

        return view;
    }
}
