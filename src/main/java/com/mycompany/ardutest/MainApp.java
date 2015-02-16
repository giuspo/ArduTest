package com.mycompany.ardutest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.duration.FiniteDuration;

public class MainApp extends Application
{
	private static final Logger _tLogger = LogManager.getLogger(MainApp.class);
	private static ActorSystem _tActSys;
	private static Config _tAppConf;
	private static ActorRef _tSockAct;
	private static ActorRef _tTailerAct;
	private static ActorRef _tUpdLogAct;
	private static ActorRef _tSockRecvAct;
	private static ActorRef _tConfAct;
	private static final ObservableList<LogDataModel> _rgtLogData = FXCollections.observableArrayList();
	private static Stage _tStage;

	public static ActorRef getSockAct()
	{
		return _tSockAct;
	}

	public static Config getAppConf()
	{
		Inbox tInbox = Inbox.create(_tActSys);
		ConfMsg tConfLoadMsg = new ConfMsg(ConfMsg.EType.GET_EVT,
			null);
		
		tInbox.send(_tConfAct, tConfLoadMsg);
		
		ConfMsg tConfLoadedMsg = (ConfMsg)tInbox.receive(FiniteDuration.Zero());
		
		return tConfLoadedMsg.getConf();
	}
	
	public static void setAppConf(Config tConf)
	{
		Inbox tInbox = Inbox.create(_tActSys);
		ConfMsg tConfLoadMsg = new ConfMsg(ConfMsg.EType.SAVE_EVT,
			tConf);
		
		tInbox.send(_tConfAct, tConfLoadMsg);
	}

	public static ActorSystem getActSys()
	{
		return _tActSys;
	}

	public static Logger getLogger()
	{
		return _tLogger;
	}
	
	public static Stage getStage()
	{
		return _tStage;
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{
		_tStage = stage;
		
		// SG: 1
		// Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
		// Scene scene = new Scene(root);
		
		// SG: 2
//		FXMLController tFXMLControl = new FXMLController(_rgtLogData);
//		FXMLLoader tLoader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
		
//		tLoader.setController(tFXMLControl);
//		
//		Parent tPane = (Parent)tLoader.load();
		
		// SG: 3
		FXMLLoader tLoader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
		Parent tParent = tLoader.load();
		Scene scene = new Scene(tParent);
		
		scene.getStylesheets().add("/styles/Styles.css");
		stage.setTitle("JavaFX and Maven");
		stage.setScene(scene);
		stage.setOnCloseRequest((WindowEvent event) ->
		{
			_tActSys.shutdown();
		});
		
		FXMLController tFXMLControl = tLoader.<FXMLController>getController();
		
		tFXMLControl.InitData(_rgtLogData);
		
		getLogger().info("GUI Start");
		
		stage.show();
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{	
		_tActSys = ActorSystem.create();
		
		_tConfAct = _tActSys.actorOf(Props.create(ConfAct.class),
			"ConfAct");
		
		_tUpdLogAct = _tActSys.actorOf(Props.create(UpdLogAct.class, _rgtLogData),
			"UpdLogAct");
		
		_tTailerAct = _tActSys.actorOf(Props.create(TailerAct.class),
			"TailerAct");
		
		_tSockRecvAct = _tActSys.actorOf(Props.create(SockRecvAct.class),
			"SockRecvAct");
		
		_tSockAct = _tActSys.actorOf(Props.create(SockAct.class),
			"SockAct");
		
		getLogger().info("Application Start");
		
		launch(args);
	}
}
