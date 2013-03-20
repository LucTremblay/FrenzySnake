package com.luccharles.frenzysnake;

import org.andengine.AndEngine;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import org.andengine.AndEngine;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class MenuFrenzySnake extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_menu_frenzy_snake);
	}

	public void NewGame(View view)
	{
		 Intent i = new Intent(this, SnakeGameActivity.class);
		 startActivity(i);
	}
}