
Android RecyclerView Sample (Kotlin)
====================================

Sample demonstrating the use of RecyclerView to layout elements with a
LinearLayoutManager and with a GridLayoutManager. It also demonstrates
how to handle touch events on elements.


Introduction
------------

Sample demonstrating the use of [RecyclerView][1] to layout elements with a
[LinearLayoutManager][2] or with a [GridLayoutManager][3].

[RecyclerView][1] can display large datasets that can be scrolled
efficiently by recycling a limited number of views. Click listeners can be
defined when [ViewHolder][4] views are instantiated. [RecyclerView][1] is
available in the v7 Support Library, thus compatible with API level 7 and above.

Tap "Show Log" menu item to display log of elements as they are laid out and
tapped. Use radio buttons to toggle between [LinearLayoutManager][2] and
[GridLayoutManager][3].

[1]: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
[2]: https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager.html
[3]: https://developer.android.com/reference/android/support/v7/widget/GridLayoutManager.html
[4]: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html

Pre-requisites
--------------

- Android SDK 27
- Android Gradle Plugin 3.0
- Android Support Repository

Screenshots
-------------

<img src="screenshots/1-linear.png" height="400" alt="Screenshot"/> <img src="screenshots/2-grid.png" height="400" alt="Screenshot"/>

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------

- Google+ Community: https://plus.google.com/communities/105153134372062985968
- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/googlesamples/android-RecyclerView

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.

License
-------

Copyright 2017 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
