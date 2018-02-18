package com.rahbaran.shirazservice;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by behroozhj on 9/8/17.
 */

public class App extends Application {

    public static   Context             context;
    public static   SharedPreferences   preferences;
    public static   Typeface            appfont;
    public static   String              versionName;
    public static   String              instagram;
    public static   String              website;
    public static   String              telegram;
    public static   String              mobile;
    public static   int              versioCode;
    public static Dialog dialog;
    private PackageInfo pInfo;


    @Override
    public void             onCreate() {
        super.onCreate();

        context= getApplicationContext();
        appfont= getFont();
       // overrideFont(context,"SERIF", "sansfarsi.ttf");



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("sansfarsi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName= pInfo.versionName;
            versioCode= pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


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
        layout.setBackgroundColor(Color.parseColor("#0059b3"));
        TextView view=new TextView(context);
        view.setText(messgae);
        view.setTextColor(Color.parseColor("#ffffff"));
        view.setTextSize(15);
        view.setTypeface(appfont);
        view.setPadding(18, 18, 18, 18);
        view.setGravity(Gravity.CENTER);
        layout.addView(view);
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
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
