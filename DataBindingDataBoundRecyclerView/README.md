** This is a sample application that shows how data binding can be used with RecyclerView **
It has 2 RecyclerView lists. 1 of them has the same item type and the other one has mixed items.

All of the items from the first list are included in the second list so you can observe how
two lists stay in sync.

You should take a look at [BaseDataBoundAdapter](https://github.com/google/android-ui-toolkit-demos/blob/master/DataBinding/DataBoundRecyclerView/app/src/main/java/com/example/android/databoundrecyclerview/BaseDataBoundAdapter.java) class which adds a callback to the data binding class to prevent items from updating themselves. Instead, it notifies the RecyclerView about the change and waits for the RecyclerView to call onBind for that item.

This allows RecyclerView animations to work well and avoid unnecessary layout passes.

There are 2 adapters that are built on top of `BaseDataBoundAdapter`. 

* [DataBoundAdapter](https://github.com/google/android-ui-toolkit-demos/blob/master/DataBinding/DataBoundRecyclerView/app/src/main/java/com/example/android/databoundrecyclerview/DataBoundAdapter.java) is suitable for lists where there is only 1 type of view. This way `bindItem(DataBoundViewHolder<T> holder, int position, List<Object> payloads)` implementation becomes type safe.
* [MultiTypeDataBoundAdapter](https://github.com/google/android-ui-toolkit-demos/blob/master/DataBinding/DataBoundRecyclerView/app/src/main/java/com/example/android/databoundrecyclerview/MultiTypeDataBoundAdapter.java) demonstrates using multiple item types. It works based on the asusmption that each item view receives a variable called data. This helps it to work with multiple activities. 

You can see actual usage of these two adapters in the [MainActivity](https://github.com/google/android-ui-toolkit-demos/blob/master/DataBinding/DataBoundRecyclerView/app/src/main/java/com/example/android/databoundrecyclerview/MainActivity.java).

Note that this is not a library, rather a reference implementation. You can (should) customize it for your app to get the best of it.
