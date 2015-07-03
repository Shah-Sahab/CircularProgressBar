/**
 * The MIT License (MIT)

 Copyright (c) [2015] [Syed Ahmed Hussain]

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package src.limitindicator.com.limitindicatorlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

/**
 * Limit Indicator is used to show any kind of limits such as Balance and Actual
 * Amount present whether its gas, wallet or anything else. In order to use this in the XML
 * Layout, please include the following: <br />
 * <br />
 *
 * xmlns:custom="http://schemas.android.com/apk/res-auto"
 *
 * <br /> <br />
 *
 * Following custom attributes are provided: <br />
 * <br />
 *
 * custom:borderColor <br />
 * custom:borderRadius <br />
 * custom:outerCircleRadius <br />
 * custom:text <br />
 * custom:textSize <br />
 * custom:innerCircleColor <br />
 *
 * @author Syed Ahmed Hussain
 * 4/19/15
 */
public class LimitIndicator extends ViewGroup {

    // ----------------------------------------------------------------------------------------------------------
    // Variables Declaration

    private int     mInnerCircleColor;
    private int     mBorderColor;
    private int     mTextColor;
    private float   mTextSize;
    private String  mTitleText = "";

    private float mHalfOfBorderWidth    = 0.0f;
    private float mOuterCircleRadius    = 2.0f;
    private float mBorderWidth          = 30.0f;

    private Paint mDialPaint, mTextPaint, mBorderPaint, mInnerCirclePaint;

    private float mCenterX = 100.0f;
    private float mCenterY = 100.0f;

    private int mTotalProgressInDegrees;
    private int mTotalProgress  = -1;

    // Start Angle should be 90 degrees to create a clockwise illusion.
    private int mStartAngle = 270;

    // This should be the one which provides us a percentage wise drawing
    private int mSweepAngle = 1;

    // Add up to counter
    private int mCounter = 0;

    private RectF mBorderBounds = null;

    public enum ANIMATION_TYPE {
        NORMAL, INCREASE_WIDTH
    }

    private ANIMATION_TYPE animation_type;

    // ----------------------------------------------------------------------------------------------------------
    // Constructors

    public LimitIndicator(Context pContext) {
        super(pContext);
        Log.d("LimitIndicator", "LimitIndicator(Context pContext) called");
        initialize();
    }

    public LimitIndicator(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
        Log.d("LimitIndicator", "LimitIndicator(Context pContext, AttributeSet pAttrs) called");
        TypedArray typedArray = pContext.obtainStyledAttributes(pAttrs, R.styleable.LimitIndicator, 0, 0);

        try {
            mOuterCircleRadius  = typedArray.getDimension(R.styleable.LimitIndicator_outerCircleRadius, mOuterCircleRadius);
            mInnerCircleColor   = typedArray.getColor(R.styleable.LimitIndicator_innerCircleColor, Color.GREEN);
            mTotalProgress      = typedArray.getInteger(R.styleable.LimitIndicator_numerator, mTotalProgress);
            mBorderColor        = typedArray.getColor(R.styleable.LimitIndicator_borderColor, Color.BLACK);
            mBorderWidth        = typedArray.getDimension(R.styleable.LimitIndicator_borderRadius, mBorderWidth);
            mTextColor          = typedArray.getColor(R.styleable.LimitIndicator_textColor, Color.WHITE);
            mTitleText          = typedArray.getString(R.styleable.LimitIndicator_text);
            mTextSize           = typedArray.getDimension(R.styleable.LimitIndicator_textSize, 25);
        } finally {
            typedArray.recycle();
        }
        initialize();
    }

    // ----------------------------------------------------------------------------------------------------------
    // Initialization

    /**
     * Initialize all elements
     */
    private void initialize() {

        // Set up the paint for the dial
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setStyle(Paint.Style.FILL);
        mDialPaint.setColor(Color.GRAY);

        // Set up the paint for the label text
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.SANS_SERIF);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        // Set up the paint for the border
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setDither(true);

        // Cap can be changed from round to any other type.
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);
        mInnerCirclePaint.setColor(mInnerCircleColor);

        // mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        // mBorderPaint.setStrokeCap(Paint.Cap.ROUND);

        mBorderBounds = new RectF(getLeft(), getTop(), getRight(), getBottom());
    }

    // ----------------------------------------------------------------------------------------------------------
    // Drawing on surface

    @Override
    protected void onDraw(Canvas pCanvas) {
        super.onDraw(pCanvas);
        Log.d("LimitIndicator", "OnDraw called");

        Log.d("Measured Spec Width", mCenterX + "");
        Log.d("Measured Spec Height", mCenterY + "");

        pCanvas.drawCircle(mCenterX, mCenterY, mOuterCircleRadius, mDialPaint);
        pCanvas.drawCircle(mCenterX, mCenterY, mOuterCircleRadius - mBorderWidth + 1, mInnerCirclePaint);
        pCanvas.drawText(mTitleText, mCenterX + 15, mCenterY + 15, mTextPaint);

        pCanvas.drawArc(mBorderBounds, mStartAngle, mSweepAngle, false, mBorderPaint);

        if (mSweepAngle < mTotalProgressInDegrees) {
            mSweepAngle += 3;
            if (animation_type.equals(ANIMATION_TYPE.INCREASE_WIDTH)) {
                mCounter++;
                if (mCounter <= mTotalProgress) {
                    mTitleText = mCounter + " %";
                    pCanvas.drawText(mTitleText, mCenterX + 15, mCenterY + 15, mTextPaint);
                }
                mBorderPaint.setStrokeWidth(mBorderWidth++);
            } else {
                if (mCounter <= mTotalProgress) {
                    mCounter++;
                    pCanvas.drawText(mTitleText, mCenterX + 15, mCenterY + 15, mTextPaint);
                }
                mBorderPaint.setStrokeWidth(mBorderWidth);

                mTitleText = mCounter + " %";
            }
            invalidate();
        }

    }

    @Override
    protected void onLayout(boolean pChanged, int pLeft, int pTop, int pRight, int pBottom) {
        Log.d("LimitIndicator", "OnLayout called");
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, 0, pRight, pBottom);
        }
    }

    @Override
    protected void onSizeChanged(int pW, int pH, int pOldw, int pOldh) {
        super.onSizeChanged(pW, pH, pOldw, pOldh);
        Log.d("LimitIndicator", "OnSizeChanged called");

        float xPad = (getPaddingLeft() + getPaddingRight());
        float yPad = (getPaddingTop() + getPaddingBottom());

        // To draw Circle in the middle
        mCenterX    = (float) ((pW - xPad) * 0.5);
        mCenterY    = (float) ((pH - yPad) * 0.5);


        // This (mBorderBounds.bottom needs to be fixed. Width &
        // Height should be equal in order
        // to create a perfect circle. Otherwise an
        // Oval will be created! :P

        // Bounds for creating an arc
        mHalfOfBorderWidth = (float) (mBorderWidth * 0.5);
        mBorderBounds.right     = mCenterX + mOuterCircleRadius - mHalfOfBorderWidth;
        mBorderBounds.left      = mCenterX - mOuterCircleRadius + mHalfOfBorderWidth;
        mBorderBounds.top       = mCenterY - mOuterCircleRadius + mHalfOfBorderWidth;
        mBorderBounds.bottom    = mCenterY + mOuterCircleRadius - mHalfOfBorderWidth;

    }

    // ----------------------------------------------------------------------------------------------------------

    /**
     * Start the progress/animation. Use this method to start the animated view.
     */
    public void startProgress() {
        if (mTotalProgress >= 0) {
            float progressInDegrees = mTotalProgress;
            mTotalProgressInDegrees = (int) (progressInDegrees/100 * 360);
            invalidate();
        }
    }


    /**
     * To restart the animation
     */
    public void restartProgress() {
//        initialize();
        mTotalProgress = 0;
        refreshDrawableState();
        invalidate();
    }

    /**
     * Utiliy method to change the cap on the limit indicator
     * @param cap
     */
    public void setIndicatorCap(Paint.Cap cap) {
        mBorderPaint.setStrokeCap(cap);
    }

    /**
     * Keeps the animation same while changing few elements.
     * Animation Type -> ANIMATION_TYPE -> NORMAL, INCREASE_WIDTH
     * @param animationType ANIMATION_TYPE
     */
    public void setAnimationType(ANIMATION_TYPE animationType) {
        this.animation_type = animationType;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent pEvent) {

        return super.dispatchPopulateAccessibilityEvent(pEvent);
    }


    // ----------------------------------------------------------------------------------------------------------
    // Getters && Setters!

    /**
     * @return the dialRadius
     */
    public float getDialRadius() {
        return mOuterCircleRadius;
    }

    /**
     * @param pDialRadius
     *            the dialRadius to set
     */
    public void setDialRadius(float pDialRadius) {
        mOuterCircleRadius = pDialRadius;
    }


    /**
     * @return the textSize
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * @param pTextSize the textSize to set
     */
    public void setTextSize(float pTextSize) {
        mTextSize = pTextSize;
    }

    /**
     * @return the textColor
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * @param pTextColor the textColor to set
     */
    public void setTextColor(int pTextColor) {
        mTextColor = pTextColor;
    }

    /**
     * @return the borderColor
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * @param pBorderColor the borderColor to set
     */
    public void setBorderColor(int pBorderColor) {
        mBorderColor = pBorderColor;
    }

    /**
     * @return the innerCircleColor
     */
    public int getInnerCircleColor() {
        return mInnerCircleColor;
    }

    /**
     * @param pInnerCircleColor the innerCircleColor to set
     */
    public void setInnerCircleColor(int pInnerCircleColor) {
        mInnerCircleColor = pInnerCircleColor;
    }

    /**
     * @return the titleText
     */
    public String getTitleText() {
        return mTitleText;
    }

    /**
     * @param pTitleText the titleText to set
     */
    public void setTitleText(String pTitleText) {
        mTitleText = pTitleText;
    }

    /**
     * @return the outerCircleRadius
     */
    public float getOuterCircleRadius() {
        return mOuterCircleRadius;
    }

    /**
     * @param pOuterCircleRadius the outerCircleRadius to set
     */
    public void setOuterCircleRadius(float pOuterCircleRadius) {
        mOuterCircleRadius = pOuterCircleRadius;
    }

    /**
     * @return the borderWidth
     */
    public float getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * @param pBorderWidth the borderWidth to set
     */
    public void setBorderWidth(float pBorderWidth) {
        mBorderWidth = pBorderWidth;
    }

    /**
     * @return the totalProgress
     */
    public int getTotalProgress() {
        return mTotalProgress;
    }

    /**
     * @param pTotalProgress the totalProgress to set
     */
    public void setTotalProgress(int pTotalProgress) {
        mTotalProgress = pTotalProgress;
    }

}