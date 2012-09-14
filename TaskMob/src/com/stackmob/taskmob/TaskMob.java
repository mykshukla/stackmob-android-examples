package com.stackmob.taskmob;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.taskmob.R;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobSession;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.util.StackMobLogger;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskMob extends ListActivity {
	
	protected static final String TASKLIST_KEY = "task_list";
	protected static final String TASKLIST_RETURN_KEY = "modified_task_list";
	protected static final String TASKLIST_INDEX = "task_list_index";
	protected static final String LOGGED_IN_USER = "logged_in_user";

	private TaskListAdapter adapter;
	private Button addTaskListButton;
	private TextView addTaskListName;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StackMobAndroid.init(getApplicationContext(), 0, "5c29caee-71f9-4c64-9cf8-fb10a11841f3");
		StackMob.getStackMob().getSession().getLogger().setLogging(true);
		addTaskListButton = (Button) this.findViewById(R.id.add_tasklist_button);
		addTaskListName = (TextView) this.findViewById(R.id.add_tasklist_text);
		addTaskListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addTaskList(addTaskListName.getText().toString());
			}
		});
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		   
		    	i.putExtra(TASKLIST_INDEX, pos);
		    	i.putExtra(TASKLIST_KEY, adapter.getItem(pos).toJson(StackMobOptions.depthOf(1)));
		    	startActivityForResult(i, 0);
		    }
		});
		
		if(StackMob.getStackMob().isLoggedIn()) {
			User.getLoggedInUser(User.class, StackMobOptions.depthOf(2), new StackMobQueryCallback<User>() {
				@Override
				public void success(List<User> list) {
					user = list.get(0);
					initAdapter();
				}

				@Override
				public void failure(StackMobException e) {
					doLogin();
				}
			});
		} else {
			doLogin();
		}
	}
	
	private void doLogin() {
    	Intent i = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivityForResult(i, 1);
	}

	private void addTaskList(String name) {
		TaskList newList = new TaskList(name);
		user.getTaskLists().add(newList);
		user.save(StackMobOptions.depthOf(2));
		adapter.notifyDataSetChanged();
	}
	
	private void initAdapter() {
		//StackMobCallbacks come back on a different thread
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				adapter = new TaskListAdapter(getApplicationContext(), R.layout.tasklistrow, user.getTaskLists());
				setListAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(requestCode == 0) {
				if(data.getExtras().containsKey(TASKLIST_INDEX) && data.getExtras().containsKey(TASKLIST_RETURN_KEY)) {
					adapter.getItem(data.getIntExtra(TASKLIST_INDEX, 0)).fillFromJson(data.getStringExtra(TASKLIST_RETURN_KEY));
					adapter.notifyDataSetChanged();
				}
			} else {
				if(data.getExtras().containsKey(LOGGED_IN_USER)) {
					user = User.newFromJson(User.class, data.getStringExtra(LOGGED_IN_USER));
					initAdapter();
				}
			}
		} catch (StackMobException e) {
		}
	}
}
