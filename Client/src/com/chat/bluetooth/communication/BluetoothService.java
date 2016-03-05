package com.chat.bluetooth.communication;

import java.io.IOException;
import java.util.UUID;

import com.chat.bluetooth.activity.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;


public class BluetoothService{
	 
	 private final String NAME_SERVICE_BT = "bluetooth";
	 private final UUID ID_CONECTION = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); 
						// UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
	 public BluetoothSocket startServer(BluetoothAdapter adaptador) {
		 BluetoothSocket bluetoothSocket = null;
		 
		 try {
			 BluetoothServerSocket serverSocket = adaptador.listenUsingRfcommWithServiceRecord(NAME_SERVICE_BT, ID_CONECTION); 
			 bluetoothSocket = serverSocket.accept(MainActivity.BT_TIMER_VISIBLE * 1000);
			 
		 } catch (IOException e) { 
			 e.printStackTrace();
		 }
		 
		 return bluetoothSocket;
	 }
	 
}