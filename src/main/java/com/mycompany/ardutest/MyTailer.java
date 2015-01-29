/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.ActorRef;
import akka.actor.dsl.Inbox;
import static akka.actor.dsl.Inbox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.TailerListenerAdapter;

/**
 *
 * @author giulio
 */
public class MyTailer extends TailerListenerAdapter
{
	private final ActorRef _tTailerAct;
	
	public MyTailer(ActorRef tAct)
	{
		super();
		_tTailerAct = tAct;
	}
	
	@Override
	public void handle(String strLine)
	{
		if("" != strLine)
		{
			Pattern tPat = Pattern.compile("\\[(.+?)\\];");
			Matcher tMatch = tPat.matcher(strLine);
			String strDate = "";
			String strLevel = "";
			String strMsg1 = "";
			String strMsg2 = "";
			String strGroup;

			for(;tMatch.find();)
			{
				strGroup = tMatch.group(1);
				if(strGroup.matches("(\\d{4})-(\\d{2})-(\\d{2})\\s+(\\d{2}):(\\d{2}):(\\d{2}),(\\d+)"))
				{
					strDate = strGroup;
				}
				else if(strGroup.matches("(FATAL|ERROR|WARN|INFO|DEBUG|TRACE)"))
				{
					strLevel = strGroup;
				}
				else if("" != strMsg1)
				{
					strMsg2 = strGroup;
				}
				else
				{
					strMsg1 = strGroup;
				}
			}

			if("" != strDate)
			{
				LocalDateTime tLocalTime = null;
				
				try
				{
					tLocalTime = LocalDateTime.parse(strDate,
						DateTimeFormatter.ofPattern("u-M-d H:m:s,SSS"));
				}
				catch(DateTimeParseException tExc)
				{
					tLocalTime = LocalDateTime.now();
					strMsg1 += " (DateTime Parse Exception!)";
				}
				
//				_tTailerAct.tell(new LogDataModel(tLocalTime,
//					strLevel,
//					strMsg1,
//					strMsg2),
//					null);
				
				Inbox tInbox = new Inbox.Inbox(MainApp.getActSys());
				
				tInbox.inbox(null)
			}
		}
		
		super.handle(strLine);
	}
}
