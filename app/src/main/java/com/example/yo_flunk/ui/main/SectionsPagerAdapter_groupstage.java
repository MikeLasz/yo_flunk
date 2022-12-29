package com.example.yo_flunk.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yo_flunk.Fragment_groupsTournament;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter_groupstage extends FragmentPagerAdapter {

    private final Context mContext;
    private final int mNumGroups;

    public SectionsPagerAdapter_groupstage(Context context, FragmentManager fm, int numGroups) {
        super(fm);
        mContext = context;
        mNumGroups = numGroups;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        Bundle args = new Bundle();
        String ARG_PARAM1 = "group_index";
        String ARG_PARAM2 = "param2";
        args.putString(ARG_PARAM2, "ignore");
        if(mNumGroups==4){
            switch(position)
            {
                case 0:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 0);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 1);
                    fragment.setArguments(args);
                    break;
                case 2:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 2);
                    fragment.setArguments(args);
                    break;
                case 3:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 3);
                    fragment.setArguments(args);
                    break;
            }
        }else{ // only 2 groups
            switch(position) {
                case 0:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 0);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new Fragment_groupsTournament();
                    args.putInt(ARG_PARAM1, 1);
                    fragment.setArguments(args);
                    break;
            }
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String[] titles = new String[mNumGroups];
        titles[0] = "Gruppe 1";
        titles[1] = "Gruppe 2";
        if(mNumGroups==4) {
            titles[2] = "Gruppe 3";
            titles[3] = "Gruppe 4";
        }
        return titles[position];
        //return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return mNumGroups;
    }
}