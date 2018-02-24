package com.rahbaran.shirazservice.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
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
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.listerner.EndlessRecyclerViewScrollListener;
import com.rahbaran.shirazservice.R;
import com.rahbaran.shirazservice.listerner.TransTypeOnItemSelectedListener;
import com.rahbaran.shirazservice.Transaction;
import com.rahbaran.shirazservice.adapters.TransactionAdapter;
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

import static com.rahbaran.shirazservice.App.dialog;

public class TransactionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener  {
    public static Handler h;
    private ImageButton ibmenu;
    public static String requestId;
    public  static  TransactionActivity.AsyncCallWS task;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RatingBar ratingbar1;

    private RecyclerView recyclerView;
    EndlessRecyclerViewScrollListener scrollListener;


    private TextView txtEtebar;

    private static LinearLayout filterRemove;

    private ArrayList<String> TransactionType;
    public static ArrayList<String> TransactionTypeID;
    private ArrayList<String> TransactionTime;
    public static ArrayList<String> TransactionTimeID;

    public static String typepos ="";
    public static String  date ="" ;

    public static String timepos ="";
    public static String datepos ="";

    TransactionAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Transaction> array;

    Spinner spinerType;
    Spinner spinerTime;
    ArrayAdapter<String> spinnerArrayAdapterTypes;
    ArrayAdapter<String> spinnerArrayAdapterTimes;
    public static  String typeId = "" , timeId ="";
    private Button txtDate;


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

        setContentView(R.layout.activity_transaction);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);



        ImageButton back1 = (ImageButton)  findViewById(R.id.back_ib);
        LinearLayout back = (LinearLayout)  findViewById(R.id.ll_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout filter = (LinearLayout)findViewById(R.id.ll_filter);
        filterRemove = (LinearLayout)findViewById(R.id.filter_remove);
        ibmenu=(ImageButton) findViewById(R.id.ib_menu);

        recyclerView=(RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new TransactionAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
       // scroll = (ScrollView) findViewById(R.id.scroll);


       if(!typepos.isEmpty() || !timepos.isEmpty() || !datepos.isEmpty()){
            filterRemove.setVisibility(View.VISIBLE);
        }else{
            filterRemove.setVisibility(View.GONE);
        }

        filterRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    typeId="";
                    typepos="";
                    timeId="";
                    timepos="";
                    datepos="";
                    date= "";

                    filterRemove.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(true);
                    array.clear();

                    task = new TransactionActivity.AsyncCallWS();
                    task.execute();                }else {
                    final Dialog dialog = new Dialog(TransactionActivity.this,android.R.style.Theme_Black_NoTitleBar);
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

                                typeId="";
                                typepos="";
                                timeId="";
                                timepos="";
                                datepos="";
                                date= "";

                                filterRemove.setVisibility(View.GONE);

                                swipeRefreshLayout.setRefreshing(true);
                                array.clear();

                                task = new TransactionActivity.AsyncCallWS();
                                task.execute();
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();                    swipeRefreshLayout.setRefreshing(false);
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

            task = new TransactionActivity.AsyncCallWS();
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

                        task = new TransactionActivity.AsyncCallWS();
                        task.execute();


                        dialog.dismiss();
                    }
                }
            });

            dialog.show();            swipeRefreshLayout.setRefreshing(false);
        }
        TransactionType = new ArrayList<String>();
        TransactionTypeID = new ArrayList<String>();
        TransactionType.add("انتخاب کنید");
        TransactionTypeID.add("");


        spinnerArrayAdapterTypes = new ArrayAdapter<String>(
                TransactionActivity.this, R.layout.spinner_item, TransactionType);
        spinnerArrayAdapterTypes.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );



        AsyncCallWSGetType taskType = new AsyncCallWSGetType();
        taskType.execute();



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(MainActivity.this , Filter.class);
               // startActivity(intent);

                dialog = new Dialog(view.getRootView().getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_transaction);

                txtDate = (Button)dialog.findViewById(R.id.btn_date);


                txtDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PersianCalendar now = new PersianCalendar();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                (DatePickerDialog.OnDateSetListener) TransactionActivity.this,
                                now.getPersianYear(),
                                now.getPersianMonth(),
                                now.getPersianDay()
                        );
                        dpd.show(getFragmentManager() , "DatePickerDialog");



                    }
                });




                /////////// AREA /////////

                spinerType = (Spinner) dialog.findViewById(R.id.spinnerType);

                spinerType.setAdapter(spinnerArrayAdapterTypes);
                spinerType.setOnItemSelectedListener(new TransTypeOnItemSelectedListener());

                if(!typepos.isEmpty()){
                    spinerType.setSelection(Integer.parseInt(typeId));
                }




                Button btnFilter = (Button) dialog.findViewById(R.id.btn_filter2) ;

                btnFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        if (mWifi.isConnected() || isMobileDataEnabled()) {
                            swipeRefreshLayout.setRefreshing(true);
                            array.clear();

                            task = new TransactionActivity.AsyncCallWS();
                            task.execute();

                            typepos = typeId;
                            datepos = date;

                            if(!typepos.isEmpty() || !timepos.isEmpty() || !datepos.isEmpty()){
                                filterRemove.setVisibility(View.VISIBLE);
                            }else{
                                filterRemove.setVisibility(View.GONE);
                            }


                            dialog.dismiss();
                        }else {
                            final Dialog dialog2 = new Dialog(TransactionActivity.this,android.R.style.Theme_Black_NoTitleBar);
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

                                        task = new TransactionActivity.AsyncCallWS();
                                        task.execute();

                                        typepos = typeId;
                                        datepos = date;

                                        if(!typepos.isEmpty() || !timepos.isEmpty() || !datepos.isEmpty()){
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
                    task = new TransactionActivity.AsyncCallWS();
                    task.execute();
                }else{
                    final Dialog dialog = new Dialog(TransactionActivity.this,android.R.style.Theme_Black_NoTitleBar);
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
                                task = new TransactionActivity.AsyncCallWS();
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

                    TransactionType.clear();
                    TransactionTypeID.clear();
                    TransactionType.add("انتخاب کنید");
                    TransactionTypeID.add("");


                    AsyncCallWSGetType taskType = new AsyncCallWSGetType();
                    taskType.execute();



                    task = new TransactionActivity.AsyncCallWS();
                    task.execute();
                    swipeRefreshLayout.setRefreshing(true);


                }else {
                    final Dialog dialog = new Dialog(TransactionActivity.this,android.R.style.Theme_Black_NoTitleBar);
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

                                TransactionType.clear();
                                TransactionTypeID.clear();
                                TransactionType.add("انتخاب کنید");
                                TransactionTypeID.add("");


                                AsyncCallWSGetType taskType = new AsyncCallWSGetType();
                                taskType.execute();



                                task = new TransactionActivity.AsyncCallWS();
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
                Intent intent = new Intent(TransactionActivity.this , TransactionActivity.class);
                startActivity(intent);

            }
        });


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(TransactionActivity.this, MainActivity.class);
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
            Intent intent = new Intent(TransactionActivity.this , AfzayeshEtebar.class);
            startActivity(intent);
        }
        });

        raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://shiraz-service.ir/terms/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));            startActivity(intent);
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
                Intent intent = new Intent(TransactionActivity.this , MyServices.class);
                startActivity(intent);
        }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(TransactionActivity.this);
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

                        if(!typepos.isEmpty() || !timepos.isEmpty() || !datepos.isEmpty()){
                            filterRemove.setVisibility(View.VISIBLE);
                        }else{
                            filterRemove.setVisibility(View.GONE);
                        }
                        break;

                }
            }

        };





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

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getWorkmanFinancialTransactions";
        String METHOD_NAME = "getWorkmanFinancialTransactions";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));
            Request.addProperty("typeId", typeId);
            Request.addProperty("insrtDateShamsi", date);


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

                    array.add(new Transaction(result.get(i)));

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





    private class AsyncCallWSGetType extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getType();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



        }

    }

    public void getType() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getFinancialTransactionStates";
        String METHOD_NAME = "getFinancialTransactionStates";
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
                TransactionType.add(so.getPropertyAsString("typeTitle").toString());
                TransactionTypeID.add(so.getPropertyAsString("typeId").toString());
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
        String month = String.valueOf((monthOfYear+1));
        String day = String.valueOf((dayOfMonth));
        if((monthOfYear+1)<10){
             month = "0"+(monthOfYear+1);
        }
        if((dayOfMonth)<10){
             day = "0"+(dayOfMonth);
        }
        date =   year   + (month) + day;
        PersianCalendar now = new PersianCalendar();
        txtDate.setText(" تاریخ " + year + "/" + (month) + "/" + day);


    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time2 =  hourString + ":" + minuteString ;

    }





}







