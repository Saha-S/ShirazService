package com.rahbaran.shirazservice.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.rahbaran.shirazservice.Request;
import com.rahbaran.shirazservice.activities.MainActivity;
import com.rahbaran.shirazservice.activities.MyServices;
import com.rahbaran.shirazservice.activities.RequestDetails;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by nadia on 3/2/2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.PosterHolder> {

    static List<Request> mDataset;
    static List<Request> filterPoster;
    Context context;

    private int lastPosition = -1;
    private String requestId;
    private String res;
    private String resStr;
    private String newCredit;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        private final CircleImageView img;
        private final Button select;
        private final CardView item;
        private final TextView areaName;
        private final TextView Time;
        private final TextView Title;


        public PosterHolder(View itemView) {
            super(itemView);

            areaName = (TextView) itemView.findViewById(R.id.txt_area);
            Title = (TextView) itemView.findViewById(R.id.txt_title);
            Time = (TextView) itemView.findViewById(R.id.txt_insert_time);
            item = (CardView) itemView.findViewById(R.id.ll_row);
            select = (Button) itemView.findViewById(R.id.btn_select);
            img = (CircleImageView) itemView.findViewById(R.id.imageViewR);

        }
    }


    public RequestAdapter(Context context, List<Request> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterPoster = new ArrayList<>(myDataset);

    }

    public void filter(String area , String cat , String sub , String olaviat) {
        area = area.toLowerCase();
        cat = cat.toLowerCase();
        sub = sub.toLowerCase();
        olaviat = olaviat.toLowerCase();

            filterPoster = new ArrayList<>();
            for (int i = 0; i < mDataset.size(); i++) {
                String areaS = mDataset.get(i).areaId;
                String catS = mDataset.get(i).catId;
                String subS = mDataset.get(i).subCatId;
                String olaviatS = mDataset.get(i).priority;

                if (areaS.toLowerCase().contains(area) && catS.toLowerCase().contains(cat) && subS.toLowerCase().contains(sub) && olaviatS.toLowerCase().contains(olaviat)) {
                    filterPoster.add(mDataset.get(i));
                }

        }
        notifyDataSetChanged();
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_requests, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {
        try {
            holder.Title.setText(filterPoster.get(position).serviceTitle);


            holder.Title.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = holder.Title.getLineCount();

                    if (lineCount >= 2) {
                        holder.Title.setLines(2);
                    }
                }
            });

            holder.areaName.setText(filterPoster.get(position).areaTitle);
            holder.Time.setText(filterPoster.get(position).insrtTimeSimple);


            if(!filterPoster.get(position).servicePicAddress.isEmpty()) {
                Picasso.with(context)
                        .load(filterPoster.get(position).servicePicAddress)
                        .placeholder(R.drawable.nopic)
                        .into(holder.img);
            }else{
                Picasso.with(context)
                        .load(R.drawable.nopic)
                        .placeholder(R.drawable.nopic)
                        .into(holder.img);

            }
        } catch (Exception e) {
        }


        requestId = filterPoster.get(position).requestId;
        Locale farsi = new Locale("fa", "IR");
        NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

        String c = numberFormatDutch.format(new BigDecimal(filterPoster.get(position).calculatedPrice.toString()));
        final String cc = c.replace("ریال", " " + "\u200e");

        MainActivity.price = cc;
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                App.dialog = new Dialog(view.getRootView().getContext());
                App.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                App.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                App.dialog.setCancelable(false);
                App.dialog.setContentView(R.layout.loading);

                final Dialog dialog = new Dialog(view.getRootView().getContext());
                dialog.setContentView(R.layout.dialog);

                dialog.setTitle("انتخاب درخواست");

                // set the custom dialog components - text, image and button
                TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
                TextView txt1 = (TextView) dialog.findViewById(R.id.dialog_txt);
                TextView txt2 = (TextView) dialog.findViewById(R.id.dialog_txt2);
                TextView price = (TextView) dialog.findViewById(R.id.dialog_price);
                TextView priceType = (TextView) dialog.findViewById(R.id.dialog_price_type);
                //  title.setText("افزایش اعتبار");
                txt1.setText("با انتخاب این سرویس مبلغ");
                txt2.setText("از حساب شما کسر خواهد شد. آیا مطمئن هستید؟");
                price.setText(cc);
                priceType.setText(" ریال");

                Button yes = (Button) dialog.findViewById(R.id.dialog_yes);
                Button no = (Button) dialog.findViewById(R.id.dialog_no);
                // if button is clicked, close the custom dialog
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                        if (mWifi.isConnected() || isMobileDataEnabled()) {
                            App.dialog.show();

                            dialog.dismiss();

                            AsyncCallWSSelectRequest task = new AsyncCallWSSelectRequest();
                            task.execute();
                        }else {
                            final Dialog dialog2 = new Dialog(App.context,android.R.style.Theme_Black_NoTitleBar);
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog2.setContentView(R.layout.internetdialog);



                            Button yes = (Button) dialog2.findViewById(R.id.dialog_yes);
                            Button no = (Button) dialog2.findViewById(R.id.dialog_no);
                            // if button is clicked, close the custom dialog
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                                    final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                    if (mWifi.isConnected() || isMobileDataEnabled()) {

                                        App.dialog.show();

                                        dialog.dismiss();

                                        AsyncCallWSSelectRequest task = new AsyncCallWSSelectRequest();
                                        task.execute();

                                        dialog2.dismiss();
                                    }
                                }
                            });

                            dialog2.show();                        }


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

            }
        });


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context, RequestDetails.class);
                MainActivity.requestId = filterPoster.get(position).requestId;
                MainActivity.stitle = filterPoster.get(position).serviceTitle;
                MainActivity.area = filterPoster.get(position).areaTitle;
                MainActivity.stat = filterPoster.get(position).stateTitle;
                MainActivity.desc = filterPoster.get(position).desc;
                MainActivity.time = filterPoster.get(position).insrtTimePersian;
                MainActivity.cat = filterPoster.get(position).catTitle;
                MainActivity.olaviat = filterPoster.get(position).priorityTitle;
                MainActivity.price = filterPoster.get(position).calculatedPrice;
                MainActivity.timeDesc = filterPoster.get(position).timeDesc;
                MainActivity.timeDesc = filterPoster.get(position).timeDesc;
                MainActivity.dateFromPersian   = filterPoster.get(position).dateFromPersian  ;
                MainActivity.dateToPersian  = filterPoster.get(position).dateToPersian ;
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);

            }
        });
    }

    public void update(List<Request> list) {
        filterPoster = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }




    ////////////////

    private class AsyncCallWSSelectRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            selectService();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            App.dialog.hide();

            if(res.equals("0")){
                App.CustomToast(resStr);
                Intent intent = new Intent(App.context, MyServices.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                SharedPreferences.Editor editor = App.context.getSharedPreferences("INFO", MODE_PRIVATE).edit();
                editor.putString("credit", newCredit);
                editor.commit();

                MainActivity.h.sendEmptyMessage(1);
                App.context.startActivity(intent);

            }else{
                App.CustomToast(resStr);
            }




        }

    }

    public void selectService() {

        String SOAP_ACTION = "http://shiraz-service.ir/webServiceServer.php?wsdl#pickRequestByServiceMan";
        String METHOD_NAME = "pickRequestByServiceMan";
        String NAMESPACE = "http://shiraz-service.ir/webServiceServer.php?wsdl";
        String URL = "http://shiraz-service.ir/webServiceServer.php?wsdl";

//stuff that updates ui


        try {
            SharedPreferences prefs = App.context.getSharedPreferences("INFO", MODE_PRIVATE);

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("tokenId", "2085");
            Request.addProperty("tokenKey", "W/*@!&R~k^Ma$#._=N");
            Request.addProperty("requestId", requestId);
            Request.addProperty("servicemanId", prefs.getString("servicemanId", "0"));
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

            res = result.getPropertyAsString("res");
            resStr = result.getPropertyAsString("resStr");
            if(res.equals("0")){
            newCredit = result.getPropertyAsString("newCredit");
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