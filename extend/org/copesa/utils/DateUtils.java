package org.copesa.utils;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {
	public Timestamp today()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);						
		Timestamp instant = new Timestamp(calendar.getTimeInMillis());
		return instant;

	}

	public Timestamp now()
	{
		Calendar calendar = Calendar.getInstance();
		Timestamp instant = new Timestamp(calendar.getTimeInMillis());
		return instant;
	}

	public Timestamp nextDay( Timestamp _day )
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(_day.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);						
		calendar.add(Calendar.DATE, 1);							
		return new Timestamp(calendar.getTimeInMillis());
	}
}
