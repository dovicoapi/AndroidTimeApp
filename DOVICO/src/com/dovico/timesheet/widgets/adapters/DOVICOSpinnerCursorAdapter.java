package com.dovico.timesheet.widgets.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dovico.timesheet.R;

public class DOVICOSpinnerCursorAdapter extends SimpleCursorAdapter{

	private static final String TAG = "DOVICOCursorAdapter";
	private Context context;
	
	private String cursorPrompt;
	private boolean firstTime = true;
	
	public DOVICOSpinnerCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, String cursorPrompt) {		
		
		super(context, layout, c, from, to);
		this.context = context;
		this.cursorPrompt = cursorPrompt;
	}
	

	    @Override
	    public int getCount() {
	        return super.getCount();
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        if ((firstTime && position == 0) || (position < 0)) {
	        	firstTime = false;
	            if (convertView == null) {
	                convertView = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
	                        R.layout.client_spinner_item, parent, false);
	            }

	            // Custom binding for the first item
	            TextView clientName = ((TextView) convertView.findViewById(R.id.client_name));
	            clientName.setText(cursorPrompt);
	            clientName.setTypeface(null, Typeface.NORMAL);
	            //convertView.findViewById(R.id.client_contact).setVisibility(View.GONE);

	            return convertView;
	        }
	        
	        
	        return super.getView(position, convertView, parent);
	    }
	    

}
