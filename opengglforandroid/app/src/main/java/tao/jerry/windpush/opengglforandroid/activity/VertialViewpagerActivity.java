package tao.jerry.windpush.opengglforandroid.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.Locale;
import java.util.Random;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import tao.jerry.windpush.opengglforandroid.R;

/**
 * Created by WindPush on 16/1/15.
 */
public class VertialViewpagerActivity extends FragmentActivity {

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    private DummyAdapter adapter;
    private VerticalViewPager verticalViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertialviewpager);
        adapter = new DummyAdapter(getSupportFragmentManager());
        verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        verticalViewPager.setAdapter(adapter);
        verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });

        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setColor(ChangeColor());
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public String ChangeColor() {
        Random random = new Random();
        return Integer.toHexString(random.nextInt(16777216 - 11777216) + 11777216);
    }

    public class DummyAdapter extends FragmentPagerAdapter {

        public DummyAdapter(FragmentManager fm) {
            super(fm);
            Random random = new Random();
            mColor = Integer.toHexString(random.nextInt(16777216 - 11777216) + 11777216);
        }

        private String mColor;

        public void setColor(String color) {
            mColor = color;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, mColor);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 111;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "PAGE 1";
                case 1:
                    return "PAGE 2";
                case 2:
                    return "PAGE 3";
                default:
                    return "PAGE " + position;
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String color) {
            PlaceholderFragment fragment = new PlaceholderFragment(color);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private String mColor;

        public PlaceholderFragment(String color) {
            mColor = color;
        }

        String hex;
        View cacheView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            cacheView = new View(container.getContext());
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(container.getWidth(), container.getHeight());
            cacheView.setLayoutParams(params);
            cacheView.setBackgroundColor(Color.parseColor("#" + mColor));
            return cacheView;
        }


    }

}