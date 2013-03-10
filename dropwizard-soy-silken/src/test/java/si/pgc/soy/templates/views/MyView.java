package si.pgc.soy.templates.views;

import com.google.common.collect.ImmutableMap;

public class MyView extends SoyView {

    private final String name;

    public MyView(String name) {
        super("example.simple.helloName", ImmutableMap.of("name", name));
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
