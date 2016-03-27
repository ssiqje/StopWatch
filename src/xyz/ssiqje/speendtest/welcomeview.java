package xyz.ssiqje.speendtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class welcomeview extends SurfaceView implements SurfaceHolder.Callback2,Runnable{
	private Bitmap welcometu;
	private SurfaceHolder holder;
	private int displayW,displayH;
	private Paint paint;
	private Handler handler;
	//private BluetoothChat bluetoothChat;
	
	public welcomeview(Context context,Handler handle,int displayW,int displayH) {
		super(context);
		this.handler=handle;
		this.displayW=displayW;
		this.displayH=displayH;
		paint=new Paint();
		this.getHolder().addCallback(this);
		init();
	}

	
	private void init() {
		// TODO Auto-generated method stub
		//bluetoothChat=new BluetoothChat();
		welcometu=BitmapFactory.decodeResource(getResources(), R.drawable.welcome);
		holder=getHolder();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		new Thread(this).start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceRedrawNeeded(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas=holder.lockCanvas();
		canvas.drawBitmap(welcometu, new Rect(0, 0, welcometu.getWidth(), welcometu.getHeight()), new Rect(0, 0, displayW, displayH), paint);
		holder.unlockCanvasAndPost(canvas);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.sendEmptyMessage(123);
		
	}
	
	

	
}
