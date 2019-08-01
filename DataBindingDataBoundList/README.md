** This is a sample application that shows how data binding can be used with Lists **
It has a single list that can be updated by buttons at the top and bottom of the
screen. Instead of being a RecyclerView, the list is a LinearLayout containing
items. The BindingAdapter tracks the list and updates the Views inside
the ViewGroup.

This type of data binding is useful for ViewGroups containing a small number of
Views that should be shown on the screen at the same time. If you expect scrolling
of your list, you should use a RecyclerView instead.

The [ListBindingAdapters](https://github.com/google/android-ui-toolkit-demos/blob/master/DataBinding/DataBoundList/app/src/main/java/com/example/android/databoundlist/ListBindingAdapters.java)
class contains the BindingAdapter that ties lists to ViewGroups.  You can use
this with any ViewGroup that needs only addView() and removeView() to manage child
Views, such as LinearLayout.  The BindingAdapter works with both ObservableList
and List, depending on whether you need to track updates or not.

Note that this is not a library, rather a reference implementation. You can
(should) customize it for your app to get the best of it.
