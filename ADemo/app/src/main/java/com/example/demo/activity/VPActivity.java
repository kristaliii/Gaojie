package com.example.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.fragment.AddFragment;
import com.example.demo.fragment.QueryFragment;
import com.example.demo.fragment.SettingFragment;
import com.example.demo.interfaces.FragmentListener;

public class VPActivity extends FragmentActivity implements FragmentListener{
	 	private MyPagerAdapter adapter;  
	    private AddFragment addFragment;  
	    private List<String> tagList;  
	    private SettingFragment settingFragment;  
	    private QueryFragment queryFragment;  
	    private ViewPager pager;  
	    private FragmentManager fm;  
	    private List<Fragment> list;  
	  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_main);  
	        pager = (ViewPager) findViewById(R.id.viewPager);  
	        initFragment();  
	        fm = getSupportFragmentManager();  
	        tagList = new ArrayList<String>();  
	        adapter = new MyPagerAdapter(fm);  
	        pager.setAdapter(adapter);  
	      
	    }  
	  
	    public void initFragment() {  
	        list = new ArrayList<Fragment>();  
	        addFragment = new AddFragment();  
	        queryFragment = new QueryFragment();  
	        settingFragment = new SettingFragment();  
	        list.add(addFragment);  
	        list.add(queryFragment);  
	        list.add(settingFragment);  
	    }  
	  
	  
	  
	    public class MyPagerAdapter extends FragmentPagerAdapter {  
	  
	        public MyPagerAdapter(FragmentManager fm) {  
	  
	            super(fm);  
	        }  
	  
	        private final String[] titles = { "1", "2", "3" };  
	  
	        @Override  
	        public CharSequence getPageTitle(int position) {  
	            return titles[position];  
	        }  
	  
	        @Override  
	        public int getCount() {  
	            return titles.length;  
	        }  
	  
	        @Override  
	        public Fragment getItem(int position) {  
	            return list.get(position);  
	        }  
	  
	        public Object instantiateItem(ViewGroup container, int position) {  
	            tagList.add(makeFragmentName(container.getId(), (int) getItemId(position)));  
	            return super.instantiateItem(container, position);  
	        }  
	  
	        public void update(int item) {  
	            Fragment fragment = fm.findFragmentByTag(tagList.get(item));  
	            if (fragment != null) {  
	                switch (item) {  
	                case 0:  
	  
	                    break;  
	                case 1:  
	                    ((QueryFragment) fragment).update();  
	                    break;  
	                case 2:  
	  
	                    break;  
	                default:  
	                    break;  
	                }  
	            }  
	        }  
	  
	    }  
	  
	    public static String makeFragmentName(int viewId, int index) {  
	        return "android:switcher:" + viewId + ":" + index;  
	    }  
	  
	  
	  
	    public void onFragmentClickListener(int item) {  
	        adapter.update(item);  
	        adapter.update(item);  
	    }  
	      
	   
}
