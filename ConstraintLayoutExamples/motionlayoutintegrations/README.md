# Motion Layout Integrations samples

This sample showcases realistic animations built using `MotionLayout` in 
real-world settings.

It is recomended that you use Android Studio 4.0 or higher to view this sample
to explore the animations in Motion Editor. For optimal experience, use Android
Studio 4.2 or higher.

## ViewPagerIntegration
Integrate with `ViewPager` to create a dynamic header that animates as the user
swipes.

![Preview of View Pager integration](https://user-images.githubusercontent.com/119115/91504425-f3630700-e881-11ea-894e-f88704f8ce4d.gif)

This screen shows how to use a swipe event in another view to drive seekable 
animations in MotionLayout. It has a relatively simple animation, containing
only two views, but by coordinating motion with user input it makes the screen
engaging without adding a lot of code.

|  File                                                                                                 | Content                                                        |
| ----------------------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| [Activity](src/main/java/com/example/androidstudio/motionlayoutintegrations/ViewPagerIntegration.kt)  | Activity showing how to coordinate MotionLayout with ViewPager |
| [layout](src/main/res/layout/activity_view_pager_integration.xml)                                     | Layout with ViewPager and a MotionLayout                       |
| [scene](src/main/res/xml/activity_view_pager_integration_scene.xml)                                   | Scene to describe the animation with two states                |

## Entrance

Build an entrance animation to play when the user enters a screen.

![Preview of Entrance animation](https://user-images.githubusercontent.com/119115/91504411-ea723580-e881-11ea-89f0-e46b6b04bc64.gif)

This example shows how to use `motion:autoTransition="animateToEnd"` to 
automatically progress animations through states.  Visually it appears to be
a single animation, but in code it's broken into four animations. For 
animations that play for a long time like this one, this can help break the 
animation into smaller pieces when developing. When viewed and edited in 
Motion Editor this structure makes it easier to work on just one part of the 
animation.

![Animation created from multiple constraint sets](https://user-images.githubusercontent.com/119115/91505778-48ece300-e885-11ea-8670-4068a0afc03a.png)
> Animation is created by chaining multiple ConstraintSets

|  File                                                                                     | Content                                                        |
| ----------------------------------------------------------------------------------------- | -------------------------------------------------------------- |
| [Activity](src/main/java/com/example/androidstudio/motionlayoutintegrations/Entrance.kt)  | Regular Activity (no extra code)                               |
| [layout](src/main/res/layout/activity_entrance.xml)                                       | Layout all Views used in this animation                        |
| [scene](src/main/res/xml/activity_entrance_scene.xml)                                     | Scene containing multiple ConstraintSet and Transition         |

## CollapsingToolbar
Build a collapsing toolbar with support for insets using MotionLayout.

![Preview of Collapsing Toolbar animation](https://user-images.githubusercontent.com/119115/91504419-f0681680-e881-11ea-9143-fa3810c01abd.gif)

This example shows a complex custom collapsing toolbar built using 
MotionLayout. It shows how to use insets to set guidelines in a MotionLayout
to avoid drawing under cutouts and display an animated systembar underlay.

It also shows how to integrate MotionLayout with a custom view which draws the
circular coutouts and animates the background.

|  File                                                                                              | Content                                                                              |
| -------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| [Activity](src/main/java/com/example/androidstudio/motionlayoutintegrations/CollapsingToolbar.kt)  | Integrate MotionLayout, CoordanatorLayout, and Custom View, and Insets               |
| [layout](src/main/res/layout/activity_collapsing_toolbar.xml)                                      | Layout all Views used in this animation, including inset guidelines & custom view    |
| [scene](src/main/res/xml/activity_collapsing_toolbar_scene.xml)                                    | Scene showing how to integrate with custom views using CustomAttribute               |
