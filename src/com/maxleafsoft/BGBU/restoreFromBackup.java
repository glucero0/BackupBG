package com.maxleafsoft.BGBU;

import android.app.*;
import android.view.*;
import android.widget.ArrayAdapter;
import android.os.*;
import android.content.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import android.util.Log;
import android.widget.*;
import android.widget.AdapterView.*;

public class restoreFromBackup extends Activity
{
	ListView listView;
	ArrayAdapter adapter;
	String restoreSourceBase;
	String restoreDest;
	String restoreSource;
	List<String> saveList = new ArrayList<String>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.restorefrombackup);
		
		Button button = (Button)findViewById(R.id.btnSave);
		button.setEnabled(false);
		
		// handle intent, destination path, 
		// and populate list view
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		restoreDest = bundle.getString("srcpath");
		restoreSourceBase = bundle.getString("dstpath");
		List<String> gameSaveList = getStringArrayOfSubDirs(new File(restoreSourceBase));
		adapter = new ArrayAdapter<String>(this, 
			android.R.layout.simple_list_item_1, android.R.id.text1, gameSaveList);
		listView = (ListView) findViewById(R.id.listBackups);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
			{
				Button button = (Button)findViewById(R.id.btnSave);
				button.setEnabled(true);
				restoreSource = restoreSourceBase + "/" + listView.getItemAtPosition(position).toString();
				TextView help = (TextView) findViewById(R.id.helpText);
				help.setText("Restore from: " + restoreSource +
							 "\r\n\r\nRestore to: " + restoreDest);            
			}
		});
	}
	
	public List<String> getStringArrayOfSubDirs(File sourceLocation)
	{
		//Log.d(sourceLocation.toString(),"src loc is dir");
		
		if (sourceLocation.isDirectory())
		{
			//Log.d(sourceLocation.toString(),"src loc is dir");
			
			String[] children = sourceLocation.list();
			for(int i = 0; i < sourceLocation.listFiles().length; i++)
			{
				if (new File(sourceLocation, children[i]).isDirectory())
				{
					//Log.d("gonna chk if is Dated",children[i].toString());
					if (!children[i].equals("Dated"))
					{
						//Log.d("is not Dated",children[i].toString());
						saveList.add(children[i]);
					}
					else
					{
						//Log.d("is Dated",children[i].toString());
						getStringArrayOfSubDirs(new File(sourceLocation + "/" + children[i]));
					}
							
				}
			}
		}
		
		return saveList;
	}
	
	public File getSelectedRestore()
	{
		listView = (ListView)findViewById(R.id.listBackups);
		
		return new File(listView.getSelectedItem().toString());
	}
	
	public void restoreSave(View view)
	{
		//Log.d("in restoreSave","hello");
		try
		{
			utilityFunctions.copyFolders(new File(restoreSource), new File(restoreDest));
			TextView help = (TextView) findViewById(R.id.helpText);
			help.setText("Restore Successful");
		}
		catch (Exception ex)
		{
			TextView help = (TextView) findViewById(R.id.helpText);
			help.setText("Error: " + ex.toString());
		}
	}
}
