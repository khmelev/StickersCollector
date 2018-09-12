package ru.av3969.stickerscollector.ui.editcoll;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private List<TransactionVO> transactions;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private OnTranSwitchChangeListener switchListener;
    private OnTranClickListener clickTitleListener;
    private OnTranStickerClickListener clickStickerListener;

    public TransactionListAdapter(List<TransactionVO> transactions,
                                  OnTranSwitchChangeListener switchListener,
                                  OnTranClickListener clickTitleListener,
                                  OnTranStickerClickListener clickStickerListener) {
        this.transactions = transactions;
        this.switchListener = switchListener;
        this.clickTitleListener = clickTitleListener;
        this.clickStickerListener = clickStickerListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.tranActiveSwitch)
        Switch tranActiveSwitch;
        @BindView(R.id.transDescription)
        LinearLayout transDescription;
        @BindView(R.id.tranStickerList)
        TextView tranStickerList;

        View.OnClickListener editTransClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            editTransClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickTitleListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickTitleListener.loadTransactionRow(transactions.get(position));
                        }
                    }
                }
            };
            transDescription.setOnClickListener(editTransClickListener);
            tranStickerList.setOnClickListener(editTransClickListener);

            tranStickerList.setOnLongClickListener(v -> {
                if (clickStickerListener != null) {
                    clickStickerListener.copyToClipboard(tranStickerList.getText());
                }
                return true;
            });
            tranActiveSwitch.setOnCheckedChangeListener((view, isChecked) -> {
                if (switchListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        TransactionVO transaction = transactions.get(position);
                        if(transaction.getActive() != isChecked)
                            switchListener.deactivateTransaction(transaction);
                    }
                }
            });
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
        TransactionVO transaction = transactions.get(position);
        holder.title.setText(transaction.getTitle());
        holder.date.setText(simpleDateFormat.format(transaction.getDate()));
        holder.tranActiveSwitch.setChecked(transaction.getActive());
        holder.tranStickerList.setText(transaction.getTransStickersText());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void replaceDataSet(List<TransactionVO> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public interface OnTranSwitchChangeListener {
        void deactivateTransaction(TransactionVO transaction);
    }

    public interface OnTranClickListener {
        void loadTransactionRow(TransactionVO transaction);
    }

    public interface OnTranStickerClickListener {
        void copyToClipboard(CharSequence text);
    }
}
