package com.example.nonhlanhla.serverlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText ed_cell_Number, ed_pin_Number;
    private Button btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_cell_Number = findViewById(R.id.ed_cell_Number);
        ed_pin_Number = findViewById(R.id.ed_pin_Number);
        btn_Login = findViewById(R.id.btn_Login);


        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Client client = new Client();
                try {
                    client.startClient();

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        });


    }


    public class Server {
        ServerSocket serversocket;
        Socket client;
        Connect c = new Connect();
        BufferedReader input;
        PrintWriter output;

        public void start() throws IOException {
            System.out.println("Connection Starting on port:" + c.getPort());
            //make connection to client on port specified
            serversocket = new ServerSocket(c.getPort());

            //accept connection from client
            client = serversocket.accept();

            System.out.println("Waiting for connection from client");

            try {
                logInfo();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Server server = new Server();
            try {
                server.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void logInfo() throws Exception {
            //open buffered reader for reading data from client
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String username = input.readLine();
            System.out.println("SERVER SIDE" + username);
            String password = input.readLine();
            System.out.println("SERVER SIDE" + password);

            //open printwriter for writing data to client
            output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

            if(username.equals(c.getUsername()) &&password.equals(c.getPassword())){
                output.println("Welcome, " + username);
            }else{
                output.println("Login Failed");
            }
            output.flush();
            output.close();

        }
    }

    public class Client {
        private final String FILENAME = null;
        Connect c = new Connect();
        Socket socket;
        BufferedReader read;
        PrintWriter output;

        public void startClient() throws UnknownHostException, IOException {
            //Create socket connection
            socket = new Socket(c.gethostName(), c.getPort());

            final String username = ((TextView) findViewById(R.id.ed_cell_Number)).getText().toString();
            output.println(username);

            final String password = ((TextView) findViewById(R.id.ed_pin_Number)).getText().toString();
            output.println(password);


            output.flush();

            //create Buffered reader for reading response from server
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //read response from server
            String response = read.readLine();
            System.out.println("This is the response: " + response);

        }


    }

    public class Connect {
        private String CELLNUMBER = "java";
        private String PASSWORD = "java";
        private int PORT = 3110;
        private String HOSTNAME = "196.31.118.146";

        public String getUsername() {
            return this.CELLNUMBER;
        }

        public String getPassword() {

            return this.PASSWORD;
        }

        public int getPort() {
            return this.PORT;
        }

        public String gethostName() {
            return this.HOSTNAME;
        }
    }
}
