# AlarmManager
This repository contains a sample code about AlarmManager.

## How to use

``` kotlin
AlarmBuilder().with(context)
              .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
              .setId("UPDATE_INFO_SYSTEM_SERVICE")
              .setAlarmType(AlarmType.REPEAT)
              .setAlarm()
```

register listener in `onResume` as shown below:

``` kotlin
override fun onResume() {
    super.onResume()
    builder?.addListener(this)
}
```

un-register listener in `onPause` as shown below:
``` kotlin
override fun onPause() {
    super.onPause()
    builder?.removeListener(this)
}
```

## Complete Example:

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
