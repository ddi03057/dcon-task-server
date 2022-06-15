package kr.co.dcon.taskserver.common.util;

import kr.co.dcon.taskserver.common.constants.CommonConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtils {

	private DateUtils(){}

	private static final String FORMAT_STR = "yyyy/MM";

	public static String getLastMonthFirstDay() {
		return getLastMonth() + "/01";
	}

	public static String getLastMonthLastDay() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_STR+"/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getLastMonthDate());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH,0);
		Date date = calendar.getTime();
		return formater.format(date);
	}


	public static String getLastMonth() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_STR);
		Date date = getLastMonthDate();
		return formater.format(date);
	}

	public static String getThisMonth() {
		SimpleDateFormat formater = new SimpleDateFormat(FORMAT_STR);
		Date date = new Date();
		return formater.format(date);
	}

	public static Date getLastMonthDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		date = calendar.getTime();
		return date;
	}
	public static String getCurrentTimeGmt() {
		TimeZone krTime = TimeZone.getTimeZone ("Asia/Seoul");
		Calendar cal = Calendar.getInstance ( krTime );
		StringBuilder str = new StringBuilder();
		str.append(cal.get ( Calendar.YEAR ));
		str.append("년 ");
		str.append(cal.get ( Calendar.MONTH ) + 1 );
		str.append("월 ");
		str.append(cal.get ( Calendar.DATE) );
		str.append("일 ");
		str.append(cal.get ( Calendar.HOUR_OF_DAY) );
		str.append("시 ");
		str.append(cal.get ( Calendar.MINUTE) );
		str.append("분 ");
		str.append(cal.get ( Calendar.SECOND) );
		str.append("초");


		return str.toString();
	}

	public static String getDateStrFronTimeStamp(String timestamp) {
		try {
			long loginTimestamp = Long.parseLong(timestamp);
			Calendar ca = Calendar.getInstance();
			ca.setTime(new Date(loginTimestamp));
			SimpleDateFormat commonFormat = new SimpleDateFormat(CommonConstants.COMMON_DATE_TIME_FORMAT);
			timestamp = commonFormat.format(ca.getTime());
		} catch (Exception e) {
			timestamp = "";
		}
		return timestamp;
	}
}
