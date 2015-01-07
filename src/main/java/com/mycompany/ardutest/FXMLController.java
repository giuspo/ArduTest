package com.mycompany.ardutest;

import com.typesafe.config.Config;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable
{
	@FXML
    private TextField SockSendTxt;
	
	@FXML
    private void OnActionSetting(ActionEvent event)
	{
		
    }
	
	@FXML
    private void OnActionConnect(ActionEvent event)
	{	
		try
		{
			// boolean bRet = MainApp.getAppConf().hasPath("Sock");
			Config tConf = MainApp.getAppConf().getConfig("Sock");

			String strHost = tConf.getString("Host");
			int iPort = tConf.getInt("Port");

			ConnectMsg tConnMsg = new ConnectMsg(strHost,
				iPort);
			
			MainApp.getSockAct().tell(tConnMsg, null);
		}
		catch(com.typesafe.config.ConfigException tExc)
		{
			String strMsg = tExc.getMessage();
			
			MainApp.getLogger().debug(strMsg);
		}
    }
	
	@FXML
    private void OnActionDisConn(ActionEvent event)
	{
		DisConnMsg tDisConnMsg = new DisConnMsg();
		
		MainApp.getSockAct().tell(tDisConnMsg, null);
	}
	
	@FXML
    private void OnActionSend(ActionEvent event)
	{
		SockSendMsg tSockSendMsg = new SockSendMsg(SockSendTxt.getText());
		
		MainApp.getSockAct().tell(tSockSendMsg, null);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// TODO
	}
}
