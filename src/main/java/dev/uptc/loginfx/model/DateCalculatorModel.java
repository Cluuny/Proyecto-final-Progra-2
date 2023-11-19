package dev.uptc.loginfx.model;  // Declaraci�n del paquete en el que se encuentra esta clase

import java.time.LocalDate;  // Importaci�n de la clase LocalDate para trabajar con fechas

public class DateCalculatorModel {
    public LocalDate calculateFutureDate(LocalDate currentDate, int daysToAdd) {
        // M�todo para calcular una fecha futura a partir de una fecha actual y d�as a agregar
        return currentDate.plusDays(daysToAdd);  // Calcula y devuelve la fecha futura
    }
}
