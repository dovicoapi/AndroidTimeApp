package com.dovico.timesheet.widgets;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dovico.timesheet.ApplicationDOVICO;
import com.dovico.timesheet.R;
import com.dovico.timesheet.utils.Logger;

public class CalendarView extends LinearLayout{

	public static final String TAG = "CalendarView";

	public static final String TAG_CORNER_TEXT = "tag_corner_text";

	public static final int TOP = 0x01;
	public static final int BOTTOM = 0x02;
	public static final int LEFT = 0x03;
	public static final int RIGHT = 0x04;
	public static final int TOP_LEFT = 0x05;
	public static final int TOP_RIGHT = 0x06;
	public static final int BOTTOM_LEFT = 0x07;
	public static final int BOTTOM_RIGHT = 0x08;
	public static final int CENTER = 0x09;
	public static final int CORNER_TEXT = 0x0A;

	private static final int ID_DAY_OF_MONTH = 0;
	private static final int COLOR_WHITE = 0xFFFFFFFF;
	private static final int COLOR_DARK_GRAY_BG = 0xFFC5C5C5;
	private static final int COLOR_LIGHT_GRAY_BG = 0xFFDEDEDE;
	private static final int COLOR_DARK_GREY_TEXT = 0xFF555555;
	private static final int COLOR_LIGHT_GREY_TEXT = 0xFF888888;
	private static final int COLOR_GREEN_BG = 0xFF87AB57;
	private static final int COLOR_BLUE_BG = 0xFF6B93CD;
	

	private int initDay;
	private int initMonth;
	private int initYear;
	protected int daysInCurrentMonth;
	protected int currentDay;
	protected int currentMonth;
	protected int currentYear;
	private int firstDayOfMonth;
	private int rowsInCalendar;
	private float scale;

	private Context context;
	private TextView monthYear;
	private LinearLayout calendarContent;
	private ArrayList<RelativeLayout> cells;
	private int previousCell;

	private OnDateSelectedListener dateSelectedListener;
	private OnMonthChangedListener monthChangedListener;

	private CalendarView(Context context) {
		super(context);
	}

	/*
	 * This constructor is called, when view is inflated from layout
	 */
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOrientation(VERTICAL);
		setBackgroundColor(COLOR_DARK_GRAY_BG);

		DisplayMetrics dm = new DisplayMetrics();
		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); 
		window.getDefaultDisplay().getMetrics(dm);
		scale = dm.density;

		LinearLayout topBar = new LinearLayout(context);
		{
			topBar.setOrientation(VERTICAL);

			LinearLayout navBar = new LinearLayout(context);
			{
				navBar.setGravity(Gravity.CENTER);

				ImageView arrowLeft = new ImageView(context);
				arrowLeft.setImageResource(R.drawable.cal_previous);
				LinearLayout.LayoutParams arrowLeftParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 1.0);
				arrowLeft.setLayoutParams(arrowLeftParams);
				arrowLeft.setPadding(0, (int) (10*scale), 0, (int) (10*scale));
				arrowLeft.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						prevMonth();
					}
				});

				monthYear = new TextView(context);
				LinearLayout.LayoutParams monthYearParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 5.0);
				monthYear.setLayoutParams(monthYearParams);
				monthYear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				monthYear.setTypeface(Typeface.DEFAULT_BOLD);
				monthYear.setTextColor(COLOR_DARK_GREY_TEXT);
				monthYear.setShadowLayer(1, 1, 1, COLOR_WHITE);
				monthYear.setGravity(Gravity.CENTER);

				ImageView arrowRight = new ImageView(context);
				arrowRight.setImageResource(R.drawable.cal_next);
				LinearLayout.LayoutParams arrowRightParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
				arrowRightParams.weight = 1;
				arrowRight.setLayoutParams(arrowRightParams);
				arrowRight.setPadding(0, (int) (10*scale), 0, (int) (10*scale));
				arrowRight.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						nextMonth();
					}
				});

				navBar.addView(arrowLeft);
				navBar.addView(monthYear);
				navBar.addView(arrowRight);
			}

			LinearLayout daysOfWeek = new LinearLayout(context);
			{
				for (int i=0; i<7; i++) {
					TextView day = new TextView(context);
					LinearLayout.LayoutParams sunParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 1.0);
					day.setLayoutParams(sunParams);
					day.setGravity(Gravity.CENTER);
					day.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
					day.setTextColor(COLOR_DARK_GREY_TEXT);
					day.setText(getDay(i));
					daysOfWeek.addView(day);
				}
			}

			topBar.addView(navBar);
			topBar.addView(daysOfWeek);
		}

		calendarContent = new LinearLayout(context);
		calendarContent.setOrientation(VERTICAL);

		addView(topBar);
		addView(calendarContent);

		Calendar today = Calendar.getInstance();
		initDay = today.get(Calendar.DAY_OF_MONTH);
		initMonth = today.get(Calendar.MONTH);
		initYear = today.get(Calendar.YEAR);
		//Maybe disable this...
		draw(today);
	}

	public void prevMonth() {
		Calendar prevMonth = Calendar.getInstance();
		prevMonth.set(currentYear, currentMonth, currentDay);
		prevMonth.add(Calendar.MONTH, -1);
		if (monthChangedListener != null) {
			monthChangedListener.onMonthChanged(prevMonth.get(Calendar.MONTH));
		}
		draw(prevMonth);
	}

	public void nextMonth() {
		Calendar nextMonth = Calendar.getInstance();
		nextMonth.set(currentYear, currentMonth, currentDay);
		nextMonth.add(Calendar.MONTH, 1);
		if (monthChangedListener != null) {
			monthChangedListener.onMonthChanged(nextMonth.get(Calendar.MONTH));
		}
		draw(nextMonth);
	}

	private void draw(Calendar today) {
		calendarContent.removeAllViews();
		StringBuilder sb = new StringBuilder();

		currentDay = today.get(Calendar.DAY_OF_MONTH);
		currentMonth = today.get(Calendar.MONTH);
		currentYear = today.get(Calendar.YEAR);
		sb.append(getMonth(currentMonth));
		sb.append(' ');
		sb.append(currentYear);
		monthYear.setText(sb.toString());

		//Get number of days in current month
		//The idea is as follows:
		today.set(Calendar.DAY_OF_MONTH, 1); //Set the date to first day of the month
		firstDayOfMonth = today.get(Calendar.DAY_OF_WEEK) - 1; //(ignore this, we will need this later)
		today.add(Calendar.MONTH, 1); //Add 1 month
		today.add(Calendar.DAY_OF_MONTH, -1); //Go back 1 day, so the current day is the last day of current (now previous) month
		daysInCurrentMonth = today.get(Calendar.DAY_OF_MONTH);

		//This is only valid if the week starts on Sunday!
		rowsInCalendar = (int) Math.ceil((daysInCurrentMonth + firstDayOfMonth) * 1.0 / 7); // -1 because days of month go from 1 to 7 (instead od 0 to 6)

		cells = new ArrayList<RelativeLayout>();

		for (int i=0; i<rowsInCalendar; i++) {
			LinearLayout row = new LinearLayout(context);
			LayoutParams rowParams = new LayoutParams(LayoutParams.FILL_PARENT, (int) (40*scale));
			rowParams.setMargins(0, 1, 0, 1);
			row.setLayoutParams(rowParams);

			for (int j=0; j<7; j++) {
				RelativeLayout cell = new RelativeLayout(context);
				LayoutParams cellParams = new LayoutParams(0, LayoutParams.FILL_PARENT, (float) 1.0);
				cellParams.setMargins(1, 0, 1, 0);
				cell.setBackgroundColor(COLOR_LIGHT_GRAY_BG);
				cell.setLayoutParams(cellParams);
				cell.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						Object tag = v.getTag();
						if (tag != null) {
							onCellClick((Integer) tag);
						}	
					}
				});

				TextView day = new TextView(context);
				RelativeLayout.LayoutParams dayParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				day.setLayoutParams(dayParams);
				day.setId(ID_DAY_OF_MONTH);
				day.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				day.setTypeface(Typeface.DEFAULT_BOLD);
				day.setShadowLayer(1, 1, 1, COLOR_WHITE);
				day.setTextColor(COLOR_DARK_GREY_TEXT);
				day.setGravity(Gravity.CENTER);
				cell.addView(day);

				View topLine = new View(context);
				topLine.setBackgroundColor(COLOR_WHITE);
				android.widget.RelativeLayout.LayoutParams topLineParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.FILL_PARENT, 1);
				topLineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				topLine.setLayoutParams(topLineParams);
				cell.addView(topLine);

				View rightLine = new View(context);
				rightLine.setBackgroundColor(COLOR_WHITE);
				android.widget.RelativeLayout.LayoutParams rightLineParams = new RelativeLayout.LayoutParams(1, android.widget.RelativeLayout.LayoutParams.FILL_PARENT);
				rightLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rightLine.setLayoutParams(rightLineParams);
				cell.addView(rightLine);

				row.addView(cell);
				//Do not add empty cells to the list, so the cells.get(0) corresponds to first day of the month
				if ((i*7+j) >= firstDayOfMonth) {
					cells.add(cell);
				}
			}

			calendarContent.addView(row);
		}

		for (int i=0; i<daysInCurrentMonth; i++) {
			RelativeLayout cell = cells.get(i);
			((TextView) cell.getChildAt(0)).setText(Integer.toString(i+1));
			cell.setTag(i);
		}
		init();

		//Highlight today
		if ((currentYear == initYear) && (currentMonth == initMonth)) {
			cells.get(initDay-1).setBackgroundColor(COLOR_DARK_GRAY_BG);
		}
	}

	private void onCellClick(int whichCell) {
		if (whichCell < cells.size()) {
			//if (previousCell <= cells.size()) {   ???????? <=
			if (previousCell < cells.size()) {
				if ((currentYear == initYear) && (currentMonth == initMonth) && (previousCell == (initDay-1))) {
					cells.get(previousCell).setBackgroundColor(COLOR_DARK_GRAY_BG);
				} else {
					cells.get(previousCell).setBackgroundColor(COLOR_LIGHT_GRAY_BG);
				}
			}
			previousCell = whichCell;
			RelativeLayout selectedCell = cells.get(whichCell);
			selectedCell.setBackgroundColor(COLOR_BLUE_BG);
			if (dateSelectedListener != null) {
				StringBuilder sb = new StringBuilder(8); //8 is the necessary capacity to hold yyyymmdd format date
				sb.append(currentYear).append("-").append(String.format("%02d", currentMonth+1)).append("-").append(String.format("%02d", whichCell+1));
				Logger.d(TAG, "Selected date: " + sb.toString());
				dateSelectedListener.onDateSelected(selectedCell, sb.toString());
			}
		}
	}

	private RelativeLayout.LayoutParams getLayoutParams(int position) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		switch (position) {
		case TOP:
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.setMargins(0, 1, 0, 0);
			break;
		case BOTTOM:
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
		case LEFT:
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			break;
		case RIGHT:
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.setMargins(0, 0, 1, 0);
			break;
		case TOP_LEFT:
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.setMargins(0, 1, 0, 0);
			break;
		case TOP_RIGHT:
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(0, 1, 1, 0);
			break;
		case BOTTOM_LEFT:
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.setMargins((int) (3*scale), 0, 0, (int) (3*scale));
			break;
		case BOTTOM_RIGHT:
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(0, 0, 1, 0);
			break;
		case CENTER:
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			break;
		case CORNER_TEXT:
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.setMargins((int) (2*scale), (int) (-1*scale), 0, 0);
			break;
		default:
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			break;
		}
		return params;
	}

	public static String getMonth(int month) {
		String result;
		switch (month) {
		case 0:
			result = ApplicationDOVICO.getInstance().getString(R.string.january);
			break;
		case 1:
			result = ApplicationDOVICO.getInstance().getString(R.string.february);
			break;
		case 2:
			result = ApplicationDOVICO.getInstance().getString(R.string.march);
			break;
		case 3:
			result = ApplicationDOVICO.getInstance().getString(R.string.april);
			break;
		case 4:
			result = ApplicationDOVICO.getInstance().getString(R.string.may);
			break;
		case 5:
			result = ApplicationDOVICO.getInstance().getString(R.string.june);
			break;
		case 6:
			result = ApplicationDOVICO.getInstance().getString(R.string.july);
			break;
		case 7:
			result = ApplicationDOVICO.getInstance().getString(R.string.august);
			break;
		case 8:
			result = ApplicationDOVICO.getInstance().getString(R.string.september);
			break;
		case 9:
			result = ApplicationDOVICO.getInstance().getString(R.string.october);
			break;
		case 10:
			result = ApplicationDOVICO.getInstance().getString(R.string.november);
			break;
		case 11:
			result = ApplicationDOVICO.getInstance().getString(R.string.december);
			break;
		default:
			result = ApplicationDOVICO.getInstance().getString(R.string.january);
			break;
		}
		return result;
	}

	public static String getShortMonth(int month) {
		String result;
		switch (month) {
		case 0:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_january);
			break;
		case 1:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_february);
			break;
		case 2:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_march);
			break;
		case 3:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_april);
			break;
		case 4:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_may);
			break;
		case 5:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_june);
			break;
		case 6:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_july);
			break;
		case 7:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_august);
			break;
		case 8:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_september);
			break;
		case 9:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_october);
			break;
		case 10:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_november);
			break;
		case 11:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_december);
			break;
		default:
			result = ApplicationDOVICO.getInstance().getString(R.string.short_january);
			break;
		}
		return result;
	}

	protected static String getDay(int day) {
		String result;
		switch (day) {
		case 0:
			result = ApplicationDOVICO.getInstance().getString(R.string.sunday_short);
			break;
		case 1:
			result = ApplicationDOVICO.getInstance().getString(R.string.monday_short);
			break;
		case 2:
			result = ApplicationDOVICO.getInstance().getString(R.string.tuesday_short);
			break;
		case 3:
			result = ApplicationDOVICO.getInstance().getString(R.string.wednesday_short);
			break;
		case 4:
			result = ApplicationDOVICO.getInstance().getString(R.string.thursday_short);
			break;
		case 5:
			result = ApplicationDOVICO.getInstance().getString(R.string.friday_short);
			break;
		case 6:
			result = ApplicationDOVICO.getInstance().getString(R.string.saturday_short);
			break;
		default:
			result = ApplicationDOVICO.getInstance().getString(R.string.sunday_short);
			break;
		}
		return result;
	}

	public static Calendar getCalendarFromYyyymmdd(int yyyymmdd) {
		//Year is extracted by dividing yyyymmdd by 10000 (to truncate the last 4 digits)
		int year = yyyymmdd / 10000;
		//Then, year is multiplied by 10000 to produce yyyy0000
		//mmdd = yyyymmdd - yyyy0000
		yyyymmdd -= year * 10000;
		//Month is extracted by dividing mmdd by 100 (to truncate the last 2 digits)
		int month = yyyymmdd / 100;
		//Month is then multiplied by 100 to produce mm00
		//dd = mmdd - mm00
		yyyymmdd -= month * 100;
		//Day is left in yyyymmdd variable
		int day = yyyymmdd;

		//Construct the Calendar instance
		Calendar result = Calendar.getInstance();
		//result.setTimeInMillis(0);
		result.set(Calendar.YEAR, year);
		//Subtract 1 to match the android's date format (months are represented as integers in range 0 to 11)
		result.set(Calendar.MONTH, month-1);
		result.set(Calendar.DAY_OF_MONTH, day);
		return result;
	}

	public static int getYyyymmddFromCalendar(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	public static int getDifferenceInDays(Calendar c1, Calendar c2) {
		return (int) Math.round((c1.getTimeInMillis() - c2.getTimeInMillis()) / 1000 * 1.0 / 60 / 60 / 24);
	}

	public static String getDateString(int yyyymmdd, boolean showYear) {
		Calendar date = getCalendarFromYyyymmdd(yyyymmdd);
		return getDateString(date, showYear);
	}

	public static String getDateString(Calendar date, boolean showYear) {
		StringBuilder sb = new StringBuilder();
		sb.append(getShortMonth(date.get(Calendar.MONTH)));
		sb.append(' ');
		sb.append(date.get(Calendar.DAY_OF_MONTH));
		if (showYear) {
			sb.append(", ");
			sb.append(date.get(Calendar.YEAR));
		}
		return sb.toString();
	}

	public interface OnDateSelectedListener {
		public void onDateSelected(RelativeLayout cell, String yyyymmdd);
	}

	public void setOnDateSelectedListener(OnDateSelectedListener listener) {
		this.dateSelectedListener = listener;
	}

	public interface OnMonthChangedListener {
		/**
		 * @param newMonth
		 * number indicating new month (January = 0; December = 11)
		 */
		public void onMonthChanged(int newMonth);
	}

	public void setOnMonthChanged(OnMonthChangedListener listener) {
		this.monthChangedListener = listener;
	}

	protected void init() {}

	public void goToDate(Calendar calendar, boolean select) {
		int dayToSelect = calendar.get(Calendar.DAY_OF_MONTH);
		draw(calendar);
		if (select) {
			setSelectedDay(dayToSelect);
		}
	}

	public void setSelectedDay(int day) {
		onCellClick(day-1);
	}

	public ArrayList<RelativeLayout> getCells() {
		return cells;
	}

	/**
	 * Puts the drawables in a cell
	 * @param cell - The cell to put the drawables into
	 * @param topLeft - Resource ID of the top-left drawable
	 * @param top - Resource ID of the top drawable
	 * @param topRight - Resource ID of the top-right drawable
	 * @param right - Resource ID of the right drawable
	 * @param bottomRight - Resource ID of the bottom-right drawable
	 * @param bottom - Resource ID of the bottom drawable
	 * @param bottomLeft - Resource ID of the bottom-left drawable
	 * @param left - Resource ID of the left drawable
	 * @param center - Resource ID of the center drawable
	 */
	public void setCellDrawables(RelativeLayout cell, int topLeft, int top, int topRight, int right, int bottomRight, int bottom, int bottomLeft, int left, int center) {
		setCellDrawable(cell, TOP_LEFT, topLeft);
		setCellDrawable(cell, TOP, top);
		setCellDrawable(cell, TOP_RIGHT, topRight);
		setCellDrawable(cell, RIGHT, right);
		setCellDrawable(cell, BOTTOM_RIGHT, bottomRight);
		setCellDrawable(cell, BOTTOM, bottom);
		setCellDrawable(cell, BOTTOM_LEFT, bottomLeft);
		setCellDrawable(cell, LEFT, left);
		setCellDrawable(cell, CENTER, center);
	}

	/**
	 * Adds the drawable to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param resId - Resource ID of the drawable
	 */
	public void setCellDrawable(RelativeLayout cell, int position, int resId) {
		setCellDrawable(cell, position, resId, null);
	}

	/**
	 * Adds the drawable to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param drawable - Drawable
	 */
	public void setCellDrawable(RelativeLayout cell, int position, Drawable drawable) {
		setCellDrawable(cell, position, drawable, null);
	}

	/**
	 * Adds the bitmap to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param bitmap - Bitmap
	 */
	public void setCellDrawable(RelativeLayout cell, int position, Bitmap bitmap) {
		setCellDrawable(cell, position, bitmap, null);
	}

	/**
	 * Adds the view to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param v - View
	 */
	public void setCellDrawable(RelativeLayout cell, int position, View v) {
		setCellDrawable(cell, position, v, null);
	}

	/**
	 * Adds the drawable to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param resId - Resource ID of the drawable
	 * @param tag - Tag
	 */
	public void setCellDrawable(RelativeLayout cell, int position, int resId, Object tag) {
		ImageView image = new ImageView(context);
		image.setImageResource(resId);
		setCellDrawable(cell, position, image, tag);
	}

	/**
	 * Adds the drawable to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param drawable - Drawable
	 * @param tag - Tag
	 */
	public void setCellDrawable(RelativeLayout cell, int position, Drawable drawable, Object tag) {
		ImageView image = new ImageView(context);
		image.setImageDrawable(drawable);
		setCellDrawable(cell, position, image, tag);
	}

	/**
	 * Adds the bitmap to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param bitmap - Bitmap
	 * @param tag - Tag
	 */
	public void setCellDrawable(RelativeLayout cell, int position, Bitmap bitmap, Object tag) {
		ImageView image = new ImageView(context);
		image.setImageBitmap(bitmap);
		setCellDrawable(cell, position, image, tag);
	}

	/**
	 * Adds the view to a cell
	 * @param cell - The cell to put drawable into
	 * @param position - Position (one of the following: TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER)
	 * @param v - View
	 * @param tag - Tag
	 */
	public void setCellDrawable(RelativeLayout cell, int position, View v, Object tag) {
		RelativeLayout.LayoutParams params = getLayoutParams(position);
		v.setTag(tag);
		cell.addView(v, params);
	}

	/**
	 * Set number in the top-left corner of the cell
	 * @param cell - The cell to put the number into
	 * @param number - Number
	 * @param darkGrayColor - Color of text, true = DARK_GRAY, false = LIGHT_GRAY
	 */
	public void setCornerText(RelativeLayout cell, int number,  boolean darkGrayColor) {
		setCornerText(cell, Integer.toString(number), darkGrayColor);
	}

	/**
	 * Set text in the top-left corner of the cell
	 * @param cell - The cell to put the text into
	 * @param text - Text
	 * @param darkGrayColor - Color of text, true = DARK_GRAY, false = LIGHT_GRAY
	 */
	public void setCornerText(RelativeLayout cell, String text, boolean darkGrayColor) {
		TextView textViewCornerText = (TextView) cell.findViewWithTag(TAG_CORNER_TEXT);
		if (textViewCornerText == null) {
			textViewCornerText = new TextView(context);
			textViewCornerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
			textViewCornerText.setTypeface(Typeface.DEFAULT_BOLD);
			if (darkGrayColor)
				textViewCornerText.setTextColor(COLOR_DARK_GREY_TEXT);
			else
				textViewCornerText.setTextColor(COLOR_LIGHT_GREY_TEXT);
			textViewCornerText.setTag(TAG_CORNER_TEXT);
			RelativeLayout.LayoutParams params = getLayoutParams(CORNER_TEXT);
			cell.addView(textViewCornerText, params);
		}
		textViewCornerText.bringToFront();
		textViewCornerText.setText(text);
	}
}