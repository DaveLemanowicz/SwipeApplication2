package org.lemanowicz.swipeapplication2;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dave on 9/22/2015.
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public MyPageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        if (position==0){
            return "Test Server";
        } else {
            return "Test Config";
        }
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }


    @Override
    public int getCount() {
        return this.fragments.size();
    }

}
