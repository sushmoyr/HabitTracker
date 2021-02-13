package com.sushmoyr.habittracker.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sushmoyr.habittracker.R
import com.sushmoyr.habittracker.database.Habit
import com.sushmoyr.habittracker.databinding.FragmentUpdateBinding
import com.sushmoyr.habittracker.ui.viewmodel.HabitViewModel
import com.sushmoyr.habittracker.util.Calculations
import java.util.*

class UpdateFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var _binding:FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

    private var title = ""
    private var description = ""
    private var drawableSelected = 0
    private var timeStamp = ""

    private lateinit var habitViewModel: HabitViewModel

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var cleanDate = ""
    private var cleanTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        subscribeUI()
        pickDateAndTime()
        drawableSelected()

        binding.updateBtn.setOnClickListener {
            updateDB()
        }


        return binding.root
    }

    private fun updateDB() {
        title = binding.tvUpdateTitle.text.toString()
        description = binding.tvUpdateDesc.text.toString()
        timeStamp = "$cleanDate $cleanTime"


        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(args.selectedHabits.id, title, description, timeStamp, drawableSelected)


            habitViewModel.updateHabit(habit)
            Toast.makeText(context, "Habit updated successfully!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun subscribeUI() {

        binding.tvUpdateTitle.setText(args.selectedHabits.habit_title)
        binding.tvUpdateDesc.setText(args.selectedHabits.habit_description)
    }

    private fun getTimeCalender() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun getDateCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDateAndTime() {
        binding.updatePickDateBtn.setOnClickListener {
            getDateCalender()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        binding.updatePickTimeBtn.setOnClickListener {
            getTimeCalender()
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }
    }

    private fun drawableSelected()
    {
        binding.updateFastfood.setOnClickListener {
            binding.updateFastfood.isSelected = !binding.updateFastfood.isSelected
            drawableSelected = R.drawable.ic_fastfood

            binding.updateCigar.isSelected = false
            binding.updateTea.isSelected = false

        }

        binding.updateCigar.setOnClickListener {
            binding.updateCigar.isSelected = !binding.updateCigar.isSelected
            drawableSelected = R.drawable.ic_smoking2

            binding.updateFastfood.isSelected = false
            binding.updateTea.isSelected = false
        }

        binding.updateTea.setOnClickListener {
            binding.updateTea.isSelected = !binding.updateTea.isSelected
            drawableSelected = R.drawable.ic_tea

            binding.updateFastfood.isSelected = false
            
            binding.updateCigar.isSelected = false
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        cleanTime = Calculations.cleanTime(hourOfDay, minute)
        binding.updateShowTime.text = cleanTime
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cleanDate = Calculations.cleanDate(dayOfMonth, month, year)
        binding.updateShowDate.text = cleanDate
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.single_item_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId)
        {
            R.id.nav_delete -> deleteHabit(args.selectedHabits)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteHabit(selectedHabits: Habit) {
        habitViewModel.deleteHabit(selectedHabits)
        Toast.makeText(context, "Habit Deleted", Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateFragment_to_habitList)
    }


}