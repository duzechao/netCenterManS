package com.gdin.netcentermans.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MStringRequest extends StringRequest{

	private ProgressDialog dialog;//网络请求提示框
	private HashMap<String, String> params;
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

	public MStringRequest(int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public MStringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		if(params!=null){
			return params;
		}else{
			return super.getParams();
		}
	}
	
	/**
	 * 调用的话会在出现一个提示窗口  当窗口可以取消的话   取消的时候会取消网络链接
	 *  2014-11-28 上午10:51:00 
	 * @author DZC
	 * @return void
	 * @param context
	 * @param titleId string.xml里面的string的id
	 * @param msgId  string.xml里面的string的id
	 * @param request
	 * @param cancelable  	标志提示窗口是否可以按返回键取消
	 * @TODO
	 */
	public void showDialog(Context context,int titleId,int msgId,final boolean cancelable){
		showDialog(context, context.getResources().getString(titleId),  context.getResources().getString(msgId), cancelable);
	}
	
	/**
	 * 调用的话会在出现一个提示窗口  当窗口可以取消的话   取消的时候会取消网络链接
	 *  2014-11-28 上午10:53:47 
	 * @author DZC
	 * @return void
	 * @param context
	 * @param title
	 * @param msg
	 * @param request
	 * @param cancelable  	
	 * @TODO
	 */
	public void showDialog(Context context,String title,String msg,final boolean cancelable){
		dialog = ProgressDialog.show(context,title, title, true, cancelable, new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if(cancelable){
					if(!isCanceled()){
						cancel();
					}
					dialog.dismiss();
				}
			}
		});
	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		super.deliverResponse(response);
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		super.deliverError(error);
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
}
