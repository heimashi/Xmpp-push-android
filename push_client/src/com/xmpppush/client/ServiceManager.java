package com.xmpppush.client;

import java.util.Properties;

import org.jivesoftware.smack.packet.IQ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

public final class ServiceManager {

    private static final String LOGTAG = CommonUtil
            .makeLogTag(ServiceManager.class);

    private Context context;

    private SharedPreferences sharedPrefs;

    private Properties props;

    private String version = "0.5.0";

    private String apiKey;

    private String xmppHost;

    private String xmppPort;

    private String callbackActivityPackageName;

    private String callbackActivityClassName;

    public ServiceManager(Context context) {
        this.context = context;

        if (context instanceof Activity) {
            Log.i(LOGTAG, "Callback Activity...");
            Activity callbackActivity = (Activity) context;
            callbackActivityPackageName = callbackActivity.getPackageName();
            callbackActivityClassName = callbackActivity.getClass().getName();
        }

        //        apiKey = getMetaDataValue("ANDROIDPN_API_KEY");
        //        Log.i(LOGTAG, "apiKey=" + apiKey);
        //        //        if (apiKey == null) {
        //        //            Log.e(LOGTAG, "Please set the androidpn api key in the manifest file.");
        //        //            throw new RuntimeException();
        //        //        }

        props = loadProperties();
        apiKey = props.getProperty("apiKey", "");
        xmppHost = props.getProperty("xmppHost", "127.0.0.1");
        xmppPort = props.getProperty("xmppPort", "5222");
        Log.i(LOGTAG, "apiKey=" + apiKey);
        Log.i(LOGTAG, "xmppHost=" + xmppHost);
        Log.i(LOGTAG, "xmppPort=" + xmppPort);

        sharedPrefs = context.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.API_KEY, apiKey);
        editor.putString(Constants.VERSION, version);
        editor.putString(Constants.XMPP_HOST, xmppHost);
        editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
        editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
                callbackActivityPackageName);
        editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
                callbackActivityClassName);
        editor.commit();
        // Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
    }

    public void startService() {
        Intent intent = NotificationService.getIntent();
        context.startService(intent);
    }

    public void stopService() {
        Intent intent = NotificationService.getIntent();
        context.stopService(intent);
    }

    
    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier("androidpn", "raw",
                    context.getPackageName());
            props.load(context.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
            e.printStackTrace();
        }
        return props;
    }


    public void setNotificationIcon(int iconId) {
        Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.NOTIFICATION_ICON, iconId);
        editor.commit();
    }

    public static void viewNotificationSettings(Context context) {
        Intent intent = new Intent().setClass(context,
                NotificationSettingsActivity.class);
        context.startActivity(intent);
    }
    
    public void setAlias(String alias){
    	String username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
    	if(TextUtils.isEmpty(username)||TextUtils.isEmpty(alias)) return;
    	SetAliasIQ setAliasIQ = new SetAliasIQ();
    	setAliasIQ.setType(IQ.Type.SET);
    	setAliasIQ.setUsename(username);
    	setAliasIQ.setAlias(alias);
    	
    }
    
}
