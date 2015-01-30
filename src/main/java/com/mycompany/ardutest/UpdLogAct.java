/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.UntypedActor;
import javafx.application.Platform;
import javafx.collections.ObservableList;

/**
 *
 * @author giulio
 */
public class UpdLogAct extends UntypedActor
{
	private final ObservableList<LogDataModel> _rgtObsLogData;
	
	public UpdLogAct(ObservableList<LogDataModel> rgtObsLogData)
	{
		_rgtObsLogData = rgtObsLogData;
	}
	
	@Override
	public void preStart() throws Exception
	{
		getContext().system().eventStream().subscribe(getSelf(),
			LogDataMsg.class);
		
		super.preStart();
	}
	
	@Override
	public void onReceive(Object objMsg) throws Exception
	{
		if(objMsg instanceof LogDataMsg)
		{
			LogDataMsg tLogData = (LogDataMsg)objMsg;
		
			Platform.runLater(()->
			{
				_rgtObsLogData.add(new LogDataModel(tLogData.gettDateTime(),
					tLogData.getLevel(),
					tLogData.getMsg1(),
					tLogData.getMsg2()));
			});
		}
	}
}
