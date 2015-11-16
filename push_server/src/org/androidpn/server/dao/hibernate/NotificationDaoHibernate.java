package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.NotificationDao;
import org.androidpn.server.model.Notification;
import org.androidpn.server.model.User;
import org.androidpn.server.service.UserNotFoundException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class NotificationDaoHibernate extends HibernateDaoSupport implements
		NotificationDao {

	public void saveOrUpdateNotification(Notification notification) {
		getHibernateTemplate().saveOrUpdate(notification);
		getHibernateTemplate().flush();
	}

	public void deleteNotification(Notification notification) {
		getHibernateTemplate().delete(notification);
	}


	public Notification getNotification(Long id) {
		return (Notification) getHibernateTemplate().get(Notification.class, id);
	}

	public Notification findNotificationByUuid(String uuid) {
		List notifications = getHibernateTemplate().find("from Notification where uuid=?",
				uuid);
		if (notifications == null || notifications.isEmpty()) {
			return null;
		} else {
			return (Notification) notifications.get(0);
		}
	}


	
}
