package cn.myapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class XtDate {
	
	public static Date getCurrentDayOriginalDate() {
		Calendar calendar = Calendar.getInstance() ;
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.MINUTE, 0); 
		calendar.set(Calendar.SECOND, 0); 
		Date today = calendar.getTime();
		return today ;
	}
			
	 /**
     * 当月第一天
     * @return
     */
    public static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        
        return day_first ;
    }
    
    /**
     * 当月最后一天
     * @return
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        
        int MaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, MaxDay);                
        
        String s = df.format(gcLast.getTime());
        
        return s ;
    }
}
