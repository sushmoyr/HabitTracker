package com.sushmoyr.habittracker.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.habittracker.ui.fragments.HabitListFragmentDirections
import com.sushmoyr.habittracker.R
import com.sushmoyr.habittracker.database.Habit
import kotlinx.android.synthetic.main.custom_row.view.*

class HabitListAdapter : RecyclerView.Adapter<HabitListAdapter.MyViewHolder>() {

    var habitsList = emptyList<Habit>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.cv_cardView.setOnClickListener {
                val position = adapterPosition
                Log.d("HabitsListAdapter", "Item clicked at: $position")

                val action = HabitListFragmentDirections.actionHabitListToUpdateFragment(habitsList[position])
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentHabit = habitsList[position]
        holder.itemView.iv_habit_icon.setImageResource(currentHabit.imageId)
        holder.itemView.tv_item_description.text = currentHabit.habit_description
        holder.itemView.tv_timeElapsed.text =
            Calculations.calculateTimeBetweenDates(currentHabit.habit_startTime)
        holder.itemView.tv_item_createdTimeStamp.text = "Since: ${currentHabit.habit_startTime}"
        holder.itemView.tv_item_title.text = currentHabit.habit_title
    }

    override fun getItemCount(): Int {
        return habitsList.size
    }

    fun setData(habit: List<Habit>) {
        this.habitsList = habit
        notifyDataSetChanged()
    }


}