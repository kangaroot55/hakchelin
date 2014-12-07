package com.example.hakchelin;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StarFragment extends Fragment {


	private static TextView tv_star;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_star, container, false);
		Fragment newFragment;
		
		

		tv_star = (TextView)view.findViewById(R.id.tv_frg_star);

		
		return view;
	}
	
	
	
	
}
