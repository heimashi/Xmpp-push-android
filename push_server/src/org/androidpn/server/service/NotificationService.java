package org.androidpn.server.service;

import org.androidpn.server.model.Notification;


public interface NotificationService {
	
	void saveOrUpdateNotification(Notification notification);
	
	public Notification findNotificationByUuid(String uuid);
	
	Notification getNotification(Long id);
	
	void deleteNotification(Notification notification);
	
}
