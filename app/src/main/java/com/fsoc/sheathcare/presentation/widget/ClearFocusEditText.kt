package com.fsoc.sheathcare.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText

/**
 * Created by son.nguyen on 2020/10/20.
 */
class ClearFocusEditText: EditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            this.text.clear()
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }
}
