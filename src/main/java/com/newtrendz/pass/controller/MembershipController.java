package com.newtrendz.pass.controller;

import com.newtrendz.pass.entity.Membership;
import com.newtrendz.pass.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public List<Membership> getAllMemberships() {
        return membershipService.getAllMemberships();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membership> getMembershipById(@PathVariable String id) {
        Optional<Membership> membership = membershipService.getMembershipById(id);
        return membership.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Membership addMembership(@RequestBody Membership membership) {
        return membershipService.addMembership(membership);
    }

    @PutMapping("/edit/{id}")  // Changed to PUT mapping
    public ResponseEntity<Membership> editMembership(@PathVariable String id, @RequestBody Membership membership) {
        Membership updatedMembership = membershipService.editMembership(id, membership);
        return updatedMembership != null ? ResponseEntity.ok(updatedMembership) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")  // New endpoint to delete memberships
    public ResponseEntity<Void> deleteMembership(@PathVariable String id) {
        if (membershipService.deleteMembership(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
