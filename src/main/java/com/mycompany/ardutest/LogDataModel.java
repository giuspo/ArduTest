/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author giulio
 */
public class LogDataModel
{
	private final ObjectProperty<LocalDateTime> _tDateTimeProp = new SimpleObjectProperty<>();
	private final IntegerProperty _iLevelProp = new SimpleIntegerProperty();
	private final StringProperty _strMsg1Prop = new SimpleStringProperty();
	private final StringProperty _strMsg2Prop = new SimpleStringProperty();

	public ObjectProperty<LocalDateTime> getDateTimeProp()
	{
		return _tDateTimeProp;
	}

	public IntegerProperty getLevelProp()
	{
		return _iLevelProp;
	}

	public StringProperty getMsg1Prop()
	{
		return _strMsg1Prop;
	}
	
	public StringProperty getMsg2Prop()
	{
		return _strMsg2Prop;
	}
	
	public LocalDateTime getDateTime()
	{
		return _tDateTimeProp.get();
	}

	public void setDateTime(LocalDateTime tDateTime)
	{
		this._tDateTimeProp.set(tDateTime);
	}

	public Integer getLevel()
	{
		return _iLevelProp.get();
	}

	public void setLevel(int iLevel)
	{
		this._iLevelProp.set(iLevel);
	}

	public String getMsg1()
	{
		return _strMsg1Prop.get();
	}

	public void setMsg1(String strMsg)
	{
		this._strMsg1Prop.set(strMsg);
	}
	
	public String getMsg2()
	{
		return _strMsg2Prop.get();
	}

	public void setMsg2(String strMsg)
	{
		this._strMsg2Prop.set(strMsg);
	}
}
