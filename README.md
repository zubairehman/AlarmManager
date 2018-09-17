# AlarmManager
This repository contains a sample code about AlarmManager.

## How to use

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
        .setAlarm()
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
        Timber.i("Do your work here")
    }
}

```
