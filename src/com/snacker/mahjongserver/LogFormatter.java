package com.snacker.mahjongserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		
		buf.append("[");
		buf.append(calcDate(record.getMillis()));
		buf.append("] ");
		
		buf.append(record.getMessage());
		buf.append("\n");
		
		return buf.toString();
	}
	
	private String calcDate(long millisecs) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
		Date resultdate = new Date(millisecs);
		return format.format(resultdate);
	}

}
