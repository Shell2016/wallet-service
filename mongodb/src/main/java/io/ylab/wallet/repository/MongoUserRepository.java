package io.ylab.wallet.repository;

import io.ylab.wallet.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends MongoRepository<UserDocument, Long> {
}
