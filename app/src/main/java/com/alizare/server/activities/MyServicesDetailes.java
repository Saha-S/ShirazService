package com.alizare.server.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MyServicesDetailes extends AppCompatActivity {


    private Button select;
    private LinearLayout ll;
    private MyServicesDetailes.AsyncCallWS task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton back = (ImageButton)  findViewById(R.id.back_ib);
        select = (Button) findViewById(R.id.btn_select);
        ll = (LinearLayout) findViewById(R.id.ll_mysrvices);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll.setVisibility(View.GONE);
        select.setVisibility(View.GONE);


        task = new MyServicesDetailes.AsyncCallWS();
        task.execute();




    }




    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    public void getDetails() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getRequestDetails";
        String METHOD_NAME = "getRequestDetails";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("requestId", MyServices.requestId);
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


            Log.i("uuuuuuuuuuuuuuuuuuu"  , result.toString());


            new Thread()
            {
                public void run()
                {
                    MyServicesDetailes.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            TextView fullname = (TextView) findViewById(R.id.txt_fullname);
                            TextView title = (TextView) findViewById(R.id.txt_title);
                            TextView phone = (TextView) findViewById(R.id.txt_phone);
                            TextView address = (TextView) findViewById(R.id.txt_address);
                            TextView desc = (TextView) findViewById(R.id.txt_desc);

                            TextView cat = (TextView) findViewById(R.id.txt_cat_subcat);
                            TextView area = (TextView) findViewById(R.id.txt_area);
                            TextView mobile = (TextView) findViewById(R.id.txt_mobile);
                            TextView time = (TextView) findViewById(R.id.txt_time);
                            TextView state = (TextView) findViewById(R.id.txt_state);
                            TextView olaviat = (TextView) findViewById(R.id.txt_olaviat);
                            TextView price = (TextView) findViewById(R.id.txt_price);


                            fullname.setText(Html.fromHtml("<b> نام و نام خانوداگی : </b>"+result.getPropertyAsString("personName")));
                            title.setText(Html.fromHtml("<b>عنوان درخواست : </b>"+result.getPropertyAsString("serviceTitle")));
                            phone.setText(Html.fromHtml("<b> شماره تلفن ثابت : </b>"+result.getPropertyAsString("phone")));
                            address.setText(Html.fromHtml("<b>آدرس : </b>"+ result.getPropertyAsString("address")));
                            desc.setText(Html.fromHtml("<b>توضیحات :</b> "+result.getPropertyAsString("desc")));
                            cat.setText(Html.fromHtml("<b> گروه و زیر گروه :</b> "+result.getPropertyAsString("catTitle")+" - "+result.getPropertyAsString("subCatTitle")) );
                            area.setText(Html.fromHtml("<b>محدوده : </b>"+result.getPropertyAsString("areaTitle")));
                            mobile.setText(Html.fromHtml("<b>شماره موبایل : </b>"+result.getPropertyAsString("mobile")));
                            time.setText(Html.fromHtml("<b>تاریخ درخواست : </b>"+result.getPropertyAsString("insrtTimePersian")));
                            state.setText(Html.fromHtml("<b>وضعیت درخواست :</b> "+result.getPropertyAsString("stateTitle")));
                            olaviat.setText(Html.fromHtml("<b>اولویت درخواست : </b>"+result.getPropertyAsString("priorityTitle")));
                            String s = result.getPropertyAsString("calculatedPrice");
                            Locale farsi = new Locale("fa", "IR");
                            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                            String c = numberFormatDutch.format(new BigDecimal(s.toString()));
                            String cc = c.replace("ریال", " " + "\u200e");

                            price.setText(Html.fromHtml("<b>هزینه محاسبه شده :</b> "+ cc + "ریال"));

                            ll.setVisibility(View.VISIBLE);
                            select.setVisibility(View.VISIBLE);


                            mobile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+result.getPropertyAsString("mobile")));
                                    startActivity(intent);

                                }
                            });

                            phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+result.getPropertyAsString("phone")));
                                    startActivity(intent);

                                }
                            });


                            select.setVisibility(View.VISIBLE);



                            select.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:

                                                    MyServicesDetailes.AsyncCallWSSelectRequest task = new MyServicesDetailes.AsyncCallWSSelectRequest();
                                                    task.execute();
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //No button clicked
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MyServicesDetailes.this);
                                    builder.setMessage("آیا مایل به لغو این درخواست هستید؟").setPositiveButton("بله", dialogClickListener)
                                            .setNegativeButton("خیر", dialogClickListener).show();

                                }
                            });

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

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#cancelRequestByWorkman";
        String METHOD_NAME = "cancelRequestByWorkman";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("requestId", MyServices.requestId);
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
                    MyServicesDetailes.this.runOnUiThread(new Runnable()
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
                                Intent intent = new Intent(MyServicesDetailes.this, MyServices.class);
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
