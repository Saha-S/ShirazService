package com.alizare.server.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.alizare.server.R;

public class MyServicesDetailes extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView fullname = (TextView) findViewById(R.id.txt_fullname);
        TextView title = (TextView) findViewById(R.id.txt_title);
        TextView phone = (TextView) findViewById(R.id.txt_phone);
        TextView address = (TextView) findViewById(R.id.txt_address);
        TextView desc = (TextView) findViewById(R.id.txt_desc);

        fullname.setText(" نام و نام خانوداگی : "+MyServices.fullname);
        title.setText("عنوان درخواست : "+MyServices.stitle);
        phone.setText(" شماره تلفن ثابت : "+MyServices.phone);
        address.setText("آدرس : "+ MyServices.address);
        desc.setText("توضیحات : "+MyServices.desc);




    }


}
