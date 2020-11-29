package ir.ui.golestan.mamadreza;

public class CourseNotFoundException extends RuntimeException{

    CourseNotFoundException(int id) {
        super("Could not find course " + id);
    }
}
