package com.gdin.netcentermans.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
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
import com.avos.avoscloud.SaveCallback;
import com.gdin.netcentermans.R;

import java.util.List;

public class MyRepairInfoActivity extends ActionBarActivity {

    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvReason;
    private TextView tvManagerName;
    private TextView tvProgress;
    private TextView tvAppointTime;
    private TextView tvResolvtime;
    private TextView tvUserName;
    private TextView tvBuilding;
    private TextView tvHouse;

    private ImageView imgReturn;




    String avId ="";
    String  title=  "";
    String  reason= "";
    String  appointTime= "";
    String  house= "";
    String buildingName ="";
    String managerName ="";
    String userName ="";
    String buildingId ="";
    String progress = "";
    List managerUserIds =null;
    String appointmentTime = "";
    String resolveTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_repair_info);
        initView();
    }

    private void initView(){

        imgReturn = (ImageView) findViewById(R.id.img_my_repair_info_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle = (TextView) findViewById(R.id.tv_my_repair_info_title);
        tvTime = (TextView) findViewById(R.id.tv_my_repair_info_time);
        tvReason = (TextView) findViewById(R.id.tv_my_repair_info_reason);
        tvManagerName = (TextView) findViewById(R.id.tv_my_repair_info_manager_name);
        tvProgress = (TextView) findViewById(R.id.tv_my_repair_info_progress);
        tvAppointTime = (TextView) findViewById(R.id.tv_my_repair_info_appointtime);
        tvResolvtime = (TextView) findViewById(R.id.tv_my_repair_info_resolve_time);
        tvUserName = (TextView) findViewById(R.id.tv_my_repair_info_user_name);
        tvBuilding = (TextView) findViewById(R.id.tv_my_repair_info_building);
        tvHouse = (TextView) findViewById(R.id.tv_my_repair_info_house);

        Bundle bundle = getIntent().getExtras();
        String repairAvId = bundle.getString("id");
/*		String title = bundle.getString("title");
		String managerName = bundle.getString("managerName");
		String progress = bundle.getString("progress");
		String appointTime = bundle.getString("appointTime");
		String resolveTime = bundle.getString("resolveTime");
		String reason = bundle.getString("resaon");
        String userName = bundle.getString("userName");
        String building = bundle.getString("buildingName");
        String house = bundle.getString("house");*/
        AVQuery.getQuery("repair").whereEqualTo("objectId",repairAvId).include("user").include("user.building").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null && avObjects != null && !avObjects.isEmpty()) {
                    AVObject repair = avObjects.get(0);
                    AVUser user = repair.getAVUser("user");
                    AVObject building = user.getAVObject("building");
                    buildingId = building.getObjectId();
                    userName = user.getString("sName");
                    progress = repair.getString("progress");
                    title = repair.getString("title");
                    reason = repair.getString("reason");
                    appointmentTime = repair.getString("appointmentMTime");
                    resolveTime = repair.getString("resolveTime");

                    appointTime = repair.getString("appointTime");
                    managerUserIds = repair.getList("managerUsers");
                    buildingName = building.getString("name");
                    house = user.getString("house");


                    tvTitle.setText(title);
                    tvUserName.setText(userName);
                    tvBuilding.setText(buildingName);
                    tvHouse.setText(house);
                    tvReason.setText(reason);
                    //tvCheckRepairManagerName.setText(managerName);
                    tvProgress.setText(progress);
                    tvTime.setText(appointTime);
                    tvResolvtime.setText(resolveTime);
                    tvAppointTime.setText(appointmentTime);


                    AVUser.getQuery().whereEqualTo("isManager", true).whereContainedIn("objectId", managerUserIds).findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> avUsers, AVException e) {
                            if (e == null) {
                                String strs = "";
                                for (int i = 0; i < avUsers.size(); i++) {
                                    strs = strs + avUsers.get(i).getUsername() + ",";
                                }
                                //managerUserObjId.addAll(Arrays.asList(ids));
                                tvManagerName.setText(strs.substring(0, strs.length() - 1));
                                //tvCheckRepairManagerName.setVisibility(View.VISIBLE);
                               /* names = new String[avUsers.size()];
                                isCheckeds = new boolean[avUsers.size()];
                                objId = new String[avUsers.size()];
                                for(int i = 0;i<avUsers.size();i++){
                                    AVUser user = avUsers.get(i);
                                    names[i] = user.getUsername();
                                    objId[i] = user.getObjectId();
                                    if(list!=null&&list.contains(user.getObjectId())){
                                        isCheckeds[i] = true;
                                    }
                                }
                                Log.d("", Arrays.toString(names)+ "  "+Arrays.toString(isCheckeds));
                                tvManager.setVisibility(View.VISIBLE);*/
                            }
                        }
                    });


                }
            }
        });


    }
}
