package ru.av3969.stickerscollector.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ru.av3969.stickerscollector.App;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.di.component.ActivityComponent;
import ru.av3969.stickerscollector.di.component.DaggerActivityComponent;
import ru.av3969.stickerscollector.di.module.ActivityModule;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App app = (App) getApplication();
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(app.getAppComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

}
