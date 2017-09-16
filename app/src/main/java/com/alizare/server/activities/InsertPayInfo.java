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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alizare.server.App;
import com.alizare.server.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class InsertPayInfo extends AppCompatActivity {

    EditText edtShomareCart , edtName , edtTime , edtPeygiri , edtPrice ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    int type;
    Button btnInsert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pay_info);

        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtShomareCart = (EditText) findViewById(R.id.edt_shomare_hesab);
        edtTime = (EditText) findViewById(R.id.edt_time);
        edtPeygiri = (EditText) findViewById(R.id.edt_rahgiri);
        edtPrice = (EditText) findViewById(R.id.edt_price);
        btnInsert = (Button) findViewById(R.id.btn_insert);


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertPayInfo.AsyncCallWS task = new InsertPayInfo.AsyncCallWS();
                task.execute();

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
            Request.addProperty("depositTime", "2");
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



}

