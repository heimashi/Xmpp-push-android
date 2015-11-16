package com.xmpppush.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class NotificationDetailsActivity extends Activity {

    private static final String LOGTAG = CommonUtil
            .makeLogTag(NotificationDetailsActivity.class);

    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public NotificationDetailsActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = this.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        callbackActivityPackageName = sharedPrefs.getString(
                Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
        callbackActivityClassName = sharedPrefs.getString(
                Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

        Intent intent = getIntent();
        String notificationId = intent
                .getStringExtra(Constants.NOTIFICATION_ID);
        String notificationApiKey = intent
                .getStringExtra(Constants.NOTIFICATION_API_KEY);
        String notificationTitle = intent
                .getStringExtra(Constants.NOTIFICATION_TITLE);
        String notificationMessage = intent
                .getStringExtra(Constants.NOTIFICATION_MESSAGE);
        String notificationUri = intent
                .getStringExtra(Constants.NOTIFICATION_URI);

        Log.d(LOGTAG, "notificationId=" + notificationId);
        Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
        Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
        Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
        Log.d(LOGTAG, "notificationUri=" + notificationUri);


        View rootView = createView(notificationTitle, notificationMessage,
                notificationUri);
        setContentView(rootView);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(mBroadcastReceiver, filter);
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mBroadcastReceiver);
    };

    ProgressBar progressBar = null;
    TextView showtv = null;
    
    private View createView(final String title, final String message,
            final String uri) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(0xffeeeeee);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);

        TextView textTitle = new TextView(this);
        textTitle.setText(title);
        textTitle.setTextSize(18);
        // textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textTitle.setTextColor(0xff000000);
        textTitle.setGravity(Gravity.CENTER);

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 30, 0);
        textTitle.setLayoutParams(layoutParams);
        linearLayout.addView(textTitle);

        TextView textDetails = new TextView(this);
        textDetails.setText(message);
        textDetails.setTextSize(14);
        // textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textDetails.setTextColor(0xff333333);
        textDetails.setGravity(Gravity.CENTER);

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 10, 30, 20);
        textDetails.setLayoutParams(layoutParams);
        linearLayout.addView(textDetails);

        Button okButton = new Button(this);
        okButton.setText("Ok");
        okButton.setWidth(100);

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent;
                if (uri != null
                        && uri.length() > 0
                        && (uri.startsWith("http:") || uri.startsWith("https:")
                                || uri.startsWith("tel:") || uri
                                .startsWith("geo:"))) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                } else {
                    intent = new Intent().setClassName(
                            callbackActivityPackageName,
                            callbackActivityClassName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                }

                NotificationDetailsActivity.this.startActivity(intent);
                NotificationDetailsActivity.this.finish();
            }
        });

        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setGravity(Gravity.CENTER);
        innerLayout.addView(okButton);

        linearLayout.addView(innerLayout);
        
        if(!TextUtils.isEmpty(uri)){
        	TextView fileTv = new TextView(this);
        	fileTv.setText(uri);
        	fileTv.setTextSize(14);
        	fileTv.setTextColor(0xff333333);
        	fileTv.setGravity(Gravity.CENTER);

            layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 10, 30, 20);
            fileTv.setLayoutParams(layoutParams);
            linearLayout.addView(fileTv);
            
            showtv = new TextView(this);
            showtv.setText(uri);
            showtv.setTextSize(14);
            showtv.setTextColor(0xff333333);
            showtv.setGravity(Gravity.CENTER);

            layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 10, 30, 20);
            showtv.setLayoutParams(layoutParams);
            linearLayout.addView(showtv);
            
            progressBar = new ProgressBar(this);
            progressBar.setId(1111);
            progressBar.setMax(100);
            
            layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            linearLayout.addView(progressBar);
            
            Button startBtn = new Button(this);
            startBtn.setText("start");
            startBtn.setWidth(100);
            startBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FileInfo fileInfo = new FileInfo(0,uri,uri.substring(uri.lastIndexOf("/")),0,0); 
					Intent intent = new Intent(NotificationDetailsActivity.this,DownloadService.class);
					intent.setAction(DownloadService.ACTION_START);
					intent.putExtra(DownloadService.DOWNLOAD_INTENT_EXTRA, fileInfo);
					startService(intent);
				}
			});
            
            Button stopBtn = new Button(this);
            stopBtn.setText("stop");
            stopBtn.setWidth(100);
            stopBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FileInfo fileInfo = new FileInfo(0,uri,uri.substring(uri.lastIndexOf("/")),0,0); 
					Intent intent = new Intent(NotificationDetailsActivity.this,DownloadService.class);
					intent.setAction(DownloadService.ACTION_STOP);
					intent.putExtra(DownloadService.DOWNLOAD_INTENT_EXTRA, fileInfo);
					startService(intent);
				}
			});
            linearLayout.addView(startBtn);
            linearLayout.addView(stopBtn);
        }

        return linearLayout;
    }
    
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    		if(DownloadService.ACTION_UPDATE.equals(intent.getAction())){
    			int finished = intent.getIntExtra("finished", 0);
    			if(progressBar!=null)	progressBar.setProgress(finished);
    			if(showtv!=null) showtv.setText("downloading  "+finished+"%");
    			Log.i("test", "++++++++++++++++++++++++++++"+finished);
    		}
    	};
    };

}
