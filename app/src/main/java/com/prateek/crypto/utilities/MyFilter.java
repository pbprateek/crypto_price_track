package com.prateek.crypto.utilities;

import android.widget.Filter;

import com.prateek.crypto.fragments.CryptoListChildFragment;
import com.prateek.crypto.models.Coin;

import java.util.ArrayList;

public class MyFilter extends Filter {
    private final CryptoListChildFragment.CoinAdapter adapter;
    private final ArrayList<Coin> originalList;
    private final ArrayList<Coin> filteredList;

    public MyFilter(CryptoListChildFragment.CoinAdapter adapter,ArrayList<Coin> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = new ArrayList<>(originalList);
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase();
            for (Coin item : originalList) {
                if(item.getName().toLowerCase().equals(filterPattern)){
                    filteredList.add(item);

                }
            }
        }

        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.mCoins.clear();
        adapter.mCoins.addAll((ArrayList<Coin>) results.values);
        adapter.notifyDataSetChanged();

    }
}
