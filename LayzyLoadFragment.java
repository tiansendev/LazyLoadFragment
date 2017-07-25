package com.esimtek.edas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.esimtek.edas.bean.settingbean.SettingsRoot;
import com.esimtek.edas.constant.Constants;
import com.esimtek.edas.mvpbaseinterfc.IView;
import com.esimtek.edas.utils.SharedPrefereceHelp;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment+viewpager需要懒加载
 *
 * Created by ts on 2017/5/9 0009.
 */

public abstract class LazyLoadFragment extends RxFragment implements IView, View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    /**
     * 控件是否初始化完成
     */
    private boolean isViewCreated;
    /**
     * 数据是否已加载完毕
     */
    private boolean isLoadDataCompleted;
    public View view;
    private Unbinder knife;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayout(), container, false);
        knife = ButterKnife.bind(this,view);
        initViews(view);
        registerEventBus();
        isViewCreated = true;
        return view;
    }

    protected void registerEventBus() {};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPresenter();
    }

    protected void bindPresenter() {};

    @Override
    public void onDestroy() {
        super.onDestroy();
        knife.unbind();
        unRegisterEventBus();
    }

    private void unRegisterEventBus() {}

    protected abstract int getLayout();
    protected void initViews(View view) {};

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            isLoadDataCompleted = true;
            initData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getUserVisibleHint()) {
            isLoadDataCompleted = true;
            initData();
        }
        initEvent();
    }

    /**
     * 子类在此方法中实现数据的初始化
     */
    public  void initData() {}

    /**
     * 子类可以复写此方法初始化事件
     */
    protected  void initEvent(){}

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 保存配置数据
     */
    public void saveConfig(SettingsRoot root) {
        if (root != null) {
            SharedPrefereceHelp.getInstance(getContext()).putSettingConfig(root);
        }
    }

    public SettingsRoot getConfig() {
        return SharedPrefereceHelp.getInstance(getContext()).getSettingConfig();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
