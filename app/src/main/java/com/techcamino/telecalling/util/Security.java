package com.techcamino.telecalling.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.techcamino.telecalling.R;

import java.io.File;


public class Security {

    private static final String SODPREFRENCES = "ABERIS";

    /**
     * Sharedpreferences start
     */
    public static boolean savePrefrences(String key, String values, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, values);
        editor.apply();
        return true;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean saveBooleanPrefrences(String key, boolean values, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, values);
        editor.apply();
        return true;
    }

    public static boolean getBooleanPref(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        return sharedPreferences.getBoolean(key,false);
    }
    /*public static boolean saveLong(String key,Long values , Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key , values );
        editor.apply();
        return true;
    }
    public static long getLong(String key , Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        return sharedPreferences.getLong(key , 0);

    }*/

    public static boolean savePrefrences(String key, int values, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, values);
        editor.apply();
        return true;
    }

    public static String getPreference(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);

        return sharedPreferences.getString(key, "missing");
    }

    public static int getIntPreference(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        return sharedPreferences.getInt(key, Integer.MIN_VALUE);
    }

    public static boolean deletePrefrences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                SODPREFRENCES, 0);
        return sharedPreferences.edit().clear().commit();
    }

    /**
     * Sharedprefrences End
     */

	/* Progress Bar */
    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message.toString());
        dialog.setIndeterminate(false);
        dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.logo));
        dialog.setCancelable(false);
        return dialog;
    }
    /* End of progress Bar*/

    public static Dialog loadDialog(Activity context){
        Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.progress_dialog);

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(context)
                .load(R.drawable.wait)
                .placeholder(R.drawable.wait)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);

        //...finaly show it
        //dialog.show();
        return  dialog;
    }


    public static void showError(String titleMsg,String errorMsg,Context context)
    {
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(titleMsg);

        // set values for custom dialog components - text, image and button
        //String fontPath = context.getString(R.string.tf_normal);
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        //Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        //text.setTypeface(tf);

        text.setText(errorMsg);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(R.drawable.logo);

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

    }

    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }


    public static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        //return formateMilliSeccond(Long.parseLong(durationStr)); // if direct duration is needed
        return String.valueOf(Long.parseLong(durationStr)); //if nanoseconds is needed
    }
}
