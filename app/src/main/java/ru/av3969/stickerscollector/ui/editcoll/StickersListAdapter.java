package ru.av3969.stickerscollector.ui.editcoll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class StickersListAdapter extends RecyclerView.Adapter<StickersListAdapter.ViewHolder> {

    private List<StickerVO> stickers;

    private Boolean checkRemains = false;

    StickersListAdapter(List<StickerVO> stickers) {
        this.stickers = stickers;
    }

    StickersListAdapter(List<StickerVO> stickers, Boolean checkRemains) {
        this(stickers);
        this.checkRemains = checkRemains;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.imageButtonPlus)
        ImageButton btPlus;
        @BindView(R.id.imageButtonMinus)
        ImageButton btMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btPlus.setOnClickListener(l -> {
                StickerVO stiker = stickers.get(getAdapterPosition());
                StickerVO linkedSticker = stiker.getLinkedSticker();
                if(checkRemains) {
                    if(linkedSticker != null && linkedSticker.getQuantity() > stiker.getQuantity())
                        stiker.incQuantity();
                } else {
                    stiker.incQuantity();
                }

                quantity.setText(String.valueOf(stiker.getQuantity()));
            });
            btMinus.setOnClickListener(l -> {
                StickerVO stiker = stickers.get(getAdapterPosition());
                if(stiker.getQuantity() > 0) {
                    stiker.decQuantity();
                    quantity.setText(String.valueOf(stiker.getQuantity()));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StickerVO stiker = stickers.get(position);

        holder.number.setText(stiker.getNumber());
        holder.name.setText(stiker.getName());
        holder.type.setText(stiker.getType());
        holder.quantity.setText(String.valueOf(stiker.getQuantity()));

        if(stiker.getStickerId() == 0L) {
            holder.btPlus.setVisibility(View.INVISIBLE);
            holder.btMinus.setVisibility(View.INVISIBLE);
            holder.quantity.setVisibility(View.INVISIBLE);
        } else {
            holder.btPlus.setVisibility(View.VISIBLE);
            holder.btMinus.setVisibility(View.VISIBLE);
            holder.quantity.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public void replaceDataSet(List<StickerVO> stickers) {
        this.stickers = stickers;
        notifyDataSetChanged();
    }

    public List<StickerVO> getDataSet() {
        return stickers;
    }
}
