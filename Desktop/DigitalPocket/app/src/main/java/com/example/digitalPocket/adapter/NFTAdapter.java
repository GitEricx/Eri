package com.example.digitalPocket.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.digitalPocket.R;
import com.example.digitalPocket.data.model.NFT;

import java.util.List;

public class NFTAdapter extends RecyclerView.Adapter<NFTAdapter.NFTViewHolder> {

    private List<NFT> nftList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NFT nft);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NFTAdapter(List<NFT> nftList) {
        this.nftList = nftList;
    }

    public void setNftList(List<NFT> nftList) {
        this.nftList = nftList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NFTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nft_item, parent, false);
        return new NFTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NFTViewHolder holder, int position) {
        NFT nft = nftList.get(position);
        holder.bind(nft, listener);
    }

    @Override
    public int getItemCount() {
        return nftList == null ? 0 : nftList.size();
    }

    static class NFTViewHolder extends RecyclerView.ViewHolder {
        private TextView costView;
        private TextView nameView;
        private ImageView nftImageView;

        public NFTViewHolder(@NonNull View itemView) {
            super(itemView);
            costView = itemView.findViewById(R.id.costView);
            nameView = itemView.findViewById(R.id.ownerView);
            nftImageView = itemView.findViewById(R.id.nftImageView);
        }

        public void bind(NFT nft, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(nft);
                    }
                }
            });

            costView.setText(String.valueOf(nft.getCost()));
            nameView.setText(nft.getName());

            String imageUrl = nft.getData();
            loadImage(nftImageView, imageUrl);
        }

        private void loadImage(ImageView imageView, String url) {
            // 首先尝试加载为 GIF
            Glide.with(imageView.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_dino)
                    .error(R.drawable.ic_mine)
                    .into(imageView);
        }
    }
}
