package com.chat.bluetooth.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import com.chat.bluetooth.business.IBusinessLogic;
import com.chat.bluetooth.communication.BluetoothClient;
import com.chat.bluetooth.util.ToastUtil;
import com.chat.bluetooth.R;

public class BluetoothClientTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket>{

	private Context context;
	private ProgressDialog progressDialog;
	
	private ToastUtil toastUtil;
	private BluetoothClient bluetoothClient;
	private IBusinessLogic.OnConnectionBluetoothListener onBluetoothListener;
	
	public BluetoothClientTask(Context context, IBusinessLogic.OnConnectionBluetoothListener onBluetoothListener){
		this.context = context;
		this.onBluetoothListener = onBluetoothListener;
		
		toastUtil = new ToastUtil(context);
		bluetoothClient = new BluetoothClient();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(context, 
											 "Success",
											 context.getText(R.string.msg_connecting_bluetooth));
	}
	// context.getText(R.string.waiting)
	@Override
	protected BluetoothSocket doInBackground(BluetoothDevice... devices) {

		return bluetoothClient.conectedBluetooth(devices[0]);
	}
	
	@Override
	protected void onPostExecute(BluetoothSocket bluetoothSocket) {
		super.onPostExecute(bluetoothSocket);
		bluetoothSocket.getRemoteDevice().getUuids().toString();
		closeDialog();
		
		if(bluetoothSocket != null){
			toastUtil.showToast(bluetoothSocket.getRemoteDevice().getUuids().toString());
			onBluetoothListener.onConnectionBluetooth(bluetoothSocket);
		}else{
			toastUtil.showToast(context.getString(R.string.connection_failed));
		}
	}
	
	private void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}	