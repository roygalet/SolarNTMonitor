package com.roygalet.www.solarntmonitor;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import PVOutputData.PVAccountSettings;
import PVOutputData.PVSystem;
import PVOutputData.PVSystemsCollection;

public class MonitorActivity extends AppCompatActivity {
    private String data;
//    BarChart barChart;
    PVOutputData.PVSystemsCollection nearbyCollection;
    int sid = 47892;
    String key = "4012c804abb523bf7466ef415c9ba808e8aae946";
    int postCode = 810;
    int distance = 100;
    WebView webView;
    String html="";
    private ProgressDialog progressDialog;
    TextView btnDaily;
    TextView btnMonthly;
    TextView btnYearly;
    List<String> systems;
    Spinner spinSystems;
    final String dontCompareMessage = "(Show my data only)";
    final int DAILY = 1;
    final int MONTHLY = 2;
    final int YEARLY = 3;
    int currentFrequency=DAILY;
    String compareSystem = dontCompareMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        new PVOutputConnection().execute();


        spinSystems = (Spinner) findViewById(R.id.spinSystems);
        spinSystems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                compareSystem = selectedItem;
                generateGraph();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btnDaily = (TextView)findViewById(R.id.btnDaily);
        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFrequency = DAILY;
                generateGraph();
            }
        });

        btnMonthly = (TextView)findViewById(R.id.btnMonthly);
        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFrequency = MONTHLY;
                generateGraph();
            }
        });

        btnYearly = (TextView)findViewById(R.id.btnYearly);
        btnYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFrequency = YEARLY;
                generateGraph();
            }
        });

        ((Button)findViewById(R.id.btnSettings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MonitorActivity.this, MonitorPreferences.class));
            }
        });

    }

    private void highLightButton(TextView button, boolean highlight){
        if(highlight){
            button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            button.setTextColor(Color.BLACK);
        }else{
            button.setTypeface(Typeface.DEFAULT);
            button.setTextColor(Color.GRAY);
        }
        button.setAllCaps(highlight);
    }

    private void changeButtonStatus(){
        highLightButton(btnDaily, currentFrequency==DAILY);
        highLightButton(btnMonthly, currentFrequency==MONTHLY);
        highLightButton(btnYearly, currentFrequency==YEARLY);
    }

    private void showOtherSystem(boolean show){
        int visibility = View.GONE;
        if(show)visibility = View.VISIBLE;

        findViewById(R.id.otherSystemName).setVisibility(visibility);
        findViewById(R.id.otherSystemSize).setVisibility(visibility);
        findViewById(R.id.otherSystemPostCode).setVisibility(visibility);
        findViewById(R.id.otherSystemOrientation).setVisibility(visibility);
        findViewById(R.id.otherSystemPanel).setVisibility(visibility);
        findViewById(R.id.otherSystemInverter).setVisibility(visibility);
        findViewById(R.id.otherSystemDistance).setVisibility(visibility);
        findViewById(R.id.otherSystemLatitude).setVisibility(visibility);
        findViewById(R.id.otherSystemLongitude).setVisibility(visibility);
    }

    private void generateGraph(){
        changeButtonStatus();
        if (currentFrequency == DAILY) {
            if(compareSystem.compareToIgnoreCase(dontCompareMessage)==0) {
                webView.loadDataWithBaseURL("", nearbyCollection.generateMyDailyData(11), "text/html", "UTF-8", "");
            }else{
                webView.loadDataWithBaseURL("", nearbyCollection.compareDaily(compareSystem,11), "text/html", "UTF-8", "");
            }
        }else if (currentFrequency == MONTHLY) {
            if(compareSystem.compareToIgnoreCase(dontCompareMessage)==0) {
                webView.loadDataWithBaseURL("", nearbyCollection.generateMyMonthlyData(13), "text/html", "UTF-8", "");
            }else{
                webView.loadDataWithBaseURL("", nearbyCollection.compareMonthly(compareSystem,13), "text/html", "UTF-8", "");
            }
        }else if (currentFrequency == YEARLY) {
            if(compareSystem.compareToIgnoreCase(dontCompareMessage)==0) {
                webView.loadDataWithBaseURL("", nearbyCollection.generateMyYearlyData(10), "text/html", "UTF-8", "");
            }else{
                webView.loadDataWithBaseURL("", nearbyCollection.compareYearly(compareSystem,5), "text/html", "UTF-8", "");
            }
        }

        showOtherSystem(compareSystem.compareToIgnoreCase(dontCompareMessage)!=0);

        if(compareSystem.compareToIgnoreCase(dontCompareMessage)!=0){
            PVSystem otherSystem = (PVSystem)nearbyCollection.getPvSystems().get(compareSystem);
            ((TextView)findViewById(R.id.otherSystemName)).setText(otherSystem.getName());
            ((TextView)findViewById(R.id.otherSystemSize)).setText(String.valueOf(otherSystem.getSize()));
            ((TextView)findViewById(R.id.otherSystemPostCode)).setText(String.valueOf(otherSystem.getPostCode()));
            ((TextView)findViewById(R.id.otherSystemOrientation)).setText(otherSystem.getOrientation());
            ((TextView)findViewById(R.id.otherSystemPanel)).setText(otherSystem.getPanel());
            ((TextView)findViewById(R.id.otherSystemInverter)).setText(otherSystem.getInverter());
            ((TextView)findViewById(R.id.otherSystemDistance)).setText(String.valueOf(otherSystem.getDistance()));
            ((TextView)findViewById(R.id.otherSystemLatitude)).setText(String.valueOf(otherSystem.getLatitude()));
            ((TextView)findViewById(R.id.otherSystemLongitude)).setText(String.valueOf(otherSystem.getLongitude()));
        }

    }

    class PVOutputConnection extends AsyncTask <String, Long, PVOutputData.PVSystemsCollection>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MonitorActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Retrieving Data . . . Please Wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected PVSystemsCollection doInBackground(String... params) {
            nearbyCollection = new PVSystemsCollection(new PVAccountSettings(sid, key, postCode));
            nearbyCollection.getMySystem();
            nearbyCollection.getNearbySystemsWithLatestOutputs(distance,5);
            return nearbyCollection;
        }

        @Override
        protected void onPostExecute(PVSystemsCollection pvSystemsCollection) {
            super.onPostExecute(pvSystemsCollection);
            systems = new ArrayList<String>();
            systems.add(dontCompareMessage);
            for(int i=0; i<nearbyCollection.getPvSystems().size(); i++){
                systems.add((String) nearbyCollection.getPvSystems().keySet().toArray()[i]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter <String> (MonitorActivity.this,R.layout.spinner_item, systems);
            spinSystems.setAdapter(adapter);
            currentFrequency = DAILY;
            compareSystem = dontCompareMessage;

            ((TextView)findViewById(R.id.mySystemName)).setText(nearbyCollection.getMySystem().getName());
            ((TextView)findViewById(R.id.mySystemSize)).setText(String.valueOf(nearbyCollection.getMySystem().getSize()));
            ((TextView)findViewById(R.id.mySystemPostCode)).setText(String.valueOf(nearbyCollection.getMySystem().getPostCode()));
            ((TextView)findViewById(R.id.mySystemOrientation)).setText(nearbyCollection.getMySystem().getOrientation());
            ((TextView)findViewById(R.id.mySystemPanel)).setText(nearbyCollection.getMySystem().getPanel());
            ((TextView)findViewById(R.id.mySystemInverter)).setText(nearbyCollection.getMySystem().getInverter());
            ((TextView)findViewById(R.id.mySystemDistance)).setText(String.valueOf(nearbyCollection.getMySystem().getDistance()));
            ((TextView)findViewById(R.id.mySystemLatitude)).setText(String.valueOf(nearbyCollection.getMySystem().getLatitude()));
            ((TextView)findViewById(R.id.mySystemLongitude)).setText(String.valueOf(nearbyCollection.getMySystem().getLongitude()));

            generateGraph();
            progressDialog.hide();
            System.out.println(html);
        }
    }
}
