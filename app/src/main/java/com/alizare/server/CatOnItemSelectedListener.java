package com.alizare.server;

/**
 * Created by Nadia on 9/18/2017.
 */

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.alizare.server.activities.Filter;

public class CatOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Filter.catId = Filter.ServiceCatsID.get(pos);
        Filter.ServiceSubId.clear();
        Filter.ServiceSub.clear();
        Filter.getSubCat();


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}