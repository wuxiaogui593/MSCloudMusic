package com.example.hp.mycloudmusic.ui.onLine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.TabFragmentPagerAdapter;
import com.example.hp.mycloudmusic.custom.SimpleViewPagerIndicator;
import com.example.hp.mycloudmusic.custom.StickNavLayout;
import com.example.hp.mycloudmusic.fragment.callback.ClickListener;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.fragment.instance.ADSongListFragment;
import com.example.hp.mycloudmusic.fragment.instance.PlayMusicFragment;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.ui.BaseActivity;
import com.example.hp.mycloudmusic.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ArtistDetailActivity extends BaseActivity implements SimpleViewPagerIndicator.IndicatorClickListener, StickNavLayout.MyStickyListener, View.OnClickListener, ClickListener {
    public static final String ARTIST = "ARTIST";
    public static final String BUNDLE = "BUNDLE";
    public static final String[] titles = new String[]{"单曲","专辑","MV","歌手信息"};


    @Bind(R.id.id_stickynavlayout)
    StickNavLayout mStickNavLayout;
    @Bind(R.id.id_stickynavlayout_avatar)
    ImageView iv_avatar;
    @Bind(R.id.id_stickynavlayout_indicator)
    SimpleViewPagerIndicator mIndicator;
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.search_back)
    ImageView ivBack;

    private TabFragmentPagerAdapter mAdapter;
    private Artist artist;

    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_artist_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE);
        artist = bundle.getParcelable(ARTIST);
        if(artist == null){
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        //不可行
//        mViewPager.layout(mViewPager.getLeft(),mViewPager.getTop()+300,mViewPager.getRight(),mViewPager.getBottom()+300);
        //可行
//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
//        lp.topMargin = 300;
//        mViewPager.setLayoutParams(lp);
//        ViewGroup.LayoutParams layoutParams = iv_avatar.getLayoutParams();
//        layoutParams.height = layoutParams.height+500;
//        iv_avatar.setLayoutParams(layoutParams);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(this);

        mIndicator.setIndicatorClickListener(this);
        mIndicator.setTitles(titles);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position,positionOffset);
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        for(int i=0;i<titles.length;i++){
            mFragments.add(ADSongListFragment.Companion.newInstance(artist));
        }
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setCurrentItem(0);

        mStickNavLayout.setScrollListener(this);

        int height = DisplayUtil.getScreenHeight(ArtistDetailActivity.this)-DisplayUtil.dip2px(ArtistDetailActivity.this,65)-DisplayUtil.dip2px(ArtistDetailActivity.this,40);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.height = height;
        mViewPager.setLayoutParams(layoutParams);
    }

    public static void toArtistDetailActivity(Context context, Artist artist){
        Intent intent = new Intent(context,ArtistDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTIST,artist);
        intent.putExtra(BUNDLE,bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClickItem(int k) {
        mViewPager.setCurrentItem(k);
    }


    private float getMobileWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    @Override
    public void imageScale(float bottom) {
        float height = DisplayUtil.dip2px(ArtistDetailActivity.this,220);
        float mScale = bottom/height;
        float width = getMobileWidth()*mScale;
        float dx = (width-getMobileWidth())/2;
        iv_avatar.layout((int)(0-dx),0,(int)(getMobileWidth()+dx),(int)bottom);
    }

    @Override
    public void setAlpha(float alpha) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocalItemClick(int adapterPosition) {

    }

    @Override
    protected void onStop() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PlayMusicFragment fragment = FragmentFactory.getInstance(this).getmPlayMusicFragment();
        if(fragment != null && fragment.isAdded()){
            transaction.remove(fragment);
        }
        transaction.commitAllowingStateLoss();
        super.onStop();
    }
}