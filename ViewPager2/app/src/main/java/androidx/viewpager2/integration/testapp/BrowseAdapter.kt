package androidx.viewpager2.integration.testapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BrowseAdapter(
    private val data: List<Map<String, Any>>,
    private val onItemClickListener: (item: Map<String, Any>) -> Unit
) : RecyclerView.Adapter<BrowseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position]["title"] as? String ?: "")
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val text1 = itemView.findViewById<TextView>(android.R.id.text1)

        init {
            text1.setOnClickListener {
                val adapterPosition = bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener.invoke(data[adapterPosition])
                }
            }
        }

        fun bind(text: String) {
            text1.text = text
        }
    }
}
