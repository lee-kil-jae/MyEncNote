package com.studiolkj.myencnote.model.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.studiolkj.myencnote.R
import com.studiolkj.myencnote.databinding.RowMainListBinding
import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.viewModel.MainViewModel

class ListAdapterMemo(var vm : MainViewModel)
    : PagedListAdapter<MemoData, RecyclerView.ViewHolder>(DIFF_CALLBACK){

    companion object{

        private val DIFF_CALLBACK: DiffUtil.ItemCallback<MemoData> =
            object : DiffUtil.ItemCallback<MemoData>() {
                override fun areItemsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
                    return oldItem.index == newItem.index
                }

                override fun areContentsTheSame(oldItem: MemoData, newItem: MemoData): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }

    class ItemViewHolder(val binding: RowMainListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            DataBindingUtil.inflate(
                inflater,
                R.layout.row_main_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).binding.run {
            memo = getItem(position)
            vmMain = vm
        }
    }

    fun getItemPosition(storybookId: Int): Int{
        currentList?.let{
            var inc = 0
            for(storybook in it){
                if(storybook.index == storybookId){
                    return inc
                }
                inc++
            }
        }

        return -1
    }
}