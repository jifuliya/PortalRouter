package jifuliya.lyl.annotationprocess;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class PortalGenerator {

    private Elements mElementUtils;
    private Element annotatedElement;


    public PortalGenerator(Element annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    /**
     * 获取类名字字符串
     *
     * @param typeElement
     * @param myPackage
     * @return
     */
    private String getClassString(TypeElement typeElement, String myPackage) {
        PackageElement packageElement = getPackageElement(typeElement);
        String packageString = packageElement.getQualifiedName().toString();
        String className = typeElement.getQualifiedName().toString();
        if (packageString != null && !packageString.isEmpty()) {
            if (packageString.equals(myPackage)) {
                className = cutPackage(myPackage, className);
            } else if (packageString.equals("java.lang")) {
                className = typeElement.getSimpleName().toString();
            }
        }
        return className;
    }

    private String cutPackage(String paket, String className) {
        if (className.startsWith(paket + '.')) {
            // Don't use TypeElement.getSimpleName, it doesn't work for us with inner classes
            return className.substring(paket.length() + 1);
        } else {
            // Paranoia
            throw new IllegalStateException("Mismatching " + paket + " vs. " + className);
        }
    }

    /**
     * 获取包
     *
     * @param subscriberClass
     * @return
     */
    private PackageElement getPackageElement(TypeElement subscriberClass) {
        Element candidate = subscriberClass.getEnclosingElement();
        while (!(candidate instanceof PackageElement)) {
            candidate = candidate.getEnclosingElement();
        }
        return (PackageElement) candidate;
    }

    /**
     * 根据类的全名获取类名
     *
     * @param clazz
     */
    private String getSimpleNameForClassPath(String clazz) {
        int pos = clazz.lastIndexOf('.');
        return clazz.substring(pos + 1);
    }

    static class SubscribeInfo {
        ClassName bindingClassName;
        TypeName targetType;
        ClassName eventClassName;
        String packageName;
        String methodName;

        public SubscribeInfo(ClassName bindingClassName, TypeName targetType,
                             ClassName eventClassName, String packageName, String methodName) {
            this.bindingClassName = bindingClassName;
            this.targetType = targetType;
            this.eventClassName = eventClassName;
            this.packageName = packageName;
            this.methodName = methodName;
        }
    }

}
