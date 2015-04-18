package com.gdin.netcentermans;

import android.app.Application;
import android.view.SurfaceHolder;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application{
	
	public static RequestQueue mQueue;
    public static String installationId = "";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mQueue = Volley.newRequestQueue(this);
        AVOSCloud.initialize(this,
                "n672vu0h76ujkvbszsoe0yhedsuepp5i9rt9dp2k7t1p42zd",
                "wp5jgq58xhoj4h7mcnr29wxoe8ifxasui717sjs15gyzj06f");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.subscribe(this, "public", MainActivity.class);
        PushService.subscribe(this, "private", MainActivity.class);
        PushService.subscribe(this, "protected", MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(
                new SaveCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            // 保存成功
                            installationId = AVInstallation
                                    .getCurrentInstallation()
                                    .getInstallationId();
                            AVUser avUser = AVUser.getCurrentUser();
                            if(avUser!=null){
                                avUser.put("installationId",installationId);
                                avUser.saveInBackground();
                            }
                            // 关联 installationId 到用户表等操作……
                        } else {
                            // 保存失败，输出错误信息
                        }
                    }
                });

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).diskCacheSize(200).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
	}

}
