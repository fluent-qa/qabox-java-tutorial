package org.jctools.queues.atomic;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

/**
 * This generator takes in an JCTools 'ArrayQueue' Java source file and patches {@link sun.misc.Unsafe} accesses into
 * atomic {@link java.util.concurrent.atomic.AtomicLongFieldUpdater}. It outputs a Java source file with these patches.
 * <p>
 * An 'ArrayQueue' is one that is backed by a circular array and use a <code>producerLimit</code> and a
 * <code>consumerLimit</code> field to track the positions of each.
 */
public final class JavaParsingAtomicArrayQueueGenerator extends JavaParsingAtomicQueueGenerator {

    public static void main(String[] args) throws Exception {
        main(JavaParsingAtomicArrayQueueGenerator.class, args);
    }

    JavaParsingAtomicArrayQueueGenerator(String sourceFileName) {
        super(sourceFileName);
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        super.visit(n, arg);
        // Update the ctor to match the class name
        n.setName(translateQueueName(n.getNameAsString()));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration node, Void arg) {
        super.visit(node, arg);

        replaceParentClassesForAtomics(node);

        node.setName(translateQueueName(node.getNameAsString()));

        if (isCommentPresent(node, GEN_DIRECTIVE_CLASS_CONTAINS_ORDERED_FIELD_ACCESSORS)) {
            node.setComment(null);
            removeStaticFieldsAndInitialisers(node);
            patchAtomicFieldUpdaterAccessorMethods(node);
        }

        for (MethodDeclaration method : node.getMethods()) {
            if (isCommentPresent(method, GEN_DIRECTIVE_METHOD_IGNORE)) {
                method.remove();
            }
        }

        if (!node.getMethodsByName("failFastOffer").isEmpty()) {
            MethodDeclaration deprecatedMethodRedirect = node.addMethod("weakOffer", Keyword.PUBLIC);
            patchMethodAsDeprecatedRedirector(deprecatedMethodRedirect, "failFastOffer", PrimitiveType.intType(),
                    new Parameter(classType("E"), "e"));
        }

        node.setJavadocComment(formatMultilineJavadoc(0,
                "NOTE: This class was automatically generated by "
                        + JavaParsingAtomicArrayQueueGenerator.class.getName(),
                "which can found in the jctools-build module. The original source file is " + sourceFileName + ".")
                + node.getJavadocComment().orElse(new JavadocComment("")).getContent());
    }

    String fieldUpdaterFieldName(String fieldName) {
        switch (fieldName) {
        case "producerIndex":
            return "P_INDEX_UPDATER";
        case "consumerIndex":
            return "C_INDEX_UPDATER";
        case "producerLimit":
            return "P_LIMIT_UPDATER";
        default:
            throw new IllegalArgumentException("Unhandled field: " + fieldName);
        }
    }

    /**
     * Given a variable declaration of some sort, check it's name and type and
     * if it looks like any of the key type changes between unsafe and atomic
     * queues, perform the conversion to change it's type.
     */
    void processSpecialNodeTypes(NodeWithType<?, Type> node, String name) {
        Type type = node.getType();
        if ("buffer".equals(name) && isRefArray(type, "E")) {
            node.setType(atomicRefArrayType((ArrayType) type));
        } else if ("sBuffer".equals(name) && isLongArray(type)) {
            node.setType(atomicLongArrayType());
        } else if (PrimitiveType.longType().equals(type)) {
            switch(name) {
            case "mask":
            case "offset":
            case "seqOffset":
            case "lookAheadSeqOffset":
            case "lookAheadElementOffset":
                node.setType(PrimitiveType.intType());
            }
        }
    }

    /**
     * Given a method declaration node this method will replace it's code and
     * signature with code to redirect all calls to it to the
     * <code>newMethodName</code>. Method signatures of both methods must match
     * exactly.
     */
    @SuppressWarnings("SameParameterValue")
    private void patchMethodAsDeprecatedRedirector(MethodDeclaration methodToPatch, String toMethodName,
            Type returnType, Parameter... parameters) {
        methodToPatch.setType(returnType);
        for (Parameter parameter : parameters) {
            methodToPatch.addParameter(parameter);
        }
        methodToPatch.addAnnotation(new MarkerAnnotationExpr("Deprecated"));

        methodToPatch.setJavadocComment(
                formatMultilineJavadoc(1, "@deprecated This was renamed to " + toMethodName + " please migrate"));

        MethodCallExpr methodCall = methodCallExpr("this", toMethodName);
        for (Parameter parameter : parameters) {
            methodCall.addArgument(new NameExpr(parameter.getName()));
        }

        BlockStmt body = new BlockStmt();
        body.addStatement(new ReturnStmt(methodCall));
        methodToPatch.setBody(body);
    }

    /**
     * For each method accessor to a field, add in the calls necessary to
     * AtomicFieldUpdaters. Only methods start with so/cas/sv/lv/lp followed by
     * the field name are processed. Clearly <code>lv<code>, <code>lp<code> and
     * <code>sv<code> are simple field accesses with only <code>so and <code>cas
     * <code> using the AtomicFieldUpdaters.
     *
     * @param n the AST node for the containing class
     */
    private void patchAtomicFieldUpdaterAccessorMethods(ClassOrInterfaceDeclaration n) {
        String className = n.getNameAsString();

        for (FieldDeclaration field : n.getFields()) {
            if (field.getModifiers().contains(Modifier.staticModifier())) {
                // Ignore statics
                continue;
            }

            boolean usesFieldUpdater = false;
            for (VariableDeclarator variable : field.getVariables()) {
                String variableName = variable.getNameAsString();
                String methodNameSuffix = capitalise(variableName);

                for (MethodDeclaration method : n.getMethods()) {
                    usesFieldUpdater |= patchAtomicFieldUpdaterAccessorMethod(variableName, method, methodNameSuffix);
                }

                if (usesFieldUpdater) {
                    n.getMembers().add(0, declareLongFieldUpdater(className, variableName));
                }
            }

            if (usesFieldUpdater) {
                field.addModifier(Keyword.VOLATILE);
            }
        }
    }

    private boolean isLongArray(Type in) {
        if (in instanceof ArrayType) {
            ArrayType aType = (ArrayType) in;
            return PrimitiveType.longType().equals(aType.getComponentType());
        }
        return false;
    }

    private ClassOrInterfaceType atomicRefArrayType(ArrayType in) {
        ClassOrInterfaceType out = new ClassOrInterfaceType(null, "AtomicReferenceArray");
        out.setTypeArguments(in.getComponentType());
        return out;
    }

    private ClassOrInterfaceType atomicLongArrayType() {
        return new ClassOrInterfaceType(null, "AtomicLongArray");
    }

}