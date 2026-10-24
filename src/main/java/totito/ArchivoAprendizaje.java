package totito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchivoAprendizaje {
    public void guardar(AgenteIA ia, Path archivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Map.Entry<String, double[]> entrada : ia.getTabla().entrySet()) {
            StringBuilder linea = new StringBuilder(entrada.getKey());
            for (double valor : entrada.getValue()) linea.append(';').append(valor);
            lineas.add(linea.toString());
        }
        Path padre = archivo.toAbsolutePath().getParent();
        if (padre != null) Files.createDirectories(padre);
        Files.write(archivo, lineas, StandardCharsets.UTF_8);
    }

    public void cargar(AgenteIA ia, Path archivo) throws IOException {
        if (!Files.exists(archivo)) return;
        HashMap<String, double[]> datos = new HashMap<>();
        for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
            String[] campos = linea.split(";", -1);
            if (campos.length != 10 || !campos[0].matches("[XO-]{9}")) {
                throw new IOException("El archivo de aprendizaje tiene un formato incorrecto.");
            }
            double[] valores = new double[9];
            try {
                for (int i = 0; i < 9; i++) {
                    valores[i] = Double.parseDouble(campos[i + 1]);
                    if (!Double.isFinite(valores[i])) throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new IOException("El archivo contiene valores incorrectos.", e);
            }
            datos.put(campos[0], valores);
        }
        ia.getTabla().clear();
        ia.getTabla().putAll(datos);
    }
}
