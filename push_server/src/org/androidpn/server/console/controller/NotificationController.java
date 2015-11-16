package org.androidpn.server.console.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/** 
 * A controller class to process the notification related requests.  
 *
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;

    public NotificationController() {
        notificationManager = new NotificationManager();
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        mav.setViewName("notification/form");
        return mav;
    }

    public ModelAndView send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String broadcast = null;
    	//ServletRequestUtils.getStringParameter(request,
//                "broadcast", "0");
        String username = null;
        String alias = null;
        String title = null;
        String message = null;
        String uri = null;

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        List<FileItem> fileItems = servletFileUpload.parseRequest(request);
        for(FileItem item : fileItems){
        	if("broadcast".equals(item.getFieldName())){
        		broadcast = item.getString("utf-8");
        	}else if("username".equals(item.getFieldName())){
        		username = item.getString("utf-8");
        	}else if("alias".equals(item.getFieldName())){
        		alias = item.getString("utf-8");
        	}else if("title".equals(item.getFieldName())){
        		title = item.getString("utf-8");
        	}else if("message".equals(item.getFieldName())){
        		message = item.getString("utf-8");
        	}else if("uri".equals(item.getFieldName())){
        		uri = uploadFile(request, item);//item.getString("utf-8");
        	}
        }
        
        

        if (broadcast.equalsIgnoreCase("0")) {
            notificationManager.sendBroadcast(apiKey, title, message, uri);
        } else if (broadcast.equalsIgnoreCase("1")){
            notificationManager.sendNotifcationToUser(apiKey, username, title,
                    message, uri);
        } else if (broadcast.equalsIgnoreCase("2")){
        	notificationManager.sendNotifcationToUserByAlias(apiKey, alias, title, message, uri);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do");
        return mav;
    }

    private String uploadFile(HttpServletRequest request, FileItem fileItem) throws Exception{
    	String uploadPath = getServletContext().getRealPath("/upload");
    	File uploadDir = new File(uploadPath);
    	if(!uploadDir.exists()){
    		uploadDir.mkdir();
    	}
    	if(fileItem!=null&&fileItem.getName().length()>0){//&&fileItem.getContentType().startsWith("image")
    		String suffix = fileItem.getName().substring(fileItem.getName().indexOf("."));
    		String fileName = System.currentTimeMillis()+suffix;
    		InputStream inputStream = fileItem.getInputStream();
    		FileOutputStream fos = new FileOutputStream(uploadDir+"/"+fileName);
    		byte[] b = new byte[1024];
    		int len = 0;
    		while((len=inputStream.read(b))>0){
    			fos.write(b, 0, len);
    			fos.flush();
    		}
    		fos.close();
    		inputStream.close();
    		String serverName = request.getServerName();//"10.0.209.99";
    		int serverPort = request.getServerPort();
    		String fileUrl = "http://"+serverName+":"+serverPort+"/upload/"+fileName;
    		System.out.println(fileUrl);
    		return fileUrl;
    	}
    	return "";
    }
    
}
