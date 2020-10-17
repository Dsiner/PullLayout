# PullLayout for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-11%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Download](https://api.bintray.com/packages/dsiner/maven/pulllayout/images/download.svg) ](https://bintray.com/dsiner/maven/pulllayout/_latestVersion)

> Damping effect & Pull down refresh, load more.

## Demo
<p>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/PullLayout/pulllayout.gif" width="320" alt="Screenshot"/>
</p>

## Set up
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>pulllayout</artifactId>
  <version>1.0.1</version>
</dependency>
```
or Gradle:
```groovy
implementation 'com.dsiner.lib:pulllayout:1.0.1'
```

## Chapter 1 Sliding damping

### Support
- [x] ViewGroup
- [x] ListView
- [x] RecyclerView
- [x] ScrollView

### Usage
```java
    <com.d.lib.pulllayout.PullLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:lib_pull_enable="true"
        app:lib_pull_gravity="top|left|right">

        <ViewGroup
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.d.lib.pulllayout.PullLayout>
```

## Chapter 2 Pull down refresh, load more

### Support
- [x] PullRecyclerLayout
    - [x] PullRecyclerView
    - [x] RecyclerView
    - [x] ListView
- [x] PullRecyclerView

More usage see [Demo](app/src/main/java/com/d/pulllayout/MainActivity.java)

## Licence

```txt
Copyright 2018 D

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
