package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;
import com.kongzue.dialogx.util.TextInfo;
import com.kongzue.dialogx.util.views.BlurView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.views.ProgressView;

import java.lang.ref.WeakReference;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/27 14:50
 */
public class WaitDialog extends BaseDialog {
    
    protected OnBindView<WaitDialog> onBindView;
    
    public enum TYPE {
        NONE,
        SUCCESS,
        WARNING,
        ERROR
    }
    
    protected static WeakReference<WaitDialog> me;
    protected CharSequence message;
    protected long tipShowDuration = 1500;
    protected float waitProgress = -1;
    protected int showType = -1;        //-1:Waitdialog 状态标示符，其余为 TipDialog 状态标示
    protected TextInfo messageTextInfo;
    
    private DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback;
    
    protected WaitDialog() {
        super();
        me = new WeakReference<>(this);
        cancelable = DialogX.cancelableTipDialog;
    }
    
    public static WaitDialog show(CharSequence message) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().message = message;
        me().showType = -1;
        if (dialogImpl != null) {
            dialogImpl.progressView.loading();
            setMessage(message);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.message = message;
            waitDialog.show();
            return waitDialog;
        }
    }
    
    public static WaitDialog show(int messageResId) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().preMessage(messageResId);
        me().showType = -1;
        if (dialogImpl != null) {
            dialogImpl.progressView.loading();
            setMessage(messageResId);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show();
            return waitDialog;
        }
    }
    
    public static WaitDialog show(CharSequence message, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(message);
        if (dialogImpl != null) {
            setMessage(message);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(message);
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(int messageResId, float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        me().preMessage(messageResId);
        if (dialogImpl != null) {
            setMessage(messageResId);
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.preMessage(messageResId);
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public static WaitDialog show(float progress) {
        DialogImpl dialogImpl = me().dialogImpl;
        me().showType = -1;
        if (dialogImpl != null) {
            me().setProgress(progress);
            return me();
        } else {
            WaitDialog waitDialog = new WaitDialog();
            waitDialog.show();
            waitDialog.setProgress(progress);
            return waitDialog;
        }
    }
    
    public float getProgress() {
        return waitProgress;
    }
    
    public WaitDialog setProgress(float waitProgress) {
        this.waitProgress = waitProgress;
        refreshUI();
        return this;
    }
    
    private View dialogView;
    
    public WaitDialog show() {
        super.beforeShow();
        dialogView = createView(R.layout.layout_dialogx_wait);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
        return this;
    }
    
    protected DialogImpl dialogImpl;
    
    public class DialogImpl implements DialogConvertViewInterface {
        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout bkg;
        public BlurView blurView;
        public RelativeLayout boxProgress;
        public ProgressViewInterface progressView;
        public RelativeLayout boxCustomView;
        public TextView txtInfo;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            blurView = convertView.findViewById(R.id.blurView);
            boxProgress = convertView.findViewById(R.id.box_progress);
            View progressViewCache = (View) style.overrideWaitTipRes().overrideWaitView(getContext(), isLightTheme());
            if (progressViewCache == null) {
                progressViewCache = new ProgressView(getContext());
            }
            progressView = (ProgressViewInterface) progressViewCache;
            boxProgress.addView(progressViewCache, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            boxCustomView = convertView.findViewById(R.id.box_customView);
            txtInfo = convertView.findViewById(R.id.txt_info);
            init();
            refreshView();
        }
        
        public void init() {
            if (messageTextInfo == null) messageTextInfo = DialogX.tipTextInfo;
            if (backgroundColor == -1) backgroundColor = DialogX.tipBackgroundColor;
            
            blurView.setRadiusPx(dip2px(15));
            boxRoot.setClickable(true);
            
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            int enterAnimResId = R.anim.anim_dialogx_default_enter;
                            Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                            enterAnim.setInterpolator(new DecelerateInterpolator());
                            bkg.startAnimation(enterAnim);
                            
                            boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null);
                            
                            getDialogLifecycleCallback().onShow(me());
                        }
                    });
                    
                    if (onBindView != null) onBindView.onBind(me.get(), onBindView.getCustomView());
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    dialogImpl = null;
                    getDialogLifecycleCallback().onDismiss(me());
                    me.clear();
                }
            });
            
            if (readyTipType != null) {
                progressView.noLoading();
                ((View) progressView).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showTip(readyTipType);
                    }
                }, 100);
            }
            
            boxRoot.setOnBackPressedListener(new OnBackPressedListener() {
                @Override
                public boolean onBackPressed() {
                    if (onBackPressedListener != null && onBackPressedListener.onBackPressed()) {
                        dismiss();
                        return false;
                    }
                    if (cancelable) {
                        dismiss();
                    }
                    return false;
                }
            });
        }
        
        private float oldProgress;
        
        public void refreshView() {
            if (style.overrideWaitTipRes() != null) {
                int overrideBackgroundColorRes = style.overrideWaitTipRes().overrideBackgroundColorRes(isLightTheme());
                if (overrideBackgroundColorRes == 0) {
                    overrideBackgroundColorRes = isLightTheme() ? R.color.dialogxWaitBkgDark : R.color.dialogxWaitBkgLight;
                }
                blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(overrideBackgroundColorRes) : backgroundColor);
                int overrideTextColorRes = style.overrideWaitTipRes().overrideTextColorRes(isLightTheme());
                if (overrideTextColorRes == 0) {
                    overrideTextColorRes = isLightTheme() ? R.color.white : R.color.black;
                }
                txtInfo.setTextColor(getResources().getColor(overrideTextColorRes));
                progressView.setColor(getResources().getColor(overrideTextColorRes));
                blurView.setUseBlur(style.overrideWaitTipRes().blurBackground());
            } else {
                if (isLightTheme()) {
                    blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(R.color.dialogxWaitBkgDark) : backgroundColor);
                    progressView.setColor(Color.WHITE);
                    txtInfo.setTextColor(Color.WHITE);
                } else {
                    blurView.setOverlayColor(backgroundColor == -1 ? getResources().getColor(R.color.dialogxWaitBkgLight) : backgroundColor);
                    progressView.setColor(Color.BLACK);
                    txtInfo.setTextColor(Color.BLACK);
                }
            }
            if (DialogX.tipProgressColor != -1) progressView.setColor(DialogX.tipProgressColor);
            
            if (waitProgress >= 0 && waitProgress <= 1 && oldProgress != waitProgress) {
                progressView.progress(waitProgress);
                oldProgress = waitProgress;
            }
            
            showText(txtInfo, message);
            useTextInfo(txtInfo, messageTextInfo);
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                boxCustomView.removeView(onBindView.getCustomView());
                ViewGroup.LayoutParams lp = boxCustomView.getLayoutParams();
                if (lp == null) {
                    lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                boxCustomView.setVisibility(View.VISIBLE);
                boxProgress.setVisibility(View.GONE);
                boxCustomView.addView(onBindView.getCustomView(), lp);
            } else {
                boxCustomView.setVisibility(View.GONE);
                boxProgress.setVisibility(View.VISIBLE);
            }
        }
        
        public void doDismiss(final View v) {
            boxRoot.post(new Runnable() {
                @Override
                public void run() {
                    if (v != null) v.setEnabled(false);
                    
                    int exitAnimResId = R.anim.anim_dialogx_default_exit;
                    Animation enterAnim = AnimationUtils.loadAnimation(getContext(), exitAnimResId);
                    enterAnim.setInterpolator(new AccelerateInterpolator());
                    bkg.startAnimation(enterAnim);
                    
                    boxRoot.animate().setDuration(300).alpha(0f).setInterpolator(new AccelerateInterpolator()).setDuration(enterAnim.getDuration()).setListener(new AnimatorListenerEndCallBack() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dismiss(dialogView);
                        }
                    });
                }
            });
        }
        
        public void showTip(TYPE tip) {
            showType = tip.ordinal();
            if (progressView == null) return;
            switch (tip) {
                case NONE:
                    progressView.loading();
                    return;
                case SUCCESS:
                    progressView.success();
                    break;
                case WARNING:
                    progressView.warning();
                    break;
                case ERROR:
                    progressView.error();
                    break;
            }
            
            //此事件是在完成衔接动画绘制后执行的逻辑
            progressView.whenShowTick(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                    ((View) progressView).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (showType > -1) {
                                doDismiss(null);
                            }
                        }
                    }, tipShowDuration);
                }
            });
        }
    }
    
    @Override
    public boolean isLightTheme() {
        if (DialogX.tipTheme == null) {
            return super.isLightTheme();
        } else {
            return DialogX.tipTheme == DialogX.THEME.LIGHT;
        }
    }
    
    public void refreshUI() {
        if (dialogImpl == null) return;
        getRootFrameLayout().post(new Runnable() {
            @Override
            public void run() {
                dialogImpl.refreshView();
            }
        });
    }
    
    public void doDismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(null);
    }
    
    public static void dismiss() {
        me().doDismiss();
    }
    
    protected static WaitDialog me() {
        if (me == null || me.get() == null) me = new WeakReference<>(new WaitDialog());
        return me.get();
    }
    
    protected TYPE readyTipType;
    
    protected void showTip(CharSequence message, TYPE type) {
        showType = type.ordinal();
        this.message = message;
        readyTipType = type;
        show();
    }
    
    protected void showTip(int messageResId, TYPE type) {
        showType = type.ordinal();
        this.message = getString(messageResId);
        readyTipType = type;
        show();
    }
    
    public static CharSequence getMessage() {
        return me().message;
    }
    
    public static WaitDialog setMessage(CharSequence message) {
        me().preMessage(message);
        me().refreshUI();
        return me();
    }
    
    public static WaitDialog setMessage(int messageResId) {
        me().preMessage(messageResId);
        me().refreshUI();
        return me();
    }
    
    /**
     * 用于从 WaitDialog 到 TipDialog 的消息设置
     * 此方法不会立即执行，而是等到动画衔接完成后由事件设置
     *
     * @param message 消息
     * @return me
     */
    protected WaitDialog preMessage(CharSequence message) {
        me().message = message;
        return me();
    }
    
    protected WaitDialog preMessage(int messageResId) {
        me().message = getString(messageResId);
        return me();
    }
    
    public DialogLifecycleCallback<WaitDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<WaitDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public WaitDialog setDialogLifecycleCallback(DialogLifecycleCallback<WaitDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public WaitDialog setCustomView(OnBindView<WaitDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public WaitDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public WaitDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        refreshUI();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public WaitDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }
    
    public WaitDialog setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }
}
