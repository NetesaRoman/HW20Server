package server;

import model.ClientModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 * @author Roman Netesa
 *
 */
public class Server {
    static ExecutorService executeIt = Executors.newFixedThreadPool(20);


    /**
     *
     */
    public static void main(String[] args) {

        List<ClientModel> clients = new ArrayList<>();
        List<MonoThreadClientHandler> servers = new ArrayList<>();

        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Server socket created, command console reader for listen to server commands");


            int numOfClients = 0;
            while (!server.isClosed()) {



                Socket client = server.accept();
                ClientModel clientModel = new ClientModel(client, numOfClients);
                numOfClients++;
                clients.add(clientModel);
                for (MonoThreadClientHandler monoServer : servers){
                    monoServer.joinInfo(clientModel);
                }


                MonoThreadClientHandler newServer = new MonoThreadClientHandler(clientModel, numOfClients);
                servers.add(newServer);
                    executeIt.execute(newServer);



                System.out.print("Connection accepted.");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
