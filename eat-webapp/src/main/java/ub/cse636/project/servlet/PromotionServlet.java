package ub.cse636.project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ub.cse636.project.UberPrice;
import ub.cse636.project.service.UberService;

import com.google.gson.Gson;

/**
 * Servlet implementation class PromotionServlet
 */
@WebServlet("/PromotionServlet")
public class PromotionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PromotionServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		String[] promotions;
		Gson gson = new Gson();
		
		Double sl = Double.parseDouble(request.getParameter("sl").toString()); 
		Double sln = Double.parseDouble(request.getParameter("sln").toString()); 
		Double dl = Double.parseDouble(request.getParameter("dl").toString()); 
		Double dln = Double.parseDouble(request.getParameter("dln").toString());
		//System.out.println("Price Servlet "+sl+" "+sln+" "+dl+" "+dln);
		
		promotions = UberService.uberPromotionsAPICall(sl, sln, dl, dln);
		
		String promotion = gson.toJson(promotions);
		
		//System.out.println(promotion);
		out.println("{\"Promotions\":"+promotion+"}");
	}

}
