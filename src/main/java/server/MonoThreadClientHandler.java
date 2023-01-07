package server;

import model.ClientModel;

import java.io.*;
import java.net.Socket;

/*
 *
 * @author Roman Netesa
 *
 */
public class MonoThreadClientHandler implements Runnable {
    private  Socket clientDialog;
    private  ClientModel client;
    private  int num;
    private  static int fileCount = 1;
    private  DataOutputStream dataOutputStream = null;
    private  DataInputStream dataInputStream = null;

    public MonoThreadClientHandler(ClientModel client,int num) {
        this.clientDialog = client.getSocket();
        this.client = client;
        this.num = num;
        this.client.setName("Client" + num);
    }

    @Override
    public void run() {

        try {

            PrintWriter out = new PrintWriter(clientDialog.getOutputStream(), true);


            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            System.out.println("DataInputStream created");

            System.out.println("DataOutputStream  created");

            while (!clientDialog.isClosed()) {



                System.out.println("Server reading from channel");

                String entry = in.readLine();


                System.out.println(client.getName() + ": " + entry);

                if (entry.split(" ")[0].equalsIgnoreCase("file")) {
                    dataInputStream = new DataInputStream(
                            clientDialog.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            clientDialog.getOutputStream());

                    receiveFile("NewFile" + fileCount +".txt");
                    fileCount++;

                    dataInputStream.close();
                    dataOutputStream.close();
                }

                if (entry.equalsIgnoreCase("exit")) {

                    System.out.println("Client initialize connections suicide ...");
                    out.println("Server reply - " + entry + " - OK");
                    Thread.sleep(3000);
                    break;
                }

                System.out.println("Server try writing to channel");
                out.println("Server reply - " + entry + " - OK");
                System.out.println("Server Wrote message to " + client.getName());

            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();

            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void joinInfo(ClientModel client) {
        if (!clientDialog.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(clientDialog.getOutputStream(), true);
                out.println("[SERVER] " + client.getName() + " connected!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveFile(String fileName)
            throws Exception
    {
        int bytes = 0;
        FileOutputStream fileOutputStream
                = new FileOutputStream(fileName);

        long size
                = dataInputStream.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0
                && (bytes = dataInputStream.read(
                buffer, 0,
                (int)Math.min(buffer.length, size)))
                != -1) {

            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }

        System.out.println("File is Received");
        fileOutputStream.close();
    }
}
