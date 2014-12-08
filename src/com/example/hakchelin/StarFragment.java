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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StarFragment extends Fragment {

	private static TextView tv_star;
	ListView lv_star;
	private static ListViewAdapter mAdapter;
	
	private AlertDialog mDialog;

	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_star, container, false);
		Fragment newFragment;
		Button btn_star = (Button)view.findViewById(R.id.btn_frg_star);
		final EditText et_star = (EditText)view.findViewById(R.id.et_frg_star);
		
		int i;
		tv_star = (TextView) view.findViewById(R.id.tv_frg_star);
		/*
		 * for(i=1;i<=3;i++){ getData(2014,11,i); }
		 */

		lv_star = (ListView)view.findViewById(R.id.lv_frg_star);
		mAdapter = new ListViewAdapter(getActivity().getBaseContext());
		lv_star.setAdapter(mAdapter);
		lv_star.setClickable(true);
		
		lv_star.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				
				mDialog = createDialog(inflater, position);
				mDialog.show();
				
			}
	    	
	    });


		mAdapter.addItem("301동","35","뼈없는닭갈비볶음","1.45","1");
		mAdapter.addItem("301동","30","야끼우동","4.15","2");
		mAdapter.addItem("301동","40","돈까스5종,유부장국","2.58","3");
		mAdapter.addItem("301동","35","야끼우동","1.15","4");
		mAdapter.addItem("302동","30","새우볶음밥&칠리소스","3.69","5");
		mAdapter.addItem("302동","30","갈비탕","4.45","6");
		mAdapter.addItem("농생대","30","새우볶음밥&칠리소스","3.69","5");
		mAdapter.addItem("농생대","30","갈비탕","4.45","6");
		
	    btn_star.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				
				mAdapter.search(et_star.getText().toString());
			}
		});
	    
		return view;
	}



	private AlertDialog createDialog(LayoutInflater inflater, final int position) {
		final View innerView = inflater.inflate(R.layout.popup_star, null);

		Button btn_confirm = (Button) innerView.findViewById(R.id.btn_popup_star);

		final RatingBar rb_star = (RatingBar) innerView.findViewById(R.id.rb_popup_star);
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAdapter.setSumPlus(position, rb_star.getRating());
				mAdapter.setHumanPlus(position);
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
	
	public void getData(int year, int month, int day) {

		try {

			URL url = new URL("http://mini.snu.kr/cafe/set/" + String.valueOf(year) + "-" + String.valueOf(month) + "-"
					+ String.valueOf(day) + "/acdefvghinjkl");
			InputStream html = url.openStream();
			Source source = null;
			source = new Source(url);
			source.fullSequentialParse();

			Element div = source.getAllElements(HTMLElementName.DIV).get(1);
			Element table = div.getAllElements(HTMLElementName.TABLE).get(0);
			List trList = table.getAllElements(HTMLElementName.TR);
			Iterator trIter = trList.iterator();
			ArrayList<MenuListViewData> d = new ArrayList<MenuListViewData>();

			int i, j;
			int flag = 0;
			int cnt = 0;
			int dcnt = 0;
			int ffflag = 0;

			String loc = null;

			while (trIter.hasNext()) {
				Element tr = (Element) trIter.next();
				List dataList = tr.getAllElements(HTMLElementName.TD);
				Iterator dataIter = dataList.iterator();
				cnt = 0;

				while (dataIter.hasNext()) {

					Element data = (Element) dataIter.next();
					String value = data.getContent().getTextExtractor().toString();
					if (value.equals("아침")) {
						flag = 1;
					}
					else if (value.equals("점심")) {
						flag = 2;
					}
					else if (value.equals("저녁")) {
						flag = 3;
					}

					if (flag == 2) { // 점심일때

						if (cnt % 2 == 0) { // 식당 이름
							loc = value;
						}
						else if (cnt % 2 == 1) { // 메뉴 이름
							String array[] = value.split(" ");

							for (i = 0; i < array.length; i++) {
								ffflag = 0;
								for (j = 0; j < dcnt; j++) {
									if (loc.equals(d.get(j).loc)
											&& array[i].substring(0, 2).equals(d.get(j).price)
											&& array[i].substring(2).equals(d.get(j).menu)) {
										ffflag = 1;
										break;
									}

								}
								if (ffflag == 0) {
									MenuListViewData s = null;
									s = new MenuListViewData();

									s.loc = loc;
									s.price = array[i].substring(0, 2);
									s.menu = array[i].substring(2);
									s.rate = "4.45";
									d.add(s);
								}

							}
						}

					}
					cnt++;

				}

			}

			for (i = 0; i < d.size(); i++) {
				tv_star.append(d.get(i).loc + "  " + d.get(i).menu + "\n");
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

		
		public void clear(){
			mMenuListViewData.clear();
		}

		public void search(String s){
			for(int i=0;i<mMenuListViewData.size();i++){
				if(!mMenuListViewData.get(i).menu.contains(s)){
					mMenuListViewData.remove(i);
					i--;
				}		
			}
			dataChange();
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

				if(position%2==1) 
					convertView.setBackgroundColor(Color.parseColor("#DDDDDD"));
				
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

		public void addItem(String loc, String price, String menu, String rate, String menu_id) {
			MenuListViewData addData = null;
			addData = new MenuListViewData();
			addData.loc = loc;
			addData.price = price;
			addData.menu = menu;
			addData.rate = rate;
			addData.human = 0;
			addData.sum = 0;
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
		public String menu_id;

	}
}
