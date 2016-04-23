package xyz.ssiqje.speendtest;

import java.util.Set;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class setBluetoothActivity extends Activity
{
	ListView pairedDevicelv,newDevicelv;
	ArrayAdapter<String> pairedAdapter,newDeviceAdapter;
	
	BluetoothAdapter bluetoothAdapter=null;
	String connectBluetoothDevice=null;
	
	BroadcastReceiver discoveryReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			Toast.makeText(setBluetoothActivity.this, action, Toast.LENGTH_LONG).show();
			if(BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				newDeviceAdapter.add("设备名称:"+device.getName()+"\n设备地址:"+device.getAddress());
			} else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				((ProgressBar)findViewById(R.id.discoveryProgress)).setVisibility(View.GONE);
            	((TextView)findViewById(R.id.discoveryTv)).setText("搜索到的新设备：");
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.set_bluetooth);
		pairedDevicelv=(ListView) findViewById(R.id.pairedDeviceslv);
		newDevicelv=(ListView) findViewById(R.id.newDeviceslv);
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		pairedAdapter=new ArrayAdapter<String>(this, R.layout.device_item);
		newDeviceAdapter=new ArrayAdapter<String>(this, R.layout.device_item);
		pairedDevicelv.setAdapter(pairedAdapter);
		pairedDevicelv.setOnItemClickListener(bluetoothDevictClick);
		newDevicelv.setAdapter(newDeviceAdapter);
		newDevicelv.setOnItemClickListener(bluetoothDevictClick);
		Set<BluetoothDevice> pDevices=bluetoothAdapter.getBondedDevices();
		if(pDevices.size()>0)
		{
			for (BluetoothDevice bluetoothDevice : pDevices) {
				pairedAdapter.add("设备名称:"+bluetoothDevice.getName()+"\n设备地址:"+bluetoothDevice.getAddress());
			}
		}
		IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(discoveryReceiver, filter);
		filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(discoveryReceiver, filter);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(discoveryReceiver);
	}

	OnItemClickListener bluetoothDevictClick=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if(bluetoothAdapter.isDiscovering())
			{
				bluetoothAdapter.cancelDiscovery();
			}
			String deviceString=((TextView)view).getText().toString();
			connectBluetoothDevice=deviceString.substring(deviceString.length()-17);
			((TextView)findViewById(R.id.readConnectDevice)).setText("连接设备为:"+deviceString.substring(5, deviceString.indexOf("设备地址")));
			((View)findViewById(R.id.bluetoothsetlayout)).setVisibility(View.GONE);
			((View)findViewById(R.id.scanNewDevBut)).setVisibility(View.GONE);
			((View)findViewById(R.id.scanDeviceLayout)).setVisibility(View.GONE);
		}
	};
	public void seting(View v)
	{
			Intent intent=new Intent();
			intent.putExtra("device", connectBluetoothDevice);
			this.setResult(RESULT_OK, intent);
			this.finish();
		
		
	}
	public void scanDevice(View v)
	{
		View view=(View)findViewById(R.id.scanDeviceLayout);
		view.setVisibility(view.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
		doDiscovery();
	}
	
	public void parameterSet(View v)
	{
		View view=(View)findViewById(R.id.parameterSetLayout);
		view.setVisibility(view.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
	}
	public void doDiscovery()
	{
		if(bluetoothAdapter.isDiscovering())
			bluetoothAdapter.cancelDiscovery();
		bluetoothAdapter.startDiscovery();
	}

}
