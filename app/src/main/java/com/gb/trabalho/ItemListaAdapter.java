package com.gb.trabalho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.ItemLista;
import com.gb.trabalho.R;

import java.util.List;

public class ItemListaAdapter extends RecyclerView.Adapter<ItemListaAdapter.ViewHolder> {
    private List<ItemLista> itemList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItemLista item);
    }

    public ItemListaAdapter(Context context, List<ItemLista> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtValor, txtDescricao, txtData;
        CardView cardView;
        int tipo;

        public ViewHolder(View view) {
            super(view);
            txtValor = view.findViewById(R.id.txt_valor);
            txtDescricao = view.findViewById(R.id.txt_descricao);
            txtData = view.findViewById(R.id.txt_data);
            cardView = (CardView) view;
        }

        public void bind(ItemLista item, OnItemClickListener listener) {
            txtValor.setText(item.getValor());
            txtDescricao.setText(item.getDescricao());
            txtData.setText(item.getData());
            int tipo = item.getTipo(); // 0=despesa 1=receita
            if (tipo == 1) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.receita));
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.despesa));
            }
            cardView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    @Override
    public ItemListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemListaAdapter.ViewHolder holder, int position) {
        holder.bind(itemList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
