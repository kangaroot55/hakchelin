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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class StarFragment extends Fragment {

	private static TextView tv_star;

	private AlertDialog mDialog;

	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_star, container, false);
		Fragment newFragment;
		Button btn_star = (Button)view.findViewById(R.id.btn_frg_star);
		
		int i;
		tv_star = (TextView) view.findViewById(R.id.tv_frg_star);
		/*
		 * for(i=1;i<=3;i++){ getData(2014,11,i); }
		 */


		return view;
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
			ArrayList<MenuInfo> d = new ArrayList<MenuInfo>();

			int i, j;
			int flag = 0;
			int cnt = 0;
			int dcnt = 0;
			int ffflag = 0;

			String restaurant = null;

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
							restaurant = value;
						}
						else if (cnt % 2 == 1) { // 메뉴 이름
							String array[] = value.split(" ");

							for (i = 0; i < array.length; i++) {
								ffflag = 0;
								for (j = 0; j < dcnt; j++) {
									if (restaurant.equals(d.get(j).restaurant)
											&& array[i].substring(0, 2).equals(d.get(j).price)
											&& array[i].substring(2).equals(d.get(j).menu)) {
										ffflag = 1;
										break;
									}

								}
								if (ffflag == 0) {
									MenuInfo s = null;
									s = new MenuInfo();

									s.restaurant = restaurant;
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
				tv_star.append(d.get(i).restaurant + "  " + d.get(i).menu + "\n");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MenuInfo {
		
		public String restaurant;

		public String price;

		public String menu;

		public String rate;
		public float sum;
		public int human;
	}

}
