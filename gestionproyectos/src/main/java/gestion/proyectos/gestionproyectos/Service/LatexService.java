package gestion.proyectos.gestionproyectos.Service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;

@Service
public class LatexService {
    private static final Logger logger = LoggerFactory.getLogger(LatexService.class);

    @Value("${latex.output.path}")
    private String outputPath;

    public byte[] generateDocument(String templatePath, Map<String, String> data) throws IOException {
        logger.info("Generating document from template: {}", templatePath);

        // Verificar que el template existe
        Path templateFilePath = Paths.get(templatePath).normalize();
        if (!Files.exists(templateFilePath)) {
            logger.error("Template file not found: {}", templateFilePath);
            throw new FileNotFoundException("Template not found: " + templateFilePath);
        }

        // Leer y poblar la plantilla
        String template = Files.readString(templateFilePath);
        String document = replaceTemplateVariables(template, data);

        // Generar PDF
        Path tempDirPath = createTempDirectory();
        try {
            // Escribir el contenido del documento en el directorio temporal
            Path texFile = tempDirPath.resolve("document.tex");
            Files.write(texFile, document.getBytes(StandardCharsets.UTF_8));

            // Generar PDF
            Path pdfPath = generatePDF(texFile);
            if (Files.exists(pdfPath)) {
                return Files.readAllBytes(pdfPath);
            } else {
                throw new IOException("PDF file was not generated");
            }
        } finally {
            try {
                FileUtils.deleteDirectory(tempDirPath.toFile());
            } catch (IOException e) {
                logger.warn("Error deleting temporary directory: {}", tempDirPath, e);
            }
        }
    }

    private boolean isTableFormat(String content) {
        // Si el contenido es nulo o está vacío, no es tabla
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        // Verificar simplemente si contiene al menos un '&'
        return content.contains("&");
    }

    private String replaceTemplateVariables(String template, Map<String, String> data) {
        if (data == null) return template;

        String result = template;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String value = entry.getValue() != null ? entry.getValue() : "";

            // Verificamos si el contenido es parte del EDT (contiene '#')
            if (value.contains("{") && value.contains("}")) {
                value = processEDTContent(value);
            }
            // Si detectamos al menos un '&', procesamos como tabla
            else if (isTableFormat(value)) {
                value = processTableContent(value);
            } else {
                // De lo contrario, se escapan caracteres especiales de LaTeX
                value = escapeLatexSpecialChars(value);
            }

            result = result
                    .replace("${" + entry.getKey() + "}", value)
                    .replace("{{" + entry.getKey() + "}}", value);
        }
        return result;
    }

    /**
     * Procesa el contenido EDT/WBS dado el formato:
     * #Fase,##Tarea,##Tarea;Descripción#OtraFase,##Tarea ...
     */
    private String processEDTContent(String edtContent) {
        StringBuilder forest = new StringBuilder();
        forest.append("\\begin{forest}\n")
                .append("for tree={\n")
                .append("    grow=east,\n")
                .append("    draw,\n")
                .append("    rounded corners,\n")
                .append("    node options={align=center},\n")
                .append("    edge={-latex},\n")
                .append("    l sep+=10pt,\n")
                .append("    s sep+=10pt\n")
                .append("}\n")
                .append("[Proyecto\n");  // Nodo raíz

        // Dividir el contenido en fases usando el separador |
        String[] phases = edtContent.split("\\|");

        for (String phase : phases) {
            phase = phase.trim();
            if (phase.isEmpty()) continue;

            // Extraer el nombre de la fase y sus tareas
            // El formato es: NombreFase{Tarea1,Tarea2,Tarea3}
            int openBraceIndex = phase.indexOf("{");
            int closeBraceIndex = phase.lastIndexOf("}");

            if (openBraceIndex == -1 || closeBraceIndex == -1) continue;

            // Obtener el nombre de la fase
            String phaseName = phase.substring(0, openBraceIndex).trim();
            if (phaseName.isEmpty()) continue;

            // Abrir el nodo de la fase
            forest.append("    [").append(escapeLatexSpecialChars(phaseName)).append("\n");

            // Obtener y procesar las tareas
            String tasksContent = phase.substring(openBraceIndex + 1, closeBraceIndex);
            String[] tasks = tasksContent.split(",");

            for (String task : tasks) {
                task = task.trim();
                if (task.isEmpty()) continue;

                // Verificar si la tarea tiene descripción
                if (task.contains(";")) {
                    String[] taskParts = task.split(";", 2);
                    String taskName = taskParts[0].trim();
                    String taskDesc = taskParts[1].trim();

                    forest.append("        [")
                            .append(escapeLatexSpecialChars(taskName))
                            .append("\\\\")  // Salto de línea en LaTeX
                            .append(escapeLatexSpecialChars(taskDesc))
                            .append("]\n");
                } else {
                    forest.append("        [")
                            .append(escapeLatexSpecialChars(task))
                            .append("]\n");
                }
            }

            // Cerrar el nodo de la fase
            forest.append("    ]\n");
        }

        // Cerrar el nodo raíz y el entorno forest
        forest.append("]\n")
                .append("\\end{forest}\n");

        return forest.toString();
    }



    private String processTableContent(String content) {
        // Si no es formato de tabla, retornar el texto escapado
        if (!isTableFormat(content)) {
            return escapeLatexSpecialChars(content);
        }

        // Remover los & del inicio y final (en caso de que los uses así)
        content = content.substring(1, content.length() - 1);

        StringBuilder tableContent = new StringBuilder();
        String[] rows = content.split("&");

        // Determinar el número de columnas basado en la primera fila
        String[] firstRow = rows[0].split(",");
        int numColumns = firstRow.length - 1; // -1 porque el primer elemento suele ser un índice

        // Crear el formato de la tabla
        tableContent.append("\\begin{center}\n");
        tableContent.append("\\begin{tabularx}{\\textwidth}{|c|");
        for (int i = 0; i < numColumns; i++) {
            tableContent.append("X|");
        }
        tableContent.append("}\n\\hline\n");

        // Procesar las filas
        for (String row : rows) {
            if (row.trim().isEmpty()) continue;

            String[] columns = row.split(",");
            if (columns.length > 1) {
                // Añadir el número/índice
                tableContent.append(columns[0].trim());

                // Añadir el resto de columnas
                for (int i = 1; i < columns.length; i++) {
                    tableContent.append(" & ")
                            .append(escapeLatexSpecialChars(columns[i].trim()));
                }
                tableContent.append(" \\\\ \\hline\n");
            }
        }

        tableContent.append("\\end{tabularx}\n");
        tableContent.append("\\end{center}\n");
        return tableContent.toString();
    }

    private String escapeLatexSpecialChars(String input) {
        if (input == null) return "";
        return input
                .replace("\\", "\\textbackslash{}")
                .replace("~", "\\textasciitilde{}")
                .replace("^", "\\textasciicircum{}")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(">", "\\textgreater{}")
                .replace("<", "\\textless{}")
                .replace("&", "\\&");
    }

    private Path createTempDirectory() throws IOException {
        Path outputDir = Paths.get(outputPath).normalize();
        Files.createDirectories(outputDir);
        return Files.createTempDirectory(outputDir, "latex_");
    }

    private Path generatePDF(Path texFile) throws IOException {
        Path tempDirPath = texFile.getParent();

        // Ejecutar pdflatex dos veces para referencias cruzadas
        for (int i = 0; i < 2; i++) {
            ProcessBuilder pb = new ProcessBuilder(
                    "pdflatex",
                    "-interaction=nonstopmode",
                    "-halt-on-error",
                    texFile.getFileName().toString()
            );
            pb.directory(tempDirPath.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    logger.debug("pdflatex output: {}", line);
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    logger.error("pdflatex failed with exit code: {}. Output:\n{}", exitCode, output);
                    throw new IOException("pdflatex failed with exit code: " + exitCode);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("PDF generation process interrupted", e);
            }
        }

        Path pdfFile = tempDirPath.resolve(texFile.getFileName().toString().replace(".tex", ".pdf"));
        if (!Files.exists(pdfFile)) {
            throw new IOException("PDF file was not generated");
        }
        return pdfFile;
    }

    private String cleanLatexText(String input) {
        if (input == null) return "";
        return input
                .replace("\\", "\\textbackslash{}")
                .replace("$", "\\$")
                .replace("%", "\\%")
                .replace("&", "\\&")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("~", "\\textasciitilde{}")
                .replace("^", "\\textasciicircum{}")
                .replace(">", "\\textgreater{}")
                .replace("<", "\\textless{}")
                .trim();
    }
}
