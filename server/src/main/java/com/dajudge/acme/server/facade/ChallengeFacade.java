package com.dajudge.acme.server.facade;

import com.dajudge.acme.server.model.Account;
import com.dajudge.acme.server.model.AuthorizationChallenge;
import com.dajudge.acme.server.model.AuthorizationRequest;
import com.dajudge.acme.server.model.Order;
import com.dajudge.acme.server.repository.CentralRepository;
import com.dajudge.acme.server.transport.ChallengeStatusCTO;
import com.dajudge.acme.server.transport.ChallengeStatusEnum;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;

import static com.dajudge.acme.server.facade.mapper.AuthorizationChallengeMapper.toTransportObject;

@Dependent
public class ChallengeFacade {
    private final CentralRepository centralRepository;

    @Inject
    public ChallengeFacade(final CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    public ChallengeStatusCTO getChallengeStatus(final String challengeId) {
        final AuthorizationRequest request = centralRepository.getAccounts().values().stream()
                .map(Account::getOrders).flatMap(Collection::stream)
                .map(Order::getIdentifiers).flatMap(Collection::stream)
                .filter(hasChallengeWithId(challengeId))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No such challenge: " + challengeId));
        final AuthorizationChallenge challenge = request.getChallenges().stream()
                .filter(it -> it.getId().equals(challengeId))
                .findAny()
                .get();
        challenge.setStatus(ChallengeStatusEnum.VALID);
        challenge.setValidated(new Date());
        return new ChallengeStatusCTO(toTransportObject(challenge), request.getId());
    }

    private Predicate<? super AuthorizationRequest> hasChallengeWithId(final String challengeId) {
        return it -> it.getChallenges().stream().anyMatch(c -> c.getId().equals(challengeId));
    }
}
