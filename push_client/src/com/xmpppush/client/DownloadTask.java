package com.xmpppush.client;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.content.Intent;

public class DownloadTask {

	private Context mContext = null;

	private FileInfo mFileInfo = null;
	
	private ThreadDAO mDao =null;
	
	private int mFinished = 0;
	
	private boolean isPause = false;

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	public DownloadTask(Context context, FileInfo fileInfo) {
		super();
		this.mContext = context;
		this.mFileInfo = fileInfo;
		mDao = new ThreadDAOImpl(mContext);
	}
	
	public void download(){
		List<ThreadInfo> threadInfos = mDao.getThreadInfos(mFileInfo.getUrl());
		ThreadInfo threadInfo;
		if(threadInfos==null||threadInfos.size()==0){
			threadInfo = new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),0);
		}else {
			threadInfo = threadInfos.get(0);
		}
		new DownloadThread(threadInfo).start();
	}

	class DownloadThread extends Thread{
		
		private ThreadInfo mThreadInfo = null;

		public DownloadThread(ThreadInfo mThreadInfo) {
			super();
			this.mThreadInfo = mThreadInfo;
		}
		
		@SuppressWarnings("resource")
		@Override
		public void run() {
			if(!mDao.isExist(mThreadInfo.getUrl(), mThreadInfo.getId())){
				mDao.insertThread(mThreadInfo);
			}
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			InputStream inputStream = null;
			try{
				URL url = new URL(mThreadInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				//download range
				int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
				conn.setRequestProperty("Range", "bytes="+start+"-"+mThreadInfo.getEnd());
				//write range
				File file = new File(DownloadService.DOWNLOAD_PATH,mFileInfo.getFilename());
				raf = new RandomAccessFile(file,"rwd");
				raf.seek(start);
				//start download
				Intent intent = new Intent(DownloadService.ACTION_UPDATE);
				mFinished += mThreadInfo.getFinished();
				if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT){
					inputStream = conn.getInputStream();
					byte[] buffer = new byte[1024*4];
					int len = -1;
					long time = System.currentTimeMillis();
					while((len=inputStream.read(buffer))!=-1){
						raf.write(buffer,0,len);
						mFinished +=len;
						if(System.currentTimeMillis()-time>500){
							time = System.currentTimeMillis();
							intent.putExtra("finished", mFinished*100/mFileInfo.getLength());
							mContext.sendBroadcast(intent);
						}
						if(isPause){
							mDao.upadteThread(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
							return;
						}
					}
					mDao.deleteThread(mThreadInfo.getUrl(), mThreadInfo.getId());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
					try {
						if(inputStream!=null){
							inputStream.close();
						}
						if(raf!=null){
							raf.close();
						}
						if(conn!=null){
							conn.disconnect();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			
		}
	}
	
}
