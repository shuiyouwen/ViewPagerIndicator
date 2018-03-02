package com.iuicity.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
        ViewPager viewPager = findViewById(R.id.view_pager);

        List<String> data = Arrays.asList( "图片", "头条号", "科技", "军事", "体育", "段子");
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(fragmentAdapter);

        viewPagerIndicator.setupViewpager(viewPager);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
