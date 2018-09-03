# RefreshLayout for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-11%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=11)

> Damping effect.

## Demo
<p>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/RefreshLayout/refreshlayout.gif" width="320" alt="Screenshot"/>
</p>

## Support
- [x] ViewGroup
- [x] ListView
- [x] RecyclerView
- [x] ScrollView

## Usage
```java
    <com.d.lib.refreshlayout.RefreshLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:lib_refresh_refreshlayout_enable="true"
        app:lib_refresh_refreshlayout_gravity="top|left|right">

        <ViewGroup
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.d.lib.refreshlayout.RefreshLayout>
```

More usage see [Demo](app/src/main/java/com/d/refreshlayout/MainActivity.java)

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
