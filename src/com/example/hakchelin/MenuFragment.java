package com.example.hakchelin;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MenuFragment extends Fragment {

	ListView lv_menu;
	static ListViewAdapter mAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		Fragment newFragment;

		lv_menu = (ListView)view.findViewById(R.id.lv_frg_menu);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());

		lv_menu.setAdapter(mAdapter);

		mAdapter.addItem("301동","35","뼈없는닭갈비볶음","1.45","1");
		mAdapter.addItem("301동","30","야끼우동","4.15","2");
		mAdapter.addItem("301동","40","돈까스5종,유부장국","2.58","3");
		mAdapter.addItem("301동","35","야끼우동","1.15","4");
		mAdapter.addItem("302동","30","새우볶음밥&칠리소스","3.69","5");
		mAdapter.addItem("302동","30","갈비탕","4.45","6");
		getData();
		
		
		return view;
	}
	
	private static void getData(){

		try {
			URL url = new URL("http://mini.snu.kr/cafe/set/2014-12-3/acdefvghinjkl");
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
	        
	        while(trIter.hasNext()){
	        	Element tr = (Element) trIter.next();
	            List dataList = tr.getAllElements(HTMLElementName.TD);
	            Iterator dataIter = dataList.iterator();
	            cnt=0;
	            
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
	                	for(i=0;i<array.length;i++){
	                		mAdapter.addItem(restaurant, array[i].substring(0, 2),array[i].substring(2),"4.45","6");
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