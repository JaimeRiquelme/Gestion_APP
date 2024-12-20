package gestion.proyectos.gestionproyectos.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TemplatePathResolver {

    private static final Logger logger = LoggerFactory.getLogger(TemplatePathResolver.class);

    @Value("${latex.template.path}")
    private String templatePath;

    public String resolve(String templateName) {
        // Construir la ruta completa usando Paths
        Path resolvedPath = Paths.get(templatePath).resolve(templateName);
        logger.info("Ruta de template resuelta: {}", resolvedPath);
        return resolvedPath.toString();
    }
}