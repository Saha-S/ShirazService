package com.rahbaran.shirazservice.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.moslem_deris.apps.zarinpal.PaymentBuilder;
import ir.moslem_deris.apps.zarinpal.ZarinPal;
import ir.moslem_deris.apps.zarinpal.enums.ZarinPalError;
import ir.moslem_deris.apps.zarinpal.listeners.OnPaymentListener;
import ir.moslem_deris.apps.zarinpal.models.Payment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AfzayeshEtebar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtEtebar , txtMeghdarEtebar , txtEtebarnaahaii;
    Button btnPay , btnOnline;
    ToggleButton btn10 , btn20 , btn30, btn50, btn100, btn200 ;
    EditText edtMablagh;
    int s10 , s20 , s30 , s50 , s100 , s200 = 0;
    public static int sum =0;
            int etebarfelii;
    private CheckBox chkPrice;
    TextView err;
    private String peygiri;
    private TextView txtEtebarMenu;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afzayesh_etebar);

        txtEtebar = (TextView) findViewById(R.id.txt_etebarf);
        txtMeghdarEtebar = (TextView) findViewById(R.id.txt_etebar_taghiir);
        txtEtebarnaahaii = (TextView) findViewById(R.id.txt_etebar_nahaii);
        edtMablagh = (EditText) findViewById(R.id.edt_mablagh);
        err = (TextView) findViewById(R.id.err);

        View view = this.findViewById(android.R.id.content).getRootView();
        App.dialog = new Dialog(view.getRootView().getContext());
        App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        App.dialog.setCancelable(false);
        App.dialog.setContentView(R.layout.loading);



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

        //////////////////////////////////

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        TextView tvtitle=(TextView) findViewById(R.id.title);

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
        txtEtebarMenu = (TextView)findViewById(R.id.txt_etebar);
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
                Intent intent = new Intent(AfzayeshEtebar.this , TransactionActivity.class);
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
                Intent intent = new Intent(AfzayeshEtebar.this , AfzayeshEtebar.class);
                startActivity(intent);
            }
        });

        raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://shiraz-service.ir/terms/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));                startActivity(intent);
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
                Intent intent = new Intent(AfzayeshEtebar.this , MyServices.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AfzayeshEtebar.this);
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
                Intent browserIntent = new Intent(AfzayeshEtebar.this, MainActivity.class);
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
        final NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

        String n = numberFormatDutch.format(new BigDecimal(prefs.getString("credit", "0").toString()));
        final String nn = n.replace("ریال", "" + "\u200e");

        txtEtebarMenu.setText("اعتبار "+nn+" ریال");

        String discountPercent = prefs.getString("discountPercent", "0").toString();
        txtTakhfif.setText("تخفیف "+discountPercent+ " %");
        Picasso.with(App.context).load(prefs.getString("picAddress", "0")).placeholder(R.drawable.profile).into(img);


        ////////////////////////////////////////

        btn10 = (ToggleButton) findViewById(R.id.btn_10);
        btn20 = (ToggleButton) findViewById(R.id.btn_20);
        btn30 = (ToggleButton) findViewById(R.id.btn_30);
        btn50 = (ToggleButton) findViewById(R.id.btn_50);
        btn100 = (ToggleButton) findViewById(R.id.btn_100);
        btn200 = (ToggleButton) findViewById(R.id.btn_200);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnOnline = (Button) findViewById(R.id.btn_online);
        final TextInputLayout p = (TextInputLayout) findViewById(R.id.p);



        chkPrice = (CheckBox) findViewById(R.id.chk_price);
        chkPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    p.setVisibility(View.VISIBLE);
                    btn10.setEnabled(false);
                    btn20.setEnabled(false);
                    btn30.setEnabled(false);
                    btn50.setEnabled(false);
                    btn100.setEnabled(false);
                    btn200.setEnabled(false);

                    btn10.setChecked(false);
                    btn20.setChecked(false);
                    btn30.setChecked(false);
                    btn50.setChecked(false);
                    btn100.setChecked(false);
                    btn200.setChecked(false);
                }
                else{
                    p.setVisibility(View.GONE);
                    edtMablagh.setText("");
                    btn10.setEnabled(true);
                    btn20.setEnabled(true);
                    btn30.setEnabled(true);
                    btn50.setEnabled(true);
                    btn100.setEnabled(true);
                    btn200.setEnabled(true);

                }
            }
        });



        etebarfelii= Integer.parseInt(prefs.getString("credit", "0"));

        String q = numberFormatDutch.format(new BigDecimal(etebarfelii));
        String qq = q.replace("ریال", "" + "\u200e");

        txtEtebar.setText("اعتبار فعلی "+qq+" ریال");

        String d = numberFormatDutch.format(new BigDecimal(sum));
        String dd = d.replace("ریال", "" + "\u200e");


        String styledText = " <font color='#330000'>"+dd+"</font>";

        txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
        String e = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
        String ee = e.replace("ریال", "" + "\u200e");

        txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (ee) + " ریال");


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sum>1000) {
                    err.setVisibility(View.GONE);
                    Intent intent = new Intent(AfzayeshEtebar.this, InsertPayInfo.class);
                    startActivity(intent);
                }else
                    err.setVisibility(View.VISIBLE);
            }
        });



        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sum>1000) {
                    err.setVisibility(View.GONE);


                    final Dialog dialog = new Dialog(AfzayeshEtebar.this);
                        dialog.setContentView(R.layout.dialog);
                        dialog.setTitle("افزایش اعتبار");

                        // set the custom dialog components - text, image and button
                        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
                        TextView txt1 = (TextView) dialog.findViewById(R.id.dialog_txt);
                        TextView txt2 = (TextView) dialog.findViewById(R.id.dialog_txt2);
                        TextView price = (TextView) dialog.findViewById(R.id.dialog_price);
                        TextView priceType = (TextView) dialog.findViewById(R.id.dialog_price_type);
                        //  title.setText("افزایش اعتبار");
                        txt1.setText("اعتبار شما به میزان");
                        txt2.setText("افزایش خواهد یافت. آیا مطمئن هستید؟");
                        Locale farsi = new Locale("fa", "IR");
                        final NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                       String c = numberFormatDutch.format(new BigDecimal(sum));
                        String cc = c.replace("ریال", "" + "\u200e");

                        price.setText(cc);
                        priceType.setText(" ریال");

                        Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                        Button no = (Button) dialog.findViewById(R.id.dialog_no);
                        // if button is clicked, close the custom dialog
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                dialog.dismiss();
                                if (mWifi.isConnected() || isMobileDataEnabled()) {

                                    err.setVisibility(View.GONE);
                                    App.dialog.show();
                                    pay();

                                } else {
                                    final Dialog dialog = new Dialog(AfzayeshEtebar.this,android.R.style.Theme_Black_NoTitleBar);
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

                                                err.setVisibility(View.GONE);
                                                App.dialog.show();
                                                pay();


                                                dialog.dismiss();
                                            }
                                        }
                                    });

                                    dialog.show();                                }
                            }
                            });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                App.dialog.hide();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        }else{
                    err.setVisibility(View.VISIBLE);
                }
            }
        });



        btn10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s10= 100000;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");

                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");

                } else {
                    s10= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                }
            }
        });
        btn20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s20= 200000;
                    sum = s10 + s20 + s30 + s50+s100+s200;
                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");



                } else {
                    s20= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                }
            }
        });
        btn30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s30= 300000;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");

                } else {
                    s30= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                }
            }
        });
        btn50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s50= 500000;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                } else {
                    s50= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                }
            }
        });
        btn100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s100= 1000000;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                } else {
                    s100= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");

                }
            }
        });
        btn200.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    s200= 2000000;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                } else {
                    s200= 0;
                    sum = s10 + s20 + s30 + s50+s100+s200;

                    String b = numberFormatDutch.format(new BigDecimal(sum));
                    String bb = b.replace("ریال", "" + "\u200e");

                    String a = numberFormatDutch.format(new BigDecimal(sum+etebarfelii));
                    String aa = a.replace("ریال", "" + "\u200e");


                    String styledText = " <font color='#330000'>"+bb+"</font>";

                    txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                    txtEtebarnaahaii.setText("اعتبار پس از افزایش " + (aa) + " ریال");


                }
            }
        });


        TextWatcher inputTextWatcherMatn = new TextWatcher() {
            public void afterTextChanged(Editable s) {

                try
                {
                    edtMablagh.removeTextChangedListener(this);
                    String value = edtMablagh.getText().toString();


                    if (value != null && !value.equals(""))
                    {

                        if(value.startsWith(".")){
                            edtMablagh.setText("0.");
                        }
                        if(value.startsWith("0") && !value.startsWith("0.")){
                            edtMablagh.setText("");

                        }


                        String str = edtMablagh.getText().toString().replaceAll(",", "");
                        if (!value.equals(""))
                            edtMablagh.setText(getDecimalFormattedString(str));
                        edtMablagh.setSelection(edtMablagh.getText().toString().length());
                    }

                    if(s.length()>0){
                        btn10.setEnabled(false);
                        btn10.setChecked(false);

                        btn20.setEnabled(false);
                        btn20.setChecked(false);

                        btn30.setEnabled(false);
                        btn30.setChecked(false);

                        btn50.setEnabled(false);
                        btn50.setChecked(false);

                        btn100.setEnabled(false);
                        btn100.setChecked(false);

                        btn200.setEnabled(false);
                        btn200.setChecked(false);

                        if(s.length()>0) {

                            String b = numberFormatDutch.format(new BigDecimal(Long.parseLong(s.toString().replace("," , "").replace("." ,"").replace("$" ,""))));
                            String bb = b.replace("ریال", "" + "\u200e");

                            String a = numberFormatDutch.format(new BigDecimal((Long.parseLong(s.toString().replace("," , "").replace("." ,"").replace("$" ,"")) + etebarfelii)));
                            String aa = a.replace("ریال", "" + "\u200e");


                            String styledText = " <font color='#330000'>"+bb+"</font>";

                            txtMeghdarEtebar.setText(Html.fromHtml("افزایش اعتبار به مبلغ " + "<b>"+styledText+"</b>" + " ریال"));
                            txtEtebarnaahaii.setText("اعتبار پس از افزایش " + aa + " ریال");

                            sum = Integer.parseInt(s.toString().replace("," , "").replace("$" ,"").replace("." ,""));
                        }

                    }
                    if(s.length()==0){

                        String b = numberFormatDutch.format(new BigDecimal(Integer.parseInt("0")));
                        String bb = b.replace("ریال", "" + "\u200e");

                        String a = numberFormatDutch.format(new BigDecimal((Integer.parseInt("0")+etebarfelii)));
                        String aa = a.replace("ریال", "" + "\u200e");



                        txtMeghdarEtebar.setText("افزایش اعتبار به مبلغ " + bb + " ریال");
                        txtEtebarnaahaii.setText("اعتبار پس از افزایش " + aa + " ریال");
                        sum = 0;

                    }

                    edtMablagh.addTextChangedListener(this);
                    return;
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    edtMablagh.addTextChangedListener(this);
                }




            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        };

        edtMablagh.addTextChangedListener(inputTextWatcherMatn);






    }




    /////////////// ZARINPAL ////////////////////////

    private void pay(){
        String str = String.valueOf(sum);
        if (str != null && str.length() > 0 ) {
            str = str.substring(0, str.length() - 1);
        }

        Payment payment = new PaymentBuilder()
                .setMerchantID("e6846e64-c7e8-11e7-863c-000c295eb8fc")  //  This is an example, put your own merchantID here.
                .setAmount(Integer.parseInt(str))                                        //  In Toman
                .setDescription("پرداخت آنلاین شیراز سرویس")
                .setEmail("info@shiraz-service.ir")                     //  This field is not necessary.
                .setMobile(App.mobile)                               //  This field is not necessary.
                .create();
        //   dialog.hide();

        ZarinPal.pay(this, payment, new OnPaymentListener() {
            @Override
            public void onSuccess(String refID) {
                peygiri=refID.toString();
                Log.wtf("TAG", "::ZarinPal::  RefId: " + refID);
                App.dialog.dismiss();
                AfzayeshEtebar.AsyncCallWSinsrtDepositMoney task = new AfzayeshEtebar.AsyncCallWSinsrtDepositMoney();
                task.execute();


            }
            @Override
            public void onFailure(ZarinPalError error) {
                String errorMessage = "";
                switch (error){
                    case INVALID_PAYMENT: errorMessage = "پرداخت تایید نشد";           break;
                    case USER_CANCELED:   errorMessage = "پرداخت توسط کاربر متوقف شد"; break;
                    case NOT_ENOUGH_DATA: errorMessage = "اطلاعات پرداخت کافی نیست";    break;
                    case UNKNOWN:         errorMessage = "خطای ناشناخته";              break;
                }
                App.dialog.dismiss();
                Log.wtf("TAG", "::ZarinPal::  ERROR: " + errorMessage);
                //   textView.setText("خطا!!!" + "\n" + errorMessage);
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
///////////////////////////////////////////////////

    private class AsyncCallWSinsrtDepositMoney extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
//            pd.show();
            insrtDepositMoney();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
  //          pd.hide();

        }

    }

    public void insrtDepositMoney() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#insrtDepositMoney";
        String METHOD_NAME = "insrtDepositMoney";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs2 = getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            final String fullname = prefs2.getString("fullname", "0");

            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            InputStream stream = new ByteArrayInputStream(fullname.getBytes());

            String a = getResponseString(stream);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("servicemanId",prefs2.getString("servicemanId", "0"));
            Request.addProperty("type", "3");
            Request.addProperty("cardNo", " ");
            Request.addProperty("fullName", a);
            Request.addProperty("trackingCode", peygiri);
            Request.addProperty("price", sum);
            Request.addProperty("depositTime", currentDateTimeString);
            Request.addProperty("ip", " ");
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


            if(result.getPropertyAsString("res").equals("0")) {

                new Thread()
                {
                    public void run()
                    {
                        AfzayeshEtebar.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                App.CustomToast(result.getPropertyAsString("resStr"));
                                String credit = result.getPropertyAsString("newCredit").toString();
                                SharedPreferences.Editor editor = getSharedPreferences("INFO", MODE_PRIVATE).edit();
                                editor.putString("credit", result.getPropertyAsString("newCredit"));
                                editor.commit();

                                MainActivity.h.sendEmptyMessage(1);

                                Locale farsi = new Locale("fa", "IR");
                                NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                                String c = numberFormatDutch.format(new BigDecimal(result.getPropertyAsString("newCredit")));
                                final String cc = c.replace("ریال", "" + "\u200e");

                                txtEtebarMenu.setText("اعتبار "+cc+" ریال");

                                Intent intent = new Intent(AfzayeshEtebar.this, MainActivity.class);
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
                        AfzayeshEtebar.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
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
    private String getResponseString(InputStream stream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"ISO-8859-1"));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        finally {
            stream.close();
        }
        return sb.toString();
    }


    public static String getDecimalFormattedString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    public static String trimCommaOfString(String string) {
//        String returnString;
        if(string.contains(",")){
            return string.replace(",","");}
        else {
            return string;
        }

    }


}
