package org.giwi.networkgraph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InputFragment extends Fragment {

    private String src, dest, thres, ks;
    private Spinner srcSpinner, destSpinner;
    private EditText source, destination, threshold, kstp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_input, container, false);

//        source = view.findViewById(R.id.srcInput);
        srcSpinner = view.findViewById(R.id.srcInput);
        destSpinner = view.findViewById(R.id.destInput);
        threshold = view.findViewById(R.id.thresholdInput);
        kstp = view.findViewById(R.id.jumlahstpInput);


        ArrayList<String> items = GraphFragment.vertexname;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, items);

        srcSpinner.setAdapter(adapter);
        srcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                src = srcSpinner.getSelectedItem().toString().toUpperCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        destSpinner.setAdapter(adapter);
        destSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                dest = destSpinner.getSelectedItem().toString().toUpperCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        Button buttonSend = view.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(buttonSendOnClickListener);

        return view;
    }

    View.OnClickListener buttonSendOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
//                    String src = srcSpinner.getText().toString().toUpperCase();
//                    String dest = destination.getText().toString().toUpperCase();
                    String thres = threshold.getText().toString();
                    String ks = kstp.getText().toString();

//                    HomeFragment.Client.send(src + ", " + dest + ", " + thres + ", " + ks);
                    SocketSendQueryThread socketSendQueryThread = new SocketSendQueryThread(src, dest, thres, ks);
                    socketSendQueryThread.run();

                    Client client = new Client();
                    client.run();
//                    SocketReceiveAnswerThread socketReceiveAnswerThread = new SocketReceiveAnswerThread();
//                    socketReceiveAnswerThread.execute();

                    assert getFragmentManager() != null;
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.main_frame, new AnswerFragment());
                    ft.commit();
                }};

    private class SocketSendQueryThread extends Thread {

        private String sourceSend, destinationSend, thresholdSend, kstpSend;

        SocketSendQueryThread(String s, String d, String t, String k) {
            sourceSend = s;
            destinationSend = d;
            thresholdSend = t;
            kstpSend = k;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HomeFragment.out.write(sourceSend + ", " + destinationSend + ", " + thresholdSend+ ", " + kstpSend);
                    HomeFragment.out.flush();
                }
            }).start();
        }
    }

    public class Client extends Thread{
        Client(){
        }

        @Override
        public void run(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(HomeFragment.socket.isConnected()) {
                        try {
                            byte[] bytes = new byte[4000000];
                            int bytesRead = 0;
                            bytesRead = HomeFragment.in.read(bytes);
                            StringBuilder sBuffer = new StringBuilder(bytesRead);
                            if (bytesRead > 0) {
                                for (int i = 0; i < bytesRead; i++) {
                                    sBuffer.append((char) bytes[i]);
                                }

                                AnswerFragment.answer = sBuffer.toString();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

//    @SuppressLint("StaticFieldLeak")
//    public class SocketReceiveAnswerThread extends AsyncTask<Void, Void, Void> {
//
//        SocketReceiveAnswerThread(){
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    ByteArrayOutputStream byteArrayOutputStream =
//                            new ByteArrayOutputStream(2048);
//                    byte[] buffer = new byte[2048];
//                    int bytesRead;
//                    try {
//                        while ((bytesRead = HomeFragment.in.read(buffer)) != -1) {
//                            byteArrayOutputStream.write(buffer, 0, bytesRead);
//                            AnswerFragment.answer = byteArrayOutputStream.toString("UTF-8");
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }finally{
//                        if(HomeFragment.socket != null){
//                            try {
//                                HomeFragment.socket.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }).start();
//            return null;
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//        }
//
//    }
}