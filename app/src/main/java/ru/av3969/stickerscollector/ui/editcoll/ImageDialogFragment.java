package ru.av3969.stickerscollector.ui.editcoll;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;

public class ImageDialogFragment extends DialogFragment {
    private static String IMG_URL_KEY = "image_url";

    ImageView collCover;

    public ImageDialogFragment() {
    }

    public static ImageDialogFragment newInstance(String imageUrl) {
        ImageDialogFragment imageDialogFragment = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(IMG_URL_KEY, imageUrl);
        imageDialogFragment.setArguments(args);
        return imageDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        collCover = view.findViewById(R.id.collCover);
        collCover.setOnClickListener(l -> {
            getDialog().dismiss();
        });

        String imageUrl = getArguments().getString(IMG_URL_KEY, "");

//        Glide.with(view).load(imageUrl).apply(RequestOptions.overrideOf(500, 600)).into(collCover);
        Glide.with(view).load(imageUrl).apply(RequestOptions.centerCropTransform()).into(collCover);
    }
}
