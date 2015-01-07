/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

/**
 *
 * @author giulio
 */
public class ConnectMsg
{
	private final String _strHost;
	private final int _iPort;

	public String getHost()
	{
		return _strHost;
	}

	public int getPort()
	{
		return _iPort;
	}

	public ConnectMsg(String strHost, int iPort)
	{
		this._strHost = strHost;
		this._iPort = iPort;
	}
}
