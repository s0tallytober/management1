package com.venkatesh.schoolmanagement.activity.admin


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.venkatesh.schoolmanagement.ApiClient

import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.RetrofitCallback
import com.venkatesh.schoolmanagement.model.MaterialUpload
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.fragment_material.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MaterialFragment : Fragment() {

    companion object {
        fun getInstance(it: String): MaterialFragment = MaterialFragment().apply {
            arguments = Bundle().apply { putString(Constants.materials, it) }
        }
    }

    private lateinit var material: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.containsKey(Constants.materials).apply {
                if (this) {
                    material = it.getString(Constants.materials)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_material, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Constants.userProfile?.role == Constants.studnet)
            addMaterial.visibility = View.GONE

        addMaterial.setOnClickListener {
            val intent = Intent(activity, AddMaterialActivity::class.java)
            intent.putExtra(Constants.materials, material)
            startActivityForResult(intent, 601)
        }

        recyclerViewMaterials.layoutManager = LinearLayoutManager(activity)

        when (material) {
            getString(R.string.first_class) -> {
                if (Constants.firstMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.firstMaterial)
            }

            getString(R.string.sec_class) -> {
                if (Constants.secMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.secMaterial)
            }

            getString(R.string.third_class) -> {
                if (Constants.thirdMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.thirdMaterial)
            }

            getString(R.string.four_class) -> {
                if (Constants.fourthMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.fourthMaterial)
            }

            getString(R.string.five_class) -> {
                if (Constants.fifthMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.fifthMaterial)
            }

            getString(R.string.six_class) -> {
                if (Constants.sixthMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.sixthMaterial)
            }

            getString(R.string.seven_class) -> {
                if (Constants.seventhMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.seventhMaterial)
            }

            getString(R.string.eight_class) -> {
                if (Constants.eightMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.eightMaterial)
            }

            getString(R.string.nine_class) -> {
                if (Constants.nineMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.nineMaterial)
            }

            getString(R.string.ten_class) -> {
                if (Constants.tenMaterial.isNotEmpty())
                    recyclerViewMaterials.adapter = MaterialAdapter(Constants.tenMaterial)
            }
        }

        loadClassWiseMaterials()
    }

    private fun loadClassWiseMaterials() {
        context?.let {
            ApiClient.getClassMaterials(it, material, object : RetrofitCallback() {
                override fun onResponse(any: Any) {
                    when (material) {
                        getString(R.string.first_class) -> {
                            Constants.firstMaterial.clear()
                            Constants.firstMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.firstMaterial)
                        }

                        getString(R.string.sec_class) -> {
                            Constants.secMaterial.clear()
                            Constants.secMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.secMaterial)
                        }

                        getString(R.string.third_class) -> {
                            Constants.thirdMaterial.clear()
                            Constants.thirdMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.thirdMaterial)
                        }

                        getString(R.string.four_class) -> {
                            Constants.fourthMaterial.clear()
                            Constants.fourthMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.fourthMaterial)
                        }

                        getString(R.string.five_class) -> {
                            Constants.fifthMaterial.clear()
                            Constants.fifthMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.fifthMaterial)
                        }

                        getString(R.string.six_class) -> {
                            Constants.sixthMaterial.clear()
                            Constants.sixthMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.sixthMaterial)
                        }

                        getString(R.string.seven_class) -> {
                            Constants.seventhMaterial.clear()
                            Constants.seventhMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.seventhMaterial)
                        }

                        getString(R.string.eight_class) -> {
                            Constants.eightMaterial.clear()
                            Constants.eightMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.eightMaterial)
                        }

                        getString(R.string.nine_class) -> {
                            Constants.nineMaterial.clear()
                            Constants.nineMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.nineMaterial)
                        }

                        getString(R.string.ten_class) -> {
                            Constants.tenMaterial.clear()
                            Constants.tenMaterial.addAll(any as List<MaterialUpload>)
                            recyclerViewMaterials.adapter = MaterialAdapter(Constants.tenMaterial)
                        }
                    }

                }
            })
        }

    }

    inner class MaterialAdapter(val materials: List<MaterialUpload>) :
        RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MaterialViewHolder {
            return MaterialViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_material, p0, false))
        }

        override fun getItemCount(): Int = materials.size

        override fun onBindViewHolder(p0: MaterialViewHolder, p1: Int) {

            p0.run {
                fileTitle.text = materials[p1].materialDescription
                fileDownload.setOnClickListener {
                    activity?.let { it1 ->
                        ApiClient.downloadDocument(it1, materials[p1])
                    }
                }
            }
        }

        inner class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fileTitle: TextView = view.findViewById<TextView>(R.id.fileTitle)
            val fileDownload: ImageView = view.findViewById<ImageView>(R.id.fileDownload)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 601) {
            loadClassWiseMaterials()
        }
    }
}
