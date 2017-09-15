package com.alizare.server;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by behroozhj on 9/8/17.
 */

public class App extends Application {

    public static   Context             context;
    public static   SharedPreferences   preferences;
    public static   Typeface            appfont;
    public static   String              username;
    public static   String              loginname;
    public static   String              complexname;
    public static   String              serverdate;
    public static   int                 kol;
    public static   int                 cnt;
    public static   String              karbaran;
    public static   String              serverAddress="http://192.168.0.246/matbakh/public/api";


    @Override
    public void             onCreate() {
        super.onCreate();

        context= getApplicationContext();
        appfont= getFont();
        overrideFont(context,"SERIF", "sansfarsi.ttf");


    }

    public static void      overrideFont( Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("TypefaceUtil", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }

    public static void      setAllTextView(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                setAllTextView((ViewGroup) child);
            } else if (child instanceof TextView) {
                ((TextView) child).setTypeface(getFont());
            }
        }
    }

    public static Typeface  getFont() {
        return Typeface.createFromAsset(context.getAssets(), "sansfarsi.ttf");
    }

    public static void      CustomToast(String messgae) {

        LinearLayout layout=new LinearLayout(context);
        layout.setBackgroundColor(Color.parseColor("#392323"));
        TextView view=new TextView(context);
        view.setText(messgae);
        view.setTextColor(Color.parseColor("#FFFFFF"));
        view.setTextSize(18);
        view.setTypeface(appfont);
        view.setPadding(20, 20, 20, 20);
        view.setGravity(Gravity.CENTER);
        layout.addView(view);
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void      newactivity(Context context,Class activity){

        Intent intent =new Intent(context,activity);
        context.startActivity(intent);

    }

    public static String numberformatlong(long input){

        return NumberFormat.getNumberInstance(Locale.US).format(input);

    }

    public static String numberformatStr(String input){

        return NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(input));

    }




}
