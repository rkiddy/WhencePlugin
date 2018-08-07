package org.ganymede.minecraft.whence;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(PluginDescriptionFile.class)
public class WhencePluginTest {

    private PluginDescriptionFile description;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {

        description = Mockito.mock(PluginDescriptionFile.class, "getDescription");

        //description = Mockito.mock(PluginDescriptionFile.class);
        Mockito.when(description.getName()).thenReturn("WhencePlugin");
        Mockito.when(description.getVersion()).thenReturn("0.0");
        Mockito.when(description.getMain()).thenReturn("org.ganymede.minecraft.whence.WhencePlugin");
    }

    @Test
    public void testFirst() {
        System.out.println("I am a test!");
        WhencePlugin test = Mockito.mock(WhencePlugin.class);
        Mockito.when(test.getDescription()).thenReturn(description);
        System.out.println("I have an object here: " + test);
    }
}
