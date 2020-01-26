package com.example.calculator

import org.mariuszgromada.math.mxparser.Expression

class Calculator {
    companion object {

        fun calculate(expression: String): String = doubleToInt("${Expression(expression).calculate()}")

        private fun doubleToInt(expression: String): String {
            if (expression.endsWith(".0"))
                return expression.split(".").first()
            return expression
        }
    }
}