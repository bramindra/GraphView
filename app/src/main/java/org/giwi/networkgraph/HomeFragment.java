package org.giwi.networkgraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HomeFragment extends Fragment {

    private EditText editTextAddress;

    public static Socket socket;
    public static InputStream in;
    public static PrintWriter out;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextAddress = view.findViewById(R.id.ipAddressinput);
        Button buttonConnect = view.findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        return view;
    }

    OnClickListener buttonConnectOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View arg0) {
//                    Client client = new Client(editTextAddress.getText().toString(),8888);
//                    client.run();
                    MyClientTask myClientTask= new MyClientTask(
                            editTextAddress.getText().toString(),
                            8888);
                    myClientTask.execute();
                    assert getFragmentManager() != null;
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_frame, new GraphFragment());
                    ft.commit();
                }};

//    public static class Client extends Thread{
//
//        String dstAddress;
//        int dstPort;
//
//        Client(String s, int i) {
//            dstAddress = s;
//            dstPort = i;
//        }
//
//        static void send(final String string){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    HomeFragment.out.write(string);
//                    HomeFragment.out.flush();
//                }
//            }).start();
//        }
//
//        @Override
//        public void run(){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    socket = null;
//                    try {
//                        socket = new Socket(dstAddress, dstPort);
//
//                        out = new PrintWriter(socket.getOutputStream());
//                        in = socket.getInputStream();
//
//                        while(socket.isConnected()) {
//                            try {
//                                byte[] bytes = new byte[4000000];
//                                int bytesRead = 0;
//                                bytesRead = in.read(bytes);
//                                StringBuilder sBuffer = new StringBuilder(bytesRead);
//                                if (bytesRead > 0) {
//                                    for (int i = 0; i < bytesRead; i++) {
//                                        sBuffer.append((char) bytes[i]);
//                                    }
//
//                                    GraphFragment.graphString = sBuffer.toString();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(2048);
                byte[] buffer = new byte[2048];

                int bytesRead;
                out = new PrintWriter(socket.getOutputStream());
                in = socket.getInputStream();

                bytesRead = in.read(buffer);
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                GraphFragment.graphString = byteArrayOutputStream.toString("UTF-8");

            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}