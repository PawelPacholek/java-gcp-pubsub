package com.main_owner_service.persistence;

import com.main_owner_service.domain.models.LabeledOwner;
import com.main_owner_service.domain.ports.OwnerGateway;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.main_owner_service.persistence.DataClassSerialization.deserialize;
import static com.main_owner_service.persistence.DataClassSerialization.serialize;

public class OwnerGatewayInMemoryImp implements OwnerGateway {

  private static final Map<Long, LabeledOwner> MEMORY_MAP = new ConcurrentHashMap<>();

    @Override
    public void save(LabeledOwner labeledOwner) {
      MEMORY_MAP.put(labeledOwner.id(), labeledOwner);
    }

    @Override
    public LabeledOwner fetch(Long id) {
      return MEMORY_MAP.get(id);
    }

}
