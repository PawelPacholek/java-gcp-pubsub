package com.main_owner_service.persistence;

import com.main_owner_service.domain.models.LabeledOwner;
import com.main_owner_service.domain.ports.OwnerGateway;
import org.springframework.data.redis.core.RedisTemplate;

import static com.main_owner_service.persistence.DataClassSerialization.deserialize;
import static com.main_owner_service.persistence.DataClassSerialization.serialize;

public class OwnerGatewayImp implements OwnerGateway {

  private final RedisTemplate<Long, String> redisClient;

  public OwnerGatewayImp(RedisTemplate<Long, String> redisClient) {
    this.redisClient = redisClient;
  }

  @Override
    public void save(LabeledOwner labeledOwner) {
      redisClient.opsForValue().set(labeledOwner.id(), serialize(labeledOwner));
    }

    @Override
    public LabeledOwner fetch(Long id) {
      String raw = redisClient.opsForValue().get(id);
      return raw == null ? null : deserialize(raw, LabeledOwner.class);
    }

}
