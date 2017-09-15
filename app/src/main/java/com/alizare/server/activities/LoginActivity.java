package com.alizare.server.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alizare.server.App;
import com.alizare.server.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    EditText etusername,etpassword;
    Button   btnlogin;

    ProgressBar pblogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etusername  =(EditText) findViewById(R.id.username_et);
        etpassword  =(EditText) findViewById(R.id.password_et);
        btnlogin    =(Button)   findViewById(R.id.login_btn);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncCallWS task = new AsyncCallWS();
                task.execute();


            }
        });
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

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getServiceManInfo";
        String METHOD_NAME = "getServiceManInfo";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("username", etusername.getText().toString());
            Request.addProperty("password", etpassword.getText().toString());
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

            Log.i("khkhkhkhkhk" , result.getPropertyAsString(0));

            if(result.getPropertyAsString(0).equals("0")) {

                String fname = result.getPropertyAsString(2);
                String fullname = fname + " " + result.getPropertyAsString(3);
                Log.i("chiiii3", fullname.toString());


                SharedPreferences.Editor editor = getSharedPreferences("INFO", MODE_PRIVATE).edit();
                editor.putString("fullname", fullname.toString());
                editor.putString("picAddress", result.getPropertyAsString(4));
                editor.putString("personTypeIdx", result.getPropertyAsString(5));
                editor.putString("personType", result.getPropertyAsString(6));
                editor.putString("titleId", result.getPropertyAsString(7));
                editor.putString("title", result.getPropertyAsString(8));
                editor.putString("provinceId", result.getPropertyAsString(9));
                editor.putString("province", result.getPropertyAsString(10));
                editor.putString("cityId", result.getPropertyAsString(11));
//                editor.putString("city", result.getPropertyAsString(12));
                editor.putString("areaId", result.getPropertyAsString(13));
                editor.putString("area", result.getPropertyAsString(14));
                editor.putString("address", result.getPropertyAsString(15));
                editor.putString("totalPoint", result.getPropertyAsString(16));
                editor.putString("rating", result.getPropertyAsString(17));
                editor.putString("credit", result.getPropertyAsString(18));
                editor.putString("tempCredit", result.getPropertyAsString(19));
//            editor.putString("discountPercent", result.getPropertyAsString(20));
                editor.commit();


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } if(result.getPropertyAsString(0).equals("-1") || result.getPropertyAsString(0).equals("-1")){
                new Thread()
                {
                    public void run()
                    {
                        LoginActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.CustomToast(" ﻧﺎم ﮐﺎرﺑﺮی / رﻣﺰ ﻋﺒﻮر وارد ﺷﺪه ﻧﺎﻣﻌﺘﺒﺮ اﺳت ");
                            }
                        });
                    }
                }.start();


            }
             if(result.getPropertyAsString(0).equals("-3")){
                new Thread()
                {
                    public void run()
                    {
                        LoginActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.CustomToast(" سرویس دهنده غیر فعال شده است");
                            }
                        });
                    }
                }.start();


            }
             if(result.getPropertyAsString(0).equals("-1000")){
                new Thread()
                {
                    public void run()
                    {
                        LoginActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.CustomToast(" کلید تبادل درج شده اشتباه است");
                            }
                        });
                    }
                }.start();


            }
            else{
                new Thread()
                {
                    public void run()
                    {
                        LoginActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.CustomToast(" خطایی رخ داده است");
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



}
