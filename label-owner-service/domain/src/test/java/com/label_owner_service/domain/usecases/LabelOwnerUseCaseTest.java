package com.label_owner_service.domain.usecases;

import com.label_owner_service.domain.models.InitialOwner;
import com.label_owner_service.domain.models.LabeledOwner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelOwnerUseCaseTest {

  @Test
  public void addsHomelessLabel() {
    InitialOwner initialOwner = new InitialOwner(0L, "Zbyszek", null, "phone", "email");
    List<LabeledOwner> labeledOwners = new ArrayList<>();
    LabelOwnerUseCase useCase = new LabelOwnerUseCase(labeledOwners::add);

    useCase.labelAndSendOwner(initialOwner);

    assertThat(labeledOwners).containsExactly(
      new LabeledOwner(0L, "Zbyszek", null, "phone", "email", Set.of("HOMELESS"))
    );
  }

  @Test
  public void addsCountryLabel() {
    InitialOwner initialOwner = new InitialOwner(0L, "Zbyszek", "address", "+48phone", "email");
    List<LabeledOwner> labeledOwners = new ArrayList<>();
    LabelOwnerUseCase useCase = new LabelOwnerUseCase(labeledOwners::add);

    useCase.labelAndSendOwner(initialOwner);

    assertThat(labeledOwners).containsExactly(
      new LabeledOwner(0L, "Zbyszek", "address", "+48phone", "email", Set.of("POLAND"))
    );
  }

  @Test
  public void addsOldFashionedLabel() {
    InitialOwner initialOwner = new InitialOwner(0L, "Zbyszek", "address", "phone", null);
    List<LabeledOwner> labeledOwners = new ArrayList<>();
    LabelOwnerUseCase useCase = new LabelOwnerUseCase(labeledOwners::add);

    useCase.labelAndSendOwner(initialOwner);

    assertThat(labeledOwners).containsExactly(
      new LabeledOwner(0L, "Zbyszek", "address", "phone", null, Set.of("OLD_FASHIONED"))
    );
  }

  @Test
  public void addsAllLabels() {
    InitialOwner initialOwner = new InitialOwner(0L, "Zbyszek", null, "+48phone", null);
    List<LabeledOwner> labeledOwners = new ArrayList<>();
    LabelOwnerUseCase useCase = new LabelOwnerUseCase(labeledOwners::add);

    useCase.labelAndSendOwner(initialOwner);

    assertThat(labeledOwners).containsExactly(new LabeledOwner(
      0L,
      "Zbyszek",
      null,
      "+48phone",
      null,
      Set.of("OLD_FASHIONED", "HOMELESS", "POLAND")
      )
    );
  }

}
