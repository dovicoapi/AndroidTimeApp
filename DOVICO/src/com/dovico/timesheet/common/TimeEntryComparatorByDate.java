package com.dovico.timesheet.common;

import java.util.Comparator;
import java.util.Date;

import com.dovico.timesheet.beans.TimeEntry;

public class TimeEntryComparatorByDate implements Comparator<TimeEntry> {

	@Override
	public int compare(TimeEntry object1, TimeEntry object2) {
		String date1String = object1.getDate();
		String date2String = object2.getDate();
		
		int day = Integer.parseInt(date1String.substring(8));
		int month = Integer.parseInt(date1String.substring(5, 7)) - 1;
		int year = Integer.parseInt(date1String.substring(0, 4)) - 1900;
		
		
		Date date1 = new Date(year, month, day);

		day = Integer.parseInt(date2String.substring(8));
		month = Integer.parseInt(date2String.substring(5, 7)) - 1;
		year = Integer.parseInt(date2String.substring(0, 4)) - 1900;
		
		Date date2 = new Date(year, month, day);
		
		if (date1.before(date2)) {
			return 1;
		} else if (date1.after(date2)) {
			return -1;
		} else {
			return 0;
		}
	}

}