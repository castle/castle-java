package io.castle.client.utils;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class SeparateClassloaderTestRunner extends BlockJUnit4ClassRunner {

    public SeparateClassloaderTestRunner(Class<?> clazz) throws InitializationError {
        super(getFromTestClassloader(clazz));
    }

    private static Class<?> getFromTestClassloader(Class<?> clazz) throws InitializationError {
        try {
            ClassLoader testClassLoader = new TestClassLoader();
            return Class.forName(clazz.getName(), true, testClassLoader);
        } catch (ClassNotFoundException e) {
            throw new InitializationError(e);
        }
    }

    public static class TestClassLoader extends URLClassLoader {
        public TestClassLoader() {
            super(classpathUrls());
        }

        // Derive the classpath from the java.class.path system property rather
        // than casting the system class loader to URLClassLoader, which fails
        // on JDK 9+ where the application class loader is no longer a
        // URLClassLoader.
        private static URL[] classpathUrls() {
            String classpath = System.getProperty("java.class.path");
            List<URL> urls = new ArrayList<URL>();
            for (String entry : classpath.split(File.pathSeparator)) {
                if (entry.isEmpty()) {
                    continue;
                }
                try {
                    urls.add(new File(entry).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new IllegalStateException("Unable to build test classpath URL for " + entry, e);
                }
            }
            return urls.toArray(new URL[0]);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name.startsWith("io.castle.")) {
                return super.findClass(name);
            }
            return super.loadClass(name);
        }
    }
}