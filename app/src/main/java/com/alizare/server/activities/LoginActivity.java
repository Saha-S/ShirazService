package com.alizare.server.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alizare.server.App;
import com.alizare.server.R;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    Button btnlogin;

    ProgressBar pblogin;
    private SliderLayout mDemoSlider;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;
    private String username , password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etusername = (EditText) findViewById(R.id.username_et);
        etpassword = (EditText) findViewById(R.id.password_et);
        btnlogin = (Button) findViewById(R.id.login_btn);
        pblogin = (ProgressBar) findViewById(R.id.login_progress);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        TextView tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "mjdalalst.ttf");
        tvtitle.setTypeface(face);


        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            etusername.setText(loginPreferences.getString("username", ""));
            etpassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }


        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1",R.drawable.s1);
        file_maps.put("2",R.drawable.s2);
        file_maps.put("3",R.drawable.s3);
        file_maps.put("4", R.drawable.s4);
        file_maps.put("5", R.drawable.s5);
        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }






        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etusername.getText().length() == 0){
                    etusername.setError("لطفا نام کاربری خود را وارد کنید");
                }
                if(etpassword.getText().length() == 0){
                    etpassword.setError("لطفا رمز عبور خود را وارد کنید");
                }

                if(etpassword.getText().length() != 0 && etusername.getText().length() != 0){
                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();

                    } else {
                        App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                        pblogin.setVisibility(View.INVISIBLE);
                        btnlogin.setEnabled(true);
                    }
                }



            }
        });
    }


    /////////////////// web servise /////////////////////


    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pblogin.setVisibility(View.VISIBLE);
            btnlogin.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            LoginTask();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


        }

    }

    public void LoginTask() {


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


            if (result.getPropertyAsString(0).equals("0")) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etusername.getWindowToken(), 0);

                username = etusername.getText().toString();
                password = etpassword.getText().toString();

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }


                String fullname = result.getPropertyAsString("firstName") + " " + result.getPropertyAsString("lastName");


                SharedPreferences.Editor editor = getSharedPreferences("INFO", MODE_PRIVATE).edit();
                editor.putString("fullname", fullname.toString());
                editor.putString("servicemanId", result.getPropertyAsString("servicemanId"));
                editor.putString("picAddress", result.getPropertyAsString("picAddress"));
               // editor.putString("personTypeIdx", result.getPropertyAsString(6));
                //editor.putString("personType", result.getPropertyAsString(7));
               // editor.putString("titleId", result.getPropertyAsString(8));
             //   editor.putString("title", result.getPropertyAsString(9));
             //   editor.putString("provinceId", result.getPropertyAsString(10));
             //   editor.putString("province", result.getPropertyAsString(11));
           //     editor.putString("cityId", result.getPropertyAsString(12));
//                editor.putString("city", result.getPropertyAsString(13));
         //       editor.putString("areaId", result.getPropertyAsString(14));
           //     editor.putString("area", result.getPropertyAsString(15));
         //       editor.putString("time", result.getPropertyAsString(16));
          //      editor.putString("totalPoint", result.getPropertyAsString(17));
                editor.putString("rating", result.getPropertyAsString("rating"));
                editor.putString("credit", result.getPropertyAsString("credit"));
                editor.putString("tempCredit", result.getPropertyAsString("tempCredit"));
//            editor.putString("discountPercent", result.getPropertyAsString(21));
                editor.commit();
                new Thread() {
                    public void run() {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                //finish();
                                pblogin.setVisibility(View.INVISIBLE);
                                btnlogin.setEnabled(true);

                                {
                                    App.CustomToast("خوش آمدید");
                                }

                            }
                        });
                    }
                }.start();


            } else {
                if (result.getPropertyAsString(0).equals("-1") || result.getPropertyAsString(0).equals("-1")) {
                    new Thread() {
                        public void run() {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    App.CustomToast(" ﻧﺎم ﮐﺎرﺑﺮی / رﻣﺰ ﻋﺒﻮر وارد ﺷﺪه ﻧﺎﻣﻌﺘﺒﺮ اﺳت ");
                                    pblogin.setVisibility(View.INVISIBLE);
                                    btnlogin.setEnabled(true);

                                }
                            });
                        }
                    }.start();


                }

                if (result.getPropertyAsString(0).equals("-3")) {
                    new Thread() {
                        public void run() {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    App.CustomToast(" سرویس دهنده غیر فعال شده است");
                                    pblogin.setVisibility(View.INVISIBLE);
                                    btnlogin.setEnabled(true);

                                }
                            });
                        }
                    }.start();


                }
                if (result.getPropertyAsString(0).equals("-1000")) {
                    new Thread() {
                        public void run() {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    App.CustomToast(" کلید تبادل درج شده اشتباه است");
                                    pblogin.setVisibility(View.INVISIBLE);
                                    btnlogin.setEnabled(true);

                                }
                            });
                        }
                    }.start();


                }
                if (result.getPropertyAsString(0).equals("-4")) {
                    new Thread() {
                        public void run() {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    App.CustomToast(" شماره موبایل وارد شده اشتباه است.");
                                    pblogin.setVisibility(View.INVISIBLE);
                                    btnlogin.setEnabled(true);

                                }
                            });
                        }
                    }.start();


                }
                else {
                    new Thread() {
                        public void run() {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    App.CustomToast(" خطایی پیش آمده است .");
                                    pblogin.setVisibility(View.INVISIBLE);
                                    btnlogin.setEnabled(true);

                                }
                            });
                        }
                    }.start();


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




