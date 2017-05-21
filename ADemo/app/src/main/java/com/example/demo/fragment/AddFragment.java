package com.example.demo.fragment;

import com.example.demo.interfaces.FragmentListener;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class AddFragment extends Fragment{

	
	private FragmentListener listener;

	@SuppressWarnings("deprecation")
	public void onAttach(Activity activity) {    
	    super.onAttach(activity);    
	    try {    
	        listener = (FragmentListener)activity;    
	    } catch (Exception e) {    
	        e.printStackTrace();    
	    }    
	    if (listener != null) {  
            listener.onFragmentClickListener(1);  
        }  

	}    
}
