package fr.mrcraftcod.simulator.jfx.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2019-01-17.
 *
 * @author Thomas Couchoud
 * @since 2019-01-17
 */
public class JFXUtils{
	private static final Logger LOGGER = LoggerFactory.getLogger(JFXUtils.class);
	
	public static void displayExceptionAlert(final Throwable e, final String title, final String header, final String content){
		final var alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		var exceptionText = "UNKNOWN";
		try(final var sw = new StringWriter(); final var pw = new PrintWriter(sw)){
			e.printStackTrace(pw);
			exceptionText = sw.toString();
		}
		catch(final IOException e1){
			LOGGER.error("Error displaying error in alert window", e1);
			return;
		}
		
		final var label = new Label("The exception stacktrace was:");
		
		final var textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		final var expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}
}
