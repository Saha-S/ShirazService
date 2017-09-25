package com.alizare.server.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alizare.server.App;
import com.alizare.server.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

public class MyServices extends AppCompatActivity {

    private ArrayList<String> ServiceArea;
    private ArrayList<String> ServicePrice;
    private ArrayList<String> ServiceTitle;
    private ArrayList<String> ServiceCat;
    private ArrayList<String> ServiceTime;
    private ArrayList<String> ServiceStat;
    private ArrayList<String> ServiceOlaviat;
    private ArrayList<String> ServiceDesc;
    private LinearLayout container;
    public static String stitle , cat, area, time, desc , stat , olaviat, price;
    private ProgressDialog pd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RatingBar ratingbar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton back = (ImageButton)  findViewById(R.id.back_ib);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ServiceTitle = new ArrayList<String>();
        ServicePrice = new ArrayList<String>();
        ServiceArea = new ArrayList<String>();

        ServiceCat = new ArrayList<String>();
        ServiceTime = new ArrayList<String>();
        ServiceStat = new ArrayList<String>();
        ServiceOlaviat = new ArrayList<String>();
        ServiceDesc = new ArrayList<String>();

        container = (LinearLayout)findViewById(R.id.ll_mysrvices);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);




        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            swipeRefreshLayout.setRefreshing(true);

            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            swipeRefreshLayout.setRefreshing(false);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    //   ServiceTime.clear();
                    //   ServiceTitle.clear();
                    //    ServiceArea.clear();
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                    swipeRefreshLayout.setRefreshing(true);


                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }
                //  scrollListener.resetState();
                // scroll.rese
                container.removeAllViewsInLayout();
                ServiceTime.clear();
                ServiceTitle.clear();
                ServiceArea.clear();
                ServiceStat.clear();
                ServicePrice.clear();
                ServiceOlaviat.clear();
                ServiceCat.clear();
                ServiceDesc.clear();



            }
        });




    }
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        private int index;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            Toast.makeText(ActivitygetAreas.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            for(int i = 0; i< ServiceTitle.size(); i++) {

                final View child = inflater.inflate(R.layout.item_my_service, null, false);
                TextView areaName = (TextView) child.findViewById(R.id.txt_area);
                TextView title = (TextView) child.findViewById(R.id.txt_title);
                TextView state = (TextView) child.findViewById(R.id.txt_state);
                TextView txttime = (TextView) child.findViewById(R.id.txt_time);
                CardView item = (CardView) child.findViewById(R.id.ll_row);

                ratingbar1=(RatingBar)child.findViewById(R.id.ratingBar1);

                SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

                final String rating  = prefs.getString("rating", "0");

                ratingbar1.setRating(Integer.parseInt(rating));


                areaName.setText("محدوده ی "+ServiceArea.get(i));
                title.setText(ServiceTitle.get(i));
                state.setText(ServiceStat.get(i));
                if(ServiceStat.get(i).equals("ثبت شده")){
                    state.setBackgroundColor(Color.parseColor("#8c8c8c"));
                }
                if(ServiceStat.get(i).equals("انتخاب شده")){
                    state.setBackgroundColor(Color.parseColor("#ff9900"));
                }
                if(ServiceStat.get(i).equals("لغو / حذف شده")){
                    state.setBackgroundColor(Color.parseColor("#cc0000"));
                }
                if(ServiceStat.get(i).equals("پذیرش شده")){
                    state.setBackgroundColor(Color.parseColor("#0000cc"));
                }
                if(ServiceStat.get(i).equals("انجام شده")){
                    state.setBackgroundColor(Color.parseColor("#006600"));
                }
                if(ServiceStat.get(i).equals("عدم ارائه سرویس")){
                    state.setBackgroundColor(Color.parseColor("#6b6b47"));
                }
                txttime.setText(ServiceTime.get(i));
                container.addView(child);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        stitle = ServiceTitle.get(index);
                        area = ServiceArea.get(index);
                        stat = ServiceStat.get(index);
                        desc = ServiceDesc.get(index);
                        time = ServiceTime.get(index);
                        cat = ServiceCat.get(index);
                        olaviat = ServiceOlaviat.get(index);
                        price = ServicePrice.get(index);

                        index = ((LinearLayout) child.getParent()).indexOfChild(child);
                        Intent intent = new Intent(MyServices.this, MyServicesDetailes.class);
                        startActivity(intent);




                    }
                });

            }
           swipeRefreshLayout.setRefreshing(false);




        }

    }

    public void calculate() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getMyServices";
        String METHOD_NAME = "getMyServices";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId", "2");
            Request.addProperty("state", "");
            // Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;


            // Set output SOAP object
            envelope.setOutputSoapObject(Request);

            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            SoapObject response;

            // StringBuffer result = null;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();
            Log.i("teeeees", result.toString());

            int length = result.size();

            for (int i = 0; i < length; ++i) {
                SoapObject so = result.get(i);
                //   smart = new Smartphone();
                //  for (int j = 0; j < so.getPropertyCount(); j++) {
                // smart.setProperty(j, so.getProperty(j));
                ServiceTitle.add(so.getPropertyAsString("serviceTitle"));
                ServiceArea.add(so.getPropertyAsString("areaTitle"));
                ServiceCat.add(so.getPropertyAsString("catTitle") + " - " +so.getPropertyAsString("subCatTitle") );
                ServiceTime.add(so.getPropertyAsString("insrtTimePersian"));
                ServiceStat.add(so.getPropertyAsString("stateTitle"));
                ServiceOlaviat.add(so.getPropertyAsString("priorityTitle"));
                ServiceDesc.add(so.getPropertyAsString("desc"));
                ServicePrice.add(so.getPropertyAsString("calculatedPrice"));

            }

        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            Log.d("Error", "Some exception occurred", e);
        } catch (XmlPullParserException e) {
            Log.d("Error", "Some exception occurred", e);
        } catch (NetworkOnMainThreadException e) {
            Log.d("Error", "Some exception occurred", e);
        }

    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
