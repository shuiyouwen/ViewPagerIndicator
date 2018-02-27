package com.iuicity.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPagerIndicator viewPagerIndicator = findViewById(R.id.view_pager_indicator);
        List<String> data = Arrays.asList("你好", "阿莎", "嘻嘻哒", "呼伦贝尔", "上海", "房产", "新时代", "图片", "头条号", "科技", "军事", "体育", "段子");
        viewPagerIndicator.setTitles(data);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
