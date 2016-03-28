package xyz.ssiqje.speendtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class gameview extends SurfaceView implements SurfaceHolder.Callback2,Runnable{
	private Bitmap speendtu;
	private Boolean flag;
	private SurfaceHolder holder;
	private int displayW,displayH;
	private int speendNum=0;
	//private BluetoothChat bluetoothChat;
	
	public gameview(Context context,int displayW,int displayH) {
		super(context);
		this.displayW=displayW;
		this.displayH=displayH;
		this.getHolder().addCallback(this);
		init();
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public void setSpeendNum(int i)
	{
		this.speendNum=i;
	}
	private void init() {
		// TODO Auto-generated method stub
		//bluetoothChat=new BluetoothChat();
		speendtu=BitmapFactory.decodeResource(getResources(), R.drawable.speendtu);
		flag=false;
		holder=getHolder();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		flag=true;
		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		flag=false;
	}

	@Override
	public void surfaceRedrawNeeded(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Paint darwpaintPaint=new Paint();
		Paint stringpaint=new Paint();
		stringpaint.setColor(Color.RED);
		stringpaint.setTextSize(60);
		int i=100;
		try {
			while(flag)
			{
				Thread.sleep(50);
				Canvas canvas=holder.lockCanvas();
				if(canvas!=null)
				{
					canvas.drawBitmap(speendtu, new Rect(0, 0, speendtu.getWidth(), speendtu.getHeight()), new Rect(0, 0, (int)(displayW*0.8), displayH), darwpaintPaint);
					canvas.drawRect(0, 0, displayW, (float)displayH/100*(i-speendNum), darwpaintPaint);
					canvas.drawText(""+speendNum, (float)(displayW*0.8),(float)(displayH/2), stringpaint);
					Log.i("info", displayW+"~"+(float)(displayW*0.8)+"~"+displayH+"~"+(float)(displayH/2)+"----->"+speendNum);
				}
				else {
					{
						Log.i("info", "»­canvasÎª¿Õ£¡");
					}
				}
				if(canvas!=null)
				holder.unlockCanvasAndPost(canvas);
				else {
					Log.i("info", "ºÃcanvasÎª¿Õ£¡");
				}
				
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
