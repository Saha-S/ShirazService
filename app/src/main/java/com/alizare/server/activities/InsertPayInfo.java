package com.alizare.server.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alizare.server.App;
import com.alizare.server.R;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;

public class InsertPayInfo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    EditText edtShomareCart , edtName , edtTime , edtPeygiri , edtPrice ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    int type;
    Button btnInsert , time;
    private ProgressDialog pd;
    private String date;
    private String time2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pay_info);

        ImageButton back = (ImageButton)  findViewById(R.id.back_ib);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtShomareCart = (EditText) findViewById(R.id.edt_shomare_hesab);
       // edtTime = (EditText) findViewById(R.id.edt_time);
        edtPeygiri = (EditText) findViewById(R.id.edt_rahgiri);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        btnInsert = (Button) findViewById(R.id.btn_insert);
        time = (Button) findViewById(R.id.btn_time);
        pd = new ProgressDialog(InsertPayInfo.this);
        pd.setMessage("لطفا صبر کنید..");

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
                    pd.show();

                    InsertPayInfo.AsyncCallWS task = new InsertPayInfo.AsyncCallWS();
                    task.execute();
                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    pd.hide();
                    finish();
                }


            }
        });





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


            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId",prefs.getString("servicemanId", "0"));
            Request.addProperty("type", type);
            Request.addProperty("cardNo", edtShomareCart.getText().toString());
            Request.addProperty("fullName", edtName.getText().toString());
            Request.addProperty("trackingCode", edtPeygiri.getText().toString());
            Request.addProperty("price", edtPrice.getText().toString());
            Request.addProperty("depositTime", time2);
            //Request.addProperty("ip", etpassword.getText().toString());
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


            if(result.getPropertyAsString(0).equals("0")) {

                Log.i("chiiii3", result.toString());
                new Thread()
                {
                    public void run()
                    {
                        InsertPayInfo.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
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
                                App.CustomToast("خطایی رخ داده است . لطفا مجددا امتحان کنید.");

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




}

