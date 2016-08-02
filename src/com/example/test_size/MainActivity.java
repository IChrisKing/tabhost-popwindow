package com.example.test_size;

import java.util.ArrayList;
import java.util.List;

import com.view.pop.ActionItem;
import com.view.pop.QuickAction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends Activity {
	/**
	 * @author jin
	 */
	private static final String TAG = "XAppManager|jin";
	public static final int UNINSTALL_COMPLETE = 0;
  public static final int INSTALL_COMPLETE = 1;
  public static final int SHOW_MENU = 2;
	private ListView nor_lv,sec_lv;
	private ListViewAdapter nor_lvAdapter;//,sec_lvAdapeter;
	private ProgressDialog progressDialog;
	private List<ApplicationInfo> nor_appInfos = new ArrayList<ApplicationInfo>();
	private List<ApplicationInfo> sec_appInfos = new ArrayList<ApplicationInfo>();
//	private AppInfoManager appInfoManager;
//	private InstallHelper installHelper;
	protected QuickAction mItemClickQuickAction = null;
	
	private TabHost tabHost;
	private TabWidget tabWidget;
	
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNINSTALL_COMPLETE:
//                	updateList();
                  break;
                case INSTALL_COMPLETE:
//                	updateList();
                	break;
                case SHOW_MENU:
//                	int position = (int) msg.obj;
                	View view = (View) msg.obj;
                	mItemClickQuickAction.show(view);
//                	boolean isSecZone = msg.arg1 == 1;
//                	List<ApplicationInfo> applicationInfos;
//                	if(isSecZone){
//                		applicationInfos = sec_appInfos;
//             			}else{
//                		applicationInfos = nor_appInfos;			
//                					}
//                	String appName = applicationInfos.get(position).loadLabel(getPackageManager()).toString();
//    							String pkgName = applicationInfos.get(position).packageName;
//    							int uid = applicationInfos.get(position).uid;
////    							int userId = UserHandle.getUserId(uid);
//    							Drawable icon = applicationInfos.get(position).loadIcon(getPackageManager());
//    							showMenu(icon,appName,pkgName,0);
    							break;
                default:
                    break;
            }
        }

    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        appInfoManager = AppInfoManager.getInstance();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        installHelper = new InstallHelper(mHandler);
        setContentView(R.layout.activity_main);        
        setViews();
        initPop();
    }

	@Override
	protected void onResume() {
		super.onResume();
		dismissProgressDialog();//if permissin denied this will perfected
//        getApplications();
		nor_appInfos = getApp();
        nor_lvAdapter = new ListViewAdapter(MainActivity.this, mHandler, nor_appInfos, false);
      nor_lv.setAdapter(nor_lvAdapter);
//      sec_lvAdapeter = new ListViewAdapter(XAppManagerActivity.this, mHandler, sec_appInfos, true);
//      sec_lv.setAdapter(sec_lvAdapeter);
	}
	
	
    private List<ApplicationInfo> getApp() {
		// TODO Auto-generated method stub
    	ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>(); //用来存储获取的应用信息数据

    	List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
    	        
    	        for(int i=0;i<packages.size();i++) { 
    	        PackageInfo packageInfo = packages.get(i); 
    	        ApplicationInfo tmpInfo =new ApplicationInfo(); 
    	        tmpInfo.name = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(); 
    	        tmpInfo.packageName = packageInfo.packageName; 
//    	        tmpInfo.versionName = packageInfo.versionName; 
//    	        tmpInfo.versionCode = packageInfo.versionCode; 
    	        tmpInfo.icon = R.drawable.icon;//packageInfo.applicationInfo.loadIcon(getPackageManager());
    	        //Only display the non-system app info
    	            appList.add(tmpInfo);//如果非系统应用，则添加至appList
    	        
    	       }
		return appList;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	Log.d("jin", "destroy dismiss dialog");
    	dismissProgressDialog();
		super.onDestroy();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
		
    private void setViews() {
    	LayoutInflater inflater = getLayoutInflater();
        
    	View view1 = inflater.inflate(R.layout.tabhost_tag, null);
    	TextView txt1 = (TextView) view1.findViewById(R.id.tab_lable);
    	txt1.setText(getString(R.string.normal));
    	        
    	View view2 = inflater.inflate(R.layout.tabhost_tag, null);
    	TextView txt2 = (TextView) view2.findViewById(R.id.tab_lable);
    	txt2.setText(getString(R.string.owner));
    	        
    	//得到TabHost对象实例
    	tabHost =(TabHost) findViewById(R.id.tabhost);
    	//调用 TabHost.setup()
    	tabHost.setup();
    	tabWidget = tabHost.getTabWidget();
    	//创建Tab标签
    	tabHost.addTab(tabHost.newTabSpec("one").setIndicator(view1).setContent(R.id.nor_zone));
    	tabHost.addTab(tabHost.newTabSpec("two").setIndicator(view2).setContent(R.id.owner_zone));
    	
    	//设置第一次打开时默认显示的标签
    	tabHost.setCurrentTab(0);
    	//初始化Tab的颜色，和字体的颜色
    	updateTab(tabHost);
    	//选择监听器
    	tabHost.setOnTabChangedListener(new tabChangedListener());
    	
    	
    	nor_lv = (ListView) findViewById(R.id.nor_zone_apps);
    	sec_lv = (ListView) findViewById(R.id.owner_zone_apps);
    	
//        nor_lvAdapter = new ListViewAdapter(XAppManagerActivity.this, mHandler, nor_appInfos, false);
////        nor_lv.setAdapter(nor_lvAdapter);
//        sec_lvAdapeter = new ListViewAdapter(XAppManagerActivity.this, mHandler, sec_appInfos, true);
////        sec_lv.setAdapter(sec_lvAdapeter);
//    	nor_lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				mItemClickQuickAction.show(arg1);
//			}
//		});
	} 
    
    /**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) 
    {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) 
        {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView text = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_lable);
            ImageView line = (ImageView) tabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_line);
//            tv.setTextSize(16);
//            tv.setTypeface(Typeface.SERIF, 0); // 设置字体和风格
            if (tabHost.getCurrentTab() == i) 
            {
                //选中
//                view.setBackground(getResources().getDrawable(R.drawable.tabhost_current));//选中后的背景
                text.setTextColor(this.getResources().getColorStateList(R.color.green));
                line.setVisibility(View.VISIBLE);
            } 
            else 
            {
                //不选中
//                view.setBackground(getResources().getDrawable(R.drawable.tabhost_default));//非选择的背景
                text.setTextColor(this.getResources().getColorStateList(R.color.gray));
                line.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * TabHost选择监听器
     * @author 
     *
     */
    private class tabChangedListener implements OnTabChangeListener {

          @Override
          public void onTabChanged(String tabId) 
          {
              tabHost.setCurrentTabByTag(tabId);
              updateTab(tabHost);
          }
    }
    
	private void initPop() {
		mItemClickQuickAction = new QuickAction(this, QuickAction.VERTICAL);
		
		ActionItem pointItem = new ActionItem(1, "111", null);
		ActionItem areaItem = new ActionItem(2, "222", null);
		pointItem.setIcon(null);
		areaItem.setIcon(null);
		
		mItemClickQuickAction.addActionItem(pointItem);
		mItemClickQuickAction.addActionItem(areaItem);
		
		mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
//				if (pos == 0) {
//					Intent intent = new Intent(source.GetActionItemsGroup().getContext(),ListViewItemShowActivity.class);
//					RecentItem item = new RecentItem();
//					item = mRecentDatas.get(mClickItem);
//					Bundle bundle = new Bundle();
//					bundle.putInt("HeadImg", item.getHeadImg());
//					bundle.putInt("NewNum", item.getNewNum());
//					bundle.putString("Name", item.getName());
//					intent.putExtras(bundle);
//					startActivity(intent);					
				}
//				//��������ʧ
//				mItemClickQuickAction.dismiss();
//			}
		});
	}
    
    private void showMenu(Drawable icon, String appName,final String packageName,final int userId){
    	
    }
    
 
    
//  private void showMenu(Drawable icon, String appName,final String packageName,final int userId){
//		AlertDialog.Builder builder = new AlertDialog.Builder(XAppManagerActivity.this);
//		builder.setTitle(appName);
//		builder.setIcon(icon);
//		builder.setCancelable(true);
//
//		final int belongTo = appInfoManager.belongTo(packageName);
//
//		CharSequence[] itemsOfOwnerZoneApp = {// app installed in owner zone
//				getResources().getText(R.string.uninstall),
//				getResources().getText(R.string.move_to_normal),
//				getResources().getText(R.string.install_all)};
//
//		CharSequence[] itemsOfNormalZoneApp = {// app installed in normal zone
//				getResources().getText(R.string.uninstall),
//				getResources().getText(R.string.move_to_owner),
//				getResources().getText(R.string.install_all)};
//
//		CharSequence[] itemsOfBothZoneApp = {// app installed in two zone
//				getResources().getText(R.string.uninstall_normal),
//				getResources().getText(R.string.uninstall_owner),
//				getResources().getText(R.string.uninstall_all)};
//		
//		CharSequence[] items = 
//				belongTo == AppInfoManager.USER_ALL? itemsOfBothZoneApp:(userId==UserHandle.USER_OWNER? itemsOfOwnerZoneApp:itemsOfNormalZoneApp);
//		builder.setItems(items, new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface arg0, int position) {
//				switch (position) {
//				case 0:
//					showProgressDialog(getResources().getText(R.string.uninstalling));
//					if (belongTo == AppInfoManager.USER_ALL) {
//						installHelper.unInstallAppAsUser(packageName, false, UserHandle.USER_NORMAL);
//					}else {
//						installHelper.unInstallAppAsUser(packageName, false, userId);
//					}
//					
//					break;
//				case 1:
//					if (belongTo == AppInfoManager.USER_ALL) {
//						installHelper.unInstallAppAsUser(packageName, false, UserHandle.USER_OWNER);
//					}else{
//						showProgressDialog(getResources().getText(R.string.moving));	
//						installHelper.installAppAsUser(packageName,userId==UserHandle.USER_OWNER? 
//								UserHandle.USER_NORMAL:UserHandle.USER_OWNER);
//						
//				        installHelper.setCache(packageName,userId+"");
//					}
//					break;
//				case 2:										
//					if (belongTo == AppInfoManager.USER_ALL) {
//						showProgressDialog(getResources().getText(R.string.uninstalling_all));
//						installHelper.unInstallAppAsUser(packageName, true, AppInfoManager.USER_ALL);
//					}else {
//						showProgressDialog(getResources().getText(R.string.installing_all));	
//						installHelper.installAppAsUser(packageName,userId==UserHandle.USER_OWNER? UserHandle.USER_NORMAL:UserHandle.USER_OWNER);
//					}
//					
//					break;
//				default:
//					break;
//				}
//			}
//		});
//		AlertDialog menu = builder.create();
//		menu.show();
//	}
    
//	private void showProgressDialog(CharSequence msg) {
//		progressDialog = new ProgressDialog(XAppManagerActivity.this);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressDialog.setMessage(msg);
//		progressDialog.setCancelable(false);
//		progressDialog.show();
//	}
	

	private void dismissProgressDialog(){
    if(progressDialog!=null){
        	progressDialog.dismiss();
        	progressDialog = null;
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	 private void getApplications(){
//     	nor_appInfos = appInfoManager.getNorUnSysAppInfos();
//     	sec_appInfos = appInfoManager.getSecUnSysAppInfos();
//     }	 

//	 private void updateList() {
//			// TODO Auto-generated method stub
//	    		getApplications();
//	    		setViews();
//	        nor_lvAdapter.notifyDataSetChanged();
//	        sec_lvAdapeter.notifyDataSetChanged();
//	        dismissProgressDialog();
//		}

}
