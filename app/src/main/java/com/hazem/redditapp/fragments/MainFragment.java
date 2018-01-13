package com.hazem.redditapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hazem.redditapp.MainActivity;
import com.hazem.redditapp.R;
import com.hazem.redditapp.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private int postiton = -2;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle data = getArguments();
        postiton = data.getInt(Constants.FRAGMENT_POSITION, -1);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.text);
        textView.setText(String.valueOf(postiton + MainActivity.type));


    }
}
