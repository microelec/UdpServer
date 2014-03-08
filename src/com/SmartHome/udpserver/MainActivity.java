package com.SmartHome.udpserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
 
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class MainActivity extends Activity implements OnClickListener{
    public static final String SERVERIP = "127.0.0.1"; // 'Within' the emulator!
    public static final int SERVERPORT = 4444;
      public TextView text1;
      public EditText input;
      public Button btn;
      public boolean serverStart;
      public boolean clientStart;
      public Handler Handler;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
       text1=(TextView)findViewById(R.id.textView1);
       input=(EditText)findViewById(R.id.editText1);
       btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
        clientStart=false;
        serverStart=false;
        new Thread(new Server()).start();
        try {
                        Thread.sleep(500);
                } catch (InterruptedException e) { }
        new Thread(new Client()).start();
        Handler = new Handler() {
      @Override public void handleMessage(Message msg) {
            String text = (String)msg.obj;
            text1.append(text);
      }
      };
    }
 
    public class Client implements Runnable {
        @Override
        public void run() {
        	while(true)
        	{
            while(clientStart==false)
            {
            }
            try {
                        Thread.sleep(500);
                  } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                  }
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                updatetrack("Client: Start connecting\n");
                DatagramSocket socket = new DatagramSocket();
                byte[] buf;
                if(!input.getText().toString().isEmpty())
                {
                  buf=input.getText().toString().getBytes();
                }
                else
                {
                  buf = ("Default message").getBytes();
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, SERVERPORT);
                updatetrack("Client: Sending '" + new String(buf) + "'\n");
                socket.send(packet);
                updatetrack("Client: Message sent\n");
                updatetrack("Client: Succeed!\n");
        } catch (Exception e) {
                  updatetrack("Client: Error!\n");
        }
           clientStart = false;
        }
        }

}
    public class Server implements Runnable {
        
        private DatagramSocket socket;

		@Override
        public void run() {
            while(serverStart==false)
            {
            }
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                updatetrack("\nServer: Start connecting\n");
                socket = new DatagramSocket(SERVERPORT, serverAddr);
         } catch (Exception e) {
          updatetrack("Server: Error!\n");
        }
        	while(true){
            while(serverStart==false)
            {
            }
                try {

                        byte[] buf = new byte[17];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        updatetrack("Server: Receiving\n");
                        socket.receive(packet);
                        updatetrack("Server: Message received: '" + new String(packet.getData()) + "'\n");
                        updatetrack("Server: Succeed!\n");
                } catch (Exception e) {
                  updatetrack("Server: Error!\n");
                }
        	}
        }

}
      @Override
      public void onClick(View v) {
            // TODO Auto-generated method stub
            serverStart=true;
            clientStart=true;
      }
      public void updatetrack(String s){
                        Message msg = new Message();
                        String textTochange = s;
                        msg.obj = textTochange;
                        Handler.sendMessage(msg);
      }
     
}