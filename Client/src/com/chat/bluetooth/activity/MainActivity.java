//
package com.chat.bluetooth.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.chat.bluetooth.R;
import com.chat.bluetooth.business.ChatBusinessLogic;
import com.chat.bluetooth.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends GenericActivity{
	
	public static int MSG_TOAST = 0;
	public static int MSG_BLUETOOTH = 2;
	public static int JSON_BLUETOOTH = 3;
	public static int BT_TIMER_VISIBLE = 30;

	/*****CONSTANTS ****************************/
	public static int SONG_SELECT = 1;
	public static int SONG_SELECTED = 2;
	public static int DJ_COMMENT = 3;
	public static int SKIP_SONG  = 4;
	public static int ECHO_SHARED_PREF_SONGS = 5;
	public static int REMOTE_SELECT = 6;
	public static int WANT_END = 7;
	/********************************************/
	
	private final int BT_ACTIVATE = 0;
	private final int BT_VISIBLE = 1;
	private final int SONG_SELECTED_RETURN = 101;
	private final int DJ_RETURN = 102;
	private final int SKIP_RETURN = 103;
	/******************************************/
	private Button buttonDB;
	private Button buttonClient;
	private Button buttonDelete;
	private Button buttonload;
	/***************************************/
	private Button buttonSongRequest;
	private Button buttonDJComment;
	private Button buttonSkip;
	private Button buttonEchoPref;
	private Button buttonRemote;
	private Button buttonDone;
	/***************************************/

	final int MY_PREFS_PRIV_MODE = Activity.MODE_PRIVATE;
	final String MY_PREFS_FILE = "MusicPreferences";
	private String mySdPath;
	// create a reference to the shared preferences object
	SharedPreferences mySharedPreferences;
	// obtain an editor to add data to my SharedPreferences object
	SharedPreferences.Editor myEditor;

	private ImageButton buttonSend;
	private EditText editTextMessage;
	private ListView listVewHistoric;
	private ArrayAdapter<String> historic;
	
	private ToastUtil toastUtil;
	private ChatBusinessLogic chatBusinessLogic;
	private ProgressDialog progressDialog;
    Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		
		settingsAttributes();
		settingsView();
		
		initializaBluetooth();
		registerFilters();
		context = this;
		sharedPreferences();
	}

	public void sharedPreferences(){
		mySdPath = Environment.getExternalStorageDirectory().getAbsolutePath();

		// create a reference to the SharedPreferences file
		mySharedPreferences = getSharedPreferences(MY_PREFS_FILE, MY_PREFS_PRIV_MODE);
		// obtain an editor to add data to (my)SharedPreferences object
		myEditor = mySharedPreferences.edit();

		String name = mySharedPreferences.getString("User", "");
		if(!name.equalsIgnoreCase("Tom"))
		{
			myEditor.putString("User", "Tom");
			toastUtil.showToast("saved");
		}

		myEditor.putBoolean("Flag", false);

		myEditor.commit();

		//String favColor = settings.getString("favorite_color", "default black");
		//int favNumber = settings.getInt("favorite_number", 0);

		//Toast.makeText(this, favColor + " " + favNumber, 1).show();
	}

	public void readSharedPreferences(){
		mySharedPreferences = getSharedPreferences(MY_PREFS_FILE,
			Activity.MODE_PRIVATE );


		String name = mySharedPreferences.getString("User", "");
		if(!name.equalsIgnoreCase(""))
		{
			name = name;
		}
		// retrieving data from SharedPreferences container
		//String favColor = mySharedPreferences.getString("User", "default black");
		//mySharedPreferences.getStringSet("UserPreferences", "UserDetails");
		//int favNumber = mySharedPreferences.getInt("favorite_number", 0);
		toastUtil.showToast(name);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_chat_activity, menu);
        return true;
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clean:
               // historic.clear();
               // historic.notifyDataSetChanged();
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void settingsAttributes() {
		toastUtil = new ToastUtil(this);
		//create business logic object for communication
		chatBusinessLogic = new ChatBusinessLogic(this, handler);
	}

	@Override
	public void settingsView() {
		editTextMessage = (EditText)findViewById(R.id.editTextMessage);

		//historic = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//listVewHistoric = (ListView)findViewById(R.id.listVewHistoric);
		//listVewHistoric.setAdapter(historic);

//		buttonSend = (ImageButton)findViewById(R.id.buttonSend);
//		buttonSend.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String message = editTextMessage.getText().toString();
//
//				if(message.trim().length() > 0){
//					if(chatBusinessLogic.sendMessage(message,1)){
//						editTextMessage.setText("");
//
//						historic.add("Me: " + message);
//						historic.notifyDataSetChanged();
//					}
//				}else{
//					toastUtil.showToast(getString(R.string.enter_message));
//				}
//			}
//		});

		buttonClient = (Button)findViewById(R.id.buttonClient);
		buttonClient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chatBusinessLogic.startFoundDevices();
			}
		});

//		buttonload = (Button)findViewById(R.id.load_button);
//		buttonload.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				sharedPreferences();
//				Intent dbIntent = new Intent (MainActivity.this,
//					ToDoActivity.class);
//
//				Bundle myDataBundle = new Bundle();
//
//				// attach the container to the intent
//				dbIntent.putExtras(myDataBundle);
//
//				startActivityForResult(dbIntent, 101);
//			}
//		});

//		buttonDelete = (Button)findViewById(R.id.delete_button);
//		buttonDelete.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				readSharedPreferences();
//			//	toastUtil.showToast("delete");
//			}
//		});
//
//		buttonDB = (Button)findViewById(R.id.db);
//		buttonDB.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent dbIntent = new Intent (MainActivity.this,
//					ViewFlipperMainActivity.class);
//			}
//		});

		buttonSongRequest = (Button)findViewById(R.id.request_button);
		buttonSongRequest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (chatBusinessLogic.sendMessage("a", SONG_SELECT)) {
					editTextMessage.setText("");

					//historic.add("Me: " + "sent request");
					//historic.notifyDataSetChanged();
				} else {
					toastUtil.showToast(getString(R.string.enter_message));
				}
			}
		});

		buttonDJComment = (Button)findViewById(R.id.dj_button);
		buttonDJComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = mySharedPreferences.getString("User", "");
				String DJComment = editTextMessage.getText().toString();
				if(DJComment.trim().length() > 0) {
					if (chatBusinessLogic.sendMessage(name + ": " +DJComment, DJ_COMMENT)) {
						//historic.add("Me: " + DJComment);
						//historic.notifyDataSetChanged();
					} else {
						toastUtil.showToast("oops! something went wrong");
					}
				}else{
					toastUtil.showToast(getString(R.string.enter_message));
				}
			}
		});

		buttonSkip = (Button)findViewById(R.id.skip_button);
		buttonSkip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

					if (chatBusinessLogic.sendMessage("skip", SKIP_SONG)) {

					} else {
						toastUtil.showToast("oops! something went wrong");
					}

			}
		});

//		buttonEchoPref = (Button)findViewById(R.id.echo_button);
//		buttonEchoPref.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if (chatBusinessLogic.sendMessage("", ECHO_SHARED_PREF_SONGS)) {
//
//				} else {
//					toastUtil.showToast("");
//				}
//			}
//		});

//		buttonRemote = (Button)findViewById(R.id.remote_button);
//		buttonRemote.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				if (chatBusinessLogic.sendMessage("", REMOTE_SELECT)) {
//
//				} else {
//					toastUtil.showToast("");
//				}
//			}
//		});

//		buttonDone = (Button)findViewById(R.id.done_button);
//		buttonDone.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Intent dbIntent = new Intent (MainActivity.this,
//					DJActivity.class);
//
//				Bundle myDataBundle = new Bundle();
//
//				// attach the container to the intent
//				dbIntent.putExtras(myDataBundle);
//
//				startActivityForResult(dbIntent, 101);
//			}
//		});
	}
	
	public void initializaBluetooth() {
		if (chatBusinessLogic.getBluetoothManager().verifySuportedBluetooth()) {
			if (!chatBusinessLogic.getBluetoothManager().isEnabledBluetooth()) { 
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
				startActivityForResult(enableBtIntent, BT_ACTIVATE);
			}
		} else {
			toastUtil.showToast(getString(R.string.no_support_bluetooth));
			finish();
		}
	}
	
	public void registerFilters(){
		chatBusinessLogic.registerFilter();
	}
	/*****************************************************************************************************/
	//handles messages received from music host
	private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (msg) {
                switch (msg.what) {
					case 0://SONG_SELECT
						String theOptions = new String ((String)(msg.obj));
						toastUtil.showToast(theOptions);
						new updateGUIOptions().execute(theOptions);
						break;
                	case 1://SONG_SELECT
                		//toastUtil.showToast("1 here");
						Intent dbIntent = new Intent (MainActivity.this,
							ViewFlipperMainActivity.class);
						Bundle myDataBundle = new Bundle();
						String packStr = new String ((String)(msg.obj));
						//toastUtil.showToast(packStr);
						myDataBundle.putByteArray("package", (packStr.getBytes()));
						dbIntent.putExtras(myDataBundle);
						startActivityForResult(dbIntent, 101);
						//listVewHistoric.requestFocus();
                		break;
                	case 2://SONG_SELECTED
						toastUtil.showToast((String)(msg.obj));
                		//historic.add((String)(msg.obj));
       				 	//historic.notifyDataSetChanged();
						chatBusinessLogic.stopCommucanition();
						break;
					case 3://DJ_COMMENT
						toastUtil.showToast("3 here ");
						Intent djIntent = new Intent (MainActivity.this,
							DJActivity.class);

						Bundle myDJBundle = new Bundle();

						String DJString = new String ((String)(msg.obj));

						myDJBundle.putByteArray("package", (DJString.getBytes()));

						djIntent.putExtras(myDJBundle);

						startActivityForResult(djIntent, 101);
       				 	break;
					case 4://SKIP_SONG
						toastUtil.showToast("4 here ");
						Intent skipIntent = new Intent (MainActivity.this,
							DJActivity.class);

						Bundle mySkipBundle = new Bundle();

						String skipString = new String ((String)(msg.obj));

						mySkipBundle.putByteArray("package", (skipString.getBytes()));

						skipIntent.putExtras(mySkipBundle);

						startActivityForResult(skipIntent, 101);
						break;
                }
            }
        };
    };
	/*****************************************************************************************************/

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
			case BT_ACTIVATE:
				if (RESULT_OK != resultCode) {
					toastUtil.showToast(getString(R.string.activate_bluetooth_to_continue));
					finish(); 
				}
				break;
			case SONG_SELECTED_RETURN:
				if (Activity.RESULT_CANCELED == resultCode){
					buttonSongRequest.setVisibility(View.INVISIBLE);
					buttonDJComment.setVisibility(View.INVISIBLE);
					buttonSkip.setVisibility(View.INVISIBLE);
				}
				if (RESULT_OK == resultCode) {
					Bundle myResultBundle = data.getExtras();
					String myResult = myResultBundle.getString("result");
					toastUtil.showToast("song: " + myResult + " sent to music host");
					if(chatBusinessLogic.sendMessage(myResult,SONG_SELECTED)) {
					}
					buttonSongRequest.setVisibility(View.INVISIBLE);
					buttonDJComment.setVisibility(View.INVISIBLE);
					buttonSkip.setVisibility(View.INVISIBLE);
				}
				break;
			case DJ_RETURN:
				if (resultCode == RESULT_OK) {
					toastUtil.showToast("there you go");
				} else {
					toastUtil.showToast(getString(R.string.device_must_visible));
				}
				buttonSongRequest.setVisibility(View.INVISIBLE);
				buttonDJComment.setVisibility(View.INVISIBLE);
				buttonSkip.setVisibility(View.INVISIBLE);
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		chatBusinessLogic.unregisterFilter();
		chatBusinessLogic.stopCommucanition();
	}


private class updateGUIOptions extends AsyncTask<String, Integer, boolean[]> {

	@Override
	protected boolean[] doInBackground(String... params) {
		String packStr0 = new String(params[0]);
		packStr0 = packStr0.replace("[", "");
		packStr0 = packStr0.replace("]", "");
		packStr0 = packStr0.replaceAll(",", "");
		String[] parts = packStr0.split(" ");
		boolean[] array = new boolean[parts.length];
		for (int i = 0; i < parts.length; i++) {
			array[i] = Boolean.parseBoolean(parts[i]);
		}

		return array;
	}

	@Override
	protected void onPostExecute(boolean[] array) {

		if(array[0]==true)
			buttonSongRequest.setVisibility(View.VISIBLE);
		else
			buttonSongRequest.setVisibility(View.INVISIBLE);

		if(array[1]==true)
			buttonDJComment.setVisibility(View.VISIBLE);
		else
			buttonDJComment.setVisibility(View.INVISIBLE);

		if(array[2]==true)
			buttonSkip.setVisibility(View.VISIBLE);
		else
			buttonSkip.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

	}
}
}