package com.example.hakchelin;

import android.os.Bundle;
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
         
        btn_1.setOnClickListener(onBtnClickListener);
        btn_2.setOnClickListener(onBtnClickListener);
        btn_3.setOnClickListener(onBtnClickListener);
        btn_4.setOnClickListener(onBtnClickListener);
         
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment newFragment;
         
        newFragment = new MenuFragment();
         
        ft.replace(R.id.main_fl, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        
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
/*        	FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment newFragment;
             
            newFragment = new MenuFragment();
             
            ft.replace(R.id.main_fl, newFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            */
        }
    };
}