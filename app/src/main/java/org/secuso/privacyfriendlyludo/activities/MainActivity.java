package org.secuso.privacyfriendlyludo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyludo.R;
import org.secuso.privacyfriendlyludo.tutorial.PrefManager;
import org.secuso.privacyfriendlyludo.tutorial.TutorialActivity;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use the a button to display the welcome screen
        Button b = (Button) findViewById(R.id.button_welcomedialog);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrefManager prefManager = new PrefManager(getBaseContext());
                    prefManager.setFirstTimeLaunch(true);
                    Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }

        overridePendingTransition(0, 0);

        final MainActivity.SectionsPagerAdapter mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.scroller);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        int index = mSharedPreferences.getInt("lastChosenPage", 0);

        mViewPager.setCurrentItem(index);
        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        //care for initial postiton of the ViewPager
        mArrowLeft.setVisibility((index == 0) ? View.INVISIBLE : View.VISIBLE);
        mArrowRight.setVisibility((index == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);

        //Update ViewPager on change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrowLeft.setVisibility((position == 0) ? View.INVISIBLE : View.VISIBLE);
                mArrowRight.setVisibility((position == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);

                //save position in settings
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("lastChosenPage", position);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_left:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.arrow_right:
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
                break;
            case R.id.game_button_start:
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return MainActivity.PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainActivity.PageFragment newInstance(int sectionNumber) {
            MainActivity.PageFragment fragment = new MainActivity.PageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int id = 0;
            if (getArguments() != null) {
                id = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Mode: " + String.valueOf(id));
            return rootView;
        }
    }

}