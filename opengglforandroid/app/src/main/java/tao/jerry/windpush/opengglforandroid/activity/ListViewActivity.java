package tao.jerry.windpush.opengglforandroid.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tao.jerry.windpush.opengglforandroid.R;
import tao.jerry.windpush.opengglforandroid.view.MusListView;

/**
 * Created by WindPush on 16/1/15.
 */
public class ListViewActivity extends FragmentActivity {
    private MusListView mListView;
    private FullScreenAdapter fullScreenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewactivity);
        bindView();
        initData();
        initView();
    }

    private void bindView() {
        mListView = (MusListView) findViewById(R.id.listview);
    }

    private void initData() {
        fullScreenAdapter = new FullScreenAdapter();
    }

    private void initView() {
        mListView.setAdapter(fullScreenAdapter);
        mListView.setFastScrollEnabled(false);
        mListView.setFriction(ViewConfiguration.getScrollFriction() * 20);
        mListView.setSelection(0);
    }


    private class FullScreenAdapter extends BaseAdapter {
        private List<Integer> mFakeList = new ArrayList<>();

        public FullScreenAdapter() {
            for (int fakeCount = 0; fakeCount < 220; fakeCount++) {
                mFakeList.add(fakeCount);
            }

        }

        @Override
        public int getCount() {
            return mFakeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFakeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View cacheView = null;
            if (convertView == null) {

                cacheView = new View(parent.getContext());
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(parent.getWidth(), parent.getHeight());
                cacheView.setLayoutParams(params);
                convertView = cacheView;
            }
            Random random = new Random();
            String hex = Integer.toHexString(random.nextInt(16777216-11777216)+11777216);

            convertView.setBackgroundColor(Color.parseColor("#" + hex));


            return convertView;
        }
    }


}
