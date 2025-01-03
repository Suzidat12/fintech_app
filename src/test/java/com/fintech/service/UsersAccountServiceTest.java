package com.fintech.service;

import com.fintech.dto.ResponseDto;
import com.fintech.dto.request.UserAccountRequest;
import com.fintech.exception.BadRequestException;
import com.fintech.model.UsersAccount;
import com.fintech.model.enums.AppStatus;
import com.fintech.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UsersAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersAccountService userAccountService;

    private UserAccountRequest userAccountRequest;
    private UsersAccount usersAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userAccountRequest = new UserAccountRequest();
        userAccountRequest.setEmail("user@example.com");
        userAccountRequest.setPassword("password123");
        userAccountRequest.setFirstName("John");
        userAccountRequest.setLastName("Doe");
        userAccountRequest.setGender("Male");
        userAccountRequest.setAddress("123 Main St");
        userAccountRequest.setDateOfBirth(String.valueOf(LocalDateTime.now()));
        userAccountRequest.setBvn("1234567890");

        // Setting up a mock user account
        usersAccount = new UsersAccount();
        usersAccount.setId(1L);
        usersAccount.setEmail("olasunkanmi@gmail.com");
        usersAccount.setFullName("John Doe");
        usersAccount.setCreatedDate(LocalDateTime.now());
        usersAccount.setAccountStatus(AppStatus.PENDING);
    }

    @Test
    void create_SuccessfulAccountCreation() {
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        ResponseEntity<ResponseDto<UsersAccount>> response = userAccountService.create(userAccountRequest);
        verify(userAccountRepository, times(1)).save(any(UsersAccount.class));
        assertNotNull(response);
        assertEquals("User created successfully", response.getBody().getStatusMessage());
        assertEquals("user@example.com", response.getBody().getData().getEmail());
        assertEquals("encodedPassword", response.getBody().getData().getPassword());  // Ensure password is encoded
    }

    @Test
    void create_AccountAlreadyExists() {
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(usersAccount));
        Exception exception = assertThrows(BadRequestException.class, () -> {
            userAccountService.create(userAccountRequest);
        });
        assertEquals("Account already exist", exception.getMessage());
    }

    @Test
    void update_SuccessfulAccountUpdate() {
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(usersAccount));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ResponseEntity<ResponseDto<UsersAccount>> response = userAccountService.update(userAccountRequest, 1L);

        verify(userAccountRepository, times(1)).save(any(UsersAccount.class));
        assertNotNull(response);
        assertEquals("User updated successfully", response.getBody().getStatusMessage());
        assertEquals("encodedPassword", response.getBody().getData().getPassword());  // Ensure password is encoded
    }

    @Test
    void update_AccountDoesNotExist() {
        // Mocking repository to return empty for non-existing user
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            userAccountService.update(userAccountRequest, 1L);
        });

        assertEquals("Account does not exist", exception.getMessage());
    }

    @Test
    void delete_SuccessfulAccountDeletion() {
        // Mocking repository to return an existing user account
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(usersAccount));

        ResponseEntity<ResponseDto<String>> response = userAccountService.delete(1L);

        verify(userAccountRepository, times(1)).deleteById(anyLong());
        assertNotNull(response);
        assertEquals("User account deleted successfully", response.getBody().getStatusMessage());
    }

    @Test
    void delete_AccountDoesNotExist() {
        // Mocking repository to return empty for non-existing user
        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            userAccountService.delete(1L);
        });

        assertEquals("Account does not exist", exception.getMessage());
    }

    @Test
    void retrieve_SuccessfulAccountRetrieval() {
        when(userAccountRepository.findAll()).thenReturn(Arrays.asList(usersAccount));
        ResponseEntity<ResponseDto<List<UsersAccount>>> response = userAccountService.retrieve();
        assertNotNull(response);
        assertEquals("Users data retrieve successfully", response.getBody().getStatusMessage());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void retrieve_EmptyAccountList() {
        when(userAccountRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<ResponseDto<List<UsersAccount>>> response = userAccountService.retrieve();
        assertNotNull(response);
        assertEquals("Users data retrieve successfully", response.getBody().getStatusMessage());
        assertTrue(response.getBody().getData().isEmpty());
    }
}