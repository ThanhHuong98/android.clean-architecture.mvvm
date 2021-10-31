package com.nphau.android.shared.screens.widgets;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ToolBarType.CENTER, ToolBarType.LEFT})
@Retention(RetentionPolicy.SOURCE)
public @interface ToolBarType {
    int CENTER = 0;
    int LEFT = 1;
}