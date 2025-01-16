package org.dis.front;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;
import org.dis.back.TipoEmpleado;

import java.util.Arrays;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private TextField creaLabel(String texto){
        TextField etiqueta = new TextField();
        etiqueta.setCaption(texto);
        return etiqueta;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout salarioBruto = new HorizontalLayout();
        final HorizontalLayout salarioNeto = new HorizontalLayout();

        final VerticalLayout salarioBrutoContenedor = new VerticalLayout();
        final VerticalLayout salarioNetoContenedor = new VerticalLayout();

        //Parametros salario bruto
        //TextField tipo = creaLabel("tipo de empleado");
        ComboBox<String> tipoEmpleadoComboBox = new ComboBox<>("Tipo de empleado",
        Arrays.asList(TipoEmpleado.ENCARGADO, TipoEmpleado.VENDEDOR));

        TextField ventaMes = creaLabel("ventas del mes");
        TextField horasExtra = creaLabel("horas extra");

        //Parametros salario neto
        //TextField inSalarioNeto = creaLabel("introduce salario neto");
        TextField inSalarioBruto = creaLabel("introduce salario bruto");

        salarioBruto.addComponents(tipoEmpleadoComboBox, ventaMes, horasExtra);
        Button botonSalarioBruto = new Button("Calcular salario bruto");
        botonSalarioBruto.addClickListener(e -> {
            String tipoEmpleadoIn = tipoEmpleadoComboBox.getValue();
            double ventasMesIn = Double.parseDouble(ventaMes.getValue());
            double horasExtraIn = Double.parseDouble(horasExtra.getValue());

            EmpleadoBR empleado = new EmpleadoBR();

            try {
                double resultado = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventasMesIn, horasExtraIn);
                Label labelSalarioBruto = new Label("Salario bruto obtenido: " + resultado);
                salarioBrutoContenedor.removeAllComponents();
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            } catch (BRException brException) {
                Label labelSalarioBruto = new Label("Error: " + brException.getMessage());
                salarioBrutoContenedor.removeAllComponents();
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            }
        });


        Button botonSalarioNeto = new Button("Calcular salario neto");
        botonSalarioNeto.addClickListener(e -> {
            double salarioBrutoIn = Double.parseDouble(inSalarioBruto.getValue());
            EmpleadoBR empleado = new EmpleadoBR();
            try {
                double resultado = empleado.calculaSalarioNeto(salarioBrutoIn);
                Label labelSalarioNeto = new Label("Salario neto obtenido: " + resultado);
                salarioNetoContenedor.removeAllComponents();
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            }catch (BRException brException){
                Label labelSalarioNeto = new Label("Error: " + brException.getMessage());
                salarioNetoContenedor.removeAllComponents();
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            }

        });

        salarioBruto.addComponents(tipoEmpleadoComboBox, ventaMes, horasExtra);
        salarioBrutoContenedor.addComponents(salarioBruto, botonSalarioBruto);

        salarioNeto.addComponents(inSalarioBruto);
        salarioNetoContenedor.addComponents(salarioNeto, botonSalarioNeto);

        TabSheet tabs = new TabSheet();
        tabs.addTab(salarioBrutoContenedor, "Calcula salario Bruto");
        tabs.addTab(salarioNetoContenedor, "Calcula salario Neto");
        
        layout.addComponents(tabs);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
