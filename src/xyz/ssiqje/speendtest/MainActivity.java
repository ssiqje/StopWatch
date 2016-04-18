package xyz.ssiqje.speendtest;

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
import android.content.Intent;
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
	private static final int SETING = 3;
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
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer

				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.startsWith("(") && readMessage.endsWith(")")) {
					Log.i("info", "接收到一个正确的数据" + "~" + readMessage);
					readMessage = new String(readBuf, 1, (msg.arg1 - 2));
					Log.i("info", "去头尾后的数据" + "~" + readMessage);
				} else {
					Log.i("info", "接收到一个错误的数据");
					break;
				}
				String[] data = readMessage.split("=");
				for (String string : data) {
					Log.i("info", "~" + string + "~");
				}
				if (sdppoint != null) {
					AnimationSet animation = new AnimationSet(true);
					RotateAnimation rotate = new RotateAnimation(
							sdpcurrentangle,
							(Float.parseFloat(data[0])) / 5 * 19,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					rotate.setDuration(1000);
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
					rotate.setDuration(1000);
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
				lightlivilimg.setImageResource(data[8].equals("0")?R.drawable.liang0:data[8].equals("1")?R.drawable.liang1:
											   data[8].equals("2")?R.drawable.liang2:R.drawable.liang3);

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


	private void initGameview() {
		// TODO Auto-generated method stub
		// gameview=new gameview(this, width,height);
		this.setContentView(R.layout.gameview);
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
		lightlivilimgList=new int []{R.drawable.liang0,
									 R.drawable.liang1,
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
		case SETING:
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
				// Get the device MAC address
				String address = data.getExtras().getString(
						"device");
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
			}
		}
	}

	public void seting(View v) {
		
		startActivityForResult(new Intent(this, setingActivity.class), SETING);
	}
}
