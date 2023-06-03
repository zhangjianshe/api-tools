package cn.mapway.document.parser.source;

import cn.mapway.document.annotation.Doc;
import cn.mapway.document.module.ApiDoc;
import cn.mapway.document.module.ClassSummary;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * SourceParser
 * 利用 JavaParser  解析源码
 *
 * @author zhangjianshe <zhangjianshe@gmail.com>
 */
public class SourceParser {
    public static void main(String[] args) {
        SourceParser app = new SourceParser();
        app.run();

    }

    private void run() {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        String sourcePath = "D:\\dev\\ai-server\\ai-server-web\\src\\main\\java\\cn\\imagebot\\aiserver";
        combinedTypeSolver.add(new JavaParserTypeSolver(new File(sourcePath)));

        List<String> jarFiles = Lang.list(
                "C:\\Users\\zhang\\.m2\\repository\\org\\springframework\\spring-web\\5.2.15.RELEASE\\spring-web-5.2.15.RELEASE.jar"
                , "C:\\Users\\zhang\\.m2\\repository\\cn\\mapway\\api-tools-doc\\3.20.0\\api-tools-doc-3.20.0.jar"
                , "C:\\Users\\zhang\\.m2\\repository\\org\\projectlombok\\lombok\\1.18.20\\lombok-1.18.20.jar"
                , "C:\\Users\\zhang\\.m2\\repository\\org\\springframework\\spring-context\\5.2.15.RELEASE\\spring-context-5.2.15.RELEASE.jar"
        );
        jarFiles.forEach(path -> {
            try {
                JarTypeSolver jarTypeSolver = new JarTypeSolver(new File(path));
                combinedTypeSolver.add(jarTypeSolver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(javaSymbolSolver);

        ApiDoc apiDoc = new ApiDoc();
        File[] files = Files.lsFile(sourcePath + "/api", "(\\.java)$");
        for (File f : files) {
            try {
                CompilationUnit compilationUnit = StaticJavaParser.parse(f);
                System.out.println("file " + f.getAbsolutePath());
                System.out.println(Strings.dup("-", f.getAbsolutePath().length() + 5));
                Optional<TypeDeclaration<?>> primaryType = compilationUnit.getPrimaryType();
                primaryType.ifPresent(t -> {
                    if (t.isClassOrInterfaceDeclaration()) {
                        ClassOrInterfaceDeclaration declaration = t.asClassOrInterfaceDeclaration();
                        NodeList<AnnotationExpr> annotations = declaration.getAnnotations();
                        if ((hasController(annotations, RestController.class)
                                || hasController(annotations, Controller.class)
                        ) && hasController(annotations, Doc.class)
                        ) {
                            //这个类是一个Spring的控制器类 并且 加入了注解Doc
                            processController(apiDoc, declaration);
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                System.out.println("file not fount" + e.getMessage());
            }
        }
    }

    /**
     * 处理API控制器
     *
     * @param apiDoc
     * @param declaration
     */
    private void processController(ApiDoc apiDoc, ClassOrInterfaceDeclaration declaration) {
        boolean isRestController = hasController(declaration.getAnnotations(), RestController.class);

        ClassSummary classSummary;
        if (isRestController) {
            classSummary = parseClassSummaryWithRestController(declaration);
        }
    }

    /**
     * 解析ClassSummary
     *
     * @param declaration
     * @return
     */
    private ClassSummary parseClassSummaryWithRestController(ClassOrInterfaceDeclaration declaration) {
        ClassSummary summary = new ClassSummary();
        summary.setTitle(declaration.getNameAsString());
        summary.setAuthor("");
        summary.setClassName(declaration.getNameAsString());
        summary.setSummary("");
        summary.setBasePath("");
        summary.setGroup("");

        NodeList<AnnotationExpr> annotations = declaration.getAnnotations();

        AnnotationExpr doc = findAnnotation(annotations, Doc.class);
        AnnotationExpr rest = findAnnotation(annotations, RestController.class);
        if (doc != null) {

            String value = getAnnotationProperty(doc, "value");
            if (Strings.isNotBlank(value)) {
                summary.setTitle(value);
            }
            String group = getAnnotationProperty(doc, "group");
            if (Strings.isNotBlank(group)) {
                summary.setGroup(group);
            }
            String author = getAnnotationProperty(doc, "author");
            if (Strings.isNotBlank(author)) {
                summary.setAuthor(author);
            }
        }
        return summary;
    }

    String getAnnotationProperty(AnnotationExpr annotationExpr, String key) {
        ResolvedAnnotationDeclaration resolve = annotationExpr.resolve();

        List<Node> childNodes = annotationExpr.getChildNodes();
        for (Node n : childNodes) {
            if (n instanceof MemberValuePair) {
                MemberValuePair memberValuePair = (MemberValuePair) n;
                if (memberValuePair.getName().toString().equals(key)) {
                    return memberValuePair.getValue().toString();
                }
            }
        }
        return "";
    }

    /**
     * 判断注解集合中是否包含 某个注解
     *
     * @param annotations
     * @param type
     * @return
     */
    boolean hasController(NodeList<AnnotationExpr> annotations, Type type) {
        if (annotations.isEmpty()) {
            return false;
        }
        for (int index = 0; index < annotations.size(); index++) {
            AnnotationExpr annotationExpr = annotations.get(index);
            try {
                ResolvedAnnotationDeclaration resolve = annotationExpr.resolve();
                if (resolve.getQualifiedName().equals(type.getTypeName())) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("unable resolve " + annotationExpr.getName());
            }
        }
        return false;
    }

    /**
     * 注解集合中查询某个注解
     *
     * @param annotations
     * @param type
     * @return
     */
    private AnnotationExpr findAnnotation(NodeList<AnnotationExpr> annotations, Type type) {
        if (annotations.isEmpty()) {
            return null;
        }
        for (int index = 0; index < annotations.size(); index++) {
            AnnotationExpr annotationExpr = annotations.get(index);
            try {
                ResolvedAnnotationDeclaration resolve = annotationExpr.resolve();
                if (resolve.getQualifiedName().equals(type.getTypeName())) {
                    return annotationExpr;
                }
            } catch (Exception e) {
                System.out.println("unable resolve " + annotationExpr.getName());
            }
        }
        return null;
    }
}
