package com.dovico.timesheet.widgets;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.dovico.timesheet.utils.Logger;

public class DOVICOSpinner extends Spinner {

    private static final String TAG = "DOVICOSpinner";
	private static Method s_pSelectionChangedMethod = null;


    static {        
        try {
            Class noparams[] = {};
            Class targetClass = AdapterView.class;

            s_pSelectionChangedMethod = targetClass.getDeclaredMethod("selectionChanged", noparams);            
            if (s_pSelectionChangedMethod != null) {
                s_pSelectionChangedMethod.setAccessible(true);              
            }
            

        } catch( Exception e ) {
        	 Logger.e(TAG, "Exception: " + e);   
            throw new RuntimeException(e);
        }
    }

    public DOVICOSpinner(Context context) {
        super(context);
    }
    
    @Deprecated
    public void setNextSelection() {
    	try {
            final Method m = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt",int.class);
            m.setAccessible(true);
            m.invoke(this,-1);

            final Method n = AdapterView.class.getDeclaredMethod("setSelectedPositionInt",int.class);
            n.setAccessible(true);
            n.invoke(this,-1);

        } catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }

    public DOVICOSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DOVICOSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void testReflectionForSelectionChanged() {
        try {
            Class noparams[] = {};          
            s_pSelectionChangedMethod.invoke(this, noparams);
        } catch (Exception e) {
            Logger.e(TAG, "Exception: " + e);             
        }
    } 


    private Object ob=null; //class level variable
    @Override
   public boolean onTouchEvent(MotionEvent m)
   {
       if (m.getAction()==MotionEvent.ACTION_DOWN)
       {
           ob=this.getSelectedItem();
       }
       return super.onTouchEvent(m);
   }
   @Override
   public void onClick(DialogInterface diaLogger, int which) {    
       super.onClick(diaLogger, which);
       if (this.getSelectedItem().equals(ob))
           testReflectionForSelectionChanged();
   }
}