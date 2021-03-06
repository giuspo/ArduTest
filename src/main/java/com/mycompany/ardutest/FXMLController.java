package com.mycompany.ardutest;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLController implements Initializable
{
	private ObservableList<LogDataModel> _rgtLogData;
	
	@FXML
    private TextField SockSendTxt;
	
	@FXML
	private TableView<LogDataModel> LogDataTbl;
	
	@FXML
	private TableColumn<LogDataModel, LocalDateTime> DateCol;
	
	@FXML
	private TableColumn<LogDataModel, String> LevelCol;
	
	@FXML
	private TableColumn<LogDataModel, String> Msg1Col;
	
	@FXML
	private TableColumn<LogDataModel, String> Msg2Col;
	
	@FXML
    private void OnActionSetting(ActionEvent event) throws IOException
	{
		FXMLLoader tLoader = new FXMLLoader(getClass().getResource("/fxml/ConfUI.fxml"));
		Parent tParent = tLoader.load();
		Scene tScene = new Scene(tParent);
		
		tScene.getStylesheets().add("/styles/Styles.css");
		
		Stage tStage = new Stage();
		
		tStage.initModality(Modality.WINDOW_MODAL);
		tStage.initOwner(MainApp.getStage());
		tStage.setTitle("Configuration");
		tStage.setScene(tScene);
		
		ConfModel tConfModel = new ConfModel(MainApp.getAppConf());
		ConfUIController tConfUiControl = tLoader.<ConfUIController>getController();
		
		tConfUiControl.InitData(tStage,
			tConfModel);
		tStage.showAndWait();
		
		if(tConfUiControl.isOk())
		{	
			MainApp.setAppConf(tConfModel.getConfig());
		}
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
	
	@FXML
    private void OnActionTest(ActionEvent event)
	{
		LogDataModel tLog = new LogDataModel(LocalDateTime.now(),
			"INFO",
			"",
			"Hello World!!!");
		_rgtLogData.add(tLog);
		
		MainApp.getLogger().info("Add a Test Log Value");
    }

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// TODO
		DateCol.setCellValueFactory(tData -> tData.getValue().getDateTimeProp());
		LevelCol.setCellValueFactory(tData -> tData.getValue().getLevelProp());
		Msg1Col.setCellValueFactory(tData -> tData.getValue().getMsg1Prop());
		Msg2Col.setCellValueFactory(tData -> tData.getValue().getMsg2Prop());
	}
	
	public void InitData(ObservableList<LogDataModel> rgtObsLogData)
	{
		_rgtLogData = rgtObsLogData;
		LogDataTbl.setItems(_rgtLogData);
	}
}
