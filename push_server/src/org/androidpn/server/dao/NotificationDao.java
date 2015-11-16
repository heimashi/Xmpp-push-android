package org.androidpn.server.dao;

import org.androidpn.server.model.Notification;


public interface NotificationDao {
	
	void saveOrUpdateNotification(Notification notification);
	
	Notification getNotification(Long id);
	
	void deleteNotification(Notification notification);

	public Notification findNotificationByUuid(String uuid);	
}
