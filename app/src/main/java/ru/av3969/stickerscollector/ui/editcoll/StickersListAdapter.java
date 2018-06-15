package ru.av3969.stickerscollector.ui.editcoll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class StickersListAdapter extends RecyclerView.Adapter<StickersListAdapter.ViewHolder> {

    private List<StickerVO> stickers;

    public StickersListAdapter(List<StickerVO> stickers) {
        this.stickers = stickers;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.type)
        TextView type;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
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
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public void replaceDataSet(List<StickerVO> stickers) {
        this.stickers = stickers;
        notifyDataSetChanged();
    }
}
