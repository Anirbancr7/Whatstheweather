package com.example.anirban.whatstheweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    TextView header,resultView,tempView,typeView;
    EditText editText;
    Button button;
    ImageView imageView;

    public void findWeather(View view)
    {
        String enteredWeather="";
        enteredWeather=editText.getText().toString();

        DownloadTask task=new DownloadTask();
        task.execute("https://api.apixu.com/v1/current.json?key=af155251fa7c49b9820184856182403&q="+enteredWeather);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header=findViewById(R.id.enterTheCity);
        editText=findViewById(R.id.cityName);;
        button=findViewById(R.id.viewWeather);;
        resultView=findViewById(R.id.weatherInfo);;
        imageView=findViewById(R.id.imageView);
        tempView=findViewById(R.id.tempView);
        typeView=findViewById(R.id.typeView);
    }
    @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;
            String result = "";
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1) {
                    char next = (char) data;
                    result += next;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(),"Can't find the Weather! check the spelling maybe? :(",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Can't find the Weather! check the spelling maybe? :(",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.matches(""))
                Toast.makeText(getApplicationContext(),"Can't find the Weather! check the spelling maybe? :(",Toast.LENGTH_LONG).show();
            else{
            try
            {
                String name="",region="",country="",temp="",condition="",weatherType="";
                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo=jsonObject.getString("location");
                JSONObject weatherInfoJSON=new JSONObject(weatherInfo);
                name=weatherInfoJSON.getString("name");
                region=weatherInfoJSON.getString("region");
                country=weatherInfoJSON.getString("country");


                String weatherCurrent=jsonObject.getString("current");
                JSONObject weatherCurrentJSON=new JSONObject(weatherCurrent);
                temp=weatherCurrentJSON.getString("temp_c");
                condition=weatherCurrentJSON.getString("condition");
                JSONObject conditionJSON=new JSONObject(condition);
                weatherType=conditionJSON.getString("text");

                resultView.setText(name+"\n"+region+"\n"+country);
                tempView.setText(temp+"°C");
                typeView.setText(weatherType);
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Can't find the Weather! check the spelling maybe? :(",Toast.LENGTH_LONG).show();
            }}

        }
    }
}
