# NoteSpace

Simple Notes Android app.


## Running the App

### 1. Add Firebase

1. Go to your Firebase console and create a new Firebase project.

2. Create a new Android app with the package name: 'com.ashwin.android.notespace'
Note: You can change this package name in the `app/build.gradle` value of applicationId.

3. Add google-services.json file from the Firebase console in the `app/` directory.

### 2. Enable Google Authentication

1. Go to your Firebase console, Authentication and under Sign-in method.

2. Enable Google Sign-in, provide project support email and then save.

### 3. Enable Firestore Database

1. In your Firebase console, Database, enable Firestore for testing.

### 4. Run the App

1. In Android Studio, click on Build -> Rebuild Project

2. Connect an Android device to your system and run the app from Android Studio.
