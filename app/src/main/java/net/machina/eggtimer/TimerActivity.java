package net.machina.eggtimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.machina.eggtimer.common.Constants;
import net.machina.eggtimer.common.TimeUtils;

public class TimerActivity extends AppCompatActivity {

    protected TextView textTimeLeft;
    protected ProgressBar progressBar;
    protected long time;
    protected CountDownTimer timer;
    protected boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.label_countdown);
        textTimeLeft = (TextView) findViewById(R.id.textTimeLeft);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(this.getIntent().getExtras() == null) finish();
        else {
            time = this.getIntent().getExtras().getLong("time");
            Log.i(Constants.LOGGER_TAG, "Czas: " + time);
            progressBar.setMax((int) (time / 1000));
            timer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(final long millisUntilFinished) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textTimeLeft.setText(TimeUtils.getTimeString(millisUntilFinished / 1000));
                            progressBar.setProgress((int)((time / 1000) - (millisUntilFinished / 1000)));
                        }
                    });
                }

                @Override
                public void onFinish() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textTimeLeft.setText(R.string.label_countdown_end_short);
                            progressBar.setProgress(progressBar.getMax());
                        }
                    });

                    isFinished = true;

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerActivity.this);
                    builder.setSmallIcon(R.drawable.ic_alarm)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getString(R.string.label_countdown_end))
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notifier.notify(Constants.NOTIFICATION_ID, builder.build());
                }
            };

            timer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public static long back_pressed;
    @Override
    public void onBackPressed()
    {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            timer.cancel();
            super.onBackPressed();
        }
        else if(isFinished) super.onBackPressed();
        else Toast.makeText(this, getString(R.string.label_countdown_interrupt), Toast.LENGTH_SHORT).show();

        back_pressed = System.currentTimeMillis();
    }
}
