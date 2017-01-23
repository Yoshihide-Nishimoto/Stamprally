package yoshihide.nishimoto.stamprally;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import yoshihide.nishimoto.stamprally.Stamprally;

public class AndroidLauncher extends AndroidApplication implements LocationListener {

	// 更新時間(目安)
	private static final int LOCATION_UPDATE_MIN_TIME = 0;
	// 更新距離(目安)
	private static final int LOCATION_UPDATE_MIN_DISTANCE = 0;

	private LocationManager mLocationManager;
	private Stamprally stamprally;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		stamprally = new Stamprally();
		initialize(stamprally, config);

		mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		requestLocationUpdates();
	}

	@Override
	public void onLocationChanged(Location location) {
		showLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		showProvider(provider);
		switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				// if the provider is out of service, and this is not expected to change in the near future.
				String outOfServiceMessage = provider + "が圏外になっていて取得できません。";
				showMessage(outOfServiceMessage);
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// if the provider is temporarily unavailable but is expected to be available shortly.
				String temporarilyUnavailableMessage = "一時的に" + provider + "が利用できません。もしかしたらすぐに利用できるようになるかもです。";
				showMessage(temporarilyUnavailableMessage);
				break;
			case LocationProvider.AVAILABLE:
				// if the provider is currently available.
				if (provider.equals(LocationManager.GPS_PROVIDER)) {
					String availableMessage = provider + "が利用可能になりました。";
					showMessage(availableMessage);
					requestLocationUpdates();
				}
				break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		String message = provider + "が有効になりました。";
		showMessage(message);
		showProvider(provider);
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			requestLocationUpdates();
		}
	}

	// Called when the provider is disabled by the user.
	@Override
	public void onProviderDisabled(String provider) {
		showProvider(provider);
		if (provider.equals(LocationManager.GPS_PROVIDER)) {
			String message = provider + "が無効になってしまいました。";
			showMessage(message);
		}
	}

	private void requestLocationUpdates() {
		showProvider(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		showNetworkEnabled(isNetworkEnabled);
		Log.d("isNetworkEnabled",String.valueOf(isNetworkEnabled));
		if (isNetworkEnabled) {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					LOCATION_UPDATE_MIN_TIME,
					LOCATION_UPDATE_MIN_DISTANCE,
					this);
			Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				showLocation(location);
			}
		} else {
			String message = "Networkが無効になっています。";
			showMessage(message);
		}
	}

	private void showLocation(Location location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		//if (!(longitude > 135.141967 && longitude < 135.142407) || !(latitude > 34.252059 && latitude < 34.252411)) {
			String message = "ビッグジョイヤマトから離れました。";
			showMessage(message);
			stamprally.setGPS(longitude,latitude);
		//} else {
		//	String message = "ビッグジョイヤマトに居ます。";
		//	showMessage(message);
		//}

	}

	private void showMessage(String message) {
		stamprally.setMessage(message);
	}

	private void showProvider(String provider) {
		stamprally.setProvider(provider);
	}

	private void showNetworkEnabled(boolean isNetworkEnabled) {
		stamprally.setNetworkenabled(isNetworkEnabled);
	}
}
