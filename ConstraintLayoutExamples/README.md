MotionLayout / Constraint Layout Samples
=========================
This repository contains a list of layouts that showcases the various features and usage of
[ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html) and 
[MotionLayout](https://developer.android.com/reference/android/support/constraint/motion/MotionLayout)

Pre-requisites
--------------
- Android Studio 3.3+
- Constraint Layout library 2.0.0-alpha5+

Getting Started
---------------
Import the project using Android Studio. Navigate to the app>res>layout> and open one of the layouts
in the layout editor. This sample is best understood by seeing the constraints in the Design mode
of the layout editor.

MotionLayout samples overview
-----------------------------

|  Title  |  GIF  | Layout | MotionScene | 
| :----: | :----: | :----: | :----: |
|  Basic Example (1/3) | <img src="https://user-images.githubusercontent.com/796361/53616329-6ce64e80-3c25-11e9-8fe1-a4d60bf25584.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_01_basic.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_01.xml) | 
|  Basic Example (2/3) | <img src="https://user-images.githubusercontent.com/796361/53616351-84bdd280-3c25-11e9-83ce-b933e276d08e.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_02_basic.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_02.xml) | 
|  Basic Example (3/3) | <img src="https://user-images.githubusercontent.com/796361/57440055-4296ba00-7282-11e9-8209-981ba020cda1.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_02_basic_autocomplete_false.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_02_autocomplete_false.xml) | 
|  Custom Attribute | <img src="https://user-images.githubusercontent.com/796361/53616369-91dac180-3c25-11e9-9245-7ab48fc94334.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_03_custom_attribute.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_03.xml) | 
|  ImageFilterView (1/2) | <img src="https://user-images.githubusercontent.com/796361/53616380-9c955680-3c25-11e9-801e-d6d2bbf140a3.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_04_imagefilter.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_04.xml) | 
|  ImageFilterView (2/2) | <img src="https://user-images.githubusercontent.com/796361/53616396-a6b75500-3c25-11e9-985f-8c800b1bb174.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_05_imagefilter.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_05.xml) | 
|  Keyframe Position (1/3) | <img src="https://user-images.githubusercontent.com/796361/53616407-b171ea00-3c25-11e9-8cd7-03c1631b4fa1.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_06_keyframe.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_06.xml) | 
|  Keyframe Interpolation (2/3) | <img src="https://user-images.githubusercontent.com/796361/53616488-26452400-3c26-11e9-9e6a-eb216d0d0379.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_07_keyframe.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_07.xml) | 
|  Keyframe Cycle (3/3) | <img src="https://user-images.githubusercontent.com/796361/53616423-c5b5e700-3c25-11e9-98a4-d98351664844.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_08_cycle.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_08.xml) | 
|  CoordinatorLayout Example (1/3) | <img src="https://user-images.githubusercontent.com/796361/53616433-cea6b880-3c25-11e9-9a56-1512385772e5.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_09_coordinatorlayout.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_09.xml) | 
|  CoordinatorLayout Example (2/3) | <img src="https://user-images.githubusercontent.com/796361/53616441-d9f9e400-3c25-11e9-8b5b-e49cbb255850.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_10_coordinatorlayout.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_10.xml) | 
|  CoordinatorLayout Example (3/3) | <img src="https://user-images.githubusercontent.com/796361/53616794-a029dd00-3c27-11e9-9fa9-848c1cde736b.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_11_coordinatorlayout.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_11_header.xml) | 
|  DrawerLayout Example (1/2) | <img src="https://user-images.githubusercontent.com/796361/53616524-4d9bf100-3c26-11e9-85db-88b1450be0a3.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_12_drawerlayout.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_12_content.xml) | 
|  DrawerLayout Example (2/2) | <img src="https://user-images.githubusercontent.com/796361/53616767-8092b480-3c27-11e9-8a25-cfba87a5dedf.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_13_drawerlayout.xml) | [Content](motionlayout/src/main/res/xml/scene_12_content.xml) <br> [Menu](motionlayout/src/main/res/xml/scene_13_menu.xml)| 
|  Side Panel Example | <img src="https://user-images.githubusercontent.com/796361/53616774-8b4d4980-3c27-11e9-9f98-00b42a6f862d.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_14_side_panel.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_14.xml) | 
|  Parallax Example | <img src="https://user-images.githubusercontent.com/796361/53616582-a4092f80-3c26-11e9-8f80-05d91fe42f8f.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_15_parallax.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_15.xml) | 
|  ViewPager Example | <img src="https://user-images.githubusercontent.com/796361/53616757-74a6f280-3c27-11e9-8b20-0b166a00928d.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_16_viewpager.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_15.xml) | 
|  ViewPager Lottie Example | <img src="https://user-images.githubusercontent.com/796361/53616613-cb5ffc80-3c26-11e9-8b6b-2b2b9f4ef883.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_23_viewpager.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_23.xml) | 
|  Complex Motion Example (1/4) | <img src="https://user-images.githubusercontent.com/796361/53616661-06623000-3c27-11e9-8616-901a22d2cf38.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_17_coordination.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_17.xml) | 
|  Complex Motion Example (2/4) | <img src="https://user-images.githubusercontent.com/796361/53616679-1ed24a80-3c27-11e9-9435-badc2ad2da97.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_18_coordination.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_18.xml) | 
|  Complex Motion Example (3/4) | <img src="https://user-images.githubusercontent.com/796361/53616684-2691ef00-3c27-11e9-9009-4e54debb1636.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_19_coordination.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_19.xml) | 
|  Complex Motion Example (4/4) | N/A | [Layout](motionlayout/src/main/res/layout/motion_20_reveal.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_20.xml) | 
|  Fragment Transition Example (1/2) | <img src="https://user-images.githubusercontent.com/796361/53616688-301b5700-3c27-11e9-9868-f5cd6a788b78.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/main_activity.xml) | [MotionScene](motionlayout/src/main/res/xml/main_scene.xml) |
|  Fragment Transition Example (2/2) | <img src="https://user-images.githubusercontent.com/796361/53616701-3c9faf80-3c27-11e9-85f7-487668e44f79.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/main_activity.xml) | [MotionScene](motionlayout/src/main/res/xml/main_scene.xml) |  
|  YouTube like motion Example | <img src="https://user-images.githubusercontent.com/796361/53616722-4f19e900-3c27-11e9-86b4-7ecaaeb57d9a.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_24_youtube.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_24.xml) |  
|  Example using KeyTrigger | <img src="https://user-images.githubusercontent.com/796361/53616732-59d47e00-3c27-11e9-89b6-b6174c4bddfe.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_25_keytrigger.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_25.xml) |  
|  Example using Multi State | <img src="https://user-images.githubusercontent.com/796361/53616746-635de600-3c27-11e9-9ad8-451cc175de3d.gif" height="360" width="180" > | [Layout](motionlayout/src/main/res/layout/motion_26_multistate.xml) | [MotionScene](motionlayout/src/main/res/xml/scene_26.xml) |  


ConstraintLayout samples screenshots
------------

![Constraint Set Example](screenshots/constraint_set_example.png "Constraint Set Example")
![Advanced Chains Example](screenshots/advanced_chains.png "Advanced usage of Chains")

Support
-------
If you've found an error in this sample, please file an issue:
https://github.com/android/views-widgets/issues

To learn more about ConstraintLayout checkout the
[Constraint Layout Training Guide](https://developer.android.com/training/constraint-layout/index.html)

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see the [contributing guidelines](CONTRIBUTING.md) for more details.
