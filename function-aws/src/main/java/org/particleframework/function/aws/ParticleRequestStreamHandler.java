/*
 * Copyright 2017 original authors
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
 */
package org.particleframework.function.aws;

import com.amazonaws.services.lambda.runtime.*;
import org.particleframework.context.ApplicationContext;
import org.particleframework.function.executor.StreamFunctionExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>An implementation of the {@link RequestStreamHandler} for Particle</p>
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public class ParticleRequestStreamHandler extends StreamFunctionExecutor<Context> implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        execute(input, output, context);
    }

    @Override
    protected ApplicationContext buildApplicationContext(Context context) {
        ApplicationContext applicationContext = super.buildApplicationContext(context);
        registerContextBeans(context, applicationContext);
        return applicationContext;
    }

    private void registerContextBeans(Context context, ApplicationContext applicationContext) {
        applicationContext.registerSingleton(context);
        LambdaLogger logger = context.getLogger();
        if(logger != null) {
            applicationContext.registerSingleton(logger);
        }
        ClientContext clientContext = context.getClientContext();
        if(clientContext != null) {
            applicationContext.registerSingleton(clientContext);
        }
        CognitoIdentity identity = context.getIdentity();
        if(identity != null) {
            applicationContext.registerSingleton(identity);
        }
    }

}
