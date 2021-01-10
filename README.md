# PullLayout for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-11%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Download](https://api.bintray.com/packages/dsiner/maven/pulllayout/images/download.svg) ](https://bintray.com/dsiner/maven/pulllayout/_latestVersion)

> A reusable Pull to Refresh library for Android.

## Set up
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>pulllayout</artifactId>
  <version>2.0.0</version>
</dependency>
```
or Gradle:
```groovy
AndroidX
implementation 'com.dsiner.lib:pulllayout:2.0.0'
or Support
implementation 'com.dsiner.lib:pulllayout:1.0.4'
```

## Features
 * Supports both Pulling Down from the top, and Pulling Up from the bottom (or even both).
 * Animated Scrolling for all devices.
 * Currently works with:
 	* **RecyclerView**
 	* **ListView**
 	* **ScrollView**
 	* **WebView**
 	* **ViewPager**
 	* **CoordinatorLayout**
 * Integrated End of List Listener for use of detecting when the user has scrolled to the bottom.
 * Callback method to be invoked when Pullable's scroll state changes.
 * Dynamically add headers and footers.
 * Support `duration` `factor` `TimeInterpolator`
 * Sliding damping, supports all directions (`left` `top` `right` `bottom`).
 * Multi-type adapter support.
 * Drag and drop sort.
 * Lots of [Customisation](https://github.com/Dsiner/PullLayout/blob/master/app/src/main/java/com/d/pulllayout/MainActivity.java) options!

## Screenshot
![Artboard](https://github.com/Dsiner/Resouce/blob/master/lib/PullLayout/pulllayout.png)

## How do I use it?

## Configuration ##

### Via XML ###
#### Damp ####
```XML
    <com.d.lib.pulllayout.PullLayout
        ...
        app:lib_pull_enable="true"
        app:lib_pull_gravity="left|top|right|bottom">

        <ViewGroup
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.d.lib.pulllayout.PullLayout>
```

|  attribute name | description |
|---|---|
| lib_pull_enable  | Draggable |
| lib_pull_gravity  | Draggable direction |

#### Pull to Refresh list ####
```XML
    <com.d.lib.pulllayout.PullRecyclerLayout
        ...
        app:lib_pull_type="recyclerView" />
```

|  attribute name | description |
|---|---|
| lib_pull_type | Nested style - `PullRecyclerview` (by default) or `RecyclerView` or `ListView` |

## Animation ##

### Refreshing callback ###
Just implement `Refreshable.OnRefreshListener`:

```Java

mPullList.setOnRefreshListener(new Refreshable.OnRefreshListener() {
    @Override
    public void onRefresh() {
        // Refresh your data here
    }

    @Override
    public void onLoadMore() {
        // Load your data here
    }
});
```

### Pulling callback ###
Just implement `Pullable.OnPullListener`:

```Java

mPullList.addOnPullListener(new Pullable.OnPullListener() {
    @Override
    public void onPullStateChanged(Pullable pullable, int newState) {
        // Callback method to be invoked when Pullable's scroll state changes.
    }

    @Override
    public void onPulled(Pullable pullable, int dx, int dy) {
        // Callback method to be invoked when the Pullable has been scrolled.
    }
});
```

### To start or stop animation: ###

```Java
mPullList.refresh();
mPullList.loadMore();
mPullList.refreshSuccess();
mPullList.refreshError();
mPullList.loadMoreSuccess();
mPullList.loadMoreError();
mPullList.loadMoreNoMore();
```

### Using custom views ###
For using custom views just implement `IEdgeView`:

```Java

mPullList.setHeader(new HeaderView(context));
mPullList.setFooter(new FooterView(context));
```

## Adapter ##

### Simple adapter ###
```Java
public class SimpleAdapter extends CommonAdapter<Bean> {

    public SimpleAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final int position, CommonHolder holder, Bean item) {
        ...
    }
}
```

### Multiple adapter ###
```Java
public class MultipleAdapter extends CommonAdapter<Bean> {

    public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void convert(final int position, CommonHolder holder, Bean item) {
        switch (holder.layoutId) {
            ...
        }
    }
}
```

### Support headers or footers ###
```Java
mPullList.addHeaderView(view);
mPullList.addFooterView(view);

mPullList.removeHeaderView(view);
mPullList.removeFooterView(view);
```

More usage see [Demo](app/src/main/java/com/d/pulllayout/MainActivity.java)

## Thanks
- [Android-PullToRefresh](https://github.com/chrisbanes/Android-PullToRefresh)  - A pull to refresh widget
- [XRecyclerView](https://github.com/jianghejie/XRecyclerView)  - RecyclerView that implements pullrefresh , loadingmore and header featrues

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
