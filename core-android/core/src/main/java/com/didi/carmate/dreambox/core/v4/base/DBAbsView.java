package com.didi.carmate.dreambox.core.v4.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;

import com.didi.carmate.dreambox.core.v4.data.DBData;
import com.didi.carmate.dreambox.core.v4.render.view.DBFrameLayoutView;
import com.didi.carmate.dreambox.core.v4.render.view.DBLinearLayoutView;
import com.didi.carmate.dreambox.core.v4.render.view.DBYogaLayoutView;
import com.didi.carmate.dreambox.core.v4.utils.DBLogger;
import com.didi.carmate.dreambox.core.v4.utils.DBScreenUtils;
import com.didi.carmate.dreambox.core.v4.utils.DBUtils;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaPositionType;

import java.util.HashMap;
import java.util.Map;

import static com.didi.carmate.dreambox.core.v4.base.DBConstants.ALIGN_SELF_BASELINE;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.ALIGN_SELF_CENTER;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.ALIGN_SELF_END;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.ALIGN_SELF_START;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.ALIGN_SELF_STRETCH;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.POSITION_TYPE_ABSOLUTE;
import static com.didi.carmate.dreambox.core.v4.base.DBConstants.POSITION_TYPE_RELATIVE;

/**
 * author: chenjing
 * date: 2020/11/10
 */
public abstract class DBAbsView<V extends View> extends DBBindView {
    private final Map<String, Integer> mapGravity = new HashMap<>();

    protected SparseArray<View> mViews = new SparseArray<>(); // 待优化，暂时用下标记住和数据源的映射关系
    protected View mNativeView;
    // 通用属性
    protected int id = DBConstants.DEFAULT_ID_VIEW;

    protected int width;
    protected float widthPercent;
    protected int minWidth;
    protected float minWidthPercent;
    protected int height;
    protected float heightPercent;
    protected int minHeight;
    protected float minHeightPercent;

    protected String background; // 背景图片
    protected int radius; // 背景圆角半径
    protected int radiusLT; // 左上角圆角半径
    protected int radiusRT; // 右上角圆角半径
    protected int radiusRB; // 右下角圆角半径
    protected int radiusLB; // 左下角圆角半径
    protected String backgroundColor; // 背景颜色
    protected int margin;
    protected int marginTop;
    protected int marginBottom;
    protected int marginLeft;
    protected int marginRight;
    protected int padding;
    protected int paddingTop;
    protected int paddingBottom;
    protected int paddingLeft;
    protected int paddingRight;

    protected int layoutGravity;
    protected int gravity;

    // YogaLayout属性
    protected float flexGrow;
    protected float flexShrink;
    protected float flexBasis;
    protected float flexBasisPercent;
    protected String alignSelf;

    protected String positionType;
    protected float positionLeft;
    protected float positionLeftPercent;
    protected float positionTop;
    protected float positionTopPercent;
    protected float positionRight;
    protected float positionRightPercent;
    protected float positionBottom;
    protected float positionBottomPercent;
    protected float aspectRatio;

    protected DBAbsView(DBContext dbContext) {
        super(dbContext);

        mapGravity.put(DBConstants.STYLE_GRAVITY_LEFT, Gravity.START);
        mapGravity.put(DBConstants.STYLE_GRAVITY_RIGHT, Gravity.END);
        mapGravity.put(DBConstants.STYLE_GRAVITY_TOP, Gravity.TOP);
        mapGravity.put(DBConstants.STYLE_GRAVITY_BOTTOM, Gravity.BOTTOM);
        mapGravity.put(DBConstants.STYLE_GRAVITY_CENTER, Gravity.CENTER);
        mapGravity.put(DBConstants.STYLE_GRAVITY_CENTER_VERTICAL, Gravity.CENTER_VERTICAL);
        mapGravity.put(DBConstants.STYLE_GRAVITY_CENTER_HORIZONTAL, Gravity.CENTER_HORIZONTAL);
    }

    protected abstract View onCreateView();

    protected V onGetParentNativeView() {
        return null;
    }

    public int getId() {
        return id;
    }

    public View getNativeView() {
        return mNativeView;
    }

    @CallSuper
    protected void onParseLayoutAttr(Map<String, String> attrs) {
        // 边距
        margin = DBScreenUtils.processSize(mDBContext, attrs.get("margin"), DBConstants.DEFAULT_SIZE_EDGE);
        marginLeft = DBScreenUtils.processSize(mDBContext, attrs.get("marginLeft"), DBConstants.DEFAULT_SIZE_EDGE);
        marginTop = DBScreenUtils.processSize(mDBContext, attrs.get("marginTop"), DBConstants.DEFAULT_SIZE_EDGE);
        marginRight = DBScreenUtils.processSize(mDBContext, attrs.get("marginRight"), DBConstants.DEFAULT_SIZE_EDGE);
        marginBottom = DBScreenUtils.processSize(mDBContext, attrs.get("marginBottom"), DBConstants.DEFAULT_SIZE_EDGE);
        // padding
        padding = DBScreenUtils.processSize(mDBContext, attrs.get("padding"), DBConstants.DEFAULT_SIZE_EDGE);
        paddingLeft = DBScreenUtils.processSize(mDBContext, attrs.get("paddingLeft"), DBConstants.DEFAULT_SIZE_EDGE);
        paddingTop = DBScreenUtils.processSize(mDBContext, attrs.get("paddingTop"), DBConstants.DEFAULT_SIZE_EDGE);
        paddingRight = DBScreenUtils.processSize(mDBContext, attrs.get("paddingRight"), DBConstants.DEFAULT_SIZE_EDGE);
        paddingBottom = DBScreenUtils.processSize(mDBContext, attrs.get("paddingBottom"), DBConstants.DEFAULT_SIZE_EDGE);

        // 宽高
        width = DBConstants.DEFAULT_SIZE_WIDTH;
        String w = attrs.get("width");
        if (null != w) {
            if (w.endsWith("%")) {
                w = w.substring(0, w.length() - 1);
                if (DBUtils.isNumeric(w)) {
                    widthPercent = Float.parseFloat(w);
                }
            } else {
                width = DBScreenUtils.processSize(mDBContext, w, DBConstants.DEFAULT_SIZE_WIDTH);
            }
        }
        minWidth = DBConstants.DEFAULT_SIZE_WIDTH;
        String minW = attrs.get("minWidth");
        if (null != minW) {
            if (minW.endsWith("%")) {
                minW = minW.substring(0, minW.length() - 1);
                if (DBUtils.isNumeric(minW)) {
                    minWidthPercent = Float.parseFloat(minW);
                }
            } else {
                minWidth = DBScreenUtils.processSize(mDBContext, minW, DBConstants.DEFAULT_SIZE_WIDTH);
            }
        }
        height = DBConstants.DEFAULT_SIZE_HEIGHT;
        String h = attrs.get("height");
        if (null != h) {
            if (h.endsWith("%")) {
                h = h.substring(0, h.length() - 1);
                if (DBUtils.isNumeric(h)) {
                    heightPercent = Float.parseFloat(h);
                }
            } else {
                height = DBScreenUtils.processSize(mDBContext, h, DBConstants.DEFAULT_SIZE_HEIGHT);
            }
        }
        minHeight = DBConstants.DEFAULT_SIZE_HEIGHT;
        String minH = attrs.get("minHeight");
        if (null != minH) {
            if (minH.endsWith("%")) {
                minH = minH.substring(0, minH.length() - 1);
                if (DBUtils.isNumeric(minH)) {
                    minHeightPercent = Float.parseFloat(minH);
                }
            } else {
                minHeight = DBScreenUtils.processSize(mDBContext, minH, DBConstants.DEFAULT_SIZE_HEIGHT);
            }
        }

        // 圆角
        radius = DBScreenUtils.processSize(mDBContext, attrs.get("radius"), 0);
        radiusLT = DBScreenUtils.processSize(mDBContext, attrs.get("radiusLT"), 0);
        radiusRT = DBScreenUtils.processSize(mDBContext, attrs.get("radiusRT"), 0);
        radiusRB = DBScreenUtils.processSize(mDBContext, attrs.get("radiusRB"), 0);
        radiusLB = DBScreenUtils.processSize(mDBContext, attrs.get("radiusLB"), 0);

        // YogaLayout属性
        String fg = attrs.get("flex-grow");
        if (DBUtils.isNumeric(fg)) {
            flexGrow = Float.parseFloat(fg);
        }
        String fs = attrs.get("flex-shrink");
        if (DBUtils.isNumeric(fs)) {
            flexShrink = Float.parseFloat(fs);
        }
        String fb = attrs.get("flex-basis");
        if (null != fb) {
            if (fb.endsWith("%")) {
                fb = fb.substring(0, fb.length() - 1);
                if (DBUtils.isNumeric(fb)) {
                    flexBasisPercent = Float.parseFloat(fb);
                }
            } else {
                flexBasis = DBScreenUtils.processSize(mDBContext, fb, 0);
            }
        }
        alignSelf = attrs.get("align-self");
        positionType = attrs.get("positionType");
        String posLeft = attrs.get("positionLeft");
        if (null != posLeft) {
            if (posLeft.endsWith("%")) {
                posLeft = posLeft.substring(0, posLeft.length() - 1);
                if (DBUtils.isNumeric(posLeft)) {
                    positionLeftPercent = Float.parseFloat(posLeft);
                }
            } else {
                positionLeft = DBScreenUtils.processSize(mDBContext, posLeft, 0);
            }
        }
        String posTop = attrs.get("positionTop");
        if (null != posTop) {
            if (posTop.endsWith("%")) {
                posTop = posTop.substring(0, posTop.length() - 1);
                if (DBUtils.isNumeric(posTop)) {
                    positionTopPercent = Float.parseFloat(posTop);
                }
            } else {
                positionTop = DBScreenUtils.processSize(mDBContext, posTop, 0);
            }
        }
        String posRight = attrs.get("positionRight");
        if (null != posRight) {
            if (posRight.endsWith("%")) {
                posRight = posRight.substring(0, posRight.length() - 1);
                if (DBUtils.isNumeric(posRight)) {
                    positionRightPercent = Float.parseFloat(posRight);
                }
            } else {
                positionRight = DBScreenUtils.processSize(mDBContext, posRight, 0);
            }
        }
        String posBottom = attrs.get("positionBottom");
        if (null != posBottom) {
            if (posBottom.endsWith("%")) {
                posBottom = posBottom.substring(0, posBottom.length() - 1);
                if (DBUtils.isNumeric(posBottom)) {
                    positionBottomPercent = Float.parseFloat(posBottom);
                }
            } else {
                positionBottom = DBScreenUtils.processSize(mDBContext, posBottom, 0);
            }
        }
        String ratio = attrs.get("aspectRatio");
        if (null != ratio && DBUtils.isNumeric(ratio)) {
            aspectRatio = Float.parseFloat(ratio);
        }
    }

    @CallSuper
    protected void onAttributesBind(final Map<String, String> attrs) {
        // visibleOn
        final String rawVisibleOn = attrs.get("visibleOn");
        String visibleOn;
        if (null == rawVisibleOn) {
            visibleOn = "1";
        } else {
            visibleOn = getString(rawVisibleOn);
        }
        if ("-1".equals(visibleOn)) {
            mNativeView.setVisibility(View.GONE);
        } else if ("0".equals(visibleOn)) {
            mNativeView.setVisibility(View.INVISIBLE);
        } else {
            mNativeView.setVisibility(View.VISIBLE);
        }
        if (null != rawVisibleOn) {
            mDBContext.observeDataPool(new DBData.IDataObserver() {
                @Override
                public void onDataChanged(String key) {
                    DBLogger.d(mDBContext, "key: " + key);
                    if (null != mNativeView) {
                        String visibleOn = getString(rawVisibleOn);
                        if ("-1".equals(visibleOn)) {
                            mNativeView.setVisibility(View.GONE);
                        } else if ("0".equals(visibleOn)) {
                            mNativeView.setVisibility(View.INVISIBLE);
                        } else {
                            mNativeView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public String getKey() {
                    return attrs.get("visibleOn");
                }
            });
        }

        // background
        background = getString(attrs.get("background"));
        if (!DBUtils.isEmpty(background)) {
            Context context = mDBContext.getContext();
            int resId = context.getResources().getIdentifier(background, "drawable", context.getPackageName());
            mNativeView.setBackgroundResource(resId);
        }
        // backgroundColor
        backgroundColor = getString(attrs.get("backgroundColor"));
        if (DBUtils.isColor(backgroundColor)) {
            // 外矩形 左上、右上、右下、左下的圆角半径
            float[] outerRadiusAll = {radius, radius, radius, radius, radius, radius, radius, radius};
            float[] outerRadius = {radiusLT, radiusLT, radiusRT, radiusRT, radiusRB, radiusRB, radiusLB, radiusLB};
            // 背景drawable
            RoundRectShape bgShape = new RoundRectShape(radius != 0 ? outerRadiusAll : outerRadius, null, null);
            ShapeDrawable bgDrawable = new ShapeDrawable(bgShape);
            bgDrawable.getPaint().setColor(Color.parseColor(backgroundColor));
            bgDrawable.getPaint().setAntiAlias(true);
            bgDrawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);//描边
            mNativeView.setBackground(bgDrawable);
        }
        // layoutGravity
        String rawLayoutGravity = getString(attrs.get("layoutGravity"));
        if (!DBUtils.isEmpty(rawLayoutGravity)) {
            layoutGravity = convertGravity(rawLayoutGravity);
        }
        // layoutGravity
        String rawGravity = getString(attrs.get("gravity"));
        if (!DBUtils.isEmpty(rawGravity)) {
            gravity = convertGravity(rawGravity);
        }
    }

    @CallSuper
    protected void onViewAdded(ViewGroup parentView) {
        if (parentView instanceof DBYogaLayoutView) {
            // YogaLayout
            bindAttributesInYogaLayout(parentView);
        } else if (parentView instanceof DBLinearLayoutView) {
            // LinearLayout
            bindAttributesInLinearLayout(parentView);
        } else if (parentView instanceof DBFrameLayoutView) {
            // FrameLayout
            bindAttributesInFrameLayout(parentView);
        }
    }

    private int convertGravity(String gravity) {
        if (null == gravity) {
            return 0;
        }
        String[] gravityArr = gravity.split("\\|");
        int iGravity = 0;
        for (String strGravity : gravityArr) {
            Integer tmp = mapGravity.get(strGravity);
            if (null != tmp) {
                iGravity |= tmp;
            }
        }
        return iGravity;
    }

    private void bindAttributesInLinearLayout(ViewGroup parentView) {

    }

    private void bindAttributesInFrameLayout(ViewGroup parentView) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mNativeView.getLayoutParams();
        layoutParams.gravity = layoutGravity;
    }

    private void bindAttributesInYogaLayout(ViewGroup parentView) {
        YogaNode node = ((DBYogaLayoutView) parentView).getYogaNodeForView(mNativeView);
        if (margin > 0) {
            node.setMargin(YogaEdge.LEFT, margin);
            node.setMargin(YogaEdge.TOP, margin);
            node.setMargin(YogaEdge.RIGHT, margin);
            node.setMargin(YogaEdge.BOTTOM, margin);
        } else {
            if (marginLeft != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setMargin(YogaEdge.LEFT, marginLeft);
            }
            if (marginTop != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setMargin(YogaEdge.TOP, marginTop);
            }
            if (marginRight != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setMargin(YogaEdge.RIGHT, marginRight);
            }
            if (marginBottom != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setMargin(YogaEdge.BOTTOM, marginBottom);
            }
        }

        if (padding > 0) {
            node.setPadding(YogaEdge.LEFT, padding);
            node.setPadding(YogaEdge.TOP, padding);
            node.setPadding(YogaEdge.RIGHT, padding);
            node.setPadding(YogaEdge.BOTTOM, padding);
        } else {
            if (paddingLeft != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setPadding(YogaEdge.LEFT, paddingLeft);
            }
            if (paddingTop != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setPadding(YogaEdge.TOP, paddingTop);
            }
            if (paddingRight != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setPadding(YogaEdge.RIGHT, paddingRight);
            }
            if (paddingBottom != DBConstants.DEFAULT_SIZE_EDGE) {
                node.setPadding(YogaEdge.BOTTOM, paddingBottom);
            }
        }

        if (flexGrow != 0) {
            node.setFlexGrow(flexGrow);
        }
        if (flexShrink != 0) {
            node.setFlexShrink(flexShrink);
        }
        if (flexBasis != 0) {
            node.setFlexBasis(flexBasis);
        }
        if (flexBasisPercent != 0) {
            node.setFlexBasisPercent(flexBasisPercent);
        }

        if (width > 0) {
            node.setWidth(width);
        } else if (widthPercent != 0) {
            node.setWidthPercent(widthPercent);
        }
        if (minWidth > 0) {
            node.setMinWidth(minWidth);
        } else if (minWidthPercent != 0) {
            node.setMinWidthPercent(minWidthPercent);
        }
        if (height > 0) {
            node.setHeight(height);
        } else if (heightPercent != 0) {
            node.setHeightPercent(heightPercent);
        }
        if (minHeight > 0) {
            node.setMinHeight(minHeight);
        } else if (minHeightPercent != 0) {
            node.setMinHeightPercent(minHeightPercent);
        }

        if (null != alignSelf) {
            switch (alignSelf) {
                case ALIGN_SELF_START:
                    node.setAlignSelf(YogaAlign.FLEX_START);
                    break;
                case ALIGN_SELF_END:
                    node.setAlignSelf(YogaAlign.FLEX_END);
                    break;
                case ALIGN_SELF_CENTER:
                    node.setAlignSelf(YogaAlign.CENTER);
                    break;
                case ALIGN_SELF_STRETCH:
                    node.setAlignSelf(YogaAlign.STRETCH);
                    break;
                case ALIGN_SELF_BASELINE:
                    node.setAlignSelf(YogaAlign.BASELINE);
                    break;
            }
        }
        if (null != positionType) {
            switch (positionType) {
                case POSITION_TYPE_RELATIVE:
                    node.setPositionType(YogaPositionType.RELATIVE);
                    break;
                case POSITION_TYPE_ABSOLUTE:
                    node.setPositionType(YogaPositionType.ABSOLUTE);
                    break;
            }
        }
        if (positionLeftPercent > 0) {
            node.setPositionPercent(YogaEdge.LEFT, positionLeftPercent);
        } else if (positionLeft > 0) {
            node.setPosition(YogaEdge.LEFT, positionLeft);
        }
        if (positionTopPercent > 0) {
            node.setPositionPercent(YogaEdge.TOP, positionTopPercent);
        } else if (positionTop > 0) {
            node.setPosition(YogaEdge.TOP, positionTop);
        }
        if (positionRightPercent > 0) {
            node.setPositionPercent(YogaEdge.RIGHT, positionRightPercent);
        } else if (positionRight > 0) {
            node.setPosition(YogaEdge.RIGHT, positionRight);
        }
        if (positionBottomPercent > 0) {
            node.setPositionPercent(YogaEdge.BOTTOM, positionBottomPercent);
        } else if (positionBottom > 0) {
            node.setPosition(YogaEdge.BOTTOM, positionBottom);
        }

        if (aspectRatio != 0) {
            node.setAspectRatio(aspectRatio);
        }
    }
}