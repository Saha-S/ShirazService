package com.rahbaran.shirazservice.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Ghavanin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtRules;
    private String fullText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghavanin);

        ImageButton back1 = (ImageButton)  findViewById(R.id.back_ib);
        LinearLayout back = (LinearLayout)  findViewById(R.id.ll_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ////////////////////////////////////////////////

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);

        View view = this.findViewById(android.R.id.content).getRootView();
        App.dialog = new Dialog(view.getRootView().getContext());
        App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        App.dialog.setCancelable(false);
        App.dialog.setContentView(R.layout.loading);

        ibmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

            }
        });


        final TextView txtName = (TextView)findViewById(R.id.txt_name);
        final TextView txtEtebar = (TextView)findViewById(R.id.txt_etebar);
        final TextView txtTakhfif = (TextView)findViewById(R.id.txt_takhfif);
        final CircleImageView img = (CircleImageView) findViewById(R.id.imageView);

        final TableRow credit = (TableRow) findViewById(R.id.credit);
        final TableRow raw = (TableRow) findViewById(R.id.raw);
        final TableRow call = (TableRow) findViewById(R.id.call);
        final TableRow myservice = (TableRow) findViewById(R.id.myservice);
        final TableRow exit = (TableRow) findViewById(R.id.exit);
        final ImageView telegram = (ImageView) findViewById(R.id.telegram);
        final ImageView instagram = (ImageView) findViewById(R.id.instagram);
        final ImageView website = (ImageView) findViewById(R.id.website);

        final TableRow transaction = (TableRow) findViewById(R.id.transaction);

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ghavanin.this , TransactionActivity.class);
                startActivity(intent);

            }
        });


        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="tg://resolve?domain="+App.telegram;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = App.instagram;

                Uri uri = Uri.parse("http://instagram.com/_u/"+ url);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/"+ url)));
                }

            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(App.website));
                startActivity(browserIntent);

            }
        });

        final TextView version  = (TextView)findViewById(R.id.version);
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText("نسخه "+  App.versionName);


        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ghavanin.this , AfzayeshEtebar.class);
                startActivity(intent);
            }
        });

        raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ghavanin.this , Ghavanin.class);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+App.mobile));
                startActivity(intent);
            }
        });
        myservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ghavanin.this , MyServices.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Ghavanin.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.exitdialog);

                Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                Button no = (Button) dialog.findViewById(R.id.dialog_no);
                TextView txt = (TextView) dialog.findViewById(R.id.dialog_txt2);
                txt.setText("کاربر گرامی آیا مایل به خروج از حساب هستید؟");
                // if button is clicked, close the custom dialog
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        final TableRow list = (TableRow) findViewById(R.id.requests);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Ghavanin.this, MainActivity.class);
                startActivity(browserIntent);

            }
        });



        RatingBar ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);

        SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

        final String rating  = prefs.getString("rating", "0");

        ratingbar1.setRating(Float.parseFloat(rating));



        final String fullname = prefs.getString("fullname", "0");

        txtName.setText(fullname);

        Locale farsi = new Locale("fa", "IR");
        NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

        String c = numberFormatDutch.format(new BigDecimal(prefs.getString("credit", "0")));
        final String cc = c.replace("ریال", "" + "\u200e");

        txtEtebar.setText("اعتبار "+cc+" ریال");

        String discountPercent = prefs.getString("discountPercent", "0").toString();
        txtTakhfif.setText("تخفیف "+discountPercent+ " %");
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).placeholder(R.drawable.profile).into(img);

        //////////////////////////////////////////////////////////////////

        txtRules = (TextView) findViewById(R.id.txtRules);

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            App.dialog.show();
            Ghavanin.AsyncCallWS task = new Ghavanin.AsyncCallWS();
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

                        App.dialog.show();
                        Ghavanin.AsyncCallWS task = new Ghavanin.AsyncCallWS();
                        task.execute();

                        dialog.dismiss();
                    }
                }
            });

            dialog.show();            App.dialog.dismiss();
        }


        txtRules.setText(fullText);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
            new Thread()
            {
                public void run()
                {
                    Ghavanin.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            txtRules.setText(fullText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ").replaceAll("&nbsp;"," "));
                            App.dialog.dismiss();
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
