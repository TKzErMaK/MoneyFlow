package com.gb.trabalho.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.trabalho.Domain.ItemLista;
import com.gb.trabalho.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            txtValor.setText(nf.format(item.getValor()));
            txtDescricao.setText(item.getDescricao());
            txtData.setText(item.getData());

            try{
                if (item.getPrazo() > 0 && item.getTipo() == 0)
                    txtValor.setText(String.valueOf(item.getPrazo()));
            } catch (Exception ignored) {};

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
