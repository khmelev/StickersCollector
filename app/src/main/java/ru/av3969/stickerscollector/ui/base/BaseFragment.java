package ru.av3969.stickerscollector.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import ru.av3969.stickerscollector.App;
import ru.av3969.stickerscollector.di.component.DaggerFragmentComponent;
import ru.av3969.stickerscollector.di.component.FragmentComponent;
import ru.av3969.stickerscollector.di.module.FragmentModule;

public class BaseFragment extends Fragment {

    private FragmentComponent fragmentComponent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        App app = (App) getActivity().getApplication();
        fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule())
                .appComponent(app.getAppComponent())
                .build();
    }

    public FragmentComponent getFragmentComponent() {
        return fragmentComponent;
    }
}
