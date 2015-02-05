/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author giulio
 */
public class ConfModel
{
	private final StringProperty _strHostProp = new SimpleStringProperty();
	private final IntegerProperty _iPortProp = new SimpleIntegerProperty();
	
	public ConfModel(String strHost,
		int iPort)
	{
		_strHostProp.setValue(strHost);
		_iPortProp.setValue(iPort);
	}
	
	public StringProperty getHostProp()
	{
		return _strHostProp;
	}
	
	public IntegerProperty getPortProp()
	{
		return _iPortProp;
	}
	
	public String getHost()
	{
		return _strHostProp.getValue();
	}
	
	public void setHost(String strHost)
	{
		_strHostProp.setValue(strHost);
	}
	
	public int getPort()
	{
		return _iPortProp.getValue();
	}
	
	public void setPort(int iPort)
	{
		_iPortProp.setValue(iPort);
	}
}
