package com.rahbaran.shirazservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rahbaran.shirazservice.App;
import com.rahbaran.shirazservice.MyService;
import com.rahbaran.shirazservice.R;
import com.rahbaran.shirazservice.activities.MyServices;
import com.rahbaran.shirazservice.activities.MyServicesDetailes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyServiseAdapter extends RecyclerView.Adapter<MyServiseAdapter.PosterHolder> {

    static List<MyService> mDataset;
    static List<MyService> filterPoster;
    Context context;

    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        private final CircleImageView img;
        private final CardView item;
        private final TextView areaName;
        private final TextView Time;
        private final TextView Title;
        private final TextView State;
        private final RatingBar ratingbar1;


        public PosterHolder(View itemView) {
            super(itemView);

            areaName = (TextView) itemView.findViewById(R.id.txt_area);
            Title = (TextView) itemView.findViewById(R.id.txt_title);
            Time = (TextView) itemView.findViewById(R.id.txt_time);
            State = (TextView) itemView.findViewById(R.id.txt_state);
            item = (CardView) itemView.findViewById(R.id.ll_row);
            img = (CircleImageView) itemView.findViewById(R.id.imageViewR);
            ratingbar1=(RatingBar)itemView.findViewById(R.id.ratingBar1);


        }
    }

    public MyServiseAdapter(Context context, List<MyService> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterPoster=new ArrayList<>(myDataset);

    }

    public void filter(String searchKeyword){
        searchKeyword=searchKeyword.toLowerCase();
        if (searchKeyword.isEmpty()){
            filterPoster=new ArrayList<>(mDataset);
        }else {
            filterPoster=new ArrayList<>();
            for (int i = 0; i < mDataset.size(); i++) {
                String title =mDataset.get(i).serviceTitle;

                if (title.toLowerCase().contains(searchKeyword)){
                    filterPoster.add(mDataset.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_service, parent, false);
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

            if(lineCount>=2){
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



    if(filterPoster.get(position).state.toString().equals("1") || filterPoster.get(position).state.toString().equals("3") || filterPoster.get(position).state.toString().equals("2")){
        holder.State.setBackgroundDrawable(App.context.getResources().getDrawable(R.drawable.frame_orange) );
        holder.State.setBackgroundResource(R.drawable.frame_orange);

        holder.State.setText("منتظر تایید");
       // holder.State.setTextColor(Color.parseColor("#e69900"));
        holder.ratingbar1.setVisibility(View.GONE);

    }
    if(filterPoster.get(position).state.toString().equals("4")){
       // holder.State.setBackgroundDrawable(App.context.getResources().getDrawable(R.drawable.frame_ok) );
        holder.State.setBackgroundResource(R.drawable.frame_ok);

        // holder.State.setTextColor(Color.parseColor("#4d4d4d"));
        holder.State.setText("تایید شده");
        holder.ratingbar1.setVisibility(View.GONE);

    }
    if(filterPoster.get(position).state.toString().equals("6")){
       // holder.State.setBackgroundDrawable(App.context.getResources().getDrawable(R.drawable.frame_green) );
        holder.State.setBackgroundResource(R.drawable.frame_green);
       // holder.State.setTextColor(Color.parseColor("#006600"));
        holder.State.setText("انجام شده");
        holder.ratingbar1.setVisibility(View.VISIBLE);
        holder.ratingbar1.setRating(Float.parseFloat(filterPoster.get(position).rate));



    }
    if(filterPoster.get(position).state.toString().equals("5")){
        // state.setBackgroundColor(Color.parseColor("#6b6b47"));
        holder.ratingbar1.setVisibility(View.GONE);

    }


}catch(Exception e){}

    holder.item.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(App.context, MyServicesDetailes.class);
            MyServices.requestId = filterPoster.get(position).reqId;
            MyServices.state = filterPoster.get(position).state;
            MyServices.serviceId = filterPoster.get(position).serviceId;
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.context.startActivity(intent);
            }catch (Exception e){}

        }
    });
    }

    public void update(List<MyService> list) {
        filterPoster = list;

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }


}
