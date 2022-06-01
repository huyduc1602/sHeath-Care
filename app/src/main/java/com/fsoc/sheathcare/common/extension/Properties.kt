package com.fsoc.sheathcare.common.extension

import org.jetbrains.anko.internals.AnkoInternals

var android.view.View.backgroundColor: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) = setBackgroundColor(v)

var android.view.View.backgroundResource: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) = setBackgroundResource(v)

var android.widget.TextView.textColor: Int
    @Deprecated(AnkoInternals.NO_GETTER, level = DeprecationLevel.ERROR) get() = AnkoInternals.noGetter()
    set(v) = setTextColor(v)
