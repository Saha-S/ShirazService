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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InsertPayInfo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    EditText edtShomareCart , edtName , edtTime , edtPeygiri , edtPrice ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    int type;
    Button btnInsert , time;
    private String date;
    private String time2;
    private String ip;
    TextView err;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pay_info);

        ImageButton back1 = (ImageButton)  findViewById(R.id.back_ib);
        LinearLayout back = (LinearLayout)  findViewById(R.id.ll_back);
        err = (TextView)  findViewById(R.id.err);

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

        View view = this.findViewById(android.R.id.content).getRootView();
        App.dialog = new Dialog(view.getRootView().getContext());
        App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        App.dialog.setCancelable(false);
        App.dialog.setContentView(R.layout.loading);


        //////////////////////////////

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
        final TextView txtEtebar = (TextView)findViewById(R.id.txt_etebar);
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
                Intent intent = new Intent(InsertPayInfo.this , TransactionActivity.class);
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
                Intent intent = new Intent(InsertPayInfo.this , AfzayeshEtebar.class);
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
                Intent intent = new Intent(InsertPayInfo.this , MyServices.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(InsertPayInfo.this);
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
                Intent browserIntent = new Intent(InsertPayInfo.this, MainActivity.class);
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

        ////////////////////////////////////////////

        ConnectivityManager cm = (ConnectivityManager)App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        WifiManager wm = (WifiManager)App.context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo connectionInfo = wm.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        ip = Formatter.formatIpAddress(ipAddress);

        if(ip.toString().equals("0.0.0.0")){
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            ip= inetAddress.getHostAddress().toString();
                            Log.i("ipppp" , ip);
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e("IP Address", ex.toString());
            }
        }

        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtShomareCart = (EditText) findViewById(R.id.edt_shomare_hesab);
       // edtTime = (EditText) findViewById(R.id.edt_time);
        edtPeygiri = (EditText) findViewById(R.id.edt_rahgiri);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        btnInsert = (Button) findViewById(R.id.btn_insert);
        time = (Button) findViewById(R.id.btn_time);

        String r = numberFormatDutch.format(new BigDecimal(AfzayeshEtebar.sum));
        String rr = r.replace("ریال", "" + "\u200e");

        edtPrice.setText(rr );
        edtPrice.setEnabled(false);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar now = new PersianCalendar();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) InsertPayInfo.this,
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay()
                );
               // dpd.setThemeDark(modeDarkDate.isChecked());
             //   dpd.show(getFragmentManager(), DATEPICKER);
                dpd.show(getFragmentManager() , "DatePickerDialog");


            }
        });


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    Boolean e1 , e2 , e3 , e4  , e5 = false;

                    if(edtShomareCart.getText().toString().equals("")){
                        edtShomareCart.setError("لطفا شماره کارت را وارد کنید");
                        e1=false;
                    }else e1 = true;
                    if(time.getText().equals("انتخاب تاریخ") || time.getText().equals("لطفا تاریخ واریز را مشخص کنید") ){
                        err.setVisibility(View.VISIBLE);
                        err.setText("لطفا تاریخ واریز را مشخص کنید");
                        e2=false;
                    }else{ e2 = true;
                    err.setVisibility(View.GONE);
                    }

                if(edtName.getText().toString().equals("")){
                        edtName.setError("لطفا نام و نام خانودگی را وارد کنید");
                        e3=false;
                }else e3 = true;
                    if(edtPeygiri.getText().toString().equals("")){
                        edtPeygiri.setError("لطفا کد پیگیری را وارد کنید");
                        e4=false;
                    }else e4 = true;
                    if(edtPrice.getText().toString().equals("")){
                        edtPrice.setError("لطفا مبلغ واریز شده را وارد کنید");
                        e5=false;
                    }else e5 = true;
                    if(e1==true && e2==true && e3 == true && e4 ==true && e5==true)
                    {

                        App.dialog.show();
                        InsertPayInfo.AsyncCallWS task = new InsertPayInfo.AsyncCallWS();
                        task.execute();
                    }

                }else {
                    final Dialog dialog = new Dialog(InsertPayInfo.this,android.R.style.Theme_Black_NoTitleBar);
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
                                Boolean e1 , e2 , e3 , e4  , e5 = false;

                                if(edtShomareCart.getText().toString().equals("")){
                                    edtShomareCart.setError("لطفا شماره کارت را وارد کنید");
                                    e1=false;
                                }else e1 = true;
                                if(time.getText().equals("انتخاب تاریخ") || time.getText().equals("لطفا تاریخ واریز را مشخص کنید") ){
                                    err.setVisibility(View.VISIBLE);
                                    err.setText("لطفا تاریخ واریز را مشخص کنید");
                                    e2=false;
                                }else{ e2 = true;
                                    err.setVisibility(View.GONE);
                                }

                                if(edtName.getText().toString().equals("")){
                                    edtName.setError("لطفا نام و نام خانودگی را وارد کنید");
                                    e3=false;
                                }else e3 = true;
                                if(edtPeygiri.getText().toString().equals("")){
                                    edtPeygiri.setError("لطفا کد پیگیری را وارد کنید");
                                    e4=false;
                                }else e4 = true;
                                if(edtPrice.getText().toString().equals("")){
                                    edtPrice.setError("لطفا مبلغ واریز شده را وارد کنید");
                                    e5=false;
                                }else e5 = true;
                                if(e1==true && e2==true && e3 == true && e4 ==true && e5==true)
                                {

                                    App.dialog.show();
                                    InsertPayInfo.AsyncCallWS task = new InsertPayInfo.AsyncCallWS();
                                    task.execute();
                                }
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();                    App.dialog.dismiss();
                   // finish();
                }


            }
        });





    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

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

        }

    }

    public void calculate() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#insrtDepositMoney";
        String METHOD_NAME = "insrtDepositMoney";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
            radioTypeButton = (RadioButton) findViewById(selectedId);

            if (radioTypeButton != null) {

                if( radioTypeButton.getText().toString().equals("کارت به کارت")){
                    type = 1;
                }
                if( radioTypeButton.getText().toString().equals("واریز به حساب")){
                    type = 2;
                }
            }

            InputStream stream = new ByteArrayInputStream(edtName.getText().toString().getBytes());

            String a = getResponseString(stream);



            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId",prefs.getString("servicemanId", "0"));
            Request.addProperty("type", String.valueOf(type));
            Request.addProperty("cardNo", edtShomareCart.getText().toString());
            Request.addProperty("fullName", a);
            Request.addProperty("trackingCode", edtPeygiri.getText().toString());
            Request.addProperty("price", AfzayeshEtebar.sum);
            Request.addProperty("depositTime", time2 +" " + date);
            Request.addProperty("ip", " ");
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


            if(result.getPropertyAsString("res").equals("0")) {

                new Thread()
                {
                    public void run()
                    {
                        InsertPayInfo.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.dialog.dismiss();
                                App.CustomToast("اطلاعات با موفقیت ثبت شد");
                                Intent intent = new Intent(InsertPayInfo.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }
                }.start();


            } else {
                new Thread()
                {
                    public void run()
                    {
                        InsertPayInfo.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.dialog.dismiss();
                                App.CustomToast(result.getPropertyAsString("resStr").toString());

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



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // Note: monthOfYear is 0-indexed
        date =   year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
        PersianCalendar now = new PersianCalendar();


        TimePickerDialog tpd = TimePickerDialog.newInstance(
                InsertPayInfo.this,

                now.get(PersianCalendar.HOUR_OF_DAY),
                now.get(PersianCalendar.MINUTE),
                true

        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePickerDialog", "Dialog was cancelled");
            }
        });


        tpd.show(getFragmentManager(), "TimePickerDialog");


    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        time2 =  hourString + ":" + minuteString ;
        time.setText("تاریخ واریز : "+time2 +" " + date);
    }


    private String getResponseString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"ISO-8859-1"));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        finally {
            stream.close();
        }
        return sb.toString();
    }


}

