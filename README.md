# LocalSDK
Location Tracking Library written in Kotlin to easy the process of location tracking. it's compatible with the latest Android API. 

# Configure project
Add the following lines to the application:
```
allprojects {
    repositories {
      maven { url 'https://jitpack.io' }
    }
}
```
and for the other gradle file
```
dependencies {

    implementation 'com.github.nikiizvorski:localsdk:v1.0'
          
    // or
          
    implementation(name: 'localsdk', ext:'aar')
          
    // google
   implementation 'com.google.android.gms:play-services-gcm:16.1.0'
   implementation 'com.google.android.gms:play-services-location:16.0.0'
}
  
```

# Local requires the following permissions, included automatically by the SDK manifest:

```
ACCESS_FINE_LOCATION, for location services
INTERNET and ACCESS_NETWORK_STATE, to send API requests
RECEIVE_BOOT_COMPLETED, to restore geofences on boot
```

# Supported Java and Kotlin

Initialize the SDK in your Application class before calling any other methods. In onCreate(), call:

```
Local.initialize(publishableKey)
```

where publishableKey is a string containing your publishable API key.

# To identify the user when logged in, call:

```
Local().setUserId and Local().getUserId
```

# To set the tracking priority:

```
Local().setTrackingPriority(Local.LocalPriority.RESPONSIVE), Local().setTrackingPriority(Local.LocalPriority.EFFICIENT)
```

# Request permissions
Permission Request is done with the following method its done for user convinience. You have two methods: 

```
Local().requestPermissions() and Local().checkSelfPermissions()
```

# Tracking options
Once you have initialized the SDK, you have identified the user, and the user has granted permissions, you can track the user's location.

# To track the user's location and get last known location and get it once use this call:
```
Local().trackOnce(add callback)
```

# The request status can be:

```
Local.SUCCESS: the request succeeded
Local.ERROR_PUBLISHABLE_KEY: the SDK was not initialized
Local.ERROR_PERMISSIONS: the user has not granted location permissions for the app
```

# Tracking the user constantly with the following call:

```
Local().startTracking(add callback) and Local().stopTracking()
```

# The SDK Size and ProGuard Rules:

SDK size is 150kb and its ProGuard Friendly

# Next to be added in the library would be on request Background Tracking which would be simple with a JobService and a Receiver and Geofences.

This is a test configuration for a SDK. It doesn't have a production setup. If you need production setup please let me know and i can do it depending on the services and end result that you need. Also architecture is to minimum for size and integration problems with other people dependecy management with Dagger etc...No need to exclude libraries after.


