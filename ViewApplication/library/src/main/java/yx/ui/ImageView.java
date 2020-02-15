package yx.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

public class ImageView extends android.widget.ImageView {

    private float ratio;
    private boolean mAdjustViewBounds = false;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DimensionRatioView);

        String dimensionRatio = a.getString(R.styleable.DimensionRatioView_dimensionRatio);
        setRatio(dimensionRatio);

        boolean adjustViewBounds = a.getBoolean(R.styleable.DimensionRatioView_android_adjustViewBounds, false);
        setAdjustViewBounds(adjustViewBounds);

        a.recycle();
    }

    public void setRatio(String ratio) {
        float dimensionRatioValue = 0.0f;
        int dimensionRatioSide = -1;
        if (!TextUtils.isEmpty(ratio)) {
            int len = ratio.length();
            int commaIndex = ratio.indexOf(',');
            if (commaIndex > 0 && commaIndex < len - 1) {
                String colonIndex = ratio.substring(0, commaIndex);
                if (colonIndex.equalsIgnoreCase("W")) {
                    dimensionRatioSide = 0;
                } else if (colonIndex.equalsIgnoreCase("H")) {
                    dimensionRatioSide = 1;
                }

                ++commaIndex;
            } else {
                commaIndex = 0;
            }

            int colonIndex = ratio.indexOf(':');
            String r;
            if (colonIndex >= 0 && colonIndex < len - 1) {
                r = ratio.substring(commaIndex, colonIndex);
                String denominator = ratio.substring(colonIndex + 1);
                if (r.length() > 0 && denominator.length() > 0) {
                    try {
                        float nominatorValue = Float.parseFloat(r);
                        float denominatorValue = Float.parseFloat(denominator);
                        if (nominatorValue > 0.0F && denominatorValue > 0.0F) {
                            if (dimensionRatioSide == 1) {
                                dimensionRatioValue = Math.abs(denominatorValue / nominatorValue);
                            } else {
                                dimensionRatioValue = Math.abs(nominatorValue / denominatorValue);
                            }
                        }
                    } catch (NumberFormatException e) {
                        ;
                    }
                }
            } else {
                r = ratio.substring(commaIndex);
                if (r.length() > 0) {
                    try {
                        dimensionRatioValue = Float.parseFloat(r);
                    } catch (NumberFormatException e) {
                        ;
                    }
                }
            }
        }
        this.ratio = dimensionRatioValue;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        //requestLayout();
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        this.mAdjustViewBounds = adjustViewBounds;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父容器传过来的宽高方向上的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 父容器传过来的宽高的值
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft() - getPaddingRight();

        Drawable drawable = getBackground();
        if (mAdjustViewBounds && drawable != null) {
            ratio = drawable.getIntrinsicWidth()*1.0f/drawable.getIntrinsicHeight();
        }

        if (widthMode == MeasureSpec.EXACTLY
                && heightMode != MeasureSpec.EXACTLY && ratio != 0f) {
            // 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
            // 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
            // 且图片的宽高比已经赋值完毕，不再是0.0f
            // 表示宽度确定，要测量高度
            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else if (widthMode != MeasureSpec.EXACTLY
                && heightMode == MeasureSpec.EXACTLY && ratio != 0f) {
            // 判断条件跟上面的相反，宽度方向和高度方向的条件互换
            // 表示高度确定，要测量宽度
            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
