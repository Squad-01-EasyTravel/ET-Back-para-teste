package com.decolatech.easytravel.domain.user.controller;

import com.decolatech.easytravel.domain.user.dto.UserDTO;
import com.decolatech.easytravel.domain.user.enums.UserRole;
import com.decolatech.easytravel.domain.user.enums.UserStatus;
import com.decolatech.easytravel.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO sampleUserDTO;

    @BeforeEach
    void setUp() {
        sampleUserDTO = new UserDTO();
        sampleUserDTO.setId(1);
        sampleUserDTO.setUsername("testuser");
        sampleUserDTO.setEmail("test@example.com");
        sampleUserDTO.setCpf("12345678901");
        sampleUserDTO.setPassport("AB123456");
        sampleUserDTO.setTelephone("11999999999");
        sampleUserDTO.setUserRole(UserRole.USER);
        sampleUserDTO.setUserStatus(UserStatus.ACTIVATED);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        // Teste para buscar todos os usuários
        List<UserDTO> users = List.of(sampleUserDTO);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].userRole").value(0)) // USER = 0
                .andExpect(jsonPath("$[0].userStatus").value(1)); // ACTIVATED = 1

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByIdFound() throws Exception {
        // Teste para buscar usuário por ID existente
        when(userService.getUserById(1)).thenReturn(Optional.of(sampleUserDTO));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserByIdNotFound() throws Exception {
        // Teste para buscar usuário por ID inexistente
        when(userService.getUserById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserSuccess() throws Exception {
        // Teste para criar novo usuário com sucesso
        UserDTO inputDTO = new UserDTO();
        inputDTO.setUsername("newuser");
        inputDTO.setEmail("newuser@example.com");
        inputDTO.setCpf("98765432100");
        inputDTO.setPassport("XY987654");
        inputDTO.setTelephone("11888888888");
        inputDTO.setUserPassword("password123");
        inputDTO.setUserRole(UserRole.USER);
        inputDTO.setUserStatus(UserStatus.ACTIVATED);

        UserDTO createdDTO = new UserDTO();
        createdDTO.setId(2);
        createdDTO.setUsername("newuser");
        createdDTO.setEmail("newuser@example.com");
        createdDTO.setCpf("98765432100");
        createdDTO.setPassport("XY987654");
        createdDTO.setTelephone("11888888888");
        createdDTO.setUserRole(UserRole.USER);
        createdDTO.setUserStatus(UserStatus.ACTIVATED);

        when(userService.createUser(any(UserDTO.class))).thenReturn(createdDTO);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.userRole").value(0));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateUserValidationError() throws Exception {
        // Teste para criar usuário com dados inválidos
        UserDTO invalidDTO = new UserDTO();
        // Campos obrigatórios não preenchidos

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUserSuccess() throws Exception {
        // Teste para atualizar usuário com sucesso
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername("updateduser");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setTelephone("11777777777");
        updateDTO.setUserStatus(UserStatus.DEACTIVATED);

        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1);
        updatedDTO.setUsername("updateduser");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setCpf("12345678901");
        updatedDTO.setPassport("AB123456");
        updatedDTO.setTelephone("11777777777");
        updatedDTO.setUserRole(UserRole.USER);
        updatedDTO.setUserStatus(UserStatus.DEACTIVATED);

        when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.userStatus").value(0)); // DEACTIVATED = 0

        verify(userService, times(1)).updateUser(eq(1), any(UserDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserSuccess() throws Exception {
        // Teste para deletar usuário com sucesso
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsersByRole() throws Exception {
        // Teste para buscar usuários por role
        List<UserDTO> employees = List.of(sampleUserDTO);
        when(userService.getUsersByRole(UserRole.EMPLOYEE)).thenReturn(employees);

        mockMvc.perform(get("/api/users/role/EMPLOYEE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getUsersByRole(UserRole.EMPLOYEE);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsersByStatus() throws Exception {
        // Teste para buscar usuários por status
        List<UserDTO> activeUsers = List.of(sampleUserDTO);
        when(userService.getUsersByStatus(UserStatus.ACTIVATED)).thenReturn(activeUsers);

        mockMvc.perform(get("/api/users/status/ACTIVATED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).getUsersByStatus(UserStatus.ACTIVATED);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUnauthorizedAccess() throws Exception {
        // Teste de acesso não autorizado (usuário comum tentando acessar admin)
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testServiceException() throws Exception {
        // Teste quando o service lança exceção
        when(userService.createUser(any(UserDTO.class)))
                .thenThrow(new RuntimeException("Erro interno"));

        UserDTO inputDTO = new UserDTO();
        inputDTO.setUsername("testuser");
        inputDTO.setEmail("test@example.com");
        inputDTO.setCpf("12345678901");
        inputDTO.setPassport("AB123456");
        inputDTO.setTelephone("11999999999");
        inputDTO.setUserPassword("password123");
        inputDTO.setUserRole(UserRole.USER);
        inputDTO.setUserStatus(UserStatus.ACTIVATED);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }
}
