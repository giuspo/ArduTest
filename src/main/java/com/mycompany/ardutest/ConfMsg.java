/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import com.typesafe.config.Config;

/**
 *
 * @author giulio
 */
public class ConfMsg
{
	public enum EType
	{
		GET_EVT,
		SAVE_EVT
	}
	
	private EType _eType;
	private Config _tConf;
	
	public EType getType()
	{
		return _eType;
	}

	public Config getConf()
	{
		return _tConf;
	}
	
	public ConfMsg(EType eType,
		Config tConf)
	{
		_eType = eType;
		_tConf = tConf;
	}
}
