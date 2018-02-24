package com.rahbaran.shirazservice.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RequestDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtRules;
    private String fullText;
    LinearLayout ll;
    Button select;
    private TextView txtEtebar;
    private TextView cat;
    private TextView area;
    private TextView time;
    private TextView state;
    private TextView olaviat;
    private TextView price;
    private TextView timeDesc;
    private TextView dateFromPersian;
    private TextView dateToPersian;
    private TextView title;
    private TextView desc;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        View view = this.findViewById(android.R.id.content).getRootView();
        App.dialog = new Dialog(view.getRootView().getContext());
        App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        App.dialog.setCancelable(false);
        App.dialog.setContentView(R.layout.loading);

        ImageButton back1 = (ImageButton)  findViewById(R.id.back_ib);
        LinearLayout back = (LinearLayout)  findViewById(R.id.ll_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    finish();
                } else {
                    String newString= extras.getString("PUSH");
                    if(newString.equals("push")){
                        Intent intent = new Intent(RequestDetails.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }

            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    finish();
                } else {
                    String newString= extras.getString("PUSH");
                    if(newString.equals("push")){
                        Intent intent = new Intent(RequestDetails.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        TextView tvtitle=(TextView) findViewById(R.id.title);

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
        final TableRow transaction = (TableRow) findViewById(R.id.transaction);

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetails.this , TransactionActivity.class);
                startActivity(intent);

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
                Intent intent = new Intent(RequestDetails.this , AfzayeshEtebar.class);
                startActivity(intent);
            }
        });

        raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://shiraz-service.ir/terms/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));                startActivity(intent);
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
                Intent intent = new Intent(RequestDetails.this , MyServices.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RequestDetails.this);
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
        final TableRow list = (TableRow) findViewById(R.id.requests);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(RequestDetails.this, MainActivity.class);
                startActivity(browserIntent);

            }
        });



        RatingBar ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);

        SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

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
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).placeholder(R.drawable.profile).into(img);

////////////////////////////////////////////////////////



        txtRules = (TextView) findViewById(R.id.txtRules);
        ll = (LinearLayout) findViewById(R.id.ll_mysrvices);
        select = (Button) findViewById(R.id.btn_select);

        ll.setVisibility(View.GONE);
        select.setVisibility(View.GONE);

        title = (TextView) findViewById(R.id.txt_title);
        desc = (TextView) findViewById(R.id.txt_desc);

        cat = (TextView) findViewById(R.id.txt_cat_subcat);
        area = (TextView) findViewById(R.id.txt_area);
        time = (TextView) findViewById(R.id.txt_time);
        state = (TextView) findViewById(R.id.txt_state);
        olaviat = (TextView) findViewById(R.id.txt_olaviat);
        price = (TextView) findViewById(R.id.txt_price);
        timeDesc = (TextView) findViewById(R.id.txt_timeDesc);
        dateFromPersian  = (TextView) findViewById(R.id.dateFromPersian);
        dateToPersian  = (TextView) findViewById(R.id.dateToPersian);



        App.dialog.show();
        RequestDetails.AsyncCallGetInfo taskInfo = new RequestDetails.AsyncCallGetInfo();
        taskInfo.execute();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    /////////////////// web servise /////////////////////


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

            final SoapObject result = (SoapObject) envelope.getResponse();

            final String res = result.getPropertyAsString("res");
            new Thread()
            {
                public void run()
                {
                    RequestDetails.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if(res.equals("0")){
                                App.CustomToast("اطلاعات با موفقیت ثبت شد");
                                Intent intent = new Intent(RequestDetails.this, MyServices.class);

                                SharedPreferences.Editor editor = getSharedPreferences("INFO", MODE_PRIVATE).edit();
                                editor.putString("credit", result.getPropertyAsString("newCredit"));
                                editor.commit();

                                MainActivity.h.sendEmptyMessage(1);

                                Locale farsi = new Locale("fa", "IR");
                                NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                                String c = numberFormatDutch.format(new BigDecimal(result.getPropertyAsString("newCredit")));
                                final String cc = c.replace("ریال", "" + "\u200e");

                                txtEtebar.setText("اعتبار "+cc+" ریال");

                                startActivity(intent);
                                finish();

                            }
                            else{
                                App.CustomToast(result.getPropertyAsString("resStr"));
                            }



                            App.dialog.dismiss();



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
    private class AsyncCallGetInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }


    public void getInfo() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getRequestInfo";
        String METHOD_NAME = "getRequestInfo";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui

       // Log.i("lllllllll" , MainActivity.requestId);

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);
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

            final SoapObject result = (SoapObject) envelope.getResponse();


            new Thread()
            {
                public void run()
                {
                    RequestDetails.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Log.i("lllllllll22" , result.getPropertyAsString("serviceTitle")+"--");

                            title.setText(Html.fromHtml("<b>عنوان درخواست: </b>"+ result.getPropertyAsString("serviceTitle")));
                            cat.setText(Html.fromHtml("<b> گروه و زیر گروه:</b> "+ result.getPropertyAsString("catTitle")+" - "+result.getPropertyAsString("subCatTitle")) );
                            area.setText(Html.fromHtml("<b>محدوده: </b>"+result.getPropertyAsString("areaTitle")));
                            state.setText(Html.fromHtml("<b>وضعیت درخواست:</b> "+ result.getPropertyAsString("stateTitle")));
                            time.setText(Html.fromHtml("<b>تاریخ ثبت: </b>"+result.getPropertyAsString("insrtTimePersian")));
                            olaviat.setText(Html.fromHtml("<b>اولویت درخواست: </b>"+result.getPropertyAsString("priorityTitle")));
                            timeDesc.setText(Html.fromHtml("<b>ساعت مطلوب: </b>"+result.getPropertyAsString("timeDesc")));
                            dateToPersian.setText(Html.fromHtml("<b>تاریخ انجام تا: </b>"+ result.getPropertyAsString("dateToPersian")));
                            dateFromPersian.setText(Html.fromHtml("<b>تاریخ انجام از: </b>"+ result.getPropertyAsString("dateFromPersian")));
                            Locale farsi = new Locale("fa", "IR");
                            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                            String s = result.getPropertyAsString("calculatedPrice");
                            String c = numberFormatDutch.format(new BigDecimal(s.toString()));
                            final String cc = c.replace("ریال", " " + "\u200e");

                            String styledText = " <font color='red'>"+cc+"</font>";

                            price.setText(Html.fromHtml("<b>هزینه محاسبه شده:</b> "+ "<b>"+styledText+"</b>" + "ریال"));
                            desc.setText(Html.fromHtml("<b>توضیحات:</b> "+result.getPropertyAsString("desc")));





                            select.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog dialog = new Dialog(RequestDetails.this);
                                    dialog.setContentView(R.layout.dialog);
                                    dialog.setTitle("انتخاب درخواست");

                                    // set the custom dialog components - text, image and button
                                    TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
                                    TextView txt1 = (TextView) dialog.findViewById(R.id.dialog_txt);
                                    TextView txt2 = (TextView) dialog.findViewById(R.id.dialog_txt2);
                                    TextView price = (TextView) dialog.findViewById(R.id.dialog_price);
                                    TextView priceType = (TextView) dialog.findViewById(R.id.dialog_price_type);
                                    //  title.setText("افزایش اعتبار");
                                    txt1.setText("با انتخاب این سرویس مبلغ");
                                    txt2.setText("از حساب شما کسر خواهد شد. آیا مطمئن هستید؟");
                                    price.setText(cc);
                                    priceType.setText(" ریال");

                                    Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                                    Button no = (Button) dialog.findViewById(R.id.dialog_no);
                                    // if button is clicked, close the custom dialog
                                    yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                            dialog.dismiss();
                                            if (mWifi.isConnected() || isMobileDataEnabled()) {
                                                App.dialog.show();
                                                RequestDetails.AsyncCallWSSelectRequest task = new RequestDetails.AsyncCallWSSelectRequest();
                                                task.execute();
                                            }else {
                                                final Dialog dialog = new Dialog(RequestDetails.this,android.R.style.Theme_Black_NoTitleBar);
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

                                                            App.dialog.show();
                                                            RequestDetails.AsyncCallWSSelectRequest task = new RequestDetails.AsyncCallWSSelectRequest();
                                                            task.execute();

                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });

                                                dialog.show();                        }
                                        }
                                    });
                                    no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            App.dialog.hide();
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();

                                }
                            });

//                            fullname.setText(Html.fromHtml("<b> نام و نام خانودگی: </b>"+result.getPropertyAsString("personName")));
//                            title.setText(Html.fromHtml("<b>عنوان درخواست: </b>"+result.getPropertyAsString("serviceTitle")));
//                            cat.setText(Html.fromHtml("<b> گروه و زیر گروه:</b> "+result.getPropertyAsString("catTitle")+" - "+result.getPropertyAsString("subCatTitle")) );
//                            area.setText(Html.fromHtml("<b>محدوده: </b>"+result.getPropertyAsString("areaTitle")));
//                            time.setText(Html.fromHtml("<b>تاریخ ثبت: </b>"+result.getPropertyAsString("insrtTimePersian")));
//                            olaviat.setText(Html.fromHtml("<b>اولویت درخواست: </b>"+result.getPropertyAsString("priorityTitle")));
//                            timeDesc.setText(Html.fromHtml("<b>ساعت مطلوب: </b>"+result.getPropertyAsString("timeDesc")));
//                            dateToPersian.setText(Html.fromHtml("<b>تاریخ انجام تا: </b>"+ result.getPropertyAsString("dateToPersian")));
//                            dateFromPersian.setText(Html.fromHtml("<b>تاریخ انجام از: </b>"+ result.getPropertyAsString("dateFromPersian")));
//                            Locale farsi = new Locale("fa", "IR");
//                            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);
//
//                            String s = result.getPropertyAsString("calculatedPrice");
//                            String c = numberFormatDutch.format(new BigDecimal(s.toString()));
//                            String cc = c.replace("ریال", " " + "\u200e");
//
//                            String styledText = " <font color='red'>"+cc+"</font>";
//                            priceee  = styledText;
//
//                            price.setText(Html.fromHtml("<b>هزینه محاسبه شده:</b> "+ "<b>"+styledText+"</b>" + "ریال"));
//                            desc.setText(Html.fromHtml("<b>توضیحات:</b> "+result.getPropertyAsString("desc")));
                            ll.setVisibility(View.VISIBLE);
                            select.setVisibility(View.VISIBLE);

                            App.dialog.dismiss();

                        }
                    });
                }
            }.start();



        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
            App.dialog.dismiss();

        } catch (IOException e) {
            Log.d("Error", "Some exception occurred", e);
            App.dialog.dismiss();

        } catch (XmlPullParserException e) {
            Log.d("Error", "Some exception occurred", e);
            App.dialog.dismiss();

        } catch (NetworkOnMainThreadException e) {
            Log.d("Error", "Some exception occurred", e);
            App.dialog.dismiss();

        }

    }


    @Override
    public void onBackPressed() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            finish();
        } else {
            String newString= extras.getString("PUSH");
            if(newString.equals("push")){
                Intent intent = new Intent(RequestDetails.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }


}
