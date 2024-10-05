package com.example.taskreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.example.taskreminder.databinding.ActivityInputReminderBinding

class InputReminderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityInputReminderBinding
    private val calender = Calendar.getInstance()
    private lateinit var selectedDate: String;
    private lateinit var selectedTime: String;
    private lateinit var repeat: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInputReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repeats = resources.getStringArray(R.array.repeat)
        with(binding) {
            val adapterSpinnerRepeat = ArrayAdapter(
                this@InputReminderActivity,
                android.R.layout.simple_spinner_item,
                repeats
            )
            spinnerRepeat.adapter = adapterSpinnerRepeat
            spinnerRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    repeat = repeats.get(position)
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            selectDate.setOnClickListener {
                DatePickerDialog(
                    this@InputReminderActivity,
                    this@InputReminderActivity,
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            selectTime.setOnClickListener{
                TimePickerDialog(
                    this@InputReminderActivity,
                    this@InputReminderActivity,
                    calender.get(Calendar.HOUR_OF_DAY),
                    calender.get(Calendar.MINUTE),
                    false
                ).show()
            }

            btnToResult.setOnClickListener {
                val builder = AlertDialog.Builder(this@InputReminderActivity)
                    .setTitle("SimpeRemind")
                    .setMessage("Do yo want to add this as new task?")
                    .setPositiveButton("Yes"){_, _ ->
                        val intentToResult =
                            Intent(this@InputReminderActivity, ResultReminderActivity::class.java)
                        intentToResult.putExtra("EXTRA_TITLE", etTitle.text.toString())
                        intentToResult.putExtra("EXTRA_REPEAT", repeat)
                        intentToResult.putExtra("EXTRA_DATE", selectedDate)
                        intentToResult.putExtra("EXTRA_TIME", selectedTime)
                        startActivity(intentToResult)
                    }
                    .setNegativeButton("No"){dialog, _ -> dialog.dismiss()}
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun displayTimeDate (time: String){
        binding.selectTime.text = time
    }

    private fun displayTextDate (date: String){
        binding.txtSelectDate.text = date
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calender.set(year, month, dayOfMonth)
        selectedDate = "$dayOfMonth/${month+1}/$year"
        displayTextDate(selectedDate)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calender.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calender.set(Calendar.MINUTE, minute)
        selectedTime = "$hourOfDay : $minute : 00"
        displayTimeDate(selectedTime)
    }
}