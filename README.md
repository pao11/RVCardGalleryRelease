[![](https://jitpack.io/v/pao11/RVCardGalleryRelease.svg)](https://jitpack.io/#pao11/RVCardGalleryRelease)

# RecyclerViewCardGalleryRelease

RecyclerView实现Card Gallery效果，替代ViewPager方案。能够快速滑动并最终定位到居中位置

![RecyclerViewCardGallery.gif](https://github.com/pao11/RVCardGalleryRelease/blob/master/art/RecyclerViewCardGallery_blur.gif)

## Gradle 添加引用
```
compile 'com.github.pao11:RVCardGalleryRelease:v1.2.0' 
```

## Usage

调用`new PageScaleHelper().attachToRecyclerView(mRecyclerView);`扩展RecyclerView
```
final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
mRecyclerView.setLayoutManager(linearLayoutManager);
mRecyclerView.setAdapter(new CardAdapter());

// mRecyclerView绑定scale效果
mCardScaleHelper = new CardScaleHelper();
mCardScaleHelper.setSnapHelperType(CardScaleHelper.LINEAR_SNAP_HELPER);
mCardScaleHelper.attachToRecyclerView(mRecyclerView);
```

在adapter相应的位置调用
```
mCardAdapterHelper.onCreateViewHolder(parent, itemView);
mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
```

## Apk download
![app_debug.apk](https://github.com/pao11/RVCardGalleryRelease/blob/master/art/app-debug.apk?raw=true)


##更新记录


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