package com.udeajobs.identity.account_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enumeración que define los tipos de roles disponibles para los usuarios en UdeAJobs.
 *
 * Esta enumeración determina el tipo de usuario y sus permisos dentro del sistema,
 * distinguiendo entre usuarios que buscan trabajo y usuarios que ofrecen trabajo.
 *
 * @author UdeAJobs Team
 * @version 1.0
 * @since 1.0
 */
public enum ROLE {

    /** Usuario freelancer que busca proyectos y trabajos */
    FREELANCER,

    /** Usuario empleador que publica proyectos y contrata freelancers */
    EMPLOYER;

    /**
     * Crea un ROLE a partir de un valor String, manejando la conversión de manera segura.
     *
     * @param value el valor string a convertir (no sensible a mayúsculas/minúsculas)
     * @return el ROLE correspondiente al valor proporcionado
     * @throws IllegalArgumentException si el valor no corresponde a ningún ROLE válido
     */
    @JsonCreator
    public static ROLE fromString(String value) {
        try {
            return ROLE.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + value);
        }
    }
}
