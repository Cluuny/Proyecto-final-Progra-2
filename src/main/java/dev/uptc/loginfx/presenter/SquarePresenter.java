package dev.uptc.loginfx.presenter;

import dev.uptc.loginfx.model.SquareModel;
import dev.uptc.loginfx.views.SquareView;

public class SquarePresenter {

    private SquareModel model;      // Declaraci�n de una instancia de SquareModel para el modelo
    private SquareView view;        // Declaraci�n de una instancia de SquareView para la vista

    public SquarePresenter(SquareView view) {  // Constructor de la clase SquarePresenter
        this.model = new SquareModel();        // Creaci�n de una instancia de SquareModel
        this.view = view;                      // Asociaci�n de la vista pasada como argumento con esta instancia
    }

    public void calculateSquare() {  // M�todo para calcular el cuadrado del n�mero ingresado
        double number = view.getInputNumber();  // Obtiene el n�mero de la vista
        double result = model.square(number);   // Calcula el cuadrado utilizando el modelo
        view.setResult(result);                 // Establece el resultado en la vista
    }
}
