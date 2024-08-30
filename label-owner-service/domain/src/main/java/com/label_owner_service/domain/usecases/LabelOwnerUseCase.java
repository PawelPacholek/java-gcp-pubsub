package com.label_owner_service.domain.usecases;

import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import com.label_owner_service.domain.models.InitialOwner;
import com.label_owner_service.domain.models.LabeledOwner;

import java.util.HashSet;
import java.util.Set;

public class LabelOwnerUseCase {

    private final LabeledOwnerSender labeledOwnerSender;

    public LabelOwnerUseCase(LabeledOwnerSender initialOwnerSender) {
        this.labeledOwnerSender = initialOwnerSender;
    }

    public void labelAndSendOwner(InitialOwner initialOwner) {
        LabeledOwner labeledOwner = label(initialOwner);
        labeledOwnerSender.send(labeledOwner);
    }

    private static LabeledOwner label(InitialOwner initialOwner) {
        return new LabeledOwner(
                initialOwner.id(),
                initialOwner.name(),
                initialOwner.address(),
                initialOwner.phone(),
                initialOwner.email(),
                labels(initialOwner)
        );
    }

    private static Set<String> labels(InitialOwner initialOwner) {
        Set<String> labels = new HashSet<>();
        if (initialOwner.address() == null || initialOwner.address().isBlank())
            labels.add("HOMELESS");
        if (initialOwner.phone() != null && initialOwner.phone().startsWith("+48"))
            labels.add("POLAND");
        if (initialOwner.email() == null || initialOwner.email().isBlank())
            labels.add("OLD_FASHIONED");
        return labels;
    }

}
