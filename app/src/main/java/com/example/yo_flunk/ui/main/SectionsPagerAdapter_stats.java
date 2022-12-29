package com.example.yo_flunk.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yo_flunk.Fragment_gamesPlayed;
import com.example.yo_flunk.Fragment_gamesTimeSeries;
import com.example.yo_flunk.Fragment_treffer;
import com.example.yo_flunk.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter_stats extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_spiele, R.string.tab_treffer, R.string.tab_saison};
    private final Context mContext;

    public SectionsPagerAdapter_stats(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        switch(position)
        {
            case 0:
                fragment = new Fragment_gamesPlayed();
                break;
            case 1:
                fragment = new Fragment_treffer();
                break;
            case 2:
                fragment = new Fragment_gamesTimeSeries();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}