package com.xmpppush.client;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class DownloadService extends Service {

	
	public static final String ACTION_START = "action_start";
	public static final String ACTION_STOP = "action_stop";
	public static final String ACTION_UPDATE = "action_update";
	public static final String DOWNLOAD_INTENT_EXTRA = "download_intent_extra";
	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/";
	private static final int MSG_INIT = 1;
	private DownloadTask mDownloadTask = null;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(ACTION_START.equals(intent.getAction())){
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(DOWNLOAD_INTENT_EXTRA);
			new InitThread(fileInfo).start();
		}else if(ACTION_STOP.equals(intent.getAction())){
			//FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(DOWNLOAD_INTENT_EXTRA);
			if(mDownloadTask!=null){
				mDownloadTask.setPause(true);
			}
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	Handler handler = new MyHandler(this);
	
	static class MyHandler extends Handler{
		
		private WeakReference<Service> mService;
		
		public MyHandler(Service service) {
			mService = new WeakReference<Service>(service);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(mService.get()==null||!(mService.get() instanceof DownloadService)) return;
			DownloadService ref = (DownloadService)mService.get();
			switch (msg.what) {
			case MSG_INIT:
				FileInfo fileInfo = (FileInfo) msg.obj;
				ref.mDownloadTask = new DownloadTask(ref, fileInfo);
				ref.mDownloadTask.download();
				break;
			default:
				break;
			}
		}
	}
	

	class InitThread extends Thread{
		
		private FileInfo fileInfo;
		
		public InitThread(FileInfo fileInfo){
			this.fileInfo=fileInfo;
		}
		
		@Override
		public void run() {
			super.run();
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			try {
				URL url = new URL(fileInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				int length = -1;
				if(conn.getResponseCode() == HttpStatus.SC_OK){
					length = conn.getContentLength();
				}
				if(length<=0) return;
				File dir = new File(DOWNLOAD_PATH);
				if(!dir.exists()){
					dir.mkdir();
				}
				File file = new File(dir,fileInfo.getFilename());
				raf = new RandomAccessFile(file, "rwd");
				raf.setLength(length);
				fileInfo.setLength(length);
				handler.obtainMessage(MSG_INIT,fileInfo).sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(raf!=null){
						raf.close();
					}
					if(conn!=null){
						conn.disconnect();
						conn=null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
