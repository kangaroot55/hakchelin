package com.example.hakchelin;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyLocationActivity extends Activity {

	TextView text01;
	LocationManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylocation);
		text01 = (TextView) findViewById(R.id.tv_mylocation);
		Button button01 = (Button) findViewById(R.id.btn_mylocation);
		button01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// LocationManager 객체 초기화 , LocationListener 리스너 설정
				getMyLocation();
			}
		});
	}

	// LocationManager 객체 초기화 , LocationListener 리스너 설정
	private void getMyLocation() {
		if (manager == null) {
			manager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
		}
		// provider 기지국||GPS 를 통해서 받을건지 알려주는 Stirng 변수
		// minTime 최소한 얼마만의 시간이 흐른후 위치정보를 받을건지 시간간격을 설정 설정하는 변수
		// minDistance 얼마만의 거리가 떨어지면 위치정보를 받을건지 설정하는 변수
		// manager.requestLocationUpdates(provider, minTime, minDistance,
		// listener);

		// 10초
		long minTime = 10000;

		// 거리는 0으로 설정
		// 그래서 시간과 거리 변수만 보면 움직이지않고 10초뒤에 다시 위치정보를 받는다
		float minDistance = 0;

		MyLocationListener listener = new MyLocationListener();

		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
				minDistance, listener);

		appendText("내 위치를 요청 했습니다.");
	}


	private void appendText(String msg) {
		text01.append(msg + "\n");
	}

	class MyLocationListener implements LocationListener {

		// 위치정보는 아래 메서드를 통해서 전달된다.
		@Override
		public void onLocationChanged(Location location) {
			appendText("onLocationChanged()가 호출되었습니다");

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

			appendText("현재 위치:" + latitude + "," + longitude);
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

}