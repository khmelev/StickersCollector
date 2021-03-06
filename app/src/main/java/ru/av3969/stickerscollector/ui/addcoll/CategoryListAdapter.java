package ru.av3969.stickerscollector.ui.addcoll;

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
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private List<CatalogCategory> catalogCategoryList;

    private OnItemClickListener listener;

    CategoryListAdapter(List<CatalogCategory> catalogCategoryList) {
        this.catalogCategoryList = catalogCategoryList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textItem1)
        TextView textItem1;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CatalogCategory catalogCategory = catalogCategoryList.get(position);
                        listener.onItemClick(itemView, catalogCategory.getId(),
                                catalogCategory.getParentId());
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogCategory category = catalogCategoryList.get(position);
        holder.textItem1.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        return catalogCategoryList.size();
    }

    public void replaceDataSet(List<CatalogCategory> catalogCategoryList) {
        this.catalogCategoryList = catalogCategoryList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, Long targetId, Long parentId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
