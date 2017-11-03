package org.particleframework.inject.failures

import org.particleframework.context.BeanContext
import org.particleframework.context.DefaultBeanContext
import org.particleframework.context.exceptions.CircularDependencyException
import spock.lang.Specification

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by graemerocher on 16/05/2017.
 */
class ConstructorCircularDependencyFailureSpec extends Specification {

    void "test simple constructor circular dependency failure"() {
        given:
        BeanContext context = new DefaultBeanContext()
        context.start()

        when:"A bean is obtained that has a setter with @Inject"
        B b =  context.getBean(B)

        then:"The implementation is injected"
        def e = thrown(CircularDependencyException)
        e.message == '''\
Failed to inject value for field [a] of class: org.particleframework.inject.failures.ConstructorCircularDependencyFailureSpec$B

Message: Circular dependency detected
Path Taken: 
B.a --> new A([C c]) --> new C([B b])
^                                  |
|                                  |
|                                  |
+----------------------------------+'''
    }

    static class C {
        @Inject C( B b ) {}
    }
    @Singleton
    static class A {
        A(C c) {}
    }

    @Singleton
    static class B {
        @Inject protected A a
    }
}

