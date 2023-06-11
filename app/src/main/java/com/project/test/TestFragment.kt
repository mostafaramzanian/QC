package com.project.test


import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.project.test.databinding.SpinnerBinding


class TestFragment : Fragment() {

    private lateinit var binding : SpinnerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SpinnerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val processId= SharedPreferences(requireActivity()).getInt("process_id", 0)
        data class Node(val id:Int, val name: String, val parent:String,var children: MutableList<Node> = mutableListOf(),
                        var space: Int = 0, var space1: Int = 0,val charNull: String =" " ,val star: String ="* ")
        val nodes = mutableListOf<Node>()
        val controlStation = Query(requireActivity()).controlStation(processId)
        val columnIndex = controlStation.getColumnIndexOrThrow("name")
        val columnIndex1 = controlStation.getColumnIndexOrThrow("parent")
        val columnIndex2 = controlStation.getColumnIndexOrThrow("id")
        fun findNode(node: Node, id: Int): Node? {
            if (node.id == id) {
                return node
            }
            for (child in node.children) {
                val foundNode = findNode(child, id)
                if (foundNode != null) {
                    return foundNode
                }
            }
            return null
        }
        if (controlStation.moveToFirst()) {
            do {
                val name = controlStation.getString(columnIndex)
                val parent = controlStation.getString(columnIndex1)
                val id = controlStation.getInt(columnIndex2)
                val node= Node(id,name,parent)
                    val parentNode = nodes.find { it.id.toString() == parent }

                    if (parentNode != null) {
                        if (!parentNode.children.contains(node)) {
                            parentNode.children += node
                            node.space = parentNode.space + 1
                            node.space1 = parentNode.space1 + 5
                        }
                    } else {
                        nodes += node
                    }
            } while (controlStation.moveToNext())
        }else{
            CustomToast(requireContext()).toastAlert(null,"ایستگاه کنترلی یافت نشد!")
        }


        fun buildStringRecursive(node: Node, list: MutableList<String>) {
            list.add(node.charNull.repeat(node.space1 + 0)+node.star.repeat(node.space + 0) + node.name)
            node.children.forEach { child ->
                buildStringRecursive(child, list)
            }
        }
        val list = mutableListOf<String>()
        nodes.forEach { node ->
            buildStringRecursive(node, list)
        }

/*
        val toastText = buildString {
            fun buildStringRecursive(node: Node) {
                append(node.character.repeat(node.space + 0))
                append(node.name)
                append("\n")
                node.children.forEach { child ->
                    buildStringRecursive(child)
                }
            }
            nodes.forEach { node ->
                buildStringRecursive(node)
            }
        }


 */


      // binding.textSpinner1.text= list.toString()


        val slideDown: Animation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_down
        )
        val slideUp: Animation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.slide_up
        )
        binding.textSpinner.setOnClickListener {
            if (binding.bodySpinner.visibility == View.VISIBLE) {
                slideUp.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.bodySpinner.clearAnimation();
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                binding.bodySpinner.startAnimation(slideUp);
                binding.bodySpinner.visibility=View.INVISIBLE
            } else {
                binding.bodySpinner.visibility=View.VISIBLE
                binding.bodySpinner.startAnimation(slideDown);
            }
        }
        val typeface1 = ResourcesCompat.getFont(requireContext(), R.font.vazirmatn_medium)
        var previousTextViewId = View.NO_ID
        for (i in list.indices) {
            val newTextView = TextView(requireContext())
            newTextView.id = View.generateViewId()
            newTextView.text = list[i]

            newTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            newTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                15f
            ) // Set the desired text size here
            newTextView.typeface = typeface1 // Set the desired font family here


            binding.bodySpinner.addView(newTextView)

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.bodySpinner)
            if (i == 0) {
                constraintSet.connect(
                    newTextView.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    16
                )
            } else {
                constraintSet.connect(
                    newTextView.id,
                    ConstraintSet.TOP,
                    previousTextViewId,
                    ConstraintSet.BOTTOM,
                    16
                )
            }
            constraintSet.connect(
                newTextView.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                30
            )

            constraintSet.applyTo(binding.bodySpinner)

            previousTextViewId = newTextView.id

            if (i > 0 && i < list.size - 1) {
                val lineView = View(requireContext())
                lineView.id = View.generateViewId()
                lineView.setBackgroundColor(Color.BLACK)
                binding.bodySpinner.addView(lineView)

                val lineConstraintSet = ConstraintSet()
                lineConstraintSet.clone(binding.bodySpinner)
                lineConstraintSet.connect(
                    lineView.id,
                    ConstraintSet.TOP,
                    previousTextViewId,
                    ConstraintSet.BOTTOM,
                    16
                )
                lineConstraintSet.connect(
                    lineView.id,
                    ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT
                )
                lineConstraintSet.connect(
                    lineView.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
                lineConstraintSet.constrainHeight(lineView.id, 2)
                lineConstraintSet.applyTo(binding.bodySpinner)

                previousTextViewId = lineView.id
            }
        }

    }
}


