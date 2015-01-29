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
	private final StringProperty _strLevelProp = new SimpleStringProperty();
	private final StringProperty _strMsg1Prop = new SimpleStringProperty();
	private final StringProperty _strMsg2Prop = new SimpleStringProperty();

	public LogDataModel(LocalDateTime tLocalTime,
		String strLevel,
		String strMsg1,
		String strMsg2)
	{
		_tDateTimeProp.setValue(tLocalTime);
		_strLevelProp.setValue(strLevel);
		_strMsg1Prop.setValue(strMsg1);
		_strMsg2Prop.setValue(strMsg2);
	}

	public ObjectProperty<LocalDateTime> getDateTimeProp()
	{
		return _tDateTimeProp;
	}

	public StringProperty getLevelProp()
	{
		return _strLevelProp;
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

	public String getLevel()
	{
		return _strLevelProp.get();
	}

	public void setLevel(String strLevel)
	{
		_strLevelProp.set(strLevel);
	}

	public String getMsg1()
	{
		return _strMsg1Prop.get();
	}

	public void setMsg1(String strMsg)
	{
		_strMsg1Prop.set(strMsg);
	}
	
	public String getMsg2()
	{
		return _strMsg2Prop.get();
	}

	public void setMsg2(String strMsg)
	{
		_strMsg2Prop.set(strMsg);
	}
}
