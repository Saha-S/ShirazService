package com.rahbaran.shirazservice.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.rahbaran.shirazservice.App.context;


public class LoginActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    Button btnlogin;

    private SliderLayout mDemoSlider;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;
    private String username , password;
    private Dialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Intent intent = new Intent(LoginActivity.this, AfzayeshEtebar.class);
//        startActivity(intent);


        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {

            LoginActivity.AsyncGetBaseInfoOfApp task = new LoginActivity.AsyncGetBaseInfoOfApp();
            task.execute();
        }else {
            final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
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

                        LoginActivity.AsyncGetBaseInfoOfApp task = new LoginActivity.AsyncGetBaseInfoOfApp();
                        task.execute();

                        dialog.dismiss();
                    }
                }
            });

            dialog.show();        }





        etusername = (EditText) findViewById(R.id.username_et);
        etpassword = (EditText) findViewById(R.id.password_et);
        btnlogin = (Button) findViewById(R.id.login_btn);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        View view = this.findViewById(android.R.id.content).getRootView();
        App.dialog = new Dialog(view.getRootView().getContext());
        App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        App.dialog.setCancelable(false);
        App.dialog.setContentView(R.layout.loading);



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
                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();

                    } else {
                        final Dialog dialog = new Dialog(LoginActivity.this,android.R.style.Theme_Black_NoTitleBar);
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

                                    AsyncCallWS task = new AsyncCallWS();
                                    task.execute();


                                    dialog.dismiss();
                                }
                            }
                        });

                        dialog.show();                        App.dialog.dismiss();
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


                App.dialog.show();
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

            final SoapObject result = (SoapObject) envelope.getResponse();


            if (result.getPropertyAsString("res").equals("0")) {
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
                editor.putString("discountPercent", result.getPropertyAsString("discountPercent"));
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
                editor.putString("credit", result.getPropertyAsString("tempCredit"));
              //  editor.putString("tempCredit", result.getPropertyAsString("tempCredit"));
//            editor.putString("discountPercent", result.getPropertyAsString(21));
                editor.commit();
                new Thread() {
                    public void run() {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                //finish();
                                App.dialog.dismiss();
                                btnlogin.setEnabled(true);

                                {
                                    App.CustomToast("خوش آمدید");
                                }

                            }
                        });
                    }
                }.start();


            } else {

                new Thread() {
                    public void run() {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                App.CustomToast(result.getPropertyAsString("resStr"));
                                App.dialog.dismiss();
                                btnlogin.setEnabled(true);

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
        Object connectivityService = context.getSystemService(CONNECTIVITY_SERVICE);
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

    /////////////////////////////////////////////// Version /////////

    private class AsyncGetBaseInfoOfApp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            getBaseInfoOfApp();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


        }

    }

    public void getBaseInfoOfApp() {


        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getBaseInfoOfApp";
        String METHOD_NAME = "getBaseInfoOfApp";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";



        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
          //  Request.addProperty("version", App.versioCode);

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


            if (result.getPropertyAsString("res").equals("0")) {

                final String sreverV = result.getPropertyAsString("androidServiceManAppVer");
                App.telegram = result.getPropertyAsString("telegramLink");
                App.mobile = result.getPropertyAsString("supportPhone");
                App.instagram = result.getPropertyAsString("instagramLink");
                App.website = "http://shiraz-service.ir";


                if (!compareVersion(App.versionName, sreverV) && result.getPropertyAsString("androidServiceManAppForceUpdate").toString().equals("1")){

                new Thread() {
                    public void run() {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {


                                    dialog = new Dialog(LoginActivity.this);
                                    dialog.setContentView(R.layout.dialog);
                                    dialog.setTitle("بروز رسانی شیراز سرویس");

                                    dialog.setCancelable(false);

                                    // set the custom dialog components - text, image and button
                                    TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
                                    TextView txt1 = (TextView) dialog.findViewById(R.id.dialog_txt);
                                    TextView txt2 = (TextView) dialog.findViewById(R.id.dialog_txt2);
                                    TextView price = (TextView) dialog.findViewById(R.id.dialog_price);
                                    TextView priceType = (TextView) dialog.findViewById(R.id.dialog_price_type);

                                    Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                                    Button no = (Button) dialog.findViewById(R.id.dialog_no);

                                    yes.setText("شروع بروز رسانی");

                                    txt1.setVisibility(View.GONE);
                                    price.setVisibility(View.GONE);
                                    priceType.setVisibility(View.GONE);
                                    no.setVisibility(View.GONE);

                                    txt2.setText("جهت دریافت نسخه آخر لطفاً بر روی لینک زیر کلیک نمایید." +
                                            "توجه نمایید که تا نرم افزار خود را بروز نکنید، نمی توانید از شیراز سرویس استفاده نمایید. ");

                                    // if button is clicked, close the custom dialog
                                    yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            //dialog.dismiss();

                                            String link = result.getPropertyAsString("androidAppDlLink");
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                            startActivity(browserIntent);

                                        }
                                    });

                                    dialog.show();



                            }
                        });
                    }
                }.start();

                }
            } else {

                new Thread() {
                    public void run() {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                App.CustomToast(result.getPropertyAsString("resStr"));


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
    private boolean compareVersion(String currrentsVersion, String officialVersion) {
        Boolean isEqual = false;

        String[] string1 = currrentsVersion.split("[.]");
        String[] string2 = officialVersion.split("[.]");
        Integer[] number2 = new Integer[string2.length];

        Integer[] numbers = new Integer[string2.length];


        for (int i = 0; i < string2.length; i++) {
            if (string1.length-1<i)
                numbers[i] = 0;
            else
                numbers[i] = Integer.parseInt(string1[i]);
            System.out.println("number1 ::: " + numbers[i]);
        }


        for (int i = 0; i < string2.length; i++) {
            number2[i] = Integer.parseInt(string2[i]);
            System.out.println("number2 ::: " + number2[i]);
        }

        for (int i = 0; i < number2.length; i++) {
            if (number2[i] > numbers[i]) {
                isEqual = false;
                break;
            } else {
                isEqual = true;
            }
        }
        return isEqual;
    }

}




