package com.ablanco.flowingtabssample;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.flowingtabs.FlowingLayout;
import com.ablanco.flowingtabs.FlowingTabsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> pages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pages.add("Page 1");
        pages.add("Page 2");
        pages.add("Page 3");
        pages.add("Page 4");
        pages.add("Page 5");

        pager.setAdapter(new FlowingTabsAdapter() {
            @Nullable
            @Override
            public Drawable getPageTitleBackground() {
                return null;
            }

            @Override
            public int getPageTitleBackgroundResId() {
                return 0;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pages.get(position);
            }

            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                NestedScrollView v = new NestedScrollView(MainActivity.this);
                v.setBackgroundColor(position % 2 == 0 ? Color.RED : Color.BLUE);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        ((FlowingLayout) findViewById(R.id.flowingLayout)).setViewPager(pager);
    }


}
