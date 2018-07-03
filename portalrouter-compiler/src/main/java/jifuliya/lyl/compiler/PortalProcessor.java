package jifuliya.lyl.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import jifuliya.lyl.annotation.PortalAction;
import jifuliya.lyl.annotation.PortalRouter;

@AutoService(Processor.class)
public class PortalProcessor extends AbstractProcessor {

    private static final String ACTIVITY_FULL_NAME = "android.app.Activity";
    private static final String FRAGMENT_FULL_NAME = "android.app.Fragment";
    private static final String FRAGMENT_V4_FULL_NAME = "android.support.v4.app.Fragment";

    private Messager mMessager;
    private Elements mElementUtils;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(PortalRouter.class);
        Set<? extends Element> actionElements = roundEnvironment.getElementsAnnotatedWith(PortalAction.class);

        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null... <<<");
            return true;
        }

        // Map<String, Class<?>> map
        MethodSpec.Builder register = generateRegistMethod(elements);
        if (actionElements == null || actionElements.isEmpty()) {
            generate(generateInitializeMethodWithNoAction(), register);
        } else {
            generate(generateInitializeMethod(), register);
        }
        return true;
    }

    private MethodSpec.Builder generateInitializeMethod() {

        ClassName portalRouterCls = ClassName.get("jifuliya.lyl.portalrouter", "PortalRoutor");
        ClassName portalActionCls = ClassName.get(Constant.BASE_URL, Constant.PORTAL_ACTION);

        MethodSpec.Builder mCreateMethodSpecBuilder = MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addStatement("$T router = $T.initialize()", portalRouterCls, portalRouterCls)
                .addStatement("regist(router.getClsMap())")
                .addStatement("$T.regist(router.getActionMap())", portalActionCls);

        return mCreateMethodSpecBuilder;
    }

    private MethodSpec.Builder generateInitializeMethodWithNoAction() {

        ClassName portalRouterCls = ClassName.get("jifuliya.lyl.portalrouter", "PortalRoutor");

        MethodSpec.Builder mCreateMethodSpecBuilder = MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addStatement("$T router = $T.initialize()", portalRouterCls, portalRouterCls)
                .addStatement("regist(router.getClsMap())");

        return mCreateMethodSpecBuilder;
    }

    private MethodSpec.Builder generateRegistMethod(Set<? extends Element> elements) {
        // Map<String, Class<?>> map
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(HashMap.class),
                ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(Object.class)));
        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();

        MethodSpec.Builder mCreateMethodSpecBuilder = MethodSpec.methodBuilder("regist")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(mapParameterSpec)
                .returns(TypeName.VOID);

        Map<String, String> checkRepeatForAction = new HashMap<>();

        for (Element annotatedElement : elements) {

            //check is activity with tag interface----@PortalRouter
            if (annotatedElement.getKind().isClass() && validateClass((TypeElement) annotatedElement)) {

                //get package name
                PackageElement packageElement = mElementUtils.getPackageOf(annotatedElement);
                String pkName = packageElement.getQualifiedName().toString();

                info(String.format("package = %s", pkName));

                // get action and clsName
                String clsName = annotatedElement.getSimpleName().toString();
                String action = annotatedElement.getAnnotation(PortalRouter.class).url();

                if (checkRepeatForAction.containsKey(action)) {
                    throw new RuntimeException();
                }

                mCreateMethodSpecBuilder.addStatement("map.put($S, $T.class)", action, ClassName.get(pkName, clsName));
                checkRepeatForAction.put(action, clsName);
            }
        }
        return mCreateMethodSpecBuilder;
    }

    private void generate(MethodSpec.Builder method1, MethodSpec.Builder method2) {

        TypeSpec bind_activity = TypeSpec.classBuilder(Constant.PORTAL_ADAPTER)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(method1.build())
                .addMethod(method2.build())
                .build();


        JavaFile javaFile = JavaFile.builder(Constant.BASE_URL, bind_activity)
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateClass(TypeElement typeElement) {
        if (!isSubtype(typeElement, ACTIVITY_FULL_NAME) && !isSubtype(typeElement, FRAGMENT_V4_FULL_NAME)
                && !isSubtype(typeElement, FRAGMENT_FULL_NAME)) {
            return false;
        }
        Set<Modifier> modifiers = typeElement.getModifiers();
        // abstract class.
        if (modifiers.contains(Modifier.ABSTRACT)) {
            return false;
        }
        return true;
    }

    private boolean isSubtype(Element typeElement, String type) {
        return processingEnv.getTypeUtils().isSubtype(typeElement.asType(),
                processingEnv.getElementUtils().getTypeElement(type).asType());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(PortalRouter.class.getCanonicalName());
        annotations.add(PortalAction.class.getCanonicalName());
        return annotations;
    }


    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }
}
