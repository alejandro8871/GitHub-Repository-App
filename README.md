Repository App 
=============================

This is a Repositry Android App

## Building the Repository App

First, clone the repo:

`git clone https://github.com/alejandro8871/GitHub-Repository-App.git`

Building the App then depends on your build tools.

### Android Studio (Recommended)

* Open Android Studio and select `File->Open...` and navigate to the root directory of your project.
* Select the directory or drill in and select the file `build.gradle` in the cloned repo.
* Click 'OK' to open the the project in Android Studio.
* A Gradle sync should start, but you can force a sync and build the 'app' module as needed.

### Gradle (command line)

* Build the APK: `./gradlew build`

## Running the Repositry App

Connect an Android device to your development machine.

### Android Studio

* Select `Run -> Run 'app'` (or `Debug 'app'`) from the menu bar
* Select the device you wish to run the app on and click 'OK'

### Gradle

* Install the debug APK on your device `./gradlew installDebug`
