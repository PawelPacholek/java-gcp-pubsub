package com.label_owner_service.domain.helpers;

import com.label_owner_service.domain.models.LabeledOwner;

public interface LabeledOwnerSender {

    void send(LabeledOwner initialOwner);

}
