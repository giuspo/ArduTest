/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @author giulio
 */
public class ConfAct extends UntypedActor
{
	private Config _tConf;
	
	@Override
	public void preStart() throws Exception
	{
		super.preStart();
		
		File tAppFile = new File("App.cfg");
		
		_tConf = ConfigFactory.parseFile(tAppFile);
	}
	
	@Override
	public void onReceive(Object objMsg) throws Exception
	{
		if(objMsg instanceof ConfMsg)
		{
			ConfMsg tConfMsg = (ConfMsg)objMsg;
			
			if(ConfMsg.EType.LOAD_EVT == tConfMsg.getType())
			{
				ConfMsg tNewConf = new ConfMsg(ConfMsg.EType.LOAD_EVT,
					_tConf);
				
				getSender().tell(tNewConf, getSelf());
			}
			else if(ConfMsg.EType.SAVE_EVT == tConfMsg.getType())
			{
				ConfigRenderOptions tConfRender = ConfigRenderOptions.defaults()
					.setComments(true)
					.setOriginComments(false)
					.setJson(false);
				String strNewConf = tConfMsg.getConf().root().render(tConfRender);
				FileOutputStream tFileStream = new FileOutputStream("App.cfg");

				tFileStream.write(strNewConf.getBytes());
				
				_tConf = tConfMsg.getConf();
			}
		}
	}
}
