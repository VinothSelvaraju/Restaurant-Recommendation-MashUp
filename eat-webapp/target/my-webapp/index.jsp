<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<!-- This file has been downloaded from Bootsnipp.com. Enjoy! -->
<title>Eat - Restaurant Recommendation app</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places"></script>
<style>
 #autocomplete {
        width: 100%;
      }
</style>

    <script>
var geocoder;
var map;
var infowindow = new google.maps.InfoWindow();
var marker;


//Getting current coordinates

var x;
var latitude;
var longitude;
var place;

function getLocation() {
	 x = document.getElementById("coord");
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else { 
        alert("Geolocation is not supported by this browser.");
    }
}

function showPosition(position) {
	//alert("Latitude: " + position.coords.latitude + " Longitude: " + position.coords.longitude);
	x.value = position.coords.latitude +"|" + position.coords.longitude;
	latitude = position.coords.latitude;
	longitude = position.coords.longitude;
	/*place = document.getElementById("inpPlace").value;
	 alert(place);
	 if(place != "")
	 {
		 x = document.getElementById("coordP");
		 x.value = place;
	 }
	 else */
		 codeLatLng();
}

//end


function initialize() {
  geocoder = new google.maps.Geocoder();
 
  //Start
  
var places;
var autocomplete;
var countryRestrict = { 'country': 'us' };

 
  autocomplete = new google.maps.places.Autocomplete(
      /** @type {HTMLInputElement} */(document.getElementById('autocomplete')),
      {
        types: ['(cities)'],
        componentRestrictions: countryRestrict
      });
  google.maps.event.addListener(autocomplete, 'place_changed', onPlaceChanged);


function onPlaceChanged() {
  var place = autocomplete.getPlace();
  if (place.geometry) {
  } else {
    document.getElementById('autocomplete').placeholder = 'Enter a city';
  }
}


function setAutocompleteCountry() {
  var country = document.getElementById('country').value;
  if (country == 'all') {
    autocomplete.setComponentRestrictions([]); 
  } else {
    autocomplete.setComponentRestrictions({ 'country': country });
  }
}
  
  //End
  
}

function codeLatLng() {
  //var input = document.getElementById('latlng').value;
  //var latlngStr = input.split(',', 2);
  var lat = parseFloat(latitude);
  var lng = parseFloat(longitude);
  var latlng = new google.maps.LatLng(lat, lng);
  x = document.getElementById("coordP");
  geocoder.geocode({'latLng': latlng}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
    	
      if (results[1]) {
               var result=(results[1].formatted_address).split(',',4);
               x.value = result[1]+", "+result[2];
        infowindow.setContent(result[1]+", "+result[2]);
        infowindow.open(map, marker);
      } else {
        alert('No results found');
      }
    } else {
      alert('Geocoder failed due to: ' + status);
    }
  });
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
    




<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
</head>

<body style="margin: 0 auto; diplay: inline-block">

<form action="GetInput" method="post">
	<div class="container" style="text-align: center; margin-top: 90px;">
		<div class="row" align="center">
			<p>
				<img src="./images/eat.png" class="img-rounded"> <br /> <sub>Restaurant
					and Taxi recommendation System</sub>
			</p>
			<br />
			<div class="form-group" style="display: inline;">
				<div class="input-group col-lg-6">
					<input type="text" class="form-control"  name="query" placeholder="search" onfocus="getLocation()"/>
					
				</div>

				<h3>in</h3>
				<div class="input-group col-lg-6">
					<input type="text" class="form-control input-sm" id = "autocomplete" name="inpPlace" placeholder="Enter a city" />
				</div>
				<input type="hidden" id="coord" name="coordName" value=""/>
				<input type="hidden" id="coordP" name="coordPlace" value=""/>
				<br/>
				<input type="submit" value="submit"/>
			</div>
		</div>
	</div>
</form>
</body>
</html>
