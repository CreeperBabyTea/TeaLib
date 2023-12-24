package creeperbabytea.tealib.common.objects;

import creeperbabytea.tealib.common.registry.TeaGeneralRegister;

public abstract class AbstractRegistrableEntry<RO extends AbstractRegistrableEntry<RO>> {
    private final String name;
    private final String group;

    public AbstractRegistrableEntry(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public boolean hasCustomRegisterBehavior() {
        return false;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public abstract RO register(TeaGeneralRegister register);
}
