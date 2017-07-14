package com.example.renchunlin.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinner;

    //获取
    private Button btn_get;
    //城市
    private EditText et_city;
    //图片
    private ImageView today_img, tomorrow_img, arter_img;
    //日期
    private TextView today_date, tomorrow_date, after_date;
    //天气
    private TextView today_weather, tomorrow_weather, after_weather;
    //温度
    private TextView today_tmp, tomorrow_tmp, after_tmp;

    // 日期
    private List<String> dateList = new ArrayList<String>();
    // 天气
    private List<String> weatherList = new ArrayList<String>();
    // 温度
    private List<String> TmpList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner= (Spinner) findViewById(R.id.mSpinner);
        mSpinner.setOnItemSelectedListener(this);

        today_img = (ImageView) findViewById(R.id.today_img);
        tomorrow_img = (ImageView) findViewById(R.id.tomorrow_img);
        arter_img = (ImageView) findViewById(R.id.arter_img);

        today_date = (TextView) findViewById(R.id.today_date);
        today_weather = (TextView) findViewById(R.id.today_weather);
        today_tmp = (TextView) findViewById(R.id.today_tmp);

        tomorrow_date = (TextView) findViewById(R.id.tomorrow_date);
        tomorrow_weather = (TextView) findViewById(R.id.tomorrow_weather);
        tomorrow_tmp = (TextView) findViewById(R.id.tomorrow_tmp);

        after_date = (TextView) findViewById(R.id.after_date);
        after_weather = (TextView) findViewById(R.id.after_weather);
        after_tmp = (TextView) findViewById(R.id.after_tmp);

        et_city = (EditText) findViewById(R.id.et_city);

        btn_get= (Button) findViewById(R.id.btn_get);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_city.getText().toString())) {
                    getWeather(et_city.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取天气
     *
     * @param city 城市名
     */

    private void getWeather(String city) {

        String url = "https://free-api.heweather.com/v5/weather?city="+city+"&key=2e2860c63831415a85a3663d96caa07a";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                Volley_Json(t);
            }
        });
    }

    private void Volley_Json(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jArray = jsonObject.getJSONArray("HeWeather5");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jb1 = (JSONObject) jArray.get(i);
                JSONArray jr1 = jb1.getJSONArray("daily_forecast");
                for (int j = 0; j < jr1.length(); j++) {
                    JSONObject jb2 = (JSONObject) jr1.get(j);

                    JSONObject jb3 = jb2.getJSONObject("tmp");

                    dateList.add(jb2.getString("date"));
                    weatherList.add(jb2.getJSONObject("cond").getString("txt_n"));
                    TmpList.add(jb3.getString("min") + "-" + jb3.getString("max"));
                }
            }

            // 设置参数
            today_date.setText(dateList.get(0));
            today_weather.setText(weatherList.get(0));
            today_tmp.setText(TmpList.get(0));

            tomorrow_date.setText(dateList.get(1));
            tomorrow_weather.setText(weatherList.get(1));
            tomorrow_tmp.setText(TmpList.get(1));

            after_date.setText(dateList.get(2));
            after_weather.setText(weatherList.get(2));
            after_tmp.setText(TmpList.get(2));

            //设置图片
            if (weatherList.get(0).equals("晴")) {
                today_img.setImageResource(R.drawable.sun);
            } else if (weatherList.get(0).equals("多云")) {
                today_img.setImageResource(R.drawable.cloudy);
            } else if(weatherList.get(0).equals("阴")){
                today_img.setImageResource(R.drawable.overcast);
            } else{
                today_img.setImageResource(R.drawable.rain);
            }

            if (weatherList.get(1).equals("晴")) {
                tomorrow_img.setImageResource(R.drawable.sun);
            } else if (weatherList.get(1).equals("多云")) {
                tomorrow_img.setImageResource(R.drawable.cloudy);
            } else if(weatherList.get(1).equals("阴")){
                tomorrow_img.setImageResource(R.drawable.overcast);
            }else {
                tomorrow_img.setImageResource(R.drawable.rain);
            }

            if (weatherList.get(2).equals("晴")) {
                arter_img.setImageResource(R.drawable.sun);
            } else if (weatherList.get(2).equals("多云")) {
                arter_img.setImageResource(R.drawable.cloudy);
            } else if(weatherList.get(2).equals("阴")){
                arter_img.setImageResource(R.drawable.overcast);
            }else {
                arter_img.setImageResource(R.drawable.rain);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //清空数据
        dateList.clear();
        weatherList.clear();
        TmpList.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str=parent.getItemAtPosition(position).toString();
        et_city.setText(str);
        et_city.setSelection(str.length());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
