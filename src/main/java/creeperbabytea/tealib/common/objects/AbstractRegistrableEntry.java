package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.registry.GeneralRegister;

public abstract class AbstractRegistrableEntry<RO extends AbstractRegistrableEntry<?>> {
    private String name;
    private String group;

    public AbstractRegistrableEntry(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public abstract RO register(GeneralRegister register);

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }
}
