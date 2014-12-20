package DBUtils.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserDocServlet
 */
@WebServlet("/UserDocCacheServlet")
public class UserDocCacheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Map<String,UserDoc> UserDocMap = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserDocCacheServlet() {
		super();
		// TODO Auto-generated constructor stub
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserDoc userDoc=null;
		HttpSession session = request.getSession();
		User user=(User)session.getAttribute("user");
		int weight,frequency = 0;
		@SuppressWarnings("unchecked")
		Map<String,UserDoc>userDocMap=(Map<String, UserDoc>) getServletContext().getAttribute("UserDocMap");
		//Change the string inside getParameter according to your input field name
		String userId = user.getUserId();
		String articleId=request.getParameter("articleId");
		String categoryName=request.getParameter("categoryName");
		if(userDocMap==null || userDocMap.isEmpty())
		{
			userDocMap=new HashMap<String,UserDoc>();
			userDoc=new UserDoc();
			userDoc.setArticleId(articleId);
			userDoc.setUserId(userId);
			userDoc.setCategoryName(categoryName);
			userDoc.setWeight(1);
			userDocMap.put(articleId, userDoc);
		}else
		{
			userDoc=new UserDoc();
			userDoc.setArticleId(articleId);
			userDoc.setUserId(userId);
			userDoc.setCategoryName(categoryName);
			if(userDocMap.get(articleId)==null)
			{
				
				userDoc.setWeight(1);
				userDocMap.put(articleId, userDoc);
			}
			else
			{
				weight=userDocMap.get(articleId).getWeight();
				frequency=userDocMap.get(articleId).getCategoryFrequency();
				weight++;
				frequency++;
				userDoc.setWeight(weight);
				userDoc.setCategoryFrequency(frequency);
				userDocMap.put(articleId, userDoc);
			}
		}
		getServletContext().setAttribute("UserDocMap",userDocMap);
		/*	RequestDispatcher rd = getServletContext().getRequestDispatcher("/InsertDocServlet");
		rd.include(request, response);*/
	}

}
