package com.xmpppush.client;

import java.util.List;


public interface ThreadDAO {

	public void insertThread(ThreadInfo threadInfo);
	
	public void deleteThread(String url, int thread_id);
	
	public void upadteThread(String url, int thread_id, int finished);
	
	public List<ThreadInfo> getThreadInfos(String url);
	
	public boolean isExist(String url, int thread_id);
	
}
