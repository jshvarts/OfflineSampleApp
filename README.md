# Offline Sample App 

[![Build Status](https://travis-ci.org/jshvarts/OfflineSampleApp.svg?branch=sync-in-background)](https://travis-ci.org/jshvarts/OfflineSampleApp)

Medium post covering this app: [Building Offline-First App using MVVM, RxJava, Room and Priority Job Queue](https://proandroiddev.com/offline-apps-its-easier-than-you-think-9ff97701a73f)

## What is an Offline App?

Offline App (or Offline-First App) enables user to seamlessly interact with it by using local device storage and then synchronizing the data with some remote storage (cloud database, etc) later via a background process.

With offline apps
1) users no longer get error messages due to network connection problems.
2) users benefit from faster loading times and conserving battery life.
3) users do not see any loading bar since their actions are performed against fast local storage.

## App Overview

![Comments Screen](images/comments.png?raw=true)

This app is a working sample that showcases one way of implementing offline commenting capability on Android platform. Users' comments are stored in local Room database first. Then a background job is spawned to synchronize local data with remote database if and when Internet connection is available. 

This background job is designed to be persistent--it is guaranteed to execute even after app or device restarts while waiting for the network connection.

## Libraries Used

* Patterns and frameworks
	* MVVM (Model-View-ViewModel) using Google's new Architecture components `ViewModel`, `LiveData`, `LifecycleObserver`, etc.
	* [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) with `ViewModel` interacting with UseCases and the latter interacting with local database. Making each layer highly testable.
* Database
	* [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room.html), part of Google's new Architecture components.
* Background Job processing
	* [Android Priority JobQueue](https://github.com/yigit/android-priority-jobqueue) which uses [Job Scheduler](https://developer.android.com/reference/android/app/job/JobScheduler.html) for API level Lollipop and above and [GcmNetworkManager](https://developers.google.com/android/reference/com/google/android/gms/gcm/GcmNetworkManager) for API level below Lollipop.
* Remote Call APIs
	* [Retrofit 2](http://square.github.io/retrofit/) to perform HTTP requests.
	* Fake remote database using simple [JSONPlaceholder](https://jsonplaceholder.typicode.com) REST API.
* Dependency Injection
    * [Dagger Android 2.11](https://github.com/google/dagger/releases/tag/dagger-2.11) to manage App and Activity-scoped dependencies.
* Communication between app layers
    * [RxJava2](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid) for interacting between `ViewModel` and local database. 
    * [RxRelay](https://github.com/JakeWharton/RxRelay) for publishing requests from the background job so that lifecycle observer components can update local database. 
* Other
    * [ButterKnife](http://jakewharton.github.io/butterknife/) to simplify View and Listener bindings.
    * [Travis CI](https://travis-ci.org/) is used for automating continuous integration.
    * The following quality checks are configured: checkstyle, pmd, findbugs, lint and [RxLint](http://bitbucket.org/littlerobots/rxlint). You can perform all of them at once by executing `./gradlew check`

## Branches
* **master** - comment sync response is observed and local database is updated by `LifecycleObserver`. For this to work, the app has to be in foreground.
* **sync-in-background** - comment sync response is observed and local database is updated in the background whenever connection is available. For this to work, the app does not need to be in foreground.

## License

    Copyright 2017 James Shvarts

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

