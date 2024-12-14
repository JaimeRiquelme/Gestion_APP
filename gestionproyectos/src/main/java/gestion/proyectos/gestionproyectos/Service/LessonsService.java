package gestion.proyectos.gestionproyectos.Service;

import gestion.proyectos.gestionproyectos.Entity.Lessons;
import gestion.proyectos.gestionproyectos.Repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonsService {

    @Autowired
    private LessonsRepository lessonsRepository;

    public Lessons saveLessons(Lessons lessons) {
        return lessonsRepository.save(lessons);
    }

    public Lessons getLessonsById(Long id) {
        return lessonsRepository.findById(id).orElse(null);
    }

    public void deleteLessons(Long id) {
        lessonsRepository.deleteById(id);
    }

    public Lessons updateLessons(Lessons lessons) {
        return lessonsRepository.save(lessons);
    }


}
