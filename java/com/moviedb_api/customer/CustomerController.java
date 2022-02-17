package com.moviedb_api.customer;


import com.moviedb_api.address.AddressRepository;
import com.moviedb_api.sale.Sale;
import com.moviedb_api.security.JwtTokenUtil;
import com.moviedb_api.user_address.User_Address;
import com.moviedb_api.user_address.User_AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@Controller // This means that this class is a Controller
@RequestMapping(path="/customer") // This means URL's start with /demo (after Application path)
public class CustomerController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private CustomerRepository customerRepository;

    @Autowired
    private JwtTokenUtil authenticationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private User_AddressRepository user_addressRepository;

    @GetMapping("/")
    public ResponseEntity<?> getUser(@RequestHeader HttpHeaders headers)
    {
        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        CustomerRequest request = new CustomerRequest();
        request.setId(Integer.parseInt(userId));

        return customerService.getCustomer(request);
    }

    @PostMapping("/primary")
    @ResponseBody
    public ResponseEntity<?> updateUserPrimary(
            @RequestHeader HttpHeaders headers,
            @RequestBody CustomerRequest request) {

        System.out.println(request.getPrimaryAddress());
        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setId(Integer.parseInt(userId));

        return customerService.updatePrimary(request);
    }

    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity<?> updateUserEmail(
            @RequestHeader HttpHeaders headers,
            @RequestBody EmailRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setId(Integer.parseInt(userId));

        return customerService.updateEmail(request);
    }


    @PostMapping("/password")
    @ResponseBody
    public ResponseEntity<?> updateCustomerPassword(
            @RequestHeader HttpHeaders headers,
            @RequestBody PasswordRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setId(Integer.parseInt(userId));

        return customerService.updatePassword(request);
    }

    /**
     *
     *    ADMIN METHODS
     * **/
    @GetMapping(path="/search/{search}")
    public ResponseEntity<?> getAllSearch(
            @PathVariable String search,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> orderBy,
            @RequestParam Optional<String> sortBy
    ) {


            // This returns a JSON or XML with the movies
            return customerService.search(search,
                    PageRequest.of(
                            page.orElse(0),
                            limit.orElse(5),
                            orderBy.orElse(1) == 1 ? Sort.Direction.DESC : Sort.Direction.ASC,
                            sortBy.orElse("created")
                    )
            );

    }


    @GetMapping(path="/metadata")
    public @ResponseBody
    ResponseEntity<?> getMetaData(
    ) {

        return customerService.generateMetaData();
    }


    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<?> getAllUsers(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> orderBy,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return customerService.getAll(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        orderBy.orElse(1) == 1 ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortBy.orElse("created")
                )
        );
    }

    @GetMapping("/firstname/{fname}")
    public ResponseEntity<?> getCustomerByFirstName(
            @PathVariable(value = "fname") String fname,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return customerService.findByFirstName(fname,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/lastname/{lname}")
    public ResponseEntity<?> getCustomerByLastName(
            @PathVariable(value = "lname") String lname,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return customerService.findByLastName(lname,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable(value = "id") Integer id)
    {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setId(id);
        return customerService.getCustomer(customerRequest);
    }

    @PostMapping("/{id}")
    public @ResponseBody ResponseEntity<?> updateCustomerById(
            @PathVariable(value = "id") Integer id, @RequestBody CustomerRequest customerRequest)
    {
        return customerService.updateCustomer(customerRequest);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<?> deleteCustomerById(
            @PathVariable(value = "id") Integer id)
    {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setId(id);

        return customerService.deleteCustomer(customerRequest);
    }

}