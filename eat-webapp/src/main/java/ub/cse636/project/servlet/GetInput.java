package ub.cse636.project.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ub.cse636.project.Place;
import ub.cse636.project.UberProduct;
import ub.cse636.project.service.UberService;
import ub.cse636.project.service.YelpApiUtil;

/**
 * Servlet implementation class GetInput
 */
@WebServlet("/GetInput")
public class GetInput extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInput() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String input = request.getParameter("query");
		String coord = request.getParameter("coordName");
		
		String currentPlace = request.getParameter("coordPlace");
		String place = request.getParameter("inpPlace");
		if( place != "")
			currentPlace = place;
		System.out.println("GET INPUT "+input+" "+coord+" "+currentPlace);
		
		YelpApiUtil yelp = new YelpApiUtil(input, currentPlace);
		yelp.start();
		//uberService.uberProductSearchAPICall(lat, lng);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String input = request.getParameter("query");
		String coord = request.getParameter("coordName").toString();
		
		char[] temp = coord.toCharArray();
		StringBuilder res = new StringBuilder();
		
		String[] coordinates = new String[2];
		for(int i=0; i<temp.length; i++)
		{
			if(temp[i] != '|')
			{
				res.append(temp[i]);
			}
			else
			{
				coordinates[0] = res.toString().trim();
				res = new StringBuilder();
			}
		}
		coordinates[1] = res.toString().trim();
		
		//System.out.println("GET INPUT "+input+" "+coord+" "+coordinates[0]+" "+coordinates[1]);
		Double lat = Double.parseDouble(coordinates[0]);
		Double lng = Double.parseDouble(coordinates[1]);
		
		System.out.println("GET INPUT "+input+" "+coord+" "+lat+" "+lng);
		
		String currentPlace = request.getParameter("coordPlace");
		String place = request.getParameter("inpPlace");
		
		if( place != "")
			currentPlace = place;

		YelpApiUtil yelp = new YelpApiUtil(input, currentPlace);
		ArrayList<Place> places = yelp.start();
		request.setAttribute("places", places);
		
		ArrayList<UberProduct> products = UberService.uberProductSearchAPICall(lat, lng);
		request.setAttribute("products", products);
		
		request.setAttribute("latitude", lat);
		request.setAttribute("longitude", lng);
		
		RequestDispatcher rd = request.getRequestDispatcher("/display.jsp");
		System.out.println(request.getContextPath());
		rd.forward(request, response);
	}

}
