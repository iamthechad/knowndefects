/*******************************************************************************
 * Copyright (c) 2011 Megatome Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.megatome.knowndefect.findbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.bcel.BCELUtil;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Map;

/**
 * Abstract base class for KnownDefect and KnownAndAcceptedDefect detectors
 *
 * @author cjohnston
 */
public abstract class AbstractAnnotationDetector extends AnnotationDetector {

    protected final BugReporter bugReporter;
    private String currentClass;
    private Method currentMethod;

    /**
     * Create an instance of this detector
     *
     * @param bugReporter BugReporter
     */
    public AbstractAnnotationDetector(final BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(final JavaClass someObj) {
        this.currentClass = someObj.getClassName();
        super.visit(someObj);
    }

    @Override
    public void visit(final Method method) {
        this.currentMethod = method;
        super.visit(method);
    }

    @Override
    public void visitAnnotation(String annotationClass,
                                Map<String, Object> map, boolean runtimeVisible) {
        super.visitAnnotation(annotationClass, map, runtimeVisible);
        if (!getAnnotationClass().equals(annotationClass)) {
            return;
        }
        final MethodDescriptor md = new MethodDescriptor(this.currentClass,
                this.currentMethod.getName(),
                this.currentMethod.getSignature(), this.currentMethod
                        .isStatic());
        final BugInstance bi = new BugInstance(this, getBugType(), NORMAL_PRIORITY)
                .addClassAndMethod(md);
        this.bugReporter.reportBug(bi);
    }

    @Override
    public void visitClassContext(final ClassContext classContext) {
        final JavaClass javaClass = classContext.getJavaClass();
        if (!BCELUtil.preTiger(javaClass))
            javaClass.accept(this);
    }

    @Override
    public void report() {
        // Not sure if this is necessary...
        this.currentMethod = null;
    }

    /**
     * Get the annotation class to look for
     *
     * @return Fully qualified class name
     */
    public abstract String getAnnotationClass();

    /**
     * Get the bug type to use when reporting. Defined in messages.xml
     *
     * @return Bug Type
     */
    public abstract String getBugType();
}