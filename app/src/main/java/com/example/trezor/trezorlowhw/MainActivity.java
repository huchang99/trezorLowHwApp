package com.example.trezor.trezorlowhw;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.protobuf.Message;
import com.satoshilabs.trezor.Trezor;
import com.satoshilabs.trezor.protobuf.TrezorMessage;
import com.satoshilabs.trezor.protobuf.TrezorType;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    //button
    private Button dianjiBt;
    private Button getPublicKeybt;
    private Button UserInPutPinbt;
    private Button getAddressBt;

    private TextView DisText;
    private EditText PubilcPinet;
    //device
    private Trezor t;

    //log
    private TextView LogTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initLinstener();
        initData();
    }

    private void initLinstener() {
        dianjiBt.setOnClickListener(this);
        getPublicKeybt.setOnClickListener(this);
        UserInPutPinbt.setOnClickListener(this);
        getAddressBt.setOnClickListener(this);
    }

    private void initData() {
         t = new Trezor(MainActivity.this);
    }

    private void initViews() {
        dianjiBt =(Button) findViewById(R.id.bt);
        DisText = (TextView)findViewById(R.id.Distext);
        getPublicKeybt = (Button)findViewById(R.id.getPublicKeybt);
        UserInPutPinbt = (Button)findViewById(R.id.OKPinbt);
        PubilcPinet = (EditText)findViewById(R.id.editpin);
        getAddressBt = (Button)findViewById(R.id.getAddressBt);
        LogTv = (TextView)findViewById(R.id.LogTv);
        LogTv.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt: {
                DisText.setText("haha");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 写子线程中的操作
                        if (t.connect(MainActivity.this)) {
                            TrezorMessage.Initialize req = TrezorMessage.Initialize.newBuilder().build();
                            Message resp = t.send(req);
                            if (resp != null) {
                                TrezorMessage.Features x = (TrezorMessage.Features) resp;
                                Log.d("features", String.valueOf(x.getMajorVersion()));
                                // "got: " + resp.getClass().getSimpleName()
                                Log.d("resp", resp.toString());
                                Log.d("trezor_bt", "hahahh");

                            }
                        }
                    }
                }).start();
                break;
            }
            case R.id.getPublicKeybt: {
                        TrezorMessage.GetPublicKey BtcoinPublicKey = TrezorMessage.GetPublicKey.newBuilder().build();
                        Message btcoinPKeyMsg = t.send(BtcoinPublicKey);
                        DecideMsgContext(btcoinPKeyMsg);
                break;
            }
            case R.id.OKPinbt:{
                String tempPIN = PubilcPinet.getText().toString();
                TrezorMessage.PinMatrixAck txPinAck = TrezorMessage.PinMatrixAck.newBuilder()
                        .setPin(tempPIN)
                        .build();
                Message PinMsg = t.send(txPinAck);
                DecideMsgContext(PinMsg);
                break;
            }
            case R.id.getAddressBt:{
                TrezorMessage.GetAddress BtcoinAddr =  TrezorMessage.GetAddress.newBuilder().build();
                Message btcoinAddrMsg = t.send(BtcoinAddr);
                DecideMsgContext(btcoinAddrMsg);
            }
        }
    }

    public void DecideMsgContext(Message msg)
    {
        if(msg != null)
        if(msg.toString().isEmpty())
            Log.d("Msg1 is ", "null");
        else {
            Log.d("Msg2 is ", msg.toString());
            LogTv.setText(LogTv.getText().toString()+msg.toString()+"\n");
        }

    }
}
