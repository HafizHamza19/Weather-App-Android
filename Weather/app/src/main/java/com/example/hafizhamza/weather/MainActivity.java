package com.example.hafizhamza.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public void GetWeather(View view) {
        try {
            String EncodedCity= URLEncoder.encode(editText.getText().toString(),"UTF-8");
            downloadtask task=new downloadtask();
            task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
            InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could Not Found Weather", Toast.LENGTH_SHORT).show();
        }

    }

    public class downloadtask extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... urls) {
        try {
            String result="";
            URL url=new URL(urls[0]);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            InputStream in=connection.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            int data=reader.read();
            while (data!=-1)
            {
                char current=(char)data;
                data=reader.read();
                result+=current;

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could Not Found Weather", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject=new JSONObject(s);
            String weather=jsonObject.getString("weather");
            Log.i("Weather Content",weather);
            JSONArray array=new JSONArray(weather);
            String message="";
            for (int i=0;i<array.length();i++)
            {
                JSONObject part=array.getJSONObject(i);
                String main=part.getString("main");
                String description=part.getString("description");
                if (!main.equals("") && !description.equals(""))
                {
                    message+=main+": "+description+"\n";
                }
            }
            if(!message.equals(""))
            {
                textView.setText(message);
            }
            else {
                Toast.makeText(getApplicationContext(), "Could Not Found Weather", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could Not Found Weather", Toast.LENGTH_SHORT).show();
        }

    }
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.MyeditText);
        textView=(TextView)findViewById(R.id.textView2);
        }
}
