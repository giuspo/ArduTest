/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.UntypedActor;
import java.io.File;
import org.apache.commons.io.input.Tailer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;

/**
 *
 * @author giulio
 */
public class TailerAct extends UntypedActor
{
	private Tailer _tTailer;
	
	@Override
	public void preStart() throws Exception
	{
		
		LoggerContext tLogCtx = (LoggerContext)LogManager.getContext(true);
		RollingFileAppender tFileAppender = (RollingFileAppender)tLogCtx.getConfiguration().getAppender("file");
		
		String strLogFile = tFileAppender.getFileName();
		File tFile = new File(strLogFile);
		
		boolean bExist = tFile.exists();
		_tTailer = Tailer.create(tFile,
			new MyTailer(getSelf()),
			100,
			false,
			true);
		
		super.preStart();
	}

	@Override
	public void postStop() throws Exception
	{
		_tTailer.stop();
		
		super.postStop();
	}

	@Override
	public void onReceive(Object objMsg) throws Exception
	{
		if(objMsg instanceof LogDataMsg)
		{
			getContext().system().eventStream().publish(objMsg);
		}
	}
}
