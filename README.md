# Maggg
Tmdb (themoviedb.org) popular movies feed App. Built to showcase the Model-View-Intent
architectural pattern.

#### Building and running the project
You are free to clone this repo. This project requires:
 * Android Studio 3.1 Canary 5
 * Android Gradle Plugin `3.1.0-alpha5`
 * Kotlin `1.2.0`
 
This app (along with the unit tests) are written 100% in Kotlin 🎉

This project makes use of the free popular movies API at [themoviedb.org](http://developers.themoviedb.org).

You need to sign up for a free account and obtain an API key.
 
Once obtained, copy your API key into a/the `local.properties` file in the root of your project like:
```
maggg.tmdbApiKey=<your_tmdb_api_key>
```
and build the project.

#### Libraries Used
 * Design SupportLib
 * AppCompat
 * Retrofit
 * Gson
 * Glide
 * Architechture Components
 * Dagger (Dagger Android)
 * RxJava/RxAndroid/RxBinding
 * JUnit
 * Mockito (Mockito-Kotlin)
 
 #### Image Loading
 The tmdb api offers images in various size buckets and this app takes advantage of [Glide 4.4](http://bumptech.github.io/glide/)s 
 excellent `ModelLoader` API to load the best sized image for the current device. See the classes in the 
 [glide](https://github.com/efemoney/maggg/tree/master/app/src/main/kotlin/com/efemoney/maggg/glide) package for more information
 
 #### MVI Architecture
 This application uses the Mode-View-Intent (MVI) architecture. The MVI pattern models the entire system
 as a unidirectional flow of *immutable* **intents**, **states** and data in between. RxJava and other
 libraries are used to power this pattern.
 When running the app in `debug` mode you can view the sequence of user events and app state by filtering logs
 with the `StateLogs` log tag.
 
 #### Possible Improvements
 * Include Glide RecyclerView integration library and add preloading for a better UX
 * More animations & shared transitions
 * More tests!
