package com.moviecatalogue.showin.activity.remindersettings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.moviecatalogue.showin.R;

import java.util.Calendar;

public class ReminderSettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reminder_setting);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.reminder_settings);
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.release))) {
            releaseServices(sharedPreferences.getBoolean(s, false));
        } else if (s.equals(getString(R.string.daily))) {
            dailyReminder(sharedPreferences.getBoolean(s, false));
        }
    }

    private void releaseServices(Boolean handle) {
        if (handle) {
            enableReleaseServices();
        } else {
            disableReleaseService();
        }
    }

    private void dailyReminder(Boolean handle) {
        if (handle) {
            enableDailyReminder();
        } else {
            disableDailyReminder();
        }
    }

    private void enableReleaseServices() {
        setAlarm(ReleaseTodaySchedulerReceiver.class, ReleaseTodaySchedulerReceiver.NOTIF_ID_RELEASE, 8, 0);
        Toast.makeText(this, R.string.enable_notification, Toast.LENGTH_SHORT).show();
    }

    private void enableDailyReminder() {
        setAlarm(DailySchedulerReceiver.class, DailySchedulerReceiver.NOTIF_ID_DAILY, 7, 0);
        Toast.makeText(this, R.string.enable_reminder, Toast.LENGTH_SHORT).show();
    }

    private void disableReleaseService() {
        stopAlarm(ReleaseTodaySchedulerReceiver.class, ReleaseTodaySchedulerReceiver.NOTIF_ID_RELEASE);
        Toast.makeText(this, R.string.disable_notification, Toast.LENGTH_SHORT).show();
    }

    private void disableDailyReminder() {
        stopAlarm(DailySchedulerReceiver.class, DailySchedulerReceiver.NOTIF_ID_DAILY);
        Toast.makeText(this, R.string.disable_reminder, Toast.LENGTH_SHORT).show();
    }

    private void setAlarm(Class clss, int notifId, int hours, int minutes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, clss);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifId, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void stopAlarm(Class clss, int notifId) {
        Intent intent = new Intent(this, clss);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notifId, intent, 0);
        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.reference_reminder);
        }
    }
}
