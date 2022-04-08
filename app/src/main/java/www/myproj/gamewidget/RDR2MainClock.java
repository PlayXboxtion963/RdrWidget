package www.myproj.gamewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.view.View;
import android.widget.RemoteViews;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Implementation of App Widget functionality.
 */
public class RDR2MainClock extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.r_d_r2_main_clock);
        views.setImageViewResource(R.id.RDR2stylemainclock,R.drawable.mainclock);
        Intent intent=new Intent(context, Refresh.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent,  PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.button2,pendingIntent);
        SharedPreferences userInfo = context.getSharedPreferences("locate", 0);
        AssetManager mgr = context.getAssets();
            getweather(views, userInfo.getString("truelocate","110000"),mgr);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        System.out.println("接收到广播");
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
     public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                 System.out.println("www");

    }

    private static void getweather(RemoteViews views,String locate, AssetManager mgr){
        HttpDownloader mhttp=new HttpDownloader();

        new Thread() {
            @Override
            public void run() {
                String result;
                //这里写入子线程需要做的工作

                result=mhttp.download("https://restapi.amap.com/v3/weather/weatherInfo?city="+locate+"&key=8727a3ee6c9ad0194a3d935899d7778e&output=xml");
                try {
                    XMLdepress mx=new XMLdepress();
                    mx.saxdrpess(result);
                    String[] mresult=new String[]{mx.location, mx.city, mx.temp};
                    System.out.println(mx.location+mx.city+mx.temp);
                    views.setTextViewText(R.id.tempxx,"|"+mx.temp+"℃");
                    views.setTextViewText(R.id.textView3,mx.city);
                    views.setTextViewText(R.id.textView2,mx.location);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

                        Bitmap mbit=null;
                        try {
                            mbit = createTextBitmap(mx.location, Typeface.createFromAsset(mgr, "fonts/myfont.ttf"), 100, Color.WHITE);
                        }finally {
                            views.setImageViewBitmap(R.id.imageView,mbit);
                        }
                        views.setViewVisibility(R.id.textView2, View.INVISIBLE);

                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    public static Bitmap createTextBitmap(final String text, final Typeface typeface, final float textSizePixels, final int textColour)
    {
        final TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(textSizePixels);
        textPaint.setAntiAlias(true);
        textPaint.setSubpixelText(true);
        textPaint.setColor(textColour);
        textPaint.setTextAlign(Paint.Align.LEFT);
        Bitmap myBitmap = Bitmap.createBitmap((int) textPaint.measureText(text), (int) textSizePixels, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        myCanvas.drawText(text, 0, myBitmap.getHeight(), textPaint);
        System.out.println("finad");
        return myBitmap;
    }
}