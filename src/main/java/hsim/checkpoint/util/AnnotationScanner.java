package hsim.checkpoint.util;

import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.util.excel.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The type Annotation scanner.
 */
@Slf4j
public class AnnotationScanner {

    private List<Class<?>> allClass = null;

    /**
     * Instantiates a new Annotation scanner.
     */
    public AnnotationScanner() {
        File rootDir = this.getClassRootDirectory();

        if (rootDir != null) {
            this.allClass = getDirClassList(rootDir, null);
        } else {
            this.initJar();
        }
    }

    private void initJar() {
        File file = new File("./");

        log.info("file exist : " + file.exists());
        this.allClass = new ArrayList<>();
        if (file.isDirectory()) {
            Arrays.stream(file.list()).filter(f -> f.endsWith(".jar")).forEach(f -> {
                this.allClass.addAll(this.getJarClassList(this.getJarFile(f)));
            });
        }
    }

    private JarFile getJarFile(String jarPath) {

        log.info("jar Path :  " + jarPath);

        if (jarPath != null) {
            try {
                return new JarFile(jarPath);
            } catch (IOException e) {
                log.info("jarFile exception : " + e.getMessage());
            }
        }

        return null;
    }

    private File getClassRootDirectory() {
        URL root = ClassLoader.getSystemClassLoader().getResource("");
        if (root == null) {
            return null;
        }
        return new File(root.getFile());
    }

    private String fromFileToClassName(final String fileName) {
        String className = fileName.substring(0, fileName.length() - 6).replaceAll("/|\\\\", "\\.");
        if (className.indexOf("classes.") >= 0) {
            return className.substring(className.indexOf("classes.") + 8);
        }
        return className;
    }

    private List<Class<?>> getJarClassList(JarFile jarFile) {
        List<Class<?>> classList = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = fromFileToClassName(entry.getName());
                log.info(className);

                try {
                    if (TypeCheckUtil.isNotScanClass(className)) {
                        continue;
                    }
                    Class<?> cla = Class.forName(className);
                    classList.add(cla);
                } catch (Exception e) {
                    log.debug("class not found : " + className);
                }
            }
        }
        return classList;
    }

    private List<Class<?>> getDirClassList(File dir, String parent) {
        List<Class<?>> classList = new ArrayList<>();
        if (dir.exists()) {
            Arrays.stream(dir.list()).forEach(f -> {
                File file = new File(dir.getPath() + "/" + f);
                if (file.isDirectory()) {
                    classList.addAll(getDirClassList(file, parent == null ? file.getName() : parent + "/" + file.getName()));
                } else {
                    String filePath = parent + "/" + file.getName();
                    try {
                        String className = this.fromFileToClassName(filePath);
                        if (!TypeCheckUtil.isNotScanClass(className)) {
                            classList.add(Class.forName(className));
                        }
                    } catch (ClassNotFoundException e) {
                        log.info("class not found : " + filePath);
                    }
                }
            });
        }
        return classList;
    }


    /**
     * Gets parameter from method with annotation.
     *
     * @param parentClass     the parent class
     * @param method          the method
     * @param annotationClass the annotation class
     * @return the parameter from method with annotation
     */
    public List<DetailParam> getParameterFromMethodWithAnnotation(Class<?> parentClass, Method method, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        if (method.getParameterCount() < 1) {
            return params;
        }

        for (Parameter param : method.getParameters()) {

            Annotation[] annotations = param.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass)) {
                    params.add(new DetailParam(param.getType(), method, parentClass));
                    break;
                }
            }
        }
        return params;
    }

    /**
     * Gets parameter from class with annotation.
     *
     * @param baseClass       the base class
     * @param annotationClass the annotation class
     * @return the parameter from class with annotation
     */
    public List<DetailParam> getParameterFromClassWithAnnotation(Class<?> baseClass, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        Arrays.stream(baseClass.getDeclaredMethods()).forEach(method -> params.addAll(this.getParameterFromMethodWithAnnotation(baseClass, method, annotationClass)));
        return params;
    }

    /**
     * Gets parameter with annotation.
     *
     * @param annotation the annotation
     * @return the parameter with annotation
     */
    public List<DetailParam> getParameterWithAnnotation(Class<?> annotation) {
        List<DetailParam> params = new ArrayList<>();
        this.allClass.stream().forEach(cla -> params.addAll(this.getParameterFromClassWithAnnotation(cla, annotation)));
        return params;

    }
}
