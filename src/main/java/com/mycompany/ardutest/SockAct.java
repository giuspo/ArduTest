/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

/**
 *
 * @author giulio
 */
public class SockAct extends UntypedActor
{
	private enum FSMStateType
	{
		INIT_STS,
		START_CONN_STS,
		WAIT_CONN_STS,
		RECV_STS,
		WAIT_RECV_STS,
		SEND_STS,
		WAIT_SEND_STS
	}

	private enum FSMEventType
	{
		NO_EVN,
		CONN_EVN,
		DISCONN_EVN,
	}

	private AsynchronousSocketChannel _tSocket;
	private Future<Void> _tFutSockConn;
	private Future<Integer> _tFutSockRead;
	private Future<Integer> _tFutSockWrite;
	private final ActorRef _tRcvAct;
	private FSMStateType _tFSMState;
	private ConnectMsg _tConnMsg;
	private SockSendMsg _tSockSendMsg;
	private SockRecvMsg _tSockRecvMsg;
	private final ByteBuffer _tReadBuff = ByteBuffer.allocate(1024);
	private final ByteBuffer _tWriteBuff = ByteBuffer.allocate(1024);
	private Integer _iNRecvData = 0;

	public SockAct(ActorRef tRcvAct)
	{
		this._tRcvAct = tRcvAct;
	}

	@Override
	public void preStart()
	{
		_tFSMState = FSMStateType.INIT_STS;
		
		getContext().system().scheduler().schedule(Duration.Zero(),
			Duration.create(10, TimeUnit.MILLISECONDS),
			this.getSelf(),
			new TickMsg(),
			getContext().system().dispatcher(),
			null);

		try
		{
			FSMBody(FSMEventType.NO_EVN);
		}
		catch(IOException tExc)
		{
			MainApp.getLogger().warn(tExc.getMessage());
		}

		super.preStart(); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onReceive(Object objMsg) throws Exception
	{
		try
		{
			if(objMsg instanceof TickMsg)
			{
				FSMBody(FSMEventType.NO_EVN);
			}
			else if(objMsg instanceof ConnectMsg)
			{
				_tConnMsg = (ConnectMsg)objMsg;
				FSMBody(FSMEventType.CONN_EVN);
			}
			else if(objMsg instanceof DisConnMsg)
			{
				FSMBody(FSMEventType.DISCONN_EVN);
			}
			else if(objMsg instanceof SockSendMsg)
			{
				_tSockSendMsg = (SockSendMsg)objMsg;
			}
			else
			{
				unhandled(objMsg);
			}
		}
		catch(Exception tExc)
		{
			if(null != _tSocket)
			{
				_tSocket.close();
			}
			
			MainApp.getLogger().warn(tExc.getMessage());
		}
	}

	private void FSMBody(FSMEventType tEvn) throws IOException
	{
		switch(_tFSMState)
		{
			case INIT_STS:
			{
				_tFSMState = FSMStateType.START_CONN_STS;
			}
			break;
			case START_CONN_STS:
			{
				switch(tEvn)
				{
					case CONN_EVN:
					{
						_tSocket = AsynchronousSocketChannel.open();
						_tFutSockConn = _tSocket.connect(new InetSocketAddress(_tConnMsg.getHost(),
							_tConnMsg.getPort()));

						_tFSMState = FSMStateType.WAIT_CONN_STS;
					}
					break;
				}
			}
			break;
			case WAIT_CONN_STS:
			{
				switch(tEvn)
				{
					case NO_EVN:
					{	
						if(_tFutSockConn.isDone())
						{
							try
							{
								Void tValue = _tFutSockConn.get();
							}
							catch(InterruptedException | ExecutionException tExc)
							{
								MainApp.getLogger().warn(tExc.getMessage());
								
								_tSocket.close();
								
								_tFSMState = FSMStateType.START_CONN_STS;
								
								return;
							}
							
							_tReadBuff.clear();
							_tWriteBuff.clear();
							_tSockRecvMsg = null;
							_tSockSendMsg = null;
							_tFutSockRead = null;
							_tFutSockWrite = null;
							
							_tFSMState = FSMStateType.RECV_STS;
						}
					}
					break;
					case DISCONN_EVN:
					{
						_tSocket.close();
						
						_tFSMState = FSMStateType.START_CONN_STS;
					}
					break;
				}
			}
			break;
			case RECV_STS:
			{
				switch(tEvn)
				{
					case NO_EVN:
					{
						if(null != _tFutSockRead)
						{
							_tFSMState = FSMStateType.WAIT_RECV_STS;
							
							return;
						}
						
						try
						{
							_tFutSockRead = _tSocket.read(_tReadBuff);
						}
						catch(NotYetConnectedException tExc)
						{
							_tFSMState = FSMStateType.START_CONN_STS;
							
							return;
						}
						
						_tFSMState = FSMStateType.WAIT_RECV_STS;
					}
					break;
					case DISCONN_EVN:
					{
						_tSocket.close();
						
						_tFSMState = FSMStateType.START_CONN_STS;
					}
					break;
				}
			}
			break;
			case WAIT_RECV_STS:
			{
				switch(tEvn)
				{
					case NO_EVN:
					{	
						if(_tFutSockRead.isDone())
						{
							try
							{
								_iNRecvData = _tFutSockRead.get();
								_tReadBuff.rewind();
								if(0 < _iNRecvData)
								{
									if(null == _tSockRecvMsg)
									{
										_tSockRecvMsg = new SockRecvMsg(new String(_tReadBuff.array(),
											0,
											_iNRecvData,
											StandardCharsets.UTF_8));
									}
									else
									{
										_tSockRecvMsg = new SockRecvMsg(_tSockRecvMsg.getMsg() +
											new String(_tReadBuff.array(),
											0,
											_iNRecvData,
											StandardCharsets.UTF_8));
									}
									_tFutSockRead = null;
								}
								else
								{
									MainApp.getLogger().warn("WAIT_RECV_STS: Received <= 0 Bytes");
								}
							}
							catch(InterruptedException | ExecutionException tExc)
							{
								_tFSMState = FSMStateType.START_CONN_STS;
								
								return;
							}
						}
						else if(0 < _iNRecvData)
						{
							_iNRecvData = 0;
							_tReadBuff.clear();
							
							String strTemp = _tSockRecvMsg.getMsg();
							
							_tSockRecvMsg = null;			
							_tRcvAct.tell(strTemp, getSelf());
						}
						
						_tFSMState = FSMStateType.SEND_STS;
					}
					break;
					case DISCONN_EVN:
					{
						_tSocket.close();
						
						_tFSMState = FSMStateType.START_CONN_STS;
					}
					break;
				}
			}
			break;
			case SEND_STS:
			{
				switch(tEvn)
				{
					case NO_EVN:
					{
						if(null !=_tFutSockWrite)
						{
							_tFSMState = FSMStateType.WAIT_SEND_STS;
							
							return;
						}
						
						if(null != _tSockSendMsg)
						{
							if(_tSockSendMsg.getMsg().isEmpty())
							{
								_tSockSendMsg = null;
								
								_tFSMState = FSMStateType.RECV_STS;
								
								return;
							}
													
							int iNData = _tSockSendMsg.getMsg().getBytes(StandardCharsets.UTF_8).length;
							int iNBuffData = _tWriteBuff.capacity();
							int iMinData = Math.min(iNData, iNBuffData);
							
							_tWriteBuff.rewind();
							_tWriteBuff.put(_tSockSendMsg.getMsg().getBytes(StandardCharsets.UTF_8),
								0,
								iMinData);

							try
							{
								_tWriteBuff.flip();
								_tFutSockWrite = _tSocket.write(_tWriteBuff);
							}
							catch(NotYetConnectedException tExc)
							{
								_tFSMState = FSMStateType.START_CONN_STS;

								return;
							}
							
							_tFSMState = FSMStateType.WAIT_SEND_STS;
							
							return;
						}
						
						_tFSMState = FSMStateType.RECV_STS;
					}
					break;
					case DISCONN_EVN:
					{
						_tSocket.close();
						
						_tFSMState = FSMStateType.START_CONN_STS;
					}
					break;
				}
			}
			break;
			case WAIT_SEND_STS:
			{
				switch(tEvn)
				{
					case NO_EVN:
					{
						if(_tFutSockWrite.isDone())
						{
							Integer iNData;
							
							try
							{
								iNData = _tFutSockWrite.get();
							}
							catch(InterruptedException | ExecutionException tExc)
							{
								MainApp.getLogger().warn(tExc.getMessage());
								
								_tFSMState = FSMStateType.START_CONN_STS;
							
								return;
							}
							
							int iMaxData = _tSockSendMsg.getMsg().getBytes(StandardCharsets.UTF_8).length;
							
							if(iNData < iMaxData)
							{
								String strTemp = _tSockSendMsg.getMsg().substring(iNData);
								
								_tSockSendMsg = new SockSendMsg(strTemp);
							}
							else
							{
								_tSockSendMsg = null;
								_tWriteBuff.clear();
							}
							
							_tFutSockWrite = null;
						}
						
						_tFSMState = FSMStateType.RECV_STS;
					}
					break;
					case DISCONN_EVN:
					{
						_tSocket.close();
						
						_tFSMState = FSMStateType.START_CONN_STS;
					}
					break;
				}
			}
			break;
		}
	}
}
