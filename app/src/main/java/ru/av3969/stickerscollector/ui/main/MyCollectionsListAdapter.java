package ru.av3969.stickerscollector.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;

public class MyCollectionsListAdapter extends RecyclerView.Adapter<MyCollectionsListAdapter.ViewHolder> {

    private List<CollectionVO> collections;
    private List<CollectionVO> removedCollections;

    private OnItemClickListener listener;

    private boolean editMode = false;

    MyCollectionsListAdapter() {
        collections = new ArrayList<>();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textTitle)
        TextView title;

        @BindView(R.id.remains_to_collect)
        TextView remains_to_collect;

        @BindView(R.id.quantity)
        TextView quantity;

        @BindView(R.id.imageMenu)
        ImageView imageMenu;

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
        holder.quantity.setText(String.format(Locale.US,"%d (%d)", collection.getUnique(), collection.getQuantity()));
        holder.remains_to_collect.setText(String.valueOf(collection.getSize() - collection.getUnique()));
        holder.imageMenu.setVisibility(editMode ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public void replaceDataSet(List<CollectionVO> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public void swapItem(int from, int to) {
        //Обмен порядком сортировки
        CollectionVO a = collections.get(from);
        CollectionVO b = collections.get(to);
        Long aOrder = a.getOrder();
        a.setOrder(b.getOrder());
        b.setOrder(aOrder);

        Collections.swap(collections, from, to);
        notifyItemMoved(from, to);
    }

    public void removeItem(int pos) {
        if(removedCollections != null) removedCollections.add(collections.get(pos));
        collections.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
        if(editMode) removedCollections = new ArrayList<>();
    }

    public List<CollectionVO> getRemovedCollections() {
        return removedCollections;
    }

    public interface OnItemClickListener {
        void onItemClick(Long parentCollectionId, Long collectionId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
