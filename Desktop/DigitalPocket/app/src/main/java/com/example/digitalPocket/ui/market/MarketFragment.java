package com.example.digitalPocket.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalPocket.ui.MyNftActivity;
import com.example.digitalPocket.R;
import com.example.digitalPocket.adapter.NFTAdapter;
import com.example.digitalPocket.dao.UserDao;
import com.example.digitalPocket.data.model.NFT;
import com.example.digitalPocket.ui.NftDetailActivity;
import com.example.digitalPocket.ui.login.LoggedUser;

import java.util.Iterator;

public class MarketFragment extends Fragment {

    private MarketViewModel marketViewModel;
    private NFTAdapter nftAdapter;
    private ImageView mineView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        marketViewModel = new ViewModelProvider(this).get(MarketViewModel.class);
        View root = inflater.inflate(R.layout.fragment_market, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.NFTRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));  // 设置列数为2
        nftAdapter = new NFTAdapter(null);
        recyclerView.setAdapter(nftAdapter);
        nftAdapter.setOnItemClickListener(new NFTAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NFT nft) {
                Intent intent = new Intent(getActivity(), NftDetailActivity.class);
                intent.putExtra("flag","buy");
                intent.putExtra("nft", nft);
                startActivityForResult(intent, 1);  // 启动 NftDetailActivity
            }
        });
        mineView=root.findViewById(R.id.mineview);
        mineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MyNftActivity.class);
                startActivity(intent);
            }
        });

        observeViewModelData();

        return root;
    }

    private void observeViewModelData() {
        marketViewModel.getNFTList().observe(getViewLifecycleOwner(), nfts -> {
            if (nfts != null) {
                String userAddress = LoggedUser.loggedUserAddress;
                Iterator<NFT> iterator = nfts.iterator();
                while (iterator.hasNext()) {
                    NFT nft = iterator.next();
                    if (nft.getOwner().equals(userAddress)) {
                        iterator.remove();
                    }
                }
                nftAdapter.setNftList(nfts);
            } else {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            // 重新加载数据
            marketViewModel.loadNftList();  // 确保 marketViewModel 有加载数据的方法
        }
    }
}
