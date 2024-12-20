package gestion.proyectos.gestionproyectos.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LatexService {

    private static final Logger logger = LoggerFactory.getLogger(LatexService.class);

    @Value("${latex.output.path}")
    private String outputPath;

    public byte[] generateDocument(String templatePath, Map<String, String> data) throws IOException {
        createDirectories();
        String processId = UUID.randomUUID().toString();

        try {
            Path fullTemplatePath = Paths.get(templatePath);
            logger.info("Buscando plantilla en: {}", fullTemplatePath);

            if (!Files.exists(fullTemplatePath)) {
                throw new IOException("Template file not found: " + fullTemplatePath);
            }

            String template = new String(Files.readAllBytes(fullTemplatePath));
            String documento = processTemplate(template, data);
            String tempFilePath = saveTempFile(documento, processId);
            String pdfPath = compileLaTeX(tempFilePath);
            byte[] pdfContent = Files.readAllBytes(Paths.get(pdfPath));

            cleanupTempFiles(processId);
            return pdfContent;

        } catch (Exception e) {
            logger.error("Error generating document: ", e);
            cleanupTempFiles(processId);
            throw new IOException("Error generating LaTeX document: " + e.getMessage(), e);
        }
    }

    private String processTemplate(String template, Map<String, String> data) {
        String result = template;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String value = entry.getValue();
            String processedValue;

            if (value != null && value.contains("&")) {
                processedValue = convertToLatexTable(value, entry.getKey());
            } else {
                processedValue = escapeLatex(value);
            }

            result = result.replace("{{" + entry.getKey() + "}}", processedValue);
        }
        return result;
    }

    private String convertToLatexTable(String tableData, String tableId) {
        List<String[]> rows = parseTableData(tableData);
        if (rows.isEmpty()) return "";

        StringBuilder latex = new StringBuilder();
        String[] headers = rows.get(0); // Primera fila como encabezados
        int columnCount = headers.length;

        // Iniciar tabla
        latex.append("\\begin{center}\n");

        // Definir ancho de columnas basado en el contenido
        latex.append("\\begin{tabularx}{\\textwidth}{|");
        for (int i = 0; i < columnCount; i++) {
            latex.append("X|");
        }
        latex.append("}\n\\hline\n");

        // Añadir encabezados con color de fondo
        for (int i = 0; i < headers.length; i++) {
            latex.append("\\cellcolor{gray!40}\\textbf{")
                    .append(escapeLatex(headers[i].trim()))
                    .append("}");

            if (i < headers.length - 1) {
                latex.append(" & ");
            }
        }
        latex.append(" \\\\ \\hline\n");

        // Añadir filas de datos
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            for (int j = 0; j < headers.length; j++) {
                if (j < row.length) {
                    latex.append(escapeLatex(row[j].trim()));
                } else {
                    latex.append(""); // Celda vacía si faltan datos
                }

                if (j < headers.length - 1) {
                    latex.append(" & ");
                }
            }
            latex.append(" \\\\ \\hline\n");
        }

        // Cerrar tabla
        latex.append("\\end{tabularx}\n")
                .append("\\end{center}\n")
                .append("\\vspace{2mm}\n");

        return latex.toString();
    }

    private List<String[]> parseTableData(String tableData) {
        List<String[]> rows = new ArrayList<>();
        if (tableData == null || tableData.trim().isEmpty()) {
            return rows;
        }

        String[] rowsData = tableData.split("&");
        for (String row : rowsData) {
            row = row.trim();
            if (!row.isEmpty()) {
                // Dividir por coma y limpiar espacios en blanco
                String[] columns = row.split(",");
                // Limpiar cada columna
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = columns[i].trim();
                }
                rows.add(columns);
            }
        }

        return rows;
    }

    private String escapeLatex(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%")
                .replace("&", "\\&")
                .replace("#", "\\#")
                .replace("$", "\\$")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("^", "\\^")
                .replace("~", "\\~")
                .replace("\n", "\\\\");
    }

    private void createDirectories() throws IOException {
        try {
            Files.createDirectories(Paths.get(outputPath));
            logger.info("Directorios creados/verificados: {}", outputPath);
        } catch (Exception e) {
            logger.error("Error creating directories: ", e);
            throw e;
        }
    }

    private String saveTempFile(String content, String processId) throws IOException {
        String texFilePath = Paths.get(outputPath, processId + ".tex").toString();
        Files.write(Paths.get(texFilePath), content.getBytes());
        logger.info("Archivo temporal guardado en: {}", texFilePath);
        return texFilePath;
    }

    private String compileLaTeX(String texFilePath) throws IOException, InterruptedException {
        File texFile = new File(texFilePath);
        File workingDir = texFile.getParentFile();

        logger.info("Compilando LaTeX en directorio: {}", workingDir);
        executeLatexCommand(workingDir, texFile.getName());
        executeLatexCommand(workingDir, texFile.getName());

        String pdfPath = texFilePath.replace(".tex", ".pdf");
        if (!new File(pdfPath).exists()) {
            throw new IOException("PDF file was not generated");
        }

        return pdfPath;
    }

    private void executeLatexCommand(File workingDir, String texFileName) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "pdflatex",
                "-interaction=nonstopmode",
                "-halt-on-error",
                texFileName
        );
        pb.directory(workingDir);

        File logFile = new File(workingDir, texFileName.replace(".tex", ".log"));
        pb.redirectError(logFile);
        pb.redirectOutput(logFile);

        logger.info("Ejecutando comando pdflatex para: {}", texFileName);
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String errorLog = new String(Files.readAllBytes(logFile.toPath()));
            logger.error("Error en compilación LaTeX: {}", errorLog);
            throw new IOException("LaTeX compilation failed. Check log for details: " + errorLog);
        }
    }

    private void cleanupTempFiles(String processId) {
        try {
            String[] extensions = {".tex", ".aux", ".log", ".out"};
            for (String ext : extensions) {
                Files.deleteIfExists(Paths.get(outputPath, processId + ext));
            }
            logger.info("Archivos temporales limpiados para processId: {}", processId);
        } catch (IOException e) {
            logger.warn("Error cleaning up temp files: ", e);
        }
    }
}