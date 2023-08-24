package com.project.test.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.test.R
import com.project.test.databinding.InsertReportFragmentBinding
import com.project.test.dataclass.DataNode
import com.project.test.model.GetData
import com.project.test.utils.CustomToast
import com.project.test.utils.NavigationApp
import com.project.test.utils.SharedPreferences
import com.project.test.utils.SharedViewModel


class InsertReportFragment : Fragment() {

    private lateinit var binding: InsertReportFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InsertReportFragmentBinding.inflate(inflater)

        val view = binding.btnAdd
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.loading.layoutParams.height = view.height
                binding.loading.layoutParams.width = view.width
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.showHide("show")
        model.showcase("InsertReportFragment")

        val nodes = GetData(requireActivity(),requireActivity()).controlStation()

        val list = mutableListOf<String>()
        val set = HashSet<Int>()
        nodes.forEach { node ->
            buildStringRecursive(node, list, set)
        }
        binding.spinnerView.setItems(list)
        val animDown = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.spinner_dropdown_anim
        )
        val animUp = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.spinner_dropup_anim
        )
        binding.spinnerView.setOnClickListener {
            if (binding.spinnerView.isShowing) {
                binding.spinnerView.dismiss()
                binding.arrowSpinner.startAnimation(animUp)
            } else {
                binding.spinnerView.show()
                binding.arrowSpinner.startAnimation(animDown)
                binding.spinnerViewQuality.dismiss()
            }
        }
        val animDown1 = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.spinner_dropdown_anim
        )
        val animUp1 = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.spinner_dropup_anim
        )
        var cpValue: String? = null
        var csValueSelected: String? = null
        var csIndexSelected: Int? = null
        var cpValueSelected: String? = null
        var cpIndexSelected: Int? = null
        var productName: String? = null
        val listIdCp = mutableListOf<Int>()
        val listName = mutableListOf<String>()
        val listName1 = mutableListOf<String>()
        val product = mutableListOf<String>()
        val listId = mutableListOf<Int>()
        binding.spinnerView.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            binding.arrowSpinner.startAnimation(animUp)
            csValueSelected = newItem
            csIndexSelected = newIndex
            var parent = ""
            for (i in newIndex downTo 0) {
                if (nodes[i].parent == "#root") {
                    parent = nodes[i].name
                    break
                }
            }
            for (i in newIndex downTo 0) {
                listName.clear()
                listId.clear()
                val listCp = GetData(requireActivity(),requireActivity()).findNode1(
                    requireActivity(),
                    nodes,
                    nodes[i].id.toString()
                )
                for (cp in listCp) {
                    listName1.add(cp.cp_code)
                    listId.add(cp.id)
                    product.add(cp.product)
                    listName.add("$parent >> ${cp.product} (${cp.cp_code})")
                }

                if (nodes[i].parent == "#root" || listCp.size > 0) {
                    break
                }
            }
            binding.spinnerViewQuality.setItems(listName)
            //  listIdCp.addAll(listId)
            if (listName.size > 0) {
                cpValue = listName[0]
                binding.arrowSpinner1.visibility = View.VISIBLE
                binding.spinnerViewQuality.visibility = View.VISIBLE
                binding.noItemCp.visibility = View.GONE
                if (binding.spinnerViewQuality.visibility == View.VISIBLE) {
                    cpValueSelected = null
                    cpIndexSelected = null
                    productName = null
                }
            } else {
                cpValue = null
                cpValueSelected = null
                cpIndexSelected = null
                productName = null
                binding.arrowSpinner1.clearAnimation()
                binding.arrowSpinner1.visibility = View.GONE
                binding.spinnerViewQuality.visibility = View.GONE
                binding.noItemCp.visibility = View.VISIBLE
            }
        }
        binding.spinnerViewQuality.setOnClickListener {
            if (cpValue != null) {
                if (binding.spinnerViewQuality.isShowing) {
                    binding.spinnerViewQuality.dismiss()
                    binding.arrowSpinner1.startAnimation(animUp1)
                } else {
                    binding.spinnerViewQuality.show()
                    binding.arrowSpinner1.startAnimation(animDown1)
                    binding.spinnerView.dismiss()
                }
            }
            binding.spinnerViewQuality.spinnerPopupHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        binding.spinnerViewQuality.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            binding.arrowSpinner1.startAnimation(animUp1)

            cpValueSelected = listName1[newIndex]
            cpIndexSelected = listId[newIndex]
            productName = product[newIndex]
        }

        binding.constraintLayout1.setOnClickListener {
            if (binding.spinnerView.isShowing) {
                binding.spinnerView.dismiss()
                binding.arrowSpinner.startAnimation(animUp)
            }
            if (binding.spinnerViewQuality.isShowing) {
                binding.spinnerViewQuality.dismiss()
                binding.arrowSpinner1.startAnimation(animUp1)
            }
        }
        val btn = binding.btnAdd
        btn.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.loading.layoutParams.height = btn.height
                btn.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        model.showLastFragment("InsertReportFragment")
        binding.btnAdd.setOnClickListener {
            if (binding.spinnerView.isShowing) {
                binding.spinnerView.dismiss()
                binding.arrowSpinner.startAnimation(animUp)
            }
            if (binding.spinnerViewQuality.isShowing) {
                binding.spinnerViewQuality.dismiss()
                binding.arrowSpinner1.startAnimation(animUp1)
            }
         //   val sharedPreferences = SharedPreferences(requireContext())
//            if (!sharedPreferences.getBoolean("menuExit", false)) {
                when {
                    cpValueSelected != null -> {
                        binding.btnAdd.visibility = View.GONE
                        binding.loading.visibility = View.VISIBLE
                        val model1 =
                            ViewModelProvider(requireActivity())[SharedViewModel::class.java]
                        model1.sendMessage1("start")
                        model1.isDraft(1)
                        NavigationApp(
                            requireActivity(),
                            parentFragmentManager,
                            R.id.fragmentContainer
                        ).navigationForward(
                            "ShowMoreFormFragment"
                        )
//                        sharedPreferences.putString(
//                            "cpValueSelected",
//                            cpValueSelected!!
//                        )
//                        sharedPreferences.putString(
//                            "csValueSelected",
//                            nodes[csIndexSelected!!].name
//                        )
//                        sharedPreferences.putString(
//                            "productName",
//                            productName!!
//                        )
//                        sharedPreferences.putInt(
//                            "csIdSelected",
//                            nodes[csIndexSelected!!].id
//                        )
//                        sharedPreferences.putInt(
//                            "cpIdSelected",
//                            cpIndexSelected!!
//                        )
                        val id = GetData(requireActivity(),requireActivity()).insertReportFragment(cpIndexSelected!!)
                        if (id > 0) {
//                            sharedPreferences.putInt(
//                                "idReports",
//                                id
//                            )
                            model1.insertInformationData(
                                cpValueSelected!!,
                                nodes[csIndexSelected!!].name,
                                productName!!,
                                nodes[csIndexSelected!!].id,
                                cpIndexSelected!!,
                                id
                            )
                            CustomToast(requireContext()).toastAlert(
                                null,
                                "توجه: کاربر گرامی شما گزارش فعال برای این طرح کیفیت دارید!",
                                15f,
                                Gravity.CENTER
                            )
                        } else {
//                            sharedPreferences.putInt(
//                                "idReports",
//                                id
//                            )
                            model1.insertInformationData(
                                cpValueSelected!!,
                                nodes[csIndexSelected!!].name,
                                productName!!,
                                nodes[csIndexSelected!!].id,
                                cpIndexSelected!!,
                                id
                            )
                        }
//                        GetData(requireActivity()).count(
//                            requireActivity(),
//                            requireActivity(),
//                            requireActivity(),
//                            sharedPreferences.getInt("idReports", 5)
//                        )
                    }

                    csValueSelected == null -> {
                        CustomToast(requireContext()).toastAlert(
                            null,
                            "به دلیل عدم انتخاب ایستگاه کنترلی امکان ایجاد فرم گزارش وجود ندارد!",
                            15f,
                            Gravity.CENTER
                        )
                    }

                    cpValueSelected == null -> {
                        CustomToast(requireContext()).toastAlert(
                            null,
                            "به دلیل عدم انتخاب طرح کیفیت امکان ایجاد فرم گزارش وجود ندارد!",
                            15f,
                            Gravity.CENTER
                        )
                    }
                }
          //  }
            CustomToast(requireContext()).cancelAllToasts(1)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.spinnerViewQuality.dismiss()
        binding.spinnerView.dismiss()

    }

    override fun onDestroy() {
        super.onDestroy()
        CustomToast(requireContext()).cancelAllToasts(0)
    }
}


private fun buildStringRecursive(node: DataNode, list: MutableList<String>, set: HashSet<Int>) {
    if (set.add(node.id)) {
        val string =
            node.charNull.repeat(node.space1 + 0) + node.star.repeat(node.space + 0) + node.name
        list.add(string)
        node.children.forEach { child ->
            buildStringRecursive(child, list, set)
        }
    }
}


