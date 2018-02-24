package com.rahbaran.shirazservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.R;
import com.rahbaran.shirazservice.Transaction;
import com.rahbaran.shirazservice.activities.MainActivity;
import com.rahbaran.shirazservice.activities.MyServices;
import com.rahbaran.shirazservice.activities.MyServicesDetailes;
import com.rahbaran.shirazservice.activities.RequestDetails;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nadia on 3/2/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.PosterHolder> {

    static List<Transaction> mDataset;
    static List<Transaction> filterPoster;
    Context context;

    private int lastPosition = -1;
    private String requestId;
    private String res;
    private String resStr;
    private String newCredit;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        private final TextView action;
        private final TextView price;
        private final TextView time;
        private final TextView type;
        private final TextView desc;
        private final TextView Adesc;
        private final TextView credit;
        private final LinearLayout request;
        private final LinearLayout ll_advancedesc;
        private final LinearLayout item;


        public PosterHolder(View itemView) {
            super(itemView);

            action = (TextView) itemView.findViewById(R.id.t_action);
            price = (TextView) itemView.findViewById(R.id.t_price);
            time = (TextView) itemView.findViewById(R.id.t_time);
            type = (TextView) itemView.findViewById(R.id.t_type);
            desc = (TextView) itemView.findViewById(R.id.t_desc);
            Adesc = (TextView) itemView.findViewById(R.id.t_advancedesc);
            credit = (TextView) itemView.findViewById(R.id.t_credit);
            request = (LinearLayout) itemView.findViewById(R.id.request);
            ll_advancedesc = (LinearLayout) itemView.findViewById(R.id.ll_advancedesc);
            item = (LinearLayout) itemView.findViewById(R.id.ll_item);

        }
    }


    public TransactionAdapter(Context context, List<Transaction> myDataset) {
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
          //  for (int i = 0; i < mDataset.size(); i++) {
            //    String areaS = mDataset.get(i).areaId;
              //  String catS = mDataset.get(i).catId;
            //    String subS = mDataset.get(i).subCatId;
           //     String olaviatS = mDataset.get(i).priority;

            //    if (areaS.toLowerCase().contains(area) && catS.toLowerCase().contains(cat) && subS.toLowerCase().contains(sub) && olaviatS.toLowerCase().contains(olaviat)) {
            //        filterPoster.add(mDataset.get(i));
           //     }

      //  }
      //  notifyDataSetChanged();
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {


        if (filterPoster.get(position).advanceDesc.isEmpty()){
            holder.ll_advancedesc.setVisibility(View.GONE);
        }else{
            holder.ll_advancedesc.setVisibility(View.VISIBLE);
            holder.Adesc.setText(filterPoster.get(position).advanceDesc);

        }

        try {
            Locale farsi = new Locale("fa", "IR");
            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

            holder.desc.setText(filterPoster.get(position).desc);
            holder.action.setText(filterPoster.get(position).actionTitle);
            String d = numberFormatDutch.format(new BigDecimal(filterPoster.get(position).remainCredit.toString()));
            final String dd = d.replace("ریال", " ریال " + "\u200e");

            holder.credit.setText(Html.fromHtml("<b>" + dd + "</b> "));
            holder.type.setText(filterPoster.get(position).typeTitle);
            holder.time.setText(filterPoster.get(position).insrtTimePersian);

            String c = numberFormatDutch.format(new BigDecimal(filterPoster.get(position).price.toString()));
            final String cc = c.replace("ریال", " ریال " + "\u200e");
            final String ccc = cc.replace("−\u200E ریال \u200E", "\u200E ریال -\u200E" + "\u200e");

            if(filterPoster.get(position).actionTitle.equals("برداشت")){
                holder.item.setBackgroundColor(Color.parseColor("#ffebe6"));
            }
            if(filterPoster.get(position).actionTitle.equals("واریز")){
                holder.item.setBackgroundColor(Color.parseColor("#e6ffee"));
            }
            holder.price.setText(Html.fromHtml("<b>" + ccc + "</b> ") );
            if(filterPoster.get(position).requestId.toString().equals("0")){
                holder.request.setVisibility(View.GONE);
            }else{
                holder.request.setVisibility(View.VISIBLE);
            }

            holder.request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(App.context , RequestDetails.class);
                    MainActivity.requestId = filterPoster.get(position).requestId;
                   // MyServices.state = "00";
                    MyServices.serviceId = "";
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    App.context.startActivity(intent);
                }
            });


        } catch (Exception e) {
        }





    }

    public void update(List<Transaction> list) {
        filterPoster = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }





}