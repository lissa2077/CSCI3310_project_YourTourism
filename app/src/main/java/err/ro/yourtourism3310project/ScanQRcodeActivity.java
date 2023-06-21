package err.ro.yourtourism3310project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

//refer from https://github.com/yuriy-budiyev/code-scanner
public class ScanQRcodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr_code);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanQRcodeActivity.this,"found QRcode" , Toast.LENGTH_SHORT).show();
                        String str = result.getText();
                        String[] arr = str.split(";;");
                        if(arr.length == 3){
                            try {
                                Intent i = new Intent(ScanQRcodeActivity.this,InsertActivity.class);
                                i.putExtra("title",arr[0]);
                                i.putExtra("lat",Double.parseDouble(arr[1]));
                                i.putExtra("lng",Double.parseDouble(arr[2]));
                                startActivity(i);
                                finish();
                            }catch (Exception e){
                                Toast.makeText(ScanQRcodeActivity.this,"not vaild QRcode" , Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }else{
                            Toast.makeText(ScanQRcodeActivity.this,"not vaild QRcode" , Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}