package xyz.ssiqje.speendtest;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.ssiqje.bluetooth.BluetoothChatService;
import xyz.ssiqje.speendtest.anim.Sanim;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int TIME = 6;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int SET_BLUETOOTH = 3;
	private static final int SET_PARAMETER = 4;
	private BluetoothChatService mChatService = null;
	private BluetoothAdapter mBluetoothAdapter;
	private String mConnectedDeviceName;
	int width;
	int height;
	ImageView sdppoint;
	ImageView wdppoint;
	ImageView switchy;
	ImageView switchj;
	ImageView turnl;
	ImageView turnr;
	ImageView electricquantityimg;
	ImageView lightlivilimg;
	TextView totalKMTv;
	TextView timeTV;
	int[] electricquantityImgList;
	int [] lightlivilimgList;
	float sdpcurrentangle = 0;
	float wdpcurrentangle = 0;
	Sanim sanim = null;
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_READ:
				String[] data = null;
				String readMessage=null;
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				try {
					readMessage=new String(readBuf, 0, msg.arg1, "UTF-8");
					if (readMessage.startsWith("(") && readMessage.endsWith(")")) 
					{
						readMessage = new String(readBuf, 1, (msg.arg1 - 2));
						data = readMessage.split("=");
						if(data.length!=9)
						{
							Toast.makeText(MainActivity.this, "参数不全！", Toast.LENGTH_LONG).show();
							break;
						}
					} 
					else 
					{
						Toast.makeText(MainActivity.this, "参数格式不正确！", Toast.LENGTH_LONG).show();
						break;
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if (sdppoint != null) {
					AnimationSet animation = new AnimationSet(true);
					RotateAnimation rotate = new RotateAnimation(
							sdpcurrentangle,
							(Float.parseFloat(data[0])) / 5 * 19,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					rotate.setDuration(500);
					animation.addAnimation(rotate);
					animation.setFillAfter(true);
					sdppoint.startAnimation(animation);
					sdpcurrentangle = (Float.parseFloat(data[0])) / 5 * 19;

				}
				if (wdppoint != null) {
					AnimationSet animation = new AnimationSet(true);
					RotateAnimation rotate = new RotateAnimation(
							wdpcurrentangle,
							19.08f / 4 * (Float.parseFloat(data[6])),
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					rotate.setDuration(500);
					animation.addAnimation(rotate);
					animation.setFillAfter(true);
					wdppoint.startAnimation(animation);
					wdpcurrentangle = 19.08f / 4 * (Float.parseFloat(data[6]));
				}
				turnl.setImageResource(data[1].equals("0") ? R.drawable.nu
						: R.drawable.l);
				turnr.setImageResource(data[2].equals("0") ? R.drawable.nu
						: R.drawable.r);
				switchy.setImageResource(data[3].equals("0") ? R.drawable.nu
						: R.drawable.y);
				switchj.setImageResource(data[4].equals("0") ? R.drawable.nu
						: R.drawable.j);
				electricquantityimg
						.setImageResource(electricquantityImgList[(Integer
								.parseInt(data[5])) ]);
				totalKMTv.setText("Total:" + data[7] + "KM");
				lightlivilimg.setImageResource(data[8].equals("0")?R.drawable.liang1:
											   data[8].equals("1")?R.drawable.liang2:R.drawable.liang3);

				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case 123:
				initGameview();
				break;
			case TIME:
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				timeTV.setText(dateFormat.format(new Date()));
				mHandler.sendEmptyMessageDelayed(TIME, 1000);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		width = dm.widthPixels;
		height = dm.heightPixels;
		sanim = new Sanim();
		setContentView(new welcomeview(this, mHandler, width, height));
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "对不起，你本机没有蓝牙设备！", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
//			Intent enableIntent = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			mBluetoothAdapter.enable();
			// Otherwise, setup the chat session
		}
		
			setupChat();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void setupChat() {
		// TODO Auto-generated method stub
		if (mChatService == null)
		mChatService = new BluetoothChatService(this, mHandler);
	}

BroadcastReceiver mainuiReceiver =new BroadcastReceiver() {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED))
		{
			((View)findViewById(R.id.bluetooth_not_connectimg)).setVisibility(View.GONE);
			((View)findViewById(R.id.bluetooth_setBut)).setVisibility(View.GONE);
			((View)findViewById(R.id.config_setBut)).setVisibility(View.VISIBLE);
		}
		else
		{
			((View)findViewById(R.id.bluetooth_not_connectimg)).setVisibility(View.VISIBLE);
			((View)findViewById(R.id.bluetooth_setBut)).setVisibility(View.VISIBLE);
			((View)findViewById(R.id.config_setBut)).setVisibility(View.GONE);
		}
		
	}
};
	private void initGameview() {
		// TODO Auto-generated method stub
		// gameview=new gameview(this, width,height);
		this.setContentView(R.layout.gameview);
		IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		registerReceiver(mainuiReceiver, filter);
		sdppoint = (ImageView) findViewById(R.id.sdpimg);
		wdppoint = (ImageView) findViewById(R.id.wdpimg);
		switchy = (ImageView) findViewById(R.id.switchy);
		switchj = (ImageView) findViewById(R.id.switchj);
		turnl = (ImageView) findViewById(R.id.turnl);
		turnr = (ImageView) findViewById(R.id.turnr);
		electricquantityimg = (ImageView) findViewById(R.id.electricquantityimg);
		lightlivilimg=(ImageView) findViewById(R.id.lightlivilimg);
		electricquantityImgList = new int[] { 	R.drawable.electricquantity1,
												R.drawable.electricquantity2, 
												R.drawable.electricquantity3,
												R.drawable.electricquantity4, 
												R.drawable.electricquantity5,
												R.drawable.electricquantity6 };
		lightlivilimgList=new int []{R.drawable.liang1,
									 R.drawable.liang2,
									 R.drawable.liang3};
		totalKMTv = (TextView) findViewById(R.id.totalKM_Tv);
		timeTV = (TextView) findViewById(R.id.timeTV);
		mHandler.sendEmptyMessageDelayed(TIME, 1000);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
					setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		case SET_BLUETOOTH:
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						"device");
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
			}
		case SET_PARAMETER:
			if (resultCode == Activity.RESULT_OK) {
				String time = data.getStringExtra("time");
				String gy = data.getStringExtra("gy");
				Toast.makeText(this, "->" + time + "<->" + gy + "<-",
						Toast.LENGTH_LONG).show();

					if (mChatService.getState() == mChatService.STATE_CONNECTED) 
					{
						if(time!=null)
						mChatService.write(time.getBytes());
						if(gy!=null)
						mChatService.write(gy.getBytes());
					}
				
			}
		}
	}

	public void setBluetooth(View v) {
		
		startActivityForResult(new Intent(this, setBluetoothActivity.class), SET_BLUETOOTH);
	}
	public void setParameter(View v)
	{
		startActivityForResult(new Intent(this, setParameterActivity.class), SET_PARAMETER);
	}
}
