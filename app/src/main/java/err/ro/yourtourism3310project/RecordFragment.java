package err.ro.yourtourism3310project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class RecordFragment  extends Fragment {

    SQLiteDatabase db;
    String path;


    public RecordFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.records, container, false);
        path = getActivity().getFilesDir()+"/db";
        Log.d("DeM", "path: $path");
        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);

        ScrollView recordList = view.findViewById(R.id.record_list);
        TableLayout table = new TableLayout(getContext());
        table.setStretchAllColumns(true);
        String[] tableHeader = {"Date","Step","Attraction Visit Count"};

        //set table header row
        TableRow tableHeaderRow = new TableRow(getContext());
        for(int i = 0;i < tableHeader.length;i++){
            TextView tvh = new TextView(getContext());
            String itemh = tableHeader[i];
            tvh.setText(itemh);
            tvh.setPadding(5,2,3,2);
            tvh.setBackgroundColor(Color.GREEN);
            tvh.setTextSize(14);
            //Toast.makeText(getApplicationContext(),tvh.getText().toString(),Toast.LENGTH_LONG);
            tableHeaderRow.addView(tvh);
        }
        table.addView(tableHeaderRow);

        Cursor cursor = db.rawQuery("select * from steplog order by date ",null);
        //int count = cursor.getCount();
        String[] sqlTableCol = {"date","step"};
        while(cursor.moveToNext()){
            TableRow row = new TableRow(getContext());

            String preview = "";
            String rowDate = "";
            for(int j = 0; j < sqlTableCol.length; j++) {
                TextView tv2 = new TextView(getContext());
                tv2.setPadding(5,2,3,2);

                int index = cursor.getColumnIndex(sqlTableCol[j]);
                String item;
                if(index >= 0){
                    item = cursor.getString(index);
                }else{
                    item = "";
                }
                if(sqlTableCol[j].equals("date")){
                    rowDate = item;
                }
                preview += item + ",";
                tv2.setText(item);
                row.addView(tv2);
            }
            if(!rowDate.equals("")){
                Cursor cursor2 = db.rawQuery("select * from attractions where date = '"+rowDate+"' ORDER BY date DESC",null);
                int count = cursor2.getCount();

                TextView tv2 = new TextView(getContext());
                tv2.setPadding(5,2,3,2);
                tv2.setText(count+"");
                row.addView(tv2);
            }

            Log.d("meD",preview+"count");

            table.addView(row);
        }

        recordList.addView(table);

        Button chartBtn = view.findViewById(R.id.chart_btn);
        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),BarGraphActivity.class);
                startActivity(i);
            }
        });


        return view;
    }

}
