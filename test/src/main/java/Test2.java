import com.google.common.annotations.VisibleForTesting;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import jdk.nashorn.internal.objects.annotations.Property;
import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;

import javax.annotation.Generated;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test2 <T extends Test2.C & Serializable & @Test2.AA("Comparable<T>") Comparable<T>> {

    private T[] ts;

    public static void main(String[] args) {

        TypeVariable<Class<Test2>>[] typeParameters = Test2.class.getTypeParameters();
        for (TypeVariable<Class<Test2>> p : typeParameters) {
            System.out.println("p: " + p);
            AnnotatedType[] annotatedBounds = p.getAnnotatedBounds();
            for (AnnotatedType annotatedBound : annotatedBounds) {
                Type type = annotatedBound.getType();
                System.out.println("annote: "+ type.getTypeName());
                Annotation[] annotations = annotatedBound.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    System.out.println("antype: " +annotation.annotationType());
                }
            }
            Type[] bounds = p.getBounds();
            for (Type bound : bounds) {
                System.out.println("type: " + bound.getTypeName());
                TypeVariable<? extends Class<? extends Type>>[] typeParameters1 = bound.getClass().getTypeParameters();

            }

        }

        String[][] ss= new String[0][];

        Object[] signers = ss.getClass().getSigners();
        for (Object signer : signers) {
            System.out.println(signer);
        }

        int innerClassPrivateField = new C().innerClassPrivateField;

        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
//        reflectionFactory.newConstructorAccessor()  ;
//        reflectionFactory.ne

    }

    @Target(ElementType.TYPE_USE)
    @interface AA {
        String value() default "";
    }

    public static class C {
        private int innerClassPrivateField =0;
        static {

        }
    }
}
