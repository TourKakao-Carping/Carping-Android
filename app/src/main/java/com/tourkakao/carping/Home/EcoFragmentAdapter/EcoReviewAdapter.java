package com.tourkakao.carping.Home.EcoFragmentAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tourkakao.carping.EcoCarping.Activity.EcoCarpingDetailActivity;
import com.tourkakao.carping.Home.EcoDataClass.EcoReview;
import com.tourkakao.carping.databinding.EcoReviewListItemBinding;

import java.util.ArrayList;

public class EcoReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<EcoReview> ecoReviews;
    EcoReviewListItemBinding ecoReviewListItemBinding;
    public EcoReviewAdapter(Context context, ArrayList<EcoReview> ecoReviews){
        this.context=context;
        this.ecoReviews = ecoReviews;
    }

    public class EcoReviewViewHoler extends RecyclerView.ViewHolder{
        private EcoReviewListItemBinding binding;
        public EcoReviewViewHoler(EcoReviewListItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
            binding.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, EcoCarpingDetailActivity.class);
                    intent.putExtra("pk",binding.pk.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        public void bind(EcoReview ecoReview){
            Glide.with(context)
                    .load(ecoReview.getImage1())
                    .transform(new CenterCrop(), new RoundedCorners(30))
                    .into(binding.image);
            binding.title.setText(ecoReview.getTitle());
            binding.content.setText(ecoReview.getText());
            binding.pk.setText(ecoReview.getId());
            binding.date.setText(ecoReview.getCreated_at());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ecoReviewListItemBinding= ecoReviewListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EcoReviewViewHoler(ecoReviewListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EcoReviewViewHoler vh=(EcoReviewAdapter.EcoReviewViewHoler)holder;
        vh.bind(ecoReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return ecoReviews ==null?0: ecoReviews.size();
    }
}
