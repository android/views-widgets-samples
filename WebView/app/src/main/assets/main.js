/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function sendAndroidMessage() {
	Weather.sendMessage("The weather in " + document.getElementById("title").innerText +
	" today is: " + document.getElementById("shortDescription").innerText + " and " +
	document.getElementById("currentTemp").innerText);
}

function getData() {
	fetch("https://gcoleman799.github.io/Asset-Loader/weather.json").then(function(resp) {
		return resp.json();
	}).then(function(data) {
		var form = document.getElementById("location");
		var currentLocation = form.options[form.selectedIndex].value;
		document.getElementById("title").innerText = form.options[form.selectedIndex].text;
		if (currentLocation == "london") {
			document.getElementById("currentTemp").innerText = data.london.currentTemp;
			document.getElementById("shortDescription").innerText = data.london.description;
			document.getElementById("longDescription").innerText = "Today in London there is a " +
			data.london.chancePrecip + " chance of precipitation and the humidity is  " +
			data.london.humidity + ".";
			document.getElementById("icon").src = data.london.icon;
		} else if (currentLocation == "newYork") {
		    document.getElementById("currentTemp").innerText = data.newYork.currentTemp;
			document.getElementById("shortDescription").innerText = data.newYork.description;
			document.getElementById("longDescription").innerText = "Today in New York there is a " +
			data.newYork.chancePrecip + " chance of precipitation and the humidity is  " +
			data.newYork.humidity+ ".";
			document.getElementById("icon").src = data.newYork.icon;
		} else {
		    document.getElementById("currentTemp").innerText = data.sanFrancisco.currentTemp;
			document.getElementById("shortDescription").innerText = data.sanFrancisco.description;
			document.getElementById("longDescription").innerText = "Today in San Francisco there is a " +
			data.sanFrancisco.chancePrecip + " chance of precipitation and the humidity is  "  +
			data.sanFrancisco.humidity + ".";
			document.getElementById("icon").src = data.sanFrancisco.icon;
		}
	})
}