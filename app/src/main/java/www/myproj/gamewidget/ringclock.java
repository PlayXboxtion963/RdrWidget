package www.myproj.gamewidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ringclock extends Service {
    public ringclock() {
    }
    Timer timer=new Timer();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /** 当服务被创建时调用. */
    @Override
    public void onCreate() {
        System.out.println("启动服务");

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        }, 1000, 1000*60);

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            if(msg.what == 0){
                System.out.println("尝试更新");
                //这里可以进行UI操作，如Toast，Dialog等
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                RemoteViews rvs = new RemoteViews(getPackageName(),R.layout.pocketclock);
                Bitmap hourx= BitmapFactory.decodeResource(getResources(), R.drawable.hour);
                hourx=rotateBitmap(hourx,(hour + new Float(minute) / 60) * 360 / 12);
                rvs.setImageViewBitmap(R.id.widget_hour,hourx);
                Bitmap minutex= BitmapFactory.decodeResource(getResources(), R.drawable.minutes);
                minutex=rotateBitmap(minutex,((minute + new Float(second) / 60) * 360 / 60));
                rvs.setImageViewBitmap(R.id.widget_minutes,minutex);
                rvs.setImageViewResource(R.id.clocktop,R.drawable.clocktop);
                rvs.setImageViewResource(R.id.widget_pocketclock,R.drawable.pocketclockback);
                AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName cn = new ComponentName(getApplicationContext(),pocketclock.class);
                manager.updateAppWidget(cn,rvs);

            }
        }
    };

    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        System.out.println("停止服务");
    }
}