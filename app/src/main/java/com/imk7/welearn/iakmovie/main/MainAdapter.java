package com.imk7.welearn.iakmovie.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imk7.welearn.iakmovie.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder>{

    private List<MainDao> mData;

    public MainAdapter(List<MainDao> mData) {
        this.mData = mData;
    }

    @Override
    public MainAdapter.MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main,parent,false);
        MainHolder holder = new MainHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainHolder holder, final int position) {
        holder.titleRow.setText(mData.get(position).getTitle());

        Picasso.with(holder.imageRow.getContext())
                .load(mData.get(position).getImageUrl())
                .into(holder.imageRow);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), mData.get(position).getTitle() , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder {
        private ImageView imageRow;
        private TextView titleRow;

        public MainHolder(View itemView) {
            super(itemView);

            imageRow = itemView.findViewById(R.id.imageRow);
            titleRow = itemView.findViewById(R.id.titleRow);
        }
    }
}
