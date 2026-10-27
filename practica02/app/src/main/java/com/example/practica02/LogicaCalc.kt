package com.example.practica02

/**
 * Lógica aritmética de la calculadora (Paso 5 del guion).
 *
 * Se define como `object` (instancia única / Singleton) por dos motivos:
 *  - Agrupación de funciones: reúne `calcular` (operaciones básicas) y
 *    `calcularPorcentaje` en un único espacio de nombres.
 *  - Se puede invocar directamente como LogicaCalc.calcular(...) sin crear
 *    una instancia nueva cada vez que el usuario presiona un botón.
 */
object LogicaCalc {

    /**
     * Realiza la operación indicada y devuelve el resultado como texto.
     *
     * Manejo de errores:
     *  - Entradas vacías o no numéricas -> toDoubleOrNull() devuelve null y la
     *    función regresa cadena vacía, sin lanzar excepción.
     *  - División entre cero -> devuelve Double.NaN en lugar de colapsar.
     *
     * @param num1 primer operando (texto capturado por la interfaz).
     * @param num2 segundo operando.
     * @param operation uno de "+", "-", "x" o "÷".
     * @return el resultado formateado: sin decimales cuando el resultado es
     *         entero, con decimales en caso contrario.
     */
    fun calcular(num1: String, num2: String, operation: String): String {
        val n1 = num1.toDoubleOrNull() ?: return ""
        val n2 = num2.toDoubleOrNull() ?: return ""

        val result = when (operation) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "x" -> n1 * n2
            "÷" -> if (n2 != 0.0) n1 / n2 else Double.NaN
            else -> 0.0
        }

        return if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    /**
     * Convierte el valor capturado en su equivalente porcentual (n / 100).
     * Usa el mismo criterio de formateo que [calcular].
     */
    fun calcularPorcentaje(num: String): String {
        val n = num.toDoubleOrNull() ?: return ""
        val result = n / 100
        return if (result % 1.0 == 0.0) result.toLong().toString() else result.toString()
    }
}
