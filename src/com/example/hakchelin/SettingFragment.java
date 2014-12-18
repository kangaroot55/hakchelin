package com.example.hakchelin;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class SettingFragment extends Fragment{

	


	double user_lat;
	double user_lng;
	
	
	TextView tv_location;
	LocationManager manager;

	ListView lv_menu;
	ListViewAdapter mAdapter;

	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		Fragment newFragment;

		

		tv_location = (TextView)view.findViewById(R.id.tv_mylocation);
		lv_menu = (ListView)view.findViewById(R.id.lv_frg_setting);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());

		lv_menu.setAdapter(mAdapter);

		
		if(getMyLocation()==true){
			
		}
		

		
		return view;
	}

	// LocationManager 객체 초기화 , LocationListener 리스너 설정
	private boolean getMyLocation() {
		if (manager == null) {
			manager = (LocationManager)getActivity().getBaseContext()
					.getSystemService(Context.LOCATION_SERVICE);
		}
		// provider 기지국||GPS 를 통해서 받을건지 알려주는 Stirng 변수
		// minTime 최소한 얼마만의 시간이 흐른후 위치정보를 받을건지 시간간격을 설정 설정하는 변수
		// minDistance 얼마만의 거리가 떨어지면 위치정보를 받을건지 설정하는 변수
		// manager.requestLocationUpdates(provider, minTime, minDistance,
		// listener);

		// 10초
		long minTime = 1000;

		// 거리는 0으로 설정
		// 그래서 시간과 거리 변수만 보면 움직이지않고 10초뒤에 다시 위치정보를 받는다
		float minDistance = 0;

		MyLocationListener listener = new MyLocationListener();

		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime,
				minDistance, listener);
		
		return false;
	}



	class MyLocationListener implements LocationListener {

		// 위치정보는 아래 메서드를 통해서 전달된다.
		@Override
		public void onLocationChanged(Location location) {

			user_lat =  location.getLatitude();
			user_lng = location.getLongitude();

			tv_location.setText(user_lat+" "+user_lng);

			mAdapter.clear();
			
			mAdapter.addItem("학생회관", 37.459326, 126.950660, "5.00");
			mAdapter.addItem("농식", 37.456847, 126.948472, "5.00");
			mAdapter.addItem("두레미담", 37.456847, 126.948472, "5.00");
			mAdapter.addItem("서당골", 37.460669, 126.955695, "5.00");
			mAdapter.addItem("감골식당", 37.463234, 126.950097, "5.00");
			mAdapter.addItem("901동", 37.461699, 126.957792, "5.00");
			mAdapter.addItem("919동", 37.463254, 126.958442, "5.00");
			mAdapter.addItem("동원관", 37.465000, 126.951729, "5.00");
			mAdapter.addItem("자하연", 37.460923, 126.952515, "5.00");
			mAdapter.addItem("대학원 220동", 37.464209, 126.954065, "5.00");
			mAdapter.addItem("301동", 37.450247, 126.952593, "5.00");
			mAdapter.addItem("302동", 37.448715, 126.952427, "5.00");
			mAdapter.addItem("공깡", 37.457238, 126.950787, "5.00");
			
			mAdapter.dataChange();
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override	
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	}

	public double calc_dist(double latA, double lngA, double latB, double lngB) {
		
		double distance;

		Location locationA = new Location("point A");

		locationA.setLatitude(latA);
		locationA.setLongitude(lngA);

		Location locationB = new Location("point B");

		locationB.setLatitude(latB);
		locationB.setLongitude(lngB);

		distance = locationA.distanceTo(locationB);

		return distance;
	}
	
	

	private class ViewHolder {

		public TextView tv_distance;
		public TextView tv_name;
		public TextView tv_rate;
		public RatingBar rb_rate;

	}	
	
	/*
	 * ListView
	 * 
	 * */

	private class ListViewAdapter extends BaseAdapter {
		private Context mContext = null;
		private ArrayList<RestaurantListData> mRestaurantListViewData = new ArrayList<RestaurantListData>();

		public ListViewAdapter(Context mContext) {
			super();
			this.mContext = mContext;
		}
		
		public void clear(){
			mRestaurantListViewData.clear();
		}

		@Override
		public int getCount() {
			return mRestaurantListViewData.size();
		}

		@Override
		public Object getItem(int position) {
			return mRestaurantListViewData.get(position);
		}

		public double getRestaurantLat(int position) {
			return mRestaurantListViewData.get(position).lat;
		}
		public double getRestaurantLng(int position) {
			return mRestaurantListViewData.get(position).lng;
		}
		public String getRestaurantName(int position) {
			return mRestaurantListViewData.get(position).name;
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
						.inflate(R.layout.listview_frg_restaurant, null);


				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_restaurant_distance);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_restaurant_name);
				holder.tv_rate = (TextView) convertView
						.findViewById(R.id.tv_lv_frg_restaurant_rate);
				holder.rb_rate = (RatingBar)convertView
						.findViewById(R.id.rb_lv_frg_restaurant);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position%2==1) 
				convertView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			else
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));

			RestaurantListData mData = mRestaurantListViewData.get(position);
			
			holder.tv_distance.setText(String.valueOf(Math.round(mData.distance))+"m");
			holder.tv_name.setText(mData.name);
			holder.tv_rate.setText(mData.rate);
			holder.rb_rate.setRating(Float.parseFloat(mData.rate));
			
			return convertView;
		}

		public void addItem(String name, double lat, double lng, String rate) {
			RestaurantListData addData = null;
			addData = new RestaurantListData();
			addData.lat = lat;
			addData.lng = lng;
			addData.name = name;
			addData.rate = rate;
			addData.distance = calc_dist(addData.lat, addData.lng, user_lat, user_lng);

			mRestaurantListViewData.add(addData);
		}

		public void remove(int position) {
			mRestaurantListViewData.remove(position);
			dataChange();
		}

		public void dataChange() {
			
			mAdapter.notifyDataSetChanged();
		}
	}
	public class RestaurantListData {
		
		public double lat;
		public double lng;
		public double distance;
		public String rate;
		public String name;

	}


}