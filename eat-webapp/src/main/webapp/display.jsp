<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %> 
<%@ page import="java.util.Map" %> 

<%@ page import="ub.cse636.project.Place" %>
<%@ page import="ub.cse636.project.UberProduct" %>
<%@ page import="ub.cse636.project.UberPrice" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

<title>Eat - Recommendation App</title>



<style>
table {
	font-size: 12px;
}

#map-canvas {
	width: 800px;
	height: 800px;
}

#listing {
	position: absolute;
	width: 200px;
	height: 470px;
	overflow: auto;
	left: 442px;
	top: 0px;
	cursor: pointer;
	overflow-x: hidden;
}
</style>

<style>
			.modal-content {
							width: 900px;
							margin-left: -150px;
						   }
	</style>

	<style>
      #directions-panel {
        height: 700px;
        float: right;
        width: 390px;
        overflow: auto;
      }

     #map-canvas-2 {
	width: 440px;
	height: 700px;
}

      #control {
        background: #fff;
        padding: 5px;
        font-size: 14px;
        font-family: Arial;
        border: 1px solid #ccc;
        box-shadow: 0 2px 2px rgba(33, 33, 33, 0.4);
        display: none;
      }
      
      .border-row {
    border-bottom: 1px solid #ccc;
}

.box-content {
    display: inline-block;
    width: 400px;
}
    </style>
    
    

<!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
	
	<script src="js/jquery-1.11.1.js"></script>
	<script src="js/jquery-2.1.1.js"></script>

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAC1HtOdf3NhBOzsbsFRs9NcZZa943wzZE">
    </script>	
	<% 	
		Place temp = new Place();
		Place show = new Place();
		UberProduct product = new UberProduct();
		ArrayList<Place> places = (ArrayList<Place>) request.getAttribute("places");
		ArrayList<UberProduct> products = (ArrayList<UberProduct>) request.getAttribute("products");
		Map<String,UberPrice> uberProductPriceEstMap = new HashMap<String,UberPrice>();
		Double latitude =  (Double) request.getAttribute("latitude");
		Double longitude =  (Double) request.getAttribute("longitude");
    %>

<script type = "text/javascript">
	
	var detail = [];
	var prodDetails = [];
	var mbr_index;
	var index;
	var index1;
	var radioValue;
	var travel;
	var productSize = <%= products.size() %>;
	var i;
	var lat = <%= latitude %>;
	var lng = <%= longitude %>;
	
	
	$(document).ready(function(){
	
		$('#myModal').on('show.bs.modal', function() {
			   //Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! 
			   
			   initializeDirection();
			   google.maps.event.trigger(map, "resize");
			   $('#tab2').html("<address> <strong>"+detail[0]+"</strong><br>"+detail[1]+"<br> Rating: "+ detail[2]+"</address>");
			   $('#tab3').empty();
			   $('#tab4').empty();
			   for(i=0; i<productSize; i++)
			   {
				     index = '#prod'+i;
				     prodDetails = $(index).html().split("|");
				     //$('#tab3').append("<div class='border-row'><div class='box-content left'><strong> Product Id: "+prodDetails[0]+"</strong><br> Type: "+prodDetails[1]+"<br> Description: "+prodDetails[2]+"<br> Capacity: "+prodDetails[3]+"<br>");
				     
					   //AJAX CALL STARTS
							$.ajax
							({
								type: "POST",
								url: "PriceServlet?sl="+lat+"&sln="+lng+"&dl="+detail[4]+"&dln="+detail[5]+"&pid="+prodDetails[0],
								dataType:"json",
									success: function(data)
									{
										if(data.Price.length)
										{
											$.each(data.Price, function(i,data)
											{
												//$('#tab3').append("<div class='border-row'><div class='box-content left'><strong> Product Id: "+prodDetails[0]+"</strong><br> Type: "+prodDetails[1]+"<br> Description: "+prodDetails[2]+"<br> Capacity: "+prodDetails[3]+"<br>");
												$('#tab3').append("<div class='border-row'><div class='box-content left'><strong> Type: "+prodDetails[1]+"</strong><br> Description: "+prodDetails[2]+"<br> Capacity: "+prodDetails[3]+"<br>Low Estimate: "+data.lowEstimate+" "+data.currencyCode+"<br> High Estimate: "+data.highEstimate+" "+data.currencyCode+"<br> Distance: "+data.distance+"</div><div class='box-content right'><img src="+prodDetails[4]+" class='pull-right'/></div></div>");
												//$('#tab3').append("</div><div class='box-content right'>"+"<img src="+prodDetails[4]+" class='pull-right'/></div></div>");
												//alert(data.productId+" "+data.displayName);
											});
										}
										else
										{
											$('#tab3').empty();
											$('#tab3').append("<div><h3>No Uber Service for this service</h3></div>");
										}
									},
							
								async:false
							});
					   
					   //AJAX CALL ENDS
			   }
			   
			   //Promotion AJAX Call
			   
			 				$.ajax
							({
								type: "POST",
								url: "PromotionServlet?sl="+lat+"&sln="+lng+"&dl="+detail[4]+"&dln="+detail[5],
								dataType:"json",
									success: function(data)
									{
										if(data.Promotions.length)
										{
												$('#tab4').append("<div class='border-row'><div class='box-content left'>Name: "+data.Promotions[0]+"<br> Value: "+data.Promotions[1]+"<br> Type: "+data.Promotions[2]+"</div></div>");
										}
									},
							
								async:false
							});	
			   
			   //Ends
			   
			   
			   
			}); 
		
		$('#myModal').on('shown.bs.modal', function() {
			google.maps.event.trigger(map, "resize");
		});
		
		var $items = $('#list li');
		$items.find('a').click(function () { 
		     mbr_index = $items.index($(this).parent()); // one based index
		     index = '#ind'+mbr_index;
		     detail = $(index).html().split("|");
		    		
		     $('#myModal').modal('show');
		     $("#res-name").text(detail[0]);
		     $('.nav-tabs a[href="#tab1').tab('show');
		     //alert(detail[0]);
		});
		
		$("input[type='radio']").click(function(){

        	radioValue = $("input[name='rt']:checked").val();
            if(radioValue){
            	initializeDirection();
            	google.maps.event.trigger(map, "resize");
            }
        });

});
	
	

	
	
// In the following example, markers appear when the user clicks on the map.
// The markers are stored in an array.
// The user can then click an option to hide, show or delete the markers.
var map;
var markers = [];
var directionsDisplay;
var directionsService = new google.maps.DirectionsService();
var a;
var b;

function initializeDirection() {
	 var lat = <%= latitude %>;
	 var lng = <%= longitude %>;
	 directionsDisplay = new google.maps.DirectionsRenderer();
	 a = new google.maps.LatLng(lat, lng);
	  b = new google.maps.LatLng(detail[4], detail[5]);
  var mapOptions = {
    zoom: 12,
    center: a
  };
  map = new google.maps.Map(document.getElementById('map-canvas-2'),
      mapOptions);

   directionsDisplay.setMap(map);
   document.getElementById('directions-panel').innerHTML = "";
   directionsDisplay.setPanel(document.getElementById('directions-panel'));

		  if( radioValue == "Transit" )
		  {
			travel = google.maps.TravelMode.TRANSIT;
		  }
		  else if(radioValue == "Walking")
		  {
			travel = google.maps.TravelMode.WALKING;
		  }
		  else if(radioValue == "Bicycling")
		  {
			travel = google.maps.TravelMode.BICYCLING;
		  }
		  else if(radioValue == "Driving" || radioValue == null)
		  {
			travel = google.maps.TravelMode.DRIVING;
		  }
  
 calcRoute(travel);
}

// Add a marker to the map and push to the array.
function addMarker(location) {
  var marker = new google.maps.Marker({
    position: location,
    map: map
  });
  markers.push(marker);
}

function calcRoute(travel) {
	  var request = {
	      origin: a,
	      destination: b,
	      // Note that Javascript allows us to access the constant
	      // using square brackets and a string value as its
	      // "property."
	      travelMode: travel,
	      
	      unitSystem: google.maps.UnitSystem.IMPERIAL
	     
	  };
	  directionsService.route(request, function(response, status) {
	    if (status == google.maps.DirectionsStatus.OK) {
	      directionsDisplay.setDirections(response);
	    }
	  });
	}




    </script>
	
<script>
	var map;
	var haightAshbury = [];
	function initialize() 
	{
		//alert("HI");	
		alert("Product size : "+<%= products.size() %>)
		haightAshbury[0] = new google.maps.LatLng(<%= places.get(0).getLattitude() %>, <%= places.get(0).getLongitude() %>); 
		var mapOptions = {
		          center: haightAshbury[0],
		          zoom: 12
		        };
		 map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
		 
		 
		 <%
		 for(int i = 0; i < places.size(); i++) 
		 {
			temp = places.get(i);
		 %>
			haightAshbury[<%= i %>] = new google.maps.LatLng(<%= temp.getLattitude() %>, <%= temp.getLongitude() %>);
			addMarker(haightAshbury[<%= i %>], <%= i %>);
		<%	 
		}
		 %>
	}
	
	function addMarker(location, count) {
  	  var marker = new google.maps.Marker({
  	    position: location,
  	    map: map, 
  	    icon: 'https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld='+(count+1)+'|FF776B|000000'
  	  });
  	}
	google.maps.event.addDomListener(window, 'load', initialize);
    </script>

<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>

</head>

<body style="margin:0px; padding:0px;">
 
  
  <div class="container">
    <div class="row">
        <div class="col-md-7" style="margin-top: 30px;">
		<div id="map-canvas">
		</div>
        </div>
        <div class="col-md-5">
        
        <%
		out.println("<p>Top Restaurants</p>");
		for(int i = 0; i < places.size(); i++) 
		{
			temp = places.get(i);
			
			out.println("<ul class='list-group' id='list'>");
			out.println("<li class='list-group-item' id=\""+i+"\" ><a id='openbtn"+i+"'>"+ temp.getName()+"</a></li>");
		}
		out.println("</ul>");

		for(int i = 0; i < places.size(); i++) 
		{
			temp = places.get(i);
			String details = temp.getName()+"|"+temp.getAddress()+"|"+temp.getRating()+"|"+temp.getReviewCount()+"|"+temp.getLattitude()+"|"+temp.getLongitude();
			out.println("<div id='ind"+i+"' style='display: none'>"+details+"</div> ");
		}
		
	    for(int i = 0; i < products.size(); i++) 
		{
			product = products.get(i);
			String prod = product.getProductId()+"|"+product.getDisplayName()+"|"+product.getDescription()+"|"+product.getCapacity()+"|"+product.getImageURL();
			System.out.println(prod);
			out.println("<div id='prod"+i+"' style='display: none' >"+prod+"</div> ");
		} 
		%>
		</div>
	</div>
</div> 

<!-- Pop up Menu Starts -->

<div class="modal" id="myModal">
	<div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="res-name"></h4>
        </div>
        <div class="modal-body">
        <div class="tabbable"> <!-- Only required for left/right tabs -->
        <ul class="nav nav-tabs">
        <li class="active"><a href="#tab1" data-toggle="tab">Route</a></li>
        <li><a href="#tab2" data-toggle="tab">About</a></li>
        <li><a href="#tab3" data-toggle="tab">Uber</a></li>
         <li><a href="#tab4" data-toggle="tab">Uber Promotions</a></li>
         <li><a href="#tab5" data-toggle="tab">NearBy Places</a></li>
        </ul>
        <div class="tab-content">
        <div class="tab-pane active" id="tab1">
			
			<div>
			<input type="radio" name="rt" value="Transit" />Transit
			<input type="radio" name="rt" checked="checked" value="Driving" />Driving
			<input type="radio" name="rt" value="Walking" />Walking
			<input type="radio" name="rt" value="Bicycling" />Bicycling
			</div>
			
            <div id="directions-panel" style="overflow: auto;"></div>
			<div id="map-canvas-2"></div>
        </div>
        <div class="tab-pane" id="tab2">
        	
        </div>
        <div class="tab-pane" id="tab3" style="overflow: auto;">
        	
        </div>
        <div class="tab-pane" id="tab4">
        	
        </div>
        <div class="tab-pane" id="tab5">
        	
        </div>
        </div>
        </div>
   </div>
    

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Scrolling Nav JavaScript -->
    <script src="js/jquery.easing.min.js"></script>
    

          
          
        <div class="modal-footer">
          <a href="#" data-dismiss="modal" class="btn">Close</a>
          
        </div>
      </div>
    </div>
</div>

<!--  ENds -->


</body>
</html>