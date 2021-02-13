package com.sushmoyr.habittracker.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sushmoyr.habittracker.R
import com.sushmoyr.habittracker.database.Habit
import com.sushmoyr.habittracker.databinding.FragmentCreateHabitBinding
import com.sushmoyr.habittracker.ui.viewmodel.HabitViewModel
import com.sushmoyr.habittracker.util.Calculations
import java.util.*

class CreateHabitFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var _binding : FragmentCreateHabitBinding? = null
    private val binding get() = _binding!!

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateHabitBinding.inflate(inflater, container, false)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        binding.confirmBtn.setOnClickListener {
            addHabitToDB()
        }

        pickDateAndTime()
        drawableSelected()

        return binding.root
    }

    private fun pickDateAndTime() {
        binding.pickDateBtn.setOnClickListener {
            getDateCalender()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
        binding.pickTimeBtn.setOnClickListener {
            getTimeCalender()
            TimePickerDialog(requireContext(), this, hour, minute, true).show()
        }
    }

    private fun drawableSelected()
    {
        binding.fastfood.setOnClickListener {
            binding.fastfood.isSelected = !binding.fastfood.isSelected
            drawableSelected = R.drawable.ic_fastfood

            binding.cigar.isSelected = false
            binding.tea.isSelected = false

        }

        binding.cigar.setOnClickListener {
            binding.cigar.isSelected = !binding.cigar.isSelected
            drawableSelected = R.drawable.ic_smoking2

            //de-select the other options when we pick an image
            binding.fastfood.isSelected = false
            binding.tea.isSelected = false
        }

        binding.tea.setOnClickListener {
            binding.tea.isSelected = !binding.tea.isSelected
            drawableSelected = R.drawable.ic_tea

            //de-select the other options when we pick an image
            binding.fastfood.isSelected = false
            binding.cigar.isSelected = false
        }
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

    private fun addHabitToDB() {
        //Get text from editTexts
        title = binding.tvTitle.text.toString()
        description = binding.tvDesc.text.toString()

        //Create a timestamp string for our recyclerview
        timeStamp = "$cleanDate $cleanTime"

        //Check that the form is complete before submitting data to the database
        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(0, title, description, timeStamp, drawableSelected)

            //add the habit if all the fields are filled
            habitViewModel.addHabit(habit)
            Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()

            //navigate back to our home fragment
            findNavController().navigate(R.id.action_createHabitFragment_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        cleanTime = Calculations.cleanTime(hourOfDay, minute)
        binding.showTime.text = cleanTime
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        cleanDate = Calculations.cleanDate(dayOfMonth, month, year)
        binding.showDate.text = cleanDate
    }
}