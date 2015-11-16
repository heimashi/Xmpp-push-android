package org.androidpn.server.xmpp.handler;

import org.androidpn.server.model.Notification;
import org.androidpn.server.model.User;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.UnauthorizedException;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.dom4j.Element;
import org.xmpp.packet.IQ;
import org.xmpp.packet.PacketError;

public class IQNotificationReceivedHandler extends IQHandler {

	private static final String NAMESPACE = "pushserver:iq:notification_received";

	private NotificationService notificationService;
	
	private UserService userService;
	
	public IQNotificationReceivedHandler() {
		notificationService = ServiceLocator.getNotificationService();
		userService = ServiceLocator.getUserService();
	}
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ reply = null;

		ClientSession session = sessionManager.getSession(packet.getFrom());
		if (session == null) {
			log.error("Session not found for key " + packet.getFrom());
			reply = IQ.createResultIQ(packet);
			reply.setChildElement(packet.getChildElement().createCopy());
			reply.setError(PacketError.Condition.internal_server_error);
			return reply;
		}

		if (IQ.Type.set.equals(packet.getType())) {
			if (session.getStatus() == Session.STATUS_AUTHENTICATED) {
				Element element = packet.getChildElement();
				String uuid = element.elementText("uuid");
				Notification notification= notificationService.findNotificationByUuid(uuid);
				User user;
				try {
					user = userService.getUserByUsername(session.getUsername());
					if(notification != null&&user!=null){
						notification.getUsers().remove(user);
						if(notification.getUsers()==null||notification.getUsers().size()==0){
                			notificationService.deleteNotification(notification);
                		}else {
							notificationService.saveOrUpdateNotification(notification);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		return null;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

}
