package ru.av3969.stickerscollector.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;

public class MyCollectionsListAdapter extends RecyclerView.Adapter<MyCollectionsListAdapter.ViewHolder> {

    private List<CollectionVO> collections;

    private OnItemClickListener listener;

    MyCollectionsListAdapter(List<CollectionVO> collections) {
        this.collections = collections;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textTitle)
        TextView title;

        @BindView(R.id.textStickersOrCards)
        TextView textStickersOrCards;

        @BindView(R.id.quantity)
        TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(l -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CollectionVO collectionVO = collections.get(position);
                        listener.onItemClick(collectionVO.getCollectionId(), collectionVO.getId());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_my_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionVO collection = collections.get(position);
        holder.title.setText(collection.getTitle());
        holder.textStickersOrCards.setText(collection.getStype().equals(CatalogCollection.stickerType)
                ? R.string.quantity_of_stickers : R.string.quantity_of_cards);
        holder.quantity.setText(String.format(Locale.getDefault(),"%d (%d)", collection.getUnique(), collection.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public void replaceDataSet(List<CollectionVO> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Long parentCollectionId, Long collectionId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
