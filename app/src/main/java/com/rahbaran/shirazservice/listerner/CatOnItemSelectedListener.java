package com.rahbaran.shirazservice.listerner;

/**
 * Created by Nadia on 9/18/2017.
 */

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.rahbaran.shirazservice.activities.MainActivity;

public class CatOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        MainActivity.catId = MainActivity.ServiceCatsID.get(pos);
        MainActivity.catpos = String.valueOf(pos);
        MainActivity.ServiceSubId.clear();
        MainActivity.ServiceSub.clear();
     //   MainActivity.subpos= "";
        MainActivity.getSubCat();



    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}