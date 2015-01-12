/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author giulio
 */
public class LogDataModel
{
	private ObjectProperty<LocalDateTime> _tDateTime;
	private IntegerProperty _iLevel;
	private StringProperty _strMsg;

	public ObjectProperty<LocalDateTime> getDateTime()
	{
		return _tDateTime;
	}

	public void setDateTime(ObjectProperty<LocalDateTime> _tDateTime)
	{
		this._tDateTime = _tDateTime;
	}

	public IntegerProperty getLevel()
	{
		return _iLevel;
	}

	public void setLevel(IntegerProperty _iLevel)
	{
		this._iLevel = _iLevel;
	}

	public StringProperty getMsg()
	{
		return _strMsg;
	}

	public void setMsg(StringProperty _strMsg)
	{
		this._strMsg = _strMsg;
	}
}
