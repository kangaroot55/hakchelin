package com.example.hakchelin;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
 
public class MenuBarFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menubar, null);
         
        Button btn_1 = (Button) view.findViewById(R.id.btn_fragment_menubar_1);
        Button btn_2 = (Button) view.findViewById(R.id.btn_fragment_menubar_2);
        Button btn_3 = (Button) view.findViewById(R.id.btn_fragment_menubar_3);
        Button btn_4 = (Button) view.findViewById(R.id.btn_fragment_menubar_4);
         
        if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
         
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        final Fragment firstFragment;
         
        firstFragment = new MenuFragment();
         
        ft.replace(R.id.main_fl, firstFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        
        
        btn_1.setOnClickListener(onBtnClickListener);
        
        btn_1.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				FragmentTransaction newft = getFragmentManager().beginTransaction();
				Fragment newFragment;
		        newFragment = new MenuFragment();
		         
		        newft.replace(R.id.main_fl, newFragment);
		        newft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		        newft.commit();
		        				
			}
		});
        
        btn_2.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				FragmentTransaction newft = getFragmentManager().beginTransaction();
				Fragment newFragment;
		        newFragment = new SuggestFragment();
		         
		        newft.replace(R.id.main_fl, newFragment);
		        newft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		        newft.commit();
		        				
			}
		});

        btn_3.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				FragmentTransaction newft = getFragmentManager().beginTransaction();
				Fragment newFragment;
		        newFragment = new StarFragment();
		         
		        newft.replace(R.id.main_fl, newFragment);
		        newft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		        newft.commit();
		        				
			}
		});

        
        btn_4.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				FragmentTransaction newft = getFragmentManager().beginTransaction();
				Fragment newFragment;
		        newFragment = new SettingFragment();
		         
		        newft.replace(R.id.main_fl, newFragment);
		        newft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		        newft.commit();
		        				
			}
		});

        return view;
    }
     
     

    /*
    private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_ONE:
			newFragment = new OneFragment();
			break;
		case FRAGMENT_TWO:
			newFragment = new TwoFragment();
			break;
		case FRAGMENT_THREE:
			newFragment = new ThreeFragment();
			break;

		default:
			Log.d(TAG, "Unhandle case");
			break;
		}

		return newFragment;
	}
*/
     
    private OnClickListener onBtnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        	
            
        }
    };
}