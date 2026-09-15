package com.eurofarma.euroforma.auth.dto;

import com.eurofarma.euroforma.usuario.Role;

public record LoginResponse(
        String token,
        Long id,
        String nome,
        String email,
        Role role
) {
}
