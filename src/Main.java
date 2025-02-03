import java.lang.annotation.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Annotation[] annos = Main.class.getDeclaredMethod("foo").getDeclaredAnnotations();
        for (Annotation a : annos) {
            System.out.println(a);
        }

        MyAnnoContainer con = Main.class.getDeclaredMethod("foo").getDeclaredAnnotation(MyAnnoContainer.class);
        System.out.println(con);
    }

    @MyAnnoContainer(
            value = {
                    @MyAnno(name = "Anno 1"),
                    @MyAnno(name = "Anno 2")
            },
            description = "The coolest"
    )
    @MyAnno(name = "Anno 3")
    static void foo() {

    }
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MyAnnoContainer.class)
@interface MyAnno {
    String name();
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnoContainer {
    MyAnno[] value();

    String description() default "";
}