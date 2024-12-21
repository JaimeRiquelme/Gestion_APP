package gestion.proyectos.gestionproyectos.Service;

import java.io.IOException;
import java.util.Map;

public interface DocumentService {
    /**
     * Genera un documento a partir de los datos proporcionados
     * @param data Mapa de datos necesarios para generar el documento
     * @return Array de bytes del documento generado
     */
    byte[] generateDocument(Map<String, String> data, Long idExit) throws IOException;

    /**
     * Valida que los datos necesarios para generar el documento estén presentes
     * @param data Mapa de datos a validar
     */
    void validateData(Map<String, String> data);
}