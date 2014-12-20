package DBUtils.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.DBUtilException;
import util.DBUtils;

/**
 * Servlet implementation class InsertDocServlet
 */
@WebServlet("/InsertDocServlet")
public class InsertDocServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Map<String,UserDoc> UserDocMap = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InsertDocServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//To be called during logout so that the map constructed for the user can get inserted into DB
		boolean successFlag=false;
		User userMetadata = (User) request.getSession().getAttribute("user");
		String userId = userMetadata.getUserId();
		UserDocMap=((Map<String, UserDoc>) getServletContext().getAttribute("UserDocMap"));
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		DBUtils dbUtils = null;
		try {
			dbUtils = new DBUtils(con);

			successFlag=dbUtils.InsertUserDocMapInDB(UserDocMap,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
