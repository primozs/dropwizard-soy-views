package si.pgc.soytmplt.tests;

import org.junit.Test;
import si.pgc.soy.templates.views.MyView;
import static org.fest.assertions.api.Assertions.assertThat;

public class ViewTest {

    private final MyView view = new MyView("primoz");

    @Test
    public void hasTemplate() throws Exception {
        assertThat(view.getTemplateName()).isEqualTo("example.simple.helloName");
    }
}
