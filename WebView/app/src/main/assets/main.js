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
	Weather.sendMessage("The weather in " + document.getElementById("title").innerText + " today is: " + document.getElementById("description").innerText);
}

function getData() {
	fetch("https://gcoleman799.github.io/Asset-Loader/weather.json").then(function(resp) {
		return resp.json();
	}).then(function(data) {
		var form = document.getElementById("location");
		var currentLocation = form.options[form.selectedIndex].value;
		console.log(currentLocation);
		document.getElementById("title").innerText = form.options[form.selectedIndex].text;
		if (currentLocation == "london") {
			document.getElementById("description").innerText = data.london.description;
			document.getElementById("icon").src = data.london.icon;
		} else if (currentLocation == "newYork") {
			document.getElementById("description").innerText = data.newYork.description;
			document.getElementById("icon").src = data.newYork.icon;
		} else {
			document.getElementById("description").innerText = data.sanFrancisco.description;
			document.getElementById("icon").src = data.sanFrancisco.icon;
		}

		//		switch (currentLocation) {
		//			case "newYork":
		//				document.getElementById("description").innerText = data.newYork.description;
		//				document.getElementById("icon").innerText = data.newYork.icon;
		//			case "london":
		//				document.getElementById("description").innerText = data.london.description;
		//              document.getElementById("icon").innerText = data.london.icon;
		//			case "sanFrancisco":
		//				document.getElementById("description").innerText = data.sanFrancisco.description;
	    //                document.getElementById("icon").innerText = data.sanFrancisco.icon;
		//		}
	})
}

function postMessage(myObject) {
	myObject.onmessage = function(event) {
		// prints "Got it!" when we receive the app's response.
		console.log("Hello");
	}
	myObject.postMessage("I'm ready!");
	//does this need more parameters?
}