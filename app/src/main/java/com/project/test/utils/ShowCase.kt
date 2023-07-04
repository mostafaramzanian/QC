package com.project.test.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.project.test.R

class ShowCase {
    fun createShowCase(context:Activity,targets:Array<TapTarget>,radius:Array<Int>,index1:Array<Any>) {
        val sequence = TapTargetSequence(context)
        for ((index, target) in targets.withIndex()) {
            sequence.targets(
                target
                    .outerCircleAlpha(0.99f)
                    .targetCircleColor(R.color.white)
                    .titleTextSize(30)
                    .titleTextColor(R.color.white)
                    .descriptionTextSize(20)
                    .textTypeface(
                        ResourcesCompat.getFont(
                            context,
                            R.font.vazirmatn_medium
                        )
                    )

                    .dimColor(R.color.black)
                    .drawShadow(true)
                    .cancelable(true)
                    .transparentTarget(false)
                    .targetRadius(radius[index]),
            )
            if(index == index1[index])
            {
                target.tintTarget(false)
            }else{
                target.tintTarget(true)
            }

        }

        sequence.start()
    }
}