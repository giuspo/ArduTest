/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ardutest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;

/**
 * FXML Controller class
 *
 * @author giulio
 */
public class ConfUIController implements Initializable
{
	private ConfModel _tConfModel;
	private Stage _tStage;
	private boolean _bOk = false;

	public boolean isOk()
	{
		return _bOk;
	}
	
	@FXML
    private TextField HostTxt;
	
	@FXML
    private TextField PortTxt;
	
	
	@FXML
    private void OnActionOk(ActionEvent tEvn)
	{
		_bOk = true;
		_tStage.close();
	}
	
	@FXML
    private void OnActionCancel(ActionEvent tEvn)
	{
		_tStage.close();
	}
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// TODO
	}
	
	public void InitData(Stage tStage,
		ConfModel tConfModel)
	{
		_tStage = tStage;
		_tConfModel = tConfModel;
		HostTxt.textProperty().bindBidirectional(_tConfModel.getHostProp());
		Bindings.bindBidirectional(PortTxt.textProperty(),
			tConfModel.getPortProp(),
			new NumberStringConverter());
	}
}
