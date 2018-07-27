package com.mill.transition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SharedElementCallback;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imageload.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListActivity extends FragmentActivity {
    private GridView mListView;
    private MyListAdapter mListAdapter;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
        mListView = new GridView(this);
        mListView.setNumColumns(4);
        mListView.setHorizontalSpacing(10);
        mListView.setVerticalSpacing(10);
        setContentView(mListView);

        mData.add("http://p7.qhimg.com/t013364db345cfdb214.png");
        mData.add("http://p9.qhimg.com/t01832aae1f3a762be7.png");
        mData.add("http://p0.qhimg.com/t01b2db80411e18b932.png");
        mData.add("http://p5.qhimg.com/t017c90c76938469d99.png");
        mData.add("http://p5.qhimg.com/t0110b7d5ab71b869f0.png");
        mData.add("http://p3.qhimg.com/t01ecdd798c4c4fdf3a.png");
        mData.add("https://p5.ssl.qhimg.com/t016c6e3b53da298e1a.png");
        mData.add("http://p5.qhimg.com/t0110b7d5ab71b869f0.png");
        mData.add("https://p5.ssl.qhimg.com/t013c6a70cb5fd0f428.png");
        mData.add("https://p5.ssl.qhimg.com/t0121d68a66ccc55118.png");

        mListView.setAdapter(mListAdapter = new MyListAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageActivity.mCurPosition = position;
                Intent intent = new Intent(ListActivity.this, PageActivity.class);
                intent.putExtra("data", mData);
                intent.putExtra("position", position);
                ActivityCompat.startActivity(ListActivity.this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ListActivity.this, view, "share").toBundle());
            }
        });

        Log.i("ListActivityA", "onCreate ");

        ActivityCompat.setExitSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                Log.i("ListActivityA", "setExitSharedElementCallback " + names + "   " + sharedElements);
                sharedElements.put("share", getItemViewByPosition(PageActivity.mCurPosition));
            }
        });
    }

    public View getItemViewByPosition(int position) {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View itemView = mListView.getChildAt(i);
            Log.d("ListActivityA", "getItemViewByPosition " + itemView.getTag(R.id.position));
            if (position == (int) itemView.getTag(R.id.position)) {
                return itemView;
            }
        }
        return null;
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img = null;
            if (convertView == null) {
                img = new ImageView(ListActivity.this);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                convertView = img;
            } else {
                img = (ImageView) convertView;
            }
            img.setTag(R.id.position, position);

            Glide.with(img.getContext()).load(mData.get(position)).into(img);
            return convertView;
        }
    }
}
