/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
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
	
	public StringProperty getHostProp()
	{
		return _strHostProp;
	}
	
	public IntegerProperty getPortProp()
	{
		return _iPortProp;
	}
	
	public Config getConfig()
	{
		Config tNewConf = ConfigFactory.empty()
			.withValue("Host", ConfigValueFactory.fromAnyRef(_strHostProp.get()))
			.withValue("Port", ConfigValueFactory.fromAnyRef(_iPortProp.get()))
			.atPath("Sock");
		
		return tNewConf;
	}
	
	public ConfModel(Config tConf)
	{
		tConf.getConfig("Sock");
		String strHost = tConf.getString("Host");
		
		_strHostProp.setValue(strHost);
		
		int iPort = tConf.getInt("Port");
		
		_iPortProp.setValue(iPort);
	}	
}
