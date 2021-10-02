package com.tourkakao.carping.Post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.tabs.TabLayout;
import com.tourkakao.carping.MypageMainActivities.Fragment.LikeCarpingFragment;
import com.tourkakao.carping.MypageMainActivities.Fragment.MyCarpingFragment;
import com.tourkakao.carping.Post.DTO.PostInfoDetail;
import com.tourkakao.carping.Post.Fragment.IntroduceFragment;
import com.tourkakao.carping.Post.Fragment.QuestionFragment;
import com.tourkakao.carping.Post.Fragment.RecommendationFragment;
import com.tourkakao.carping.Post.Fragment.ReviewFragment;
import com.tourkakao.carping.Post.ViewModel.PostDetailViewModel;
import com.tourkakao.carping.Post.ViewModel.PostListViewModel;
import com.tourkakao.carping.R;
import com.tourkakao.carping.databinding.ActivityPostCategoryBinding;
import com.tourkakao.carping.databinding.ActivityPremiumPostBinding;

public class PremiumPostActivity extends AppCompatActivity {
    private ActivityPremiumPostBinding binding;
    private Fragment selected;
    private PostDetailViewModel viewModel;
    private Context context;
    private int pk;
    private int likeCount;
    private int userpost_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPremiumPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=getApplicationContext();

        viewModel =new ViewModelProvider(this).get(PostDetailViewModel.class);
        viewModel.setContext(this);

        Intent intent=getIntent();
        pk=intent.getIntExtra("pk",0);
        viewModel.setPk(pk);
        viewModel.loadInfoDetail();

        settingToolbar();
        settingTab();
        settingInfo();
        pushLike();
        clickPayment();
    }

    void settingToolbar(){
        Toolbar toolbar=binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void settingTab(){
        Fragment introduceFragment=new IntroduceFragment();
        Fragment questionFragment=new QuestionFragment();
        Fragment recommendationFragment=new RecommendationFragment();
        Fragment reviewFragment=new ReviewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.post_container,introduceFragment).commit();
        binding.tabs.addTab(binding.tabs.newTab().setText("소개"));
        binding.tabs.addTab(binding.tabs.newTab().setText("리뷰"));
        binding.tabs.addTab(binding.tabs.newTab().setText("문의"));
        binding.tabs.addTab(binding.tabs.newTab().setText("추천"));

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition() ;
                if (pos == 0) { // 첫 번째 탭 선택.
                    selected=introduceFragment;
                }
                if(pos==1){
                    selected=reviewFragment;
                }
                if(pos==2){
                    selected=questionFragment;
                }
                if(pos==3){
                    selected=recommendationFragment;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.post_container,selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void settingInfo(){
        viewModel.getPostInfo().observe(this, new Observer<PostInfoDetail>() {
            @Override
            public void onChanged(PostInfoDetail postInfoDetail) {
                Glide.with(context).load(postInfoDetail.getAuthor_profile())
                        .transform(new CenterCrop(), new RoundedCorners(100))
                        .into(binding.profile);
                binding.name.setText(postInfoDetail.getAuthor_name());
                Glide.with(context).load(postInfoDetail.getThumbnail()).into(binding.thumbnail);
                if(postInfoDetail.getPoint()!=0){
                    Glide.with(context).load(R.drawable.premium_mark).into(binding.premiumImage);
                }else{
                    Glide.with(context).load(R.drawable.free_mark).into(binding.premiumImage);
                }
                binding.star.setText("★ "+postInfoDetail.getTotal_star_avg());
                binding.title.setText(postInfoDetail.getTitle());
                binding.point.setText(Integer.toString(postInfoDetail.getPoint())+"원");
                if(postInfoDetail.getIs_liked().equals("false")){
                    Glide.with(context).load(R.drawable.like_mark).into(binding.likeMark);
                }else{
                    Glide.with(context).load(R.drawable.is_pushed_like).into(binding.likeMark);
                    binding.likeCount.setTextColor(Color.parseColor("#ff4e5e"));
                }
                likeCount=postInfoDetail.getLike_count();
                binding.likeCount.setText(Integer.toString(postInfoDetail.getLike_count()));
                userpost_id=postInfoDetail.getUserpost_id();
            }
        });

    }

    public void pushLike(){
        viewModel.getLikeStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("true")){
                    Glide.with(getApplicationContext()).load(R.drawable.is_pushed_like).into(binding.likeMark);
                    binding.likeCount.setTextColor(Color.parseColor("#ff4e5e"));
                }
                if(s.equals("false")){
                    Glide.with(getApplicationContext()).load(R.drawable.like_mark).into(binding.likeMark);
                }
            }
        });
        binding.likeMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getLikeStatus().getValue().equals("true")){
                    viewModel.cancelLike(pk);
                    likeCount=likeCount-1;
                    binding.likeCount.setTextColor(Color.parseColor("#000000"));
                    binding.likeCount.setText(Integer.toString(likeCount));
                }
                if(viewModel.getLikeStatus().getValue().equals("false")){
                    viewModel.postLike(pk);
                    likeCount=likeCount+1;
                    binding.likeCount.setTextColor(Color.parseColor("#ff4e5e"));
                    binding.likeCount.setText(Integer.toString(likeCount));
                }
            }
        });
    }

    void clickPayment(){
        binding.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PayActivity.class);
                intent.putExtra("id",userpost_id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadInfoDetail();
    }
}