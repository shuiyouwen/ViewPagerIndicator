package com.iuicity.myapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shui on 2018/2/28.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<String> mData;
    private List<MyFragment> mMyFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, List<String> data) {
        super(fm);
        mData = data;
        for (String datum : data) {
            MyFragment myFragment = MyFragment.newInstance(datum);
            mMyFragments.add(myFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mMyFragments.get(position);
    }

    @Override
    public int getCount() {
        return mMyFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mData.get(position);
    }
}
