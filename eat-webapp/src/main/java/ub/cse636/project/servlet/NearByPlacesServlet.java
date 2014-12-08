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

import ub.cse636.project.Place;
import ub.cse636.project.UberPrice;
import ub.cse636.project.service.PlacesService;
import ub.cse636.project.service.UberService;

import com.google.gson.Gson;

/**
 * Servlet implementation class NearByPlacesServlet
 */
@WebServlet("/NearByPlacesServlet")
public class NearByPlacesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NearByPlacesServlet() {
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
		Gson gson = new Gson();
		ArrayList<Place> result = new ArrayList<Place>();
		ArrayList<String> typeList = new ArrayList<String>();
		
		Double dl = Double.parseDouble(request.getParameter("dl").toString()); 
		Double dln = Double.parseDouble(request.getParameter("dln").toString());
		int radius = Integer.parseInt(request.getParameter("radius").toString());
		String type = request.getParameter("type").toString();
		String[] temp;
		
		if(type.contains("|"))
		{
			temp = type.split("|");
			for(String x : temp)
			{
					typeList.add(x);
			}
			result = PlacesService.nearbySearchAPICall(dl, dln, radius, typeList);
		}
		else
			result = PlacesService.nearbySearchAPICall(dl, dln, radius);
		
		
		System.out.println("NearByPlaces "+result.size());
		for(Place t : result)
		{
			System.out.println(t.getName());
			
		}
		String message = gson.toJson(result);
		out.println("{\"NearByPlaces\":"+message+"}");
	}

}
