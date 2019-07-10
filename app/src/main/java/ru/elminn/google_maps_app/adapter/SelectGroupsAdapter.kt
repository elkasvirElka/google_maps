package ru.elminn.google_maps_app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.elminn.google_maps_app.R

class SelectGroupsAdapter : RecyclerView.Adapter<SelectGroupsAdapter.ViewHolder>() {

    private var mGroups = ArrayList<String>()
   private lateinit var mListener: OnClickAdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_select_group, parent, false))
    }

    override fun getItemCount(): Int {
        return mGroups.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mGroups[position])
        if(position == 0)
            holder.itemView.setBackgroundColor(R.color.colorAccent)
        holder.itemView.setOnClickListener{
            if(mListener != null)
            mListener.onClickItem(position)
        }
    }

    fun addData(groups: ArrayList<String>) {
        mGroups.addAll(groups)
        notifyDataSetChanged()
    }

    fun getData() = mGroups

    fun clear() {
        mGroups.clear()
        notifyDataSetChanged()
    }

    interface OnClickAdapterListener {
        fun onClickItem(position: Int)
    }
    fun setListeners(listener: OnClickAdapterListener){
        mListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        fun bind(value: String) {

            (itemView as TextView).text = value
         //   itemView.setOnClickListener(this)
        }

       /* override fun onClick(v: View?) {
            listener.onClickItem(adapterPosition)
        }*/
    }

}


