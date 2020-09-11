package nl.meine.master.testsuite;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class InlineCompiler {


    File sourceFile;
    File classFile;

    String processFunction(String functionBody){
        if(!functionBody.startsWith("public")){
            functionBody = "public " + functionBody;
        }
        return functionBody;
    }

    public Object init(String functionBody) throws Exception {
        StringBuilder sb = new StringBuilder(64);
        sb.append("package nl.meine.master.testsuite;\n");
        sb.append("public class SubmittedFunction  {\n");
        sb.append(processFunction(functionBody));
        sb.append("}\n");
        Object returnVal = null;
        File basePath = new File("nl/meine/master/testsuite");
        sourceFile = new File(basePath, "SubmittedFunction.java");
        classFile = new File(basePath, "SubmittedFunction.class");

        /** Compilation Requirements *********************************************************************************************/
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        if (sourceFile.getParentFile().exists() || sourceFile.getParentFile().mkdirs()) {

            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(sourceFile);
                    writer.write(sb.toString());
                    writer.flush();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }

                // This sets up the class path that the compiler will use.
                // I've added the .jar file that contains the DoStuff interface within in it...
                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + File.pathSeparator + "dist/InlineCompiler.jar");

                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                /********************************************************************************************* Compilation Requirements **/
                if (task.call()) {
                    // compilation succeeded. Execution comes later
                  //  returnVal = execute(functionName, params, paramTypes);
                    int a = 0;
                } else {
                    String message = "";
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        message += String.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                    throw new UncompilableException("Function not compilable: " + message);
                }
            }  finally {
                fileManager.close();
            }
        }
        return returnVal;
    }


    public Object execute(String functionName, Object[] params, Class... paramTypes) throws
            Exception {

        Object returnVal = null;
        /** Load and execute *************************************************************************************************/
        // Create a new custom class loader, pointing to the directory that contains the compiled
        // classes, this should point to the top of the package structure!
        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
        // Load the class from the classloader by name....
        Class<?> loadedClass = classLoader.loadClass("nl.meine.master.testsuite.SubmittedFunction");
        // Create a new instance...
        Object obj = loadedClass.newInstance();

        Method loadedMethod = obj.getClass().getMethod(functionName, paramTypes);

        returnVal = loadedMethod.invoke(obj, params);
        /************************************************************************************************* Load and execute **/

        return returnVal;
    }

    public void tearDown(){
        if(sourceFile != null){
            sourceFile.delete();
        }
        if(classFile != null){
            classFile.delete();
        }
    }

}