package com.example.calculator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), Calculatable {

    private var expressions = mutableListOf<String>()
    private var lastResult = ""

    private lateinit var expressionTextView: TextView
    private lateinit var resultTextView: TextView

    private companion object {
        const val EXPRESSIONS = "EXPRESSIONS"
        const val EXPRESSION_KEY = "EXPRESSION_KEY"
        const val RESULT_KEY = "RESULT_KEY"
        const val LAST_RESULT = "LAST_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTextView = findViewById(R.id.resultTextView)
        expressionTextView = findViewById(R.id.equationTextView)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXPRESSION_KEY, expressionTextView.text.toString())
        outState.putString(RESULT_KEY, resultTextView.text.toString())
        outState.putStringArray(EXPRESSIONS, expressions.toTypedArray())
        outState.putString(LAST_RESULT, lastResult)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        expressionTextView.text = savedInstanceState.getString(EXPRESSION_KEY)
        resultTextView.text = savedInstanceState.getString(RESULT_KEY)
        expressions = savedInstanceState.getStringArray(EXPRESSIONS)!!.toMutableList()
        lastResult = savedInstanceState.getString(LAST_RESULT)!!
    }

    override fun push(element: String) {
        if (expressions.isNotEmpty()) isNeedMult(element)
        expressions.add(element)
        expressionTextView.append(element)
        prepareResult()
    }

    override fun pop() {
        if (expressions.isNotEmpty()) {
            val last = expressions.last()
            expressions.removeAt(expressions.lastIndex)
            expressionTextView.text = expressionTextView.text.toString().dropLast(last.length)
            prepareResult()
        }
    }

    override fun clearAll() {
        expressions.clear()
        expressionTextView.text = ""
        resultTextView.text = ""
        lastResult = ""
    }
    private fun isNeedMult(element: String) {
        val matchString = "1234567890pie)"
        val compare = listOf("pi", "e", "sin(", "cos(", "tg(", "ctg(", "ln(", "log(", "log2(", "log10(", "abs(", "sqrt(", "(")
        val consts = "pie"
        val numbers = "12345678pie"

        if (expressions.last() in matchString && element in compare || expressions.last() in consts && element in numbers) {
            expressions.add("*")
            expressionTextView.append("*")
        }
    }

    private fun prepareResult() {
        when (val calculation = Calculator.calculate(expressionTextView.text.toString())) {
            "NaN" -> {
                if (expressionTextView.text.toString() == "") {
                    resultTextView.text = ""
                    return
                }
            }
            else -> {
                resultTextView.text = calculation
                lastResult = calculation
            }
        }
    }
}
