/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.views;

import Server.Server;
import Server.ServerHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import models.DisplayPlayers;

/**
 * Server Controller class
 *
 * @author Mostafa
 */
public class ServerController implements Initializable {
    @FXML private TableView<DisplayPlayers> tableView;
    @FXML private TableColumn<DisplayPlayers, String> name;
    @FXML private TableColumn<DisplayPlayers, String> status;
    @FXML private TableColumn<DisplayPlayers, Integer> score;
    @FXML
    private Button onbtn;
    @FXML
    private Button offbtn;
    @FXML
    private Button showbtn;
    @FXML
    private Label lblonstatus;
    
    ServerSocket  myServerSocket;
     Thread th;
     
     public void fillList()
    {
        Server.Playerslist = Server.db.displayPlayers();
        tableView.setItems(Server.Playerslist);
        tableView.setVisible(true);
       
    }
     
    // Start Server
    public void startServer() throws SQLException, IOException
    {
        new Server();
        myServerSocket = new ServerSocket(12345);
        th = new Thread( ()->{
        while(true)
        {
              try {
                    Socket s = myServerSocket.accept();             
                    new ServerHandler(s);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        });
            lblonstatus.setTextFill(Color.web("green"));
           Platform.runLater(()->lblonstatus.setText(new Date()+ ":Server Started at socket 12345"));
          th.start();
        
          offbtn.setDisable(false);
          onbtn.setDisable(true);
          showbtn.setDisable(false);
    }
      
    // Stop Server
    public void stopServer(){
        lblonstatus.setTextFill(Color.web("red"));
         Platform.runLater(()->lblonstatus.setText("Server Has Been Stopped"));
                        try{
                            closeServerHandler();
                            myServerSocket.close();
                        } catch(IOException ex){
                            ex.printStackTrace();
                        }
                        th.stop();
                  
                         offbtn.setDisable(true);
                        onbtn.setDisable(false);
                         showbtn.setDisable(true);
    }
    
    public void closeServerHandler() throws IOException{
        ServerHandler.closeAllConnections();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setCellValueFactory(new PropertyValueFactory<DisplayPlayers, String>("name"));
        status.setCellValueFactory(new PropertyValueFactory<DisplayPlayers, String>("status"));
        score.setCellValueFactory(new PropertyValueFactory<DisplayPlayers, Integer>("score"));
         //tableView.setItems(Server.db.displayPlayers( ClientGui.loggedPlayer.getUsername()));
         
    }    
    
}
