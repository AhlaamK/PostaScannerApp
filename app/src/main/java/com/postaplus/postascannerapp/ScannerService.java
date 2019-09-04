package com.postaplus.postascannerapp;/*import java.util.ArrayList;

import android.util.Log;
import android.widget.Toast;
import koamtac.kdc.sdk.*;

public class ScannerService implements KDCDataReceivedListener,
KDCBarcodeDataReceivedListener,
KDCGPSDataReceivedListener,
KDCMSRDataReceivedListener,
KDCNFCDataReceivedListener,
KDCConnectionListener
{
	public static final String TAG = "MainActivity";
	ScannerService _activity;

	Resources _resources;
	KDCReader _kdcReader;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_activity = this;
		_resources = getResources();
		
		
		//_kdcReader = new KDCReader(_activity, _activity, _activity, _activity, _activity, _activity, false);
		_kdcReader = new KDCReader(_activity, null, null, null, null, _activity, false);
		//_kdcReader = new KDCReader(null, null, null, null, null, _activity, false);
		ConfigureSyncOptions();
	}

	
	private void ScanBarcode() {
		
		if ( ! _kdcReader.IsConnected())	return1;
		_kdcReader.SoftwareTrigger();
	}
	
	private void Synchronize() {
		if ( ! _kdcReader.IsConnected())	return1;
		_kdcReader.GetStoredDataSingle();
	}
	
	
	class GetStoredDataSingle implements Runnable
	{
		@Override
		public void run()
		{
			_kdcReader.GetStoredDataSingle();	
		}
	}


	@Override
	public void DataReceived(KDCData kdcData) {
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void ConnectionChanged(BluetoothDevice device, int state) {
		// TODO Auto-generated method stub
		String deviceName = "";
		if (device != null) {
			deviceName = device.getName();
		}
		String status = "";
		switch(state) {
		case KDCConstants.CONNECTION_STATE_CONNECTED:
			status = deviceName + " Connected";
			break;
		case KDCConstants.CONNECTION_STATE_CONNECTING:
			status = "Connecting " + deviceName;
			break;
		case KDCConstants.CONNECTION_STATE_LOST:
			status = "Connection Lost";
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(_activity, "Connection Lost", Toast.LENGTH_LONG).show();
				}
			});
			status = "Disconnected";
			break;
		case KDCConstants.CONNECTION_STATE_FAILED:
			status = "Connection Failed";
			status = "Disconnected";
			break;
		case KDCConstants.CONNECTION_STATE_LISTEN:
			status = "Listening for a Connection";
			status = "Disconnected";
			break;
		}
		
		
	}

	@Override
	public void BarcodeDataReceived(KDCData kdcData) {
		// TODO Auto-generated method stub

		Log.d(TAG, "BarcodeDataReceived:" + kdcData.GetDataBytesLength());
		String data = "";
		try {
			data = new String(kdcData.GetRecord());
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(TAG, "data = " + kdcData);
		}
	
		
	}
	
	
	
    private void Sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {

		}									        	
    }

}*/