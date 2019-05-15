package com.example.sorena.wanandroidapp.util;

public abstract class LazyFragment extends BaseFragment
{
    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser,在Fragment进入可见或不可见时调用
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        LogUtil.d("日志",this + ":isVisibleToUser:" + false);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();

    protected void onInvisible(){

    }




}
