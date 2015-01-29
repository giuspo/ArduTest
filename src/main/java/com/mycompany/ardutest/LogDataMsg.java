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
	private final String _strLevel;
	private final String _strMsg1;
	private final String _strMsg2;
	
	public LogDataMsg(LocalDateTime tDateTime,
		String strLevel,
		String strMsg1,
		String strMsg2)
	{
		_tDateTime = tDateTime;
		_strLevel = strLevel;
		_strMsg1 = strMsg1;
		_strMsg2 = strMsg2;
	}

	public LocalDateTime gettDateTime()
	{
		return _tDateTime;
	}

	public String getLevel()
	{
		return _strLevel;
	}

	public String getMsg1()
	{
		return _strMsg1;
	}

	public String getMsg2()
	{
		return _strMsg2;
	}
}
