package com.mycompany.ardutest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
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
import org.apache.commons.io.input.Tailer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application
{	
	private static final Logger _tLogger = LogManager.getLogger(MainApp.class);
	private static ActorSystem _tActSys;
	private static Config _tAppConf;
	private static ActorRef _tSockAct;
	private static ActorRef _tTailerAct;
	private final ObservableList<LogDataModel> _rgtLogData = FXCollections.observableArrayList();

	public static ActorRef getSockAct()
	{
		return _tSockAct;
	}

	public static Config getAppConf()
	{
		return _tAppConf;
	}

	public static ActorSystem getActSys()
	{
		return _tActSys;
	}

	public static Logger getLogger()
	{
		return _tLogger;
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{
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
		File tAppFile = new File("App.cfg");
		
		_tAppConf = ConfigFactory.parseFile(tAppFile);
		_tActSys = ActorSystem.create();
		
		_tTailerAct = _tActSys.actorOf(Props.apply(TailerAct.class));
		
		ActorRef tRecvAct = _tActSys.actorOf(Props.apply(LogAct.class));
		
		_tSockAct = _tActSys.actorOf(new Props(() ->
		{
			return new SockAct(tRecvAct);
		}), "SockAct");
		
		getLogger().info("Application Start");
		
		launch(args);
	}
}
