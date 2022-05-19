package www.myproj.gamewidget;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextPaint;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class Refresh extends AppCompatActivity {
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    final Context context = Refresh.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        moveTaskToBack(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //判断app widget ID是否为空.(App Widget 是否添加到Launched Activity)
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.r_d_r2_main_clock);
        views.setImageViewResource(R.id.RDR2stylemainclock,R.drawable.mainclock);
        SharedPreferences userInfo = context.getSharedPreferences("locate", 0);
        getweather(views, userInfo.getString("truelocate","110000"));
        Toast.makeText(context, "尝试更新中", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
    private void getweather(RemoteViews views, String locate){
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
                        AssetManager mgr = getAssets();
                        Bitmap mbit=null;
                        try {
                            mbit = createTextBitmap(mx.location, Typeface.createFromAsset(mgr, "fonts/myfont.ttf"), 100, Color.WHITE);
                        }finally {
                            views.setImageViewBitmap(R.id.imageView,mbit);
                        }
                        views.setViewVisibility(R.id.textView2, View.INVISIBLE);

                    }
                    Looper.prepare();
                    if(mx.statue){
                    Toast.makeText(Refresh.this, mx.location+mx.city+mx.temp, Toast.LENGTH_SHORT).show();}
                    else {
                        Toast.makeText(Refresh.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
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
    /**
     * Creates and returns a new bitmap containing the given text.
     */
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