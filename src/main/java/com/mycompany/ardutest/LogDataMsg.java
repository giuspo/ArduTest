/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import java.time.LocalDateTime;

/**
 *
 * @author giulio
 */
public class LogDataMsg
{
	private final LocalDateTime _tDateTime;
	private final int _iLevel;
	private final String _strMsg1;
	private final String _strMsg2;
	
	public LogDataMsg(LocalDateTime tDateTime,
		int iLevel,
		String strMsg1,
		String strMsg2)
	{
		this._tDateTime = tDateTime;
		this._iLevel = iLevel;
		this._strMsg1 = strMsg1;
		this._strMsg2 = strMsg2;
	}

	public LocalDateTime gettDateTime()
	{
		return _tDateTime;
	}

	public int getiLevel()
	{
		return _iLevel;
	}

	public String getStrMsg1()
	{
		return _strMsg1;
	}

	public String getStrMsg2()
	{
		return _strMsg2;
	}
}
