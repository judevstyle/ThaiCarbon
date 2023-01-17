package com.taitos.testpjk.view.bill

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taitos.testpjk.R
import com.taitos.testpjk.databinding.ItemBillBinding
import com.taitos.testpjk.model.Bill

class BillAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1
    var onClickImageListener: ((Bill) -> Unit)? = null

    var dataItem: ArrayList<Bill>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflate = LayoutInflater.from(parent.context)

//        if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemBillBinding.inflate(inflate, parent, false)
            return GalleryViewHolder(binding)
//        } else return LoadingViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
//        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is GalleryViewHolder) {
            val data = dataItem!!.get(position)

            holder.setData(data!!)
//            holder.itemView.setOnClickListener {
//
//            }

        }

    }


    fun setItem(data: ArrayList<Bill>) {

        dataItem = data

        notifyDataSetChanged()


    }

    override fun getItemViewType(position: Int): Int {
        return if (dataItem!!.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }


    override fun getItemCount(): Int {
        return dataItem?.let {
            it.size
        } ?: kotlin.run {
            0
        }
    }


    fun removeLoadingView() {
        //Remove loading item
        if (dataItem?.size != 0) {
            dataItem!!.removeAt(dataItem!!.size - 1)
            notifyItemRemoved(dataItem!!.size)
        }
    }

    fun addLoadingView() {
        //Add loading item
//        dataItem!!.add(null)
        notifyItemInserted(dataItem!!.size - 1)

    }

    fun clear() {
        dataItem?.clear()
    }

//    override fun getItemId(position: Int): Long {
//
//        dataItem?.get(position)?.let {
//            val data = dataItem!!.get(position)
//            return data!!.seq.hashCode().toLong()
//
//        }
//        return 0
//    }


    fun removeItem(position: Int) {

        Log.e("size", "${position}")
        dataItem?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataItem!!.size)

    }

    inner class GalleryViewHolder(val itemsView: ItemBillBinding) :
        RecyclerView.ViewHolder(itemsView.root) {

        @SuppressLint("ResourceAsColor")
        fun setData(data: Bill) {

            itemsView.root.setOnClickListener {
                onClickImageListener?.invoke(data)
            }
            itemsView.apply {
                itemsView.data = data


                if (data.status == 1) {
                    status.text = "สถานะ : รอการดำเนินงาน"
                    status.setTextColor(Color.parseColor("#7C7C0D"))

                }else{
                    status.setTextColor(Color.parseColor("#FF0000"))

                    status.text = "สถานะ : ดำเนินงานไม่สำเร็จ"


                }
            }


        }

    }


}