package com.mill.transition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private MyPageAdapter mPageAdapter;
    private List<String> mData = new ArrayList<>();
    public static int mCurPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mViewPager);

        ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        mCurPosition = getIntent().getIntExtra("position", 0);

        if (data != null) {
            mData.addAll(data);
        }
        mViewPager.setAdapter(mPageAdapter = new MyPageAdapter());
        mViewPager.setCurrentItem(mCurPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
            }
        });

        //延迟动画
        ActivityCompat.postponeEnterTransition(this);


        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Log.i("ListActivityA", "setEnterSharedElementCallback " + names + "   " + sharedElements);
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {
        ActivityCompat.finishAfterTransition(this);
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //启动动画
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        ActivityCompat.startPostponedEnterTransition(PageActivity.this);
                        return true;
                    }
                });
    }


    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout layout = new FrameLayout(PageActivity.this);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img = new ImageView(PageActivity.this);
            img.setLayoutParams(new ViewGroup.LayoutParams(800, 800));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(img);
            container.addView(layout);

            if (mCurPosition == position) {
                ViewCompat.setTransitionName(img, "share");
                scheduleStartPostponedTransition(img);
            }

            Glide.with(img.getContext()).load(mData.get(position)).into(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewCompat.setTransitionName(v, "share");
                    goBack();
                }
            });
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
