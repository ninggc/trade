/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ninggc.trade.address;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * <p>ViewPager对与View的适配器。</p>
 * Created by YanZhenjie on 2017/6/1.
 */
public class PagerViewAdapter<T extends View> extends PagerAdapter {

    private List<T> mTList;

    public PagerViewAdapter(List<T> tList) {
        this.mTList = tList;
    }

    @Override
    public int getCount() {
        return mTList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T t = mTList.get(position);
        container.addView(t);
        return t;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mTList.get(position));
    }
}
