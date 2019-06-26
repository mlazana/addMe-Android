package com.addme.addmeapp;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class MyQrCode extends Fragment {

    String TAG="GenerateQrCode";
    EditText edttxt;
    ImageView qrimg;
    Button start;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String inputvalue;
    SensorManager sensorManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myqrcode, container, false);


        qrimg = (ImageView) view.findViewById(R.id.qrcode);
        edttxt = (EditText) view.findViewById(R.id.eidttext);
        start = (Button) view.findViewById(R.id.createbtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputvalue = edttxt.getText().toString().trim();
                if (inputvalue.length() > 0) {
                    WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerdimension = width < height ? width : height;
                    smallerdimension = smallerdimension * 3 / 4;
                    qrgEncoder = new QRGEncoder(inputvalue, null, QRGContents.Type.TEXT, smallerdimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrimg.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    edttxt.setError("Required");
                }
            }

        });
        return view;
    }


}
