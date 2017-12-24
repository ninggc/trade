package com.ninggc.trade.activity.ease;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import static com.ninggc.trade.activity.ease.ContactActivity.contactToMap;

/**
 * Created by Ning on 11/12/2017 0012.
 */

public class ContactListFragment extends EaseContactListFragment {

    String TAG = getClass().getSimpleName();

    @Override
    public void onStart() {
        super.onStart();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContactsFromServer();
                Log.e(TAG, "onRefresh: ");
            }
        });

        if (contactList == null || contactList.size() == 0) {
            swipeRefreshLayout.setRefreshing(true);
            getContactsFromServer();
        }
    }

    public void getContactsFromServer() {
        final SPContactUtil spContactUtil = new SPContactUtil(getContext());
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e(TAG, "run: " + "开始run");
                    List<String> listFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.e(TAG, "run: " + "从服务器成功获取数据");
                    spContactUtil.setAllContacts(listFromServer);
                    setContactsMap(contactToMap(listFromServer));
                    refresh();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
