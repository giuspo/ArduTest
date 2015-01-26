/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.ActorRef;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.Tailer;
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
				try
				{
					LocalDateTime tLodDate = LocalDateTime.parse(strDate,
						DateTimeFormatter.ofPattern("u-M-d H:m:s.S"));
				}
				catch(Exception tExc)
				{
					String strExc = tExc.getMessage();
				}
			}
		}
		
		super.handle(strLine);
	}
}
