package com.alizare.server.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alizare.server.AreaOnItemSelectedListener;
import com.alizare.server.CatOnItemSelectedListener;
import com.alizare.server.OlaviatOnItemSelectedListener;
import com.alizare.server.R;
import com.alizare.server.SubOnItemSelectedListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class Filter extends Activity {

    private ArrayList<String> ServiceArea;
    public static ArrayList<String> ServiceAreaID;
    private ArrayList<String> ServiceCats;
    public static ArrayList<String> ServiceCatsID;
    private ArrayList<String> ServiceOlaviat;
    public static ArrayList<String> ServiceOlaviatID;
    public static ArrayList<String> ServiceSub;
    public static ArrayList<String> ServiceSubId;

    Spinner spiner;
    static Spinner spinerSub;
    Spinner spinerCat ,spinerCatOlaviat ;
    ArrayAdapter<String> spinnerArrayAdapterCats;
    ArrayAdapter<String> spinnerArrayAdapterOlaviat;
    ArrayAdapter<String> spinnerArrayAdapterArea;
    static ArrayAdapter<String> spinnerArrayAdapterSub;
    public static  String areaId = "" , catId ="", olaviatId  = "" ,subId  = "";
    private ProgressDialog pd;
    private static LinearLayout llSub;
    public static Filter f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        f= Filter.this;

        pd = new ProgressDialog(Filter.this);
        pd.setMessage("لطفا صبر کنید..");
       // pd.show();


        Button btnFilter = (Button) findViewById(R.id.btn_filter2) ;
         llSub = (LinearLayout) findViewById(R.id.ll_sub) ;
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.h.sendEmptyMessage(0);

                Intent intent = new Intent(Filter.this , MainActivity.class);
                startActivity(intent);

                finish();



            }
        });

        /////////// AREA /////////

        ServiceArea = new ArrayList<String>();
        ServiceAreaID = new ArrayList<String>();
        ServiceArea.add("انتخاب کنید");
        ServiceAreaID.add("");
        AsyncCallWSGetAreas task = new AsyncCallWSGetAreas();
        task.execute();

         spiner = (Spinner) findViewById(R.id.spinner);
         spinnerArrayAdapterArea = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ServiceArea);
        spinnerArrayAdapterArea.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );


        ////////// CATS //////

        ServiceCats = new ArrayList<String>();
        ServiceCatsID = new ArrayList<String>();
        ServiceCats.add("انتخاب کنید");
        ServiceCatsID.add("");

        Filter.AsyncCallWSGetCat taskCats = new Filter.AsyncCallWSGetCat();
        taskCats.execute();

        spinerCat = (Spinner) findViewById(R.id.spinnerCat);
        spinnerArrayAdapterCats = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ServiceCats);
        spinnerArrayAdapterCats.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        //////////// OLAVIAT //////

        ServiceOlaviat = new ArrayList<String>();
        ServiceOlaviatID = new ArrayList<String>();
        ServiceOlaviat.add("انتخاب کنید");
        ServiceOlaviatID.add("");

        Filter.AsyncCallWSGetOlaviat taskOlaviat = new Filter.AsyncCallWSGetOlaviat();
        taskOlaviat.execute();

        spinerCatOlaviat = (Spinner) findViewById(R.id.spinnerOlaviat);
        spinnerArrayAdapterOlaviat = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ServiceOlaviat);
        spinnerArrayAdapterOlaviat.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        ////////// SUB ////////////

        ServiceSub = new ArrayList<String>();
        ServiceSubId = new ArrayList<String>();
        ServiceSub.add("انتخاب کنید");
        ServiceSubId.add("");


        spinerSub = (Spinner) findViewById(R.id.spinnerSubCat);
        spinnerArrayAdapterSub = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ServiceSub);
        spinnerArrayAdapterSub.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );



    }
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
            spiner.setAdapter(spinnerArrayAdapterArea);
            spiner.setOnItemSelectedListener(new AreaOnItemSelectedListener());



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
            for (int i = 0; i < 1; ++i) {
                SoapObject so = result.get(i);
                ServiceArea.add(so.getProperty(3).toString());
                ServiceAreaID.add(so.getProperty(2).toString());
            }


            for (int i = 1; i < length; ++i) {
                SoapObject so = result.get(i);
                ServiceArea.add(so.getProperty(1).toString());
                ServiceAreaID.add(so.getProperty(0).toString());
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
            spinerCat.setAdapter(spinnerArrayAdapterCats);
            spinerCat.setOnItemSelectedListener(new CatOnItemSelectedListener());
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

            for (int i = 0; i < 1; ++i) {
                SoapObject so = result.get(i);
                if (!ServiceCats.contains(so.getProperty(3))){
                    ServiceCats.add(so.getProperty(3).toString());
                    ServiceCatsID.add(so.getProperty(2).toString());
                }

            }

            for (int i = 1; i < length; ++i) {
                SoapObject so = result.get(i);
                if (!ServiceCats.contains(so.getProperty(1))) {

                    ServiceCats.add(so.getProperty(1).toString());
                    ServiceCatsID.add(so.getProperty(0).toString());
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
            getSubCat();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            spinerSub.setAdapter(spinnerArrayAdapterSub);
            spinerSub.setOnItemSelectedListener(new SubOnItemSelectedListener());




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

                    for (int i = 0; i < 1; ++i) {
                        SoapObject so = result.get(i);
                        ServiceSub.add(so.getProperty(5).toString());
                        ServiceSubId.add(so.getProperty(4).toString());


                    }

                    for (int i = 1; i < length; ++i) {
                        SoapObject so = result.get(i);

                        ServiceSub.add(so.getProperty(3).toString());
                        ServiceSubId.add(so.getProperty(2).toString());

                    }

                    spinerSub.setAdapter(spinnerArrayAdapterSub);
                    spinerSub.setOnItemSelectedListener(new SubOnItemSelectedListener());

                }
                else{
                    llSub.setVisibility(View.GONE);
                    subId="";

                }

            }
            else{
                llSub.setVisibility(View.GONE);
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
            spinerCatOlaviat.setAdapter(spinnerArrayAdapterOlaviat);
            spinerCatOlaviat.setOnItemSelectedListener(new OlaviatOnItemSelectedListener());
            pd.hide();



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

            for (int i = 0; i < 1; ++i) {
                SoapObject so = result.get(i);
                ServiceOlaviat.add(so.getProperty(3).toString());
                ServiceOlaviatID.add(so.getProperty(2).toString());
            }
            for (int i = 1; i < length; ++i) {
                SoapObject so = result.get(i);
                ServiceOlaviat.add(so.getProperty(1).toString());
                ServiceOlaviatID.add(so.getProperty(0).toString());
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


}
