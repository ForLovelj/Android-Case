package com.clow.animation_case.weidgt;

/**
 * Callback used to update the UI when path control points are
 * manipulated by the user.
 */
public interface ControlPointCallback {

    void onControlPoint1Moved(float cx1, float cy1);
    void onControlPoint2Moved(float cx2, float cy2);
}
