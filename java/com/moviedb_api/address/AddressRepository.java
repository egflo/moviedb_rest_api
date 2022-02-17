package com.moviedb_api.address;
import com.moviedb_api.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("AddressRepository")
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findById(Integer id);


    Page<Address> findAllByFirstname(String firstName, Pageable page);

    Page<Address> findAllByLastname(String lastName, Pageable page);

    Page<Address> findByStreetContaining(String address, Pageable page);

    Page<Address> findByState(String state, Pageable page);

    Page<Address> findByCity(String city, Pageable page);

    Page<Address> findByPostcode(String postcode, Pageable page);

    Page<Address> findAll(Pageable page);

    Boolean existsAddressById(Integer id);
}