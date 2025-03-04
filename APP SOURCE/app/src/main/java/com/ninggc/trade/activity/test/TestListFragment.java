package com.ninggc.trade.activity.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.ninggc.trade.DAO.Campus;
import com.ninggc.trade.DAO.Province;
import com.ninggc.trade.R;
import com.ninggc.trade.activity.account.AccountUtil;
import com.ninggc.trade.activity.base.BaseActivity;
import com.ninggc.trade.activity.ease.ContactActivity;
import com.ninggc.trade.address.AddressCheckActivity;
import com.ninggc.trade.address.City;
import com.ninggc.trade.address.CampusCheckActivity;
import com.ninggc.trade.util.http.HttpResponseListener;
import com.ninggc.trade.util.nohttp.CallServer;
import com.ninggc.trade.test.TestBaiduMap;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * Created by Ning on 11/16/2017 0016.
 */

public class TestListFragment extends ListFragment {
    Gson gson = new Gson();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Adapter adapter = new Adapter();
        setListAdapter(adapter);
//        adapter.addItem("--测试Loading", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoadingView loadingView = new LoadingView(getContext());
//
//                final Dialog mWaitDialog = new LoadingDialog(getContext());
//                mWaitDialog.setTitle("Please wait...");
////                mWaitDialog.setCircleColors(...);
//                mWaitDialog.show();
//                try {
//                    Thread.sleep(2 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mWaitDialog.dismiss();
//            }
//        });

//        adapter.addItem("--测试登录", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Server.login("ning", "123", new HttpResponseListener<String>() {
//                    @Override
//                    public void onSucceed(int what, Response<String> response) {
//                        super.onSucceed(what, response);
//                        Log.e("NOHTTP", "onSucceed: " + response.get());
//                    }
//                });
//            }
//        });

        adapter.addItem("地址选择器", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress();
            }
        });

//        adapter.addItem("--Cookie", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Server.login("ning", "123", new HttpResponseListener<String>() {
//                    @Override
//                    public void onSucceed(int what, Response<String> response) {
//                        super.onSucceed(what, response);
//                    }
//
//                    @Override
//                    public void onFailed(int what, Response<String> response) {
//                        super.onFailed(what, response);
//                    }
//
//                    @Override
//                    public void onFinish(int what) {
//                        super.onFinish(what);
//                    }
//                });
//            }
//        });

//        adapter.addItem("BottomBar", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), TestBottomBar.class));
//            }
//        });

        adapter.addItem("联系人列表", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ContactActivity.class));
            }
        });

        adapter.addItem("状态信息", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String s = new StringBuilder()
                        .append("登录状态 : " + AccountUtil.isLogin()).append("\n")
                        .append("当前用户 : " + gson.toJson(AccountUtil.getCurrentUser())).append("\n")
                        .append("当前Cookie : " + gson.toJson(AccountUtil.getCookie())).append("\n")
                        .append("当前EMC用户 : " + gson.toJson(AccountUtil.getEmcUser())).append("\n")
                        .append("EMC内置用户 : " + gson.toJson(EMClient.getInstance().getCurrentUser())).append("\n")
                        .toString();
                Log.e("LongMsg", "onClick: " + s);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(s);
                builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        adapter.addItem("百度Map", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TestBaiduMap.class));
            }
        });

        adapter.addItem("百度在Base", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BaiduMapAty.class));
            }
        });

        adapter.addItem("权限检测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "恭喜你有CAMERA权限", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            Toast.makeText(getContext(), "您已经点击不再提醒，无法获得操作权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "没有CAMERA权限", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                        }
                    }
                }
            }
        });

        adapter.addItem("City Check", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCampus();
            }
        });

        adapter.addItem("TestURL === 一次性的小测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request<String> request = NoHttp.createStringRequest("http://123.207.244.139:8082/usermage/login/", RequestMethod.POST);
                request.set("type", "1");
                request.set("username", "ning");
                request.set("password", "123");
                CallServer.getInstance().add(0, request, new HttpResponseListener() {
                    @Override
                    public void onSucceed(int what, Response response) {
                        Log.e(BaseActivity.TAG_NOHTTP, "onSucceed: " + response.get());
                        super.onSucceed(what, response);
                    }

                    @Override
                    public void onFailed(int what, Response response) {
                        super.onFailed(what, response);
                    }

                    @Override
                    public void onFinish(int what) {
                        super.onFinish(what);
                    }
                });
            }
        });
    }

    private void selectCampus() {
        Intent intent = new Intent(getContext(), CampusCheckActivity.class);
        startActivityForResult(intent, 777);
    }


    /**
     * 去选择地址。
     */
    public void selectAddress() {
        Intent intent = new Intent(getContext(), AddressCheckActivity.class);
        startActivityForResult(intent, 666);
    }

    /**
     * 解析地址。
     */
    public void parseAddress(Intent intent) {
        ArrayList<City> cityList = AddressCheckActivity.parse(intent);

        String tvAddress = "", lastId = "";
        if (cityList != null) {
            for (int i = 0; i < cityList.size(); i++) {
                City campus = cityList.get(i);
                lastId = campus.getId();
                tvAddress += campus.getName();
            }
        }
        Toast.makeText(getContext(), tvAddress + "id : " + lastId, Toast.LENGTH_SHORT).show();
//        mTextView.setText(tvAddress + "\n提交到服务器的id是：" + lastId);
    }

    class Adapter implements ListAdapter {
        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        class Entity {
            protected String text;
            protected View.OnClickListener listener;
        }
        ArrayList<Entity> list = new ArrayList<>();

        public Adapter() {
            Entity e = new Entity();
            e.text = "text";
            list.add(e);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public boolean hasStableIds() {
            return false;
        }

        public void addItem(String text, View.OnClickListener onClickListener) {
            Entity e = new Entity();
            e.text = text;
            e.listener = onClickListener;
            list.add(e);
            synchronized (Thread.currentThread()) {
                try {
                    notify();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView != null) {
                view = convertView;
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.test_111, null);
            }
            Entity entity = (Entity) getItem(position);
            view.findViewById(R.id.button1).setOnClickListener(entity.listener);
            ((Button) view.findViewById(R.id.button1)).setText(entity.text);
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return list.size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) return;
//        switch (requestCode) {
//            case 666: {
//                parseAddress(data);
//                break;
//            }
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("CAMPUS", "onActivityResult: " + requestCode);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 777:
                Province province = gson.fromJson(data.getStringExtra("province"), Province.class);
                Campus campus = gson.fromJson(data.getStringExtra("campus"), Campus.class);

                Log.e("CAMPUS", "onActivityResult: " + gson.toJson(province));
                Log.e("CAMPUS", "onActivityResult: " + gson.toJson(campus));
                break;
            case 666:
                break;
            default:break;
        }
    }
}
