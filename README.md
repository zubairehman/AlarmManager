# AlarmManager
[![](https://jitpack.io/v/zubairehman/AlarmManager.svg)](https://jitpack.io/#zubairehman/AlarmManager)

`AlarmManager` provides access to the system alarm services. These allow you to schedule your application to be run at some point in the future. When an alarm goes off, the Intent that had been registered for it is broadcast by the system that you can receive inside your activity. Registered alarms are retained while the device is asleep (and can optionally wake the device up if they go off during that time), but will be cleared if it is turned off and rebooted. Alarm has been set automatically on boot completed. 

## How to use

Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```
dependencies {
    implementation 'com.github.zubairehman:AlarmManager:v1.0.0-alpha01'
}
```
Using `AlarmManager` is really simple and easy, all you need to do is to pass in your configuration and start getting notifications in your activity/fragment. This could further be illustrated in the example below:
``` kotlin
AlarmBuilder().with(context)
              .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
              .setId("UPDATE_INFO_SYSTEM_SERVICE")
              .setAlarmType(AlarmType.REPEAT)
              .setAlarm()
```
Note that you can get a builder object for later use, as shown in the code below:

``` kotlin
val builder = AlarmBuilder().with(context)
        .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
        .setId("UPDATE_INFO_SYSTEM_SERVICE")
        .setAlarmType(AlarmType.REPEAT)
        
builder?.setAlarm()
```

### Register listener
In-order to get notified, you need to register a listener in `onResume` as shown below:

``` kotlin
override fun onResume() {
    super.onResume()
    builder?.addListener(this)
}
```
### Un-Register listener

In-order to prevent your activity listening to un-attended events you need to un-register your listener in `onPause` as shown below:
``` kotlin
override fun onPause() {
    super.onPause()
    builder?.removeListener(this)
}
```
### Cancel alarm
In-order to cancel your alarm just call `cancelAlarm()` as shown below:
``` kotlin
builder?.cancelAlarm()
```
### Callback
The `perform()` method will be called once the desired time has been reached, you can write your logic in this method as shown below:
``` kotlin
override fun perform(context: Context, intent: Intent) {
    Timber.i("Do your work here")
}
```

## Complete Example:

Here is the complete code that demonstrate the use of AlarmManager:

``` kotlin
class MainActivity : AppCompatActivity(), AlarmListener {

    //Alarm builder
    var builder: AlarmBuilder? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating alarm builder
        builder = AlarmBuilder().with(this)
                .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
                .setId("UPDATE_INFO_SYSTEM_SERVICE")
                .setAlarmType(AlarmType.REPEAT)

        //setting click listeners
        btnSetAlarm.setOnClickListener {
            builder?.setAlarm()
        }

        btnCancelAlarm.setOnClickListener {
            builder?.cancelAlarm()
        }
    }

    override fun onResume() {
        super.onResume()
        builder?.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        builder?.removeListener(this)
    }

    override fun perform(context: Context, intent: Intent) {
        Log.i("Alarm", "Do your work here")
    }
}

```
