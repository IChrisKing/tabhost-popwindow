package com.example.test_size;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private Handler mHandler;
//	private AppInfoManager appInfoManager;
	private List<ApplicationInfo> applicationInfos = new ArrayList<ApplicationInfo>();
	private boolean isSecZone;
	
	public ListViewAdapter(Context context, Handler handler, List<ApplicationInfo> appInfos, boolean isSec) {
		mContext = context;
		mHandler = handler;
		mInflater = LayoutInflater.from(context);
		applicationInfos = appInfos;
//		appInfoManager = AppInfoManager.getInstance();
		isSecZone = isSec;
	}

	public int getCount() {
		return applicationInfos.size();
	}

	public Object getItem(int arg0) {
		return applicationInfos.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lv_item, parent, false);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.lv_item_icon);
			holder.appname = (TextView) convertView.findViewById(R.id.lv_item_appname);
			holder.menu = (ImageView) convertView.findViewById(R.id.lv_item_menu);
//			holder.pakagename = (TextView) convertView.findViewById(R.id.lv_item_pakagename);
//			holder.uid = (TextView) convertView.findViewById(R.id.lv_item_uid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.icon.setImageDrawable(applicationInfos.get(position).loadIcon(mContext.getPackageManager()));
//		holder.pakagename.setText(applicationInfos.get(position).packageName);
		holder.appname.setText(applicationInfos.get(position).loadLabel(mContext.getPackageManager()));
		String pkgName = applicationInfos.get(position).packageName;
		if (true) {
			// Log.i(TAG,"appsUserCache.containsKey true pkg =" + pkg);
//			holder.uid.setText(mContext.getResources().getText(R.string.both));
//			holder.uid.setTextColor(Color.BLUE);
		} 
//		else {
//			// Log.i(TAG,"appsUserCache.containsKey false pkg =" + pkg);
//			holder.uid.setText(appInfoManager.isAppBelongsToOwner(applicationInfos.get(position).uid) ? mContext.getResources().getText(R.string.owner)
//					: mContext.getResources().getText(R.string.normal));
//			holder.uid.setTextColor(appInfoManager.isAppBelongsToOwner(applicationInfos.get(position).uid) ? Color.GREEN : Color.RED);
//		}
		
		holder.menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage(MainActivity.SHOW_MENU);
				msg.obj = v;
				msg.arg1 = isSecZone ? 1:0;
				mHandler.sendMessage(msg);	
			}
		});

		return convertView;
	}
	
    public final class ViewHolder {
        ImageView icon;
        TextView appname;
        ImageView menu;
//        TextView pakagename;
//        TextView uid;
    } 
}


