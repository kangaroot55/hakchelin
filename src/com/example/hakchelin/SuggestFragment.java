package com.example.hakchelin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuggestFragment extends Fragment{

	ListView lv_menu;
	ListViewAdapter mAdapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_suggest, container, false);
		Fragment newFragment;

		
		long now = System.currentTimeMillis();

		Date date = new Date(now);
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		String strCurDate = CurDateFormat.format(date);
		
		final TextView tv_text = (TextView)view.findViewById(R.id.tv_frg_suggest);
	    tv_text.setText(strCurDate + "....\n학슐랭 추천 밥 찾기!");
	    
	    Button btn_opti = (Button)view.findViewById(R.id.btn_frg_suggest_opti);
	    Button btn_deli = (Button)view.findViewById(R.id.btn_frg_suggest_deli);
	    Button btn_near = (Button)view.findViewById(R.id.btn_frg_suggest_near);
	    
	    final LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll_frg_suggest);
	    final LinearLayout ll_ok = (LinearLayout)view.findViewById(R.id.ll_frg_suggest_ok);
	    
		lv_menu = (ListView)view.findViewById(R.id.lv_frg_suggest);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());

		lv_menu.setAdapter(mAdapter);
	     
	    btn_opti.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				ll.setVisibility(View.GONE);
				ll_ok.setVisibility(View.VISIBLE);
				mAdapter.addItem("302동","30","새우볶음밥&칠리소스","3.69","5");
			}
		});
	    btn_deli.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				tv_text.setText("멀어도 좋으니 제일 맛있는 밥이 먹고 싶어요");				
			}
		});
	    btn_near.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				tv_text.setText("너무 배고파서 가까운 곳의 밥이 먹고 싶어요");				
			}
		});

	    
		return view;
	}

	
	private class ViewHolder {

		public TextView tv_loc;
		public TextView tv_price;
		public TextView tv_menu;
		public TextView tv_rate;
		public RatingBar rb_rate;

	}	

	private class ListViewAdapter extends BaseAdapter {
		private Context mContext = null;
		private ArrayList<MenuListViewData> mMenuListViewData = new ArrayList<MenuListViewData>();

		public ListViewAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return mMenuListViewData.size();
		}

		@Override
		public Object getItem(int position) {
			return mMenuListViewData.get(position);
		}

		public String getMenuRate(int position) {
			return mMenuListViewData.get(position).rate;
		}

		public String getMenuLoc(int position) {
			return mMenuListViewData.get(position).loc;
		}
		public String getMenuMenu(int position) {
			return mMenuListViewData.get(position).menu;
		}
		public String getMenuId(int position) {
			return mMenuListViewData.get(position).menu_id;			
		}
		public String getMenuPrice(int position) {
			return mMenuListViewData.get(position).price;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.listview_frg_menu, null);

				holder.tv_loc = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_menu_loc);
				holder.tv_price = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_menu_price);
				holder.tv_menu = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_menu_menu);
				holder.tv_rate = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_menu_rate);
				holder.rb_rate = (RatingBar)convertView
						.findViewById(R.id.rb_lv_frg_menu);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MenuListViewData mData = mMenuListViewData.get(position);

			holder.tv_loc.setText(mData.loc);
			holder.tv_price.setText(mData.price);
			holder.tv_menu.setText(mData.menu);
			holder.tv_rate.setText(mData.rate);
			holder.rb_rate.setRating(Float.parseFloat(mData.rate));
			return convertView;
		}

		public void addItem(String loc, String price, String menu, String rate, String menu_id) {
			MenuListViewData addData = null;
			addData = new MenuListViewData();
			addData.loc = loc;
			addData.price = price;
			addData.menu = menu;
			addData.rate = rate;
			addData.menu_id = menu_id;

			mMenuListViewData.add(addData);
		}

		public void remove(int position) {
			mMenuListViewData.remove(position);
			dataChange();
		}

		public void dataChange() {
			mAdapter.notifyDataSetChanged();
		}
	}

	public class MenuListViewData {
		/**
		 * 리스트 정보를 담고 있을 객체 생성
		 */
		public String loc;
		public String price;
		public String menu;
		public String rate;
		public String menu_id;

	}

}