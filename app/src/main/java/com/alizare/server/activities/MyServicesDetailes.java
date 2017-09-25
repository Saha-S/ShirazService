package com.alizare.server.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alizare.server.R;

public class MyServicesDetailes extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton back = (ImageButton)  findViewById(R.id.back_ib);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView title = (TextView) findViewById(R.id.txt_title);
        TextView desc = (TextView) findViewById(R.id.txt_desc);

        TextView cat = (TextView) findViewById(R.id.txt_cat_subcat);
        TextView area = (TextView) findViewById(R.id.txt_area);
        TextView time = (TextView) findViewById(R.id.txt_time);
        TextView state = (TextView) findViewById(R.id.txt_state);
        TextView olaviat = (TextView) findViewById(R.id.txt_olaviat);
        TextView price = (TextView) findViewById(R.id.txt_price);


        title.setText(Html.fromHtml("<b>عنوان درخواست : </b>"+MyServices.stitle));
        desc.setText(Html.fromHtml("<b>توضیحات :</b> "+MyServices.desc));
        cat.setText(Html.fromHtml("<b> گروه و زیر گروه :</b> "+MyServices.cat) );
        area.setText(Html.fromHtml("<b>محدوده : </b>"+MyServices.area));
        time.setText(Html.fromHtml("<b>تاریخ درخواست : </b>"+MyServices.time));
        state.setText(Html.fromHtml("<b>وضعیت درخواست :</b> "+MyServices.stat));
        olaviat.setText(Html.fromHtml("<b>اولویت درخواست : </b>"+MyServices.olaviat));
        price.setText(Html.fromHtml("<b>هزینه محاسبه شده :</b> "+MyServices.price));





    }


}
