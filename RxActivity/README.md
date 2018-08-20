# RxActivity
Let's use `Activity#startActivityForResult` and `Activity#requestPermission` in Reactive way

## Requirements
- Knowledge of RxJava2
- Know why [OnActivityResult](http://developer.android.com/intl/es/training/basics/intents/result.html) is [breaking something](http://blog.danlew.net/2015/03/02/dont-break-the-chain/).

## Usage
- Find your `startActivityForResult` logic and change it like following:

```java
// Before
startActivityForResult(intent, MY_REQUEST_CODE);

// After (Sorry, but we need a dependency to context)
RxActivity.startActivityForResult(context, intent);
```

- Migrate your `onActivityResult` callbacks into `subscribe` after `RxActivity#startActivityForResult`:

```java
// Before
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (MY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
        // ... result handling ...
    }
}

// After
RxActivity.startActivityForResult(context, intent)
    .subscribe((Pair<Integer, Intent>) result -> {
        if (Activity.RESULT_OK == result.first) {
            // ... result handling ...
        }
    });
```

## Development background - tl;dr
As ReactiveX and MVP design pattern is introduced and adopted by many Android developers, chances of calling `startActivityForResult` or `requestPermission` at View logic are increasing which are not `Activity` or `Fragment`s.

Sadly, Android platform is not offering us easy mechanism to write callback logic near those result-requesting methods, thus it forcing us to break Reactive chaining. Let's see an example of the situation below:

```java
public class MyFriendsListActivity extends Activity {
    /*
     * This Activity takes multiple responsibilities thus we separated
     * related tasks as View and Presenter.
     */
    private FriendListPresenter friendListPresenter;
    // ... many other presenters ...
    private CameraPresenter     cameraPresenter;
        
    protected void onCreate(Bundle savedInstanceState) {
        this.friendListPresenter = new FriendListPresenter(new FriendListView(this));
        // ... many other presenters ...
        this.cameraPresenter     = new CameraPresenter(new CameraView(this));
    }
    
    /* ... maybe hundreds of codes ... */
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch(requestCode) {
                case CameraView.REQUEST_CODE_TAKE_PHOTO:
                    /* 
                     * This method will make our cameraPresenter a bit cohesive 
                     * unless we handle the result directly at here
                     */
                    cameraPresenter.onActivityResult(resultCode, data);
            }
        }
    }
}
    
class CameraView {  /* ... some initialisation codes ... */  }
    
class CameraPresenter {
    // Must not be same across all presenters used in MyFriendListActivity
    static final int REQUEST_CODE_TAKE_PHOTO = 9999;
    
    CameraPresenter(CameraView cameraView) {
        cameraView.getTakePhotoButton().setOnClickListener(view -> {
            Context uiContext = view.getContext();
            Intent intent = new Intent(uiContext, CameraActivity.class);
            /*
             * Now, if we wonder how this result is handled,
             * we must search whole onActivityResult methods that are
             * using REQUEST_CODE_TAKE_PHOTO. 
             * 
             * Even if we don't using `goto`s, this mechanism makes
             * same effect - forcing us to make spaghetti code!
             */
            uiContext.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        });
    }
    
    /*
     * Maybe we can guess this method will be called after 'taking photo' 
     * is finished. However, if we want to ENSURE that will be happen,
     * we have to track down that which logic in somewhere actually calls this.
     */
    void onActivityResult(int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            // ....
        }
    }
}
```

MyFriendListActivity contains many features and complex since it's a business requirement and a developer want to reduce its complexity, he/she bound logic as Presenter-View form. Let's have a look of an arbitary logic that invokes external camera  and handling its result.

Blame to the dependency between `startActivityForResult` - `onActivityResult` methods, now the developer wrote a spaghetti code without using any `goto`s. `requestPermissions` - `onRequestPermissionsResult` pair is not that much different although we did not demonstrated it in the code above. 😡

Another tidious thing is we have to manage all `requestCode` values are not duplicate all over presenters bound to same Activity - `MyFriendsListActivity`, in this case.

RxActivity is made to mitigate this awkwardnesses.

## How can we make this more greater
Rewriting the example code with RxActivity will be as follows:

```java
public class MyFriendsListActivity extends Activity {
    /*
     * This Activity takes multiple responsibilities thus we separated
     * related tasks as View and Presenter.
     */
    private FriendListPresenter friendListPresenter;
    // ... many other presenters ...
    private CameraPresenter     cameraPresenter;
        
    protected void onCreate(Bundle savedInstanceState) {
        this.friendListPresenter = new FriendListPresenter(new FriendListView(this));
        // ... many other presenters ...
        this.cameraPresenter     = new CameraPresenter(new CameraView(this));
    }
    
    /* ... maybe hundreds of codes ... */
    // we don' need onActivityResult inheritance anymore
}
    
class CameraView {  /* ... some initialisation codes ... */  }
    
class CameraPresenter {
    CameraPresenter(CameraView cameraView) {
        View getTakePhotoButton = cameraView.getTakePhotoButton();
        Context uiContext = getTakePhotoButton.getContext();
        /*
         * Now we can see request and result handling logic are tied together at
         * same place. A bonus is we don't need to manage request codes any further!
         */
        Intent intent = new Intent(uiContext, CameraActivity.class);
        RxActivity.startActivityForResult(uiContext, intent)
            .subscribe((Pair<Integer, Intent>) result -> {
                int resultCode = result.first;
                Intent data = result.second;
  		        if (Activity.RESULT_OK == resultCode) {
		            // ....
		        }
            })
            .takeUntil(ActivityLifeCycles.ON_DESTROY);
    }
}
```

As we can see, we don't need to shit around with any `onActivityResult`s anymore. Code is much shorter too! 😄

## Licences
Copyright 2017 Francesco Jo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
