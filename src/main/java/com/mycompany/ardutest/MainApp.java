package com.mycompany.ardutest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application
{	
	private static final Logger _tLogger = LogManager.getLogger(MainApp.class);
	private static ActorSystem _tActSys;
	private static Config _tAppConf;
	private static ActorRef _tSockAct;

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
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/styles/Styles.css");

		stage.setTitle("JavaFX and Maven");
		stage.setScene(scene);
		
		stage.setOnCloseRequest((WindowEvent event) ->
		{
			_tActSys.shutdown();
		});
		
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
		
		ActorRef tRecvAct = _tActSys.actorOf(Props.apply(RecvAct.class));
		
		_tSockAct = _tActSys.actorOf(new Props(() ->
		{
			return new SockAct(tRecvAct);
		}), "SockAct");
		
		launch(args);
	}
}
