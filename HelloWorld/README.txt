Jared Wasserman

What is printed to LogCat when we rotate the screen?

01-22 16:40:59.568: I/MainActivity(836): onPause() called
01-22 16:40:59.576: I/MainActivity(836): onStop() called
01-22 16:40:59.666: I/MainActivity(836): onDestroy() called
01-22 16:41:00.987: I/MainActivity(836): onCreate() called
01-22 16:41:00.997: I/MainActivity(836): onStart() called
01-22 16:41:01.017: I/MainActivity(836): onResume() called

When the phone display is flipped, our application is destroyed and then automatically started back up. This happens so that our application layouts and resources have a chance to reset in case we want our landscape mode to have an entirely different layout than our portrait mode.