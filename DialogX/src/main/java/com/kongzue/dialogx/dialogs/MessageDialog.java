package com.kongzue.dialogx.dialogs;

import android.animation.Animator;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.R;
import com.kongzue.dialogx.impl.AnimatorListenerEndCallBack;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.BaseOnDialogClickCallback;
import com.kongzue.dialogx.interfaces.DialogConvertViewInterface;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.DialogXStyle;
import com.kongzue.dialogx.interfaces.OnBackPressedListener;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogx.util.views.BlurView;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.dialogx.util.views.MaxRelativeLayout;
import com.kongzue.dialogx.util.TextInfo;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/9/21 17:08
 */
public class MessageDialog extends BaseDialog {
    
    protected OnBindView<MessageDialog> onBindView;
    protected MessageDialog me = this;
    
    private DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback;
    
    protected MessageDialog() {
        super();
    }
    
    private View dialogView;
    
    protected CharSequence title;
    protected CharSequence message;
    protected CharSequence okText;
    protected CharSequence cancelText;
    protected CharSequence otherText;
    protected String inputText;
    protected String inputHintText;
    
    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo okTextInfo;
    protected TextInfo cancelTextInfo;
    protected TextInfo otherTextInfo;
    protected InputInfo inputInfo;
    
    protected BaseOnDialogClickCallback okButtonClickListener;
    protected BaseOnDialogClickCallback cancelButtonClickListener;
    protected BaseOnDialogClickCallback otherButtonClickListener;
    
    protected int buttonOrientation;
    
    public static MessageDialog build() {
        return new MessageDialog();
    }
    
    public MessageDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
    }
    
    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
    }
    
    public MessageDialog(int titleResId, int messageResId, int okTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
    }
    
    public MessageDialog(int titleResId, int messageResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
    }
    
    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText);
        messageDialog.show();
        return messageDialog;
    }
    
    public static MessageDialog show(int titleResId, int messageResId, int okTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId);
        messageDialog.show();
        return messageDialog;
    }
    
    public static MessageDialog show(CharSequence title, CharSequence message) {
        MessageDialog messageDialog = new MessageDialog(title, message);
        messageDialog.show();
        return messageDialog;
    }
    
    public static MessageDialog show(int titleResId, int messageResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId);
        messageDialog.show();
        return messageDialog;
    }
    
    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
    }
    
    public MessageDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
    }
    
    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText, cancelText);
        messageDialog.show();
        return messageDialog;
    }
    
    public static MessageDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId, cancelTextResId);
        messageDialog.show();
        return messageDialog;
    }
    
    public MessageDialog(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        this.title = title;
        this.message = message;
        this.okText = okText;
        this.cancelText = cancelText;
        this.otherText = otherText;
    }
    
    public MessageDialog(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        this.title = getString(titleResId);
        this.message = getString(messageResId);
        this.okText = getString(okTextResId);
        this.cancelText = getString(cancelTextResId);
        this.otherText = getString(otherTextResId);
    }
    
    public static MessageDialog show(CharSequence title, CharSequence message, CharSequence okText, CharSequence cancelText, CharSequence otherText) {
        MessageDialog messageDialog = new MessageDialog(title, message, okText, cancelText, otherText);
        messageDialog.show();
        return messageDialog;
    }
    
    public static MessageDialog show(int titleResId, int messageResId, int okTextResId, int cancelTextResId, int otherTextResId) {
        MessageDialog messageDialog = new MessageDialog(titleResId, messageResId, okTextResId, cancelTextResId, otherTextResId);
        messageDialog.show();
        return messageDialog;
    }
    
    protected DialogImpl dialogImpl;
    
    public void show() {
        super.beforeShow();
        int layoutId = style.layout(isLightTheme());
        layoutId = layoutId == 0 ? (isLightTheme() ? R.layout.layout_dialogx_material : R.layout.layout_dialogx_material_dark) : layoutId;
        
        dialogView = createView(layoutId);
        dialogImpl = new DialogImpl(dialogView);
        show(dialogView);
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
    
    public class DialogImpl implements DialogConvertViewInterface {
        BlurView blurView;
        
        public DialogXBaseRelativeLayout boxRoot;
        public MaxRelativeLayout bkg;
        public TextView txtDialogTitle;
        public TextView txtDialogTip;
        public RelativeLayout boxCustom;
        public EditText txtInput;
        public LinearLayout boxButton;
        public TextView btnSelectOther;
        public View spaceOtherButton;
        public TextView btnSelectNegative;
        public TextView btnSelectPositive;
        
        public DialogImpl(View convertView) {
            boxRoot = convertView.findViewById(R.id.box_root);
            bkg = convertView.findViewById(R.id.bkg);
            txtDialogTitle = convertView.findViewById(R.id.txt_dialog_title);
            txtDialogTip = convertView.findViewById(R.id.txt_dialog_tip);
            boxCustom = convertView.findViewById(R.id.box_custom);
            txtInput = convertView.findViewById(R.id.txt_input);
            boxButton = convertView.findViewById(R.id.box_button);
            btnSelectOther = convertView.findViewById(R.id.btn_selectOther);
            spaceOtherButton = convertView.findViewById(R.id.space_other_button);
            btnSelectNegative = convertView.findViewById(R.id.btn_selectNegative);
            btnSelectPositive = convertView.findViewById(R.id.btn_selectPositive);
            init();
            refreshView();
        }
        
        public void init() {
            if (titleTextInfo == null) titleTextInfo = DialogX.titleTextInfo;
            if (messageTextInfo == null) messageTextInfo = DialogX.messageTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.okButtonTextInfo;
            if (okTextInfo == null) okTextInfo = DialogX.buttonTextInfo;
            if (cancelTextInfo == null) cancelTextInfo = DialogX.buttonTextInfo;
            if (otherTextInfo == null) otherTextInfo = DialogX.buttonTextInfo;
            if (inputInfo == null) inputInfo = DialogX.inputInfo;
            if (backgroundColor == -1) backgroundColor = DialogX.backgroundColor;
            
            txtDialogTitle.getPaint().setFakeBoldText(true);
            btnSelectNegative.getPaint().setFakeBoldText(true);
            btnSelectPositive.getPaint().setFakeBoldText(true);
            btnSelectOther.getPaint().setFakeBoldText(true);
            
            boxRoot.setOnLifecycleCallBack(new DialogXBaseRelativeLayout.OnLifecycleCallBack() {
                @Override
                public void onShow() {
                    isShow = true;
                    boxRoot.setAlpha(0f);
                    int enterAnimResId = style.enterAnimResId() == 0 ? R.anim.anim_dialogx_default_enter : style.enterAnimResId();
                    Animation enterAnim = AnimationUtils.loadAnimation(getContext(), enterAnimResId);
                    enterAnim.setInterpolator(new DecelerateInterpolator());
                    bkg.startAnimation(enterAnim);
                    
                    boxRoot.animate().setDuration(enterAnim.getDuration()).alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null);
                    
                    getDialogLifecycleCallback().onShow(me);
                    
                    if (style.messageDialogBlurSettings() != null && style.messageDialogBlurSettings().blurBackground()) {
                        bkg.post(new Runnable() {
                            @Override
                            public void run() {
                                int blurFrontColor = getResources().getColor(style.messageDialogBlurSettings().blurForwardColorRes(isLightTheme()));
                                blurView = new BlurView(bkg.getContext(), null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bkg.getWidth(), bkg.getHeight());
                                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                                blurView.setOverlayColor(backgroundColor == -1 ? blurFrontColor : backgroundColor);
                                blurView.setTag("blurView");
                                blurView.setRadiusPx(style.messageDialogBlurSettings().blurBackgroundRoundRadiusPx());
                                bkg.addView(blurView, 0, params);
                            }
                        });
                    }
                    
                    if (autoShowInputKeyboard) {
                        txtInput.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                txtInput.requestFocus();
                                txtInput.setFocusableInTouchMode(true);
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(txtInput, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                                txtInput.setSelection(txtInput.getText().length());
                            }
                        }, 300);
                    }
                    
                    if (onBindView != null) onBindView.onBind(me, onBindView.getCustomView());
                }
                
                @Override
                public void onDismiss() {
                    isShow = false;
                    getDialogLifecycleCallback().onDismiss(me);
                }
            });
            
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
            btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtInput != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtInput.getWindowToken(), 0);
                    }
                    if (okButtonClickListener != null) {
                        if (okButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) okButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else if (okButtonClickListener instanceof OnDialogButtonClickListener) {
                            if (!((OnDialogButtonClickListener) okButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
            btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtInput != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtInput.getWindowToken(), 0);
                    }
                    if (cancelButtonClickListener != null) {
                        if (cancelButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) cancelButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else {
                            if (!((OnDialogButtonClickListener) cancelButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
            btnSelectOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtInput != null) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtInput.getWindowToken(), 0);
                    }
                    if (otherButtonClickListener != null) {
                        if (otherButtonClickListener instanceof OnInputDialogButtonClickListener) {
                            String s = txtInput == null ? "" : txtInput.getText().toString();
                            if (!((OnInputDialogButtonClickListener) otherButtonClickListener).onClick(me, v, s)) {
                                doDismiss(v);
                            }
                        } else {
                            if (!((OnDialogButtonClickListener) otherButtonClickListener).onClick(me, v)) {
                                doDismiss(v);
                            }
                        }
                    } else {
                        doDismiss(v);
                    }
                }
            });
        }
        
        public void refreshView() {
            if (backgroundColor != -1) {
                tintColor(bkg, backgroundColor);
                if (style instanceof MaterialStyle) {
                    tintColor(btnSelectOther, backgroundColor);
                    tintColor(btnSelectNegative, backgroundColor);
                    tintColor(btnSelectPositive, backgroundColor);
                }
            }
            
            bkg.setMaxWidth(DialogX.dialogMaxWidth);
            if (me instanceof InputDialog) {
                txtInput.setVisibility(View.VISIBLE);
            } else {
                txtInput.setVisibility(View.GONE);
            }
            boxRoot.setClickable(true);
            
            showText(txtDialogTitle, title);
            showText(txtDialogTip, message);
            showText(btnSelectPositive, okText);
            showText(btnSelectNegative, cancelText);
            showText(btnSelectOther, otherText);
            
            txtInput.setText(inputText);
            txtInput.setHint(inputHintText);
            if (spaceOtherButton != null) {
                if (otherText == null) {
                    spaceOtherButton.setVisibility(View.GONE);
                } else {
                    spaceOtherButton.setVisibility(View.VISIBLE);
                }
            }
            
            useTextInfo(txtDialogTitle, titleTextInfo);
            useTextInfo(txtDialogTip, messageTextInfo);
            useTextInfo(btnSelectPositive, okTextInfo);
            useTextInfo(btnSelectNegative, cancelTextInfo);
            useTextInfo(btnSelectOther, otherTextInfo);
            if (inputInfo != null) {
                if (inputInfo.getMAX_LENGTH() != -1)
                    txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                int inputType = InputType.TYPE_CLASS_TEXT | inputInfo.getInputType();
                if (inputInfo.isMultipleLines()) {
                    inputType = inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                }
                txtInput.setInputType(inputType);
                if (inputInfo.getTextInfo() != null)
                    useTextInfo(txtInput, inputInfo.getTextInfo());
                
                if (inputInfo.isSelectAllText()) {
                    txtInput.post(new Runnable() {
                        @Override
                        public void run() {
                            txtInput.selectAll();
                        }
                    });
                }
            }
            
            int visibleButtonCount = 0;
            if (!isNull(okText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }
            if (!isNull(cancelText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }
            if (!isNull(otherText)) {
                visibleButtonCount = visibleButtonCount + 1;
            }
            
            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LinearLayout.VERTICAL) {
                //纵向
                if (style.verticalButtonOrder() != null && style.verticalButtonOrder().length != 0) {
                    boxButton.removeAllViews();
                    for (int buttonType : style.verticalButtonOrder()) {
                        switch (buttonType) {
                            case DialogXStyle.BUTTON_OK:
                                boxButton.addView(btnSelectPositive);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectPositive.setBackgroundResource(
                                            style.overrideVerticalButtonRes().overrideVerticalOkButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.BUTTON_OTHER:
                                boxButton.addView(btnSelectOther);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectOther.setBackgroundResource(
                                            style.overrideVerticalButtonRes().overrideVerticalOtherButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.BUTTON_CANCEL:
                                boxButton.addView(btnSelectNegative);
                                if (style.overrideVerticalButtonRes() != null) {
                                    btnSelectNegative.setBackgroundResource(
                                            style.overrideVerticalButtonRes().overrideVerticalCancelButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.SPACE:
                                Space space = new Space(getContext());
                                LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                spaceLp.weight = 1;
                                boxButton.addView(space, spaceLp);
                                break;
                            case DialogXStyle.SPLIT:
                                View splitView = new View(getContext());
                                splitView.setBackgroundColor(getResources().getColor(style.splitColorRes(isLightTheme())));
                                LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, style.splitWidthPx());
                                boxButton.addView(splitView, viewLp);
                                break;
                        }
                    }
                }
            } else {
                //横向
                if (style.horizontalButtonOrder() != null && style.horizontalButtonOrder().length != 0) {
                    boxButton.removeAllViews();
                    for (int buttonType : style.horizontalButtonOrder()) {
                        switch (buttonType) {
                            case DialogXStyle.BUTTON_OK:
                                boxButton.addView(btnSelectPositive);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectPositive.setBackgroundResource(
                                            style.overrideHorizontalButtonRes().overrideHorizontalOkButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.BUTTON_OTHER:
                                boxButton.addView(btnSelectOther);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectOther.setBackgroundResource(
                                            style.overrideHorizontalButtonRes().overrideHorizontalOtherButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.BUTTON_CANCEL:
                                boxButton.addView(btnSelectNegative);
                                if (style.overrideHorizontalButtonRes() != null) {
                                    btnSelectNegative.setBackgroundResource(
                                            style.overrideHorizontalButtonRes().overrideHorizontalCancelButtonBackgroundRes(visibleButtonCount, isLightTheme())
                                    );
                                }
                                break;
                            case DialogXStyle.SPACE:
                                if (boxButton.getChildCount() >= 1) {
                                    if (boxButton.getChildAt(boxButton.getChildCount() - 1).getVisibility() == View.GONE) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                                Space space = new Space(getContext());
                                LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                spaceLp.weight = 1;
                                boxButton.addView(space, spaceLp);
                                break;
                            case DialogXStyle.SPLIT:
                                if (boxButton.getChildCount() >= 1) {
                                    if (boxButton.getChildAt(boxButton.getChildCount() - 1).getVisibility() == View.GONE) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                                View splitView = new View(getContext());
                                splitView.setBackgroundColor(getResources().getColor(style.splitColorRes(isLightTheme())));
                                LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(style.splitWidthPx(), ViewGroup.LayoutParams.MATCH_PARENT);
                                boxButton.addView(splitView, viewLp);
                                break;
                        }
                    }
                }
            }
            
            //Events
            if (cancelable) {
                boxRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doDismiss(v);
                    }
                });
            } else {
                boxRoot.setOnClickListener(null);
            }
            
            if (onBindView != null && onBindView.getCustomView() != null) {
                boxCustom.removeView(onBindView.getCustomView());
                ViewGroup.LayoutParams lp = boxCustom.getLayoutParams();
                if (lp == null) {
                    lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                boxCustom.setVisibility(View.VISIBLE);
                boxCustom.addView(onBindView.getCustomView(), lp);
            } else {
                boxCustom.setVisibility(View.GONE);
            }
        }
        
        public void doDismiss(View v) {
            if (v != null) v.setEnabled(false);
            
            int exitAnimResId = style.exitAnimResId() == 0 ? R.anim.anim_dialogx_default_exit : style.exitAnimResId();
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
    }
    
    public void dismiss() {
        if (dialogImpl == null) return;
        dialogImpl.doDismiss(dialogImpl.bkg);
    }
    
    public DialogLifecycleCallback<MessageDialog> getDialogLifecycleCallback() {
        return dialogLifecycleCallback == null ? new DialogLifecycleCallback<MessageDialog>() {
        } : dialogLifecycleCallback;
    }
    
    public MessageDialog setDialogLifecycleCallback(DialogLifecycleCallback<MessageDialog> dialogLifecycleCallback) {
        this.dialogLifecycleCallback = dialogLifecycleCallback;
        return this;
    }
    
    public MessageDialog setStyle(DialogXStyle style) {
        this.style = style;
        return this;
    }
    
    public MessageDialog setTheme(DialogX.THEME theme) {
        this.theme = theme;
        return this;
    }
    
    public CharSequence getOkButton() {
        return okText;
    }
    
    public MessageDialog setOkButton(CharSequence okText) {
        this.okText = okText;
        refreshUI();
        return this;
    }
    
    public MessageDialog setOkButton(int okTextRedId) {
        this.okText = getString(okTextRedId);
        refreshUI();
        return this;
    }
    
    public MessageDialog setOkButton(OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }
    
    public MessageDialog setOkButton(CharSequence okText, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = okText;
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }
    
    public MessageDialog setOkButton(int okTextRedId, OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okText = getString(okTextRedId);
        this.okButtonClickListener = okButtonClickListener;
        refreshUI();
        return this;
    }
    
    public CharSequence getCancelButton() {
        return cancelText;
    }
    
    public MessageDialog setCancelButton(CharSequence cancelText) {
        this.cancelText = cancelText;
        refreshUI();
        return this;
    }
    
    public MessageDialog setCancelButton(int cancelTextResId) {
        this.cancelText = getString(cancelTextResId);
        refreshUI();
        return this;
    }
    
    public MessageDialog setCancelButton(OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }
    
    public MessageDialog setCancelButton(CharSequence cancelText, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = cancelText;
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public MessageDialog setCancelButton(int cancelTextResId, OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelText = getString(cancelTextResId);
        this.cancelButtonClickListener = cancelButtonClickListener;
        refreshUI();
        return this;
    }
    
    public CharSequence getOtherButton() {
        return otherText;
    }
    
    public MessageDialog setOtherButton(CharSequence otherText) {
        this.otherText = otherText;
        refreshUI();
        return this;
    }
    
    public MessageDialog setOtherButton(int otherTextResId) {
        this.otherText = getString(otherTextResId);
        refreshUI();
        return this;
    }
    
    public MessageDialog setOtherButton(OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }
    
    public MessageDialog setOtherButton(CharSequence otherText, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = otherText;
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public MessageDialog setOtherButton(int otherTextResId, OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherText = getString(otherTextResId);
        this.otherButtonClickListener = otherButtonClickListener;
        refreshUI();
        return this;
    }
    
    public OnDialogButtonClickListener<MessageDialog> getOkButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) okButtonClickListener;
    }
    
    public MessageDialog setOkButtonClickListener(OnDialogButtonClickListener<MessageDialog> okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }
    
    public OnDialogButtonClickListener<MessageDialog> getCancelButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) cancelButtonClickListener;
    }
    
    public MessageDialog setCancelButtonClickListener(OnDialogButtonClickListener<MessageDialog> cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return this;
    }
    
    public OnDialogButtonClickListener<MessageDialog> getOtherButtonClickListener() {
        return (OnDialogButtonClickListener<MessageDialog>) otherButtonClickListener;
    }
    
    public MessageDialog setOtherButtonClickListener(OnDialogButtonClickListener<MessageDialog> otherButtonClickListener) {
        this.otherButtonClickListener = otherButtonClickListener;
        return this;
    }
    
    public CharSequence getTitle() {
        return title;
    }
    
    public MessageDialog setTitle(CharSequence title) {
        this.title = title;
        refreshUI();
        return this;
    }
    
    public MessageDialog setTitle(int titleResId) {
        this.title = getString(titleResId);
        refreshUI();
        return this;
    }
    
    public CharSequence getMessage() {
        return message;
    }
    
    public MessageDialog setMessage(CharSequence message) {
        this.message = message;
        refreshUI();
        return this;
    }
    
    public MessageDialog setMessage(int messageResId) {
        this.message = getString(messageResId);
        refreshUI();
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public MessageDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public MessageDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getOkTextInfo() {
        return okTextInfo;
    }
    
    public MessageDialog setOkTextInfo(TextInfo okTextInfo) {
        this.okTextInfo = okTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getCancelTextInfo() {
        return cancelTextInfo;
    }
    
    public MessageDialog setCancelTextInfo(TextInfo cancelTextInfo) {
        this.cancelTextInfo = cancelTextInfo;
        refreshUI();
        return this;
    }
    
    public TextInfo getOtherTextInfo() {
        return otherTextInfo;
    }
    
    public MessageDialog setOtherTextInfo(TextInfo otherTextInfo) {
        this.otherTextInfo = otherTextInfo;
        refreshUI();
        return this;
    }
    
    public int getButtonOrientation() {
        return buttonOrientation;
    }
    
    public MessageDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshUI();
        return this;
    }
    
    public boolean isCancelable() {
        return cancelable;
    }
    
    public MessageDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        refreshUI();
        return this;
    }
    
    public OnBackPressedListener getOnBackPressedListener() {
        return onBackPressedListener;
    }
    
    public MessageDialog setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
        return this;
    }
    
    public DialogImpl getDialogImpl() {
        return dialogImpl;
    }
    
    public MessageDialog setCustomView(OnBindView<MessageDialog> onBindView) {
        this.onBindView = onBindView;
        refreshUI();
        return this;
    }
    
    public View getCustomView() {
        if (onBindView == null) return null;
        return onBindView.getCustomView();
    }
    
    public MessageDialog removeCustomView() {
        this.onBindView.clean();
        refreshUI();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public MessageDialog setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshUI();
        return this;
    }
    
    public String getInputText() {
        if (dialogImpl.txtInput != null) {
            return dialogImpl.txtInput.getText().toString();
        } else {
            return "";
        }
    }
    
    public MessageDialog setBackgroundColorRes(@ColorRes int backgroundColorResId) {
        this.backgroundColor = getColor(backgroundColorResId);
        refreshUI();
        return this;
    }
}
