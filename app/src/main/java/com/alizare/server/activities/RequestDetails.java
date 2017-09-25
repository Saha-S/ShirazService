package com.alizare.server.activities;

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
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class RequestDetails extends AppCompatActivity {

    private TextView txtRules;
    private String fullText;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        ImageButton back = (ImageButton)  findViewById(R.id.back_ib);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        txtRules = (TextView) findViewById(R.id.txtRules);
        pd = new ProgressDialog(RequestDetails.this);
        pd.setMessage("لطفا صبر کنید..");

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            pd.show();

            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            pd.hide();
            finish();
        }



    }




    /////////////////// web servise /////////////////////


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
            pd.hide();
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
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("requestId", MainActivity.requestId);
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
                            price.setText(Html.fromHtml("<b>هزینه محاسبه شده :</b> "+result.getPropertyAsString("calculatedPrice")));

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



                            pd.hide();

                            Button select = (Button) findViewById(R.id.btn_select);

                            select.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case DialogInterface.BUTTON_POSITIVE:

                                                    RequestDetails.AsyncCallWSSelectRequest task = new RequestDetails.AsyncCallWSSelectRequest();
                                                    task.execute();
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //No button clicked
                                                    break;
                                            }
                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetails.this);
                                    builder.setMessage("با انتخاب این سرویس مبلغ"+result.getPropertyAsString("calculatedPrice")+"تومان از حساب شما کسر میشود. آیا تایید میکنید؟").setPositiveButton("بله", dialogClickListener)
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

            //     App.CustomToast("وارد شدید");
            //  Toast.makeText(LoginActivity.this, "وارد شدید" , Toast.LENGTH_LONG).show();
            // LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      /*      for(int i = 0; i< areaNames.size(); i++) {
                View child = inflater.inflate(R.layout.layout_bank, null, false);
                TextView areaName = (TextView) child.findViewById(R.id.bank_name);
                TextView areaId = (TextView) child.findViewById(R.id.bank_code);

                areaName.setText(areaNames.get(i));
                areaId.setText(areaIds.get(i));
                container.addView(child);
            }
*/
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
                    RequestDetails.this.runOnUiThread(new Runnable()
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
                                Intent intent = new Intent(RequestDetails.this, MyServices.class);
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
