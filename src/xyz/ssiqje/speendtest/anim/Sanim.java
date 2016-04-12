package xyz.ssiqje.speendtest.anim;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;

public class  Sanim 
{
	static AnimationSet animation=new AnimationSet(false);
	static RotateAnimation rotate;
	
	public static  Animation alpha(int form,int to,int time,int count)
	{
		AlphaAnimation allpha=new AlphaAnimation(form, to);
		allpha.setDuration(time);
		allpha.setRepeatCount(count);
		animation.addAnimation(allpha);
		return animation;
		
		
	}
	public static Animation rotate(int form,int to) {
		rotate=new RotateAnimation(form, to,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(3000);
		animation.addAnimation(rotate);
		animation.setFillAfter(false);
		return animation;
	}
	
}
