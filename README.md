# Commen RecyclerView for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/dsiner/maven/xrv/images/download.svg) ](https://bintray.com/dsiner/maven/xrv/_latestVersion)

## Demo
<p>
   <img src="https://github.com/Dsiner/xRecyclerViewF/blob/master/screenshot/screenshot.gif" width="320" alt="Screenshot"/>
</p>

## Setup
Maven:
```xml
<dependency>
  <groupId>com.dsiner</groupId>
  <artifactId>xrv</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.dsiner:xrv:1.0.0'
```


## Usage
```xml
    <com.d.xrv.XRecyclerView
        android:id="@+id/xrv_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

#### Step1. 获取引用
```java
        //step1:获取引用
        XRecyclerView recyclerView = (XRecyclerView) this.findViewById(R.id.xrv_list);
```
    
#### Step2. 设置LayoutManager
```java
        recyclerView.showAsList();//listview展现形式
```


#### Step3. setHeader(可选)
```java
        View header = LayoutInflater.from(this).inflate(R.layout.view_header, (ViewGroup) findViewById(android.R.id.content), false);
        recyclerView.addHeaderView(header);
```
#### Step4. new Adapter
```java
        public class MultipleAdapter extends CommonAdapter<Bean> {
        
            /**
             * @param context:context
             * @param datas:填充数据源
             * @param multiItemTypeSupport:多布局类型支持
             */
            public MultipleAdapter(Context context, List<Bean> datas, MultiItemTypeSupport<Bean> multiItemTypeSupport) {
                super(context, datas, multiItemTypeSupport);
            }
        
            @Override
            public void convert(int position, CommonHolder holder, Bean item) {
                //先判断mLayoutId布局类型，后通过通用holder操控控件
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
                //step4-2:根据type返回layout布局
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
                //step4-1:获取type类型
                return bean.type;
            }
        });
```

#### Step5. setAdapter
```java
        recyclerView.setAdapter(adapter);
```

#### Step6. setListener
```java
        recyclerView.setLoadingListener(new IRecyclerView.LoadingListener() {
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


More usage see [demo](app/src/main/java/com/d/xrecyclerviewf/MainActivity.java)


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
