package err.ro.yourtourism3310project;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcodeActivity extends AppCompatActivity {

    Button share_photo_btn;
    Button save_photo_btn;
    ImageButton back_btn;
    Button mode_btn;
    Bitmap qr;

    boolean googleMapShare = false;
    ImageView idIVQrcode;
    TextView displayPlace;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);


        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);//permission request code is just an int
            }
        }else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        share_photo_btn = findViewById(R.id.share_photo_btn);
        share_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), qr, null,null));

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                intent.putExtra(Intent.EXTRA_SUBJECT, "Share my attraction!");
                intent.putExtra(Intent.EXTRA_TEXT, "There is my attraction: ");
                intent.setType("image/png");

                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });
        save_photo_btn = findViewById(R.id.save_photo_btn);
        save_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "Attraction_"+String.valueOf(System.currentTimeMillis()) +".jpg";
                File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    qr.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(QRcodeActivity.this, "The photo is saved at "+file.toString(), Toast.LENGTH_SHORT).show();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mode_btn = findViewById(R.id.mode_btn);
        mode_btn.setText("Your Tourism Mode");//default false
        mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapShare = !googleMapShare;
                if(googleMapShare){
                    mode_btn.setText("Google Map Link Mode");
                }else{
                    mode_btn.setText("Your Tourism Mode");
                }
                createQRcode();


            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        idIVQrcode = findViewById(R.id.idIVQrcode);
        displayPlace = findViewById(R.id.display_place);
        createQRcode();
    }

    public void createQRcode(){
        Intent i = getIntent();
        if(i.getExtras() != null){
            String name = i.getStringExtra("title");
            displayPlace.setText(name);
            double lat = i.getDoubleExtra("lat",0.0);
            double lng = i.getDoubleExtra("lng",0.0);

            int width = 400;
            int height = 400;

            // generating dimension from width and height.
            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;

            String msg = "";
            if(googleMapShare){
                msg = "https://maps.google.com/?q="+lat+","+lng;
            }else{
                msg = name+";;"+lat+";;"+lng;
            }

            QRGEncoder qrgEncoder = new QRGEncoder(msg, null, QRGContents.Type.TEXT, dimen);
            qr = qrgEncoder.getBitmap(0);
            idIVQrcode.setImageBitmap(qr);

        }
    }
}
//https://www.google.com/maps/search/?api=1&query=22.302711,%20114.177216
//https://www.google.com/maps/place/Testing%20123/@22.302711,114.177216,17z