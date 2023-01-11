package com.slackapp.main.util;

import java.sql.Date;

public class DateUtil {
	/*
	 *  convertime method use for convert timestamp to util date 
	 *  
	 */

	public String convertTime(Double string)
	{
		java.util.Date dateWithTime = new java.util.Date( (long) (string * 1000));
		return dateWithTime.getHours()+" "+dateWithTime.getMinutes()+" "+dateWithTime.getSeconds();
	}

	/*
	 * 
	 *   converTimeStampToDate method use for convert timestamp to sql date
	 * 
	 */


	public Date converTimeStampToDate(Double timeStamp) {
		Date dayAndDate = new Date(  (long) (timeStamp * 1000));
		System.out.println(dayAndDate);
		return dayAndDate;
	}



}
