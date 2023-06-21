package err.ro.yourtourism3310project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment{

    GoogleMap mMap;

    public MapFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                MapUtil.setMyLocationEnabled(getContext(),mMap,true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                LatLng hk = new LatLng(22.302711, 114.177216);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hk, 16));

                ArrayList<Attraction> aList = DatabaseUtil.getAttractions();
                for (int i = 0;i < aList.size();i++){
                    Attraction a = aList.get(i);
                    LatLng latLng = a.getLatLng();
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(a.name)
                            .snippet("Rating: "+a.rating+"/5"));
                }
                if(MapUtil.viewByMap != null){
                    LatLng latLng = MapUtil.viewByMap;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    MapUtil.viewByMap = null;
                    MapUtil.viewByMapAttraction = null;
                }

            }
        });
        // Return view
        return view;
    }


}
