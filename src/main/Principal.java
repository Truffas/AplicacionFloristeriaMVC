package main;

import com.formdev.flatlaf.FlatLightLaf;
import gui.Controlador;
import gui.Modelo;
import gui.Vista;

import javax.swing.*;

public class Principal {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("No se ha podido aplicar FlatLaf.");
        }

        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo,vista);
    }
}
