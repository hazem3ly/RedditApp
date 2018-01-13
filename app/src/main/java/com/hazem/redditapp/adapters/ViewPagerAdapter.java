package com.hazem.redditapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.hazem.redditapp.R;
import com.hazem.redditapp.fragments.MainFragment;
import com.hazem.redditapp.utils.Constants;

/**
 * Created by Hazem Ali.
 * On 1/12/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("Position ", String.valueOf(position));
        MainFragment mainFragment = new MainFragment();
        Bundle data = new Bundle();
        data.putInt(Constants.FRAGMENT_POSITION, position);
        mainFragment.setArguments(data);
        return mainFragment;
    }

    @Override
    public int getCount() {
        return Constants.PAGES_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.hot_reddites);
            case 1:
                return context.getString(R.string.new_reddites);
            case 2:
                return context.getString(R.string.top_reddites);
            case 3:
                return context.getString(R.string.controversial_reddites);
        }
        return "";
    }


}


