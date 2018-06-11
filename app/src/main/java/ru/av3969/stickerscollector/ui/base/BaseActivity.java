package ru.av3969.stickerscollector.ui.base;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ru.av3969.stickerscollector.R;

public class BaseActivity extends AppCompatActivity {

    private void replaceContentFragment(BaseFragment fragment, Boolean addToBackStack) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment);

        if (addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }
}
