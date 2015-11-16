package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.presence.PresenceManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class UserController extends MultiActionController {

    private UserService userService;

    public UserController() {
        userService = ServiceLocator.getUserService();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PresenceManager presenceManager = new PresenceManager();
        List<User> userList = userService.getUsers();
        for (User user : userList) {
            if (presenceManager.isAvailable(user)) {
                // Presence presence = presenceManager.getPresence(user);
                user.setOnline(true);
            } else {
                user.setOnline(false);
            }
            // logger.debug("user.online=" + user.isOnline());
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("userList", userList);
        mav.setViewName("user/list");
        return mav;
    }

}
