package com.sl.emailRegistration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sl.dao.UserDAO;
import com.sl.db.DBException;
import com.sl.model.StatusPojo;
import com.sl.model.UserPojo;
import com.sl.util.BCrypt;
import com.sl.util.GlobalConstants;
import com.sl.util.Utils;
/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String userId = (String) request.getSession().getAttribute(GlobalConstants.USER);
    	if(userId != null) {
    		request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);	
    	} else {
    		request.getRequestDispatcher("/index.html").forward(request, response);	
    	}
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inputEmail = request.getParameter("inputEmail");
		String inputPassword = BCrypt.hashpw(request.getParameter("inputPassword"), GlobalConstants.SALT);
		StatusPojo sp = new StatusPojo();
		LOGGER.debug(inputEmail);
		try {
			UserPojo up = UserDAO.verifyLogin(inputEmail, inputPassword);
			if(up != null) {
				if(up.getSTATUS().equals(GlobalConstants.ACTIVE) || up.getSTATUS().equals(GlobalConstants.IN_RESET_PASSWORD)) {
					request.getSession().setAttribute(GlobalConstants.USER, up.getUSER_ID());
					request.getSession().setAttribute(GlobalConstants.USER_NAME, up.getFIRST_NAME()+" "+up.getLAST_NAME());
					sp.setCode(0);
					sp.setMessage("Success");	
				} else if(up.getSTATUS().equals(GlobalConstants.NEW)){
					sp.setCode(-1);
					sp.setMessage("Account activation is in pending");
				} else {
					sp.setCode(-1);
					sp.setMessage("Unknown error");
				}
				
			} else {
				sp.setCode(-1);
				sp.setMessage("Email or Password is not valid");				
			}
		} catch (DBException e) {
			LOGGER.debug(e.getMessage());
			sp.setCode(-1);
			sp.setMessage(e.getMessage());
		}
		PrintWriter pw = response.getWriter();
		pw.write(Utils.toJson(sp));
		pw.flush();
		pw.close();
	}

}
