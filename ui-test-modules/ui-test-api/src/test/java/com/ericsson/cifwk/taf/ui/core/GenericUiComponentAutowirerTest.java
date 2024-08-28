package com.ericsson.cifwk.taf.ui.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.UiComponentContainer;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.taf.ui.spi.UiComponentStateManager;
import com.ericsson.cifwk.taf.ui.spi.UiMediator;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;

import javassist.util.proxy.ProxyFactory;

public class GenericUiComponentAutowirerTest {

    private UiMediator mediator = mock(UiMediator.class);
    private UiComponentStateManagerFactory stateManagerFactory;
    private GenericUiComponentAutowirer unit;
    private UiComponentStateManager stateManager;

    @Before
    public void setUp() {
        stateManager = mock(UiComponentStateManager.class);
        stateManagerFactory = mock(UiComponentStateManagerFactory.class);
        unit = new GenericUiComponentAutowirer(stateManagerFactory);
        when(stateManagerFactory.createStateManager(any(UiComponentMappingDetails.class))).thenReturn(stateManager);
        when(stateManagerFactory.getAutowirer()).thenReturn(unit);
    }

    @Test
    public void getFields() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        MySubComponentOrView componentContainer = new MySubComponentOrView();
        componentContainer = createJavaassistProxy(componentContainer);
        List<Field> fields = Arrays.asList(unit.getFields(componentContainer));

        // filtering artificial fields out is required
        // (e.g. to eliminate augmented code by Eclemma)
        Collection<Field> realFields = Collections2.filter(fields, new Predicate<Field>() {
            @Override
            public boolean apply(Field field) {
                return !field.getName().endsWith("jacocoData");
            }
        });

        // making sure found fields belong to target object
        Field expectedField1 = MySubComponentOrView.class.getDeclaredField("anotherLabel");
        Field expectedField2 = MySubComponentOrView.class.getDeclaredField("anotherLabels");
        Field expectedField3 = MyComponentOrView.class.getDeclaredField("firstLabel");
        Field expectedField4 = MyComponentOrView.class.getDeclaredField("allLabels");

        assertThat(realFields).containsExactlyInAnyOrder(expectedField1, expectedField2, expectedField3, expectedField4);
    }

    @Test
    public void initializeViewModel() {
        stateManagerFactory = new GenericUiComponentStateManagerFactory(mediator);
        unit = new GenericUiComponentAutowirer(stateManagerFactory);
        List<UiComponentStateManager> stateManagers = getStateManagers();
        when(mediator.retrieve(any(UiComponentMappingDetails.class))).thenReturn(stateManagers);
        // Need to use this constructor in order to prevent loop in unit initialization
        TestViewModel view = new TestViewModel(stateManagerFactory);

        unit.initialize(view);

        assertTrue(view.getErrorMessagesLabel().exists());
        assertTrue(view.getLoginFormLabel().exists());
        assertTrue(view.getOkButton().exists());
        assertTrue(view.getPasswordBox().exists());
        when(mediator.retrieve(any(UiComponentMappingDetails.class))).thenThrow(UiComponentNotFoundException.class);
        assertFalse(view.getUsernameBox().exists());
    }

    @Test
    public void canMapValue() throws Exception {
        assertFalse(canMapValue("staticLoginFormLabel"));
        assertFalse(canMapValue("usualField"));
        assertFalse(canMapValue("stringMapping"));
        assertTrue(canMapValue("loginFormLabel"));
        assertTrue(canMapValue("manyComponents"));
        assertFalse(canMapValue("manyValues"));
        assertFalse(canMapValue("manyLongValues"));
        assertTrue(canMapValue("manyLabels"));
        assertFalse(canMapValue("abstractComponent"));
        assertFalse(canMapValue("manyAbstractComponents"));
    }

    private boolean canMapValue(String fieldName) {
        try {
            Field field = getModelField(TestWrongViewModel.class, fieldName);
            return unit.canMapValue(field);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getModelField(Class<? extends ViewModel> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

    @Test
    public void setFieldValue() throws Exception {
        TestViewModel view = new TestViewModel(stateManagerFactory);
        Field field = getModelField(TestViewModel.class, "loginFormLabel");
        final UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        assertNull(view.getLoginFormLabel());
        UiComponent value = unit.instantiateComponent(field, new Supplier<UiComponentStateManager>() {
            @Override
            public UiComponentStateManager get() {
                return stateManager;
            }
        });
        unit.setFieldValue(view, field, value);
        Assert.assertNotNull(view.getLoginFormLabel());
    }

    @Test
    public void setFieldValueAsList() throws Exception {
        TestViewModel view = new TestViewModel(stateManagerFactory);

        // List<UiComponent>
        Field field = getModelField(TestViewModel.class, "manyComponents");
        List<UiComponentStateManager> stateManagers = getStateManagers();
        assertNull(view.getLoginFormLabel());
        List<UiComponent> value = unit.instantiateComponents(field, getStateManagerSupplier(stateManagers), false);
        unit.setFieldValue(view, field, value);
        List<UiComponent> manyComponents = view.getManyComponents();
        Assert.assertNotNull(manyComponents);
        for (UiComponent component : manyComponents) {
            component.click();
        }

        // List<TextBox>
        field = TestViewModel.class.getDeclaredField("manyTextBoxes");
        assertNull(view.getManyTextBoxes());
        value = unit.instantiateComponents(field, getStateManagerSupplier(stateManagers), false);
        unit.setFieldValue(view, field, value);
        List<TextBox> manyTextBoxes = view.getManyTextBoxes();
        Assert.assertNotNull(manyTextBoxes);
        for (TextBox textBox : manyTextBoxes) {
            textBox.click();
        }
    }

    @Test
    public void isUiComponentTypeOk() {
        assertTrue(unit.isUiComponentTypeOk(UiComponent.class));
        assertTrue(unit.isUiComponentTypeOk(TextBox.class));
        assertTrue(unit.isUiComponentTypeOk(TextBoxImpl.class));
        assertFalse(unit.isUiComponentTypeOk(AbstractUiComponent.class));
        assertFalse(unit.isUiComponentTypeOk(String.class));
    }

    @Test
    public void getCollectionComponentType() throws Exception {
        assertEquals(UiComponent.class, unit.getCollectionUiComponentType(getModelField(TestWrongViewModel.class, "manyComponents")));
        assertEquals(Label.class, unit.getCollectionUiComponentType(getModelField(TestWrongViewModel.class, "manyLabels")));
        assertEquals(AbstractUiComponent.class, unit.getCollectionUiComponentType(getModelField(TestWrongViewModel.class, "manyAbstractComponents")));
    }

    @Test
    public void isFieldTypeOk() throws Exception {
        assertTrue(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "staticLoginFormLabel")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "usualField")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "stringMapping")));
        assertTrue(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "loginFormLabel")));
        assertTrue(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "manyComponents")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "manyValues")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "manyLongValues")));
        assertTrue(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "manyLabels")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "abstractComponent")));
        assertFalse(unit.isFieldTypeOk(getModelField(TestWrongViewModel.class, "manyAbstractComponents")));
    }

    @Test
    public void canFieldAcceptMultiple() throws Exception {
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "staticLoginFormLabel")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "usualField")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "stringMapping")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "loginFormLabel")));
        assertTrue(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "manyComponents")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "manyValues")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "manyLongValues")));
        assertTrue(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "manyLabels")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "abstractComponent")));
        assertFalse(unit.canFieldAcceptMultiple(getModelField(TestWrongViewModel.class, "manyAbstractComponents")));
    }

    @Test
    public void isMappingDefined() throws Exception {
        assertTrue(unit.isMappingDefined(getModelField(TestWrongViewModel.class, "staticLoginFormLabel")));
        assertFalse(unit.isMappingDefined(getModelField(TestWrongViewModel.class, "usualField")));
    }

    @Test
    public void areFieldModifiersOk() throws Exception {
        assertFalse(unit.areFieldModifiersOk(getModelField(TestWrongViewModel.class, "staticLoginFormLabel")));
        assertFalse(unit.areFieldModifiersOk(getModelField(TestWrongViewModel.class, "finalComponentList")));
        assertTrue(unit.areFieldModifiersOk(getModelField(TestWrongViewModel.class, "rawComponent")));
        assertTrue(unit.areFieldModifiersOk(getModelField(TestWrongViewModel.class, "stringMapping")));
    }

    private Supplier<List<UiComponentStateManager>> getStateManagerSupplier(final List<UiComponentStateManager> stateManagers) {
        return new Supplier<List<UiComponentStateManager>>() {
            @Override
            public List<UiComponentStateManager> get() {
                return stateManagers;
            }
        };
    }

    private List<UiComponentStateManager> getStateManagers() {
        UiComponentStateManager stateManager = mock(UiComponentStateManager.class);
        List<UiComponentStateManager> stateManagers = new ArrayList<>();
        stateManagers.add(stateManager);
        return stateManagers;
    }

    protected static class MyComponentOrView implements UiComponentContainer {

        @UiComponentMapping(".label")
        private Label firstLabel;

        @UiComponentMapping(".label")
        private List<Label> allLabels;

    }

    protected static class MySubComponentOrView extends MyComponentOrView {

        @UiComponentMapping(".extraLabel")
        private Label anotherLabel;

        @UiComponentMapping(".extraLabel")
        private List<Label> anotherLabels;

    }

    protected static class MySubSubComponentOrView extends MySubComponentOrView {

        @UiComponentMapping(".extraLabel2")
        private Label anotherLabel2;

        @UiComponentMapping(".extraLabel2")
        private List<Label> anotherLabels2;

    }

    @SuppressWarnings("unchecked")
    private <T> T createJavaassistProxy(final T instance) throws IllegalAccessException, InstantiationException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(instance.getClass());
        Class clazz = factory.createClass();
        return (T) clazz.newInstance();
    }

    @Test
    public void testGetAllFields() {
        assertEquals(unit.getAllFields(MySubSubComponentOrView.class).length,
                        MyComponentOrView.class.getDeclaredFields().length
                        + MySubComponentOrView.class.getDeclaredFields().length
                        + MySubSubComponentOrView.class.getDeclaredFields().length);
    }
}
