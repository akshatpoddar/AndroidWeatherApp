package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView weather_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.city);
        weather_data = findViewById(R.id.weather_data);
    }

    public void showWeather(View view) {
        weather_data.setText("");
        if(city.getText().toString().length() <= 0)
            city.setError("Enter valid city name");
        else {
            new DownloadData().execute("https://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(city.getText().toString())+"&appid=d4155a5143f668d417778ac62e1884f0");
        }
    }

    private class DownloadData extends AsyncTask<String, String , String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            try  {
                JSONObject reader = new JSONObject(result);
                JSONObject main = reader.getJSONObject("main");
                double temp = Double.parseDouble(main.getString("temp"));
                temp = temp-273;
                DecimalFormat num = new DecimalFormat("#.00");
                weather_data.setText("Temp: "+num.format(temp));
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Invalid City", Toast.LENGTH_LONG).show();
            }
        }
    }
}
