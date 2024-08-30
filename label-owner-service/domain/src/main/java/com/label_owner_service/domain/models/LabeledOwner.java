package com.label_owner_service.domain.models;

import java.util.Set;

public record LabeledOwner(Long id, String name, String address, String phone, String email, Set<String> labels) {
}
