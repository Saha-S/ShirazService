package com.alizare.server.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.alizare.server.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Ghavanin extends AppCompatActivity {

    private TextView txtRules;
    private String fullText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghavanin);

        txtRules = (TextView) findViewById(R.id.txtRules);


        Ghavanin.AsyncCallWS task = new Ghavanin.AsyncCallWS();
        task.execute();

        txtRules.setText(fullText);

    }




    /////////////////// web servise /////////////////////


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

    public void calculate() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getStaticPagesText";
        String METHOD_NAME = "getStaticPagesText";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("type", "1");
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

            fullText =result.getPropertyAsString(3);
            Log.i("chiiii3", fullText.toString());
            new Thread()
            {
                public void run()
                {
                    Ghavanin.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            txtRules.setText(fullText);
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
