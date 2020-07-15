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
    /* In this implementation, only the single-arg version of postMessage is supported. As noted
     * in the WebViewCompat reference doc, the second parameter, MessagePorts, is optional.
     * Also note that onmessage, addEventListener and removeEventListener are not supported.
     */
	jsObject.postMessage("The weather in " + `${document.getElementById("title").innerText}` + " today is " +
	`${document.getElementById("shortDescription").innerText} `);
}

function getData() {
   	fetch("https://raw.githubusercontent.com/gcoleman799/Asset-Loader/master/weather.json").then(function(resp) {
		return resp.json();
	}).then(function(data) {
		var form = document.getElementById("location");
		var currentLocation = form.options[form.selectedIndex].value;
		console.log(data[currentLocation].description);
		document.getElementById("title").innerText = form.options[form.selectedIndex].text;
        document.getElementById("currentTemp").innerText = `${data[currentLocation].currentTemp}`+ "\xB0 F";
        document.getElementById("shortDescription").innerText = data[currentLocation].description;
        document.getElementById("longDescription").innerText = "Today in " + `${form.options[form.selectedIndex].text}`
            + " there is a " + `${data[currentLocation].chancePrecip}` + "% chance of precipitation and the humidity is "
            + `${data[currentLocation].humidity}` + "%.";
        document.getElementById("icon").src = getIcon(data[currentLocation].description);
	})
}

function getIcon(description){
    switch(description) {
        case "Rainy":
            return "https://raw.githubusercontent.com/res/drawable/rain.png";
        case "Clear Sky":
            return "https://raw.githubusercontent.com/res/drawable/sunny.png";
        default:
            return "https://raw.githubusercontent.com/res/drawable/partly_cloudy.png";
    }
}