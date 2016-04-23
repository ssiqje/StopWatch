package xyz.ssiqje.speendtest;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class setParameterActivity extends Activity
{
	EditText hourEditText,minEditText,gytEditText,bjEditText;
	String  time=null;
	String gy=null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.set_parameter);
		hourEditText=(EditText) findViewById(R.id.hourEt);
		minEditText=(EditText) findViewById(R.id.minEt);
		Calendar calendar=Calendar.getInstance();
		hourEditText.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		minEditText.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
		gytEditText=(EditText) findViewById(R.id.gytEt);
		bjEditText=(EditText) findViewById(R.id.bjEt);
		
	}
	

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
			intent.putExtra("time", time);
			intent.putExtra("gy", gy);
			this.setResult(RESULT_OK, intent);
			this.finish();
		
		
	}

}
