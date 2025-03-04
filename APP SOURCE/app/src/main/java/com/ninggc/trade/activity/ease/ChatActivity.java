package com.ninggc.trade.activity.ease;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.ninggc.trade.R;
import com.ninggc.trade.activity.MainActivity;
import com.ninggc.trade.activity.base.BaseActivity;

/**
 * @author Ning
 * Created by Ning on 11/12/2017 0012.
 */

public class ChatActivity extends BaseActivity{
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.fragment_chat);
        super.onCreate(arg0);
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            finish();
        }
    }

    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}

