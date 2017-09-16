package com.alizare.server.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alizare.server.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class MyServices extends AppCompatActivity {

    private ArrayList<String> ServiceArea;
    private ArrayList<String> ServiceState;
    private ArrayList<String> ServiceTitle;
    private ArrayList<String> ServiceFName;
    private ArrayList<String> ServiceLName;
    private ArrayList<String> ServicePhone;
    private ArrayList<String> ServiceAddress;
    private ArrayList<String> ServiceDesc;
    private LinearLayout container;
    public static String stitle , fullname , phone , address , desc;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pd = new ProgressDialog(MyServices.this);
        pd.setMessage("لطفا صبر کنید..");
        pd.show();


        ServiceTitle = new ArrayList<String>();
        ServiceState = new ArrayList<String>();
        ServiceArea = new ArrayList<String>();

        ServiceFName = new ArrayList<String>();
        ServiceLName = new ArrayList<String>();
        ServicePhone = new ArrayList<String>();
        ServiceAddress = new ArrayList<String>();
        ServiceDesc = new ArrayList<String>();

        container = (LinearLayout)findViewById(R.id.ll_mysrvices);



        AsyncCallWS task = new AsyncCallWS();
        task.execute();

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
                LinearLayout item = (LinearLayout) child.findViewById(R.id.ll_item);

                areaName.setText("محدوده ی "+ServiceArea.get(i));
                title.setText(ServiceTitle.get(i));
                state.setText(ServiceState.get(i));
                container.addView(child);

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        stitle = ServiceTitle.get(index);
                        phone = ServicePhone.get(index);
                        desc = ServiceDesc.get(index);
                        address = ServiceAddress.get(index);
                        fullname = ServiceFName.get(index) + " " +ServiceLName.get(index);
                        index = ((LinearLayout) child.getParent()).indexOfChild(child);
                        Intent intent = new Intent(MyServices.this, MyServicesDetailes.class);
                        startActivity(intent);




                    }
                });

            }
            pd.hide();




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
                ServiceTitle.add(so.getProperty(5).toString());
                ServiceArea.add(so.getProperty(13).toString());
                ServiceState.add(so.getProperty(30).toString());
                ServiceFName.add(so.getProperty(15).toString());
                ServiceLName.add(so.getProperty(14).toString());
                ServicePhone.add(so.getProperty(17).toString());
                ServiceAddress.add(so.getProperty(18).toString());
                ServiceDesc.add(so.getProperty(19).toString());


                //  }
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
