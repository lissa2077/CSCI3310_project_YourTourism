package err.ro.yourtourism3310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class AttractionCardListFragment extends Fragment {

    ListView alv;
    Button loadQrcodeBtn;

    public AttractionCardListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attractions_list, container, false);

        ArrayList<Attraction> aList = DatabaseUtil.getAttractions();
        AttractionCardViewAdapter acva = new AttractionCardViewAdapter(getContext(),aList,getActivity());
        alv = view.findViewById(R.id.attraction_card_list);
        alv.setAdapter(acva);

        loadQrcodeBtn = view.findViewById(R.id.load_qrcode_btn);
        loadQrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ScanQRcodeActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}
