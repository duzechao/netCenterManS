package com.gdin.netcentermans.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import com.gdin.netcentermans.R;
import com.gdin.netcentermans.utils.TextJustification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SignActivity extends ActionBarActivity {
    private EditText etName;
    private EditText etPass;
    private EditText etHouse;
    private TextView tvSchoolPlace;
    private TextView tvBuilding;
    private TextView tvUserLoginSubmit;
    private Map<String,List> places = new HashMap<>();
    private Map<String,String> placeNames = new HashMap<>();
    private String placeId;
    private String buildingId;
    private EditText etName2;
    private EditText etCardNum;
    private ImageView imgReturn;

    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
    }

    private void initView(){
        tvSchoolPlace = (TextView) findViewById(R.id.tv_school_place);
        tvBuilding = (TextView) findViewById(R.id.tv_building);
        tvUserLoginSubmit = (TextView) findViewById(R.id.tv_user_login_submit);
        etName = (EditText) findViewById(R.id.et_user_login_student_num);
        etName2 = (EditText) findViewById(R.id.et_user_login_student_name);
        etPass = (EditText) findViewById(R.id.et_user_login_password);
        etHouse = (EditText) findViewById(R.id.et_house);
        etCardNum = (EditText) findViewById(R.id.et_user_login_card_num);
        imgReturn = (ImageView) findViewById(R.id.img_sign_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AVQuery.getQuery("buildings").include("place").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    for (int i = 0; i < avObjects.size(); i++) {
                        AVObject building = avObjects.get(i);
                        AVObject place = building.getAVObject("place");
                        if (places.containsKey(place.getObjectId())) {
                            places.get(place.getObjectId()).add(building);
                        } else {
                            List list = new ArrayList<AVObject>();
                            list.add(building);
                            places.put(place.getObjectId(), list);
                            placeNames.put(place.getObjectId(), place.getString("name"));
                        }
                    }
                    tvSchoolPlace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (placeNames.isEmpty()) return;
                            final String[] values = new String[placeNames.size()];
                            List ll = new ArrayList(placeNames.values());
                            //final String[] keys = new String[placeNames.size()];
                            Set<String> kk = placeNames.keySet();
                            final Object[] keys = kk.toArray();
                            for (int i = 0; i < ll.size(); i++) {
                                values[i] = ll.get(i).toString();
                            }
                            new AlertDialog.Builder(SignActivity.this).setItems(values, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tvSchoolPlace.setText(values[which]);
                                    placeId = keys[which].toString();
                                }
                            }).create().show();
                        }
                    });
                    tvBuilding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (placeNames.isEmpty()) return;
                            List<AVObject> l = places.get(placeId);
                            final String[] values = new String[l.size()];
                            final String[] ids = new String[l.size()];
                            for (int i = 0; i < l.size(); i++) {
                                values[i] = l.get(i).getString("name");
                                ids[i] = l.get(i).getObjectId();
                            }
                            new AlertDialog.Builder(SignActivity.this).setItems(values, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tvBuilding.setText(values[which]);
                                    buildingId = ids[which];
                                }
                            }).create().show();
                        }
                    });
                }


                Log.d("", "");
            }
        });


        tvUserLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name2 = etName2.getText().toString();
                String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                String house = etHouse.getText().toString();
                String building = tvBuilding.getText().toString();
                String cardNum = etCardNum.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(house)||TextUtils.isEmpty(building)||TextUtils.isEmpty(name2)||TextUtils.isEmpty(cardNum)){
                    Toast.makeText(SignActivity.this,"请填写完整资料",Toast.LENGTH_LONG).show();
                    return;
                }
                AVUser user = new AVUser();
                user.setUsername(name);
                user.setPassword(pass);
                user.put("house",house);
                AVObject avObject = new AVObject("buildings");
                avObject.setObjectId(buildingId);
                user.put("building", avObject);
                user.put("sName",name2);
                user.put("sNum",name);
                user.put("cardNum",cardNum);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Toast.makeText(SignActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                        }else{
                            Log.d("",e.toString());
                        }
                    }
                });
            }
        });
    }
}
