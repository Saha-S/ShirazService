package com.alizare.server.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alizare.server.App;
import com.alizare.server.R;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Handler h;
    private ArrayList<String> ServiceTitle;
    private LinearLayout container;
    private Button btnSelect , btnFilter;
    private ImageButton ibmenu;
    private TextView tvtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ServiceTitle = new ArrayList<String>();
        container = (LinearLayout)findViewById(R.id.container);
        btnSelect = (Button)findViewById(R.id.btn_select);
        btnFilter = (Button)findViewById(R.id.btn_filter);
        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
//        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
     //   tvtitle.setTypeface(tfmorvarid);





        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , Filter.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        h = new Handler();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Menu nav_Menu = navigationView.getMenu();

        View hView =  navigationView.getHeaderView(0);

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


        final TextView txtName = (TextView)hView.findViewById(R.id.txt_name);
        final TextView txtEtebar = (TextView)hView.findViewById(R.id.txt_etebar);
        final TextView txtTakhfif = (TextView)hView.findViewById(R.id.txt_takhfif);
        final ImageView img = (ImageView) hView.findViewById(R.id.imageView);
        // final Button btnExit = (Button)hView.findViewById(R.id.nav_gallery);


        SharedPreferences prefs = getSharedPreferences("INFO", MODE_PRIVATE);

        final String fullname = prefs.getString("fullname", "0");

        txtName.setText(fullname);
        txtEtebar.setText("اعتبار فعلی "+prefs.getString("credit", "0")+" ریال");
        txtTakhfif.setText("درصد تخفیف "+prefs.getString("discountPercent", "0")+ " درصد");
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).into(img);


        MainActivity.AsyncCallWS task = new MainActivity.AsyncCallWS();
        task.execute();

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
//            Toast.makeText(ActivitygetAreas.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
            LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            for(int i = 0; i< ServiceTitle.size(); i++) {

                View child = inflater.inflate(R.layout.item_my_service, null, false);
                TextView areaName = (TextView) child.findViewById(R.id.txt_area);

                areaName.setText(ServiceTitle.get(i));
                container.addView(child);


            }

        }

    }

    public void calculate() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#getRequestsList";
        String METHOD_NAME = "getRequestsList";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId", "2");
            Request.addProperty("state", "2");
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

            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();

            int length = result.size();

            for (int i = 0; i < length; ++i) {
                SoapObject so = result.get(i);
                //   smart = new Smartphone();
                //  for (int j = 0; j < so.getPropertyCount(); j++) {
                // smart.setProperty(j, so.getProperty(j));
//                ServiceTitle.add(so.getProperty(5).toString());


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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        if (id == R.id.nav_afzayesh_etebar) {
            Intent intent = new Intent(MainActivity.this , AfzayeshEtebar.class);
            startActivity(intent);
        } else if (id == R.id.nav_ghavanin) {
            Intent intent = new Intent(MainActivity.this , Ghavanin.class);
            startActivity(intent);


        } else if (id == R.id.nav_tamas) {

        } else if (id == R.id.nav_my_service) {
            Intent intent = new Intent(MainActivity.this , MyServices.class);
            startActivity(intent);


        }

        return true;
    }
}
