# Common RecyclerView for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-9%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Download](https://api.bintray.com/packages/dsiner/maven/xrv/images/download.svg) ](https://bintray.com/dsiner/maven/xrv/_latestVersion)
[![Readme](https://img.shields.io/badge/README-%E4%B8%AD%E6%96%87-brightgreen.svg)](https://github.com/Dsiner/Xrv/blob/master/README-zh.md)

## Demo
<p>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/Xrv/xrv_list.gif" width="320" alt="Screenshot"/>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/Xrv/xrv_drag.gif" width="320" alt="Screenshot"/>
</p>

## Setup
Maven:
```xml
<dependency>
  <groupId>com.dsiner.lib</groupId>
  <artifactId>xrv</artifactId>
  <version>1.1.1</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner.lib:xrv:1.1.1'
```

## Usage
```xml
    <com.d.lib.xrv.XRecyclerView
        android:id="@+id/xrv_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

#### Step 1. Get reference
```java
        // Step1: Get reference
        XRecyclerView xrvList = (XRecyclerView) this.findViewById(R.id.xrv_list);
```
    
#### Step 2. Set layoutmanager
```java
        xrvList.showAsList(); // LinearLayoutManager
```

#### Step 3. Set header (optional)
```java
        View header = LayoutInflater.from(this).inflate(R.layout.view_header, (ViewGroup) findViewById(android.R.id.content), false);
        xrvList.addHeaderView(header);
```
#### Step 4-1. New adapter(simple type)
```java
        public class SimpleAdapter extends CommonAdapter<Bean> {
            /**
             * @param context: Context
             * @param datas: Filled data source
             * @param layoutId: Single type layout
             */
            public SimpleAdapter(Context context, List<Bean> datas, int layoutId) {
                super(context, datas, layoutId);
            }
        
            @Override
            public void convert(int position, CommonHolder holder, Bean item) {
                // Assigned by the universal holder method
                holder.setText(R.id.tv_des, "P:" + position + "_" + item.name);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.item_0);
```

#### Step 4-2. Or new adapter(multiple type)
```java
        public class MultipleAdapter extends CommonAdapter<Bean> {
        
            /**
             * @param context: Context
             * @param datas:  Filled data source
             * @param multiItemTypeSupport: Multiple layout type support
             */
            public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
                super(context, datas, multiItemTypeSupport);
            }
        
            @Override
            public void convert(int position, CommonHolder holder, Bean item) {
                // Step 4-2-3: First determine the mLayoutId layout type, and then manipulate the controls through the universal holder
                switch (holder.mLayoutId) {
                    case R.layout.item_0:
                        ...
                        break;
                    case R.layout.item_1:
                        ...
                        break;
                    case R.layout.item_2:
                        ...
                        break;
                    case R.layout.item_3:
                        ...
                        break;
                }
            }
        }
        
        MultipleAdapter adapter = new MultipleAdapter(MultipleXRvActivity.this, datas, new MultiItemTypeSupport<Bean>() {
            @Override
            public int getLayoutId(int viewType) {
                // Step 4-2-2: Return layout layout based on type
                switch (viewType) {
                    case 0:
                        return R.layout.item_0;
                    case 1:
                        return R.layout.item_1;
                    case 2:
                        return R.layout.item_2;
                    case 3:
                        return R.layout.item_3;
                    default:
                        return R.layout.item_0;
                }
            }

            @Override
            public int getItemViewType(int position, Bean bean) {
                // step 4-2-1: Get type
                return bean.type;
            }
        });
```

#### Step 5. Set adapter
```java
        xrvList.setAdapter(adapter);
```

#### Step 6. Set listener
```java
        xrvList.setLoadingListener(new IRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                ...
            }

            @Override
            public void onLoadMore() {
                ...
            }
        });
```

## Latest Changes
- [Changelog.md](CHANGELOG.md)

More usage see [Demo](app/src/main/java/com/d/xrecyclerviewf/MainActivity.java)

## Thanks
- [XRecyclerView](https://github.com/jianghejie/XRecyclerView)  - RecyclerView that implements pullrefresh , loadingmore and header featrues

## Licence

```txt
Copyright 2017 D

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
