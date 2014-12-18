	package com.example.hakchelin;

import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


/*
 * 
 * 해야할일
 *  * 검색 제대로 DB검색으로
 *  * 농식 문제...
 *  * 추천문제...
 *  
 */
public class MenuFragment extends Fragment {

	ListView lv_menu;
	private static ListViewAdapter mAdapter;
	private AlertDialog mDialog;
	public static DBMenuHelper db;
	
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		Fragment newFragment;
		db = new DBMenuHelper(getActivity().getBaseContext());
		
		long now = System.currentTimeMillis();
		Date date = new Date(now);
			
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMMddHH");
		String time = timeformat.format(date);
		
		SimpleDateFormat timeformat2 = new SimpleDateFormat("yyyy년 MM월 dd일");
		String yesi = timeformat2.format(date);
		
		final TextView tv_text = (TextView)view.findViewById(R.id.tv_frg_menu);
	    tv_text.setText("서울대학교 식당 메뉴 학슐랭\n"+yesi);
	    
		String year = time.substring(0,4);
		String month = time.substring(4,6);
		String day = time.substring(6,8);
		String hour = time.substring(8,10);
		

		lv_menu = (ListView)view.findViewById(R.id.lv_frg_menu);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());
		lv_menu.setAdapter(mAdapter);
		lv_menu.setClickable(true);
		
		lv_menu.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				
				mDialog = createDialog(inflater, position);
				mDialog.show();

				
				
			}
	    	
	    });
		

		Log.w("SIBAL",String.valueOf(db.getMenusCount()));
		/*
		mAdapter.addItem("301동","35","뼈없는닭갈비볶음","1.45","1");
		mAdapter.addItem("301동","30","야끼우동","4.15","2");
		mAdapter.addItem("301동","40","돈까스5종,유부장국","2.58","3");
		mAdapter.addItem("301동","35","야끼우동","1.15","4");
		mAdapter.addItem("302동","30","새우볶음밥&칠리소스","3.69","5");
		mAdapter.addItem("302동","30","갈비탕","4.45","6");
		mAdapter.addItem("농생대","30","새우볶음밥&칠리소스","3.69","5");
		mAdapter.addItem("농생대","30","갈비탕","4.45","6");
		*/
		
		getData(year,month,day);
		
		
		return view;
	}
	

	private AlertDialog createDialog(LayoutInflater inflater, final int position) {
		final View innerView = inflater.inflate(R.layout.popup_star, null);

		Button btn_confirm = (Button) innerView.findViewById(R.id.btn_popup_star);

		final RatingBar rb_star = (RatingBar) innerView.findViewById(R.id.rb_popup_star);
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Menu m;
				m = db.getMenu(mAdapter.getMenuLoc(position), mAdapter.getMenuMenu(position));
				db.updateMenu(new Menu(m.getLoc(),m.getPrice(),m.getMenu(),m.getRate(),m.getSum()+rb_star.getRating(),m.getHuman()+1,m.getMenu_id()));
				
				mAdapter.setSumPlus(position, rb_star.getRating());
				mAdapter.setHumanPlus(position);
				mAdapter.dataChange();
				setDismiss(mDialog);

			}
		});

		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setView(innerView);

		return ab.create();
	}

	private void setDismiss(AlertDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private static void getData(String year, String month, String day){

		try {

			
			
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
	                		
	                		Menu mn;
	                		mn = db.getMenu(restaurant, array[i].substring(2));
	                		if(mn!=null)
	                			mAdapter.addItem(mn.getLoc(), mn.getPrice(), mn.getMenu(), mn.getRate(), mn.getMenu_id(),mn.getSum(),mn.getHuman());
	                		
	                		
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
		public int getMenuId(int position) {
			return mMenuListViewData.get(position).menu_id;			
		}
		public String getMenuPrice(int position) {
			return mMenuListViewData.get(position).price;
		}
		public void setHumanPlus(int position){
			mMenuListViewData.get(position).human++;
			dataChange();
		}
		public void setSumPlus(int position, float rating){
			mMenuListViewData.get(position).sum+=rating;
			dataChange();
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

			
			if(position%2==1) 
				convertView.setBackgroundColor(Color.parseColor("#DDDDDD"));
			else
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
				holder.btn_color.setText("????");
			}

			holder.tv_menu.setText(mData.menu);
			if(mData.human!=0) 
				mData.rate=String.format("%.2f", mData.sum/(float)mData.human);
			else
				mData.rate="0.00";
			holder.tv_rate.setText(mData.rate);
			holder.rb_rate.setRating(Float.parseFloat(mData.rate));
			
			return convertView;
		}

		public void addItem(String loc, String price, String menu, String rate, int menu_id, float sum, int human) {
			MenuListViewData addData = null;
			addData = new MenuListViewData();
			addData.loc = loc;
			addData.price = price;
			addData.menu = menu;
			addData.rate = rate;
			addData.human = human;
			addData.sum = sum;
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
		public float sum;
		public int human;
		public int menu_id;

	}

}