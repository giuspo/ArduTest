/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.UntypedActor;

/**
 *
 * @author giulio
 */
public class SockRecvAct extends UntypedActor
{
	@Override
	public void preStart() throws Exception
	{	
		super.preStart();
		
		getContext().system().eventStream().subscribe(getSelf(),
			SockRecvMsg.class);
	}
	
	@Override
	public void onReceive(Object objMsg) throws Exception
	{
		if(objMsg instanceof SockRecvMsg)
		{
			SockRecvMsg tMsg = (SockRecvMsg)objMsg;
			
			MainApp.getLogger().info("Received from sock: " +
				tMsg.getMsg());
		}
	}
}
