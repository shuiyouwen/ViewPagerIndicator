package com.iuicity.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Shui on 2018/2/28.
 */

public class MyFragment extends Fragment {
    public static final String TITLE = "title";

    public static MyFragment newInstance(String title) {

        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);
        View inflate = inflater.inflate(R.layout.fragment_test, container, false);
        TextView tvTitle = inflate.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        return inflate;
    }
}
