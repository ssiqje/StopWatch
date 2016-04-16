package xyz.ssiqje.speendtest;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class setingActivity extends Activity
{
	EditText hourEditText,minEditText,gytEditText,bjEditText;
	String  time=null;
	String gy=null;
	TextWatcher twatcherH;
	ListView pairedDevicelv,newDevicelv;
	ArrayAdapter<String> pairedAdapter,newDeviceAdapter;
	
	BluetoothAdapter bluetoothAdapter=null;
	String connectBluetoothDevice=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.seting);
		hourEditText=(EditText) findViewById(R.id.hourEt);
		minEditText=(EditText) findViewById(R.id.minEt);
		Calendar calendar=Calendar.getInstance();
		hourEditText.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		minEditText.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
		gytEditText=(EditText) findViewById(R.id.gytEt);
		bjEditText=(EditText) findViewById(R.id.bjEt);
		pairedDevicelv=(ListView) findViewById(R.id.pairedDeviceslv);
		newDevicelv=(ListView) findViewById(R.id.newDeviceslv);
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		pairedAdapter=new ArrayAdapter<String>(this, R.layout.device_item);
		newDeviceAdapter=new ArrayAdapter<String>(this, R.layout.device_item);
		pairedDevicelv.setAdapter(pairedAdapter);
		pairedDevicelv.setOnItemClickListener(bluetoothDevictClick);
		newDevicelv.setAdapter(newDeviceAdapter);
		Set<BluetoothDevice> pDevices=bluetoothAdapter.getBondedDevices();
		if(pDevices.size()>0)
		{
			for (BluetoothDevice bluetoothDevice : pDevices) {
				pairedAdapter.add("设备名称:"+bluetoothDevice.getName()+"\n设备地址:"+bluetoothDevice.getAddress());
			}
		}
		
	}
	OnItemClickListener bluetoothDevictClick=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
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
			String h=hourEditText.getText().toString();
			String m=minEditText.getText().toString();
			String gyt=gytEditText.getText().toString();
			String bj=bjEditText.getText().toString();
			if(!h.equals("")||!m.equals(""))
			
				time="("+1+"="+h+m+")";
			
			if(!gyt.equals("")||!bj.equals(""))
			
				gy="("+2+"="+gyt+bj+")";
			
			Intent intent=new Intent();
			intent.putExtra("device", connectBluetoothDevice);
			intent.putExtra("time", time);
			intent.putExtra("gy", gy);
			this.setResult(RESULT_OK, intent);
			this.finish();
		
		
	}
	public void  bluetoothset(View v)
	{
		
		View bluetoothsetlayout=(View)findViewById(R.id.bluetoothsetlayout);
		View scanNewDevBut=(View)findViewById(R.id.scanNewDevBut);
		bluetoothsetlayout.setVisibility(bluetoothsetlayout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
		scanNewDevBut.setVisibility(scanNewDevBut.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
	}
	public void scanDevice(View v)
	{
		View view=(View)findViewById(R.id.scanDeviceLayout);
		view.setVisibility(view.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
	}
	
	public void parameterSet(View v)
	{
		View view=(View)findViewById(R.id.parameterSetLayout);
		view.setVisibility(view.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
	}
	//	@Override
//	public boolean onKey(View v, int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
////		if(event.ACTION_UP)
////		{
////			Toast.makeText(this, "~~~~~~~", Toast.LENGTH_LONG).show();
////		}
////		return false;
//	}
}
