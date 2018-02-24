package com.rahbaran.shirazservice.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.rahbaran.shirazservice.Request;
import com.rahbaran.shirazservice.adapters.RequestAdapter;
import com.rahbaran.shirazservice.listerner.AreaOnItemSelectedListener;
import com.rahbaran.shirazservice.listerner.CatOnItemSelectedListener;
import com.rahbaran.shirazservice.listerner.EndlessRecyclerViewScrollListener;
import com.rahbaran.shirazservice.listerner.OlaviatOnItemSelectedListener;
import com.rahbaran.shirazservice.listerner.SubOnItemSelectedListener;
import com.rahbaran.shirazservice.listerner.TimeOnItemSelectedListener;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {
    public static Handler h;
    public static Handler h2;
    private Button from , to;
    private ImageButton ibmenu;
    private TextView tvtitle;
    public static String requestId;
    public static String stitle , cat, area, time, desc , stat , olaviat, price, timeDesc , dateFromPersian  , dateToPersian ;
    public  static  MainActivity.AsyncCallWS task;
    public  static  MainActivity.AsyncCallWSsaveTokenKey Tokentask;
    private ProgressDialog pd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scroll;
    private RatingBar ratingbar1;
    private static LinearLayout llSub;
    private static LinearLayout filterRemove;




    private RecyclerView recyclerView;
    EndlessRecyclerViewScrollListener scrollListener;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;
    private TextView txtEtebar;


    private ArrayList<String> ServiceArea;
    public static ArrayList<String> ServiceAreaID;
    private ArrayList<String> ServiceCats;
    public static ArrayList<String> ServiceCatsID;
    private ArrayList<String> ServiceOlaviat;
    public static ArrayList<String> ServiceOlaviatID;
    private ArrayList<String> ServiceTime;
    public static ArrayList<String> ServiceTimeID;
    public static ArrayList<String> ServiceSub;
    public static ArrayList<String> ServiceSubId;

    public static String catpos ="";

    public static String catposf ="";
    public static String areposf ="";
    public static String olaviatposf ="";
    public static String timeposf ="";

    RequestAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Request> array;

    String sfrom="", sto= "";

    Spinner spiner;
    static Spinner spinerSub;
    Spinner spinerCat ,spinerCatOlaviat ;
    ArrayAdapter<String> spinnerArrayAdapterCats;
    ArrayAdapter<String> spinnerArrayAdapterOlaviat;
    ArrayAdapter<String> spinnerArrayAdapterTime;
    ArrayAdapter<String> spinnerArrayAdapterArea;
    static ArrayAdapter<String> spinnerArrayAdapterSub;
    public static  String areaId = "" , catId ="", olaviatId  = "" ,subId  = "" , timeId= "";
    private Dialog dialog;

    private Handler handler = new Handler();


    static Button line;
    private Spinner spinerTime;
    private String date;

    String flag;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //call this before super.onCreate
    private void forceRtlIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);




        LinearLayout filter = (LinearLayout)findViewById(R.id.ll_filter);


        filterRemove = (LinearLayout)findViewById(R.id.filter_remove);
        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "mjdalalst.ttf");
        tvtitle.setTypeface(face);

        recyclerView=(RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new RequestAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
       // scroll = (ScrollView) findViewById(R.id.scroll);


        if(!areposf.isEmpty() || !catposf.isEmpty() || !olaviatposf.isEmpty() || !timeposf.isEmpty() || !sfrom.isEmpty() || !sto.isEmpty()){
            filterRemove.setVisibility(View.VISIBLE);
        }else{
            filterRemove.setVisibility(View.GONE);
        }

        filterRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    catId="";
                    catpos="";
                    areaId="";
                    olaviatId="";
                    subId="";
                    timeId="";
                    timeposf="";

                    areposf="";
                    catposf="";
                    olaviatposf="";

                    sfrom="";
                    sto="";
                    filterRemove.setVisibility(View.GONE);
                    //  adapter.filter(areaId,catId,subId,olaviatId);
                    swipeRefreshLayout.setRefreshing(true);
                    array.clear();

                    task = new MainActivity.AsyncCallWS();
                    task.execute();
                }else {
                    final Dialog dialog = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.internetdialog);



                    Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                    Button no = (Button) dialog.findViewById(R.id.dialog_no);
                    // if button is clicked, close the custom dialog
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            if (mWifi.isConnected() || isMobileDataEnabled()) {


                                catId = "";
                                catpos = "";
                                areaId = "";
                                olaviatId = "";
                                subId = "";
                                timeId = "";
                                timeposf = "";

                                areposf = "";
                                catposf = "";
                                olaviatposf = "";

                                sfrom = "";
                                sto = "";
                                filterRemove.setVisibility(View.GONE);
                                //  adapter.filter(areaId,catId,subId,olaviatId);
                                swipeRefreshLayout.setRefreshing(true);
                                array.clear();

                                task = new MainActivity.AsyncCallWS();
                                task.execute();

                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();

                    swipeRefreshLayout.setRefreshing(false);
                }





            }
        });

///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////


        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            swipeRefreshLayout.setRefreshing(true);

            task = new MainActivity.AsyncCallWS();
            task.execute();
        }else {
            final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.internetdialog);



            Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
            Button no = (Button) dialog.findViewById(R.id.dialog_no);
            // if button is clicked, close the custom dialog
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {

                        swipeRefreshLayout.setRefreshing(true);

                        task = new MainActivity.AsyncCallWS();
                        task.execute();

                        dialog.dismiss();
                    }
                }
            });

            dialog.show();            swipeRefreshLayout.setRefreshing(false);
        }


        ///////////////// ّFILTER ///////////////


        ServiceArea = new ArrayList<String>();
        ServiceAreaID = new ArrayList<String>();
        ServiceArea.add("انتخاب کنید");
        ServiceAreaID.add("");

        ServiceCats = new ArrayList<String>();
        ServiceCatsID = new ArrayList<String>();
        ServiceCats.add("انتخاب کنید");
        ServiceCatsID.add("");

        ServiceOlaviat = new ArrayList<String>();
        ServiceOlaviatID = new ArrayList<String>();
        ServiceOlaviat.add("انتخاب کنید");
        ServiceOlaviatID.add("");

        ServiceSub = new ArrayList<String>();
        ServiceSubId = new ArrayList<String>();
        ServiceSub.add("انتخاب کنید");
        ServiceSubId.add("");

        ServiceTime = new ArrayList<String>();
        ServiceTimeID = new ArrayList<String>();
        ServiceTime.add("انتخاب کنید");
        ServiceTime.add("8 تا 12");
        ServiceTime.add("12 تا 16");
        ServiceTime.add("16 تا 20");
        ServiceTimeID.add("");
        ServiceTimeID.add("1");
        ServiceTimeID.add("2");
        ServiceTimeID.add("3");

        spinnerArrayAdapterArea = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item, ServiceArea);
        spinnerArrayAdapterArea.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerArrayAdapterCats = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item, ServiceCats);
        spinnerArrayAdapterCats.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerArrayAdapterOlaviat = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item, ServiceOlaviat);
        spinnerArrayAdapterOlaviat.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerArrayAdapterTime = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item, ServiceTime);
        spinnerArrayAdapterTime.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerArrayAdapterSub = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_item, ServiceSub);
        spinnerArrayAdapterSub.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );


        MainActivity.AsyncCallWSGetAreas taska = new MainActivity.AsyncCallWSGetAreas();
        taska.execute();
        MainActivity.AsyncCallWSGetCat taskCats = new MainActivity.AsyncCallWSGetCat();
        taskCats.execute();
        MainActivity.AsyncCallWSGetOlaviat taskOlaviat = new MainActivity.AsyncCallWSGetOlaviat();
        taskOlaviat.execute();



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(MainActivity.this , Filter.class);
               // startActivity(intent);

                dialog = new Dialog(view.getRootView().getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterdialog);


                llSub = (LinearLayout) dialog.findViewById(R.id.ll_sub) ;
                from = (Button)dialog.findViewById(R.id.btn_datefrom);
                to = (Button)dialog.findViewById(R.id.btn_dateto);


                from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersianCalendar now = new PersianCalendar();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                (DatePickerDialog.OnDateSetListener) MainActivity.this,
                                now.getPersianYear(),
                                now.getPersianMonth(),
                                now.getPersianDay()
                        );
                        dpd.show(getFragmentManager() , "DatePickerDialog");

                        flag="from";


                    }
                });
                to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersianCalendar now = new PersianCalendar();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                (DatePickerDialog.OnDateSetListener) MainActivity.this,
                                now.getPersianYear(),
                                now.getPersianMonth(),
                                now.getPersianDay()
                        );
                        dpd.show(getFragmentManager() , "DatePickerDialog");
                        flag="to";

                    }
                });


                /////////// AREA /////////

                spiner = (Spinner) dialog.findViewById(R.id.spinner);

                spiner.setAdapter(spinnerArrayAdapterArea);
                spiner.setOnItemSelectedListener(new AreaOnItemSelectedListener());

                if(!areposf.isEmpty()){
                    spiner.setSelection(Integer.parseInt(areaId));
                }

                ////////// CATS //////
                spinerCat = (Spinner) dialog.findViewById(R.id.spinnerCat);

                spinerCat.setAdapter(spinnerArrayAdapterCats);
                spinerCat.setOnItemSelectedListener(new CatOnItemSelectedListener());

                if(!catposf.isEmpty()){
                    spinerCat.setSelection(Integer.parseInt(catpos));
                }



                //////////// OLAVIAT //////

                spinerCatOlaviat = (Spinner) dialog.findViewById(R.id.spinnerOlaviat);

                spinerCatOlaviat.setAdapter(spinnerArrayAdapterOlaviat);
                spinerCatOlaviat.setOnItemSelectedListener(new OlaviatOnItemSelectedListener());

                if(!olaviatposf.isEmpty()){
                    spinerCatOlaviat.setSelection(Integer.parseInt(olaviatId));
                }

                //////////// TIME //////////

                spinerTime = (Spinner) dialog.findViewById(R.id.spinnerTime);

                spinerTime.setAdapter(spinnerArrayAdapterTime);
                spinerTime.setOnItemSelectedListener(new TimeOnItemSelectedListener());

                if(!timeposf.isEmpty()){
                    spinerTime.setSelection(Integer.parseInt(timeId));
                }



                ////////// SUB ////////////
                spinerSub = (Spinner) dialog.findViewById(R.id.spinnerSubCat);

                spinerSub.setAdapter(spinnerArrayAdapterSub);
                spinerSub.setOnItemSelectedListener(new SubOnItemSelectedListener());






////////////////////////////////////////////////



              //  dialog.setTitle("فیلتر");

                // set the custom dialog components - text, image and button
                Button btnFilter = (Button) dialog.findViewById(R.id.btn_filter2) ;
                line = (Button) dialog.findViewById(R.id.line) ;
             //   line = (Button) dialog.findViewById(R.id.line) ;


                llSub = (LinearLayout) dialog.findViewById(R.id.ll_sub) ;
                //  title.setText("افزایش اعتبار");

                // if button is clicked, close the custom dialog
                btnFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                       // adapter.filter(areaId,catId,subId,olaviatId);


                        if (mWifi.isConnected() || isMobileDataEnabled()) {
                            swipeRefreshLayout.setRefreshing(true);
                            array.clear();

                            task = new MainActivity.AsyncCallWS();
                            task.execute();

                            recyclerView.getRecycledViewPool().clear();
                            adapter.notifyDataSetChanged();


                            areposf = areaId;
                            catposf = catId;
                            olaviatposf = olaviatId;
                            timeposf = timeId;

                            if(!areposf.isEmpty() || !catposf.isEmpty() || !olaviatposf.isEmpty() || !timeposf.isEmpty() || !sfrom.isEmpty()|| !sto.isEmpty()){
                                filterRemove.setVisibility(View.VISIBLE);
                            }else{
                                filterRemove.setVisibility(View.GONE);
                            }


                            dialog.dismiss();



                        }else {
                            final Dialog dialog2 = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog2.setContentView(R.layout.internetdialog);



                            Button yes = (Button) dialog2.findViewById(R.id.dialog_yes);
                            Button no = (Button) dialog2.findViewById(R.id.dialog_no);
                            // if button is clicked, close the custom dialog
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                    final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                    if (mWifi.isConnected() || isMobileDataEnabled()) {

                                        swipeRefreshLayout.setRefreshing(true);
                                        array.clear();

                                        task = new MainActivity.AsyncCallWS();
                                        task.execute();

                                        recyclerView.getRecycledViewPool().clear();
                                        adapter.notifyDataSetChanged();


                                        areposf = areaId;
                                        catposf = catId;
                                        olaviatposf = olaviatId;
                                        timeposf = timeId;

                                        if(!areposf.isEmpty() || !catposf.isEmpty() || !olaviatposf.isEmpty() || !timeposf.isEmpty() || !sfrom.isEmpty()|| !sto.isEmpty()){
                                            filterRemove.setVisibility(View.VISIBLE);
                                        }else{
                                            filterRemove.setVisibility(View.GONE);
                                        }


                                        dialog.dismiss();
                                        dialog2.dismiss();
                                    }
                                }
                            });

                            dialog2.show();
                            swipeRefreshLayout.setRefreshing(false);
                        }





                    }
                });


                dialog.show();

            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    swipeRefreshLayout.setRefreshing(true);
                    task = new MainActivity.AsyncCallWS();
                    task.execute();
                }else{
                    final Dialog dialog = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.internetdialog);



                    Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                    Button no = (Button) dialog.findViewById(R.id.dialog_no);
                    // if button is clicked, close the custom dialog
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            if (mWifi.isConnected() || isMobileDataEnabled()) {

                                swipeRefreshLayout.setRefreshing(true);
                                task = new MainActivity.AsyncCallWS();
                                task.execute();

                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    array.clear();

                    ServiceOlaviat.clear();
                    ServiceOlaviatID.clear();

                    ServiceArea.clear();
                    ServiceAreaID.clear();

                    ServiceCats.clear();
                    ServiceCatsID.clear();

                    ServiceSub.clear();
                    ServiceSubId.clear();

                    ServiceArea.add("انتخاب کنید");
                    ServiceAreaID.add("");

                    ServiceCats.add("انتخاب کنید");
                    ServiceCatsID.add("");

                    ServiceOlaviat.add("انتخاب کنید");
                    ServiceOlaviatID.add("");

                    ServiceSub.add("انتخاب کنید");
                    ServiceSubId.add("");

                    ServiceTime.add("انتخاب کنید");
                    ServiceTime.add("8 تا 12");
                    ServiceTime.add("12 تا 16");
                    ServiceTime.add("16 تا 20");
                    ServiceTimeID.add("");
                    ServiceTimeID.add("1");
                    ServiceTimeID.add("2");
                    ServiceTimeID.add("3");



                    MainActivity.AsyncCallWSGetAreas taska = new MainActivity.AsyncCallWSGetAreas();
                    taska.execute();
                    MainActivity.AsyncCallWSGetCat taskCats = new MainActivity.AsyncCallWSGetCat();
                    taskCats.execute();
                    MainActivity.AsyncCallWSGetOlaviat taskOlaviat = new MainActivity.AsyncCallWSGetOlaviat();
                    taskOlaviat.execute();


                    task = new MainActivity.AsyncCallWS();
                    task.execute();
                    swipeRefreshLayout.setRefreshing(true);


                }else {
                    final Dialog dialog = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.internetdialog);



                    Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                    Button no = (Button) dialog.findViewById(R.id.dialog_no);
                    // if button is clicked, close the custom dialog
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            if (mWifi.isConnected() || isMobileDataEnabled()) {

                                array.clear();

                                ServiceOlaviat.clear();
                                ServiceOlaviatID.clear();

                                ServiceArea.clear();
                                ServiceAreaID.clear();

                                ServiceCats.clear();
                                ServiceCatsID.clear();

                                ServiceSub.clear();
                                ServiceSubId.clear();

                                ServiceArea.add("انتخاب کنید");
                                ServiceAreaID.add("");

                                ServiceCats.add("انتخاب کنید");
                                ServiceCatsID.add("");

                                ServiceOlaviat.add("انتخاب کنید");
                                ServiceOlaviatID.add("");

                                ServiceSub.add("انتخاب کنید");
                                ServiceSubId.add("");

                                ServiceTime.add("انتخاب کنید");
                                ServiceTime.add("8 تا 12");
                                ServiceTime.add("12 تا 16");
                                ServiceTime.add("16 تا 20");
                                ServiceTimeID.add("");
                                ServiceTimeID.add("1");
                                ServiceTimeID.add("2");
                                ServiceTimeID.add("3");



                                MainActivity.AsyncCallWSGetAreas taska = new MainActivity.AsyncCallWSGetAreas();
                                taska.execute();
                                MainActivity.AsyncCallWSGetCat taskCats = new MainActivity.AsyncCallWSGetCat();
                                taskCats.execute();
                                MainActivity.AsyncCallWSGetOlaviat taskOlaviat = new MainActivity.AsyncCallWSGetOlaviat();
                                taskOlaviat.execute();


                                task = new MainActivity.AsyncCallWS();
                                task.execute();
                                swipeRefreshLayout.setRefreshing(true);



                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();                    swipeRefreshLayout.setRefreshing(false);
                }

                scrollListener.resetState();


            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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


        final TextView txtName = (TextView)findViewById(R.id.txt_name);
        txtEtebar = (TextView)findViewById(R.id.txt_etebar);
        final TextView txtTakhfif = (TextView)findViewById(R.id.txt_takhfif);
        final CircleImageView img = (CircleImageView) findViewById(R.id.imageView);

        final TableRow credit = (TableRow) findViewById(R.id.credit);
        final TableRow raw = (TableRow) findViewById(R.id.raw);
        final TableRow call = (TableRow) findViewById(R.id.call);
        final TableRow myservice = (TableRow) findViewById(R.id.myservice);
        final TableRow exit = (TableRow) findViewById(R.id.exit);
        final ImageView telegram = (ImageView) findViewById(R.id.telegram);
        final ImageView instagram = (ImageView) findViewById(R.id.instagram);
        final ImageView website = (ImageView) findViewById(R.id.website);
        final TableRow list = (TableRow) findViewById(R.id.requests);
        final TableRow transaction = (TableRow) findViewById(R.id.transaction);

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , TransactionActivity.class);
                startActivity(intent);

            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(browserIntent);

            }
        });


        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="tg://resolve?domain="+App.telegram;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = App.instagram;

                Uri uri = Uri.parse("http://instagram.com/_u/"+ url);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/"+ url)));
                }

            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(App.website));
                startActivity(browserIntent);

            }
        });

        final TextView version  = (TextView)findViewById(R.id.version);
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText("نسخه "+  App.versionName);


        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this , AfzayeshEtebar.class);
            startActivity(intent);
        }
        });

        raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://shiraz-service.ir/terms/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
        }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+App.mobile));
            startActivity(intent);
        }
        });
        myservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , MyServices.class);
                startActivity(intent);
        }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.exitdialog);

                Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                Button no = (Button) dialog.findViewById(R.id.dialog_no);
                TextView txt = (TextView) dialog.findViewById(R.id.dialog_txt2);
                txt.setText("کاربر گرامی آیا مایل به خروج از حساب هستید؟");
                // if button is clicked, close the custom dialog
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });



        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);

        final SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

        final String rating  = prefs.getString("rating", "0");


        ratingbar1.setRating(Float.parseFloat(rating));




        final String fullname = prefs.getString("fullname", "0");

        txtName.setText(fullname);

        Locale farsi = new Locale("fa", "IR");
        NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

        String c = numberFormatDutch.format(new BigDecimal(prefs.getString("credit", "0")));
        final String cc = c.replace("ریال", "" + "\u200e");

        txtEtebar.setText("اعتبار "+cc+" ریال");

        String discountPercent = prefs.getString("discountPercent", "0").toString();
        txtTakhfif.setText("تخفیف "+discountPercent+ " %");
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).placeholder(R.drawable.profile)
                .into(img);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.item_requests, null); //log.xml is your file.

        Button select = (Button)vi.findViewById(R.id.btn_select);


        h = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;

                    case 1:
                        Locale farsi = new Locale("fa", "IR");
                        NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                        String c = numberFormatDutch.format(new BigDecimal(prefs.getString("credit", "0")));
                        final String cc = c.replace("ریال", "" + "\u200e");
                        txtEtebar.setText("اعتبار "+cc+" ریال");
                        break;

                    case 2:

                        if(!catId.isEmpty() || !areaId.isEmpty() || !olaviatId.isEmpty()){
                            filterRemove.setVisibility(View.VISIBLE);
                        }else{
                            filterRemove.setVisibility(View.GONE);
                        }
                        break;

                }
            }

        };


        ////////////////////////// NOTIFICATION ///////////////////

        if (getIntent().getExtras() != null) {
            if(!prefs.getString("servicemanId", "00").toString().equals("00")) {
                for (String key : getIntent().getExtras().keySet()) {
                    String value = getIntent().getExtras().getString(key);

                    if (key.equals("mod") && (value.equals("Confirm"))) {
                        Intent intent = new Intent(this, MyServicesDetailes.class);
                        intent.putExtra("value", value);
                        MyServices.state = "4";
                        intent.putExtra("PUSH", "push");

                        startActivity(intent);
                        finish();

                    }
                    if (key.equals("mod") && value.equals("Reject")) {
                        Intent intent = new Intent(this, MyServicesDetailes.class);
                        intent.putExtra("value", value);
                        MyServices.state = "3";
                        intent.putExtra("PUSH", "push");

                        startActivity(intent);
                        finish();

                    }
//                if (key.equals("mod") && ( value.equals("Pickup")  )) {
//                    Intent intent = new Intent(this, MyServicesDetailes.class);
//                    intent.putExtra("value", value);
//                    MyServices.state="2";
//                    intent.putExtra("PUSH", "push");
//
//                    startActivity(intent);
//                    finish();
//
//                }

                    if (key.equals("mod") && (value.equals("Cancel"))) {
                        Intent intent = new Intent(this, RequestDetails.class);
                        intent.putExtra("value", value);
                        // MyServices.state="3";
                        intent.putExtra("PUSH", "push");

                        startActivity(intent);
                        finish();

                    }
                    if (key.equals("mod") && (value.equals("New"))) {
                        Intent intent = new Intent(this, RequestDetails.class);
                        intent.putExtra("value", value);
                        // MyServices.state="100";
                        intent.putExtra("PUSH", "push");

                        startActivity(intent);
                        finish();

                    }
                    if (key.equals("mod") && (value.equals("Return"))) {
                        Intent intent = new Intent(this, RequestDetails.class);
                        intent.putExtra("value", value);
                        // MyServices.state="100";
                        intent.putExtra("PUSH", "push");

                        startActivity(intent);
                        finish();

                    }


                    if (key.equals("mod") && (value.equals("Notice"))) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }


                }
            }
        }

        subscribeToPushService();




    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("AndroidBash", "Subscribed");

        token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("AndroidBash", token);
        Tokentask = new AsyncCallWSsaveTokenKey();
        Tokentask.execute();

    }

//////////////////////////////////////////////////

    public class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getRequestsList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            swipeRefreshLayout.setRefreshing(false);

        }

    }


    public void getRequestsList() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getRequestsList";
        String METHOD_NAME = "getRequestsList";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");

            Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));
            Request.addProperty("serviceTitle", "");
            Request.addProperty("serviceId", "");
            Request.addProperty("serviceCatId", MainActivity.catId);
            Request.addProperty("serviceSubCatId", MainActivity.subId);
            Request.addProperty("areaId", MainActivity.areaId);
            Request.addProperty("priorityId", MainActivity.olaviatId);
            Request.addProperty("dateFrom", sfrom);
            Request.addProperty("dateTo", sto);
            Request.addProperty("time", timeId);
            Request.addProperty("desc", "");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            SoapObject response;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            final Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            final SoapObject soo = result.get(0);
            final int length = result.size();

/////////////////////////////
            new Thread() {
                public void run() {

                    if(soo.getPropertyAsString("res").toString().equals("0")) {
                for (int i = 0; i < length; ++i) {

                    SoapObject so = result.get(i);

                    array.add(new Request(result.get(i)));

                }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//stuff that updates ui
                                adapter.update(array);
                                swipeRefreshLayout.setRefreshing(false);


                            }
                        });

            }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                App.CustomToast(soo.getPropertyAsString("resStr"));
                                swipeRefreshLayout.setRefreshing(false);


                            }
                        });

                    }
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


    @Override
    public void onBackPressed() {


        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exitdialog);



        Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
        Button no = (Button) dialog.findViewById(R.id.dialog_no);
        // if button is clicked, close the custom dialog
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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





    ////////////////////////// TOKENKEY ///////////////////////////
    private class AsyncCallWSsaveTokenKey  extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveTokenKey();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    public void saveTokenKey() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#saveTokenKey";
        String METHOD_NAME = "saveTokenKey";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);


            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("personId", prefs.getString("servicemanId", "0"));
            Request.addProperty("deviceTokenKey",token);
            //Request.addProperty("Celsius", getCel);

            Log.i("sssssssss" , token);
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


    //////////////////////////////// FILTER /////////////////////////////


    private class AsyncCallWSGetAreas extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAreas();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



        }

    }

    public void getAreas() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getAreas";
        String METHOD_NAME = "getAreas";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            int length = result.size();
            for (int i = 0; i < length; ++i) {
                SoapObject so = result.get(i);
                ServiceArea.add(so.getPropertyAsString("areaTitle").toString());
                ServiceAreaID.add(so.getPropertyAsString("areaId").toString());
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


    private class AsyncCallWSGetCat extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getCat();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Filter.AsyncCallWSGetSubCat taskSub = new Filter.AsyncCallWSGetSubCat();
            //  taskSub.execute();





        }

    }

    public void getCat() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getServicesCats";
        String METHOD_NAME = "getServicesCats";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("catId", "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            int length = result.size();




            for (int i = 0; i < length; ++i) {
                SoapObject so = result.get(i);
                if (!ServiceCats.contains(so.getPropertyAsString("catTitle"))){
                    ServiceCats.add(so.getPropertyAsString("catTitle").toString());
                    ServiceCatsID.add(so.getPropertyAsString("catId").toString());


                }

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

    private class AsyncCallWSGetSubCat extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
           // MainActivity.subpos= "";
            getSubCat();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {




        }

    }

    public static void getSubCat() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getServicesCats";
        String METHOD_NAME = "getServicesCats";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";


        try {


            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("catId", catId);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            final Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            final int length = result.size();
            final SoapObject soo = result.get(0);




            if(!catId.equals("")) {
                if (soo.getPropertyAsString("res").equals("0")) {
                    llSub.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);

                    for (int i = 0; i < length; ++i) {
                        SoapObject so = result.get(i);
                        ServiceSub.add(so.getPropertyAsString("subCatTitle").toString());
                        ServiceSubId.add(so.getPropertyAsString("subCatId").toString());


                    }


                    spinerSub.setAdapter(spinnerArrayAdapterSub);
                    spinerSub.setOnItemSelectedListener(new SubOnItemSelectedListener());


                }
                else{
                    llSub.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    subId="";

                }

            }
            else{
                llSub.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                subId="";
            }

/*
                        }
                    });
                }
            }.start();

*/

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


    private class AsyncCallWSGetOlaviat extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getOlaviat();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



        }

    }

    public void getOlaviat() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getPriorityStates";
        String METHOD_NAME = "getPriorityStates";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";


        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(Request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.debug = true;

            System.setProperty("http.keepAlive", "false");

            androidHttpTransport.call(SOAP_ACTION, envelope);

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            int length = result.size();



            for (int i = 0; i < length; ++i) {
                SoapObject so = result.get(i);
                ServiceOlaviat.add(so.getPropertyAsString("priorityTitle").toString());
                ServiceOlaviatID.add(so.getPropertyAsString("priorityId").toString());
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // Note: monthOfYear is 0-indexed
        date =   year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

        PersianCalendar now = new PersianCalendar();
        if(flag.equals("from")) {
            from.setText("از تاریخ " + date);
            sfrom = String.valueOf(now.getTimeInMillis());

            now.setPersianDate(year,(monthOfYear ), dayOfMonth);
            sfrom = String.valueOf(now.getTimeInMillis());

            date = "";
        }
        if(flag.equals("to")) {
            to.setText("تا تاریخ " + date);
            now.setPersianDate(year,(monthOfYear ), dayOfMonth);
            sto = String.valueOf(now.getTimeInMillis());
            date = "";
        }

        flag="";


    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time2 =  hourString + ":" + minuteString ;

    }



}







