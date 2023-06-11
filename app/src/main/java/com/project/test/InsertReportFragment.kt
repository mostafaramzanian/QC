package com.project.test

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.test.databinding.InsertReportFragmentBinding


class InsertReportFragment : Fragment() {

    private lateinit var binding: InsertReportFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InsertReportFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        model.showHide("Hide")
       val processId= SharedPreferences(requireActivity()).getInt("process_id", 0)
        val nodes = mutableListOf<DataNode>()

        val controlStation = Query(requireActivity()).controlStation(processId)
        val columnIndex = controlStation.getColumnIndexOrThrow("name")
        val columnIndex1 = controlStation.getColumnIndexOrThrow("parent")
        val columnIndex2 = controlStation.getColumnIndexOrThrow("id")

        if (controlStation.moveToFirst()) {
            do {
                val name = controlStation.getString(columnIndex)
                val parent = controlStation.getString(columnIndex1)
                val id = controlStation.getInt(columnIndex2)
                val node= DataNode(id,name,parent)
                    val parentNode = nodes.find { it.id.toString() == parent }
                    if (parentNode != null) {
                            parentNode.children += node
                            node.space = parentNode.space + 1
                            node.space1 = parentNode.space1 + 6
                    }
                        nodes += node

            } while (controlStation.moveToNext())
        }else{
         CustomToast(requireContext()).toastAlert(null,"ایستگاه کنترلی یافت نشد!")
        }
        fun buildStringRecursive(node: DataNode, list: MutableList<String>, set: HashSet<Int>) {
            if (set.add(node.id)) {
                val string = node.charNull.repeat(node.space1 + 0) + node.star.repeat(node.space + 0) + node.name
                list.add(string)
                node.children.forEach { child ->
                    buildStringRecursive(child, list, set)
                }
            }
        }
        val list = mutableListOf<String>()
        val set = HashSet<Int>()
        nodes.forEach { node ->
            buildStringRecursive(node, list, set)
        }
        /*
        val toastText = buildString {
            fun buildStringRecursive(node: Node) {
                    append(node.star.repeat(node.space + 0))
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
     val test  = nodes.find { it.name.toString() == "111" }
        if (test != null) {

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
       var cpValue:String?=null
        var csValueSelected:String?=null
        var csIndexSelected:Int?=null
        var cpValueSelected:String?=null
        var cpIndexSelected:Int?=null
        val listIdCp = mutableListOf<Int>()
        val listName= mutableListOf<String>()
        val listId= mutableListOf<Int>()
        binding.spinnerView.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            binding.arrowSpinner.startAnimation(animUp)
            csValueSelected=newItem
            csIndexSelected=newIndex
            for (i in newIndex downTo 0) {
                listName.clear()
                 listId.clear()
                    val (name, id) = findNode1(
                        requireActivity(),
                        nodes,
                        nodes[i].id.toString()
                    )
                listName.addAll(name)
                listId.addAll(id)

              if(nodes[i].parent=="#root"|| name.size>0){
                  break
              }
            }
            binding.spinnerViewQuality.setItems(listName)
            listIdCp.addAll(listId)
            if(listName.size>0) {
                cpValue = listName[0]
                binding.arrowSpinner1.visibility=View.VISIBLE
                binding.spinnerViewQuality.setPadding(20, 20, 80, 20)
                binding.spinnerViewQuality.hint="انتخاب کنید"
                if(binding.spinnerViewQuality.text!=null){
                    binding.spinnerViewQuality.text="انتخاب کنید"
                }
                if( binding.spinnerViewQuality.text=="انتخاب کنید" ||  binding.spinnerViewQuality.hint=="انتخاب کنید"){
                    cpValueSelected=null
                    cpIndexSelected=null
                }
            }else{
                cpValue=null
                cpValueSelected=null
                cpIndexSelected=null
                binding.arrowSpinner1.clearAnimation()
                binding.spinnerViewQuality.setPadding(22, 22, 20, 22)
                binding.arrowSpinner1.visibility=View.INVISIBLE
                binding.spinnerViewQuality.hint="برای ایستگاه کنترلی انتخاب شده، طرح کیفیتی یافت نشد!"
              if(binding.spinnerViewQuality.text!=null){
                  binding.spinnerViewQuality.text="برای ایستگاه کنترلی انتخاب شده، طرح کیفیتی یافت نشد!"
              }
            }

        }
            binding.spinnerViewQuality.setOnClickListener {
                if (cpValue!=null) {
                    if (binding.spinnerViewQuality.isShowing) {
                        binding.spinnerViewQuality.dismiss()
                        binding.arrowSpinner1.startAnimation(animUp1)
                    } else {
                        binding.spinnerViewQuality.show()
                        binding.arrowSpinner1.startAnimation(animDown1)
                        binding.spinnerView.dismiss()
                    }
                }
            }

            binding.spinnerViewQuality.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                binding.arrowSpinner1.startAnimation(animUp1)
                cpValueSelected=newItem
                cpIndexSelected= listIdCp[newIndex]

            }

        val fragmentList = Stack()
        fragmentList.reset()
        fragmentList.push(requireActivity(), R.id.fragmentContainer)
        model.showLastFragment("InsertReportFragment")
        binding.btnAdd.setOnClickListener {
            val sharedPreferences = SharedPreferences(requireContext())
            if(!sharedPreferences.getBoolean("menuExit",false)) {
                when {
                    cpValueSelected != null -> {
                        val model1 = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
                        model1.sendMessage1("start")
                        FragmentReplacer(parentFragmentManager).replaceFragments(
                            InsertReportFragment(),
                            ShowMoreFormFragment(),
                            R.id.fragmentContainer
                        )
                        sharedPreferences.putString("cpValueSelected",
                            cpValueSelected!!
                        )
                        sharedPreferences.putString("csValueSelected",
                            nodes[csIndexSelected!!].name
                        )
                        sharedPreferences.putInt("csIdSelected",
                            nodes[csIndexSelected!!].id
                        )
                        sharedPreferences.putInt("cpIdSelected",
                            cpIndexSelected!!
                        )
                        val id =GetData(requireActivity()).insertReportFragment(cpIndexSelected!!)
                        if (id>0) {
                            sharedPreferences.putInt("idReports",
                                id
                            )
                            CustomToast(requireContext()).toastAlert(null, "توجه: کاربری گرامی شما گزارش فعال برای این طرح کیفیت دارید!")
                        }else{
                            sharedPreferences.putInt("idReports",
                                id
                            )
                        }
                        GetData(requireActivity()).count(requireActivity(),requireActivity(),requireActivity(),sharedPreferences.getInt("idReports", 5))

                    }
                    csValueSelected == null -> {
                        CustomToast(requireContext()).toastAlert(
                            null,
                            "به دلیل عدم انتخاب ایستگاه کنترلی امکان ایجاد فرم گزارش وجود ندارد!"
                        )
                    }
                    cpValueSelected == null -> {
                        CustomToast(requireContext()).toastAlert(
                            null,
                            "به دلیل عدم انتخاب طرح کیفیت امکان ایجاد فرم گزارش وجود ندارد!"
                        )
                    }
                }
            }
        }
        val size = Size(requireContext()).fontSize(0.029f)
        val size1 = Size(requireContext()).fontSize(0.04f)

        val textView = view.findViewById<TextView>(R.id.text_help)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)

        val textView1 = view.findViewById<TextView>(R.id.textView)
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, size1)
        val textView2 = view.findViewById<TextView>(R.id.quality)
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, size1)
        val button = view.findViewById<Button>(R.id.btn_add)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, size1)

        model.spinner.observe(viewLifecycleOwner, Observer {
            if(it=="hidden")
            {
                if(binding.spinnerView.isShowing)
                {
                    binding.arrowSpinner.startAnimation(animUp)
                    binding.spinnerView.dismiss()
                }
                if(binding.spinnerViewQuality.isShowing){
                    binding.arrowSpinner1.startAnimation(animUp1)
                    binding.spinnerViewQuality.dismiss()
                }
            }
        })

    }

    override fun onPause() {
        super.onPause()
        binding.spinnerViewQuality.dismiss()
        binding.spinnerView.dismiss()

    }
}

private fun findNode1(context:Activity, nods:MutableList<DataNode>, idSelect: String):Pair<List<String>,List<Int>>{

   val cp = Query(context).cp(idSelect)
    val listName = mutableListOf<String>()
    val listId = mutableListOf<Int>()
    if (cp.moveToFirst()) {
        do {
            val cpCode = cp.getString(cp.getColumnIndexOrThrow("cp_code"))
        val selectCp = Query(context).selectFromCp(cpCode)
        if(selectCp.moveToFirst()){
            val cpName=selectCp.getString(selectCp.getColumnIndexOrThrow("cp_code"))
            val cpId=selectCp.getInt(selectCp.getColumnIndexOrThrow("id"))
            val cpAccess= selectCp.getString(selectCp.getColumnIndexOrThrow("access_user_group"))
            val list = cpAccess.replace("'", "").split(",").map { it.trim().replace("[", "").replace("]", "") }
            // val index = list.indexOf( SharedPreferences(context).getString("userType", ""))
           val index = list.indexOf("MASTER_WORKER")
            if (index != -1 && cpName!=null) {
                listName.add(cpName)
                listId.add(cpId)
            } else {

            }
        }
        }
        while (cp.moveToNext())
    }
    return Pair(listName, listId)

}

//
private fun findNode(node: DataNode, id: Int): Int? {
    if (node.id == id) {
        return node.id
    }
    for (child in node.children) {
        val foundNode = findNode(child, id)
        if (foundNode != null) {
            return foundNode
        }
    }
    return null
}