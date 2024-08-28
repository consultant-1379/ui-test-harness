package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WidgetMethodInterceptorTest {

    @Mock
    private RemoteInvoker invoker;

    private WidgetMethodInterceptor interceptor;

    private ViewModel model;

    @Before
    public void setUp() {
        interceptor = new WidgetMethodInterceptor(new WidgetProxyFactory(invoker), invoker, "widgetId");
        model = createCglibProxyForClass(ViewModel.class, interceptor);
    }

    @Test
    public void shouldGetReturnType() throws Throwable {
        assertType(model, TextBox.class);
        assertType(model, Label.class);
        assertType(model, CheckBox.class);

        mockReturnType(List.class);
        assertType(List.class, model.getViewComponents("selector", UiComponent.class));
        assertType(Boolean.class, model.isCurrentView());
    }

    private void assertType(ViewModel model, Class componentClass) throws Throwable {
        mockReturnType(componentClass);
        UiComponent component = model.getViewComponent("selector", componentClass);
        assertType(componentClass, component);
    }

    private void assertType(Class expectedComponentClass, Object component) {
        String actualClassName = component.getClass().getName();
        String expectedClassName = expectedComponentClass.getName();
        assertTrue(String.format("Actual '%s' doesn't contain expected '%s'", actualClassName, expectedClassName), actualClassName.contains(expectedClassName));
    }

    private void mockReturnType(Class componentClass) throws Throwable {
        when(invoker.invoke(anyString(), any(Method.class), any(Object[].class))).
                thenReturn("{type = '" + componentClass.getName() + "', value = 'widgetId'}");
    }

    @Test
    public void interceptWithBooleanResult() throws Throwable {
        RemoteInvoker invoker = mock(RemoteInvoker.class);
        when(invoker.invoke(anyString(), any(Method.class), any(Object[].class))).thenReturn("{value = true}");
        TextBox textBox =
                createCglibProxyForClass(TextBox.class, new WidgetMethodInterceptor(null, invoker, null));
        assertEquals(Boolean.TRUE, textBox.isSelected());
    }

    @SuppressWarnings("unchecked")
    private <T> T createCglibProxyForClass(Class<T> clazz, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setUseFactory(true);
        enhancer.setCallbackType(interceptor.getClass());
        if (clazz.getSigners() != null) {
            enhancer.setNamingPolicy(NAMING_POLICY_FOR_CLASSES_IN_SIGNED_PACKAGES);
        }
        Class<?> proxyClass = enhancer.createClass();

        // instantiating class without constructor call
        ObjenesisStd objenesis = new ObjenesisStd();
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[]{interceptor});
        return (T) proxy;
    }

    private static final NamingPolicy NAMING_POLICY_FOR_CLASSES_IN_SIGNED_PACKAGES = new DefaultNamingPolicy() {
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }
    };

}