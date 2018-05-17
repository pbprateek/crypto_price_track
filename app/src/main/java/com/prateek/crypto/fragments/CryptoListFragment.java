package com.prateek.crypto.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prateek.crypto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoListFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public Toolbar mTopToolbar;
    private LinearLayout rootView;


    public CryptoListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crypto_list, container, false);

        mTopToolbar = view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mTopToolbar);
        rootView = view.findViewById(R.id.root_view);
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        tabLayout = view.findViewById(R.id.tabs);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager() {
        CollectionPagerAdapter adapter = new CollectionPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,true);

    }

    public class CollectionPagerAdapter extends FragmentPagerAdapter {
        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return  CryptoListChildFragment.newInstance();
            }else {
                return  CryptoListChildFavFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "ALL";
            else
                return "FAV";
        }
    }

}
