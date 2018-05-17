package com.prateek.crypto.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.prateek.crypto.R;
import com.prateek.crypto.models.Coin;
import com.prateek.crypto.models.CoinListResponse;
import com.prateek.crypto.network.NetworkClient;
import com.prateek.crypto.network.NetworkService;
import com.prateek.crypto.prefrences.UserPrefrenceManager;
import com.prateek.crypto.utilities.CoinListSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CryptoListChildFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private CoinAdapter mAdapter;
    private NetworkService retrofit;
    private ArrayList<Coin> coinList;
    private FrameLayout root;

    public CryptoListChildFragment(){

    }

    public static CryptoListChildFragment newInstance() {
         return new CryptoListChildFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto_list_child, container, false);
        mRecyclerView = view.findViewById(R.id.coin_list);
        coinList=new ArrayList<Coin>();
        setList();
        mProgressBar = view.findViewById(R.id.progress_bar_coins);
        root = view.findViewById(R.id.main_view);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getContext()!= null) {
            setList();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofit = NetworkClient.getClient().create(NetworkService.class);
        if(CoinListSingleton.getInstance().getList().size()==0){
            networkCall();
        }
        else {
            coinList=  CoinListSingleton.getInstance().getList();
            setList();
        }

    }

    private void networkCall() {
        Call<CoinListResponse> call = retrofit.getAllCoinList();
        call.enqueue(new Callback<CoinListResponse>() {
            @Override
            public void onResponse(Call<CoinListResponse> call, Response<CoinListResponse> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                CoinListResponse coinListResponse = response.body();
                coinList = coinListResponse.getData().getCoinsList();
                setList();
            }

            @Override
            public void onFailure(Call<CoinListResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(root, "Network Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RELOAD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                networkCall();
                            }
                        });
                snackbar.show();

            }
        });


    }

    private void setList() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CoinAdapter(coinList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class CoinHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ToggleButton button;
        private Coin mCoin;
        private Set<String> set;

        public CoinHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_coin, parent, false));
            textView = itemView.findViewById(R.id.text_view);
            button = itemView.findViewById(R.id.myToggleButton);
        }

        public void bind(Coin coin, int pos) {
            set = UserPrefrenceManager.getFavourite(getContext());
            mCoin = coin;
            textView.setText(mCoin.getCoinName());
            button.setTag(pos);
            if (set.contains(mCoin.getId())) {
                button.setChecked(true);
            } else
                button.setChecked(false);

        }
    }

    private class CoinAdapter extends RecyclerView.Adapter<CoinHolder> {
        private Set<String> set;
        private ArrayList<Coin> mCoins;

        public CoinAdapter(ArrayList<Coin> coins) {
            this.mCoins=coins;
            set = UserPrefrenceManager.getFavourite(getContext());
        }

        private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set = UserPrefrenceManager.getFavourite(getContext());
                int pos = (int) buttonView.getTag();

                if (set.contains(mCoins.get(pos).getId())) {
                    UserPrefrenceManager.removeFavourite(getContext(), mCoins.get(pos).getId());
                    notifyItemChanged(pos);

                } else {
                    UserPrefrenceManager.setFavourite(getContext(), mCoins.get(pos).getId());
                    notifyItemChanged(pos);

                }
            }
        };

        @NonNull
        @Override
        public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CoinHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
            Coin coin = mCoins.get(position);
            holder.button.setOnCheckedChangeListener(null);
            holder.bind(coin, position);
            holder.button.setOnCheckedChangeListener(checkedListener);
        }

        @Override
        public int getItemCount() {
            return mCoins.size();
        }
    }
}
