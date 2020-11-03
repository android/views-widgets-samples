Android RecyclerView Sample (Kotlin)
====================================

This application implements a RecyclerView in Kotlin with ListAdapter, onClickListener 
and Headers. If you are looking for a simpler sample, look at the RecyclerViewSimple sample
in the directory.


Introduction
------------

Sample demonstrating the use of [RecyclerView][1] to layout elements with a
[LinearLayoutManager][2].

[RecyclerView][1] can display large datasets that can be scrolled
efficiently by recycling a limited number of views. [ListAdapter][3] is used to 
efficiently compute diffs when items are added/removed from the list. Click listeners can be
defined when [ViewHolder][4] views are instantiated. 


[1]: https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView
[2]: https://developer.android.com/reference/androidx/recyclerview/widget/LinearLayoutManager
[3]: https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
[4]: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder

Pre-requisites
--------------

- Android SDK 27
- Android Gradle Plugin 3.0
- Android Support Repository

Screenshots
-------------

![image](https://user-images.githubusercontent.com/46006059/98028846-8b6df700-1dc3-11eb-9f0b-ad93569be189.png)

Getting Started
---------------

To build this project, use "Import Project" in Android Studio.

Support
-------

- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/android/views-widgets

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.