
# Application Loading Status

## About

This app project downloads a selected git project repository from GitHub.com, using a custom-built button view where:
 - the download button is a custom view,
 - all states of the custom button view are drawn using onDraw(),
 - whilst project download is in progress, the width of the button is animated from left to right to indicate download progress,
 - button text is changed based on different states of the button,
 - a download progress circle is also overlayed on the button, which animates from 0 to 360 degrees during the notificationn,
 - once download completes, a notification is sent,
 - the user can then interact with the notification itself or via an action button built into the notification to launch the app (if not in the foreground),
 - when the app launches, the detail activity screen is displayed and the notification is dismissed,
 - on the detail activity screen, the status of the download is displayed. The OK button is also automatically animated via MotionLayout when the activity starts.


## Notes for Assessor

- Main Activity > Downloading Animations:
  - The animation duration is just an arbitrary fixed value and is not calculated using an approximate file download duration.

## Dependencies

```
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.core:core-ktx:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
    implementation 'com.squareup.retrofit2:retrofit:$retrofit_version'
    implementation 'com.squareup.retrofit2:converter-moshi:$moshi_version'
```

# Core Technologies

- Retrofit

- Moshi

- MotionLayout

- Value Animator

- ConstraintLayout

- Observables

- Custom Views


# Getting Started

This project's repository can be cloned via git or downloaded as a zip file.


### Built Using

* [Android Studio](https://developer.android.com/studio) - Default IDE used to build android apps
* [Kotlin](https://kotlinlang.org/) - Default language used to build this project


### Deployment information

- <strong>Deployment Target (android API / Version):</strong> 30 / Android 11 (R)

### App Versions
- September, 2021 (version 1)


### License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)
