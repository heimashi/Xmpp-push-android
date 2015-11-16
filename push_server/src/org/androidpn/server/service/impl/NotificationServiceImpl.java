package org.androidpn.server.service.impl;

import org.androidpn.server.dao.NotificationDao;
import org.androidpn.server.model.Notification;
import org.androidpn.server.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {
	
	private NotificationDao notificationDao;
	
	public NotificationDao getNotificationDao() {
		return notificationDao;
	}

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void saveOrUpdateNotification(Notification notification) {
		notificationDao.saveOrUpdateNotification(notification);
	}

	public Notification getNotification(Long id) {
		return notificationDao.getNotification(id);
	}

	public void deleteNotification(Notification notification) {
		notificationDao.deleteNotification(notification);
	}

	public Notification findNotificationByUuid(String uuid) {
		return notificationDao.findNotificationByUuid(uuid);
	}

}
