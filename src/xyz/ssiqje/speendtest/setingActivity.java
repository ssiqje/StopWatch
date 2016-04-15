package xyz.ssiqje.speendtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class setingActivity extends Activity
{
	EditText hourEditText,minEditText,gytEditText,bjEditText;
	String  time,gy;
	TextWatcher twatcherH;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seting);
		hourEditText=(EditText) findViewById(R.id.hourEt);
		minEditText=(EditText) findViewById(R.id.minEt);
		gytEditText=(EditText) findViewById(R.id.gytEt);
		bjEditText=(EditText) findViewById(R.id.bjEt);
	}
	public void seting(View v)
	{
			String h=hourEditText.getText().toString();
			String m=minEditText.getText().toString();
			String gyt=gytEditText.getText().toString();
			String bj=bjEditText.getText().toString();
			if(h.equals("")||m.equals("")||gyt.equals("")||bj.equals(""))
			{
				Toast.makeText(this, "ÄÚÈÝ²»ÄÜ¿Õ", Toast.LENGTH_LONG).show();
				return;
			}
			else {
				time="("+1+"="+h+m+")";
				gy="("+2+"="+gyt+bj+")";
			}
			Intent intent=new Intent();
			intent.putExtra("time", time);
			intent.putExtra("gy", gy);
			this.setResult(RESULT_OK, intent);
			this.finish();
		
		
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
