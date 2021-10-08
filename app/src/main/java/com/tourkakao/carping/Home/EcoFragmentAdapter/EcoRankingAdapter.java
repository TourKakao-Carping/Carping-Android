package com.tourkakao.carping.Home.EcoFragmentAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tourkakao.carping.Home.EcoDataClass.EcoRanking;
import com.tourkakao.carping.databinding.EcoRankingListItemBinding;

import java.util.ArrayList;

public class EcoRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<EcoRanking> ecoRankings;
    EcoRankingListItemBinding ecoRankingListItemBinding;
    public EcoRankingAdapter(Context context, ArrayList<EcoRanking> ecoRankings){
        this.context=context;
        this.ecoRankings = ecoRankings;
    }

    public class EcoRankingViewHoler extends RecyclerView.ViewHolder{
        private EcoRankingListItemBinding binding;
        public EcoRankingViewHoler(EcoRankingListItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(EcoRanking ecoRanking, int position){
            binding.rank.setText(Integer.toString(position+1));
            Glide.with(context).load(ecoRanking.getImage())
                    .transform(new CenterCrop(), new RoundedCorners(100))
                    .into(binding.image);
            binding.name.setText(ecoRanking.getUsername());
            Glide.with(context).load(ecoRanking.getBadge()).into(binding.badge);
            binding.level.setText("LV."+Integer.toString(ecoRanking.getLevel()));
            binding.ecoCount.setText(ecoRanking.getEco_count()+"개");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ecoRankingListItemBinding= ecoRankingListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EcoRankingAdapter.EcoRankingViewHoler(ecoRankingListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EcoRankingAdapter.EcoRankingViewHoler vh=(EcoRankingAdapter.EcoRankingViewHoler)holder;
        vh.bind(ecoRankings.get(position),position);
    }

    @Override
    public int getItemCount() {
        return ecoRankings ==null?0: ecoRankings.size();
    }
}
