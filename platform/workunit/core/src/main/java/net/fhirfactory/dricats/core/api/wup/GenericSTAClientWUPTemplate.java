package net.fhirfactory.dricats.core.api.wup;

import net.fhirfactory.dricats.core.api.wup.common.GenericSTAWUPTemplate;
import net.fhirfactory.dricats.core.model.wup.valuesets.WUPArchetypeEnum;

public abstract class GenericSTAClientWUPTemplate extends GenericSTAWUPTemplate {

    public GenericSTAClientWUPTemplate() {
        super();
    }

    @Override
    protected WUPArchetypeEnum specifyWUPArchetype() {
        return (WUPArchetypeEnum.WUP_NATURE_API_CLIENT);
    }
}
