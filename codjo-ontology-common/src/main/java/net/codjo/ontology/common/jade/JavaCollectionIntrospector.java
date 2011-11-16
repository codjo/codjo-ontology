package net.codjo.ontology.common.jade;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsHelper;
import jade.content.abs.AbsObject;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.ReflectiveIntrospector;
import jade.content.schema.ObjectSchema;
import java.util.ArrayList;
import java.util.List;
/**
 * Convertit les objects suivant l'ontologie (pour les SEQUENCE les collections de types java sont utilisées à
 * la place de jade.util.leap.Collection).
 */
public class JavaCollectionIntrospector extends ReflectiveIntrospector {

    @Override
    protected boolean isAggregateObject(Object slotValue) {
        return slotValue instanceof List;
    }


    @Override
    protected void externaliseAndSetAggregateSlot(AbsObject abs,
                                                  ObjectSchema schema,
                                                  String slotName,
                                                  Object slotValue,
                                                  ObjectSchema slotSchema,
                                                  Ontology referenceOnto) throws OntologyException {
        List list = (List)slotValue;
        if (!list.isEmpty() || schema.isMandatory(slotName)) {
            AbsObject absSlotValue = AbsHelper.externaliseList(list, referenceOnto, slotSchema.getTypeName());
            AbsHelper.setAttribute(abs, slotName, absSlotValue);
        }
    }


    @Override
    protected Object internaliseAggregateSlot(AbsAggregate aggregate, Ontology onto)
          throws OntologyException {
        List ret = new ArrayList();

        for (int i = 0; i < aggregate.size(); i++) {
            Object element = onto.toObject(aggregate.get(i));
            // Check if the element is a Term, a primitive an AID or a List
            Ontology.checkIsTerm(element);
            //noinspection unchecked
            ret.add(element);
        }

        return ret;
    }
}
