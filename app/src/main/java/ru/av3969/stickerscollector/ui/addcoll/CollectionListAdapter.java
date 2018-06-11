package ru.av3969.stickerscollector.ui.addcoll;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {

    private List<CatalogCollection> catalogCollectionList;

    private OnItemClickListener listener;

    private Resources resources;

    CollectionListAdapter(List<CatalogCollection> catalogCollectionList, Context context) {
        this.catalogCollectionList = catalogCollectionList;
        this.resources = context.getResources();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        @BindView(R.id.textTitle)
        TextView textTitle;

        @BindView(R.id.textStickersOrCards)
        TextView textStickersOrCards;

        @BindView(R.id.textNumberOfStickers)
        TextView textNumberOfStickers;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CatalogCollection catalogCollection = catalogCollectionList.get(position);
                        listener.onItemClick(catalogCollection.getId());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogCollection catalogCollection = catalogCollectionList.get(position);
        holder.textTitle.setText(catalogCollection.getTitle());
        @StringRes int resStickersOrCards = catalogCollection.getStype().equals(CatalogCollection.stickerType)
                ? R.string.stickersGenitive : R.string.cardsGenitive;
        String strStickersOrCards = String.format(" %s: ", resources.getString(resStickersOrCards));
        holder.textStickersOrCards.setText(strStickersOrCards);
        holder.textNumberOfStickers.setText(String.valueOf(catalogCollection.getSize()));
    }

    @Override
    public int getItemCount() {
        return catalogCollectionList.size();
    }

    public void replaceDataSet(List<CatalogCollection> catalogCollectionList) {
        this.catalogCollectionList = catalogCollectionList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Long collectionId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
