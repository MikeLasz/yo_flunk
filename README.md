<img align="right" src="docs/assets/img/logo.png"> 

# Welcome! 
This GitHub repository contains the source code to build the Android application **Yo Flunk!**, which helps organizing and visualizing [flunkyball](https://www.youtube.com/watch?v=YfvTQjoIykY) matches. It allows to setup a league, in which matches are tracked and which rates users based on their flunkyball performance. Similar to chess, we implemented an ELO-rating system to calculate scores. Besides that, **Yo Flunk!** generates performance-related charts and statistics, and helps you organizing flunkyball tournaments. 
For more information and demonstrations, check out the [GitHub webpage](https://mikelasz.github.io/yo_flunk_page/). 

# How to install **Yo Flunk!**
1. Clone the repository and open the project via **Android Studio**. Executing the app from Android Studio should build all necessary remaining files.  
2. **Yo Flunk!** uses a **Firebase Database** to store and read the data. 

    2.1. In Android Studio, it is luckily very convenient to add a firebase to the project, see [here](https://firebase.google.com/docs/android/setup). 
    
    2.2. Initialize your firebase using `initial_database.json`. This provides the basic structure of the league for the database. Rename `insert_league_name` and `insert_league_name_prod` by meaningful names for the league, where the former is used for development, and the second is used in production. If you're not planning to work on the code, you can ignore the development league. 
    
    2.3. Make sure to edit `app/src/main/res/values/firebase_api.xml`, i.e. insert the firebase link, the server key, and the league name of your firebase database: 
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string name="firebase_link">Here comes your firebase link</string>
        <string name="fcm_server_key">Here comes your firebase server key</string>
        <bool name="dev_version">true</bool>
        <string name="league">insert_league_name</string>
        <string name="league_prod">insert_league_name_prod</string>
    </resources>
    ```
    `dev_version` (*developer version*) should be set to `false` for production, `dev_version=true` is useful for debugging and is using the database  `insert_league_name`. For instance, using the developer version, the code automatically generates placeholder teams when starting a tournament, which makes debugging more convenient.  
       
3. The **heatmap feature**, which visualizes played flunkyball matches on a map, is based on the [**googleMaps API**](https://developers.google.com/maps/documentation/android-sdk/overview). Hence, in order to use this feature, you need to get an API-key. Make sure to insert your API-key in `app/src/main/res/values/map_api.xml`. When running the application (after Step 4.), Android Studio should automatically generate the `yo_flunk/google-services.json` file. 

4. Finally, insert the league name (as it will be displayed) in `app/src/main/res/values/strings.xml`: 
   ```
   <string name="league_name">Here comes the name of the league (how it is being displayed)</string>
   ```
   
To distribute the app amongst your friends, [build the APK](https://www.educative.io/answers/extracting-an-apk-file-from-android-studio) and share it. 


If you are having any troubles setting up **Yo Flunk!**, please don't hesitate to contact me! I am happy to help you. :) 


# Contributing 
The app is far from perfect. For instance, there are still some features missing and the design does not adapt flawlessly to any phone size. Any help is appreciated to improve **Yo-Flunk!** There are following ToDo's: 
- Make the design adaptable to any phone size. 
- The tournaments-feature is not complete, yet: Playoffs need to be inserted to the database by hand. 
- It is not possible to switch between leagues without rebuilding a new APK. A spinner that allows to switch between leagues in the `MainActivity` would be awesome. 
- Any new visualizations of statistics are appreciated. 
- So far, the app is in German. An English translation, and implementing the possibility to switch between languages, would be a great contribution. 
