[![](https://jitpack.io/v/pao11/RVCardGalleryRelease.svg)](https://jitpack.io/#pao11/RVCardGalleryRelease)
[![Travis](https://img.shields.io/badge/Gradle-2.3.1-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/Gradle--Wrapper-gradle--3.3--all-blue.svg)]()

# RecyclerViewCardGalleryRelease

RecyclerView实现Card Gallery效果，替代ViewPager方案。能够快速滑动并最终定位到居中位置

![RecyclerViewCardGallery.gif](https://github.com/pao11/RVCardGalleryRelease/blob/master/art/RecyclerViewCardGallery_blur.gif)

## Gradle 添加引用
```
compile 'com.github.pao11:RVCardGalleryRelease:v2.0.0' 
```

## Usage

调用`new PageScaleHelper().attachToRecyclerView(mRecyclerView);`扩展RecyclerView
```
final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
mRecyclerView.setLayoutManager(linearLayoutManager);
adapter = new CardAdapter();
mRecyclerView.setAdapter(adapter);

// mRecyclerView绑定scale效果
mCardScaleHelper = new CardScaleHelper();
mCardScaleHelper.setCurrentItemPos(2);//设置默认显示图片为第二张
mCardScaleHelper.setSmoothScroll(true);//设置默认显示图片为第二张时是否带有动画滚动效果
mCardScaleHelper.setSnapHelperType(CardScaleHelper.LINEAR_SNAP_HELPER);//一次Fling时，滑动多张或一张
mCardScaleHelper.attachToRecyclerView(mRecyclerView);

//初始化数据后请调用
mCardScaleHelper.setCurrentItemPosWithNotify(3);//初始化后默认显示的图片

```

在adapter相应的位置调用
```
mCardAdapterHelper.onCreateViewHolder(parent, itemView);
mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
```

## Apk download
![app_debug.apk](https://github.com/pao11/RVCardGalleryRelease/blob/master/art/app-debug.apk?raw=true)


##更新记录

 **v2.0.0**　`2017.10.13`　发布第八个版本--SDK VERSION 24.2.0（适配乐视手机出现的bug）

 **v1.6.0**　`2017.10.12`　发布第六个版本--SDK VERSION 24.2.0（优化代码稳定性）

 **v1.5.0**　`2017.10.12`　发布第五个版本--SDK VERSION 24.2.0（修复一些闪退bug）

 **v1.3.0**　`2017.10.12`　发布第四个版本--SDK VERSION 24.2.0（增加默认显示图片位置及是否带有滚动动画）

 **v1.2.0**　`2017.10.11`　发布第三个版本--SDK VERSION 24.2.0（默认使用自定义的CardPagerSnapHelper）
 
 **v1.1.0**　`2017.10.11`　发布第二个版本--SDK VERSION 25.1.0（默认使用PagerSnapHelper）
 
 **v1.0.0**　`2017.10.10`　发布第一个版本--SDK VERSION 24.2.0（默认使用LinearSnapHelper）
 

## License

```
Copyright 2017 pao11

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
