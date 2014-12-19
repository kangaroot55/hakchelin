package com.example.hakchelin;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuggestFragment extends Fragment{

	ListView lv_menu;
	static ListViewAdapter mAdapter;
	
	LocationManager manager;

	public double user_lat;
	public double user_lng;
	private ProgressBar bar;

	public static String minrestaurant;
	public static DBMenuHelper db;
	static SharedPreferences pref;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_suggest, container, false);
		Fragment newFragment;
		
		db = new DBMenuHelper(getActivity().getBaseContext());
		
		long now = System.currentTimeMillis();
		Date date = new Date(now);
			
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMMddHH");
		String time = timeformat.format(date);
		
	    
		final String year = time.substring(0,4);
		final String month = time.substring(4,6);
		final String day = time.substring(6,8);
		String hour = time.substring(8,10);
		
		SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		String strCurDate = CurDateFormat.format(date);
		
		final TextView tv_text = (TextView)view.findViewById(R.id.tv_frg_suggest);
	    tv_text.setText(strCurDate + "....\n학슐랭 추천 밥 찾기!");
	    
	    Button btn_opti = (Button)view.findViewById(R.id.btn_frg_suggest_opti);
	    Button btn_deli = (Button)view.findViewById(R.id.btn_frg_suggest_deli);
	    Button btn_near = (Button)view.findViewById(R.id.btn_frg_suggest_near);
	    
	    bar = (ProgressBar)view.findViewById(R.id.progressBar);
	    
	    final LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll_frg_suggest);
	    final LinearLayout ll_ok = (LinearLayout)view.findViewById(R.id.ll_frg_suggest_ok);
	    
		lv_menu = (ListView)view.findViewById(R.id.lv_frg_suggest);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());

		lv_menu.setAdapter(mAdapter);
	     
		if(getMyLocation()==true){
			
		}

	    btn_opti.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				ll.setVisibility(View.GONE);
				ll_ok.setVisibility(View.VISIBLE);
				bar.setVisibility(View.VISIBLE);
				pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
				Menu mn;
        		mn = db.getMenu(pref.getString("loc", ""),pref.getString("menu", ""));
        		try{ 
        			Thread.sleep(500);
        		}catch(Exception e){}
				mAdapter.addItem(mn.getLoc(), mn.getPrice(), mn.getMenu(),mn.getRate(), String.valueOf(mn.getMenu_id()));
				bar.setVisibility(View.GONE);
			}
		});
	    btn_deli.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				ll.setVisibility(View.GONE);
				ll_ok.setVisibility(View.VISIBLE);
				
				pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
				Menu mn;
        		mn = db.getMenu(pref.getString("loc", ""),pref.getString("menu", ""));
        		
        		try{ 
        			Thread.sleep(500);
        		}catch(Exception e){}
        		mAdapter.addItem(mn.getLoc(), mn.getPrice(), mn.getMenu(),mn.getRate(), String.valueOf(mn.getMenu_id()));	
			}
		});
	    
	    btn_near.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				ll.setVisibility(View.GONE);
				ll_ok.setVisibility(View.VISIBLE);
				
				getNearData(year,month,day);
				
					
			}
		});

	    
		
	    
		return view;
	}

	private static void getNearData(String year, String month, String day){

		try {

			float star = 0;
			String maxmenu;
			String maxloc;
			
			URL url = new URL("http://mini.snu.kr/cafe/set/"+year+"-"+month+"-"+day+"/acdefvghinjkl");
			InputStream html = url.openStream();
			Source source = null;
			source = new Source(url);
			source.fullSequentialParse();

	        Element div = source.getAllElements(HTMLElementName.DIV).get(1);
	        Element table = div.getAllElements(HTMLElementName.TABLE).get(0);
	        List trList = table.getAllElements(HTMLElementName.TR);
	        Iterator trIter = trList.iterator();	         

	        int i;
	        int flag=0;
	        int cnt=0;
	        String restaurant = null;
	        
	        int cccnt=0;
	        while(trIter.hasNext()){
	        	Element tr = (Element) trIter.next();
	            List dataList = tr.getAllElements(HTMLElementName.TD);
	            Iterator dataIter = dataList.iterator();
	            cnt=0;
	            cccnt++;
	            
	            while(dataIter.hasNext()){

	                Element data = (Element) dataIter.next();
	                String value = data.getContent().getTextExtractor().toString();

	                if(value.equals("아침")){
	                	flag=1;
	                }else if(value.equals("점심")){
	                	flag=2;
	                }else if(value.equals("저녁")){
	                	flag=3;
	                }
	                if(flag==2){

	                if(cnt%2==0){	// 식당 이름 
	                	restaurant = value;
	                }else if(cnt%2==1){	// 메뉴 이름
	                	
	                	String array[] = value.split(" ");
	                	if(minrestaurant.equals("")){
	                		minrestaurant = "302동"; 
	                	}
	                	if(restaurant.equals(minrestaurant)){
		                	for(i=0;i<array.length;i++){
		                		
		                		Menu mn;
		                		mn = db.getMenu(restaurant, array[i].substring(2));
		                		if(mn!=null){
		                			
		                			mAdapter.addItem(mn.getLoc(), mn.getPrice(), mn.getMenu(), mn.getRate(), String.valueOf(mn.getMenu_id()));
		                			
		                		}
		                		
		                	}
	                	}
	                }

	                }
	                cnt++;
		        	
	            }
	            
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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

			double min = 999999999;
			user_lat = location.getLatitude();
			user_lng = location.getLongitude();

			
			if (min > calc_dist(user_lat, user_lng, 37.459326, 126.950660)) {
				minrestaurant = "학생회관";
				min = calc_dist(user_lat, user_lng, 37.459326, 126.950660);
			}
			if (min > calc_dist(user_lat, user_lng, 37.456847, 126.948472)) {
				minrestaurant = "전망대 (농대)";
				min = calc_dist(user_lat, user_lng, 37.456847, 126.948472);
			}
			if (min > calc_dist(user_lat, user_lng, 37.456847, 126.948472)) {
				minrestaurant = "두레미담";
				min = calc_dist(user_lat, user_lng, 37.456847, 126.948472);
			}
			if (min > calc_dist(user_lat, user_lng, 37.460669, 126.955695)) {
				minrestaurant = "서당골 (사범대)";
				min = calc_dist(user_lat, user_lng, 37.460669, 126.955695);
			}
			if (min > calc_dist(user_lat, user_lng, 37.463234, 126.950097)) {
				minrestaurant = "감골식당";
				min = calc_dist(user_lat, user_lng, 37.463234, 126.950097);
			}
			if (min > calc_dist(user_lat, user_lng, 37.461699, 126.957792)) {
				minrestaurant = "기숙사 (901동)";
				min = calc_dist(user_lat, user_lng, 37.461699, 126.957792);
			}
			if (min > calc_dist(user_lat, user_lng, 37.463254, 126.958442)) {
				minrestaurant = "기숙사 (919동)";
				min = calc_dist(user_lat, user_lng, 37.463254, 126.958442);
			}
			if (min > calc_dist(user_lat, user_lng, 37.456847, 126.948472)) {
				minrestaurant = "두레미담";
				min = calc_dist(user_lat, user_lng, 37.456847, 126.948472);
			}
			if (min > calc_dist(user_lat, user_lng, 37.465000, 126.951729)) {
				minrestaurant = "동원관";
				min = calc_dist(user_lat, user_lng, 37.465000, 126.951729);
			}
			if (min > calc_dist(user_lat, user_lng, 37.460923, 126.952515)) {
				minrestaurant = "자하연";
				min = calc_dist(user_lat, user_lng, 37.460923, 126.952515);
			}
			if (min > calc_dist(user_lat, user_lng, 37.464209, 126.954065)) {
				minrestaurant = "220동";
				min = calc_dist(user_lat, user_lng, 37.464209, 126.954065);
			}
			if (min > calc_dist(user_lat, user_lng, 37.450247, 126.952593)) {
				minrestaurant = "301동";
				min = calc_dist(user_lat, user_lng, 37.450247, 126.952593);
			}
			if (min > calc_dist(user_lat, user_lng, 37.448715, 126.952427)) {
				minrestaurant = "302동";
				min = calc_dist(user_lat, user_lng, 37.448715, 126.952427);
			}
			if (min > calc_dist(user_lat, user_lng, 37.457238, 126.950787)) {
				minrestaurant = "공깡";
				min = calc_dist(user_lat, user_lng, 37.457238, 126.950787);
			}

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

		public TextView tv_loc;
		public Button btn_color;
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
				holder.btn_color = (Button) convertView
						.findViewById(R.id.btn_lv_frg_menu_color);
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

			if (mData.price.equals("17")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#CAB776"));
			}
			else if (mData.price.equals("25")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#C976C6"));
			}
			else if (mData.price.equals("30")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#CDA33B"));
			}
			else if (mData.price.equals("35")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#0E379A"));
			}
			else if (mData.price.equals("40")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#5F8AF8"));
			}
			else if (mData.price.equals("45")) {
				holder.btn_color.setBackgroundColor(Color.parseColor("#525252"));
			}
			else {
				holder.btn_color.setBackgroundColor(Color.parseColor("#000000"));
			}
			if(!mData.price.equals("??")){
				holder.btn_color.setText(mData.price+"00");
			}else{
				holder.btn_color.setText("??");
			}
			
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