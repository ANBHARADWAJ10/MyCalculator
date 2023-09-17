package com.nikhil.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.nikhil.mycalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression

    var lastNumeric = false
    var lastDot = false
    var stateError = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    // operator Button adds the operators to the TextView only if the last Digit is Number.
    // (view as Button).text  -> converts the text inside the view to normal text so that it can be used to evaluate.
    fun onOperatorClick(view: View) {

        if(lastNumeric && !stateError){
            binding.txtInput.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEnterClick()
        }

    }

    // click on the button to enter the digits
    // (view as Button).text  -> converts the text inside the view to normal text so that it can be used to evaluate.
    fun onDigitButton(view: View) {

        // we first need to check if there is stateError or not
        if(stateError){
            binding.txtInput.text = (view as Button).text
            stateError = false
        }else{
            binding.txtInput.append((view as Button).text)
        }

        lastNumeric = true

        // evaluates the expression and checks if there are any errors or not
        onEnterClick()
        
    }


    // The = button which gives the Output
    // this equal gives us output by taking the output of the evaluated expression
    fun onEqualClick(view: View) {

        onEnterClick()
        binding.txtInput.text = binding.txtOutput.text.toString().drop(1)

    }



    // to clear the Input TextView
    fun onClearClick(view: View) {

        binding.txtInput.text = ""
        lastNumeric = false
        lastDot = false

    }

    // to clear the input and the output
    fun onAllClearClick(view: View) {

        binding.txtInput.text = ""
        binding.txtOutput.text = ""
        binding.txtOutput.visibility = View.GONE

        lastNumeric = false
        lastDot = false
        stateError = false

    }

    fun onBackSpaceClick(view: View) {

        binding.txtInput.text = binding.txtInput.text.toString().drop(1)

        try {
            val lastChar = binding.txtInput.text.toString().last()
            // isDigit is used to check if the specified character is digit or not.
            if(lastChar.isDigit()){
                onEnterClick()
            }
        }catch (e: Exception){
            // if all the digits in the input TextView are gone Then,
            // it goes to the output TextView and erases everything.

            binding.txtOutput.text = ""
            binding.txtOutput.visibility = View.GONE
            Log.e("char error", e.toString())
        }

    }



    // this is used to evaluate the expression
    fun onEnterClick(){

        if(lastNumeric && !stateError){

            // initializing the inputTextView so that it can be used to evaluate.
            val txt = binding.txtInput.text.toString()

            // we need to build the expression using Expression Builder
            expression = ExpressionBuilder(txt).build()

            try{

                // we need result
                val result = expression.evaluate()

                // as we know that we have made the output TextView invisible
                // So we enable it here
                binding.txtOutput.visibility = View.VISIBLE

                // we need to assign the result to the output
                binding.txtOutput.text = result.toString()

            }catch (e: ArithmeticException){
                // we write an Arithmetic exception so that we get an error
                // if the last digit is not number
                Log.e("evaluate error", e.toString())

                // this will be displayed at the time of Error
                binding.txtOutput.text = getString(R.string.error)
                lastNumeric = false
                stateError = true
            }


        }
    }



}