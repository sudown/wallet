package br.com.wallet.wallet.infrastructure.persistence;

import br.com.wallet.wallet.infrastructure.entities.WalletSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletSummaryRepository extends JpaRepository<WalletSummaryEntity, UUID> {
}