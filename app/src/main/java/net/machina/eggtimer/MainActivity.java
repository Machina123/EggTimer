package net.machina.eggtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.machina.eggtimer.common.Constants;
import net.machina.eggtimer.common.Egg;
import net.machina.eggtimer.common.TimeUtils;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected Spinner spinEggTemp;
    protected Spinner spinEggDoneness;
    protected Spinner spinEggSize;
    protected TextView textTime;
    protected SharedPreferences preferences;
    protected long timeMilliseconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        preferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        spinEggTemp = (Spinner) findViewById(R.id.spinBeginTemp);
        spinEggDoneness = (Spinner) findViewById(R.id.spinDoneness);
        spinEggSize = (Spinner) findViewById(R.id.spinEggSize);
        textTime = (TextView) findViewById(R.id.textTime);
        if(spinEggTemp != null && spinEggDoneness != null && spinEggSize != null && textTime != null) generateFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.action_start:
                Bundle extras = new Bundle();
                extras.putLong("time", timeMilliseconds);
                Intent launchIntent = new Intent(MainActivity.this, TimerActivity.class);
                launchIntent.putExtras(extras);
                startActivity(launchIntent);
                return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinBeginTemp:
                Egg.BeginTemp beginTemp = Egg.BeginTemp.fromId(position);
                preferences.edit().putInt(Constants.PREF_EGGTEMP, position).apply();
                Log.i(Constants.LOGGER_TAG, beginTemp.toString());
                break;
            case R.id.spinDoneness:
                Egg.Doneness doneness = Egg.Doneness.fromId(position);
                preferences.edit().putInt(Constants.PREF_DONENESS, position).apply();
                Log.i(Constants.LOGGER_TAG, doneness.toString());
                break;
            case R.id.spinEggSize:
                Egg.Size size = Egg.Size.fromId(position);
                preferences.edit().putInt(Constants.PREF_EGGSIZE, position).apply();
                Log.i(Constants.LOGGER_TAG, size.toString());
                break;
        }
        recalculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public void generateFields() {

        // Generowanie pól z temperaturą jaj
        ArrayAdapter<CharSequence> eggBeginTempArrayAdapter = ArrayAdapter.createFromResource(this, R.array.egg_temp, android.R.layout.simple_spinner_item);
        eggBeginTempArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEggTemp.setAdapter(eggBeginTempArrayAdapter);
        spinEggTemp.setOnItemSelectedListener(this);

        int prevTemp = preferences.getInt(Constants.PREF_EGGTEMP, 0);
        spinEggTemp.setSelection(prevTemp);

        // Generowanie pól z poziomem ścięcia jaj
        ArrayAdapter<CharSequence> eggDonenessArrayAdapter = ArrayAdapter.createFromResource(this, R.array.egg_doneness, android.R.layout.simple_spinner_item);
        eggDonenessArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEggDoneness.setAdapter(eggDonenessArrayAdapter);
        spinEggDoneness.setOnItemSelectedListener(this);

        int prevDoneness = preferences.getInt(Constants.PREF_DONENESS, 0);
        spinEggDoneness.setSelection(prevDoneness);

        // Generowanie pól z wielkościami jaj
        ArrayAdapter<CharSequence> eggSizeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.egg_size, android.R.layout.simple_spinner_item);
        eggSizeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinEggSize.setAdapter(eggSizeArrayAdapter);
        spinEggSize.setOnItemSelectedListener(this);

        int prevSize = preferences.getInt(Constants.PREF_EGGSIZE, 0);
        spinEggSize.setSelection(prevSize);

        recalculate();
    }

    public void recalculate() {
        Egg.Size size = Egg.Size.fromId(spinEggSize.getSelectedItemPosition());
        Egg.Doneness doneness = Egg.Doneness.fromId(spinEggDoneness.getSelectedItemPosition());
        Egg.BeginTemp beginTemp = Egg.BeginTemp.fromId(spinEggTemp.getSelectedItemPosition());

        double resultTimeSeconds = Egg.calculateTime(size, doneness, beginTemp);
        timeMilliseconds = (long) (resultTimeSeconds * 1000);
        textTime.setText(TimeUtils.getTimeString(resultTimeSeconds));
    }

}
