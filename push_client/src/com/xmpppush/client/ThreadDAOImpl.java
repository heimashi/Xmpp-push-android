package com.xmpppush.client;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ThreadDAOImpl implements ThreadDAO {

	private DBHelper mHelper = null;
	
	public ThreadDAOImpl(Context context) {
		mHelper = new DBHelper(context);
	}
	
	@Override
	public void insertThread(ThreadInfo threadInfo) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
				new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),
				threadInfo.getEnd(),threadInfo.getFinished()});
		db.close();
	}

	@Override
	public void deleteThread(String url, int thread_id) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("delete from thread_info where url = ? and thread_id = ?",
				new Object[]{url,thread_id});
		db.close();
	}

	@Override
	public void upadteThread(String url, int thread_id, int finished) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
				new Object[]{finished,url,thread_id});
		db.close();
	}

	@Override
	public List<ThreadInfo> getThreadInfos(String url) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor= db.rawQuery("select * from thread_info where url = ?", new String[]{url});
		List<ThreadInfo>  list = new ArrayList<ThreadInfo>();
		try {
			while(cursor.moveToNext()){
				ThreadInfo threadInfo = new ThreadInfo();
				threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
				threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
				threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
				threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
				threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
				list.add(threadInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			if(db!=null){
				db.close();
			}
		}
		return list;
	}

	@Override
	public boolean isExist(String url, int thread_id) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		Cursor cursor= db.rawQuery("select * from thread_info where url = ? and thread_id = ?", new String[]{url,thread_id+""});
		boolean exist = cursor.moveToNext();
		if(cursor!=null){
			cursor.close();
		}
		if(db!=null){
			db.close();
		}
		return exist;
	}

}
