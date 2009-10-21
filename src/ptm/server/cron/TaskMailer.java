package ptm.server.cron;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ptm.server.main.QueryManager;

@SuppressWarnings("serial")
public class TaskMailer extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException{
		QueryManager.mailTasks();
		
	}
}
