package www.myproj.gamewidget;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        }, 1000, 2000);

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
                ImageView hourx=findViewById(R.id.hour);
                hourx.setRotation((hour + new Float(minute) / 60) * 360 / 12);
                ImageView minutesx=findViewById(R.id.minute);
                minutesx.setRotation((minute + new Float(second) / 60) * 360 / 60);
            }
        }
    };
}