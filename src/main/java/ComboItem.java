/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danim
 */
// Se usa para llenar JComboBox mostrando texto legible (ej. "Luis Hernández")
// pero conservando el ID real (ej. "PAC-0001") para guardarlo en la base.
public class ComboItem {
    private final String id;
    private final String texto;

    public ComboItem(String id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return texto; // esto es lo que se ve en el combo
    }
}