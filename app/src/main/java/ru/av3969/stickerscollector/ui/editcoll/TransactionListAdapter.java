package ru.av3969.stickerscollector.ui.editcoll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.db.entity.Transaction;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    List<Transaction> transactions;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");

    public TransactionListAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.title.setText(transaction.getTitle());
        holder.date.setText(simpleDateFormat.format(transaction.getDate()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void replaceDataSet(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }
}
