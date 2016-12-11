/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 * <p>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.ballerina.core.parser.visitor;

import org.wso2.ballerina.core.model.Annotation;
import org.wso2.ballerina.core.model.Resource;
import org.wso2.ballerina.core.model.VariableDcl;
import org.wso2.ballerina.core.model.statements.Statement;
import org.wso2.ballerina.core.parser.BallerinaBaseVisitor;
import org.wso2.ballerina.core.parser.BallerinaParser;

/**
 * Visitor for resource
 */
public class ResourceVisitor extends BallerinaBaseVisitor {

    /**
     * {@inheritDoc}
     * <p>
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     *
     * @param ctx
     */
    @Override
    public Object visitResourceDefinition(BallerinaParser.ResourceDefinitionContext ctx) {
        Resource resourceObject = new Resource(ctx.Identifier().getText());

        AnnotationVisitor annotationVisitor = new AnnotationVisitor();
        for (BallerinaParser.AnnotationContext annotationContext : ctx.annotation()) {
            resourceObject.addAnnotation((Annotation) annotationContext.accept(annotationVisitor));
        }

        VariableDeclarationVisitor variableDeclarationVisitor = new VariableDeclarationVisitor();
        for (BallerinaParser.VariableDeclarationContext variableDeclarationContext :
                ctx.functionBody().variableDeclaration()) {
            resourceObject.addVariable((VariableDcl) variableDeclarationContext.accept(variableDeclarationVisitor));
        }

        StatementVisitor statementVisitor = new StatementVisitor();
        for (int i = 0; i < ctx.functionBody().statement().size(); i++) {
            resourceObject.addStatement((Statement) (ctx.functionBody().statement(i).
                    getChild(0).accept(statementVisitor)));
        }

        return resourceObject;
    }
}