package com.abzaa.abzaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by zarulizham on 10/05/2016.
 */
public class NavMain extends Fragment {

    public Activity act;
    public Context context;
    public int index;
    String gName, gId, juz_no;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static NavMain newInstance(int index, String group_id, String group_name, String juz_no) {
        NavMain fragment = new NavMain();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("group_id", group_id);
        args.putString("group_name", group_name);
        args.putString("juz_no", juz_no);
        fragment.setArguments(args);
        return fragment;
    }

    public NavMain() {

    }

    public void init(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View rootView = inflater.inflate(R.layout.nav_main, container, false);
        init(rootView);
        index = this.getArguments().getInt("index", 0);
        gName = this.getArguments().getString("group_name", "");
        gId = this.getArguments().getString("group_id", "");
        juz_no = this.getArguments().getString("juz_no", "");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        return rootView;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        act = (Activity) c;
        context = c;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return NavHome.newInstance(1, gId, gName, juz_no);
            } else {
                return PlaceholderFragment.newInstance(2);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Homepage";
//                case 1:
//                    return "Others 1";
//                case 2:
//                    return "Others 2";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
