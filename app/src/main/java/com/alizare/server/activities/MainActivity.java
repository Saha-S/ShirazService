package com.alizare.server.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alizare.server.App;
import com.alizare.server.R;
import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Handler h;
    private LinearLayout container;
    private Button btnSelect , btnFilter;
    private ImageButton ibmenu;
    private TextView tvtitle;
    private static ArrayList<String> ServiceArea;
    private static ArrayList<String> ServiceState;
    private static ArrayList<String> ServiceTitle;
    private ArrayList<String> ServiceFName;
    private ArrayList<String> ServiceLName;
    private ArrayList<String> ServicePhone;
    private ArrayList<String> RequestId;
    private static ArrayList<String> ServiceTime;
    private static ArrayList<String> ServiceDesc;
    private static ArrayList<String> ServicePrice;
    public  static  MainActivity.AsyncCallWS task;
    private ProgressDialog pd;
    public static String requestId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        h = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;

                }
            }

        };



        ServiceTitle = new ArrayList<String>();
        container = (LinearLayout)findViewById(R.id.container);
        btnFilter = (Button)findViewById(R.id.btn_filter);
        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
//        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
     //   tvtitle.setTypeface(tfmorvarid);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        scroll = (ScrollView) findViewById(R.id.scroll);


        ServiceTitle = new ArrayList<String>();
        ServiceState = new ArrayList<String>();
        ServiceArea = new ArrayList<String>();
        RequestId = new ArrayList<String>();

        ServiceFName = new ArrayList<String>();
        ServiceLName = new ArrayList<String>();
        ServicePhone = new ArrayList<String>();
        ServiceTime = new ArrayList<String>();
        ServiceDesc = new ArrayList<String>();
        ServicePrice = new ArrayList<String>();
        container = (LinearLayout)findViewById(R.id.container);

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            swipeRefreshLayout.setRefreshing(true);

            task = new MainActivity.AsyncCallWS();
            task.execute();
        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            swipeRefreshLayout.setRefreshing(false);
        }


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , Filter.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    task = new MainActivity.AsyncCallWS();
                    task.execute();
                    swipeRefreshLayout.setRefreshing(true);


                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }
                container.removeAllViewsInLayout();
                  ServiceTime.clear();
                   ServiceTitle.clear();
                    ServiceArea.clear();



            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Menu nav_Menu = navigationView.getMenu();

        View hView =  navigationView.getHeaderView(0);

        ibmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

            }
        });


        final TextView txtName = (TextView)hView.findViewById(R.id.txt_name);
        final TextView txtEtebar = (TextView)hView.findViewById(R.id.txt_etebar);
        final TextView txtTakhfif = (TextView)hView.findViewById(R.id.txt_takhfif);
        final ImageView img = (ImageView) hView.findViewById(R.id.imageView);

        // final Button btnExit = (Button)hView.findViewById(R.id.nav_gallery);


        SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

        final String fullname = prefs.getString("fullname", "0");

        txtName.setText(fullname);
        txtEtebar.setText("اعتبار فعلی "+prefs.getString("credit", "0")+" ریال");
        txtTakhfif.setText("درصد تخفیف "+prefs.getString("discountPercent", "0")+ " درصد");
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).into(img);



    }
    public class AsyncCallWS extends AsyncTask<Void, Void, Void> {

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
            final LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            for(int i = 0; i< ServiceTitle.size(); i++) {

                final View child = inflater.inflate(R.layout.item_requests, null, false);
                TextView areaName = (TextView) child.findViewById(R.id.txt_area);
                TextView Title = (TextView) child.findViewById(R.id.txt_title);
                TextView Time = (TextView) child.findViewById(R.id.txt_insert_time);
                CardView item = (CardView) child.findViewById(R.id.ll_row);
                Button select = (Button)child.findViewById(R.id.btn_select);



                areaName.setText("محدوده ی "+ServiceArea.get(i));

                Title.setText(ServiceTitle.get(i));

                Time.setText(ServiceTime.get(i));
                container.addView(child);

                item.setOnClickListener(new View.OnClickListener() {
                    public int index;

                    @Override
                    public void onClick(View view) {

                        requestId = RequestId.get(index);
                        index = ((LinearLayout) child.getParent()).indexOfChild(child);
                        Intent intent = new Intent(MainActivity.this, RequestDetails.class);
                        startActivity(intent);

                    }
                });

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:

                                        MainActivity.AsyncCallWSSelectRequest task2 = new MainActivity.AsyncCallWSSelectRequest();
                                        task2.execute();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        int index = ((LinearLayout) child.getParent()).indexOfChild(child);


                        Intent intent = new Intent(MainActivity.this, RequestDetails.class);
                        startActivity(intent);
                    }


                });




            }
            swipeRefreshLayout.setRefreshing(false);

        }

    }

    public void calculate() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getRequestsList";
        String METHOD_NAME = "getRequestsList";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));
            Request.addProperty("serviceTitle", "");
            Request.addProperty("serviceId", "");
            Request.addProperty("serviceCatId", Filter.catId);
            Request.addProperty("serviceSubCatId", Filter.subId);
            Request.addProperty("areaId", Filter.areaId);
            Request.addProperty("priorityId", Filter.olaviatId);
            Request.addProperty("timeFrom", "");
            Request.addProperty("timeTo", "");
            Request.addProperty("desc", "");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            SoapObject response;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            int length = result.size();
            SoapObject soo = result.get(0);

            if(soo.getPropertyAsString("res").toString().equals("0")) {

                for (int i = 0; i < 1; ++i) {
                    SoapObject so = result.get(i);
                    ServiceTitle.add(so.getPropertyAsString("serviceTitle").toString());
                    ServiceArea.add(so.getPropertyAsString("areaTitle").toString());
                    ServiceState.add(so.getPropertyAsString("stateTitle").toString());
                    ServiceTime.add(so.getPropertyAsString("insrtTimePersian").toString());
                    ServiceDesc.add(so.getPropertyAsString("desc").toString());
                    ServicePrice.add(so.getPropertyAsString("calculatedPrice").toString());
                    RequestId.add(so.getPropertyAsString("requestId").toString());


                }

            }

            if(soo.getPropertyAsString("res").toString().equals("-1")) {
                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                App.CustomToast("درخواست سرویسی یافت نشد");
                            }
                        });
                    }
                }.start();


            }
            if(soo.getPropertyAsString("res").toString().equals("-2")) {
                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                App.CustomToast("سرویس دهنده مد نظر یافت نشد");
                            }
                        });
                    }
                }.start();
            }
            if(soo.getPropertyAsString("res").toString().equals("-1000")) {
                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                App.CustomToast("کلید تبادل درج شده نا معتبر است");
                            }
                        });
                    }
                }.start();



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


    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("آیا میخواهید خارج شوید؟").setPositiveButton("بله", dialogClickListener)
                .setNegativeButton("خیر", dialogClickListener).show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_afzayesh_etebar) {
            Intent intent = new Intent(MainActivity.this , AfzayeshEtebar.class);
            startActivity(intent);
        } else if (id == R.id.nav_ghavanin) {
            Intent intent = new Intent(MainActivity.this , Ghavanin.class);
            startActivity(intent);


        } else if (id == R.id.nav_tamas) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+"+۹۸۹۱۷۷۱۸۹۷۳۰"));
            startActivity(intent);


        } else if (id == R.id.nav_my_service) {
            Intent intent = new Intent(MainActivity.this , MyServices.class);
            startActivity(intent);


        }
        else if (id == R.id.exit) {
finish();

        }


        return true;
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

    private class AsyncCallWSSelectRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            selectService();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    public void selectService() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#pickRequestByServiceMan";
        String METHOD_NAME = "pickRequestByServiceMan";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("requestId", MainActivity.requestId);
            Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));
            //Request.addProperty("Celsius", getCel);

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

            SoapObject result = (SoapObject) envelope.getResponse();

            final String res = result.getPropertyAsString("res");
            new Thread()
            {
                public void run()
                {
                    MainActivity.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(res.equals("-1000")){
                                App.CustomToast("کلید تبادل درج شده اشتباه است");
                            }
                            if(res.equals("-1")){
                                App.CustomToast("سرویس دهنده مورد نظر یافت نشد");
                            }
                            if(res.equals("-2")){
                                App.CustomToast("درخواست مد نظر یافت نشد");
                            }
                            if(res.equals("-3")){
                                App.CustomToast("وضعیت این سرویس تغییر کرده است. امکان انتخاب وجود ندارد");
                            }
                            if(res.equals("-4")){
                                App.CustomToast("اعتبار کافی جهت دریافت این درخواست را ندارید");
                            }
                            if(res.equals("-5")){
                                App.CustomToast("در ثبت اطلاعات خطایی رخ داده است");
                            }
                            if(res.equals("0")){
                                App.CustomToast("اطلاعات با موفقیت ثبت شد");
                                Intent intent = new Intent(MainActivity.this, MyServices.class);
                                startActivity(intent);

                            }






                        }
                    });
                }
            }.start();



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

}







