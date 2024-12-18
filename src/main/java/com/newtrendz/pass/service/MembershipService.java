package com.newtrendz.pass.service;

import com.newtrendz.pass.entity.Membership;
import com.newtrendz.pass.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    public List<Membership> getAllMemberships() {
        return membershipRepository.findAll();
    }

    public Optional<Membership> getMembershipById(String id) {
        return membershipRepository.findById(id);
    }

    public Membership addMembership(Membership membership) {
        return membershipRepository.save(membership);
    }

    public Membership editMembership(String id, Membership updatedMembership) {
        Optional<Membership> existingMembershipOpt = membershipRepository.findById(id);
        if (existingMembershipOpt.isPresent()) {
            Membership existingMembership = existingMembershipOpt.get();
            existingMembership.setName(updatedMembership.getName());
            existingMembership.setDescription(updatedMembership.getDescription());
            existingMembership.setOffers(updatedMembership.getOffers());
            existingMembership.setQrCode(updatedMembership.getQrCode());
            return membershipRepository.save(existingMembership);
        }
        return null;
    }

    public boolean deleteMembership(String id) {
        if (membershipRepository.existsById(id)) {
            membershipRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
