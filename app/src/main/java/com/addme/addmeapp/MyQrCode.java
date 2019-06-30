package com.addme.addmeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.addme.addmeapp.AccountActivity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyQrCode extends AppCompatActivity {

    String TAG="GenerateQrCode";
    String inputValue;
    private EditText edttxt;
    private Button start;
    private ImageView qrimg;
    Bitmap bitmap;

    QRGEncoder qrgEncoder;
    String userID;

    private Button back_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);

        // Back button
        back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanActivity();
            }
        });


        // Get userID from firebase
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        userID = currentFirebaseUser.getUid();


        qrimg = (ImageView) findViewById(R.id.qrCode);

        inputValue = userID;

        if (inputValue.length() > 0){

            WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);

            int width = point.x;
            int height = point.y;
            int smallerdimension = width < height ? width:height;

            smallerdimension = smallerdimension*3/4;
            qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,
                                       smallerdimension);

            try{
                bitmap = qrgEncoder.encodeAsBitmap();
                qrimg.setImageBitmap(bitmap);

            }catch (WriterException e){
                Log.v(TAG,e.toString());
            }

        }else{
            edttxt.setError("Required");
        }


    }

    private void openScanActivity() {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
    }

}
