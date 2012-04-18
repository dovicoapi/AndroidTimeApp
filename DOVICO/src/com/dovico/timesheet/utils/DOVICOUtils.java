package com.dovico.timesheet.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class DOVICOUtils {
	
	public static CharSequence addImageToText(Context context, CharSequence text, int resID, int start, int end) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		
		Drawable d = context.getResources().getDrawable(resID); 
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight()); 
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE); 
        builder.setSpan(span, text.length(), 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
			
		return builder;
	}

}
