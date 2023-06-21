package err.ro.yourtourism3310project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseUtil {

    static SQLiteDatabase db;
    static String path = "";

    public static void openDatabase(){
        if(db == null){
            db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.CREATE_IF_NECESSARY);
        }
    }

    public static void close(){
        if(db != null){
            db.close();
            db = null;
        }
    }

    public static void init(){
        openDatabase();
        String sql = "DROP TABLE IF EXISTS steplog;";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS attractions;";
        db.execSQL(sql);

        sql = "CREATE TABLE steplog (date text PRIMARY KEY, step int);";
        db.execSQL(sql);
        sql = "CREATE TABLE attractions (attid INTEGER PRIMARY KEY AUTOINCREMENT,date text,name text,rating real,experience text,lat DOUBLE, lng DOUBLE);";
        db.execSQL(sql);

        //default data
        Attraction a = new Attraction(1,"08/05/2023","name",0.5f,"experience",22.302711, 114.177216);
        insertAttraction(a,true);
        a = new Attraction(2,"08/05/2023","太古城中心",4.5f,"Maecenas feugiat rutrum felis in consequat. Vestibulum blandit ultrices neque, sed convallis erat ultrices nec. Nullam in aliquet tellus. Cras feugiat, metus ac luctus tincidunt, nulla velit commodo erat, quis faucibus orci diam ut neque. Mauris nec ante at augue ultrices posuere. Proin id velit gravida urna blandit pharetra vitae.",22.2867139,114.214173);
        insertAttraction(a,true);
        a = new Attraction(3,"09/05/2023","愛秩序灣太安街",5.0f,"Nunc eu placerat erat, nec posuere nisi. Mauris euismod nulla mollis ultrices lacinia. Nulla facilisi.",22.283544, 114.223767);
        insertAttraction(a,true);
        a = new Attraction(4,"10/05/2023","耀東",3.5f,"Nullam pharetra felis eu sapien semper condimentum. Phasellus vestibulum mi augue, at ultrices dui iaculis vel. Praesent imperdiet velit est, non maximus metus vehicula id. Vestibulum tempus massa non nisl sagittis mattis. In nec velit est. Curabitur vitae auctor odio.e",22.27586000403607, 114.22620048228166);
        insertAttraction(a,true);
        a = new Attraction(5,"10/05/2023","海邊",4.0f,"Praesent eleifend ipsum pulvinar metus fringilla iaculis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque posuere risus ut facilisis aliquet.",22.282948247754685, 114.23044375618834);
        insertAttraction(a,true);
        a = new Attraction(6,"10/05/2023","柴灣MTR",1.5f,"Suspendisse feugiat diam nec ligula molestie, condimentum viverra justo sodales. Nunc porta accumsan lorem nec consectetur. Mauris lobortis sem vel dolor malesuada, at elementum purus maximus. Suspendisse id dolor libero. Sed justo est, vestibulum id.",22.264995170115096, 114.237251189139);
        insertAttraction(a,true);
        a = new Attraction(7,"11/05/2023","柴灣海邊",2.5f,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis faucibus mauris, nec sagittis elit volutpat eu. Fusce id rhoncus velit. Duis fermentum nisi ut est feugiat scelerisque. Sed vel.",22.272415444568573, 114.24356552392345);
        insertAttraction(a,true);
        a = new Attraction(8,"11/05/2023","小西灣運動場",3.0f,"Praesent auctor pellentesque ante, imperdiet sodales odio feugiat a. Quisque.",22.267676761352373, 114.24907541053652);
        insertAttraction(a,true);
        a = new Attraction(9,"13/05/2023","形薈",5.0f,"Vestibulum id libero id lorem ornare lobortis. Duis purus arcu, convallis quis iaculis non, porta sed urna. Cras maximus tortor.",22.277220248909508, 114.22859265237093);
        insertAttraction(a,true);
        updateStepLog(5473,"08/05/2023");
        updateStepLog(7854,"09/05/2023");
        updateStepLog(6410,"10/05/2023");
        updateStepLog(9802,"11/05/2023");
        updateStepLog(1257,"12/05/2023");
        updateStepLog(3941,"13/05/2023");
        updateStepLog(8642,"14/05/2023");
    }

    public static void updateStepLog(int currentSteps){
        updateStepLog(currentSteps,getToday());
    }

    public static void updateStepLog(int currentSteps,String dateToStr){
        openDatabase();
        String sql = "INSERT OR REPLACE INTO steplog (date,step) values ('"+dateToStr+"',"+currentSteps+");";
        db.execSQL(sql);
    }

    public static ArrayList<StepLog> getStepLogTop5(){
        openDatabase();
        Cursor cursor = db.rawQuery("select * from steplog order by step DESC",null);
        String[] sqlTableCol = {"date","step"};
        ArrayList<StepLog> sList = new ArrayList<>();
        int count = 0;
        while(cursor.moveToNext() && count < 5){

            StepLog s = new StepLog();
            for(int j = 0; j < sqlTableCol.length; j++) {
                int index = cursor.getColumnIndex(sqlTableCol[j]);
                String item;
                if(index >= 0){
                    item = cursor.getString(index);
                }else{
                    item = "";
                }
                switch (sqlTableCol[j]){
                    case "date":
                        s.date = item;
                        break;
                    case "step":
                        s.step = Integer.parseInt(item);
                        break;
                }
            }
            sList.add(s);
            count++;
        }

        return sList;
    }

    public static void insertAttraction(Attraction a){
        insertAttraction(a,false);
    }

    public static void insertAttraction(Attraction a,boolean create){
        openDatabase();
        String dateToStr = getToday();
        String sql;
        if(a.attid == -1){
            //create
            Log.d("meD","insertAttraction: create");
            sql = "INSERT INTO attractions (date,name,rating,experience,lat , lng) values (" +
                    "'"+dateToStr+"','"+a.name+"',"+a.rating+",'"+a.experience+"',"+a.lat+","+a.lng+");";
        }else{
            //update
            if(create){
                //create with id and date

                sql = "INSERT INTO attractions (attid,date,name,rating,experience,lat , lng) values (" +
                        a.attid+",'"+a.date+"','"+a.name+"',"+a.rating+",'"+a.experience+"',"+a.lat+","+a.lng+");";
                Log.d("meD","insertAttraction: create with id and date, "+sql);
            }else{
                Log.d("meD","insertAttraction: update");
                sql = "Update attractions " +
                        "SET date='"+a.date+"', name='"+a.name+"',experience='"+a.experience+"', rating='"+a.rating+"' , lat="+a.lat+" , lng="+a.lng+" " +
                        "WHERE attid="+a.attid+";";
            }

        }
        db.execSQL(sql);
        //db.close();
    }

    public static void deleteAttraction(int attid){
        openDatabase();
        String sql = "DELETE FROM  attractions " +
                "WHERE attid="+attid+";";
        db.execSQL(sql);
    }



    public static ArrayList<StepLog> getAttractionsCountTop5(){
        Cursor cursor = db.rawQuery("select date, count(attid) from attractions GROUP BY date order by COUNT(attid) DESC;",null);
        String[] sqlTableCol = {"date","count(attid)"};
        ArrayList<StepLog> slist = new ArrayList<>();
        while(cursor.moveToNext()){
            StepLog s = new StepLog();
            String preview = "";
            boolean skip = false;
            for(int j = 0; j < sqlTableCol.length; j++) {
                int index = cursor.getColumnIndex(sqlTableCol[j]);
                String item;
                if(index >= 0){
                    item = cursor.getString(index);
                }else{
                    item = "";
                }
                preview += item + ",";
                switch (sqlTableCol[j]){
                    case "date":
                        if(item.equals("")){
                            skip = true;
                            break;
                        }
                        s.date = item;
                        break;
                    case "count(attid)":
                        s.attractionCount = Integer.parseInt(item);
                        break;

                }
            }
            if(skip){
                continue;
            }
            Log.d("meD",preview);
            slist.add(s);
        }
        return slist;
    }

    public static ArrayList<Attraction> getAttractions(){
        return getAttractions("select attid,date,name,rating,experience,printf('%.15f', lat) AS lat2,printf('%.15f', lng) AS lng2 from attractions order by date DESC");
    }
    public static ArrayList<Attraction> getAttractions(String sql){
        openDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        String[] sqlTableCol = {"attid","date","name","rating","experience","lat2","lng2"};
        ArrayList<Attraction> a_list = new ArrayList<>();
        while(cursor.moveToNext()){
            Attraction a = new Attraction();
            for(int j = 0; j < sqlTableCol.length; j++) {
                int index = cursor.getColumnIndex(sqlTableCol[j]);
                String item;
                if(index >= 0){
                    item = cursor.getString(index);
                }else{
                    item = "";
                }
                switch (sqlTableCol[j]){
                    case "attid":
                        a.attid = Integer.parseInt(item);
                        break;
                    case "date":
                        a.date = item;
                        break;
                    case "name":
                        a.name = item;
                        break;
                    case "rating":
                        if(!item.equals("")){
                            a.rating = Float.parseFloat(item);
                        }

                        break;
                    case "experience":
                        a.experience = item;
                        break;
                    case "lat2":
                        Log.d("meD","getAttractions: item(lat) = "+item);
                        if(!item.equals("")){
                            a.lat = Double.parseDouble(item);
                        }
                        break;
                    case "lng2":
                        if(!item.equals("")){
                            a.lng = Double.parseDouble(item);
                        }
                        break;
                }
            }
            a_list.add(a);
        }
        return a_list;
    }

    public static String getToday(){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(today);
    }


}
